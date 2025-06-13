package utils;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

public class VisionUtil {
    
    private static final Set<String> artKeywords = Set.of(
            "flower", "spring", "petal", "blossom", "grassland", "bridge",
            "sculpture", "architecture", "visual art", "landscape", "mountain", "nature", "natural environment", "natural landscape", "plain",
            "outdoor", "cloud", "sky", "sunset", "sunrise", "scenery", "valley", "forest", "lake", "river", "tree", "dusk",
            "water", "glacier", "alps", "coast", "sea", "ocean", "carribean", "beach", "tropics",
            "snow", "canyon", "photography", "lighting", "exposure", "symmetry",
            "focus", "color", "black and white", "contrast", "depth", "motion blur", "shutter", "framing", "lens", "reflections",
            "emotion", "mood", "expression", "mystery", "melancholy", "drama", "silence", "solitude", "storytelling", "introspection", "dessert"
        );
    private static final Set<String> penaltyLabels= Set.of(
            "selfie", "person", "people", "face", "finger", "hand", "eye", "skin", "smile", "gesture", "crowd",
            "portrait photography", "group", "individual", "man", "woman", "boy", "girl", "food", "dish", "meal",
            "snack", "plate", "cuisine", "drink", "coffee", "product", "bottle", "container", "package",
            "indoor", "room", "furniture", "appliance", "object", "text", "label", "screen", "monitor", "keyboard",
            "phone", "computer", "advertisement", "poster", "sign", "art", "graphcis", "waste", "plastic" 
        );
    private static boolean isArtLabel(String desc) {
        for (String keyword : artKeywords) {
            if (desc.contains(keyword)) return true;
        }
        return false;
    }

    private static boolean isPenaltyLabel(String desc) {
        for (String keyword : penaltyLabels) {
            if (desc.contains(keyword)) return true;
        }
        return false;
    }


    public static List<EntityAnnotation> detectLabels(String imagePath) throws Exception {
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            ByteString imgBytes = ByteString.readFrom(Files.newInputStream(Paths.get(imagePath)));

            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();

            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(List.of(request));
            AnnotateImageResponse res = response.getResponsesList().get(0);
            return res.getLabelAnnotationsList();
        }
    }
    
    public static int calculateArtScore(List<EntityAnnotation> labels) {
        double score = 20; 
        int artLabelCount = 0;
        int totalLabels = labels.size();

        for (EntityAnnotation label : labels) {
            String desc = label.getDescription().toLowerCase();
            float confidence = label.getScore();

            if (isArtLabel(desc)) {
                score += confidence * 10;
                artLabelCount++;
            }

            if (isPenaltyLabel(desc)) {
                score -= confidence * 5;
            }
        }

        if (totalLabels > 0) {
            double artRatio = (double) artLabelCount / totalLabels;
            score += artRatio * 10;
        }

        return (int) Math.max(0, Math.min(score, 100));
    }

}
