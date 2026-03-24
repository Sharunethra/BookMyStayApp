import java.io.*;
import java.util.*;

class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    RoomInventory() {
        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }

    public Map<String, Integer> getRooms() {
        return rooms;
    }

    public void setRoom(String type, int count) {
        rooms.put(type, count);
    }

    public void display() {
        System.out.println("\nCurrent Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + ": " + rooms.get(type));
        }
    }
}

class FilePersistenceService {

    public void saveInventory(RoomInventory inventory, String filePath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            for (Map.Entry<String, Integer> entry : inventory.getRooms().entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
            writer.close();
            System.out.println("Inventory saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory.");
        }
    }

    public void loadInventory(RoomInventory inventory, String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("No valid inventory data found. Starting fresh.");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                inventory.setRoom(parts[0], Integer.parseInt(parts[1]));
            }

            reader.close();
            System.out.println("Inventory restored successfully.");

        } catch (Exception e) {
            System.out.println("Error loading inventory. Starting fresh.");
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        FilePersistenceService service = new FilePersistenceService();

        String filePath = "inventory.txt";

        System.out.println("System Recovery");
        service.loadInventory(inventory, filePath);

        inventory.display();

        service.saveInventory(inventory, filePath);
    }
}