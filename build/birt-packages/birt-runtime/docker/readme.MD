* after successful tycho build 
* copy here birt-runtime-4.23.0-xxxxxx.zip from target folder 
* run from this folder 
```
docker build . -t birt
```
* after successful build run container 
```
docker run -p 8080:8080 birt
```
* go to http://localhost:8080/birt
* if you need another local port, change first port value e.g. -p 8084:8080 then url will be at http://localhost:8084/birt