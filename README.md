# DropBoxApplication
- getAll files : gets metadata for all uploaded files
- upload file : api to upload file
- update file : api to update existing file by id
- read file : api to read file by id
- delete file : api to delete existing file by id

## swagger urls : http://localhost:8099/swagger-ui/index.html#/


## steps to run
- setup postgres database with name dropbox and update application.properties `create DATABASE dropbox;`
- update the application.properties property `localFileStoragePath` for `write-able` local file storage path
- change port of application if needed

## command to run
- `mvn clean package`
- `java -jar target/dropbox-0.0.1-SNAPSHOT.jar`

    
