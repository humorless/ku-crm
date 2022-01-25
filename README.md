# ku-crm
A simple operational database system used to manage customer information.

## How to build

```
  bin/build.sh
```

## How to use
1. Setup the postgre-url, which points to data warehouse
2. Clear the ops_students table inside the postgre-url by `bin/ops_students.sh`
3. Run the command
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
1. the original data stored at `students` table at the data warehouse.
2. the `ops_students` table at the data warehouse should be synced from ops database.

## License

Copyright &copy; 2022 Laurence Chen and Contributors

Licensed under the term of the Mozilla Public License 2.0, see LICENSE.
