## JAVA_OPTS

#### JAVA_OPTS ，顾名思义，是用来设置JVM相关运行参数的变量。
http://blog.csdn.net/kongls08/article/details/8468713

JVM:JAVA_OPTS="-server -Xms2048m -Xmx2048m -Xss512k"

-server:一定要作为第一个参数，在多个CPU时性能佳
-Xms：初始Heap大小，使用的最小内存,cpu性能高时此值应设的大一些
-Xmx：Java heap最大值，使用的最大内存
上面两个值是分配JVM的最小和最大内存，取决于硬件物理内存的大小，建议均设为物理内存的一半。
-XX:PermSize:设定内存的永久保存区域
-XX:MaxPermSize:设定最大内存的永久保存区域
-XX:MaxNewSize:
-Xss 15120 这使得JBoss每增加一个线程（thread)就会立即消耗15M内存，而最佳值应该是128K,默认值好像是512k.
+XX:AggressiveHeap 会使得 Xms没有意义。这个参数让jvm忽略Xmx参数,疯狂地吃完一个G物理内存,再吃尽一个G的swap。
-Xss：每个线程的Stack大小
-verbose:gc 现实垃圾收集信息
-Xloggc:gc.log 指定垃圾收集日志文件
-Xmn：young generation的heap大小，一般设置为Xmx的3、4分之一
-XX:+UseParNewGC ：缩短minor收集的时间
-XX:+UseConcMarkSweepGC ：缩短major收集的时间
提示：此选项在Heap Size 比较大而且Major收集时间较长的情况下使用更合适。



## top命令

. TOP是一个动态显示过程,即可以通过用户按键来不断刷新当前状态.如果在前台执行该命令,它将独占前台,
直到用户终止该程序为止.比较准确的说,top命令提供了实时的对系统处理器的状态监视.它将显示系统中CPU最“敏感”的任务列表.
该命令可以按CPU使用.内存使用和执行时间对任务进行排序；而且该命令的很多特性都可以通过交互式命令或者在个人定制文件中进行设定.

















