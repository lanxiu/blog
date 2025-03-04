#  **Spring Gateway Run Problems Summary**

## 1 Basic Knowledge

### 1.1 ThreadLocal in Parent and Child Thread

The son thread duplicae a copy of threadlocal from father, and then they are two single objects.

+ Concept: When a child thread is created, it duplicates the parent's ThreadLocal value, resulting in two independent copies.
+ Note: Changes in the child thread's copy do not affect the parent’s ThreadLocal value and vice versa.

### 1.2 spring bean service

Its thread comes from  the method who call them.
In spring mvc, every request has a thread.

+ Execution Context: A Spring Service's methods execute in the thread that calls them.
+ Spring MVC: Each HTTP request is handled by a thread from the container’s thread pool.


### 1.3 Loadbalance & Resilience

loadbalance is used for the ability

resilience is used for the low ability server

+ Load Balancing: Distributes requests across service instances to improve availability and performance.
+ Resilience (Elasticity): Enhances a server’s ability to handle high load or low performance by mechanisms like auto-scaling, circuit breakers, etc.

### 1.4  Filters in Gateway and Service

Prefilter in the gateway extends GlobalFilter is used for the first step to modify a request, such as put some properites.
ResponseFilter in the gateway is used for modify response, such as put properties from request to response.

Prefilter in the service extends Filter is used to use the request,such as put the properties to POJO.
Interceptor implements ClientHttpRequestInterceptor is used to put the properties from original request to  the new request.

This four  filters are also used to track a request by set  a request  id.


+ Gateway Filters:
Pre-filters (GlobalFilter): Modify incoming requests (e.g., add properties).
Response Filters: Modify outgoing responses (e.g., append properties from the request).
+ Service-side Filters:
Servlet Filters: Intercept requests to transform them (e.g., map properties to a POJO).
Interceptors (ClientHttpRequestInterceptor): Forward properties from the original request to a new request.
+ Usage: These filters are commonly used for request tracking (e.g., setting a unique request ID).


### 1.5 Testing Service Discovery

by the service discovery server
+ Via Service Discovery Server:

> http://localhost:8170/eureka/apps/licensing-service

by the client with discovery
+ Via Client with Discovery:

> http://localhost:8080/discovery

by the client with loadbalance
+ Via Client with Load Balancer:

> http://localhost:8080/loadbalance

### 1.6 Test URLs for Gateway

- http://localhost:8072/licensing-service/v1/organization/2233/license/11
- http://localhost:8072/organization/v1/organization/2233/license/11

 

## 2 Spring Gateway Errors and Solutions

### 2.1  Configuration File Errors


#### 2.1.1 routes.url --> routes.uri
Incorrect Property Names
+ Issue: Using routes.url instead of routes.uri.
+ Solution: Change property name to routes.uri.
#### 2.1.2 discovery.locator -->  discovery.locator:
Discovery Locator Syntax
+ Issue: Incorrect formatting for discovery.locator.
+ Solution: Use proper YAML syntax (e.g., discovery.locator: under spring.cloud.gateway).

#### 2.1.3 can't change line, but in actual, it has no problem althogh it doesn't match the regular
Eureka URL Formatting

```
      defaultZone:
        http://${eureka.instance.hostname}:${server.port}/eureka
```
+ Note: Even if the line break appears odd, it may not affect functionality if parsed correctly.

#### 2.1.4 http://localhost:8072/actuator/gateway/routes isn't accessable
Actuator Endpoints Not Accessible

expose the endpoint by modify the config

+ Issue: http://localhost:8072/actuator/gateway/routes returns 404.
+ Solution: Expose the endpoint by modifying the configuration:
```
management:
  endpoints:
    web:
      exposure:
        include: "*"
```


#### 2.1.5 spring-cloud-starter-netflix-eureka-client package can't be imported

Import Errors for spring-cloud-starter-netflix-eureka-client

when start, the error message
+ Issue: Error indicating a missing method in ConnectionProvider.
+ Error Message:
```

Description:

An attempt was made to call a method that does not exist. The attempt was made from the following location:

    org.springframework.cloud.gateway.config.GatewayAutoConfiguration$NettyConfiguration.gatewayHttpClient(GatewayAutoConfiguration.java:597)

The following method did not exist:

    reactor.netty.resources.ConnectionProvider.elastic(Ljava/lang/String;Ljava/time/Duration;)Lreactor/netty/resources/ConnectionProvider;

The method's class, reactor.netty.resources.ConnectionProvider, is available from the following locations:

    jar:file:/Users/lenchol/.m2/repository/io/projectreactor/netty/reactor-netty/0.9.3.RELEASE/reactor-netty-0.9.3.RELEASE.jar!/reactor/netty/resources/ConnectionProvider.class

It was loaded from the following location:

    file:/Users/lenchol/.m2/repository/io/projectreactor/netty/reactor-netty/0.9.3.RELEASE/reactor-netty-0.9.3.RELEASE.jar


Action:

Correct the classpath of your application so that it contains a single, compatible version of reactor.netty.resources.ConnectionProvider
```

change the springboot version
+ Solution: Adjust the Spring Boot and Spring Cloud versions to ensure compatibility. For example:
```
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.2.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <java.version>8</java.version>
        <spring-cloud.version>
            Hoxton.SR1
        </spring-cloud.version>
    </properties>
    <dependencies>
```

#### 2.1.6 Unable to find instance for LICENSING-SERVICE

it costed me the most of the time 

the main reason is add the config

+ Issue: Gateway cannot locate an instance for licensing-service.
+ Main Cause: The configuration setting:

```
    loadbalancer:
      ribbon:
        enabled: false
```
** This is critical to switch from Ribbon to Spring Cloud LoadBalancer.**


othe solutions I don't know wether useful

add config

> preferIpAddress: true

delete config

```
  server:
    waitTimeInMsWhenSyncEmpty: 5
```

change the sensative to higer, it is no use,both are ok

> uri: lb://licensing-service

change the format

> discovery.locator:

to this. but no use, both are ok

```
discovery:
  locator:
```


+ Other Configurations to Check:
  + Setting preferIpAddress: true
+ Removing the property server.waitTimeInMsWhenSyncEmpty: 5
+ Verifying that the URI is set as lb://licensing-service ("licensing-service" is not case sensative  )
+ Using proper YAML formatting for discovery.locator (both discovery.locator: and are acceptable)


** Final Notes**
+ Basic Concepts: Understand how ThreadLocal inheritance works, the execution context of Spring services (synchronous in Spring MVC, asynchronous via @Async, or reactive in Spring WebFlux), and the differences between load balancing and resilience.
+ Gateway Filters: Know the roles of pre-filters and response filters in both the Gateway and the individual services, particularly for tracking requests with unique IDs.
Service Discovery Testing: Verify endpoints using Eureka’s UI or specific URLs.
+ Configuration Pitfalls: Ensure that all YAML keys are correctly named (e.g., routes.uri, not routes.url), that Actuator endpoints are exposed, and that dependency versions are compatible.
+ Load Balancing Configuration: Disabling Ribbon in favor of Spring Cloud LoadBalancer is crucial for instance discovery.








