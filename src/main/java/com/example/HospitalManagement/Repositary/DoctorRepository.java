package com.example.HospitalManagement.Repositary;

import com.example.HospitalManagement.Entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}