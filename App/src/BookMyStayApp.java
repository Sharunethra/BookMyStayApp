import java.util.*;

/**
 * CLASS - AddOnService
 * Represents an optional service
 */
class AddOnService {

    private String serviceName;
    private double cost;

    // Constructor
    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    // Getter for service name
    public String getServiceName() {
        return serviceName;
    }

    // Getter for cost
    public double getCost() {
        return cost;
    }
}

/**
 * CLASS - AddOnServiceManager
 * Manages services mapped to reservation IDs
 */
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> servicesByReservation;

    // Constructor
    public AddOnServiceManager() {
        servicesByReservation = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {

        // If reservation not present → create list
        servicesByReservation.putIfAbsent(reservationId, new ArrayList<>());

        // Add service
        servicesByReservation.get(reservationId).add(service);
    }

    // Calculate total cost of services
    public double calculateTotalServiceCost(String reservationId) {

        double total = 0.0;

        List<AddOnService> services = servicesByReservation.get(reservationId);

        if (services != null) {
            for (AddOnService s : services) {
                total += s.getCost();
            }
        }

        return total;
    }
}

/**
 * MAIN CLASS - BookMyStayApp
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        // Create manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Reservation ID
        String reservationId = "Single-1";

        // Create services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService spa = new AddOnService("Spa", 700);
        AddOnService pickup = new AddOnService("Airport Pickup", 300);

        // Add services
        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, spa);
        manager.addService(reservationId, pickup);

        // Calculate total cost
        double totalCost = manager.calculateTotalServiceCost(reservationId);

        // Output
        System.out.println("Add-On Service Selection");
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Total Add-On Cost: " + totalCost);
    }
}