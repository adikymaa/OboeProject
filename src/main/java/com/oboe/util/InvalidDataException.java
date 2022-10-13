package com.oboe.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidDataException extends RuntimeException {
	
	public InvalidDataException(String message) {
		super(message);
	}

}
