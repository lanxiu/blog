 








# **Spring Boot Example Practice**
 
## 1 Intellij IDEA

### 1.1: Maven Command  

In IntelliJ IDEA, the Maven commands are available on the right sidebar when you open the pom.xml file.

### 1.2:  SpringBootApplication unavailable

erro message in pom.xml

> cannot reconnect

open menu Helper->Show Log in Explorer ,search "cannot reconnect"

> Caused by: java.io.EOFException


Copy the contents of pom.xml, delete the file, and create a new one. Paste the content back in, which should resolve the issue.


### 1.3 Internationalization error

```

Fri Feb 28 16:30:21 CST 2025
There was an unexpected error (type=Bad Request, status=400).
Failed to convert value of type 'java.lang.String' to required type 'java.util.Locale'; nested exception is java.lang.IllegalArgumentException: Locale part "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7" contains invalid characters
```

Here are the potential causes:

+ First: The configuration in the Java file uses setBasename("messages"), but the actual file is named message.properties.

+ Second:, The request header contains a malformed locale en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7. The code can't work.

>  @RequestHeader(value = "Accept-Language",required = false)Locale locale


+ Third: The file messages_en_US.properties should be renamed to message_en.properties.

 + Fourth: Ensure the properties file is in the correct location. Instead of /resources, it should be in the root directory in classpath.  


  ### 1.4 Curl Test Error

I want to  use curl instead of postman ,so I used this command

```
curl -H "Content-Type:application/json" -X post -d ‘{"id":"6655","productName":"dragonquest","licenseType":"dragon","description":"nibolonggen",}’ "http://localhost:8080/v1/organization/2233/license"
```

#### 1.2.1  key&board
change the English key&board, but the foolish mac textedit change it back to  the Chinese formate
the symbols " ' , are wrong

  the error message puzzled me a long time

> curl: (3) URL rejected: Port number was not a decimal number between 0 and 65535

 #### 1.2.2 sensative
 change the format to plain text
  ```

curl -H "Content-Type:application/json" -H "Accept-Language:en-US" -X put -d '{"id":"6655","productName":"dragonquest","licenseType":"dragonB","description":"nibolonggen","licenseId":"6655"}' 'http://localhost:8080/v1/organization/2233/license'
  ```

  but the error was also here
```

{"timestamp":"2025-02-28T12:14:00.065+0000","status":405,"error":"Method Not Allowed","message":"Request method 'put' not supported","path":"/v1/organization/2233/license"}
```

#### 1.2.3   no response
checked my code ,and found no miss, when I see the chrome debugger, I change the  post to POST, then it work

```

curl -H "Content-Type:application/json" -H "Accept-Language:en-US" -X PUT -d '{"id":"6655","productName":"dragonquest","licenseType":"dragonB","description":"nibolonggen","licenseId":"6655"}' 'http://localhost:8080/v1/organization/2233/license'
  ```

 but when I test get command , no response
the reason is return null in my code , I forgot to modify it.


  #### 1.2.5 right command
  ```

curl -H "Content-Type:application/json" -H "Accept-Language:en-US" -X POST -d '{"id":"6655","productName":"dragonquest","licenseType":"dragon","description":"nibolonggen",}' 'http://localhost:8080/v1/organization/2233/license'
  ```
 ```

curl -H "Content-Type:application/json" -H "Accept-Language:en-US" -X GET   'http://localhost:8080/v1/organization/2233/license/6655'
  ```
```
  
curl -X DELETE   http://localhost:8080/v1/organization/2233/license/6655
```
### 1.5 docker

  #### 1.5.1 Base vs. Multi-stage Builds

The primary difference between base and multi-stage builds lies in image management rather than the build process itself. Multi-stage builds allow for smaller images by copying only the necessary files from a build stage into the final image.

  ### 1.6 file transfer and execution

send file from mac to linux 

> scp spring-boot-demo-1.0.0.jar root@leader20:/home

Then, execute the file:

> java -jar spring-boot-demo-1.0.0.jar

# SUMMARY


The most challenging part of this process was troubleshooting errors that were misleading, and the unfamiliar development environment.




