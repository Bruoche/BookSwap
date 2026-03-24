# exchange-manager-api

## Lancement application
docker compose up

> Swagger ui disponible à : http://localhost:8080/q/swagger-ui/

Projet déployé à l'adresse : https://bookswap-3jl8.onrender.com/api/books?pageSize=3

## Requests:


### Auth
Register
```bash
curl -X 'POST' \
  'http://localhost:8080/api/auth/register' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "username": "username",
  "password": "password",
  "email": "some@main.com"
}'
```
Login

```bash
curl -X 'POST' \
  'http://localhost:8080/api/auth/login' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "username": "username",
  "password": "password"
}'
```
> Crédentials admin en dev: (données de base en dev)
> - Username: ``admin``
> - Password: ``Admin1234!``
> 
> Utilisateurs pré-définis:
> - Username: ``otman``/``aminata``
> - Password: ``Password1!``
```bash
TOKEN="{{your token}}"
```
> *(refresh token donné dans le cookie "refreshToken")*

Profile
```bash
curl -X 'GET' \
  'http://localhost:8080/api/auth/me' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```

Change profile
```bash
curl -X 'PUT' \
  'http://localhost:8080/api/auth/me' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}' \
  -H 'Content-Type: application/json' \
  -d '{
  "username": "new username",
  "email": "new@mail.com"
}'
```

Change password
```bash
curl -X 'PATCH' \
  'http://localhost:8080/api/auth/password?new_password=new%20password&old_password=password' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```

Refresh token
```bash
curl -X 'POST' \
  'http://localhost:8080/api/auth/refresh' \
  -H 'accept: application/json' \
  -d ''
```
*-> Utilisation du cookie "refreshToken"*

### Authors
List authors
```bash
curl -X 'GET' \
  'http://localhost:8080/api/authors?index=0&pageSize=3' \
  -H 'accept: application/json'
```
New author
```bash
curl -X 'POST' \
  'http://localhost:8080/api/authors' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}' \
  -H 'Content-Type: application/json' \
  -d '{
  "firstname": "New",
  "lastname": "Author"
}'
```
Author details
```bash
curl -X 'GET' \
  'http://localhost:8080/api/authors/1' \
  -H 'accept: application/json'
```
### Books
All books
```bash
curl -X 'GET' \
  'http://localhost:8080/api/books?index=0&pageSize=3' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```
Search
```bash
curl -X 'GET' \
  'http://localhost:8080/api/books?author=Vic%20Hug&genre=Rom&index=0&isbn=97&pageSize=3&publicationYear=1800' \
  -H 'accept: application/json'
```
> *Note: La recherche supporte des mots-clefs partiels, et recherche de noms et prénom partiels ensemble tel que "Vic Hug" pour "Victor Hugot". De même, l'année est recherchée via "année livre > année voulue"*

New book
```bash
curl -X 'POST' \
  'http://localhost:8080/api/books' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}' \
  -H 'Content-Type: application/json' \
  -d '{
  "isbn": "876123456789",
  "title": "New Book",
  "description": "Some book description",
  "publicationYear": 2026,
  "coverUrl": "https://en.wikipedia.org/wiki/Book_cover",
  "authors": [
    1
  ],
  "genres": [
    1
  ]
}'
```
Delete book
```bash
curl -X 'DELETE' \
  'http://localhost:8080/api/books/1' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```
> *Note: uniquement pour les admins*

Book detail
```bash
curl -X 'GET' \
  'http://localhost:8080/api/books/1' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```
Edit book
```bash
curl -X 'PUT' \
  'http://localhost:8080/api/books/1' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}' \
  -H 'Content-Type: application/json' \
  -d '{
  "isbn": "123456789876",
  "title": "New Title",
  "description": "New description",
  "publicationYear": 2025,
  "coverUrl": "",
  "authors": [
    2
  ],
  "genres": [
    2
  ]
}'
```
> *Note: Uniquement pour le créateur du livre ou un admin*

Book reviews
```bash
curl -X 'GET' \
  'http://localhost:8080/api/books/1/reviews' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```

Add review
```bash
curl -X 'POST' \
  'http://localhost:8080/api/books/1/reviews' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}' \
  -H 'Content-Type: application/json' \
  -d '{
  "rating": 2,
  "comment": "Some comment"
}'
```
### Exchanges
All exchanges
```bash
curl -X 'GET' \
  'http://localhost:8080/api/exchanges?index=0&pageSize=2' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```
> *Note: N'affiche que les échanges nous*

Search by status
```bash
curl -X 'GET' \
  'http://localhost:8080/api/exchanges?index=0&pageSize=2&status=PENDING' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```
Ask Exchange
```bash
curl -X 'POST' \
  'http://localhost:8080/api/exchanges' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}' \
  -H 'Content-Type: application/json' \
  -d '{
  "bookId": 1,
  "type": "EXCHANGE"
}'
```
> *Note: Uniquement sur les livres d'autres personnes*

Exchange details
```bash
curl -X 'GET' \
  'http://localhost:8080/api/exchanges/1' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```

Accept exchange
```bash
curl -X 'PATCH' \
  'http://localhost:8080/api/exchanges/1/accept' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```
Refuse exchange
```bash
curl -X 'PATCH' \
  'http://localhost:8080/api/exchanges/1/refuse' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```
> *Note: Seulement si on est le receveur de la demande*

### Library
All books
```bash
curl -X 'GET' \
  'http://localhost:8080/api/library?index=0&pageSize=3' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```
Seach by status
```bash
curl -X 'GET' \
  'http://localhost:8080/api/library?index=0&pageSize=3&status=OWNED' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```
Add book to personnal library
```bash
curl -X 'POST' \
  'http://localhost:8080/api/library' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}' \
  -H 'Content-Type: application/json' \
  -d '{
  "bookId": 1,
  "status": "OWNED",
  "condition": "NEW",
  "isAvailableForExchange": true,
  "isAvailableForLoan": false
}'
```
Library entry details
```bash
curl -X 'GET' \
  'http://localhost:8080/api/library/1' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```
Edit entry
```bash
curl -X 'PUT' \
  'http://localhost:8080/api/library/1' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}' \
  -H 'Content-Type: application/json' \
  -d '{
  "status": "READ",
  "condition": "WORN",
  "isAvailableForExchange": false,
  "isAvailableForLoan": true
}'
```
Delete entry in library
```bash
curl -X 'DELETE' \
  'http://localhost:8080/api/library/1' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```
Consult public library
```bash
curl -X 'GET' \
  'http://localhost:8080/api/users/otman/library?index=0&pageSize=3' \
  -H 'accept: application/json'
```
### Admin
Delete review
```bash
curl -X 'DELETE' \
  'http://localhost:8080/api/admin/reviews/1' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```

List users
```bash
curl -X 'GET' \
  'http://localhost:8080/api/admin/users?index=0&pageSize=3' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```
Delete user
```bash
curl -X 'DELETE' \
  'http://localhost:8080/api/admin/users/1' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer ${TOKEN}'
```
Suspend user
```bash
{
  "id": 1,
  "username": "otman",
  "roles": [
    "USER"
  ],
  "email": "otman@bookswap.fr",
  "active": false,
  "createdAt": "2026-03-24T21:20:36.156938Z"
}
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/exchange-manager-api-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides


## Provided Code

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
