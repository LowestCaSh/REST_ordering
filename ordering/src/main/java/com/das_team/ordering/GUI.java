package com.das_team.ordering;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class GUI {
    private JFrame frame;
    private JPanel dataPanel;

    public GUI() {
        frame = new JFrame("GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });

        // Create panel to hold the tiles
        dataPanel = new JPanel(new GridLayout(0, 4, 10, 10)); // 4 columns (1 for each group), 10px horizontal and vertical gap

        // Create a scroll pane to hold the data panel
        JScrollPane scrollPane = new JScrollPane(dataPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(20);

        // Add components to frame
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(refreshButton, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Set frame to fullscreen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Set frame visibility
        frame.setVisible(true);
    }

    private void refreshData() {
        // Clear existing tiles
        dataPanel.removeAll();

        // Execute multiple GET requests with different URLs
        executeGetRequest("http://192.168.0.102:8080/carts/1");
        executeGetRequest("http://192.168.0.101:8000/customers");
        executeGetRequest("http://192.168.0.100:8000/v2/products/645c9ffb7d433216f16d7c87");
        executeGetRequest("http://192.168.0.107:404/api/skills");

        // Refresh the panel
        dataPanel.revalidate();
        dataPanel.repaint();
    }

    private void executeGetRequest(String urlStr) {
    	    try {
    	        // Make GET request to specified URL
    	        URL url = new URL(urlStr);
    	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    	        connection.setRequestMethod("GET");
    	        
    	        // Set a default timeout of 1 second (1000 milliseconds)
    	        connection.setConnectTimeout(10);

    	        // Get the HTTP response code
    	        int responseCode = connection.getResponseCode();

    	     // Read response
    	        BufferedReader reader;
    	        if (responseCode >= 200 && responseCode < 300) {
    	            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    	        } else {
    	            InputStream errorStream = connection.getErrorStream();
    	            if (errorStream != null) {
    	                reader = new BufferedReader(new InputStreamReader(errorStream));
    	            } else {
    	                // Handle the case when the error stream is null
    	                reader = new BufferedReader(new StringReader("Error stream is null"));
    	            }
    	        }

    	        StringBuilder response = new StringBuilder();
    	        String line;
    	        while ((line = reader.readLine()) != null) {
    	            response.append(line);
    	        }
    	        reader.close();

    	        if (responseCode >= 200 && responseCode < 300) {
    	            // Parse JSON response
    	            ObjectMapper objectMapper = new ObjectMapper();
    	            JsonNode jsonNode = objectMapper.readTree(response.toString());

    	            // Format the JSON object for display
    	            String formattedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);

    	            // Create a tile to hold the data
    	            JTextArea tileTextArea = new JTextArea();
    	            tileTextArea.setText("Executed Request: " + urlStr + "\n\n" + formattedJson);
    	            tileTextArea.setEditable(false);
    	            tileTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));

    	            // Create a panel to hold the tile with a custom border
    	            JPanel tilePanel = new JPanel(new BorderLayout());
    	            tilePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	            tilePanel.add(tileTextArea, BorderLayout.CENTER);

    	            // Add the tile panel to the data panel
    	            dataPanel.add(tilePanel);
    	        } else {
    	            // Create a tile to show the error code
    	            JTextArea errorTileTextArea = new JTextArea();
    	            errorTileTextArea.setText("Executed Request: " + urlStr + "\n\n" + "Response Code: " + responseCode);
    	            errorTileTextArea.setEditable(false);
    	            errorTileTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));

    	            JPanel errorTilePanel = new JPanel(new BorderLayout());
    	            errorTilePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	            errorTilePanel.add(errorTileTextArea, BorderLayout.CENTER);

    	            dataPanel.add(errorTilePanel);
    	        }
    	    } catch (SocketTimeoutException e) {
    	        e.printStackTrace();
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