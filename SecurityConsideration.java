import org.mindrot.jbcrypt.Bcrypt;
public class SecurityConsideration{
public static String hashPassword(String plainPassword){
    return BCrypt.hashpw(plainPassword,BCrypt.gensalt());
}
public static boolean checkPassword(String plainPassword,String hashedPassword){
    return BCrypt.checkpw(plainPassword, hashedPassword);
        
    
}
public static void main(String[] args) {
    String password="mySecurePassword";
    String hashedPassword=hashPassword(password);
    System.out.println("Hashed Password:"+hashedPassword);
    boolean isMatch=checkPassword(password,hashedPassword);
    System.out.println("Password matches:"+isMatch);
}
}