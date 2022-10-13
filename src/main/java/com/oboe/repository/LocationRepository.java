package com.oboe.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.oboe.dto.ScanLocations;
import com.oboe.dto.User;

@Repository
public interface LocationRepository extends CassandraRepository<ScanLocations, String> {

}
