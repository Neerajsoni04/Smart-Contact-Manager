package com.example.HospitalManagement.Service;

import com.example.HospitalManagement.Entity.Appointment;
import com.example.HospitalManagement.Entity.Doctor;
import com.example.HospitalManagement.Entity.Patient;
import com.example.HospitalManagement.Repositary.AppointmentRepository;
import com.example.HospitalManagement.Repositary.DoctorRepository;
import com.example.HospitalManagement.Repositary.PatientRepositary;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final DoctorRepository doctorRepository;
    private final PatientRepositary patientRepositary;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public Appointment createAppointment(Appointment appointment,Long doctor_id,Long patient_id){
        Doctor doctor = doctorRepository.findById(doctor_id).orElseThrow(IllegalArgumentException::new);
        Patient patient = patientRepositary.findById(patient_id).orElseThrow(IllegalArgumentException::new);

        if (appointment.getId() != null) throw new IllegalArgumentException("Appointment should not have Id");

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        patient.getAppointment().add(appointment); // it maintains bidirectional Consistency

        appointmentRepository.save(appointment);

        return appointment;
    }

    @Transactional
    public Appointment reAssignToDoctor(Long appointment_id,Long doctor_id){
        Doctor doctor = doctorRepository.findById(doctor_id).orElseThrow();
        Appointment appointment = appointmentRepository.findById(appointment_id).orElseThrow();

        appointment.setDoctor(doctor);

        doctor.getAppointments().add(appointment);

        return appointment;
    }

}
