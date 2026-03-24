import java.util.*;
static class RoomInventory {
        private Map<String, Integer> rooms;

        public RoomInventory() {
            rooms = new HashMap<>();
            rooms.put("Single", 5);
            rooms.put("Double", 3);
            rooms.put("Suite", 2);
        }

        public void increaseRoom(String roomType) {
            rooms.put(roomType, rooms.getOrDefault(roomType, 0) + 1);
        }

        public int getAvailability(String roomType) {
            return rooms.getOrDefault(roomType, 0);
        }
    }


    static class CancellationService {

        private Stack<String> releasedRoomIds;

        // Reservation ID -> Room Type mapping
        private Map<String, String> reservationRoomMap;

        public CancellationService() {
            releasedRoomIds = new Stack<>();
            reservationRoomMap = new HashMap<>();
        }

        // Register confirmed booking
        public void registerBooking(String reservationId, String roomType) {
            reservationRoomMap.put(reservationId, roomType);
        }

        // Cancel booking and rollback
        public void cancelBooking(String reservationId, RoomInventory inventory) {

            // Validate existence
            if (!reservationRoomMap.containsKey(reservationId)) {
                System.out.println("Cancellation failed: Invalid reservation ID.");
                return;
            }

            String roomType = reservationRoomMap.get(reservationId);

            // Push to stack (rollback history)
            releasedRoomIds.push(reservationId);

            // Restore inventory
            inventory.increaseRoom(roomType);

            // Remove booking record
            reservationRoomMap.remove(reservationId);

            System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
        }

        // Show rollback history
        public void showRollbackHistory() {
            System.out.println("\nRollback History (Most Recent First):");

            if (releasedRoomIds.isEmpty()) {
                System.out.println("No cancellations yet.");
                return;
            }

            for (int i = releasedRoomIds.size() - 1; i >= 0; i--) {
                System.out.println("Released Reservation ID: " + releasedRoomIds.get(i));
            }
        }
    }

    // ================= MAIN METHOD =================
    public static void main(String[] args) {

        System.out.println("Booking Cancellation");

        RoomInventory inventory = new RoomInventory();
        CancellationService service = new CancellationService();

        // Simulate confirmed booking
        String reservationId = "Single-1";
        service.registerBooking(reservationId, "Single");

        // Perform cancellation
        service.cancelBooking(reservationId, inventory);

        // Show rollback history
        service.showRollbackHistory();

        // Show updated inventory
        System.out.println("\nUpdated Single Room Availability: "
                + inventory.getAvailability("Single"));
    }
