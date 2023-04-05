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
public class InsuranceService {

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

    public Map<String, Object> storeInsuranceData(List<InsurancePolicy> insurancePolicy, HttpServletRequest request) {
        Map<String, Object> map1 = new LinkedHashMap<>();
        String clientEmail = null;
        String token = jwtRequestFilter.parseJwt(request);
        clientEmail = jwtUtil.getClientEmailFromToken(token);
        Client client = clientRepository.findByEmail(clientEmail);
        try {
            if (client == null) {
                throw new NullPointerException();
            }
            List<InsurancePolicy> insurancePolicy1 = new ArrayList<>();
            for (InsurancePolicy i : insurancePolicy) {
                i.setClient(client);
                insurancePolicy1.add(i);
            }
            List<InsurancePolicy> insurancePolicy2 = insuranceRepository.saveAll(insurancePolicy1);
            client.setInsurancePolicySet(insurancePolicy);
            clientRepository.save(client);
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.OK.value());
            map1.put("Message", "Insurance Policy assigned Successfully.");
            map1.put("Object", insurancePolicy2);
            return map1;
        }
        catch(NullPointerException e){
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
        List<InsurancePolicy> insurancePolicieList = insuranceRepository.findAllInsuranceData();
        if (insurancePolicieList.isEmpty()) {
                map1.put("Timestamp", new Date());
                map1.put("Status", HttpStatus.NO_CONTENT.value());
                map1.put("Message", "No Insurance Policy Available !");
                map1.put("Object", insurancePolicieList);
                return map1;
        }
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.OK.value());
            map1.put("Message", "Successfully fetch.");
            map1.put("Object", insurancePolicieList);
            return map1;

    }

    public Map<String, Object> fetchSpecificInsurance(Long id, HttpServletRequest request) {
        Map<String, Object> map1 = new LinkedHashMap<>();
        String clientEmail = null;
        String token = jwtRequestFilter.parseJwt(request);
        clientEmail = jwtUtil.getClientEmailFromToken(token);
        try {
            Client client = clientRepository.findByEmail(clientEmail);
            if (client != null) {
                try {
                    InsurancePolicy insurancePolicy = insuranceRepository.findInsuranceWithIdAndClientId(client.getClientId(),id);
                    if (insurancePolicy != null) {
                        map1.put("Timestamp", new Date());
                        map1.put("Status", HttpStatus.OK.value());
                        map1.put("Message", client.getClientName()+" Insurance records.");
                        map1.put("Object", insurancePolicy);
                         return map1;
                    }
                    throw new NullPointerException();
                } catch (NullPointerException n) {
                    map1.put("Timestamp", new Date());
                    map1.put("Status", HttpStatus.BAD_REQUEST.value());
                    map1.put("Message", "No Insurance record attached to AXA_POLICY_"+new Random().nextLong(100000)+id+" policy number.");
                    return map1;
                }
            }
        }
        catch (NullPointerException n1) {
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.BAD_REQUEST.value());
            map1.put("Message", "Client does not Exist !");
        }
        return map1;
    }

    public Map<String, Object> updateSpecificInsurance(Long id, InsurancePolicy insurancePolicy, HttpServletRequest request) {
        Map<String, Object> map1 = new LinkedHashMap<>();
        String clientEmail = null;
        String token = jwtRequestFilter.parseJwt(request);
        clientEmail = jwtUtil.getClientEmailFromToken(token);
        try {
            Client client = clientRepository.findByEmail(clientEmail);
            if(client!=null){
                try{
                    InsurancePolicy insurancePolicy1 = insuranceRepository.findInsuranceWithIdAndClientId(client.getClientId(),id);
                    if (insurancePolicy != null) {
                        insurancePolicy1.setPremium(insurancePolicy.getPremium());
                        insurancePolicy1.setType(insurancePolicy.getType());
                        insurancePolicy1.setCoverageAmount(insurancePolicy.getCoverageAmount());
                        insurancePolicy1.setEndDate(insurancePolicy.getEndDate());
                        insurancePolicy1.setStartDate(insurancePolicy.getStartDate());
                        insuranceRepository.save(insurancePolicy1);
                        map1.put("Timestamp", new Date());
                        map1.put("Status", HttpStatus.OK.value());
                        map1.put("Message", "Insurance Updated SuccessfulLy Done.");
                        map1.put("Object", insurancePolicy1);
                        return map1;
                    }
                    throw new NullPointerException();
                }
                catch (NullPointerException n) {
                    map1.put("Timestamp", new Date());
                    map1.put("Status", HttpStatus.BAD_REQUEST.value());
                    map1.put("Message", "No Insurance record attached to "+id+" policy number.");
                }
            }
        }
        catch(NullPointerException n){
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.BAD_REQUEST.value());
            map1.put("Message", "Client does not Exist !");
        }
        return map1;
    }

    public Map<String, Object> deleteSpecificInsurance(Long id, HttpServletRequest request) {
        Map<String, Object> map1 = new LinkedHashMap<>();
        String clientEmail = null;
        String token = jwtRequestFilter.parseJwt(request);
        clientEmail = jwtUtil.getClientEmailFromToken(token);
        try {
            Client client = clientRepository.findByEmail(clientEmail);
            if (client != null) {
                try {
                    InsurancePolicy deleteInsurancePolicy1 = insuranceRepository.findInsuranceWithIdAndClientId(client.getClientId(),id);
                    if (deleteInsurancePolicy1 != null) {
                        try {
                            Claim claim=claimRepository.findSpecificClaimByInsuranceId(deleteInsurancePolicy1.getPolicyNumber());
                            if(claim!=null){
                                claim.setStatus(ClaimStatus.DeActive);
                            }
                            deleteInsurancePolicy1.setEnableInsurance(false);
                        }
                        catch (NullPointerException e1){
                            deleteInsurancePolicy1.setEnableInsurance(false);
                        }
                        map1.put("Timestamp", new Date());
                        map1.put("Status", HttpStatus.OK.value());
                        map1.put("Message","Insurance Policy Deleted Successfully.");
                        map1.put("Object", insuranceRepository.saveAndFlush(deleteInsurancePolicy1));
                        return map1;
                    }
                    throw new NullPointerException();
                } catch (NullPointerException n) {
                    map1.put("Timestamp", new Date());
                    map1.put("Status", HttpStatus.BAD_REQUEST.value());
                    map1.put("Message", "Insurance Does not Exist.");
                    return map1;
                }
            }
        } catch (NullPointerException n) {
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.BAD_REQUEST.value());
            map1.put("Message", "Client does not Exist !");        }
        return map1;
    }

}
