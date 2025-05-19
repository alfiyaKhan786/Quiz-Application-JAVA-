import java.util.Map;

public class UserScore {

    private final String userId;
    private final String username;
    private final Map<String, Integer> quizScores;

    public UserScore(String userId, String username, Map<String, Integer> quizScores) {
        this.userId = userId;
        this.username = username; 
        this.quizScores = quizScores;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Map<String, Integer> getQuizScores() { 
        return quizScores;
    }

    public int getTotalScore() {
        return quizScores.values().stream().mapToInt(Integer::intValue).sum();
    }

    public double getAverageScore() {
        if (quizScores.isEmpty()) {
            return 0.0;
        }
        return quizScores.values().stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    public Integer getScoreForQuiz(String quizName) { 
        return quizScores.getOrDefault(quizName, 0);
    }
}
