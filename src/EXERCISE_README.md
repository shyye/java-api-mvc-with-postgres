# Exercise README

## Setup
- Add a file called `config.properties` in the src/main/resources/ folder.
- IMPORTANT! Ignore the `config.properties` file by adding the below in .gitignore.
```
### Personal Settings ###
*.properties
```

config.properties:  
```
db.url=<Value for PGHOST>
db.user=<Value for PGUSER>
db.password=<Value for PGPASSWORD>
db.database=<Value for PGDATABASE>
```

- Run `flyway -cleanDisabled=false clean` in the terminal. To clear all previous data.
- Later, run `flyway migrate` when the migration files are setup.