import java.util.ArrayList;

public class ConflictResolver {
    // This method do conflict resolution based on priority, timestamp, or user roles
    public void resolveConflict(final FloorPlan localPlan, final FloorPlan serverPlan, final FloorPlanManagement admin) {
        // if user is admin then admin changes will be valid
        if (admin != null && admin.checkPassword("admin_password") && admin.getRole() == Roles.ADMIN) {
            System.out.println("Admin resolving conflict. Admin's version takes priority.");
            serverPlan.uploadPlan();
        } 
        else {
            // Check priority first
            int priorityDifference = Integer.compare(localPlan.getPriority(), serverPlan.getPriority());

            if (priorityDifference > 0) {
                System.out.println("Conflict resolved!!. Uploading local version based on priority...");
                localPlan.uploadPlan();
            } 
            else if (priorityDifference < 0) {
                System.out.println("Conflict resolved!!. Merging server version based on priority...");
                serverPlan.uploadPlan();
            } 
            else {
                // If priorities are the same, check timestamp
                int timeStampDiff = localPlan.getLastModified().compareTo(serverPlan.getLastModified());
    
                if (timeStampDiff > 0) {
                    System.out.println("Conflict resolved!!. Uploading local version based on timestamp...");
                    localPlan.uploadPlan();
                } 
                else if (timeStampDiff < 0) {
                    System.out.println("Conflict resolved!!. Merging server version based on timestamp...");
                    serverPlan.uploadPlan();
                } 
                else {
                    System.out.println("Conflict Occurred.");
                    throw new IllegalStateException("Conflict Occurred.");
                }
            }
        }
    }
}