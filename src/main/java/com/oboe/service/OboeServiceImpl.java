package com.oboe.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oboe.dto.ScanLocations;
import com.oboe.dto.ScanResults;
import com.oboe.dto.SearchFieldsDTO;
import com.oboe.repository.LocationRepository;
import com.oboe.repository.ScanResultsRepository;
import com.oboe.util.DataNotFoundException;
import com.oboe.util.InvalidDataException;
import com.oboe.util.OboeServiceConstants;
import com.oboe.util.OboeServiceException;

@Service("OboeServiceImpl")
public class OboeServiceImpl implements OboeService {
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private ScanResultsRepository scanResultsRepository;
	
	@Value("${api.key}")
	private String API_KEY;
	
	private static final HttpHeaders HTTP_RESPONSE_HEADERS = new HttpHeaders();

	@Override
	public ScanLocations createLocation(ScanLocations location) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newHttpClient();
		String query = location.getName();
		
		if(null==location || location.getLocation().equals(""))
			throw new InvalidDataException("Invalid Data exception");
		
        String encodedQuery = URLEncoder.encode(query,"UTF-8");
        String requestUri = OboeServiceConstants.GEOCODING_RESOURCE + "?apiKey=" + API_KEY + "&q=" + encodedQuery;

        HttpRequest geocodingRequest = HttpRequest.newBuilder().GET().uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(2000)).build();
		
        HttpResponse<?> geocodingResponse = httpClient.send(geocodingRequest,
                HttpResponse.BodyHandlers.ofString());

        String response =(String) geocodingResponse.body();
        
        ObjectMapper mapper = new ObjectMapper();
       
        JsonNode responseJsonNode = mapper.readTree(response);

        JsonNode items = responseJsonNode.get("items");
        
        items.isEmpty();
        
        ScanLocations locationDetails = null;
        try {
	        if(!items.isEmpty()) {
		        for (JsonNode item : items) {
		            JsonNode address = item.get("address");
		            String label = address.get("label").asText();
		            JsonNode position = item.get("position");
		
		            String lat = position.get("lat").asText();
		            String lng = position.get("lng").asText();
		            locationDetails = new ScanLocations(lat+","+lng, label);
		        }
		        locationRepository.save(locationDetails);
	        }
        	
        }catch (Exception e) {
        	throw new OboeServiceException(OboeServiceConstants.TECHNICAL_ERROR);
		}
        
        return locationDetails;
	}

	@Override
	public List<ScanLocations> getScanLocations() {
		
		List<ScanLocations> scanLocations = locationRepository.findAll();
		
		if(null == scanLocations || scanLocations.isEmpty()) {
			throw new DataNotFoundException(OboeServiceConstants.DATA_NOT_FOUND);
		}
		return scanLocations;
	}

	@Override
	public ScanResults createScanResults(ScanResults scanResults)
			throws IOException, InterruptedException {
		
		if(null!= scanResults) {
			List<String> position = Arrays.asList(scanResults.getLocation().split(","));
			validateLocationCoordinates(position);
		}
		ScanResults results = null;
		try {
			results = scanResultsRepository.save(scanResults);
		}catch(OboeServiceException ex) {
			
			throw new OboeServiceException(OboeServiceConstants.TECHNICAL_ERROR);
		}
		
		return results;
	}

	@Override
	public Optional<ScanResults> readScanResults(SearchFieldsDTO searchFields) {
		
		Optional<ScanResults> scanResults = scanResultsRepository.findBylocationAndDayOfScan(searchFields.getLocation(),
				searchFields.getDate());

		if (null == scanResults || scanResults.isEmpty()) {
			throw new DataNotFoundException(OboeServiceConstants.DATA_NOT_FOUND);
		}
		return scanResults;
	}
	
	private void validateLocationCoordinates (List<String> position)
	{
		if (position == null)
			throw new InvalidDataException("Positions in a point cannot be null");
		if (position.size() != 2)
			throw new InvalidDataException("A point representing a longitude and latitude must contain 2 numbers");
		try {
			double longitude = Double.valueOf(position.get(0));
			if (longitude > 180 || longitude < -180)
				throw new InvalidDataException("The longitude of a point must be between -180 and 180");
			double latitude = Double.valueOf(position.get(1));
			if (latitude > 90 || latitude < -90)
				throw new InvalidDataException("The latitude of a point must be between -90 and 90");
		} catch (Exception e) {
			throw new InvalidDataException("Invalid data exception.");
		}
	} 

}
