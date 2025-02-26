### Virtual Machine Configuration

- **VMware Fusion Version**: 13.6  
- **Linux**: CentOS 7  
- **Docker Requirements**: Docker’s newer features require kernel support from version 4.x or 5.x. CentOS 9 Stream is planned for support.  
- **Hardware**: MacBook Pro 13.7.4 (22H420)  

---

### 1. Download Linux Mirror

Download CentOS-7-x86_64-DVD-2207-02.iso from Aliyun mirror:

> [CentOS-7-x86_64-DVD](https://mirrors.aliyun.com/centos/7/isos/x86_64/?spm=a2c6h.25603864.0.0.7cf64511v2QVXt)

When using VMware Fusion 10, an error occurred:

> **Unable to retrieve kernel symbols. Failed to initialize monitor device.**

Suggested fixes from ChatGPT:
- Increase memory and CPU cores
- In the "Processors & Memory" section, select "Enable hypervisor applications in this virtual machine"
- Switch the mirror to CentOS-7-x86_64-Minimal-2207-02.iso
- Verify the checksum using `shasum -a 256 file`

No effect. After checking VMware logs, many configuration files were missing, leading to a reinstallation of VMware Fusion 13.

> [VMware Installation](https://search.ddooo.com/search.html?wd=vmware%20fusion)

This step took the most time due to:
- Slow official site
- Outdated VMware installation with missing configurations
- Misleading online solutions

---

### 2. Set Static IP on Linux

To create a cluster, DHCP is not suitable, so manual configuration was required. The route provided by ChatGPT was:

```bash
# Locate VMware Fusion network config
# /Library/Application Support/VMware Fusion/ or /etc/vmware/

The actual path is :

> /Library/Preferences/VMware Fusion/vmnet8

## 3 Modify Mac Hosts File

Add address aliases for easier management.

## 4 Set Up YUM Repository

```
cd /etc/yum.repos.d
mkdir repo.backup
mv *.repo repo.backup/
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
yum install -y yum-utils
```

## 5 Install Docker 

Use the following command to install Docker easily, though it shows a warning about the CentOS version:

> curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun

Warning message:

```
提示
This Linux distribution (centos 7) reached end-of-life and is no longer supported by this script.
    No updates or security fixes will be released for this distribution, and users are recommended
    to upgrade to a currently maintained version of centos.
```
Note: Daocloud is unavailable.

> curl -sSL https://get.daocloud.io/docker | sh

Check installation:

>  docker version

Start Docker:

> systemctl start docker

Clean up installation files:

> yum remove docker-ce

Remove Docker images, containers, and config files:

> rm -rf /var/lib/docker


## Summary

Everything went smoothly except for the first step, as I wasn’t familiar with macOS for development.
