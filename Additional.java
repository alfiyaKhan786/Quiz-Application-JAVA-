import java.util.*;
public class Additional {
    static class Question{
        String questionText;
        List<String> options;
        String answer;
        String difficulty;
        public Question(String questionText,List<String> options,String answer,String difficulty){
            this.questionText=questionText;
            this.answer=answer;
            this.difficulty=difficulty;
        }
    }
    static class Quiz{
        private List<Question> questions;
        private int score;
        private int timePerQuestion;
        private Scanner scanner;
        public Quiz(List<Question> questions,int timePerQuestion){
            this.questions=questions;
            this.timePerQuestion=timePerQuestion;
            this.score=0;
            scanner =new Scanner(System.in);

        }
        public void start(){
            Collections.shuffle(questions);
            System.out.println("Starting the quiz! You have " + timePerQuestion + " seconds per question.\n");
            int questionNumber=1;
            for(Question q:questions){
                System.out.println("Question " + questionNumber + ":");
               boolean answeredCorrectly=askQuestion(q);
               if(answeredCorrectly){
                score++;
               }
               questionNumber++;
               System.out.println();
            }
            System.out.println("Quiz complete! Your score: " + score + "/" + questions.size());

        }
        private boolean askQuestion(Question q){
            System.out.println(q.questionText);
            char optionLetter='A';
            Map<Character,String> optionMap=new HashMap<>();
            for(String option:q.options){
                optionMap.put(optionLetter,option);
                System.out.println(optionLetter + ": " + option);
                optionLetter++;
            }
            long startTime=System.currentTimeMillis();
            String userAnswer=null;
            while(true){
                System.out.print("Your answer (A, B, C, ...): ");
                while(!scanner.hasNextLine()){

                }
                userAnswer=scanner.nextLine().trim().toUpperCase();
                long elaspsedSeconds= (System.currentTimeMillis() - startTime) / 1000;
                if(elaspsedSeconds>timePerQuestion){
                    System.out.println("\nTime's up! Moving to next question.");
                    return false;
                }
                if(userAnswer.length()==1&&optionMap.containsKey(userAnswer.charAt(0))){
                    break;
                }
                else{
                    System.out.println("Invalid option. Please enter a valid letter (A, B, C, ...).");
                }
            }
            String selectedOption=optionMap.get(userAnswer.charAt(0));
            if(selectedOption.equalsIgnoreCase(q.answer)){
                System.out.println("Correct!");
                return true;
            }
            else{
                System.out.println("Wrong! Correct answer is: " + q.answer);
                return false;
            }
        }
    }
    public static void main(String[] args) {
        List<Question> allQuestions=loadQuestions();
        Scanner scanner=new Scanner(System.in);
        System.out.println("Welcome to the Java Quiz App!");
        System.out.println("Select difficulty level:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        int choice=0;
        while(true){
            System.out.print("Enter choice (1-3): ");
            String input = scanner.nextLine();
            
            try{
                choice=Integer.parseInt(input);
                if(choice>=1&&choice<=3)
                break;
                else
                    System.out.println("Please enter a number 1, 2, or 3.");
            }catch(NumberFormatException e){
                System.out.println("Invalid input. Enter a number 1, 2, or 3.");
            }
        }
        String chosenDifficulty="";
        switch(choice){
            case 1:
            chosenDifficulty="easy";
            break;
            case 2:
            chosenDifficulty="medium";
            break;
            case 3:
            chosenDifficulty="hard";
            break;
        }
        List<Question> filteredQuestions=new ArrayList<>();
        for(Question q:allQuestions){
            if (q.difficulty.equalsIgnoreCase(chosenDifficulty)) {
                filteredQuestions.add(q);
            }
        }
        if(filteredQuestions.isEmpty()){
            System.out.println("No questions available for the chosen difficulty level.");
            System.out.println("Exiting.");
            return;
        }
        int timeLimitSeconds=20;
        System.out.println("\nYou have " + timeLimitSeconds + " seconds to answer each question.");
        System.out.println("Good luck!\n");
        Quiz quiz=new Quiz(filteredQuestions,timeLimitSeconds);
        quiz.start();

    }
    private static List<Question> loadQuestions(){
        List<Question> questions=new ArrayList<>();
        questions.add(new Question("What is the capital of France?",
                Arrays.asList("Berlin", "Madrid", "Paris", "London"),
                "Paris",
                "easy"));
                questions.add(new Question("What color do you get when you mix red and white?",
                Arrays.asList("Pink", "Purple", "Orange", "Green"),
                "Pink",
                "easy"));
         questions.add(new Question(
                "What is 2 + 2?",
                Arrays.asList("3", "4", "5", "6"),
                "4",
                "easy"));
                questions.add(new Question(
                "Which planet is known as the Red Planet?",
                Arrays.asList("Earth", "Venus", "Mars", "Jupiter"),
                "Mars",
                "medium"));
         questions.add(new Question(
                "What is the chemical symbol for gold?",
                Arrays.asList("Au", "Ag", "Pb", "Gd"),
                "Au",
                "medium"));
        questions.add(new Question(
                "Who wrote the play 'Romeo and Juliet'?",
                Arrays.asList("Charles Dickens", "William Shakespeare", "Mark Twain", "Leo Tolstoy"),
                "William Shakespeare",
                "medium"));
        questions.add(new Question(
                "What is the derivative of sin(x)?",
                Arrays.asList("cos(x)", "-cos(x)", "sin(x)", "-sin(x)"),
                "cos(x)",
                "hard"));
                questions.add(new Question(
                "In computer science, what does 'FIFO' stand for?",
                Arrays.asList("First In, First Out", "Fast Input, Fast Output", "First Input, First Output", "Fast In, Fast Out"),
                "First In, First Out",
                "hard"));
                questions.add(new Question(
                "Who developed the theory of general relativity?",
                Arrays.asList("Isaac Newton", "Albert Einstein", "Galileo Galilei", "Nikola Tesla"),
                "Albert Einstein",
                "hard"));
return questions;    
    }
}
