## Beer Catalog

This API manages a beer catalogue from different manufacturers (providers). 

Beer consumers can look up the beer catalogue in order to inspire future purchases.

## Installation

Having a machine with docker installed, and currently located inside the project folder simply run the following commands:

```
docker build -t beercatalog.jar .
```

```
docker-compose up -d
```

If you want to shutdown, run this command:

```
docker-compose down --volumes
```

## Usage

For usage instructions regarding the consumption of this API, please read the documentation file inside this project `beerCatalogAPI_doc.html`.

I recommend using [Postman](https://www.getpostman.com/) client for API consumption. 

When testing **anonymous** user select in the Authorization tab **No Auth**.

When testing **admin** or **manufacturer** user select in the Authorization tab **Basic Auth**.

To test permissions for each resource, the following users are created by default:

- Default admin user with **ROLE_ADMIN**
```
email: admin@admin.com
pwd: admin
```

- User related to the manufacturer with name **Victory Brewing Company** with **ROLE_MANUFACTURER**
```
email: victory@provider.com
pwd: provider
```

- User related to the manufacturer with name **Tröegs Brewing Company** with **ROLE_MANUFACTURER**
```
email: tröegs@provider.com
pwd: provider
```

- User related to the manufacturer with name **Paulaner Brauerei** with **ROLE_MANUFACTURER**
```
email: paulaner@provider.com
pwd: provider
```

Please ensure you **clean cookies** after switching between users credentials.

## Domain Model 

The current domain model consists of 4 tables:

### Beers

Contains the information about the beers in the catalog.

```
CREATE TABLE beers (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    type VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    abv NUMERIC NOT NULL,
    manufacturer_id INTEGER REFERENCES manufacturers(id)
);
```

### Manufacturers

Contains information that is not sensitive about the manufacturers in the catalog.

```
CREATE TABLE manufacturers (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    nationality VARCHAR NOT NULL
);
```

### Providers

This is the table for our users and consequently it is used for authentication.

```
CREATE TABLE providers (
    id SERIAL PRIMARY KEY,
    email VARCHAR UNIQUE NOT NULL,
    pwd VARCHAR NOT NULL,
    manufacturer_id INTEGER,
    FOREIGN KEY (manufacturer_id) REFERENCES manufacturers(id)
);
```

We currently use the field email as a username for authentication and all passwords are stored using Bcrypt hashing.

### Authorities

- This is the table for the permissions related to the users.

```
CREATE TABLE authorities (
    id SERIAL PRIMARY KEY,
    provider_id INTEGER NOT NULL,
    name VARCHAR NOT NULL,
    FOREIGN KEY (provider_id) REFERENCES providers(id)
);
```

Currently there are 2 types of authorities:
- **ROLE_ADMIN**
- **ROLE_MANUFACTURER**

## Highlights

- **GET** endpoints related to **beers** and **manufacturers** are accessible to **unauthenticated** requests. Since sensitive information about the manufacturers is stored in the providers table, there is no reason to block information of the manufacturers to anonymous users.

- **POST** endpoint related to **manufacturers** is only accessible to **admin** users. This action will trigger a user creation related to the new manufacturer. This new user will also be assigned with the role **ROLE_MANUFACTURER**. The default credentials for the new user follow this pattern:

```
username: newManufacturerName@provider.com
pwd: provider
```

- **DELETE** endpoint related to **manufacturers** is only accessible to **admin** users. This action will trigger the deletion of the beers and user/authorities related to the deleted manufacturer.

- **POST** and **PUT** endpoints related to **beers** and **manufacturers** are only accessible to **admin** and **manufacturer** users. The manufacturer user has to be its own manufacturer to perfom this requests. Admin user can perform these requests without any restriction.

- Using custom method security expression with @Preauthorize to check if an authenticated user is its own manufacturer.
- Implementing AuthenticationProvider for custom authentication based on a specific schema.
- Centralizing exception handling in a single class with @ControllerAdvice
- Unit testing Controller and Service layers.




