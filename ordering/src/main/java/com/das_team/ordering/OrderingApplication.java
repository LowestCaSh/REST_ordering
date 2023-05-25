package com.das_team.ordering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
public class OrderingApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(OrderingApplication.class, args);
		
		
		
		//POST to another Group
		
		String requestBody = "{\"name\":\"Das coole Produkt\",\"brand\":\"Boss\",\"color\":\"Coool\",\"size\":\"Gigantisch\",\"weight\":99.99,\"description\":\"riesig\",\"category\":\"Nicht nötig\",\"prices\":[{\"validFrom\":\"07.05.2021\",\"validTo\":\"07.05.2022\",\"price\":\"902.00\"}],\"discounted\":true,\"discountAmount\":20,\"stock\":117,\"packagingSize\":\"\",\"packagingWeight\":\"Gewicht7\",\"discontinued\":false,\"modelnumber\":\"54r12opklhdhl5ug\"}"; // Customize the request body here
	    
		String url = "http://192.168.0.100:8000/v2/products/create";
		try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes());
            outputStream.flush();

            int statusCode = connection.getResponseCode();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            // Process the response as needed
            System.out.println("Response Code: " + statusCode);
            System.out.println("Response Body: " + response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

	
	}
}