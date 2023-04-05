package com.example.demoInsuranceManagementPlatform;

import com.example.demoInsuranceManagementPlatform.Model.Client;
import com.example.demoInsuranceManagementPlatform.Model.Role;
import com.example.demoInsuranceManagementPlatform.Repository.ClientRepository;
import com.example.demoInsuranceManagementPlatform.Repository.RoleRepository;
import com.example.demoInsuranceManagementPlatform.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class DemoInsuranceManagementPlatformApplication implements CommandLineRunner {

	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private ClientService clientService;

	public static void main(String[] args) {
		SpringApplication.run(DemoInsuranceManagementPlatformApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

			/* Initial Role insert inside role data base */
			List<Role> roleList=new ArrayList<>();
			Role role=new Role(1L,"Super_User");
			Role role1=new Role(2L,"User");
			roleList.add(role);
			roleList.add(role1);
			roleRepository.saveAll(roleList);
			System.out.println("Initial 'Super User' & 'User' role is saved inside Role Table.");

			/* For Super-User Who has all authentication inside database */
			Client client=new Client();
			client.setClientId(1L);
        	client.setClientName("user");
			client.setClientEmail("user@gmail.com");
			client.setClientPassword(clientService.getEncodedPassword("user@pass"));
			client.setDateOfBirth(Date.valueOf("1997-11-02"));
			client.setAddress("Kolkata, West-Bengal");
			client.setContractNo("9903851236");
			Set<Role> roleSet1=new HashSet<>();
			Role role2= roleRepository.findById(1L).get();
			roleSet1.add(role2);
			client.setRoleEntitySet(roleSet1);
			clientRepository.save(client);
			System.out.println("Initial Super User Details are saved inside Client Table.");
	}
}
