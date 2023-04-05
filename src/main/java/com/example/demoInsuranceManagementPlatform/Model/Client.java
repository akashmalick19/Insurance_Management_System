package com.example.demoInsuranceManagementPlatform.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @NotEmpty(message = "Client name should not be Null or Empty.")
    @Size(min = 3,message = "Client name minimum has at least 3 characters.")
    private String clientName;

    @NotEmpty(message = "Client name should not be Null or Empty.")
    @Size(min = 12,message = "Client name minimum has at least 12 characters.")
    private String clientEmail;

    @NotEmpty(message = "Password should not be Null or Empty.")
    @Size(min = 6,message = "Password minimum has at least 6 characters.")
    private String clientPassword;

    private Date dateOfBirth;

    @NotEmpty(message = "Address should not be Null or Empty.")
    private String address;

    @NotEmpty(message = "contract Number should not be Null or Empty.")
    @Size(min = 10,message = "contract Number minimum has at least 10 characters.")
    private String contractNo;

    @JsonIgnore
    @Column(name = "delete_record_status",nullable = false,columnDefinition = "TINYINT(1)")
    private boolean enableClient=true;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "client")
    private List<InsurancePolicy> insurancePolicySet;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "client_role_table",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roleEntitySet;

}
