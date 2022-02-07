# ku-crm
A simple operational database system used to manage customer information.

## How to build

```
  bin/build.sh
```

## How to use
1. Config the `database-url` at the file `resources/config.edn`. The database-url points to data warehouse.
2. Build the `ku-crm` by using `bin/build.sh`
3. Clear the `ops_student` table inside the data warehouse by `bin/ops_student.sh`
4. Run the command
```
 ;; Assume that we want to store the operational database at the directory ./resources/datalevin/db
 java -jar target/ku-crm.jar :import ./resources/datalevin/db

 ;; sync the data from operational database to postgre-url
 java -jar target/ku-crm.jar :sync ./resources/datalevin/db
```

## How it works

```
    |----------------|   --> import --> |--------------|
    | data warehouse |                  | ops database |
    |----------------|   <-- sync <---  |--------------|
```
1. the original data stored at `student` table at the data warehouse.
2. the `ops_student` table at the data warehouse should be synced from ops database.

## License

Copyright &copy; 2022 Laurence Chen and Contributors

Licensed under the term of the Mozilla Public License 2.0, see LICENSE.
