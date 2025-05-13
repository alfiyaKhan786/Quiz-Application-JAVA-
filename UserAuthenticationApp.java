import java.util.HashMap;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
public class UserAuthenticationApp{
    private static HashMap<String,String> users=new HashMap<>();
    public static void main(String [] args){
Scanner sc=new Scanner(System.in);
System.out.println("Welcome to User Authentication System");
while(true){
    System.out.println("\nPlease choose an option:");
    System.out.println("1 - Register");
    System.out.println("2 - Login");
    System.out.println("3 - Exit");
    System.out.print("Choice: ");
String choice=sc.nextLine();
switch(choice){
    case "1":
    register(sc);
    break;
    case "2":
    login(sc);
    break;
    case "3":
    System.out.println("Exiting...GoodBye!!");
    sc.close();
    System.exit(0);
    default:
    System.out.println("Invalid option.Please try again");
}
}
    }


    private static void register(Scanner sc){
        System.out.print("Enter a new username:");
        String userName=sc.nextLine().trim();
        if(userName.isEmpty()){
            System.out.println("Username cannot be empty.");
            return;
        }
        if(users.containsKey(userName)){
            System.out.println("Username already exists.Please choose another.");
            return;
        }
        System.out.print("Enter a password");
        String password=sc.nextLine();
        if(password.isEmpty()){
            System.out.println("Password cannot be empty.");
            return;

        }
        String hashedPassword=hashPassword(password);
        users.put(userName,hashedPassword);
        System.out.println("Registration successfull! You can now login");
    }
    private static void login(Scanner sc){
System.out.print("Enter your username:");
String userName=sc.nextLine().trim();
if(!users.containsKey(userName)){
    System.out.println("Username not found.Please register first");
    return;
}
System.out.print("Enter your password:");
String password=sc.nextLine();
String hashedPassword=hashPassword(password);
if(hashedPassword.equals(users.get(userName))){
    System.out.println("Login successfull! Welcome,"+userName+"!");
}
else{
    System.out.println("Incorrect password.Please try again");
}
    }
    private static String hashPassword(String password){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            
            throw new RuntimeException("SHA-256 algorithm not found.");
        }

    }
    private static String bytesToHex(byte [] hash){
        StringBuilder hexString =new StringBuilder(2*hash.length);
        for(byte b:hash){
            String hex=Integer.toHexString(0xff &b);
            if(hex.length()==1){
                hexString.append('0');

            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}