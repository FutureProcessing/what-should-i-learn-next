How to run Web App
===========================
## Install:
1. Node [https://nodejs.org/]
2. Grunt [https://www.npmjs.com/package/grunt]

# Production
This document describes how to build and run webapp part of application on production

## 1. Build
To build application:

```
npm install
grunt build
```

## 2. Run

### 2a. Run in the Docker container

In distribution directory is a [Dockerfile](distribution/Dockerfile) that is used to run the application.
Use it to build an image and run an application.

```
#Build Image
docker built -t wsiln .

#Run container
docker run -d --name wsiln -p 8081:8081
```

### 2b. Run outside the container
Unzip `app.zip` to your location.

Then run        

```
npm start
```

Application is running on port **8081**

# Development
Use this script to start an application locally to develop.

```
npm install
grunt develop
```

Application is running on port **8081**
