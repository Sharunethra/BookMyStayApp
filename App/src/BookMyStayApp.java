import java.util.*;

class BookingRequest {
    String guestName;
    String roomType;

    BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class BookingRequestQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
    }

    public synchronized BookingRequest getRequest() {
        if (queue.isEmpty()) return null;
        return queue.poll();
    }
}

class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    RoomInventory() {
        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }

    public synchronized boolean allocateRoom(String type) {
        if (rooms.get(type) > 0) {
            rooms.put(type, rooms.get(type) - 1);
            return true;
        }
        return false;
    }

    public void display() {
        System.out.println("\nRemaining Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + ": " + rooms.get(type));
        }
    }
}

class RoomAllocationService {
    public void allocateRoom(BookingRequest request, RoomInventory inventory) {
        boolean success = inventory.allocateRoom(request.roomType);
        if (success) {
            System.out.println("Booking confirmed for Guest: " + request.guestName +
                    ", Room Type: " + request.roomType);
        } else {
            System.out.println("Booking failed for Guest: " + request.guestName +
                    ", Room Type: " + request.roomType);
        }
    }
}

class ConcurrentBookingProcessor implements Runnable {
    private BookingRequestQueue queue;
    private RoomInventory inventory;
    private RoomAllocationService service;

    ConcurrentBookingProcessor(BookingRequestQueue queue, RoomInventory inventory, RoomAllocationService service) {
        this.queue = queue;
        this.inventory = inventory;
        this.service = service;
    }

    public void run() {
        while (true) {
            BookingRequest request;
            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) break;

            synchronized (inventory) {
                service.allocateRoom(request, inventory);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService service = new RoomAllocationService();

        queue.addRequest(new BookingRequest("Abhi", "Single"));
        queue.addRequest(new BookingRequest("Vanmathi", "Double"));
        queue.addRequest(new BookingRequest("Kural", "Suite"));
        queue.addRequest(new BookingRequest("Subha", "Single"));
        queue.addRequest(new BookingRequest("Ravi", "Double"));
        queue.addRequest(new BookingRequest("Anu", "Suite"));

        Thread t1 = new Thread(new ConcurrentBookingProcessor(queue, inventory, service));
        Thread t2 = new Thread(new ConcurrentBookingProcessor(queue, inventory, service));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Execution interrupted");
        }

        inventory.display();
    }
}