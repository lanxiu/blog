>> 整理zookeeper 功能及原理  简单版

 

### 服务注册发现

### 域名管理

### LEADER选举

1-10 分别将各自ID发给他人，验证后，半数选举即通过，设定leading following

本算法主要解决一致性问题，以上是最理想情况，不理想的情况包括掉线 消息丢失 重加入等


### 分布式锁

以锁定某个节点，获得锁

主要有共享锁和独享锁

比较redis  优点  同步服务器，可靠性高
           缺点  性能差

参考
[Zookeeper实战——分布式锁实现以及原理](https://blog.csdn.net/KIMTOU/article/details/124846719)
