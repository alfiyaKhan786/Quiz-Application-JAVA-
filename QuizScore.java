import java.util.ArrayList;
import java.util.List;
public class QuizScore {
    public static class Question{
        private String questionText;
        private String correctAnswer;
        public Question(String questionText,String correctAnswer){
            this.questionText=questionText;
            this.correctAnswer=correctAnswer;

        }
        public String getQuestionText(){
            return questionText;
        }
        public String getCorrectAnswer(){
            return correctAnswer;
        }
    }
    public static class Quiz{
        private String quizId;
        private List<Question> questions;
        public Quiz(String quizId,List<Question> questions){
            this.quizId=quizId;
            this.questions=questions;
        }
        public String getQuizId(){
            return quizId;
        }
        public List<Question> getQuestions(){
            return questions;
        }
    }
    public static class QuizAttempt{
        private String quizId;
        private int score;
        public QuizAttempt(String quizId,int score){
            this.quizId=quizId;
            this.score=score;
        }
        public String getQuizId(){
            return quizId;
        }
        public int getScore(){
            return score;
        }
    }
    public static class User{
        private String userId;
        private List<QuizAttempt> quizAttempts;
        public User(String userId){
            this.userId=userId;
            this.quizAttempts=new ArrayList<>();
        }
        public String getUserId(){
            return userId;
        }
        public void addQuizAttempt(QuizAttempt attempt){
            quizAttempts.add(attempt);
        }
        public List<QuizAttempt> gQuizAttempts(){
            return quizAttempts;
        }
    }
    public static class QuizService{
        public int calculateScore(Quiz quiz,List<String> userAnswers){
            int score=0;
            List<Question> questions=quiz.getQuestions();
for(int i=0;i<questions.size();i++){
    if(questions.get(i).getCorrectAnswer().equalsIgnoreCase(userAnswers.get(i))){
        score++;
    }
}
return score;
        }
        public void recordQuizAttempt(User user,Quiz quiz,List<String> userAnswers){
            int score=calculateScore(quiz, userAnswers);
            QuizAttempt attempt=new QuizAttempt(quiz.getQuizId(), score);
            user.addQuizAttempt(attempt);
            System.out.println("Score for quiz"+quiz.getQuizId()+":"+score+"/"+quiz.getQuestions().size());
        }
    }
    public static void main(String[] args) {
        Question q1=new Question("What is 2+2?","4");
        Question q2=new Question("What is the capital of France","Paris");
        Question q3=new Question("WHich planet is known as Red planet?","Mars");
        Quiz quiz=new Quiz("quiz1",List.of(q1,q2,q3));
        User user=new User("user1");
        List<String> userAnswewr1=List.of("4","Paris","Mars");
       QuizService quizService=new QuizService();
       quizService.recordQuizAttempt(user, quiz, userAnswewr1);
        List<String> userAnswer2 =List.of("3","Paris","Jupiter");
        quizService.recordQuizAttempt(user, quiz, userAnswer2);
System.out.println("\nUser '" + user.getUserId() + "' past quiz attempts:");
for(QuizAttempt attempt:user.gQuizAttempts()){
            System.out.println("Quiz ID: " + attempt.getQuizId() + ", Score: " + attempt.getScore());
        }

    }
}
