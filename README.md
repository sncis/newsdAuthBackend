# newsdAuthentication

> For the sake of simplicity I left the repository private and populated some credentials.
> This was on perpuse to make it simple to use the funcktionalities without creating own mail account etc.:)

newsdAuthentication is a simple REST API to authente users and retriving stored Articles by these users. It serves as Backend Service for the NewsdMe app.
The Frontend for this Application can be found in this [Git Repo](https://github.com/snzew/newsd).
A live version of the whole Application is hosten on Heroku and can be found here --> [NewsdMe](https://newsdme.herokuapp.com/)

This REST Api has two different Endpoints one for Autehntication and one for Articles which were stored by autenticatd users.

There is also one minor Endpoint for `Admins` which is only for demonstrating a Role based Authorisation concept.

The project showcase some Security measurements taken to protect a Rest Api which are presented in the Security section of this README file. It is build with `Spting Boot` and uses the `Spring Security` Framwork to customize authentication and access-control.




## Endpoints 

newsdAuthentication has 3 endpoints:

1. **Auth**  - `/auth` with the subdomains `/register`,  `/login`, `logout` and  `/confirm` --> Endpoint allowed for unauthenticated users. Execpt from */auth/logout* which is 'restricted' by custom JWT Filter.

  * POST `/auth/register` - creates a user entity in the database which is disabled until confirmation. It takes in a RequestBody with a User object in `JSON format` with username, password and email address and return a HTTP Status code 201 when user is created.
  It Creates also a RegistrationConfrimation token which is send to the porvided email address to confirm identity. 
  
  * POST `/auth/login` - login user based on username and password. Takes in a Request Body with username and password and creates a JWT Token.  The JWT token is set into a HTML Only cookie and has to be included in all further requests to authenticate the user.
  It returnes a 200 if user is successfully logedin.
  
  * GET `/auth/logout` - logs out a user cleare session, data and  return 200 if successfuly loged out. 
  
  * GET `/auth/confirm` Request parameter `token`- enable a User after registration. It takes a **token** as request parameter which is a RegistrationToken String generated while Registration and send by Email to User's email address.


2. **Article** - `/articles` with the subdomain `/article` --> Endpoint is restricted for authenticated users with roles USER or ADMIN
  
   * GET `/articles` request parameter **username** - Return an Array of all saved articles for one specific user. Takes a **username** as request parameter.
   
   * POST `/articles` request parameter **username** - save one Article fo a given user. Takes a **username** as request parameter and a **article** in `JSON` format as request body. Return the saved article when succesfully saved. 
  
   * DELETE `/articles/article` request parameter **id** - delete one article based on article ID. Return Status 200 when article is succesfully deleted.
   
   
   
3. **Admin** - `/admin/`
   * GET `/admin` - restriced entpoint for Admins. Only for Role base authorisation demonstartion. Return a Greeting String 



## Getting Started

newsdAuthentication in a `Java Sping Boot` Application which used `Java 11`. In addition it is using `maven` as project management tool.
To send emails to users to confirm their registration the `GMail` mail client is used. For the sake of simplicity you can use the credentials from in the application-dev.properties file.
### Prerequisit
To run the Application locally make sure to met the following prerequisits:
  * having Java 11 installed on your system
  * having maven installed on your system. If you dont have `maven` already installed you can follow the instructions at [maven.apache.org](https://maven.apache.org/)


## Environement configurations
By default the App runns with https for which you need a self-signed SSL-Certiicate which will be explained in the **Enable HTTPS** section. If you don't want to run the app with https
you can also easily disable it.

### Disable HTTPS
HTTPS ins enabled by default. To disable HTTPS disable go to the `applications-dev.properties` file in the root directory and disable lines

```
server.port=8443
server.ssl.key-store=*path/to/your/certificate/*
server.ssl.key-store-type=PKCS12
server.ssl-key-password=changeit
```
... go to `WebSecurityConfig` file in the `config` package and disable the following three lines

```java
http.requiresChannel()
        .requestMatchers(matcher -> matcher.getHeader("X-Forwarded-Proto") !=null)
        .requiresSecure();
 ```
 
### Enable HTTPS (enabled by default) 
If you want to run the app with HTTPS you need a self-signed SSL-Certificate [mkCert](https://github.com/FiloSottile/mkcert) provides an easy what to create your own certificate for your local environemnt. You can of cours use any other SSL-Certficate.
mkCert will configure your computer as a trusted Certification Authority and lets you create a certificate in the JAVA spefific pkcs12  format. 

To create a certificate for localhost run: 
```
$ mkcert -pkcs12 localhost
```
... this will create a certificate in the location where you run the command. You can place the certificate in any location and set the path to it in the appliaction properties. 
By default the password is `changeit`


After creating a certificate you need to set the properties of the certificate to it in the `application-dev.properties` as follow 

```
server.port=8443  --> default port for https
server.ssl.key-store=*path/to/your/certificate/* --> file name is localhost.p12
server.ssl.key-store-type=PKCS12 --> format of the certificate
server.ssl-key-password=changeit --> default password
```

... to redirect all incomming http request and ensure https make sure this lines are enabled: 

```java
http.requiresChannel()
        .requestMatchers(matcher -> matcher.getHeader("X-Forwarded-Proto") !=null)
        .requiresSecure();
 ```
 


### Running newsdAuthentication locally

To build the application with `maven` navigate to the root appliation folder an run:

``` 
mvn install
```

... you can also run it with a clean flag to delete all previously compiled JAVA files by running: 

```
mvn clean install
```


... after having compiled the code you can run the product in `dev` environemnt by executing the following command:

```
java -jar -Dspring.profiles.active=dev target/newsdAuthentication-0.0.1-SNAPSHOT.jar 
```

For security purposes the application is configured to only allow request from the NewsdMe Fronted which avalable in this [Git Repository](https://github.com/snzew/newsd/)
To change that you could change the allowed origines to your prefered oriin in the `WebSecurityConfig` class in the `config` package.
To do so change the allowedorigine method in the `corsConfigurationSource` Bean: 

```

  @Bean
  public CorsConfigurationSource corsConfigurationSource(){
    final CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOrigins(Arrays.asList("https://localhost:3000" ,"https://newsdme.herokuapp.com")); ---> origins to change
    config.setAllowCredentials(true);
    config.setAllowedMethods(Arrays.asList("HEAD",
        "GET", "POST", "DELETE", "OPTIONS"));
  
  ```
  
  
  
