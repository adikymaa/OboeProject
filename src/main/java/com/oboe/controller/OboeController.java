package com.oboe.controller;

import com.oboe.util.OboeServiceConstants;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oboe.dto.ScanLocations;
import com.oboe.dto.ScanResults;
import com.oboe.dto.SearchFieldsDTO;
import com.oboe.service.OboeService;
import com.oboe.util.LogginConstants;

@RestController
@RequestMapping(OboeServiceConstants.OBOE_CONTROLLER_REQUEST_MAPPING)
public class OboeController {

	@Autowired
	private OboeService oboeService;
	
	@Value("${api.key}")
	private String API_KEY;
	
	private static final HttpHeaders HTTP_RESPONSE_HEADERS = new HttpHeaders();
	Logger LOGGER = LoggerFactory.getLogger(OboeController.class); 
	
	
	/**
	 * @api {post} /oboe/save/location
	 * @param location
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@PostMapping(OboeServiceConstants.CREATE_LOCATIONS_REQUEST_MAPPING)
	public ResponseEntity<ScanLocations> createLocation(@RequestBody ScanLocations location) throws IOException, InterruptedException {
		
		LOGGER.info(LogginConstants.MESSAGE + "Create location details", LogginConstants.LOCATION_DETAILS + location);
		
		ScanLocations scanlocation = oboeService.createLocation(location);
		
		return new ResponseEntity<>(scanlocation, HTTP_RESPONSE_HEADERS, HttpStatus.OK);
	}
	
	/**
	 * @api {get} /oboe//scan/locations
	 * @return
	 */
	@GetMapping(OboeServiceConstants.READ_LOCATIONS_REQUEST_MAPPING)
	public ResponseEntity<List> getScanLocations() {
		
		LOGGER.info(LogginConstants.MESSAGE + "Read scanned location details");	
		
		List scanLocations = oboeService.getScanLocations();
		
		return new ResponseEntity<>(scanLocations, HTTP_RESPONSE_HEADERS, HttpStatus.OK);
	}
	
	/**
	 * @api {post} /oboe/save/Results
	 * @param scanResults
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@PostMapping(OboeServiceConstants.CREATE_SCAN_RESULTS_REQUEST_MAPPING)
	public ResponseEntity<ScanResults> createScanResults(@RequestBody ScanResults scanResults) throws IOException, InterruptedException {
        
		LOGGER.info(LogginConstants.MESSAGE + "Create Scanned Results details", LogginConstants.SCANNED_REQUEST_DETAILS + scanResults);
		
		ScanResults scanResult = oboeService.createScanResults(scanResults);
		
		return new ResponseEntity<>(scanResult, HTTP_RESPONSE_HEADERS, HttpStatus.OK);
	}
	
	/**
	 *  @api {post} /oboe/read/Results
	 * @param searchFields
	 * @return
	 */
	@PostMapping(OboeServiceConstants.READ_SCAN_RESULTS_REQUEST_MAPPING)
	public ResponseEntity<Optional<ScanResults>> readScanResults(@RequestBody SearchFieldsDTO searchFields) {
		
		LOGGER.info(LogginConstants.MESSAGE + "Read Scanned Results details");
		
		Optional<ScanResults> scanResults = oboeService.readScanResults(searchFields);
		
		return new ResponseEntity<>(scanResults, HTTP_RESPONSE_HEADERS, HttpStatus.OK);
	}
	
}
