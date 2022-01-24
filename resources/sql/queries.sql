-- :name get-all-students :? :*
-- :doc select all the artists with all the attributes
SELECT student_name, phone_number, mobile_phone_number, ematter_student_symbol, birth_date, center_symbol  
  FROM students

-- :name create-student! :! :n
-- :doc creates a new student record
INSERT INTO ops_students
(id, name, birth, telephone, mobile, classroom_id, classroom_type, old_id)
VALUES (:id, :name, :birth, :telephone, :mobile, :classroom-id, :classroom-type, :old-id)
