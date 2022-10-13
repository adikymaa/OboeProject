package com.oboe.dto;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Table("scan_results")
@Data
@AllArgsConstructor
public class ScanResults {
	
	@PrimaryKey
	private String location;
	private String dayOfScan;
	private String birdId;
	private String birdSpecies;
	private String birdTraits;

}
