package com.example.HospitalManagement.Service;

import com.example.HospitalManagement.Entity.Patient;
import com.example.HospitalManagement.Repositary.PatientRepositary;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepositary patientRepositary;

    @Transactional
    public Patient deletePatient(Long patient_id){
        Patient patient = patientRepositary.findById(patient_id).orElseThrow();

        patientRepositary.delete(patient);
        return patient;
    }


}
