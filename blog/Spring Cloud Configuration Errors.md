# **Spring Cloud Configuration Errors**

## 1 bootstrap.yml File Format

Ensure the correct spacing in the configuration file. For example:

Wrong format
 > port:8071
Correct format
> port: 8071

## 2 bootstrap.yml Configuration

When configuring Spring Cloud, ensure the correct key names and structure.

Wrong format
```
spring:
  application:
    name:config-server
  profile:
    active:native
```

Correct format

```
spring:
  application:
    name: config-server
  profiles:
    active: native
```
Notice that the key profiles is used instead of profile. The name: config-server format has no space, but it doesn’t cause an error. However, it’s always best practice to follow the proper conventions.

## 3 Test addresses

Use these URLs to verify your configuration is working correctly in different profiles.

> http://localhost:8071/licensing-service/default
> http://localhost:8071/licensing-service/dev

## 4 Using @ConfigurationProperties

For example, to bind configuration properties with a prefix, use the @ConfigurationProperties annotation. This annotation will map values to a POJO (Plain Old Java Object) based on the provided prefix.

> @ConfigurationProperties(prefix = "example")

This will automatically set properties starting with example (e.g., example.aa) into the fields of the corresponding POJO.


