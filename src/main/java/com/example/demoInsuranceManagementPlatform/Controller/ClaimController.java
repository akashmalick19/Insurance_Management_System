package com.example.demoInsuranceManagementPlatform.Controller;

import com.example.demoInsuranceManagementPlatform.Model.Claim;
import com.example.demoInsuranceManagementPlatform.Service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Profile(value = {"test","prod"})
@RestController
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @PostMapping(value = "POST/api/claims/{policyNumber}")
    public Map<String,Object> createNewClaim(@RequestBody Claim claim,@PathVariable("policyNumber") Long InsuranceId,HttpServletRequest request){
        return claimService.storeClaimData(claim,InsuranceId,request);
    }


    @GetMapping(value = "GET/api/claims")
    public Map<String,Object> fetchAllClaimData(HttpServletRequest request){
        return claimService.fetchAllData(request);
    }

    @GetMapping(value = "GET/api/claims/{id}")
    public Map<String,Object> fetchSpecificClaimById(@PathVariable("id")Long id, HttpServletRequest request){
        return claimService.fetchSpecificClaim(id,request);
    }
    @PutMapping(value = "PUT/api/claims/{id}")
    public Map<String,Object> updateSpecificClaimById(@PathVariable("id")Long id,@RequestBody Claim claim,HttpServletRequest request){
        return claimService.updateSpecificClaim(id,claim,request);
    }

    @DeleteMapping(value = "DELETE/api/claims/{id}")
    public Map<String,Object> deleteSpecificClaimById(@PathVariable("id")Long id,HttpServletRequest request){
        return claimService.deleteSpecificClaim(id,request);
    }

}
