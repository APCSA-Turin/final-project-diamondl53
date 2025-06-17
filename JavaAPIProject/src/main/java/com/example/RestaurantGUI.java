package com.example;
//all the imports needed for the program to run such as the GUI components, JSON parsing, and ones that deal with the files
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.io.*;

public class RestaurantGUI extends JFrame {
    //initializes the API key, radius for search, and variables for user input
    private static final String API_KEY = "AIzaSyA6-T4EZ2TnGfLvIEma89qj7DncsKL4szE";
    private static final int RADIUS = 1000;
    //initializes the formats for the GUI
    private JPanel mainPanel, inputPanel, resultPanel;
    private JComboBox<String> cuisineBox;
    private JTextField latField, lonField;
    private java.util.List<String> savedRestaurants;
    //the file where the saved restaurants will be stored which is a txt file (text file)
    private static final String SAVE_FILE = "saved_restaurants.txt";

    public RestaurantGUI() {
        // Set up the main frame with the font title and size
        setTitle("RESTAURANT RECOMMENDER");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIManager.put("Label.font", new Font("Comic Sans MS", Font.PLAIN, 17));
        UIManager.put("Button.font", new Font("Comic Sans MS", Font.PLAIN, 17));
        UIManager.put("ComboBox.font", new Font("Comic Sans MS", Font.PLAIN, 17));
        UIManager.put("TextField.font", new Font("Comic Sans MS", Font.PLAIN, 17));
        //creates an arraylist to store saved restaurants
        savedRestaurants = new ArrayList<>();
        loadSavedRestaurantsFromFile();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel);
        mainPanel.setBackground(new Color (255,0,0));
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.setBackground(new Color(245, 245, 245)); 
        inputPanel.add(new JLabel("Cuisine:"));
        // creates a box with different cuisines to select from
        // The cuisines are stored in an array and then added to the JComboBox
        String[] cuisines = {"Chinese", "Italian", "Mexican", "Indian", "Japanese", "Greek", "Russian", "Thai"};
        cuisineBox = new JComboBox<>(cuisines);
        inputPanel.add(cuisineBox);
        inputPanel.add(new JLabel("Latitude:"));
        latField = new JTextField("40.7128"); 
        inputPanel.add(latField);
        inputPanel.add(new JLabel("Longitude:"));
        lonField = new JTextField("-74.0060"); 
        inputPanel.add(lonField);
        mainPanel.add(inputPanel);
        
        // Search button
        JButton searchButton = new JButton("Find Restaurants");
        //uses try catch to handle exceptions 
        searchButton.addActionListener(e -> {
            try {
                searchRestaurants();
            } catch (Exception e1) {
                
                e1.printStackTrace();
            }
        });
        mainPanel.add(searchButton);

        // button to ave restaurant 
        JButton saveButton = new JButton("Save Restaurant");
        saveButton.addActionListener(e -> showSaveDialog());
        mainPanel.add(saveButton);

        // button to view saved restaurants
        JButton viewSavedButton = new JButton("View Saved Restaurants");
        viewSavedButton.addActionListener(e -> showSavedRestaurants());
        mainPanel.add(viewSavedButton);

        
        // Results panel with all information on the restaurants
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(new Color(250, 250, 250));
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        //ability to scroll through the results
        mainPanel.add(scrollPane);
    }

    private void showSaveDialog() {
    JTextField nameField = new JTextField();
    JTextField addressField = new JTextField();
    JTextField cuisineField = new JTextField();

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Restaurant Name:"));
    panel.add(nameField);
    panel.add(new JLabel("Address:"));
    panel.add(addressField);
    panel.add(new JLabel("Cuisine:"));
    panel.add(cuisineField);

    int result = JOptionPane.showConfirmDialog(this, panel, "Save Restaurant",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        //if the user confirms, it will save the restaurant information
    if (result == JOptionPane.OK_OPTION) {
        String entry = "Name: " + nameField.getText().trim() + "\n" +
                       "Address: " + addressField.getText().trim() + "\n" +
                       "Cuisine: " + cuisineField.getText().trim();
        savedRestaurants.add(entry);
        saveRestaurantToFile(entry);
        JOptionPane.showMessageDialog(this, "Restaurant saved!");
    }
    }
    //if the user wants to view saved restaurants, it will display them in a text area but if there
    //are no saved restaurants, it will say no saved restaurants
    private void showSavedRestaurants() {
        if (savedRestaurants.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No saved restaurants.");
            return;
        }
        //creates a text area to show the saved restaurants
        JTextArea textArea = new JTextArea();
        for (String entry : savedRestaurants) {
            textArea.append(entry + "\n\n");
        }
        textArea.setEditable(false);
        textArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Saved Restaurants", JOptionPane.INFORMATION_MESSAGE);
    }
    //uses buffered writer to save the restaurant information to a text file line by line
    private void saveRestaurantToFile(String entry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE, true))) {
            writer.write(entry);
            writer.write("\n===\n"); 
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save to file.");
            e.printStackTrace();
        }
    }
    
    private void loadSavedRestaurantsFromFile() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            // Reads the file line by line and adds the restaurant to the savedRestaurants list if the user chooses to//
            while ((line = reader.readLine()) != null) {
                // If the line is "===", it means the end of a restaurant entry so the program knows to stop
                if (line.equals("===")) {
                    savedRestaurants.add(sb.toString().trim());
                    sb.setLength(0);
                } else {
                    sb.append(line).append("\n");
                }
            }
            if (sb.length() > 0) {
                savedRestaurants.add(sb.toString().trim());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load saved restaurants.");
            e.printStackTrace();
        }
    }
    //the most important method of the program to search restaurants based on the user input//
    private void searchRestaurants() throws Exception {
        String topRestaurant = "";
        double highestRating = -1;
        resultPanel.removeAll(); 
        int count = 1;
        String cuisine = (String)cuisineBox.getSelectedItem();
        double lat = Double.parseDouble(latField.getText());
        double lon = Double.parseDouble(lonField.getText());
        // URL for the Google Places API to find restaurants based on the user input
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
            "?location=" + lat + "," + lon + 
            "&radius=" + RADIUS + 
            "&type=restaurant" + 
            "&keyword=" + cuisine + 
            "&key=" + API_KEY;
        //uses the Apis class to get the data from the URL
        String jsonResponse = Apis.getData(url);
        JSONObject response = new JSONObject(jsonResponse);
        JSONArray restaurants = response.getJSONArray("results");
        
        for (int i = 0; i < Math.min(3, restaurants.length()); i++) {
            //iterates through the JSON array of restaurants and gets the information for each restaurant
            JSONObject restaurant = restaurants.getJSONObject(i);
            String name = restaurant.getString("name");
            double rating = restaurant.optDouble("rating", 0);
            if(rating > highestRating) {
                highestRating = rating;
                topRestaurant = name;
            }
            String address = restaurant.optString("vicinity", "Address not available");
            //creates a panel for each restaurant with the information
            JPanel restaurantPanel = new JPanel();
            restaurantPanel.setLayout(new BoxLayout(restaurantPanel, BoxLayout.Y_AXIS));
            restaurantPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            restaurantPanel.add(new JLabel(count + "."));
            restaurantPanel.add(new JLabel(name));
            restaurantPanel.add(new JLabel("Rating: " + rating));
            restaurantPanel.add(new JLabel("Address: " + address));
            
            //creates a URL for the restaurant photo if it exists
             if (restaurant.has("photos")) {
                 JSONArray photos = restaurant.getJSONArray("photos");
                 String photoRef = photos.getJSONObject(0).getString("photo_reference");
                 String photoUrl = "https://maps.googleapis.com/maps/api/place/photo" +
                     "?maxwidth=400&photoreference=" + photoRef + "&key=" + API_KEY;
                
                //uses a thread to prevent crashing since photos take time to load
                 new Thread(() -> {
                     try {
                         ImageIcon icon = new ImageIcon(new URL(photoUrl));
                         JLabel imageLabel = new JLabel(icon);
                         restaurantPanel.add(imageLabel);
                         restaurantPanel.revalidate();
                         //catches exceptions if the image does not load
                     } catch (Exception e) {
                         restaurantPanel.add(new JLabel("(No image available)"));
                     }
                 }).start();
             } else {
                 restaurantPanel.add(new JLabel("(No image available)"));
             }
            
             resultPanel.add(restaurantPanel);
             //uses a counter to keep track of the number of restaurants displayed
             count++;
         }
            if (count == 1) {
                resultPanel.add(new JLabel("No matching restaurants found."));
            } else {
            //the statistics function that displays the top restaurant and its rating by sorting through the rating
                resultPanel.add(new JLabel("Top Restaurant: " + topRestaurant + " with rating " + highestRating));
            }
        
         resultPanel.revalidate();
         resultPanel.repaint();
     }
    public static void main(String[] args) {
        //main method that creates the GUI and makes all components visible
        RestaurantGUI viewer = new RestaurantGUI();
        viewer.setVisible(true);
    }
}