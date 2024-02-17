import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

enum Roles {
    ADMIN,
    EMPLOYEE
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


    // This method checks for password
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
            FloorPlan localPlan = getFloorPlans("local plan");
            FloorPlan serverPlan = getFloorPlans("server plan");
            ConflictResolver conflictResolver = new ConflictResolver();
            conflictResolver.resolveConflict(localPlan, serverPlan, user);
        } else {
            System.out.println("Authentication failed. Exiting...");
            return ;
        }
    }

    private  static FloorPlan getFloorPlans(String s){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Upload the " + s);

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
            System.out.println("Enter details for Room " + i + ":");
            int floor = getIntInput(scanner, "Floor: ");
            int roomNo = getIntInput(scanner, "RoomNumber: ");
            int capacity = getIntInput(scanner, "Capacity: ");

            meetingRooms.add(new MeetingRoom(roomNo ,capacity, floor));
        }
        
        FloorPlanManagement admin = new FloorPlanManagement("admin", "admin_password", Roles.ADMIN);

        return new FloorPlan(
            1,                  
            planName,     
            version,                  
            new Date(),         
            meetingRooms,      
            new Date(),         
            admin,
            priority
        );
    }
    

    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter an integer: ");
                scanner.nextLine(); // Consume the invalid input
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
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }

    private static FloorPlanManagement authenticateUser() {
        // Define authorized users
        ArrayList<FloorPlanManagement> authorizedUsers = new ArrayList<>();
        authorizedUsers.add(new FloorPlanManagement("admin", "admin_password", Roles.ADMIN));
        authorizedUsers.add(new FloorPlanManagement("employee", "employee_password", Roles.EMPLOYEE));
    
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role (ADMIN/EMPLOYEE): ");
        String roleStr = scanner.nextLine().toUpperCase();
        Roles role;
        try {
        role = Roles.valueOf(roleStr);
    } catch (IllegalArgumentException e) {
        System.out.println("Invalid role. Please enter either ADMIN or EMPLOYEE.");
        return null;
    }
        for (FloorPlanManagement user : authorizedUsers) {
            if (user.getUsername().equals(username) && user.checkPassword(password)) {
                System.out.println("Authentication successful.");
                return user;
            }
        }
        System.out.println("Authentication failed. Invalid username or password.");
        return null;
    }

}






