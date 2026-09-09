-- ===================================================================
-- TASK 3: DATABASE SCHEMA & SEED DATA SCRIPT (MySQL)
-- ===================================================================

-- 1. Insert/Update Training Programmes
INSERT INTO training_programmes (program_id, title, required_grade, minimum_years_of_service) 
VALUES (1, 'Financial Management Programme', NULL, NULL)
ON DUPLICATE KEY UPDATE title='Financial Management Programme', required_grade=NULL, minimum_years_of_service=NULL;

INSERT INTO training_programmes (program_id, title, required_grade, minimum_years_of_service) 
VALUES (2, 'Technical Programme', NULL, NULL)
ON DUPLICATE KEY UPDATE title='Technical Programme', required_grade=NULL, minimum_years_of_service=NULL;

INSERT INTO training_programmes (program_id, title, required_grade, minimum_years_of_service) 
VALUES (3, 'Management Development Programme', 'Grade I, Grade II', 5)
ON DUPLICATE KEY UPDATE title='Management Development Programme', required_grade='Grade I, Grade II', minimum_years_of_service=5;

-- 2. Insert Programme Allowed Departments
DELETE FROM programme_allowed_departments WHERE program_id IN (1, 2, 3);

INSERT INTO programme_allowed_departments (program_id, department_name) VALUES (1, 'Finance');
INSERT INTO programme_allowed_departments (program_id, department_name) VALUES (1, 'Budget');
INSERT INTO programme_allowed_departments (program_id, department_name) VALUES (1, 'Planning');

INSERT INTO programme_allowed_departments (program_id, department_name) VALUES (2, 'IT');
INSERT INTO programme_allowed_departments (program_id, department_name) VALUES (2, 'ICT');

-- 3. Insert/Update Officers
INSERT INTO officers (officer_id, full_name, department, grade, years_of_service)
VALUES (1, 'A. Perera', 'Finance', 'Grade III', 6)
ON DUPLICATE KEY UPDATE full_name='A. Perera', department='Finance', grade='Grade III', years_of_service=6;

INSERT INTO officers (officer_id, full_name, department, grade, years_of_service)
VALUES (2, 'B. Silva', 'IT', 'Grade II', 6)
ON DUPLICATE KEY UPDATE full_name='B. Silva', department='IT', grade='Grade II', years_of_service=6;

INSERT INTO officers (officer_id, full_name, department, grade, years_of_service)
VALUES (3, 'C. Fernando', 'Planning', 'Grade I', 10)
ON DUPLICATE KEY UPDATE full_name='C. Fernando', department='Planning', grade='Grade I', years_of_service=10;

INSERT INTO officers (officer_id, full_name, department, grade, years_of_service)
VALUES (4, 'D. Bandara', 'Administration', 'Grade III', 2)
ON DUPLICATE KEY UPDATE full_name='D. Bandara', department='Administration', grade='Grade III', years_of_service=2;

INSERT INTO officers (officer_id, full_name, department, grade, years_of_service)
VALUES (5, 'E. Jayawardena', 'HR', 'Grade I', 8)
ON DUPLICATE KEY UPDATE full_name='E. Jayawardena', department='HR', grade='Grade I', years_of_service=8;

INSERT INTO officers (officer_id, full_name, department, grade, years_of_service)
VALUES (6, 'F. Gunasekara', 'Budget', 'Grade II', 5)
ON DUPLICATE KEY UPDATE full_name='F. Gunasekara', department='Budget', grade='Grade II', years_of_service=5;

INSERT INTO officers (officer_id, full_name, department, grade, years_of_service)
VALUES (7, 'G. Wickramasinghe', 'ICT', 'Grade III', 4)
ON DUPLICATE KEY UPDATE full_name='G. Wickramasinghe', department='ICT', grade='Grade III', years_of_service=4;

-- 4. Previous Participations (12 Months Rule Check)
DELETE FROM nominations WHERE nomination_id IN (1, 2, 3);

-- B. Silva (ID 2) attended Technical Programme (ID 2) 6 months ago (Within 12 months)
INSERT INTO nominations (nomination_id, program_id, officer_id, participation_date, status)
VALUES (1, 2, 2, DATE_SUB(NOW(), INTERVAL 6 MONTH), 'ATTENDED');

-- C. Fernando (ID 3) attended Financial Management (ID 1) 18 months ago (> 12 months)
INSERT INTO nominations (nomination_id, program_id, officer_id, participation_date, status)
VALUES (2, 1, 3, DATE_SUB(NOW(), INTERVAL 18 MONTH), 'ATTENDED');

-- E. Jayawardena (ID 5) attended Management Development (ID 3) 8 months ago (Within 12 months)
INSERT INTO nominations (nomination_id, program_id, officer_id, participation_date, status)
VALUES (3, 3, 5, DATE_SUB(NOW(), INTERVAL 8 MONTH), 'ATTENDED');
