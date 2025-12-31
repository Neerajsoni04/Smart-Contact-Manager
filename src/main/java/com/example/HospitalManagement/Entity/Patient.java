package com.example.HospitalManagement.Entity;

import com.example.HospitalManagement.Entity.Type.BloodGroup;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_patient_email",columnNames = {"email"}),
//                @UniqueConstraint(name = "unique_patient_name_birthdate",columnNames = {"birthdate","name"})
        },
        indexes = {
                @Index(name = "idx_patient_birthdate",columnList = "birthdate")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

//    @ToString.Exclude
    private LocalDate birthdate;
    private String email;
    private String gender;

    @OneToOne
    @MapsId
    private User user;

    @Column(name = "blood_group")
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @OneToOne(cascade = {CascadeType.ALL},orphanRemoval = true)
    @JoinColumn(name = "insurance_id")
    private Insurance insurance;

    @OneToMany(mappedBy = "patient",cascade = {CascadeType.REMOVE},orphanRemoval = true , fetch = FetchType.EAGER)
    private List<Appointment> appointment = new ArrayList<>();
}
