package com.example.demoInsuranceManagementPlatform.Repository;

import com.example.demoInsuranceManagementPlatform.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    @Query(value = "select * from role where role_name=?1 ",nativeQuery = true)
    Role findRole(String roleName);
}
