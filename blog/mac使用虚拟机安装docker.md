虚拟机使用的是VMware Fusion 13.6
Linux 使用的是CentOS7，docker的新功能需要内核4.x、5.x支持，后续替换为Centos 9stream



## 1 下载linux镜像

在阿里云镜像源下载linux  centos-7 版本中的 CentOS-7-x86_64-DVD-2207-02.iso

> https://mirrors.aliyun.com/centos/7/isos/x86_64/?spm=a2c6h.25603864.0.0.7cf64511v2QVXt

使用vmware fusion 10 安装 ，报错 

> Unable to retrieve kernel symbols. 
> Failed to initialize monitor device.

检查错误原因,根据chatgpt建议

+ 扩大内存、cpu
+ 进入 处理器与内存（Processors & Memory） 选项，确保 "Enable hypervisor applications in this virtual machine"（启用虚拟化） 选项被勾选。
+ 更换镜像 CentOS-7-x86_64-Minimal-2207-02.iso
+ 检查镜像校验码  shasum -a 256 file

没有效果

检查vmware日志中发现大量报错，各种config文件找不到，重装vmware 13
下载地址

> https://search.ddooo.com/search.html?wd=vmware%20fusion

这里浪费了最多的时间调研及排查

- 官网地址很慢
- VMware是很久之前装的，从日志里大量配置文件丢失
- 被网上的解决方案误导

## 2 设置linux静态地址

因为之后会做集群，所以不能使用动态获取地址
vmware mac版本中并不提供此项设置
需要手动更改文件路径

chatgpt的路径是
‘’‘找到 VMware Fusion 网络配置文件，这通常位于：
	•	/Library/Application Support/VMware Fusion/
	•	或者 /etc/vmware/ 目录下的 vmnet 配置文件。
’‘’
而我实际的路径是

> /Library/Preferences/VMware Fusion/vmnet8

## 3 更改mac hosts文件
添加地址别名，便于日常管理

## 4 配置yum repo

```
cd /etc/yum.repos.d
mkdir repo.backup
mv *.repo repo.backup/
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
yum install -y yum-utils
```

## 5 安装docker  

原本在mac直接安装docker ， 但是据说效果并不好


curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun

```
提示
This Linux distribution (centos 7) reached end-of-life and is no longer supported by this script.
    No updates or security fixes will be released for this distribution, and users are recommended
    to upgrade to a currently maintained version of centos.
```
地址不通

> curl -sSL https://get.daocloud.io/docker | sh



验证安装成功

>  docker version

启动docker

> systemctl start docker

删除安装包

> yum remove docker-ce

删除镜像、容器 配置文件等内容

> rm -rf /var/lib/docker


## 总结

除了第一步都很顺利，mac很少用，不熟悉 - -



