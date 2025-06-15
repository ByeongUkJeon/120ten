package utils;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
            "outdoor", "cloud", "sky", "sunset", "sunrise", "scenery", "valley", "forest", "lake", "river", "tree", "dusk", "Sunlight",
            "water", "glacier", "alps", "coast", "sea", "ocean", "carribean", "beach", "tropics",
            "snow", "canyon", "photography", "lighting", "exposure", "symmetry",
            "focus", "color", "black and white", "contrast", "depth", "motion blur", "shutter", "framing", "lens", "reflections",
            "emotion", "mood", "expression", "mystery", "melancholy", "drama", "silence", "solitude", "storytelling", "introspection", "dessert"
        );
    private static final Map<String, Double> artKeywordsMap = Map.ofEntries(
            Map.entry("landscape", 2.0), Map.entry("scenery", 2.0), Map.entry("sunset", 2.0), Map.entry("sunrise", 2.0),
            Map.entry("cloud", 1.5), Map.entry("sky", 1.5), Map.entry("tree", 1.5), Map.entry("grass", 1.0),
            Map.entry("field", 1.2), Map.entry("forest", 2.0), Map.entry("valley", 1.8), Map.entry("mountain", 2.2),
            Map.entry("water", 1.8), Map.entry("lake", 2.0), Map.entry("river", 1.8), Map.entry("sea", 2.0), Map.entry("ocean", 2.0),
            Map.entry("reflection", 2.0), Map.entry("symmetry", 2.5), Map.entry("silhouette", 2.5),
            Map.entry("lighting", 2.0), Map.entry("exposure", 2.0), Map.entry("contrast", 2.0), Map.entry("color", 1.5),
            Map.entry("black and white", 2.2), Map.entry("minimalism", 2.5), Map.entry("composition", 2.0),
            Map.entry("framing", 2.2), Map.entry("aesthetic", 2.5), Map.entry("fine art", 3.0),
            Map.entry("emotion", 2.2), Map.entry("mood", 2.0), Map.entry("melancholy", 2.0),
            Map.entry("solitude", 2.0), Map.entry("dreamlike", 2.5), Map.entry("mystery", 2.0),
            Map.entry("timeless", 2.0), Map.entry("peaceful", 1.8), Map.entry("pastoral", 2.0)
        );
    private static final Set<String> penaltyLabels= Set.of(
            "selfie", "person", "people", "face", "finger", "hand", "eye", "skin", "smile", "gesture", "crowd",
            "portrait photography", "group", "individual", "man", "woman", "boy", "girl", "food", "dish", "meal",
            "snack", "plate", "cuisine", "drink", "coffee", "product", "bottle", "container", "package",
            "indoor", "room", "furniture", "appliance", "object", "text", "label", "screen", "monitor", "keyboard",
            "phone", "computer", "advertisement", "poster", "sign", "art", "graphcis", "waste", "plastic" 
        );
    
    private static final Map<String, Double> penaltyLabelsMaps = Map.ofEntries(
            Map.entry("selfie", 2.5), Map.entry("person", 1.5), Map.entry("people", 1.5), Map.entry("group", 1.2),
            Map.entry("face", 2.0), Map.entry("finger", 1.5), Map.entry("hand", 1.0), Map.entry("eye", 1.0),
            Map.entry("smile", 1.0), Map.entry("crowd", 1.5), Map.entry("portrait", 1.5),
            Map.entry("food", 1.5), Map.entry("meal", 1.2), Map.entry("drink", 1.0), Map.entry("bottle", 1.0),
            Map.entry("advertisement", 2.0), Map.entry("poster", 2.0), Map.entry("product", 2.5),
            Map.entry("label", 1.5), Map.entry("monitor", 2.0), Map.entry("screen", 2.0), Map.entry("phone", 1.8),
            Map.entry("indoor", 1.2), Map.entry("room", 1.2), Map.entry("furniture", 1.2), Map.entry("appliance", 1.2),
            Map.entry("plastic", 1.5), Map.entry("trash", 2.0), Map.entry("waste", 2.0), Map.entry("clutter", 2.0)
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

    public static int calculateAdvancedArtScore(List<EntityAnnotation> labels) {
        double score = 20; // 기본 점수
        int artLabelCount = 0;
        int emotionalLabelCount = 0;
        Set<String> matchedCategories = new HashSet<>();
        Set<String> descSet = new HashSet<>();

        for (EntityAnnotation label : labels) {
            String desc = label.getDescription().toLowerCase();
            float confidence = label.getScore();
            descSet.add(desc);

            // 감정 키워드 (예: mystery, solitude 등)
            if (isEmotionalKeyword(desc)) {
                score += confidence * 8;
                emotionalLabelCount++;
            }

            // 예술성 라벨
            if (isArtLabel(desc)) {
                score += Math.pow(confidence, 1.3) * 10; // 곡선 가중치 적용
                artLabelCount++;
                matchedCategories.add(getArtCategory(desc));
            }

            // 패널티 라벨
            if (isPenaltyLabel(desc) && !isBalancedWithArt(desc, descSet)) {
                score -= Math.pow(confidence, 1.2) * 5;
            }
        }
        if (descSet.contains("sunset") && descSet.contains("mountain") && descSet.contains("reflection")) {
            score += 10;
        }

        if (matchedCategories.size() >= 4) {
            score += 5;
        } else if (matchedCategories.size() < 2) {
            score -= 5;
        }

        if (emotionalLabelCount > 2) {
            score += 5;
        }

        return (int) Math.max(0, Math.min(score, 100)); // 0~100 범위 제한

    }
    private static boolean isEmotionalKeyword(String desc) {
        return Set.of(
            "emotion", "mood", "expression", "mystery", "melancholy", "drama",
            "solitude", "storytelling", "introspection", "serenity"
        ).contains(desc);
    }

    private static boolean isBalancedWithArt(String desc, Set<String> allLabels) {
        return desc.equals("person") && allLabels.contains("silhouette") && allLabels.contains("sunset");
    }

    // 예술 키워드를 카테고리화해 중복 방지
    private static String getArtCategory(String keyword) {
        if (keyword.contains("mountain") || keyword.contains("valley")) return "landform";
        if (keyword.contains("sunset") || keyword.contains("sunrise")) return "light";
        if (keyword.contains("reflection") || keyword.contains("water")) return "water";
        if (keyword.contains("emotion") || keyword.contains("mood")) return "emotion";
        if (keyword.contains("forest") || keyword.contains("tree")) return "nature";
        return "other";
    }
}
