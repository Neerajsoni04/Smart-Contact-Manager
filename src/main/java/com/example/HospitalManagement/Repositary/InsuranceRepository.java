package com.example.HospitalManagement.Repositary;

import com.example.HospitalManagement.Entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
}