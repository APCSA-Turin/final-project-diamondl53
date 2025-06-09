package com.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Scanner;

public class Appstuff {
    
    private static final String API_KEY = "AIzaSyA6-T4EZ2TnGfLvIEma89qj7DncsKL4szE";
    private static final int RADIUS = 1000; // (1 km)
    private static double userLat;
    private static double userLon;
    private static String selectedCuisine;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║   RESTAURANT RECOMMENDER     ║");
        System.out.println("╚══════════════════════════════╝");
        
        while(true) {
            
            displayCuisineMenu();
            System.out.print("Choose cuisine (0-11): ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 
            
            if(choice == 0) {
                System.out.println("Thank you for using our service!");
                break;
            }
            
            if(choice == 1){selectedCuisine = "American";}
            else if(choice == 2) {selectedCuisine = "Chinese";}
            else if(choice == 3) {selectedCuisine = "Italian";}
            else if(choice == 4) {selectedCuisine = "Mexican";}
            else if(choice == 5) {selectedCuisine = "Indian";}
            else if(choice == 6) {selectedCuisine = "Japanese";}
            else if(choice == 7) {selectedCuisine = "Greek";}
            else if(choice == 8){selectedCuisine = "Russian";}
            else if(choice == 9){selectedCuisine = "Thai";}
            else if(choice == 10){selectedCuisine = "Korean";}
            else if(choice == 11){selectedCuisine = "Vietnamese";}
            else {
                System.out.println("Invalid choice! Please try again.");
                
            }
            askForLatAndLong(scanner);
            displayRestaurants();
            System.out.print("Press Enter to continue...");
            scanner.nextLine();
        }
        scanner.close();
    }
    
    public static void displayCuisineMenu() {
        printBoxTop();
        System.out.println("║ 1. American                  ║");
        System.out.println("║ 2. Chinese                   ║");
        System.out.println("║ 3. Italian                   ║");
        System.out.println("║ 4. Mexican                   ║");
        System.out.println("║ 5. Indian                    ║");
        System.out.println("║ 6. Japanese                  ║");
        System.out.println("║ 7. Greek                     ║");
        System.out.println("║ 8. Russian                   ║");
        System.out.println("║ 9. Thai                      ║");
        System.out.println("║ 10. Korean                   ║");
        System.out.println("║ 11. Vietnamese               ║");
        System.out.println("║ 0. Exit                      ║");
        printBoxBottom();
    }
    
    public static void askForLatAndLong(Scanner scanner) {
        System.out.print("Enter latitude (e.g. 40.7128): ");
        userLat = scanner.nextDouble();
        System.out.print("Enter longitude (e.g. -74.0060): ");
        userLon = scanner.nextDouble();
        scanner.nextLine(); 
    }
    
    public static void displayRestaurants() throws Exception {
    System.out.println("\nFinding " + selectedCuisine + " restaurants nearby...");

    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
    "?location=" + userLat + "," + userLon + "&radius=" + RADIUS + "&type=restaurant" + "&keyword=" + selectedCuisine + "&key=" + API_KEY;

    String jsonResponse = Apis.getData(url);
    JSONObject response = new JSONObject(jsonResponse);
    JSONArray restaurants = response.getJSONArray("results");

    printBoxTop();
    System.out.println("║    TOP " + padRight(selectedCuisine.toUpperCase(), 16) + "    ║");
    printBoxMiddle();

    int count = 0;
    for (int i = 0; i < restaurants.length(); i++) {
        if (count >= 3) {
            break;
        }
        JSONObject restaurant = restaurants.getJSONObject(i);
        String name = restaurant.getString("name"); 
        double rating = restaurant.optDouble("rating", 0);
        String address = restaurant.optString("vicinity", "Address not available");
        System.out.println("║ " + (count + 1) + ". " + padRight(name, 23) + "║");
        System.out.println("║   Rating: " + padRight(String.valueOf(rating), 16) + "║");
        System.out.println("║   " + padRight(address, 25) + "║");

        count++;
        if (count < 3) {
            printBoxMiddle();
        }
    }

    if (count == 0) {
        System.out.println("║ No matching restaurants found.         ║");
    }

    printBoxBottom();
    
    }
    public static void printBoxTop() {
        System.out.println("╔══════════════════════════════╗");
    }
    
    public static void printBoxMiddle() {
        System.out.println("╠══════════════════════════════╣");
    }
    
    public static void printBoxBottom() {
        System.out.println("╚══════════════════════════════╝");
    }
    
    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
}
