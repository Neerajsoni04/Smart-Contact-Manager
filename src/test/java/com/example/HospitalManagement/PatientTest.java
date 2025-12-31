package com.example.HospitalManagement;

import com.example.HospitalManagement.Entity.Insurance;
import com.example.HospitalManagement.Entity.Patient;
import com.example.HospitalManagement.Repositary.PatientRepositary;
import com.example.HospitalManagement.Service.InsuranceService;
import com.example.HospitalManagement.Service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class PatientTest {

    @Autowired
    private PatientRepositary patientRepositary;
    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private PatientService patientService;





    // make a function that delete the patient and their appointment should be deleted automatically
    @Test
    public void test2(){
        Patient patient = patientService.deletePatient(6L);
        System.out.println(patient);
    }

    @Test
    public void testPatientRepository(){
        List<Patient> list = patientRepositary.findAllPatientWithAppointement2();
        System.out.println(list);
    }


}
