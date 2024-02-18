import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerSyncer {
    private static final Logger logger = Logger.getLogger(ServerSyncer.class.getName());
    private final LocalStorage localStorage;

    public ServerSyncer(LocalStorage localStorage) {
        this.localStorage = localStorage;
    }

    public void synchronizeWithServer() {
        if (isInternetConnected()) {
            ArrayList<FloorPlan> localPlans = (ArrayList<FloorPlan>) localStorage.loadPlans();

            for (FloorPlan floorPlan : localPlans) {
                updateFloorPlan(floorPlan);
                logger.info("Synchronizing with server: " + floorPlan.getFloorPlanName());
            }

            localStorage.clearStorage();
        } else {
            logger.warning("No internet connection.");
            throw new IllegalStateException("No internet connection.");
        }
    }

    private boolean isInternetConnected() {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://www.google.com"))
                    .GET()
                    .build();

            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException | URISyntaxException e) {
            logger.log(Level.SEVERE, "Error occurred while checking internet connection", e);
            return false;
        }
    }

    public void updateFloorPlan(FloorPlan floorPlan) {
        logger.info("Floor plan updated to " + floorPlan);
    }
}
