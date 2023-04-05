package com.example.demoInsuranceManagementPlatform.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "insurance_policy")
public class InsurancePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long policyNumber;

    @NotEmpty(message = "Insurance type should not be Null or Empty.")
    private String type;

    private Double coverageAmount;
    private Double premium;

    private Date startDate;
    private Date endDate;

    @JsonIgnore
    @Column(name = "delete_record_status",nullable = false,columnDefinition = "TINYINT(1)")
    private boolean enableInsurance=true;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "client_id")
    @JsonIgnore
    private Client client;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "claim_number",referencedColumnName = "claimNumber")
    private Claim claim;

}
