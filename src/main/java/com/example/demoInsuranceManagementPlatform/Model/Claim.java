package com.example.demoInsuranceManagementPlatform.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long claimNumber;

    private String description;
    private Date claimDate;

    @Column(name = "claim_status")
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "claim")
    private InsurancePolicy insurancePolicy;

}
