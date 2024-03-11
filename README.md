# DMI POC Backend

Enables searching for objects in a MinIO server-managed bucket

- The list of objects obtained from the search
- must be sortable, by, name default,
- and for each individual object, detailed attributes should be available
- along with the option to download after
- download confirming a message.
- The search and download operations need to be logged in a database.


Optional Add-ons:
- Search for tags
- Upload objects to the MinIO server
- Multiple object downloads from the list
- Authentication against the MinIO server
- Dynamic configuration management
- The code is expected to be published on GitHub
- JDK 11, JPA2
- Postgres

### Requirements

Set up minio server using the command. 

    podman run \
    -p 9000:9000 \
    -p 9001:9001 \
    -v ~/tmp/miniodata:/data \
    -e "MINIO_ROOT_USER=ROOTNAME" \
    -e "MINIO_ROOT_PASSWORD=CHANGEME123" \
    quay.io/minio/minio server /data --console-address ":9001"

Set up postgres with a new database, `dmi_poc`.

Setup the application code by changing the `application.yml` file in `src/main/resources`

### Build and package

    mvn clean package

### Running

    java -jar target/dmi_poc.jar
