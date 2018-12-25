package org.devxprime.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class WebController {
	
	@RequestMapping(value="/home")
	public ResponseEntity<Object> home() {
		return new ResponseEntity<>("Hello from Spring App", HttpStatus.OK);
	}
	
	@RequestMapping(value="/post", method = RequestMethod.POST)
	public ResponseEntity<Object> postReq() {
		return new ResponseEntity<>("Hello from Spring App Post data", HttpStatus.OK);
	}
}
