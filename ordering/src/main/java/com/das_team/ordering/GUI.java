package com.das_team.ordering;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GUI {
    private JFrame frame;
    private JTextArea dataTextArea;

    public GUI() {
        frame = new JFrame("GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	dataTextArea.setText(null);
                refreshData();
            }
        });

        // Create a text area to display the data
        dataTextArea = new JTextArea();
        dataTextArea.setEditable(false);

        // Add components to the frame
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(refreshButton, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(dataTextArea), BorderLayout.CENTER);

        // Set frame to fullscreen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Set frame visibility
        frame.setVisible(true);
    }

    private void refreshData() {
        // Execute multiple GET requests with different URLs
        executeGetRequest("http://localhost:8080/carts");
        executeGetRequest("http://localhost:8080/carts/1/details");
        executeGetRequest("http://localhost:8080/orders/2");
    }

    private void executeGetRequest(String urlStr) {
        try {
            // Make GET request to specified URL
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.toString());

            // Format the JSON object for display
            String formattedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);

            // Append the formatted JSON to the text area
            dataTextArea.append("Executed Request: " + urlStr + "\n\n");
            dataTextArea.append(formattedJson);
            dataTextArea.append("\n\n--------------------\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI();
            }
        });
    }
}