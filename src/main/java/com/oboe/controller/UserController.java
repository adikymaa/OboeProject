package com.oboe.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oboe.dto.ScanLocations;
import com.oboe.dto.User;
import com.oboe.repository.LocationRepository;

@RestController
@RequestMapping("/oboe")
public class UserController {
	
	@Autowired
	private LocationRepository userRepo;
	
	private static final String GEOCODING_RESOURCE = "https://geocode.search.hereapi.com/v1/geocode";
    private static final String API_KEY = "LtaPa8tqEKkmMJlpgyFoHies5YoF5nwxER9cL8WPaeg";

	private static final HttpHeaders HTTP_RESPONSE_HEADERS = new HttpHeaders();

	/*
	@PostMapping("/adduser")
	public User adduserEntity(@RequestBody User user) {
		userRepo.save(user);
		return user;
	}
	
	@GetMapping("/allusers")
	public List<User> getAllUser(){
		List<User> users = userRepo.findAll();
		return users;
	}
	
	@PostConstruct
	public void saveUser() {
		
		System.out.println("in @PostConstruct....");
		User user = new User(101,"Adi","Secbad", 40);
		userRepo.save(user);
	}
	
	*/
	
	@GetMapping("/getlocation/result")
	public ResponseEntity<String> getLocationCordinates() throws IOException, InterruptedException {
		
		HttpClient httpClient = HttpClient.newHttpClient();
		String query ="11 Wall St, New York, NY 10005";
        String encodedQuery = URLEncoder.encode(query,"UTF-8");
        String requestUri = GEOCODING_RESOURCE + "?apiKey=" + API_KEY + "&q=" + encodedQuery;

        HttpRequest geocodingRequest = HttpRequest.newBuilder().GET().uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(2000)).build();

        HttpResponse<?> geocodingResponse = httpClient.send(geocodingRequest,
                HttpResponse.BodyHandlers.ofString());

        String response =(String) geocodingResponse.body();
       
       return new ResponseEntity<>(response, HTTP_RESPONSE_HEADERS, HttpStatus.OK);
		
	}
	
	@PostMapping("/save/location/result")
	public void createLocation(@RequestBody ScanLocations location) throws IOException, InterruptedException {
		
		HttpClient httpClient = HttpClient.newHttpClient();
		String query = location.getName();
        String encodedQuery = URLEncoder.encode(query,"UTF-8");
        String requestUri = GEOCODING_RESOURCE + "?apiKey=" + API_KEY + "&q=" + encodedQuery;

        HttpRequest geocodingRequest = HttpRequest.newBuilder().GET().uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(2000)).build();
		
        HttpResponse<?> geocodingResponse = httpClient.send(geocodingRequest,
                HttpResponse.BodyHandlers.ofString());

        String response =(String) geocodingResponse.body();
        
        ObjectMapper mapper = new ObjectMapper();
       
        JsonNode responseJsonNode = mapper.readTree(response);

        JsonNode items = responseJsonNode.get("items");
        
        ScanLocations loc = null;
        for (JsonNode item : items) {
            JsonNode address = item.get("address");
            String label = address.get("label").asText();
            JsonNode position = item.get("position");

            String lat = position.get("lat").asText();
            String lng = position.get("lng").asText();
            System.out.println(label + " is located at " + lat + "," + lng + ".");
            loc = new ScanLocations(lat+","+lng, label);
        }
        
        	userRepo.save(loc);
	}
	
	
}
