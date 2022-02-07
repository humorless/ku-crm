#!/usr/bin/env bash
PGOPTIONS="--search_path=dbt_develop"
export PGOPTIONS

psql -d kuops_dev -c "DROP TABLE IF EXISTS ops_student;"
psql -d kuops_dev -c "CREATE TABLE ops_student (id text PRIMARY KEY, name text, birth DATE, telephone text, mobile text, classroom_id text, classroom_type text, old_id text);"
