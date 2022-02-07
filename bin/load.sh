#!/usr/bin/env bash
PGOPTIONS="--search_path=dbt_develop"
export PGOPTIONS

psql -d kuops_dev -c "DROP TABLE IF EXISTS student;"
psql -d kuops_dev -c "CREATE TABLE student (
    center_symbol character varying, 
    registration_date date,
    ematter_student_symbol character varying,
    student_name character varying,
    gender character varying,
    birth_date date,
    school_grade character varying,
    graduation_year numeric,
    parent_name character varying,
    parent_gender character varying,
    parent_birth_date date,
    email_address character varying,
    mobile_phone_number character varying,
    phone_number character varying,
    postal_code character varying,
    address character varying,
    hdyhau character varying,
    hdyhau_other character varying,
    note character varying
);"

csvsql --insert ./fake_students.tsv --db-schema dbt_develop --table student --tabs --no-create --no-constraints --db postgresql://localhost/kuops_dev
