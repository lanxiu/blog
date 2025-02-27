# **Spring Cloud Environment Construction.md**

## Software Requirements

- JDK 1.8  
- Spring Boot 2.2.3  
- Maven  
- SVN  

---

## 1. SVN Server Installation

### 1.1 Docker Setup for SVN Server

#### 1.1.1 Configure Docker Registry Mirror

To speed up the Docker image pull process, you can set up a mirror source for Docker. 

1. Create the `daemon.json` file:

```bash
cd /etc/docker/
touch daemon.json
```
2.  Add the mirror URLs to the file:
   ```
{
  "registry-mirrors": [
    "http://docker.1panel.live",
    "http://image.cloudlayer.icu"
  ]
}
```
3. Reload and restart the Docker daemon:
```
systemctl daemon-reload && systemctl restart docker
```
#### 1.1.2 Verify Docker Configuration
To check if the mirror is correctly set, run the following command:

 ```
docker info
```
Check the Registry Mirrors section:
 ```
Registry Mirrors:
  https://ccr.ccs.tencentyun.com/
  https://registry.cn-hangzhou.aliyuncs.com/
```
### 1.2 Install SVN Server Using Docker
#### 1.2.1 Pull SVN Server Image
To install the SVN server, pull the Docker image:
```
bash
 
docker pull docker.1panel.live/elleflorio/svn-server
```
#### 1.2.2 Create Docker Volume
To persist data across container restarts, create a volume to store your SVN data:
```
bash
 
docker volume create svn-data
```
#### 1.2.3 Start SVN Server
Run the SVN server container:
```
bash
 
docker run -d \
  --name svn-server \
  -v svn-data:/workdir \
  -e SUBVERSION_PASSWORD=root \
  -e SUBVERSION_USER=root \
  -p 3690:3690 \
  elleflorio/svn-server:latest
```
You might see an output like this:

 ```
Unable to find image 'elleflorio/svn-server:latest' locally
latest: Pulling from elleflorio/svn-server
Digest: sha256:7b900bb84f106f4b8d97dcf02514288c856f06388f7f01c2961f4ad74a092172
Status: Downloaded newer image for elleflorio/svn-server:latest
b3522c3dd05be69e783efadd344b0ba65f4ca196a0e13402e114523425451f30
```
####  1.2.4 Check Existing Docker Images
To list all images, run the following command:

 
> docker image ls
You may see something like this:

 ```
REPOSITORY                                 TAG       IMAGE ID       CREATED         SIZE
docker.domys.cc/elleflorio/svn-server      latest    4328a30cc450   15 months ago   49.7MB
elleflorio/svn-server                      latest    4328a30cc450   15 months ago   49.7MB
docker.1panel.live/elleflorio/svn-server   latest    4328a30cc450   15 months ago   49.7MB
```
#### 1.2.5 Remove Duplicate Images
To remove duplicate images, first inspect the image ID:  

 
> docker image inspect 4328a30cc450  
Then remove the unnecessary images:

 ```
docker rmi docker.domys.cc/elleflorio/svn-server
docker rmi docker.1panel.live/elleflorio/svn-server
```
#### 1.2.6 Tag the Image for Easier Operation
You can tag the image to make it easier to reference:  

 
> docker tag 4328a30cc450 svnserver:v1
#### 1.3 Create SVN Repository
To create a repository within the SVN server, run:

 
> docker exec -it svn-server svnadmin create /workdir/repos

If you encounter the following error:  

 ```
OCI runtime exec failed: exec failed: unable to start container process: exec: "bash": executable file not found in $PATH: unknown
```
Use sh instead of bash:  

 
> docker exec -it svn-server sh

After entering the container, you can then create your SVN repository:
 
> svnadmin create /workdir/repos
#### 1.4 Manage SVN Server Container
If needed, you can stop, restart, or remove the SVN server container:

To stop the container:
 
> docker stop svn-server

To restart the container:
 
> docker restart svn-server
 
To remove the container:
 
> docker rm svn-server
