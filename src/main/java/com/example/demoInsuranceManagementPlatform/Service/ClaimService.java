package com.example.demoInsuranceManagementPlatform.Service;

import com.example.demoInsuranceManagementPlatform.Configuration.JwtRequestFilter;
import com.example.demoInsuranceManagementPlatform.Model.*;
import com.example.demoInsuranceManagementPlatform.Repository.ClaimRepository;
import com.example.demoInsuranceManagementPlatform.Repository.ClientRepository;
import com.example.demoInsuranceManagementPlatform.Repository.InsuranceRepository;
import com.example.demoInsuranceManagementPlatform.Repository.RoleRepository;
import com.example.demoInsuranceManagementPlatform.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class ClaimService {

    @Autowired
    private ClaimRepository claimRepository;
    @Autowired
    private InsuranceRepository insuranceRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    public Map<String, Object> storeClaimData(Claim claim,Long policyNumber,HttpServletRequest request) {
        Map<String, Object> map1 = new LinkedHashMap<>();
        String clientEmail = null;
        String token = jwtRequestFilter.parseJwt(request);
        clientEmail = jwtUtil.getClientEmailFromToken(token);
        try {
            Client client = clientRepository.findByEmail(clientEmail);
            if (insuranceRepository.existsByPolicyNumber(policyNumber)) {
                InsurancePolicy insurancePolicy = insuranceRepository.findByPolicyNumber(policyNumber);
                if (insurancePolicy.getClient().getClientId().equals(client.getClientId())) {
                    try {
                        Claim claim2 = claimRepository.existStatus(insurancePolicy.getClaim().getClaimNumber());
                        if (insurancePolicy.getClaim().getClaimNumber() == null || claim2 != null) {
                            throw new NullPointerException();
                        } else {
                            map1.put("Timestamp", new Date());
                            map1.put("Status", HttpStatus.BAD_REQUEST.value());
                            map1.put("Message", "Insurance Policy Already Claimed.");
                            return map1;
                        }
                    } catch (NullPointerException ex) {
                        Claim claim1 = new Claim();
                        claim1.setClaimDate(claim.getClaimDate());
                        claim1.setDescription(claim.getDescription());
                        claim1.setStatus(claim.getStatus());
                        claim1 = claimRepository.save(claim1);
                        insurancePolicy.setClaim(claim1);
                        insuranceRepository.saveAndFlush(insurancePolicy);
                        map1.put("Timestamp", new Date());
                        map1.put("Status", HttpStatus.OK.value());
                        map1.put("Message", "Claim Request sent Successfully.");
                        map1.put("Object", claim1);
                        return map1;
                    }
                }
            }
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.BAD_REQUEST.value());
            map1.put("Message", "Invalid Policy Id.");
            return map1;
        } catch (NullPointerException q) {
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.BAD_REQUEST.value());
            map1.put("Message", "Client does not Exist !");
            return map1;
        }
    }

    public Map<String, Object> fetchAllData(HttpServletRequest request) {
        Map<String, Object> map1 = new LinkedHashMap<>();
        String clientEmail = null;
        String token = jwtRequestFilter.parseJwt(request);
        clientEmail = jwtUtil.getClientEmailFromToken(token);
        try {
            Client client1 = clientRepository.findByEmail(clientEmail);
            String roleName = "Super_User";
            Client client2 = clientRepository.getRole(client1.getClientId());
            Role role = roleRepository.findRole(roleName);
            for (Role r : client2.getRoleEntitySet()) {
                if (!Objects.equals(r.getRoleName(), role.getRoleName())) {
                    throw new Exception();
                }
            }
        }
        catch (Exception e){
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.BAD_REQUEST.value());
            map1.put("Message", "You are not Authorized to access this resource Contract to Company !");
            return map1;
        }

        List<Claim> claimList= claimRepository.findAllClaimData();
        if (claimList.isEmpty()) {
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.NO_CONTENT.value());
            map1.put("Message", "No Claim request available !");
            map1.put("Object", claimList);
            return map1;
        }
        map1.put("Timestamp", new Date());
        map1.put("Status", HttpStatus.OK.value());
        map1.put("Message", "Successfully fetch.");
        map1.put("Object", claimList);
        return map1;
    }


    public Map<String, Object> fetchSpecificClaim(Long id, HttpServletRequest request) {
        Map<String, Object> map1 = new LinkedHashMap<>();
        String clientEmail = null;
        String token = jwtRequestFilter.parseJwt(request);
        clientEmail = jwtUtil.getClientEmailFromToken(token);
        try{
            Client client = clientRepository.findByEmail(clientEmail);
            if(client!=null){
                try{
                    List<InsurancePolicy> insurancePolicies= insuranceRepository.findInsuranceWithClientId(client.getClientId());
                    if(insurancePolicies!=null){
                        try{
                            Claim claim=claimRepository.findByClaimWithId(id);
                            for(InsurancePolicy i:insurancePolicies){
                                if(i.getClaim().getClaimNumber().equals(claim.getClaimNumber())){
                                    map1.put("Timestamp", new Date());
                                    map1.put("Status", HttpStatus.OK.value());
                                    map1.put("Message", "AXA_"+new Random().nextLong(100000)+claim.getClaimNumber()+" record is found. ");
                                    map1.put("Object", claim);
                                    return map1;
                                }
                            }
                        }
                        catch(NullPointerException b){
                            map1.put("Timestamp", new Date());
                            map1.put("Status", HttpStatus.BAD_REQUEST.value());
                            map1.put("Message", "Claim request not found.");
                            return map1;
                        }
                    }
                    throw new NullPointerException();
                }
                catch(NullPointerException n){
                    map1.put("Timestamp", new Date());
                    map1.put("Status", HttpStatus.BAD_REQUEST.value());
                    map1.put("Message", "Insurance does not exist.");
                    return map1;
                }
            }

        }
        catch (NullPointerException l){
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.BAD_REQUEST.value());
            map1.put("Message", "Client does not Exist !");
        }
        return map1;
    }

    public Map<String, Object> updateSpecificClaim(Long id, Claim claim, HttpServletRequest request) {
        Map<String, Object> map1 = new LinkedHashMap<>();
        String clientEmail = null;
        String token = jwtRequestFilter.parseJwt(request);
        clientEmail = jwtUtil.getClientEmailFromToken(token);
        try{
            Client client = clientRepository.findByEmail(clientEmail);
            if(client!=null){
                try{
                    List<InsurancePolicy> insurancePolicies= insuranceRepository.findInsuranceWithClientId(client.getClientId());
                    if(insurancePolicies!=null){
                        try{
                            Claim claimUpdate=claimRepository.findById(id).orElse(null);
                            for(InsurancePolicy i:insurancePolicies){
                                if(i.getClaim().getClaimNumber().equals(claimUpdate.getClaimNumber())){
                                    claimUpdate.setClaimDate(claim.getClaimDate());
                                    claimUpdate.setStatus(claim.getStatus());
                                    claimUpdate.setDescription(claim.getDescription());
                                    map1.put("Timestamp", new Date());
                                    map1.put("Status", HttpStatus.OK.value());
                                    map1.put("Message", "Updated Successfully.");
                                    map1.put("Object", claimRepository.save(claimUpdate));
                                    return map1;
                                }
                            }
                        }
                        catch(NullPointerException b){
                            map1.put("Timestamp", new Date());
                            map1.put("Status", HttpStatus.BAD_REQUEST.value());
                            map1.put("Message", "Invalid Claim !");
                            return map1;
                        }
                    }
                    throw new NullPointerException();
                }
                catch(NullPointerException n){
                    map1.put("Timestamp", new Date());
                    map1.put("Status", HttpStatus.BAD_REQUEST.value());
                    map1.put("Message", "Insurance does not exist.");
                    return map1;
                }
            }
        }
        catch (NullPointerException l){
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.BAD_REQUEST.value());
            map1.put("Message", "Client does not Exist !");
        }
        return map1;
    }

    public Map<String, Object> deleteSpecificClaim(Long id, HttpServletRequest request) {
        Map<String, Object> map1 = new LinkedHashMap<>();
        String clientEmail = null;
        String token = jwtRequestFilter.parseJwt(request);
        clientEmail = jwtUtil.getClientEmailFromToken(token);
        try{
            Client client = clientRepository.findByEmail(clientEmail);
            if(client!=null){
                try{
                    List<InsurancePolicy> insurancePolicies= insuranceRepository.findInsuranceWithClientId(client.getClientId());
                    if(insurancePolicies!=null){
                        try{
                            Claim claimDelete=claimRepository.findById(id).orElse(null);
                            for(InsurancePolicy i:insurancePolicies){
                                if(i.getClaim().getClaimNumber().equals(claimDelete.getClaimNumber())){
                                    claimDelete.setStatus(ClaimStatus.DeActive);
                                    map1.put("Timestamp", new Date());
                                    map1.put("Status", HttpStatus.OK.value());
                                    map1.put("Message", "Deleted Successfully.");
                                    map1.put("Object", claimRepository.save(claimDelete));
                                    return map1;
                                }
                            }
                        }
                        catch(NullPointerException b){
                            map1.put("Timestamp", new Date());
                            map1.put("Status", HttpStatus.BAD_REQUEST.value());
                            map1.put("Message", "Invalid Claim !");
                            return map1;
                        }
                    }
                    throw new NullPointerException();
                }
                catch(NullPointerException n){
                    map1.put("Timestamp", new Date());
                    map1.put("Status", HttpStatus.BAD_REQUEST.value());
                    map1.put("Message", "Insurance does not exist.");
                    return map1;
                }
            }
        }
        catch (NullPointerException l){
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.BAD_REQUEST.value());
            map1.put("Message", "Client does not Exist !");
        }
        return map1;
    }
}
