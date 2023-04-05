package com.example.demoInsuranceManagementPlatform.Repository;

import com.example.demoInsuranceManagementPlatform.Model.Client;
import com.example.demoInsuranceManagementPlatform.Model.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceRepository extends JpaRepository<InsurancePolicy,Long> {

    @Query(value= "select * from insurance_policy x where x.delete_record_status='1'",
            nativeQuery = true)
    List<InsurancePolicy> findAllInsuranceData();

    @Query(value= "select x.* from insurance_policy x INNER JOIN client c on x.client_id=c.client_id where x.client_id=:clientId and x.policy_number=:id and x.delete_record_status='1'",
            nativeQuery = true)
    InsurancePolicy findInsuranceWithIdAndClientId(Long clientId, Long id);


    @Query(value= "select x.* from insurance_policy x INNER JOIN client c on x.client_id=c.client_id  where x.delete_record_status='1' and x.client_id= ?1",
            nativeQuery = true)
    List<InsurancePolicy> findInsuranceWithClientId(Long clientId);

    boolean existsByPolicyNumber(Long InsuranceId);

    InsurancePolicy findByPolicyNumber(Long InsuranceId);

}
