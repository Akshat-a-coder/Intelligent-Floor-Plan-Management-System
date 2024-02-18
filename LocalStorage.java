import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class LocalStorage {
    private final List<FloorPlan> localPlans;
    private static final Logger log = Logger.getLogger(LocalStorage.class.getName());

    public LocalStorage() {
        this.localPlans = new ArrayList<>();
    }

    public void savePlan(final FloorPlan floorPlan) {
        // Save the floor plan locally
        localPlans.add(floorPlan);
        log.info("Floor plan saved locally: " + floorPlan.getFloorPlanName());
    }

    public List<FloorPlan> loadPlans() {
        // Load locally stored floor plans
        log.info("Loading locally stored floor plans...");
        return new ArrayList<>(localPlans);
    }

    public void clearStorage() {
        // Clear local storage
        localPlans.clear();
        log.info("Local storage cleared.");
    }
}
