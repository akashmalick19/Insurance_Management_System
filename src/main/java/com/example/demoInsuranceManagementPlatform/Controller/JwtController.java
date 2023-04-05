package com.example.demoInsuranceManagementPlatform.Controller;

import com.example.demoInsuranceManagementPlatform.JwtDto.JwtRequest;
import com.example.demoInsuranceManagementPlatform.JwtDto.JwtResponse;
import com.example.demoInsuranceManagementPlatform.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Profile(value = {"test","prod"})
@RestController
@CrossOrigin
public class JwtController {
    @Autowired
    private JwtService jwtService;

    @PostMapping({"/createJwtToken"})
    public Map<String,Object> createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            JwtResponse jwtResponse = jwtService.createJwtToken(jwtRequest);
            map.put("Object", jwtResponse);
        }
        catch (Exception e) {
            map.put("Timestamp", new Date());
            map.put("Status", HttpStatus.BAD_REQUEST.value());
            map.put("Message", "Client Email OR Password is incorrect !");
        }
        return map;
    }
}