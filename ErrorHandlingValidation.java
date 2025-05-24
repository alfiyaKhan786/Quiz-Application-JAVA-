import java.util.Scanner;
public class ErrorHandlingValidation {
    public static void main(String [] args){
Scanner sc=new Scanner(System.in);
try{
    System.out.print("Enter your age:");
    String input=sc.nextLine();
    int age=validateAge(input);
    System.out.println("Your age is:"+age);
}catch(IllegalArgumentException e){
    System.err.println("Input error:"+e.getMessage());
}catch(Exception e){
    System.out.println("Unexpected error occurred:"+e.getMessage());
}finally{
    sc.close();
}
    }
    // @param input
    // @return
    // @throws IllegalArgumentException
    private static int validateAge(String input) throws IllegalArgumentException{
        if(input==null||input.trim().isEmpty()){
            throw new IllegalArgumentException("Age cannot be empty");
        }
        int age;
        try{
            age=Integer.parseInt(input);
        }catch(NumberFormatException e){
            throw new IllegalArgumentException("Age must be a vaild integer");
        }
        if(age<0||age>20){
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        return age;
    }
}
