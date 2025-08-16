package com.zip4j.gip.by.annotation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class Controller {

    @GetMapping("/api")
    @GzipResponse
    public ResponseEntity<?> testGet(){
        return new ResponseEntity<>( Map.of("key", "value"), HttpStatus.OK);
    }

    @GetMapping("/api2")
    public ResponseEntity<?> testGet2(){
        return new ResponseEntity<>( Map.of("key", "value"), HttpStatus.OK);
    }

    @GetMapping("/api3")
    @GzipResponse(minResponseSize = 2048)
    public ResponseEntity<?> testGet3(){
        return new ResponseEntity<>( "long info", HttpStatus.OK);
    }
    @GetMapping("/api4")
    public ResponseEntity<?> testGet4(){
        return new ResponseEntity<>( "long info", HttpStatus.OK);
    }
}
