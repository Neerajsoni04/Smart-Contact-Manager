package com.example.HospitalManagement;

import com.example.HospitalManagement.Entity.Insurance;
import com.example.HospitalManagement.Entity.Patient;
import com.example.HospitalManagement.Service.InsuranceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class InuranceTest {

    @Autowired
    private InsuranceService insuranceService;

    @Test
    public void testinsurance(){
        Insurance in = Insurance.builder()
                .policynumber("HDFC_1234")
                .provider("HDFC")
                .validTill(LocalDate.of(2030, 12, 12))
                .build();
        Patient patient = insuranceService.assignInsuranceToPatient(in,86L);
        System.out.println(patient);
    }
}
