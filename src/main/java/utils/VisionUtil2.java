package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.ColorInfo;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageProperties;
import com.google.cloud.vision.v1.LocalizedObjectAnnotation;
import com.google.cloud.vision.v1.Vertex;
import com.google.cloud.vision.v1.WebDetection;
import com.google.protobuf.ByteString;

public class VisionUtil2 {

    private static final Set<String> artKeywords = Set.of(
        "flower", "spring", "petal", "blossom", "grassland", "bridge",
        "sculpture", "architecture", "visual art", "landscape", "mountain", "nature",
        "natural environment", "plain", "outdoor", "cloud", "sky", "sunset", "sunrise",
        "scenery", "valley", "forest", "lake", "river", "tree", "dusk", "sunlight", "water",
        "glacier", "alps", "coast", "sea", "ocean", "beach", "tropics", "snow", "canyon",
        "photography", "lighting", "exposure", "symmetry", "focus", "color", "black and white",
        "contrast", "depth", "motion blur", "shutter", "framing", "lens", "reflections",
        "emotion", "mood", "expression", "mystery", "melancholy", "drama", "silence",
        "solitude", "storytelling", "introspection", "dessert"
    );

    private static final Set<String> penaltyLabels = Set.of(
        "selfie", "person", "people", "face", "finger", "hand", "eye", "skin", "smile",
        "gesture", "crowd", "portrait photography", "group", "individual", "man", "woman",
        "boy", "girl", "food", "dish", "meal", "snack", "plate", "cuisine", "drink",
        "coffee", "product", "bottle", "container", "package", "indoor", "room",
        "furniture", "appliance", "object", "text", "label", "screen", "monitor",
        "keyboard", "phone", "computer", "advertisement", "poster", "sign", "art",
        "graphics", "waste", "plastic"
    );

    private static final Set<String> artisticKeywords = Set.of(
        "photography", "fine art", "landscape photography", "conceptual art",
        "visual storytelling", "art", "artwork", "minimalism", "moody",
        "dramatic", "surrealism"
    );

    public static class VisionResult {
        public List<EntityAnnotation> labelAnnotations;
        public List<LocalizedObjectAnnotation> objectAnnotations;
        public ImageProperties imageProperties;
        public WebDetection webDetection;
    }

    public static VisionResult analyzeImage(String imagePath) throws IOException {
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            ByteString imgBytes = ByteString.readFrom(Files.newInputStream(Paths.get(imagePath)));
            Image img = Image.newBuilder().setContent(imgBytes).build();

            List<Feature> features = List.of(
                Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build(),
                Feature.newBuilder().setType(Feature.Type.IMAGE_PROPERTIES).build(),
                Feature.newBuilder().setType(Feature.Type.OBJECT_LOCALIZATION).build(),
                Feature.newBuilder().setType(Feature.Type.WEB_DETECTION).build()
            );

            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .setImage(img)
                .addAllFeatures(features)
                .build();

            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(List.of(request));
            AnnotateImageResponse res = response.getResponsesList().get(0);

            VisionResult result = new VisionResult();
            result.labelAnnotations = res.getLabelAnnotationsList();
            result.objectAnnotations = res.getLocalizedObjectAnnotationsList();
            result.imageProperties = res.getImagePropertiesAnnotation();
            result.webDetection = res.getWebDetection();
            return result;
        }
    }

    public static int calculateArtScore(List<EntityAnnotation> labels) {
        double score = 30;
        int artLabelCount = 0;
        int totalLabels = labels.size();

        for (EntityAnnotation label : labels) {
            String desc = label.getDescription().toLowerCase();
            float confidence = label.getScore();
            if (artKeywords.stream().anyMatch(desc::contains)) {
                score += confidence * 7;
                artLabelCount++;
            }
            if (penaltyLabels.stream().anyMatch(desc::contains)) {
                score -= confidence * 4;
            }
        }

        if (totalLabels > 0) {
            double artRatio = (double) artLabelCount / totalLabels;
            score += artRatio * 5;
        }

        return (int) Math.max(0, Math.min(score, 100));
    }

    public static int evaluateColorComposition(ImageProperties imageProperties) {
        if (imageProperties == null) return 0;
        List<ColorInfo> colors = imageProperties.getDominantColors().getColorsList();
        if (colors.isEmpty()) return 0;

        double totalSaturation = 0;
        int colorCount = 0;

        for (ColorInfo colorInfo : colors) {
            float red = colorInfo.getColor().getRed();
            float green = colorInfo.getColor().getGreen();
            float blue = colorInfo.getColor().getBlue();
            float max = Math.max(red, Math.max(green, blue));
            float min = Math.min(red, Math.min(green, blue));
            float saturation = (max - min) / (max + min + 1);
            totalSaturation += saturation;
            colorCount++;
        }

        double avgSaturation = totalSaturation / colorCount;
        if (avgSaturation < 0.1) return 5;
        else if (avgSaturation > 0.7) return 3;
        else return 0;
    }

    public static int evaluateObjectLayout(List<LocalizedObjectAnnotation> objects) {
        if (objects == null || objects.isEmpty()) return 0;
        int peopleCount = 0;
        int offCenterPeople = 0;

        for (LocalizedObjectAnnotation object : objects) {
            String name = object.getName().toLowerCase();
            if (name.contains("person") || name.contains("man") || name.contains("woman")) {
                peopleCount++;
                float centerX = 0f;
                for (Vertex v : object.getBoundingPoly().getVerticesList()) {
                    centerX += v.getX();
                }
                centerX /= object.getBoundingPoly().getVerticesCount();
                if (centerX < 0.4 || centerX > 0.6) {
                    offCenterPeople++;
                }
            }
        }

        if (peopleCount == 0) return 3;
        if (peopleCount <= 2 && offCenterPeople == peopleCount) return 2;
        return -3;
    }

    public static int analyzeWebKeywords(WebDetection webDetection) {
        if (webDetection == null) return 0;
        int score = 0;
        for (WebDetection.WebEntity entity : webDetection.getWebEntitiesList()) {
            String desc = entity.getDescription().toLowerCase();
            float conf = entity.getScore();
            for (String keyword : artisticKeywords) {
                if (desc.contains(keyword)) {
                    score += Math.min(5, conf * 5);
                }
            }
        }
        return score;
    }

    public static int evaluateArtisticScore(String imagePath) throws IOException {
        VisionResult result = analyzeImage(imagePath);
        int score = calculateArtScore(result.labelAnnotations);
        score += evaluateColorComposition(result.imageProperties);
        score += evaluateObjectLayout(result.objectAnnotations);
        score += analyzeWebKeywords(result.webDetection);
        return Math.min(100, Math.max(0, score));
    }

}
