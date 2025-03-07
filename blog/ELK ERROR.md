
## **1 ELK ERROR**
### 1.1 Maven dependence error

> Cannot resolve org.apache.commons:commons-pool2:2.7.0


+ Solution: move the location from the file end to the middle
```
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <type>jar</type>
        </dependency>
```

###  1.2 The message usage

+ Send

```
@EnableBinding（Source.class）
simplesourcebean
source.output.send()
```

+ Receive
```
@EnableBinding(Sink.class)
@StreamListener(Sink.input)

```

### 1.3 @Value Error

> @Value("${redis.port)")
+Correct
> @Value("${redis.port}")


###  1.4 Docker install zookeeper kafka redis 

> kafka redis download ，but zookeeper download error

+ Solution: retry three times and more

+ the config wasn't use ,but maybe useful in the future

```

{
"dns": ["8.8.8.8", "8.8.4.4"],
"registry-mirrors": [
"https://docker.m.daocloud.io/",
"https://huecker.io/",
"https://dockerhub.timeweb.cloud",
"https://noohub.ru/",
"https://dockerproxy.com",
"https://docker.mirrors.ustc.edu.cn",
"https://docker.nju.edu.cn",
"https://xx4bwyg2.mirror.aliyuncs.com",
"http://f1361db2.m.daocloud.io",
"https://registry.docker-cn.com",
"http://hub-mirror.c.163.com"
],
"runtimes": {
"nvidia": {
"path": "nvidia-container-runtime",
"runtimeArgs": []
}
}
}

```

 

### 1.5 Formate Error

```

  kafka:
    image: wurstmeister/kafka:latest
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_LISTENERS: INSIDE://kafka:9092
      KAFKA_ADVERTISED_HOST_NAME: kafka
      KAFKA_LISTENER_SECURITY_PROTOCOL: PLAINTEXT
      KAFKA_CREATE_TOPICS: dresses:1:1,rating:1:1
      KAFKA_LISTENER_NAME_INSIDE: INSIDE
      KAFKA_LISTENER_PORT: 9092
      KAFKA_LISTENER_INTER_BROKER_PROTOCOL: INSIDE
      KAFKA_LISTENERS: INSIDE://0.0.0.0:9092
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INSIDE:PLAINTEXT
      KAFKA_LISTENER_NAME: INSIDE
    volumes:
      - "/home/docker.sock:/var/run/docker.sock"
    depends_on:
      - zookeeper
    networks:
      backend:
        aliases:
          - "kafka"
```

+ Correct

```
  kafka:
    image: wurstmeister/kafka:latest
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT
      KAFKA_CREATE_TOPICS: "dresses:1:1,rating:1:1"
    depends_on:
      - zookeeper
    networks:
      backend:
        aliases:
          - "kafka"
```

exec

> docker compose up -d kafka

### 1.6 Error connecting to node kafka:9092

+ Solution

> add kafka to /etc/hosts


###  1.7 RedisConnectionFailureException

+ Error Message

> RedisConnectionFailureException: java.net.SocketTimeoutException: Read timed out; 

+ Check: the result is ok 

> docker exec -it redis redis-cli
> redis-cli CONFIG GET protected-mode

+ Reason: my VPN Proxy is running ,it uses the alias not the ip

### 1.8 No instances available for localhost

+ Check:

The licensing-service  can't find instance, but  the  eureka server  is running well and the service xml can be access

+ Solution:
wait  a moment , and it's ok

### 1.9 Logstash Connection Error

+ Error Message

> Log destination leader20:5044: Waiting 28178ms before attempting 

+ Analyse

```
telnet leader20 5044  

Trying 192.168.174.20...
telnet: connect to address 192.168.174.20: Connection refused
telnet: Unable to connect to remote host


[root@localhost home]# netstat  -an |grep 5044
tcp        0      0 0.0.0.0:5044            0.0.0.0:*               LISTEN     
tcp6       0      0 :::5044                 :::*                    LISTEN     
[root@localhost home]# netstat  -an |grep 6379
tcp        0      0 0.0.0.0:6379            0.0.0.0:*               LISTEN     
tcp6       0      0 :::6379                 :::*                    LISTEN     

nc -zv localhost 5044

sudo systemctl stop firewalld

sudo firewall-cmd --zone=public --add-port=5044/tcp --permanent
sudo firewall-cmd --reload
```

+ reason:

The port is set to 5000 in logstash.conf

+ question:

why the 5044 can be accessed when stopping  the firewall

### 1.10 Elaticsearch Memory Error

+ Error Message:

```
Exception in thread "main" java.lang.RuntimeException: starting java failed with [1]
output:
#
# There is insufficient memory for the Java Runtime Environment to continue.
# Native memory allocation (mmap) failed to map 406847488 bytes for committing reserved memory.
# An error report file with more information is saved as:
# logs/hs_err_pid48.log

```

+  Solution:

add the virtual machine memory

> free -h

> docker compose up -


### 1.11  Docker Compose Start

+ need the docke-compose file  
+ need Docker start

> systemctl status docker
> systemctl start docker


### 1.12 Elaticsearch Connection Error

+ Error Message: 

```
[2025-03-06T10:53:51.450+00:00][ERROR][elasticsearch-service] Unable to retrieve version information from Elasticsearch nodes. connect ECONNREFUSED 172.19.0.2:9300
[2025-03-06T10:53:56.771+00:00][INFO ][plugins.screenshotting.chromium] Browser executable: /usr/share/kibana/x-pack/plugins/screenshotting/chromium/headless_shell-linux_x64/headless_shell
[2025-03-06T10:54:34.446+00:00][ERROR][elasticsearch-service] Unable to retrieve version information from Elasticsearch nodes. Parse Error: Expected HTTP/
```

+ Analyse:

The port should change to 9200

both the formats are right

> ELASTICSEARCH_HOSTS=http://elasticsearch:9200
> ELASTICSEARCH_HOSTS: "http://elasticsearch:9200"
 
Use command  
> docker compose up -d elaticsearch

Command can't apply the change  in docker-compose file

> docker compose restart elaticsearch



###  1.13 Seluth Log

It only convert the logging messages, so use this dependency to change log4j messages

```

        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-to-slf4j</artifactId>
        </dependency>

```

### 1.14 Tips

+ Message Service  Url

>  http://localhost:8080/send

+ Docker Command

```

docker ps
docker logs kafka

docker exec -it <container_name_or_id> bash
cd /var/log/logstash
rm -f *.log
```
+ Port Check

>  nc -zv leader20 5601





