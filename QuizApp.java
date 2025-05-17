
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class QuizApp extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    private JPanel topicsSelectionPanel = new JPanel();
    private JPanel questionPanel = new JPanel(new BorderLayout());
    private JLabel questionLabel = new JLabel("", SwingConstants.CENTER);
    private JPanel optionsPanel = new JPanel();
    private JButton submitButton = new JButton("Submit Answer");
    private JLabel feedbackLabel = new JLabel("", SwingConstants.CENTER);
    private JButton nextButton = new JButton("Next question");
    private Map<String, List<Question>> quizzes = new HashMap<>();
    private List<Question> currentQuizQuestions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private ButtonGroup radioGroup = new ButtonGroup();
    private List<JCheckBox> checkBoxes = new ArrayList<>();
    private List<JRadioButton> radioButtons = new ArrayList<>();
    private boolean isMultiSelect;

    public QuizApp() {
        setTitle("Quiz Taking Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        createSampleQuizzes();
        setupTopicSelectionPanel();
        setupQuestionPanel();
        mainPanel.add(topicsSelectionPanel, "TopicSelection");
        mainPanel.add(questionPanel, "QuestionPanel");
        add(mainPanel);
        cardLayout.show(mainPanel, "TopicSelection");
    }

    private void createSampleQuizzes() {
        List<Question> mathQuiz = new ArrayList<>();
        mathQuiz.add(new Question("What is 2+2?", Arrays.asList("3", "4", "5", "6"), Arrays.asList(1), false));
        mathQuiz.add(new Question("Select prime numbers:",
                Arrays.asList("2", "4", "5", "9"),
                Arrays.asList(0, 2),
                true));
        List<Question> scienceQuiz = new ArrayList<>();
        scienceQuiz.add(new Question("What planet is known as the Red Planet?",
                Arrays.asList("Earth", "Mars", "Jupiter", "Venus"),
                Arrays.asList(1), false));
        scienceQuiz.add(new Question("Select states of matter.",
                Arrays.asList("Solid", "Liquid", "Gas", "Energy"),
                Arrays.asList(0, 1, 2),
                true));
        quizzes.put("Math", mathQuiz);
        quizzes.put("Science", scienceQuiz);
    }

    private void setupTopicSelectionPanel() {
        topicsSelectionPanel.setLayout(new BorderLayout());
        JLabel label = new JLabel("Select a Quiz Topic", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        topicsSelectionPanel.add(label, BorderLayout.NORTH);
        JPanel topicsButtonPanel = new JPanel(new FlowLayout());
        for (String topic : quizzes.keySet()) {
            JButton btn = new JButton(topic);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
            btn.addActionListener(e -> {
                startQuiz(topic);
            });
            topicsButtonPanel.add(btn);
        }
        topicsSelectionPanel.add(topicsButtonPanel, BorderLayout.CENTER);
    }

    private void setupQuestionPanel() {
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        questionPanel.add(optionsPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(submitButton);
        bottomPanel.add(nextButton);
        questionPanel.add(bottomPanel, BorderLayout.SOUTH);
        feedbackLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
        feedbackLabel.setForeground(new Color(0, 102, 51));
        feedbackLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        questionPanel.add(feedbackLabel, BorderLayout.EAST);
        nextButton.setVisible(false);
        feedbackLabel.setVisible(false);
        submitButton.addActionListener(e -> submitAnswers());
        nextButton.addActionListener(e -> nextQuestion());

    }

    private void startQuiz(String topic) {
        currentQuizQuestions = quizzes.get(topic);
        currentQuestionIndex = 0;
        score = 0;
        showQuestion();
        cardLayout.show(mainPanel, "QuestionPanel");
    }

    private void showQuestion() {
        feedbackLabel.setVisible(false);
        nextButton.setVisible(false);
        submitButton.setVisible(false);
        optionsPanel.removeAll();
        radioGroup = new ButtonGroup();
        checkBoxes.clear();
        radioButtons.clear();
        Question currentQ = currentQuizQuestions.get(currentQuestionIndex);
        questionLabel.setText("<html><body style='width: 500px;'>" + (currentQuestionIndex + 1) + ". " + currentQ.getQuestion() + "</body></html>");
        isMultiSelect = currentQ.isMultiSelect();
        if (isMultiSelect) {
            for (String option : currentQ.getOptions()) {
                JCheckBox cb = new JCheckBox(option);
                cb.setFont(new Font("SansSerif", Font.PLAIN, 16));
                checkBoxes.add(cb);
                optionsPanel.add(cb);
            }
        } else {
            for (String option : currentQ.getOptions()) {
                JRadioButton rb = new JRadioButton(option);
                rb.setFont(new Font("SansSerif", Font.PLAIN, 16));
                radioGroup.add(rb);
                radioButtons.add(rb);
                optionsPanel.add(rb);
            }
        }
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void submitAnswers() {
        Question currentQ = currentQuizQuestions.get(currentQuestionIndex);
        List<Integer> selectedAnswers = new ArrayList<>();
        if (isMultiSelect) {
            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isSelected()) {
                    selectedAnswers.add(i);
                }
            }
        } else {
            for (int i = 0; i < radioButtons.size(); i++) {
                if (radioButtons.get(i).isSelected()) {
                    selectedAnswers.add(i);
                    break;
                }
            }
        }
        if (selectedAnswers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one answer", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean correct = currentQ.isCorrect(selectedAnswers);
        if (correct) {
            feedbackLabel.setForeground(new Color(0, 102, 51));
            feedbackLabel.setText("Correct");
            score++;
        } else {
            feedbackLabel.setForeground(Color.red);
            StringBuilder correctOpts = new StringBuilder();
            for (int i : currentQ.getCorrectAnswers()) {
                correctOpts.append(currentQ.getOptions().get(i)).append(",");
            }
            if (correctOpts.length() > 2) {
                correctOpts.setLength(correctOpts.length() - 2);
            }
            feedbackLabel.setText("<html>Incorrect! Correct answer(s):<br>" + correctOpts.toString() + "</html>");
        }
        feedbackLabel.setVisible(true);
        submitButton.setVisible(false);
        nextButton.setVisible(true);
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < currentQuizQuestions.size()) {
            showQuestion();
        } else {
            showQuizResult();
        }
    }

    private void showQuizResult() {
        JOptionPane.showMessageDialog(this, "Quiz complete!\nYour score: " + score + " out of " + currentQuizQuestions.size(),
                "Result",
                JOptionPane.INFORMATION_MESSAGE);
        cardLayout.show(mainPanel, "TopicSelection");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuizApp().setVisible(true);
        });
    }

    private static class Question {

        private String question;
        private List<String> options;
        private List<Integer> correctAnswers;
        private boolean multiSelect;

        public Question(String question, List<String> options, List<Integer> correctAnswers, boolean multiSelect) {
            this.question = question;
            this.options = options;
            this.correctAnswers = new ArrayList<>(correctAnswers);
            Collections.sort(this.correctAnswers);
            this.multiSelect = multiSelect;
        }

        public String getQuestion() {
            return question;
        }

        public List<String> getOptions() {
            return options;
        }

        public List<Integer> getCorrectAnswers() {
            return correctAnswers;
        }

        public boolean isMultiSelect() {
            return multiSelect;
        }

        public boolean isCorrect(List<Integer> selectedAnswers) {
            List<Integer> sel = new ArrayList<>(selectedAnswers);
            Collections.sort(sel);
            return sel.equals(correctAnswers);
            
        }
    }
}
