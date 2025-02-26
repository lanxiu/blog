The  virtural machion for mac is VMware Fusion 13.6  
The Linux used CentOS7.  
The new abilities of the docker need 4.x、5.x kernel support ,    Centos 9stream is on plan.  
The hardware environment is MacBook Pro 13.7.4 (22H420)  



## 1 download linux mirror

From aliyun mirror spot, download  CentOS-7-x86_64-DVD-2207-02.iso

> https://mirrors.aliyun.com/centos/7/isos/x86_64/?spm=a2c6h.25603864.0.0.7cf64511v2QVXt

use vmware fusion 10 install ，and the error is reported

> Unable to retrieve kernel symbols. 
> Failed to initialize monitor device.

analyse the reason ,from the adivce of chatgpt

+ increase the memory 、cpu core
+ enter the option（Processors & Memory） ，select "Enable hypervisor applications in this virtual machine"（
+ change the mirror CentOS-7-x86_64-Minimal-2207-02.iso
+ check the checksum shasum -a 256 file

no effect

check the vmware logs and find many errors, various config files missed ，re install vmware 13

> https://search.ddooo.com/search.html?wd=vmware%20fusion

this step waste most of the time

- the official site  is too slow
- VMware was installed many years ago, and lost many configs
- misleaded by  the solutions from the Internet

## 2 set linux static  address

need to create a cluster，so can't use the dhcp
no setting in vmware mac version
need to change it  by manual

the route gived by chatgpt 
```找到 VMware Fusion 网络配置文件，这通常位于：
	•	/Library/Application Support/VMware Fusion/
	•	或者 /etc/vmware/ 目录下的 vmnet 配置文件。
```
the actual route

> /Library/Preferences/VMware Fusion/vmnet8

## 3 modify mac hosts

add address alias for the daily manage

## 4 setting yum repo file

```
cd /etc/yum.repos.d
mkdir repo.backup
mv *.repo repo.backup/
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
yum install -y yum-utils
```

## 5 install docker  

 one command , easy install, but a hint for the low version

> curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun

```
提示
This Linux distribution (centos 7) reached end-of-life and is no longer supported by this script.
    No updates or security fixes will be released for this distribution, and users are recommended
    to upgrade to a currently maintained version of centos.
```
the daocloud can't be connected

> curl -sSL https://get.daocloud.io/docker | sh



check if install succeed

>  docker version

start docker

> systemctl start docker

delete the install files

> yum remove docker-ce

删除镜像、容器 配置文件等内容

> rm -rf /var/lib/docker


## summary

everything  goes on fine except the first  step， for the reason that  I am not familiar with mac in development.
