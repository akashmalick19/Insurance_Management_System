package com.example.demoInsuranceManagementPlatform.Repository;

import com.example.demoInsuranceManagementPlatform.Model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim,Long> {
    @Query(value= "select * from claim x where x.claim_status='Active'",
            nativeQuery = true)
    List<Claim> findAllClaimData();

    @Query(value= "select * from claim x where x.claim_status='Active' and x.claim_number=?1 ",
            nativeQuery = true)
    Claim findByClaimWithId(Long id);


    @Query(value = "select c.* from claim c INNER JOIN insurance_policy i on c.claim_number=i.claim_number and i.policy_number=:policyNumber where c.claim_status LIKE 'Active' ",nativeQuery = true)
    Claim findSpecificClaimByInsuranceId(Long policyNumber);

    @Query(value = "select * from claim where claim_number= ?1 and claim_status='DeActive' ",nativeQuery = true)
    Claim existStatus(Long claimNumber);


}
