# URSS (Ultra Rapid Support Service)

## REST API 

* Jersey: JAX-RS reference implementation for creating RESTful web services in Java.
* Jackson: JSON parser for Java.
* Weld: CDI reference implementation.
* Hibernate ORM: Persistence framework for relational databases (JPA implementation).
* Arquillian: Testing framework (under JUnit 4).
* JJWT: Library for creating and parsing JSON Web Tokens (JWTs) in Java.
* JBCrypt: Implementation for the BCrypt password hashing function in Java.
* PostgreSQL: Relational database.

## REST API overview

See the [curl] scripts below with the REST API supported operations:

### Authentication (JSON Web Token)

#### Authenticate (JSON Web Token)

```  
curl -X POST 'http://localhost:8080/api/a' \
    -H "Content-Type: application/json" \
    --data '{ 
         "username": "a.diongue", "password": "secret-password"
    }'
```

#### Refresh (JSON Web Token)
:unlock:

```  
curl -X GET 'http://localhost:8080/api/a' \
    -H 'Authorization: Bearer <jwt>'
```

##### Header (JSON Web Token)

How to authenticate with the token you received from `GET /api/a HTTP/1.1` ?

Add to request the following element
```  
curl -X <method> 'http://localhost:8080/api/<request-path>' \
    -H 'Authorization: Bearer <jwt>'
```
where `<jwt>` is the token.

##### Header (Konami Code) (cheat code to manually check API)

What is Konami Code ? :boom:<br/>

To simplify develpment you cheat and falsify authentication process by just telling what is your(s) role(s). <br/>
Therefore you don't need to authenticate first and your access can't expire. :see_no_evil:

```  
curl -X <method> 'http://localhost:8080/api/<request-path>' \
    -H 'Authorization: Konami Code <authorities>'
```
here `<authorities>` is a string formatted as an JSON array of strings.

### User

#### Get all users
:closed_lock_with_key: Administrator

```
curl -X GET 'http://localhost:8080/api/u'
```

#### Get a user by id or username
:closed_lock_with_key: Administrator <br/>
`<user>` is an identifier (integer) or a username (string).

``` 
curl -X GET 'http://localhost:8080/api/u/<user>' 
```
#### Create a user
:closed_lock_with_key: Administrator

```  
curl -X POST 'http://localhost:8080/api/u' \
    -H "Content-Type: application/json" \
    --data -d '{ 
        "firstName": "Lucas", "lastName": "David", 
<<<<<<< HEAD
        "email": "abdoulaye.diongued@universite-paris-saclay.fr", 
        "username": "abdoulaye.diongue", "password": "secret-password"
=======
        "email": "lucas.david@u-psud.fr", 
        "username": "lucas.david", "password": "secret-password"
    }'
```

#### Update a user
:closed_lock_with_key: Administrator <br/>
`<user>` is an identifier (integer) or a username (string).

##### merge-patch method

```  
curl -X PATCH 'http://localhost:8080/api/u/<user>' \
    -H "Content-Type: application/merge-patch+json" \
    --data '{ 
        "email": "abdoulaye.diongue@universite-paris-saclay.fr"
>>>>>>> master
    }'
```

#### Delete a user
:closed_lock_with_key: Administrator <br/>
`<user>` is an identifier (integer) or a username (string).

```  
curl -X DELETE 'http://localhost:8080/api/u/<user>'
```

[curl]: https://curl.haxx.se/
