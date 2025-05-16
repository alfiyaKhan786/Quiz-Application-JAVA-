
import java.util.*;

public class QuizManagementDynamic {

    private static Scanner scanner = new Scanner(System.in);
    private static QuizManager quizManager = new QuizManager();

    public static void main(String[] args) {
        System.out.println("Welcome to the Dynamic Quiz Management System!");

        boolean running = true;
        while (running) {
            showMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createQuiz();
                    break;
                case "2":
                    editQuiz();
                    break;
                case "3":
                    deleteQuiz();
                    break;
                case "4":
                    listQuizzes();
                    break;
                case "5":
                    running = false;
                    System.out.println("Exiting. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 5.");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Create a new quiz");
        System.out.println("2. Edit an existing quiz");
        System.out.println("3. Delete a quiz");
        System.out.println("4. List all quizzes");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void createQuiz() {
        System.out.print("Enter a title for the new quiz: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("Quiz title cannot be empty.");
            return;
        }
        Quiz quiz = new Quiz(title);

        System.out.println("Add questions to the quiz (enter 'done' to stop adding questions).");
        while (true) {
            System.out.print("Enter question title (or 'done'): ");
            String questionTitle = scanner.nextLine().trim();
            if (questionTitle.equalsIgnoreCase("done")) {
                break;
            }
            if (questionTitle.isEmpty()) {
                System.out.println("Question title cannot be empty.");
                continue;
            }

            List<String> options = new ArrayList<>();
            System.out.println("Enter options for the question (minimum 2). Type 'done' when finished:");
            while (true) {
                System.out.print("Option " + (options.size() + 1) + ": ");
                String option = scanner.nextLine().trim();
                if (option.equalsIgnoreCase("done")) {
                    if (options.size() < 2) {
                        System.out.println("At least two options are required.");
                        continue;
                    } else {
                        break;
                    }
                }
                if (option.isEmpty()) {
                    System.out.println("Option cannot be empty.");
                    continue;
                }
                options.add(option);
            }

            List<Integer> correctAnswers = new ArrayList<>();
            System.out.println("Enter correct answer option numbers separated by commas (e.g., 1,3): ");
            System.out.println("Options are numbered starting at 1.");
            while (true) {
                System.out.print("Correct answer(s): ");
                String correctInput = scanner.nextLine().trim();
                if (correctInput.isEmpty()) {
                    System.out.println("Correct answers cannot be empty.");
                    continue;
                }
                String[] parts = correctInput.split(",");
                boolean valid = true;
                correctAnswers.clear();
                for (String p : parts) {
                    try {
                        int index = Integer.parseInt(p.trim());
                        if (index < 1 || index > options.size()) {
                            System.out.println("Answer option number " + index + " is out of range.");
                            valid = false;
                            break;
                        }
                        if (!correctAnswers.contains(index - 1)) {
                            correctAnswers.add(index - 1); // zero-based
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format: " + p);
                        valid = false;
                        break;
                    }
                }
                if (valid && !correctAnswers.isEmpty()) {
                    break;
                }
            }
            quiz.addQuestion(new Question(questionTitle, options, correctAnswers));
            System.out.println("Question added.");
        }

        if (quiz.getQuestions().isEmpty()) {
            System.out.println("No questions added, quiz creation aborted.");
        } else {
            quizManager.addQuiz(quiz);
            System.out.println("Quiz '" + quiz.getTitle() + "' created successfully!");
        }
    }

    private static void editQuiz() {
        if (quizManager.listQuizzes().isEmpty()) {
            System.out.println("No quizzes available to edit.");
            return;
        }
        listQuizzesBrief();

        System.out.print("Enter the number of the quiz to edit: ");
        int quizNumber = readIntInput(1, quizManager.listQuizzes().size());
        Quiz quiz = quizManager.listQuizzes().get(quizNumber - 1);

        boolean editing = true;
        while (editing) {
            showEditMenu(quiz);
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.print("Enter new title for quiz (current: " + quiz.getTitle() + "): ");
                    String newTitle = scanner.nextLine().trim();
                    if (!newTitle.isEmpty()) {
                        quiz.setTitle(newTitle);
                        System.out.println("Quiz title updated.");
                    } else {
                        System.out.println("Title not changed.");
                    }
                    break;
                case "2":
                    editQuestions(quiz);
                    break;
                case "3":
                    editing = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private static void showEditMenu(Quiz quiz) {
        System.out.println("\nEditing Quiz: " + quiz.getTitle());
        System.out.println("1. Change quiz title");
        System.out.println("2. Edit questions");
        System.out.println("3. Return to main menu");
        System.out.print("Enter your choice: ");
    }

    private static void editQuestions(Quiz quiz) {
        boolean managingQuestions = true;
        while (managingQuestions) {
            if (quiz.getQuestions().isEmpty()) {
                System.out.println("No questions in this quiz.");
                System.out.println("1. Add a new question");
                System.out.println("2. Return to quiz editing menu");
                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine().trim();
                if ("1".equals(choice)) {
                    addQuestionToQuiz(quiz);
                } else {
                    managingQuestions = false;
                }
                continue;
            }

            System.out.println("\nQuestions:");
            List<Question> questions = quiz.getQuestions();
            for (int i = 0; i < questions.size(); i++) {
                System.out.println((i + 1) + ". " + questions.get(i).getTitle());
            }
            System.out.println((questions.size() + 1) + ". Add a new question");
            System.out.println((questions.size() + 2) + ". Return to quiz editing menu");
            System.out.print("Enter your choice: ");

            int choice = readIntInput(1, questions.size() + 2);
            if (choice >= 1 && choice <= questions.size()) {
                editSingleQuestion(quiz, choice - 1);
            } else if (choice == questions.size() + 1) {
                addQuestionToQuiz(quiz);
            } else {
                managingQuestions = false;
            }
        }
    }

    private static void addQuestionToQuiz(Quiz quiz) {
        System.out.print("Enter question title: ");
        String questionTitle = scanner.nextLine().trim();
        if (questionTitle.isEmpty()) {
            System.out.println("Question title cannot be empty.");
            return;
        }

        List<String> options = new ArrayList<>();
        System.out.println("Enter options for the question (minimum 2). Type 'done' when finished:");
        while (true) {
            System.out.print("Option " + (options.size() + 1) + ": ");
            String option = scanner.nextLine().trim();
            if (option.equalsIgnoreCase("done")) {
                if (options.size() < 2) {
                    System.out.println("At least two options are required.");
                    continue;
                } else {
                    break;
                }
            }
            if (option.isEmpty()) {
                System.out.println("Option cannot be empty.");
                continue;
            }
            options.add(option);
        }

        List<Integer> correctAnswers = new ArrayList<>();
        System.out.println("Enter correct answer option numbers separated by commas (e.g., 1,3): ");
        System.out.println("Options are numbered starting at 1.");
        while (true) {
            System.out.print("Correct answer(s): ");
            String correctInput = scanner.nextLine().trim();
            if (correctInput.isEmpty()) {
                System.out.println("Correct answers cannot be empty.");
                continue;
            }
            String[] parts = correctInput.split(",");
            boolean valid = true;
            correctAnswers.clear();
            for (String p : parts) {
                try {
                    int index = Integer.parseInt(p.trim());
                    if (index < 1 || index > options.size()) {
                        System.out.println("Answer option number " + index + " is out of range.");
                        valid = false;
                        break;
                    }
                    if (!correctAnswers.contains(index - 1)) {
                        correctAnswers.add(index - 1); // zero-based
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + p);
                    valid = false;
                    break;
                }
            }
            if (valid && !correctAnswers.isEmpty()) {
                break;
            }
        }
        quiz.addQuestion(new Question(questionTitle, options, correctAnswers));
        System.out.println("Question added successfully.");
    }

    private static void editSingleQuestion(Quiz quiz, int questionIndex) {
        Question question = quiz.getQuestions().get(questionIndex);
        boolean editingQuestion = true;
        while (editingQuestion) {
            System.out.println("\nEditing question: " + question.getTitle());
            System.out.println("1. Change question title");
            System.out.println("2. Edit options");
            System.out.println("3. Edit correct answers");
            System.out.println("4. Delete question");
            System.out.println("5. Return to questions menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter new question title (current: " + question.getTitle() + "): ");
                    String newTitle = scanner.nextLine().trim();
                    if (!newTitle.isEmpty()) {
                        question.setTitle(newTitle);
                        System.out.println("Question title updated.");
                    } else {
                        System.out.println("Title not changed.");
                    }
                    break;
                case "2":
                    editQuestionOptions(question);
                    break;
                case "3":
                    editCorrectAnswers(question);
                    break;
                case "4":
                    quiz.removeQuestion(questionIndex);
                    System.out.println("Question deleted.");
                    editingQuestion = false;
                    break;
                case "5":
                    editingQuestion = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    private static void editQuestionOptions(Question question) {
        boolean editingOptions = true;
        while (editingOptions) {
            System.out.println("\nOptions:");
            List<String> options = question.getOptions();
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ". " + options.get(i));
            }
            System.out.println((options.size() + 1) + ". Add a new option");
            System.out.println((options.size() + 2) + ". Return to question menu");
            System.out.print("Enter your choice: ");

            int choice = readIntInput(1, options.size() + 2);
            if (choice >= 1 && choice <= options.size()) {
                // Edit existing option
                System.out.print("Enter new text for option " + choice + " (current: " + options.get(choice - 1) + "): ");
                String newOption = scanner.nextLine().trim();
                if (!newOption.isEmpty()) {
                    options.set(choice - 1, newOption);
                    System.out.println("Option updated.");
                } else {
                    System.out.println("Option not changed.");
                }
            } else if (choice == options.size() + 1) {
                // Add new option
                System.out.print("Enter new option text: ");
                String newOption = scanner.nextLine().trim();
                if (!newOption.isEmpty()) {
                    options.add(newOption);
                    System.out.println("Option added.");
                } else {
                    System.out.println("Option cannot be empty.");
                }
            } else {
                editingOptions = false;
            }

            // Ensure at least 2 options remain
            if (options.size() < 2) {
                System.out.println("A question must have at least 2 options. Please add more options.");
                editingOptions = true;
            }
        }
    }

    private static void editCorrectAnswers(Question question) {
        List<String> options = question.getOptions();
        System.out.println("\nCurrent options:");
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
        System.out.println("Current correct answers (option numbers): ");
        for (Integer ansIndex : question.getCorrectAnswersIndexes()) {
            System.out.print((ansIndex + 1) + " ");
        }
        System.out.println();

        System.out.println("Enter new correct answer option numbers separated by commas (e.g., 1,3): ");
        while (true) {
            System.out.print("Correct answer(s): ");
            String correctInput = scanner.nextLine().trim();
            if (correctInput.isEmpty()) {
                System.out.println("Correct answers cannot be empty.");
                continue;
            }
            String[] parts = correctInput.split(",");
            boolean valid = true;
            List<Integer> newCorrectAnswers = new ArrayList<>();
            for (String p : parts) {
                try {
                    int index = Integer.parseInt(p.trim());
                    if (index < 1 || index > options.size()) {
                        System.out.println("Answer option number " + index + " is out of range.");
                        valid = false;
                        break;
                    }
                    if (!newCorrectAnswers.contains(index - 1)) {
                        newCorrectAnswers.add(index - 1); // zero-based
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + p);
                    valid = false;
                    break;
                }
            }
            if (valid && !newCorrectAnswers.isEmpty()) {
                question.setCorrectAnswersIndexes(newCorrectAnswers);
                System.out.println("Correct answers updated.");
                break;
            }
        }
    }

    private static void deleteQuiz() {
        if(quizManager.listQuizzes().isEmpty()) {
            System.out.println("No quizzes available to delete.");
            return;
        }
        listQuizzesBrief();

        System.out.print("Enter the number of the quiz to delete: ");
        int quizNumber = readIntInput(1, quizManager.listQuizzes().size());
        Quiz quiz = quizManager.listQuizzes().get(quizNumber - 1);

        System.out.print("Are you sure you want to delete quiz '" + quiz.getTitle() + "'? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("yes") || confirm.equals("y")) {
            quizManager.deleteQuiz(quiz.getId());
            System.out.println("Quiz deleted.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void listQuizzes() {
        List<Quiz> quizzes = quizManager.listQuizzes();
        if (quizzes.isEmpty()) {
            System.out.println("No quizzes available.");
            return;
        }
        System.out.println("\nQuizzes:");
        for (int i = 0; i < quizzes.size(); i++) {
            Quiz quiz = quizzes.get(i);
            System.out.println((i + 1) + ". " + quiz.getTitle());
            List<Question> questions = quiz.getQuestions();
            for (int j = 0; j < questions.size(); j++) {
                Question q = questions.get(j);
                System.out.println("   Q" + (j + 1) + ": " + q.getTitle());
                List<String> options = q.getOptions();
                for (int k = 0; k < options.size(); k++) {
                    String prefix = q.getCorrectAnswersIndexes().contains(k) ? "*" : " ";
                    System.out.println("      " + prefix + "Option " + (k + 1) + ": " + options.get(k));
                }
            }
        }
    }

    private static void listQuizzesBrief() {
        List<Quiz> quizzes = quizManager.listQuizzes();
        System.out.println("\nAvailable Quizzes:");
        for (int i = 0; i < quizzes.size(); i++) {
            System.out.println((i + 1) + ". " + quizzes.get(i).getTitle());
        }
    }

    private static int readIntInput(int min, int max) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.print("Please enter a number between " + min + " and " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Please enter again: ");
            }
        }
    }

}

class QuizManager {
    private Map<UUID, Quiz> quizzes = new LinkedHashMap<>();

    public void addQuiz(Quiz quiz) {
        quizzes.put(quiz.getId(), quiz);
    }

    public void deleteQuiz(UUID id) {
        quizzes.remove(id);
    }

    public List<Quiz> listQuizzes() {
        return new ArrayList<>(quizzes.values());
    }
}

class Quiz {
    private UUID id;
    private String title;
    private List<Question> questions;

    public Quiz(String title) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.questions = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public void removeQuestion(int index) {
        if (index >=0 && index < questions.size()) {
            questions.remove(index);
        }
    }
}

class Question {
    private String title;
    private List<String> options;
    private List<Integer> correctAnswersIndexes;

    public Question(String title, List<String> options, List<Integer> correctAnswersIndexes) {
       this.title = title;
        this.options = new ArrayList<>(options);
        this.correctAnswersIndexes = new ArrayList<>(correctAnswersIndexes);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<Integer> getCorrectAnswersIndexes() {
        return correctAnswersIndexes;
    }

    public void setCorrectAnswersIndexes(List<Integer> correctIndexes) {
        this.correctAnswersIndexes = new ArrayList<>(correctIndexes);
    }
}

