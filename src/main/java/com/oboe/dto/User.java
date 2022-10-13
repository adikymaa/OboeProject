package com.oboe.dto;

import java.io.Serializable;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@Table("user")
@AllArgsConstructor
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
//	@PrimaryKey
	private int id;
	private  String name;
	private String address;
	private int age;

}
