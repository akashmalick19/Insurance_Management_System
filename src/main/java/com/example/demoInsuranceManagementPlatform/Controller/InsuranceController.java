package com.example.demoInsuranceManagementPlatform.Controller;

import com.example.demoInsuranceManagementPlatform.Model.InsurancePolicy;
import com.example.demoInsuranceManagementPlatform.Service.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Profile(value = {"test","prod"})
@RestController
public class InsuranceController {

    @Autowired
    private InsuranceService insuranceService;

    @PostMapping(value = "POST/api/policies")
    public Map<String,Object> createNewInsurancePolicy(@Valid @RequestBody List<InsurancePolicy> insurancePolicy, HttpServletRequest request){
        return insuranceService.storeInsuranceData(insurancePolicy,request);
    }


    @GetMapping(value = "GET/api/policies")
    public Map<String,Object> fetchAllInsuranceData(HttpServletRequest request){
        return insuranceService.fetchAllData(request);
    }

    @GetMapping(value = "GET/api/policies/{id}")
    public Map<String,Object> fetchSpecificInsuranceById(@PathVariable("id")Long id,HttpServletRequest request){
        return insuranceService.fetchSpecificInsurance(id,request);
    }
    @PutMapping(value = "PUT/api/policies/{id}")
    public Map<String,Object> updateSpecificInsuranceById(@PathVariable("id")Long id,@RequestBody InsurancePolicy insurancePolicy,HttpServletRequest request){
        return insuranceService.updateSpecificInsurance(id,insurancePolicy,request);
    }

    @DeleteMapping(value = "DELETE/api/policies/{id}")
    public Map<String,Object> deleteSpecificInsuranceById(@PathVariable("id")Long id,HttpServletRequest request){
        return insuranceService.deleteSpecificInsurance(id,request);
    }
}
