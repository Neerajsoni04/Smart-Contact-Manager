package com.example.HospitalManagement;

import com.example.HospitalManagement.Entity.Appointment;
import com.example.HospitalManagement.Entity.Insurance;
import com.example.HospitalManagement.Entity.Patient;
import com.example.HospitalManagement.Repositary.AppointmentRepository;
import com.example.HospitalManagement.Service.AppointmentService;
import com.example.HospitalManagement.Service.InsuranceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
public class AppointmentTest {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private InsuranceService insuranceService;
    @Test
    public void testAppointment(){
        Appointment appointment = Appointment.builder()
                .appointmentTime(LocalDateTime.of(2025,7,28,15,0))
                .reason("Cancer")
                .build();

        Appointment newAppointment = appointmentService.createAppointment(appointment,2L,61L);

//        Appointment updated = appointmentService.reAssignToDoctor(newAppointment.getId(),3L);

    }


    @Test
    public void test(){
        Insurance in = Insurance.builder()
                .policynumber("HDFC_1234")
                .provider("HDFC")
                .validTill(LocalDate.of(2030, 12, 12))
                .build();
        Patient patient = insuranceService.assignInsuranceToPatient(in,61L);

        Patient newPatient = insuranceService.disAssociateInsuranceFromPatient(61L);
        System.out.println(newPatient);
    }


}
