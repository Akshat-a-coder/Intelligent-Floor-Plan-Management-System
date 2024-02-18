import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;


enum Roles {
    ADMIN,
    USER
}

class FloorPlanManagement{
    private final String username;
    private final String hashedPassword;
    private final Roles role;


    public FloorPlanManagement(final String username, final String password, final Roles role) {
        this.username = username;
        this.hashedPassword = encryptPass(password);
        this.role = role;
    }
    Roles getRole(){
        return role;
    }

    String getUsername(){
        return this.username;
    }


   public boolean checkPassword(final String enteredPassword) {
        final String enteredPasswordHash = encryptPass(enteredPassword);
        return enteredPasswordHash.equals(hashedPassword);
    }

    private String encryptPass(String password) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            final byte[] encryptedCode = md.digest(password.getBytes());
            final StringBuilder sb = new StringBuilder();

            for (byte b : encryptedCode) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } 
        catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}

public class FloorPlanManagementSystem {
    public static void main(String[] args) {
        FloorPlanManagement user = authenticateUser();
        if (user != null) {
            FloorPlan localPlan = getFloorPlans("local plan" , user);
            FloorPlan serverPlan = getFloorPlans("server plan" , user);
            ConflictResolver conflictResolver = new ConflictResolver();
            conflictResolver.resolveConflict(localPlan, serverPlan, user);
        } else {
            printNeg("Authentication failed. Exiting...");
            return ;
        }
    }

    private  static FloorPlan getFloorPlans(String s , FloorPlanManagement user){
        Scanner scanner = new Scanner(System.in);
        printPos("Upload the " + s);

        System.out.print("Enter the floor plan name: ");
        String planName = scanner.nextLine();
        
        System.out.print("Enter the version : ");
        int version = getIntInput(scanner);

        System.out.print("Enter the priority : ");
        int priority = getIntInput(scanner);

        System.out.print("Enter the number of rooms in " + planName + " : ");
        int numRooms = getIntInput(scanner);

    
        ArrayList<MeetingRoom> meetingRooms = new ArrayList<>();
        for (int i = 1; i <= numRooms; i++) {
            printPos("Enter details for Room " + i);
            int floor = getIntInput(scanner, "Floor: ");
            int roomNo = getIntInput(scanner, "RoomNumber: ");
            int capacity = getIntInput(scanner, "Capacity: ");

            meetingRooms.add(new MeetingRoom(roomNo ,capacity, floor));
        }
        
        return new FloorPlan(
            1,                  
            planName,     
            version,                  
            new Date(),         
            meetingRooms,      
            new Date(),         
            user,
            priority
        );
    }
    

    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter an integer: ");
                scanner.nextLine(); 
            }
        }
    }

    private static int getIntInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter an integer: ");
                scanner.nextLine(); 
            }
        }
    }

    private static FloorPlanManagement authenticateUser() {
        // Define authorized users
        ArrayList<FloorPlanManagement> authorizedUsers = new ArrayList<>();
        authorizedUsers.add(new FloorPlanManagement("admin", "admin_password", Roles.ADMIN));
        authorizedUsers.add(new FloorPlanManagement("user", "user_password", Roles.USER));
    
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role (ADMIN/USER): ");
        String roleStr = scanner.nextLine().toUpperCase();
        Roles role;
        try {
        role = Roles.valueOf(roleStr);
    } catch (IllegalArgumentException e) {
        printNeg("Invalid role. Please enter either ADMIN or USER.");
        return null;
    }
        for (FloorPlanManagement user : authorizedUsers) {
            if (user.getUsername().equals(username) && user.checkPassword(password)) {
                printPos("Authentication successful.");
                return user;
            }
        }
        printNeg("Authentication failed. Invalid username or password.");
        return null;
    }

    public static void printPos(String s){
        String greenColor = "\u001B[32m";
        String boldStyle = "\u001B[1m";
        String resetColorAndStyle = "\u001B[0m";
        System.out.println(greenColor + boldStyle + s + resetColorAndStyle);
    }

    public static void printNeg(String s){
        String redColor = "\u001B[31m";
        String boldStyle = "\u001B[1m";
        String resetColorAndStyle = "\u001B[0m";
        System.out.println(redColor + boldStyle + s + resetColorAndStyle);
    }
}






