package com.example.demoInsuranceManagementPlatform.Repository;

import com.example.demoInsuranceManagementPlatform.Model.Client;
import com.example.demoInsuranceManagementPlatform.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {

    @Query(value= "select * from client c where c.client_email= ?1 and c.delete_record_status='1' ",
    nativeQuery = true)
    Client findByEmail(String clientEmail);

    @Query(value= "select * from client c1 where c1.contract_no= ?1",
            nativeQuery = true)
    Client findByContactNo(String contractNo);

    @Query(value= "select c1.* from client c1 LEFT JOIN insurance_policy i on c1.client_id=i.client_id where c1.client_email= ?1 and c1.client_id= ?2 and c1.delete_record_status='1' ",
            nativeQuery = true)
    Client findByEmailAndId(String clientEmail, Long id);

    @Query(value= "select * from client c1 where c1.delete_record_status='1'",
            nativeQuery = true)
    List<Client> findAllClientData();

    @Query(value = "select * from client_role_table where client_id=?1 ",nativeQuery = true)
    Client getRole(Long clientId);

}
