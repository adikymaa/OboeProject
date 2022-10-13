package com.oboe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.oboe.dto.ScanResults;
import com.oboe.dto.SearchFieldsDTO;

@Repository
public interface ScanResultsRepository extends CassandraRepository<ScanResults, String> {
	
	@AllowFiltering
	Optional<ScanResults> findBylocationAndDayOfScan(final String location, final String dayOfScan);
}
