# newsdAuthentication

> For convenience, I have created some test accounts and users credentials which can be used to test the app.
> This was on purpose to make it simple and to give you the possibility to use the whole functionalities without creating own mail account, API Key etc.    

newsdAuthentication is a simple REST API for authenticating users and retrieving stored Articles from these users. It serves as Backend Service for the NewsdMe app.
The Frontend for this Application can be found in this [Git Repo](https://github.com/snzew/newsd).  
A live version of the whole Project is hosted on Heroku and can be found here --> [NewsdMe](https://newsdme.herokuapp.com/)  

This REST Api has two main Endpoints, one for Authentication and one for Articles stored by authenticated users. 
Once registered, you will get a Email to the provided email Address, which will redirect you to the page to confirm your registration. You con also resend the registration code if you missed it. 
If you have more then 5 failed login Attempts your Ip address will be blocked for 5 minutes. This configurations (maxAttempts and time) cna be configured (see section `Restricting login attempts`).
There is also one minor Endpoint for `Admins` which is only for demonstrating a role based authorisation concept.  


In Addition, the project showcases some security measurements taken to protect a REST Api. These measurements are presented in the Security section of this README file. It is build with `Spring Boot`with `maven` and uses the `Spring Security` Framework to customize authentication and access-control.  
Some of them are: filtering incoming request, having costume validators to validate data before storing them, implemented security headers and using HTTPS . 
a Whole list can be found at the end of this file. 
  
  

## Endpoints
  
newsdAuthentication has 3 endpoints:  

1. **Auth**  - `/auth` with the subdomains `/register`,  `/login`, `logout` and  `/confirm` --> Endpoint allowed for unauthenticated users. Except from */auth/logout* which is 'restricted' by custom JWT Filter.  
  
  
   * POST `/auth/register` - creates a user entity in the database which is disabled until confirmation. It takes in a     RequestBody with a User object in `JSON format` with username, password and email address and returns a HTTP Status     code 201 when the user is created.  
    It Creates also a RegistrationConfirmationToken which is send to the provided email address to confirm users’  identity.  
    * POST `/auth/login` - login user based on username and password. Takes in a Request Body with username and     password and creates a JWT Token.  The JWT token is set into a HTML Only cookie and has to be included in all further   requests to authenticate the user. It returns a 200 if the user is successfully logged in.  
     
     * GET `/auth/logout` - logout a user and clear session, data and  return 200 if successfully logged out.  
    
    * GET `/auth/confirm` Request parameter `token`- enable a User after registration. It takes a **token** as a request   parameter which is a RegistrationToken String generated while Registration and sent by Email to the User's email     address.  



2. **Article** - `/articles` with the subdomain `/article` --> Endpoint is restricted for authenticated users with    roles USER or ADMIN.  
  
   * GET `/articles` request parameter **username** - Return an Array of all saved articles for this specific user.   
   Takes a **username** as a request parameter.  
     
   * POST `/articles` request parameter **username** - save one article of a given user. Takes a **username** as   
   request parameter and a **article** in `JSON` format as a Request Body. Return the saved article when successfully 
   saved.  
  
   * DELETE `/articles/article` request parameter **id** - delete one article based on article ID. Return Status 200  when the article is successfully deleted.  



3. **Admin** - `/admin/`  
   * GET `/admin` - restricted endpoint for Admins. Only for Role based authorisation demonstration. Return a Greeting    String.  
 
 
 
 ## Getting Started
  
newsdAuthentication in a `Java Spring Boot` Application built with `Java 11`. As project management tool it is using `maven` and as database `Postgres`.  
  
To send emails to users to confirm their registration the `GMail` mail client is used. For the sake of simplicity the credentials are populated in the in the application-dev.properties file.  



### Prerequisite
To run the Application locally make sure to met the following prerequisites:  
  * having Java 11 installed on your system
  * having maven installed on your system. If you don't have `maven` already installed you can follow the instructions  at [maven.apache.org](https://maven.apache.org/)  


  
## Environnement configurations

By default the App runs with https for which you need a self-signed SSL-Certificate which will be explained in the **Enable HTTPS** section. If you don't want to run the app with https
you can also easily disable it.  
  
### Disable HTTPS
HTTPS is enabled by default. To disable HTTPS disable go to the `applications-dev.properties` file in the root directory and disable lines

```
server.port=8443
server.ssl.key-store=*path/to/your/certificate/*
server.ssl.key-store-type=PKCS12
server.ssl-key-password=changeit
```
 
... go to `WebSecurityConfig` file in the `config` package and disable the following three lines:

```java
http.requiresChannel()
        .requestMatchers(matcher -> matcher.getHeader("X-Forwarded-Proto") !=null)
        .requiresSecure();
 ```
  
    
### Enable HTTPS (enabled by default) 
If you want to run the app with HTTPS you need a self-signed SSL-Certificate [mkCert](https://github.com/FiloSottile/mkcert) provides an easy way to create your own certificate for your local environment. You can of course use any other SSL-Certificate.\
mkCert will configure your computer as a trusted Certification Authority and lets you create a certificate in the JAVA specific pkcs12  format.  
  
  
To create a certificate for localhost run:
```
$ mkcert -pkcs12 localhost
```

... this will create a certificate in the location where you run the command. You can place the certificate in any location and set the path to it in the application properties. 
By default the password is `changeit`.  
  
    
After creating a certificate you need to set the properties of the certificate to it in the `application-dev.properties` as follow

```
server.port=8443  --> default port for https
server.ssl.key-store=*path/to/your/certificate/* --> file name is localhost.p12
server.ssl.key-store-type=PKCS12 --> format of the certificate
server.ssl-key-password=changeit --> default password
```
  
  
... to redirect all incoming http requests and ensure https make sure these lines are enabled:

```java
http.requiresChannel()
        .requestMatchers(matcher -> matcher.getHeader("X-Forwarded-Proto") !=null)
        .requiresSecure();
 ```

### Adding a Database Connection
To run the app locally, you would nee to connect a local Database. You could either connect to your own local Database by editing\n
the connection details in the `application-dev.properties` file or use the credentials form the `application-test.properties` file:

lines to change:

```$xslt
spring.datasource.url= <uri to DB>
spring.datasource.username= <username>
spring.datasource.password= <password if existing>

```
  
### Running newsdAuthentication locally
To build the application with `maven` navigate to the root application folder and run:

``` 
mvn install
```
  
  
... you can also run it with a clean flag to delete all previously compiled JAVA files by running: 

```
mvn clean install
```
  
  
... after having compiled the code you can run the product in `dev` environment by executing the following command:

```
java -jar -Dspring.profiles.active=dev target/newsdAuthentication-0.0.1-SNAPSHOT.jar 
```

  
    
    
For security purposes the application is configured to only allow request from the NewsdMe Fronted which available in this [Git Repository](https://github.com/snzew/newsd/)
To change that you could change the allowed origines to your prefered origin in the `WebSecurityConfig` class in the `config` package.  
  
To do so change the `allowedOrigins` method in the `corsConfigurationSource` Bean: 

```

  @Bean
  public CorsConfigurationSource corsConfigurationSource(){
    final CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOrigins(Arrays.asList("https://localhost:3000" ,"https://newsdme.herokuapp.com")); ---> origins to change
    config.setAllowCredentials(true);
    config.setAllowedMethods(Arrays.asList("HEAD",
        "GET", "POST", "DELETE", "OPTIONS"));
  
  ```

### Restricting login attempts
To prevent Brute Force attacks, login attempts are restricted and the given IP address is blocked for a given amount of time.\n
The number of Login tries and the time for how long the IP Address is blocked can be customised. 

To change the possible failed login attempts change the MAX_ATTEMPTS in the LoginFailureService in the service directory.
```$xslt

@Service
public class LoginFailureService {

  @Autowired
  HttpServletRequest request;

  private final int MAX_ATTEMPTS = 5;
  private LoadingCache<String, Integer> cachedAttempts;
```
 
 
... to change the time for how long the IP Address is blocked can be changed in the same file by changing "expiresAfterWrites" method in the constructor.

```$xslt

  public LoginFailureService(){
    super();
    cachedAttempts = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
      @Override
      public Integer load(String s) throws Exception {
        return 0;
      }
    });

  }

```


 
 
## Security Measurements 

The following list shows different security measurements which had been taken to secure the application. This is not a list of all possible measurements for web application security, but shows what is implemented and applied in the project.

### List of implemented security measurements 

#### Transportation Layer
* security headers
* CORS Configuration (Working on local environment. In Production their is an unknown issue and I was not able to fix it yet, due to time reasons.)
* only HTTPS connections are allowed for local development and public version hosted on Heroku


#### Application Layer 
* Authentication based on username & password and JWT Token
* Password encoding
* Rolebased Authorisation 
* Email verification for Registration
* Custom application entrypoint filters
* validating incoming Article data based on JSON Schema
* Logging 
* Custom validators to validate incoming data
* Restricting Login attempts
* Rejecting too long request body
  
