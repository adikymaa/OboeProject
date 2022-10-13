package com.oboe.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestBody;

import com.oboe.dto.ScanLocations;
import com.oboe.dto.ScanResults;
import com.oboe.dto.SearchFieldsDTO;

public interface OboeService {
	
	public ScanLocations createLocation(@RequestBody ScanLocations location) throws IOException, InterruptedException;
	
	public List<ScanLocations> getScanLocations(); 
	
	public ScanResults createScanResults(@RequestBody ScanResults scanResults) throws IOException, InterruptedException;
	
	public Optional<ScanResults> readScanResults(@RequestBody SearchFieldsDTO searchFields); 

}
