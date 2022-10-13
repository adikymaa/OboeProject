package com.oboe.service;

import static org.mockito.ArgumentMatchers.anyString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oboe.dto.ScanLocations;
import com.oboe.dto.ScanResults;
import com.oboe.dto.SearchFieldsDTO;
import com.oboe.repository.LocationRepository;
import com.oboe.repository.ScanResultsRepository;

@RunWith(MockitoJUnitRunner.class)
public class OboeServiceImplTest {
	
	@InjectMocks
	OboeServiceImpl oboeServiceImpl;
	
	@Mock
	private LocationRepository locationRepository;
	
	@Mock
	private ScanResultsRepository scanResultsRepository;
	
	@Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
       
    }

	@SuppressWarnings("unchecked")
	@Test
	public void getScanLocations_Test() {
		
		List<ScanLocations> scanLocations = new ArrayList<>();
		ScanLocations loc1 = new ScanLocations("17.41,78.44", " 2nd Cross Road, Osmania University Teachers Colony");
		ScanLocations loc2 = new ScanLocations("17.41,72.44", " Star Hospitals, Aparna's Chandradeep, Road No 10");
		
		scanLocations.add(loc1);
		scanLocations.add(loc2);
		
		Mockito.when(locationRepository.findAll()).thenReturn(scanLocations);
		
		List<ScanLocations> locations = oboeServiceImpl.getScanLocations();
		Assert.assertNotNull(locations);
		Assert.assertEquals(2, locations.size());
	}
	
	@Test(expected = com.oboe.util.DataNotFoundException.class)
	public void readScanResults_Exception_Test() {
		
		SearchFieldsDTO searchFields = new SearchFieldsDTO();
		List<ScanResults> results = new ArrayList<>();
		oboeServiceImpl.readScanResults(searchFields);
		
	}
	
	public void readScanResults_Test() {
		
		SearchFieldsDTO searchFields = new SearchFieldsDTO();
		//List<ScanResults> results = new ArrayList<>();
		ScanResults ScanResults = new ScanResults("24,25","2024-19-10","sdfd-ewre","common loon","swim and fly");
		Optional<com.oboe.dto.ScanResults> results = Optional.ofNullable(ScanResults);
		
		Mockito.when(scanResultsRepository.findBylocationAndDayOfScan(anyString(), anyString())).thenReturn(results);
		
		Optional<com.oboe.dto.ScanResults> readScanResults = oboeServiceImpl.readScanResults(searchFields);
		Assert.assertNotNull(readScanResults);
	}
	
}
