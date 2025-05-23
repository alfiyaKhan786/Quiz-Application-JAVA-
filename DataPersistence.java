import java.sql.*;
import java.util.ArrayList;
import java.util.List;
class JDBCUtil{
    private static final String URL="jdbc:mysql://localhost:3306/quizdb";
    private static final String USER="root";
    private static final String PASSWORD="password";
    static{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e){
e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
}
class User{
    private int id;
    private String username;
    private String email;
    private String password;
    public User(){}
    public User(int id,String username,String email,String password){
        this.id=id;
        this.username=username;
        this.email=email;
        this.password=password;
    }
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public String getUsername(){return username;}
    public void setUsername(String username){this.username=username;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}
    public String getPassword(){return password;}
    public void setPassword(){this.password=password;}

}
class UserDAO{
    public boolean createUser(User user){
        String sql="INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try(Connection conn=JDBCUtil.getConnection();
        PreparedStatement ps=conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,user.getUsername());
            ps.setString(2,user.getEmail());
            ps.setString(3,user.getPassword());
            int affectedRows=ps.executeUpdate();
            if(affectedRows==0) return false;
            try(ResultSet geneeratedKeys=ps.getGeneratedKeys()){
                if(generatedKeys.next()){
                    user.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public User getUserById(int id){
        String sql= "SELECT * FROM users WHERE id = ?";
        try(Connection conn =JDBCUtil.getConnection();
        PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setInt(1,id);
            try(ResultSet rs=ps.executeQuery()){
                 if(rs.next()){
                    User user=new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }
            } catch(SQLException e){
                e.printStackTrace();
            }
            return null;
        
    }
    public boolean updateUser(User user){
        String sql="UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
        try(Connection conn=JDBCUtil.getConnection();
        PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,user.getUsername());
            ps.setString(2,user.getEmail());
            ps.setString(3,user.getPassword());
            ps.setInt(4,user.getId());
            int affectedRows=ps.executeUpdate();
            return affectedRows>0;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteUser(int id){
        String sql="DELETE FROM users WHERE id = ?";
        try(Connection conn=JDBCUtil.getConnection();
        PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setInt(1,id);
            int affectedRows=ps.executeUpdate();
            return affectedRows>0;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public List<User> getAllUsers(){
        List<User> users=new ArrayList<>();
        String sql="SELECT * FROM users";
        try(Connection conn=JDBCUtil.getConnection();
        Statement stmt=conn.createStatement();
        ResultSet rs=stmt.executeQuery(sql)){
            while(rs.next()){
                User user=new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
                );
                users.add(user);

            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return users;
    }
}
        
    class Quiz{
        private int id;
        private String title;
        private String description;
        public Quiz(){}
        public Quiz(int id,String title,String description){
            this.id=id;
            this.title=title;
            this.description;
        }
        public int getId(){return id;}
        public void setid(int id){this.id=id;}
        public String getTitle(){return title;}
        public void setTitle(String title){this.title=title;}
        public String getDescription(){return description;}
        public void setDescription(String description){this.description=description;}

    }
    class QuizDAO{
        public boolean createQuiz(Quiz quiz){
            String sql= "INSERT INTO quizzes (title, description) VALUES (?, ?)";
            try (Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
           ps.setString(1, quiz.getTitle());
           ps.setString(2, quiz.getDescription());
           int affectedRows = ps.executeUpdate();
           if (affectedRows == 0) return false;
           try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
               if (generatedKeys.next()) {
                   quiz.setId(generatedKeys.getInt(1));
               }
           }
           return true;
       } catch (SQLException e) {
           e.printStackTrace();
           return false;
       }
        }
        public Quiz getQuizById(int id){
            String sql="SELECT * FROM quizzes WHERE id=?";
            try(Connection conn=JDBCUtil.getConnection();
            PreparedStatment ps=conn.prepareStatement(sql)){
                ps.setInt(1,id);
                try(ResultSet rs=ps.executeQuery()){
                    if(rs.next()){
                        return new Quiz(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description")

                        );

                    }
                }
            } catch(SQLException e){
                e.printStackTrace();

            }
            return null;
        }
        public boolean updateQuiz(Quiz quiz){
            String sql="UPDATE quizzes SET title =?,description =?WHERE id =?";
            try(Connection conn=JDBCUtil.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
                ps.setString(1,quiz.getTitle());
                ps.setString(2,quiz.getDescription());
                ps.setInt(3,quiz.getId());
                int affectedRows=ps.executeUpdate();
                return affectedRows>0;
            }catch(SQLException e){
                e.printStackTrace();
                return false;
            }
        }
        public boolean deleteQuiz(int id){
            String sql="DELETE from quizzes WHERE id=?";
            try(Connection conn=JDBCUtil.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
                ps.setInt(1,id);
                int affectedRows=ps.executeUpdate();
                return affectedRows>0;
            }catch(SQLException e){
                e.printStackTrace();
                return false;
            }
        }
        public List<Quiz> getAllQuizzes(){
            List<Quiz> quizzes=new ArrayList<>();
            String sql="SELECT * FROM quizzes";
            try(Connection conn =JDBCUtil.getConnection();
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery(sql)){
                while(rs.next()){
                    quizzes.add(new Quiz(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description")
                    ));
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
            return quizzes;
        }
    }
    class Question{
        private int id;
        private int quizId;
        private String questionText;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private String correctOption;
        public Question(){}
        public Question(int id,int quizId,String questionText,String optionA,String optionB,String optionC,String optionD,String correctOption){
            this.id = id;
            this.quizId = quizId;
            this.questionText = questionText;
            this.optionA = optionA;
            this.optionB = optionB;
            this.optionC = optionC;
            this.optionD = optionD;
            this.correctOption = correctOption;
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQuizId() {
            return quizId;
        }

        public void setQuizId(int quizId) {
            this.quizId = quizId;
        }

        public String getQuestionText() {
            return questionText;
        }

        public void setQuestionText(String questionText) {
            this.questionText = questionText;
        }

        public String getOptionA() {
            return optionA;
        }

        public void setOptionA(String optionA) {
            this.optionA = optionA;
        }

        public String getOptionB() {
            return optionB;
        }

        public void setOptionB(String optionB) {
            this.optionB = optionB;
        }

        public String getOptionC() {
            return optionC;
        }

        public void setOptionC(String optionC) {
            this.optionC = optionC;
        }

        public String getOptionD() {
            return optionD;
        }

        public void setOptionD(String optionD) {
            this.optionD = optionD;
        }

        public String getCorrectOption() {
            return correctOption;
        }

        public void setCorrectOption(String correctOption) {
            this.correctOption = correctOption; }
    }
    class QuestionDAO{
        public boolean createQuestion(Question question){
            String sql = "INSERT INTO questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, question.getQuizId());
                ps.setString(2, question.getQuestionText());
                ps.setString(3, question.getOptionA());
                ps.setString(4, question.getOptionB());
                ps.setString(5, question.getOptionC());
                ps.setString(6, question.getOptionD());
                ps.setString(7, question.getCorrectOption());
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    return false;
                }
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        question.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        public Question getQuestionById(int id){
            String sql = "SELECT * FROM questions WHERE id = ?";
            try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Question(
                                rs.getInt("id"),
                                rs.getInt("quiz_id"),
                                rs.getString("question_text"),
                                rs.getString("option_a"),
                                rs.getString("option_b"),
                                rs.getString("option_c"),
                                rs.getString("option_d"),
                                rs.getString("correct_option")
                        );
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
        public boolean updateQuestion(Question question){
            String sql = "UPDATE questions SET quiz_id = ?, question_text = ?, option_a = ?, option_b = ?, option_c = ?, option_d = ?, correct_option = ? WHERE id = ?";
            try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, question.getQuizId());
                ps.setString(2, question.getQuestionText());
                ps.setString(3, question.getOptionA());
                ps.setString(4, question.getOptionB());
                ps.setString(5, question.getOptionC());
                ps.setString(6, question.getOptionD());
                ps.setString(7, question.getCorrectOption());
                ps.setInt(8, question.getId());
                int affectedRows = ps.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        public boolean deleteQuestion(int id){
            String sql = "DELETE FROM questions WHERE id = ?";
            try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int affectedRows = ps.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        public List<Question> getQuestionsByQuizId(int quizId) {
            List<Question> questions = new ArrayList<>();
            String sql = "SELECT * FROM questions WHERE quiz_id = ?";
            try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, quizId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Question question = new Question(
                                rs.getInt("id"),
                                rs.getInt("quiz_id"),
                                rs.getString("question_text"),
                                rs.getString("option_a"),
                                rs.getString("option_b"),
                                rs.getString("option_c"),
                                rs.getString("option_d"),
                                rs.getString("correct_option")
                        );
                        questions.add(question);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return questions;
        }
    }
    


