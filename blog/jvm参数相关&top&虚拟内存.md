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
http://www.jb51.net/LINUXjishu/34604.html
> TOP是一个动态显示过程,即可以通过用户按键来不断刷新当前状态.如果在前台执行该命令,它将独占前台,
直到用户终止该程序为止.比较准确的说,top命令提供了实时的对系统处理器的状态监视.它将显示系统中CPU最“敏感”的任务列表.
该命令可以按CPU使用.内存使用和执行时间对任务进行排序；而且该命令的很多特性都可以通过交互式命令或者在个人定制文件中进行设定.

%MEM
进程使用的物理内存百分比
VIRT
进程使用的虚拟内存总量，单位kb。VIRT=SWAP+RES
SWAP
进程使用的虚拟内存中，被换出的大小，单位kb。
RES
进程使用的、未被换出的物理内存大小，单位kb。RES=CODE+DATA
CODE
可执行代码占用的物理内存大小，单位kb
DATA
可执行代码以外的部分(数据段+栈)占用的物理内存大小，单位kb
SHR
共享内存大小，单位kb


## [增加虚拟内存](http://blog.csdn.net/shunzi19860518/article/details/4828490)
   [创建和启用Swap交换区](http://www.cnblogs.com/zsummer/p/4808422.html)


1. 打开终端，切换到root用户，输入：free -m查看内存状态
[root@lxt lxt]# free -m
             total       used       free     shared    buffers     cached
Mem:           498        357        141          0         27        162
-/+ buffers/cache:        167        331
Swap:         1023          0       1023


2. 输入df -B M（或df -m）查看各分区当前使用情况
[root@lxt lxt]# df -B M
文件系统               1M-块        已用     可用 已用% 挂载点
/dev/mapper/vg_lxt-lv_root
                        12875M     4059M     8162M  34% /
/dev/sda7                 194M       14M      170M   8% /boot
tmpfs                     250M        1M      249M   1% /dev/shm
（fdisk -l可查看磁盘分区情况）

3. 选择一个较大的分区，建立分区文件：
[root@lxt lxt]# dd if=/dev/zero of=/swapadd bs=1024 count=524288
524288+0 records in
524288+0 records out
536870912 bytes (537 MB) copied，13.0709 秒，41.1 MB/秒
以上命令在根目录新建一个名为swapadd，大小为512M的虚拟内存文件

4. 移动该文件到空间较大的其他分区：
[root@lxt lxt]# mkdir /mnt/swap
[root@lxt /]# mv swapadd /mnt/swap

5. 执行以下命令启用虚拟内存并重启电脑
[root@lxt /]# mkswap /mnt/swap/swapadd
Setting up swapspace version 1, size = 524284 KiB
no label, UUID=a5c8b651-6f64-4414-bb5f-580b742acfce
[root@lxt /]# swapon /mnt/swap/swapadd
查看内存：
[root@lxt /]# free -m
             total       used       free     shared    buffers     cached
Mem:           498        492          6          0         15        302
-/+ buffers/cache:        174        323
Swap:         1535          0       1535

6. 如果不需要使用新增的虚拟内存，则输入：
swapoff -v /mnt/swap/swapadd






