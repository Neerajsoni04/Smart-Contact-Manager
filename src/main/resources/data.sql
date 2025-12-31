-- 1. Insert users
--INSERT INTO user (id, username, password, provider_id, provider_type) VALUES
--  (1, 'aarav', 'password', NULL, 'EMAIL'),
--  (2, 'diya', 'password', NULL, 'EMAIL'),
--  (3, 'dishant', 'password', NULL, 'EMAIL'),
--  (4, 'neha', 'password', NULL, 'EMAIL'),
--  (5, 'kabir', 'password', NULL, 'EMAIL');

-- 2. Assign roles
INSERT INTO user_role (user_id, role) VALUES
  (1, 'PATIENT'),
  (2, 'PATIENT'),
  (3, 'PATIENT'),
  (4, 'PATIENT'),
  (5, 'PATIENT');

-- 3. Insert patients (user_id == PK, must match user.id)
INSERT INTO patient (user_id, name, gender, birthdate, email, blood_group) VALUES
  (1, 'Aarav Sharma', 'MALE', '1990-05-10', 'aarav.sharma@example.com', 'O_POSITIVE'),
  (2, 'Diya Patel', 'FEMALE', '1995-08-20', 'diya.patel@example.com', 'A_POSITIVE'),
  (3, 'Dishant Verma', 'MALE', '1988-03-15', 'dishant.verma@example.com', 'A_POSITIVE'),
  (4, 'Neha Iyer', 'FEMALE', '1992-12-01', 'neha.iyer@example.com', 'AB_POSITIVE'),
  (5, 'Kabir Singh', 'MALE', '1993-07-11', 'kabir.singh@example.com', 'O_POSITIVE');

-- 4. Insert doctors (also mapped with user_id)
--INSERT INTO user (id, username, password, provider_type) VALUES
--  (6, 'dr.rakesh', 'password', 'EMAIL'),
--  (7, 'dr.sneha', 'password', 'EMAIL'),
--  (8, 'dr.arjun', 'password', 'EMAIL');

INSERT INTO user_role (user_id, role) VALUES
  (6, 'DOCTOR'),
  (7, 'DOCTOR'),
  (8, 'DOCTOR');

INSERT INTO doctor (user_id, name, specialization, email) VALUES
  (6, 'Dr. Rakesh Mehta', 'Cardiology', 'rakesh.mehta@example.com'),
  (7, 'Dr. Sneha Kapoor', 'Dermatology', 'sneha.kapoor@example.com'),
  (8, 'Dr. Arjun Nair', 'Orthopedics', 'arjun.nair@example.com');

-- 5. Insert appointments (reference doctor.user_id and patient.user_id)
INSERT INTO appointment (appointment_time, reason, doctor_id, patient_id) VALUES
  ('2025-07-01 10:30:00', 'General Checkup', 6, 2),
  ('2025-07-02 11:00:00', 'Skin Rash', 7, 2),
  ('2025-07-03 09:45:00', 'Knee Pain', 8, 3),
  ('2025-07-04 14:00:00', 'Follow-up Visit', 6, 1),
  ('2025-07-05 16:15:00', 'Consultation', 6, 4),
  ('2025-07-06 08:30:00', 'Allergy Treatment', 7, 5);
