-- MySQL Master Reference Data (INSERT IGNORE prevents duplicate errors on restart)

-- Departments
INSERT IGNORE INTO departments (dept_id, dept_name, dept_code) VALUES (1, 'IT & Digital Transformation', 'IT');
INSERT IGNORE INTO departments (dept_id, dept_name, dept_code) VALUES (2, 'Finance & Administration', 'FIN');
INSERT IGNORE INTO departments (dept_id, dept_name, dept_code) VALUES (3, 'Human Resource Management', 'HRM');
INSERT IGNORE INTO departments (dept_id, dept_name, dept_code) VALUES (4, 'Operations & Planning', 'OPS');

-- Venues
INSERT IGNORE INTO venues (venue_id, venue_name, capacity, location) VALUES (1, 'Main Auditorium', 150, 'Building 1');
INSERT IGNORE INTO venues (venue_id, venue_name, capacity, location) VALUES (2, 'Executive Room 204', 50, 'Building 2');

-- Trainers
INSERT IGNORE INTO trainers (trainer_id, full_name, trainer_type, email, contact_no) VALUES (1, 'Dr. Aris Thorne', 'INTERNAL', 'a.thorne@tms.gov', '+1-555-0192');

-- Officers
INSERT IGNORE INTO officers (officer_id, emp_no, full_name, email, dept_id, designation, role) VALUES (101, 'EMP-101', 'A. Perera', 'perera@tms.gov', 1, 'Senior Accountant', 'OFFICER');
INSERT IGNORE INTO officers (officer_id, emp_no, full_name, email, dept_id, designation, role) VALUES (102, 'EMP-102', 'B. Silva', 'silva@tms.gov', 2, 'IT Lead', 'OFFICER');
INSERT IGNORE INTO officers (officer_id, emp_no, full_name, email, dept_id, designation, role) VALUES (103, 'EMP-103', 'C. Fernando', 'fernando@tms.gov', 3, 'Finance Officer', 'OFFICER');
INSERT IGNORE INTO officers (officer_id, emp_no, full_name, email, dept_id, designation, role) VALUES (104, 'EMP-104', 'D. Jayawardena', 'jayawardena@tms.gov', 4, 'Operations Lead', 'OFFICER');
INSERT IGNORE INTO officers (officer_id, emp_no, full_name, email, dept_id, designation, role) VALUES (105, 'EMP-105', 'E. Watson', 'watson@tms.gov', 1, 'HR Specialist', 'OFFICER');

-- Training Programme (Capacity: 3 for demo)
INSERT IGNORE INTO training_programmes (program_id, title, start_date, end_date, start_time, end_time, venue_id, trainer_id, max_participants, status)
VALUES (1, 'Cybersecurity Awareness Programme', '2026-10-15', '2026-10-15', '09:00:00', '16:00:00', 1, 1, 3, 'PUBLISHED');
