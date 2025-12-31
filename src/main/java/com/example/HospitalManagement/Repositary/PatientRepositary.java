package com.example.HospitalManagement.Repositary;

import com.example.HospitalManagement.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRepositary extends JpaRepository<Patient, Long> {


//    @Query("Select p from patient p left join fetch p.appointments")
    @Query("Select p from Patient p left join fetch p.appointment a left join fetch a.doctor")
    List<Patient> findAllPatientWithAppointement2();

}
