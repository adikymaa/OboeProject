package com.oboe.dto;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Table("scan_locations")
@Data
public class ScanLocations {
	
	@PrimaryKey
	private String location;
	private String name;

}
