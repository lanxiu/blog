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
提示：此选项在Heap Size 比较大而且Major收集时间较长的情况下使用更合适  

可结合-XX:+PrintFlagsInitial与-XX:+PrintFlagsFinal对比设置前、设置后的差异  
使用 jinfo 命令 查看或设置某个参数的值 
 -XX:+PrintCommandLineFlags参数  
 
#### 查看正在运行的JVM的参数

查看JVM进程的PID

    $ jcmd -l
      27940 sun.tools.jcmd.JCmd -l
      24684 org.codehaus.plexus.classworlds.launcher.Launcher -Prun
      23839 com.intellij.idea.Main
      23951 org.jetbrains.idea.maven.server.RemoteMavenServer
     
 查看进程24684的参数
 >    $ jcmd 24684 VM.flags
      24684:
      -XX:InitialHeapSize=98566144 -XX:MaxHeapSize=1547698176 \
      -XX:MaxNewSize=515899392 -XX:MinHeapDeltaBytes=524288 \
      -XX:NewSize=1572864 -XX:OldSize=96993280 \
      -XX:+UseCompressedClassPointers \
      -XX:+UseCompressedOops -XX:+UseParallelGC 


## [虚拟内存](http://blog.csdn.net/zwan0518/article/details/12059213)

   [Linux内存介绍以及C与C++内存管理](http://blog.csdn.net/zwan0518/article/details/9040467)
   

#### 背景介绍

对于Linux来说，其在服务器市场的使用已经占据了绝对的霸主地位，不可动摇。Linux的各种设计思想和使用也被传承(当然不乏各种黑Linux，而且黑的漂亮)。Linux的很多独特的设计，对性能也产生了巨大的提升，也为其他应用软件和系统提供了参考。这篇文章介绍一下Linux中swap与memory。

  对于memory没什么可说的就是机器的物理内存，读写速度低于cpu一个量级，但是高于磁盘不止一个量级。所以，程序和数据如果在内存的话，会有非常快的读写速度。但是，内存的造价是要高于磁盘的，虽然相对来说价格一直在降低。除此之外，内存的断电丢失数据也是一个原因说不能把所有数据和程序都保存在内存中。既然不能全部使用内存，那数据还有程序肯定不可能一直霸占在内存中。当内存没有可用的，就必须要把内存中不经常运行的程序给踢出去。但是踢到哪里去，这时候swap就出现了。swap全称为swap place，，即交换区，当内存不够的时候，被踢出的进程被暂时存储到交换区。当需要这条被踢出的进程的时候，就从交换区重新加载到内存，否则它不会主动交换到真是内存中。
  
#### swap介绍

在详细介绍swap之前，我们需要知道的是计算机对内存分为物理内存与虚拟内存（注意虚拟内存和虚拟地址空间的区别）。物理内存就是计算机的实际内存大小，由RAM芯片组成的。虚拟内存则是虚拟出来的、使用磁盘代替内存。虚拟内存的出现，让机器内存不够的情况得到部分解决。当程序运行起来由操作系统做具体虚拟内存到物理内存的替换和加载(相应的页与段的虚拟内存管理)。这里的虚拟内存即所谓的swap。

  当用户提交程序，然后产生进程，在机器上运行。机器会判断当前物理内存是否还有空闲允许进程调入内存运行，如果有那么则直接调入内存进行运行；如果没有，那么会根据优先级选择一个进程挂起，把该进程交换到swap中等待，然后把新的进程调入到内存中运行。根据这种换入和换出，实现了内存的循环利用，让用户感觉不到内存的限制。从这也可以看出swap扮演了一个非常重要的角色，就是暂存被换出的进程。

  内存与swap之间是按照内存页为单位来交换数据的，一般Linux中页的大小设置为4kb。而内存与磁盘则是按照块来交换数据的。


   
   swap释放：  
   用swapoff -a 关闭虚拟内存（释放）  
   再用swapon -a 打开虚拟内存  
   注：swapon / swapoff 必须要root权限才能使用  
 
   caches释放：  
   echo 3 > /proc/sys/vm/drop_caches  

## [top命令](http://www.jb51.net/LINUXjishu/34604.html)

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
   [swap未使用调查](http://blog.chinaunix.net/uid-20786165-id-3172090.html)
   [java 使用过多虚拟内存]（http://blog.csdn.net/xpylq/article/details/53984431）


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


#### overcommit_memory

内核参数overcommit_memory 

可选值：0、1、2。  
0， 表示内核将检查是否有足够的可用内存供应用进程使用；如果有足够的可用内存，内存申请允许；否则，内存申请失败，并把错误返回给应用进程。  
1， 表示内核允许分配所有的物理内存，而不管当前的内存状态如何。  
2， 表示内核允许分配超过所有物理内存和交换空间总和的内存  

有三种方式修改内核参数，但要有root权限：

  （1）编辑/etc/sysctl.conf ，改vm.overcommit_memory=1，然后sysctl -p 使配置文件生效

  （2）sysctl vm.overcommit_memory=1

  （3）echo 1 > /proc/sys/vm/overcommit_memory



