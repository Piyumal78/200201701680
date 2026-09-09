-- Master Reference Data Only (No Mock Programmes or Nominations)

-- Departments
INSERT INTO departments (dept_id, dept_name, dept_code) VALUES (1, 'IT & Digital Transformation', 'IT');
INSERT INTO departments (dept_id, dept_name, dept_code) VALUES (2, 'Finance & Administration', 'FIN');
INSERT INTO departments (dept_id, dept_name, dept_code) VALUES (3, 'Human Resource Management', 'HRM');
INSERT INTO departments (dept_id, dept_name, dept_code) VALUES (4, 'Operations & Planning', 'OPS');

-- Venues
INSERT INTO venues (venue_id, venue_name, capacity, location) VALUES (1, 'Main Auditorium Hall A', 150, 'Building 1');
INSERT INTO venues (venue_id, venue_name, capacity, location) VALUES (2, 'Executive Room 204', 50, 'Building 2');
INSERT INTO venues (venue_id, venue_name, capacity, location) VALUES (3, 'Digital Lab 102', 30, 'Building 3');

-- Trainers
INSERT INTO trainers (trainer_id, full_name, trainer_type, email, contact_no) VALUES (1, 'Dr. Aris Thorne', 'INTERNAL', 'a.thorne@tms.gov', '+1-555-0192');
INSERT INTO trainers (trainer_id, full_name, trainer_type, email, contact_no) VALUES (2, 'Prof. Elena Vance', 'EXTERNAL', 'e.vance@ai.org', '+1-555-0184');

-- Officers
INSERT INTO officers (officer_id, emp_no, full_name, email, dept_id, designation, role) VALUES (101, 'EMP-101', 'A. Perera', 'perera@tms.gov', 1, 'Senior Accountant', 'OFFICER');
INSERT INTO officers (officer_id, emp_no, full_name, email, dept_id, designation, role) VALUES (102, 'EMP-102', 'David Miller', 'david@tms.gov', 1, 'IT Lead', 'DEPT_HEAD');
INSERT INTO officers (officer_id, emp_no, full_name, email, dept_id, designation, role) VALUES (103, 'EMP-103', 'Robert Chen', 'robert@tms.gov', 2, 'Finance Officer', 'OFFICER');
