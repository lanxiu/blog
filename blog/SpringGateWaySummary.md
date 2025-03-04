#  **Spring Gateway Run Problems Summary**

## 1 basic knowledge

### 1.1 ThreadLocal in father  and son  thread

The son thread duplicae a copy of threadlocal from father, and then they are two single objects.

### 1.2 spring bean service

Its thread comes from  the method who call them.
In spring mvc, every request has a thread.

### 1.3 loadbalance & resilience

loadbalance is used for the ability

resilience is used for the low ability server

### 1.4 the filter in  the gateway and service

Prefilter in the gateway extends GlobalFilter is used for the first step to modify a request, such as put some properites.
ResponseFilter in the gateway is used for modify response, such as put properties from request to response.

Prefilter in the service extends Filter is used to use the request,such as put the properties to POJO.
Interceptor implements ClientHttpRequestInterceptor is used to put the properties from original request to  the new request.

This four  filters are also used to track a request by set  a request  id.

### 1.5 test the service discovery

by the service discovery server

> http://localhost:8170/eureka/apps/licensing-service

by the client with discovery

> http://localhost:8080/discovery

by the client with loadbalance

> http://localhost:8080/loadbalance

### 1.6 the test url used for gateway

> http://localhost:8072/licensing-service/v1/organization/2233/license/11
> http://localhost:8072/organization/v1/organization/2233/license/11

 

## 2 Spring Gateway Error

### 2.1 config file error

#### 2.1.2 routes.url --> routes.uri
#### 2.1.3 discovery.locator -->  discovery.locator:
#### 2.1.4 can't change line, but in actual, it has no problem althogh it doesn't match the regular

```
      defaultZone:
        http://${eureka.instance.hostname}:${server.port}/eureka
```

#### 2.1.5 http://localhost:8072/actuator/gateway/routes isn't accessable

expose the endpoint by modify the config
```
management:
  endpoints:
    web:
      exposure:
        include: "*"
```


#### 2.1.6 spring-cloud-starter-netflix-eureka-client package can't be imported

when start, the error message
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

#### 2.1.7 Unable to find instance for LICENSING-SERVICE

it costed me the most of the time 

the main reason is add the config

```
    loadbalancer:
      ribbon:
        enabled: false
```

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














