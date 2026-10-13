# Spring Authorization and Resource Servers for a React Client

## Introduction

1. Spring **Authorization Server (AS)** and **Resource Server (RS)**.
2. Authorization Code Grant Type with **PKCE**.
3. **JWTs** **augmented** in the AS and then **converted** in the RS to apply authorization rules.
4. **REST API** for tasks: creating, reading, updating, and deleting tasks. Made with mappers, DTOs, and AOP with `RestControllerAdvice` for exceptions.
5. Integration testing with [Testcontainers](https://java.testcontainers.org/modules/databases/mysql/); MySQL.
6. Client actor in OAuth2 is a mobile application built with **React and Expo**.
7. **Self-hosted** alternatives to GitHub and GitHub Actions.
8. Version control with **Gitea**; repository management and local image registry.
9. CI/CD with **Woodpecker**; local pipelines and each step runs in a container.

## Overview

The aim of this project is to have an **Authorization Server** and a **Resource Server** together in the context of Spring Security to establish an OAuth2 flow for a mobile application.

### Authorization Server side logic

1. Register new users in the Authorization Server.
2. Establish an Authorization Code Grant Type flow with PKCE.
3. Issue non-opaque tokens (JWTs) upon successful authentication.
4. After authentication, augment the tokens with granted authorities reflecting the authenticated user.

### Client

1. An application was built with React Native along the help of Expo. User-interface wise, `@expo/ui/jetpack-compose` was used that offers native components for the Android platform.
2. For more information regarding the client side of the actors in the OAuth2 flow visit the [React Repository]().

### Resource Server side logic

1. Establish communication with the Authorization Server JWKs endpoint.
2. Receive the self-referenced token from the client upon user requests.
3. Convert the token into an Authentication Object understood by the Spring Security Context.
4. Authorization is then enforced as the context is now populated and endpoints are secure.

## Mobile Application UI

| Landing Screen | Register Screen | OAuth2 Login | Tasks Screen |
|-----------|--------------|------------------|------------------|
| ![landing-screen](/screenshots/client/01-landing.jpg) | ![register-screen](/screenshots/client/02-register.jpg) | ![oauth-login](/screenshots/client/03-oauth-login.jpg) | ![tasks-screen](/screenshots/client/04-tasks-page.jpg) |

## Self-hosted Version Control and CI/CD

The self-hosted alternatives run in a Raspberry Pi with the basis of Docker along with [Tailscale](https://tailscale.com/) for ease of usage.

### Version Control with Gitea

[Official Gitea Website](https://about.gitea.com/)

For version control, a self-hosted alternative to GitHub was used; Gitea. Gitea is a platform where you can host code, create, and manage repositories. It also offers CI/CD through Gitea Actions that are compatible with GitHub Actions. Additionally, a local image registry is possible and was used.

#### Self-hosted Gitea Screenshots


| Home-Page | Repository | Image Registry |
|--------------|--------------|--------------|
| ![landing-screen](/screenshots/gitea/gitea-01.png) | ![register-screen](/screenshots/gitea/gitea-02.png) | ![oauth-login](/screenshots/gitea/gitea-03.png) |


### CI/CD with Woodpecker

[Official Woodpecker Website](https://woodpecker-ci.org/)

For CI/CD, Woodpecker was chosen. It is free, open-source and based on Docker containers for each step that is executed in the pipeline.

#### DockerHub Images

|Authorization Server Image   |  Resource Server Image |
|:------------------------------:|:------------------------------:|
| [DockerHub Image](https://hub.docker.com/r/k0stie/authorization-server)  | [DockerHub Image](https://hub.docker.com/r/k0stie/resource-server)  |

#### Pipeline Steps

Two (2) pipelines were built; [authorization-server.yaml](.woodpecker/authorization-server.yaml) and [resource-server.yaml](.woodpecker/resource-server.yaml).

Both pipelines run each step:

1. Woodpecker clones the repository.
2. Compiles, runs the `Testcontainer` tests using MySQL containers, and builds the JAR.
3. Publishes in a Docker CLI container a multi-platform image for `amd64` and `arm64` both in Gitea but also DockerHub.

#### Self-hosted Woodpecker Screenshots


| Pipelines Overview | Pipeline Running | Pipeline Logs |
|--------------|--------------|--------------|
| ![landing-screen](/screenshots/woodpecker/woodpecker-01.png) | ![register-screen](/screenshots/woodpecker/woodpecker-02.png) | ![oauth-login](/screenshots/woodpecker/woodpecker-03.png) |


### Getting started with Docker


1. Download the [docker-compose.yaml](docker-compose.yaml) file. It contains the Authorization Server, the Resource Server and a MySQL image.

1. Place the file into a directory of your choosing.

1. Run using docker compose.

    ```bash
    docker compose up -d
    ```
   
* Next to `docker-compose.yaml` a folder named `mysql-data` will be created which holds the database data.

* Head over to the [react repository (how to run section)]() and follow the instructions. It explains how to use the Android mobile application.