# KeyCloak integration

## Prerequisites

### Install KeyCloak on Docker

`docker pull quay.io/keycloak/keycloak:25.0.0`

Run keyCloak on port 8081

`docker run -d --name keycloak-25.0.0 -p 8081:8080 -e KEYCLOAK_ADMIN=admin 
-e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:25.0.0 start-dev`


## setup postgres