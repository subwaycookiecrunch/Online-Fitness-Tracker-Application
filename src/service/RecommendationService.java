package service;

public class RecommendationService {

    public String getRecommendation(String intensity) {
        switch (intensity.toLowerCase()) {
            case "high":
                return "Recommendation: Ensure you take a rest day tomorrow and hydrate well.";
            case "medium":
                return "Recommendation: Good job! Try increasing the duration by 5 minutes next time.";
            case "low":
                return "Recommendation: Consistency is key. Try to workout at least 3 times a week.";
            default:
                return "Recommendation: Keep moving and stay active!";
        }
    }
}
