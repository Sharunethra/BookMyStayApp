import java.util.*;
public class BookMyStayApp {


    static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
        }
    }


    static class RoomInventory {
        private Map<String, Integer> rooms;

        public RoomInventory() {
            rooms = new HashMap<>();
            rooms.put("Single", 2);
            rooms.put("Double", 2);
            rooms.put("Suite", 1);
        }

        public boolean isAvailable(String roomType) {
            return rooms.getOrDefault(roomType, 0) > 0;
        }

        public void bookRoom(String roomType) throws InvalidBookingException {
            int count = rooms.get(roomType);

            if (count <= 0) {
                throw new InvalidBookingException("No rooms available.");
            }

            rooms.put(roomType, count - 1);
        }
    }


    static class ReservationValidator {

        public void validate(String guestName,
                             String roomType,
                             RoomInventory inventory)
                throws InvalidBookingException {

            if (guestName == null || guestName.trim().isEmpty()) {
                throw new InvalidBookingException("Guest name cannot be empty.");
            }

            if (!roomType.equals("Single") &&
                    !roomType.equals("Double") &&
                    !roomType.equals("Suite")) {

                throw new InvalidBookingException("Invalid room type selected.");
            }

            if (!inventory.isAvailable(roomType)) {
                throw new InvalidBookingException("Selected room not available.");
            }
        }
    }


    static class BookingRequestQueue {
        private Queue<String> queue = new LinkedList<>();

        public void addRequest(String request) {
            queue.add(request);
        }

        public void processRequests() {
            while (!queue.isEmpty()) {
                System.out.println("Processing: " + queue.poll());
            }
        }
    }


    public static void main(String[] args) {

        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try {

            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();


            validator.validate(guestName, roomType, inventory);


            inventory.bookRoom(roomType);


            bookingQueue.addRequest(guestName + " booked " + roomType);
            bookingQueue.processRequests();

            System.out.println("Booking successful!");

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}