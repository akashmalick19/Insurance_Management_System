package com.example.demoInsuranceManagementPlatform.Controller;

import com.example.demoInsuranceManagementPlatform.Model.Client;
import com.example.demoInsuranceManagementPlatform.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Profile(value = {"test","prod"})
@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;



    @PostMapping(value = "/POST/api/clients")
    public Map<String,Object> createNewClientData(@Valid @RequestBody Client client,HttpServletRequest request){
        return clientService.storeClientData(client,request);
    }

    @GetMapping(value = "GET/api/clients")
    public Map<String,Object> fetchAllClientsData(HttpServletRequest request){
        return clientService.fetchAllData(request);
    }

    @GetMapping(value = "GET/api/clients/{id}")
    public Map<String,Object> fetchSpecificClientById(@PathVariable("id") Long id,HttpServletRequest request){
        return clientService.fetchSpecificClient(id,request);
    }

    @PutMapping(value = "PUT/api/clients/{id}")
    public Map<String,Object> updateSpecificClientById(@PathVariable("id") Long id,@RequestBody Client client,HttpServletRequest request){
        return clientService.updateSpecificClient(id,client,request);
    }

    @DeleteMapping(value = "DELETE/api/clients/{id}")
    public Map<String,Object> deleteSpecificClientById(@PathVariable("id") Long id,HttpServletRequest request){
        return clientService.deleteSpecificClient(id,request);
    }
}
