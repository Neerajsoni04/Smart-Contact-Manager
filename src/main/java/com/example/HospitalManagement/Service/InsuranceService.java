package com.example.HospitalManagement.Service;

import com.example.HospitalManagement.Entity.Insurance;
import com.example.HospitalManagement.Entity.Patient;
import com.example.HospitalManagement.Repositary.InsuranceRepository;
import com.example.HospitalManagement.Repositary.PatientRepositary;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final PatientRepositary patientRepositary;
    private final InsuranceRepository insuranceRepository;

    @Transactional
    public Patient assignInsuranceToPatient(Insurance insurance,Long id){
        Patient patient = patientRepositary.findById(id).orElseThrow(()->  new IllegalArgumentException("Patient not found with id : "+id));

        patient.setInsurance(insurance);
        insurance.setPatient(patient);

        return patient;
    }

    @Transactional
    public Patient disAssociateInsuranceFromPatient(Long patient_id){
        Patient patient= patientRepositary.findById(patient_id).orElseThrow();

        patient.setInsurance(null);

        return patient;
    }
}
