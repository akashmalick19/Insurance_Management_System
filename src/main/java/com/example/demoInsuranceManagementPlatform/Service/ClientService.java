package com.example.demoInsuranceManagementPlatform.Service;

import com.example.demoInsuranceManagementPlatform.Configuration.JwtRequestFilter;
import com.example.demoInsuranceManagementPlatform.Model.*;
import com.example.demoInsuranceManagementPlatform.Repository.ClaimRepository;
import com.example.demoInsuranceManagementPlatform.Repository.ClientRepository;
import com.example.demoInsuranceManagementPlatform.Repository.RoleRepository;
import com.example.demoInsuranceManagementPlatform.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class ClientService {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClaimRepository claimRepository;

    public Map<String, Object> storeClientData(Client client,HttpServletRequest request) {

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

        String name = null;
        if (StringUtils.hasText(client.getClientName()) && StringUtils.hasText(client.getClientPassword())) {
            name = client.getClientName().trim();
        }
        try {
            Client client1 = clientRepository.findByEmail(client.getClientEmail());
            if (client1.getClientEmail().equals(client.getClientEmail())) {
                map1.put("Timestamp", new Date());
                map1.put("Status", HttpStatus.BAD_REQUEST.value());
                map1.put("Message", "Client's Email Id is already exist.");
                return map1;
            }
            throw new NullPointerException();
        }
        catch (NullPointerException n) {
            try {
                Client client1 = clientRepository.findByContactNo(client.getContractNo());
                if (client1.getContractNo().equals(client.getContractNo())) {
                    map1.put("Timestamp", new Date());
                    map1.put("Status", HttpStatus.BAD_REQUEST.value());
                    map1.put("Message", "Client's Contract Number is already exist.");
                    return map1;
                }
                throw new NullPointerException();
            }
            catch (NullPointerException z) {
                client.setClientName(name);
                client.setClientPassword(getEncodedPassword(client.getClientPassword()));
                Set<Role> roleSet = new HashSet<>();
                Role role = roleRepository.findById(2L).get();
                roleSet.add(role);
                client.setRoleEntitySet(roleSet);
                map1.put("Timestamp", new Date());
                map1.put("Status", HttpStatus.OK.value());
                map1.put("Message", "Client Added Successfully.");
                map1.put("Object", clientRepository.save(client));
                return map1;
            }
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

        List<Client> client = clientRepository.findAllClientData();
            if (client.isEmpty()) {
                map1.put("Timestamp", new Date());
                map1.put("Status", HttpStatus.NO_CONTENT.value());
                map1.put("Message", "Client Not Exist !");
                map1.put("Object", client);
                return map1;
            }
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.OK.value());
            map1.put("Message", "Successfully Fetch.");
            map1.put("Object", client);
            return map1;
    }

    public Map<String, Object> fetchSpecificClient(Long id, HttpServletRequest request) {
        Map<String, Object> map1 = new LinkedHashMap<>();
        String clientEmail = null;
        String token = jwtRequestFilter.parseJwt(request);
        clientEmail = jwtUtil.getClientEmailFromToken(token);
        Client client = clientRepository.findByEmailAndId(clientEmail,id);
        try {
            if (client == null) {
                throw new NullPointerException();
            }
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.OK.value());
            map1.put("Message", client.getClientName()+" information.");
            map1.put("Object", client);
            return map1;
        }
        catch (NullPointerException e) {
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.BAD_REQUEST.value());
            map1.put("Message", "Client does not Exist.");
            return map1;
        }
    }

    public Map<String, Object> updateSpecificClient(Long id,Client client,HttpServletRequest request) {
        Map<String, Object> map1 = new LinkedHashMap<>();
        String clientEmail = null;
        String token = jwtRequestFilter.parseJwt(request);
        clientEmail = jwtUtil.getClientEmailFromToken(token);
        Client updateClient = clientRepository.findByEmailAndId(clientEmail,id);
        try {
            if (updateClient == null) {
                throw new NullPointerException();
            }
            updateClient.setClientName(client.getClientName());
            updateClient.setAddress(client.getAddress());
            updateClient.setClientPassword(getEncodedPassword(client.getClientPassword()));
            updateClient.setContractNo(client.getContractNo());
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.OK.value());
            map1.put("Message", "Updated Successfully.");
            map1.put("Object", clientRepository.save(updateClient));
            return map1;
        }
        catch (NullPointerException e) {
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.BAD_REQUEST.value());
            map1.put("Message", "Client does not Exist.");
            return map1;
        }
    }

    public Map<String, Object> deleteSpecificClient(Long id, HttpServletRequest request) {
        Map<String, Object> map1 = new LinkedHashMap<>();
        String clientEmail = null;
        String token = jwtRequestFilter.parseJwt(request);
        clientEmail = jwtUtil.getClientEmailFromToken(token);
        Client deleteClient = clientRepository.findByEmailAndId(clientEmail,id);
        try {
            if (deleteClient == null) {
                throw new NullPointerException();
            }
            if(deleteClient.getInsurancePolicySet()!=null){
                for(InsurancePolicy i:deleteClient.getInsurancePolicySet()){
                    try {
                        Claim claim=claimRepository.findSpecificClaimByInsuranceId(i.getPolicyNumber());
                        if(claim!=null){
                            claim.setStatus(ClaimStatus.DeActive);
                        }
                        i.setEnableInsurance(false);
                    }
                    catch (NullPointerException e1){
                        i.setEnableInsurance(false);
                    }
                    finally {
                        deleteClient.setEnableClient(false);
                    }
                }
            }
            deleteClient.setEnableClient(false);
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.OK.value());
            map1.put("Message","Client Deleted Successfully.");
            map1.put("Object", clientRepository.save(deleteClient));
            return map1;
        }
        catch (NullPointerException e) {
            map1.put("Timestamp", new Date());
            map1.put("Status", HttpStatus.BAD_REQUEST.value());
            map1.put("Message", "Client does not Exist.");
            return map1;
        }
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

}
