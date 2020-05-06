# Full integration chain DevSecOps

## Build status (badge)

[![Build Status](http://ec2-54-208-88-125.compute-1.amazonaws.com:8080/buildStatus/icon?job=fack-backend-CICD)](http://ec2-54-208-88-125.compute-1.amazonaws.com:8080/job/fack-backend-CICD/)

## Tools 

- Cloud : AWS, (EC2, CloudFormation, IAM, AWSCLI...)
- Container Engine : Docker
- Configuration Managment: Ansible
- Testing : Jmeter
- Source Code Managment : GitLab
- Scheduling : Jenkins
- Security: Clair, Gauntlt
- Notification: Slack, Email

## Project 

### The context
                
The objective of this project is to deploy the web application by creating a complete DevSecOps-like integration chain. For this application to be functional, it is necessary to deploy a MySQL database (backend) and a frontend.
In this project, the problems of businesses, related to storage, to the control of their data and processes were taken into account.

### Infrastructure

#### Description
 
We wanted to reproduce an enterprise-type infrastructure with 4 servers:
- A master type server which will contain the main applications and tools (GitLab, Jenkins, Ansible, Docker…)
- A build server to build our artifact, do unit tests and security scan of images
- A staging / pre-production server in order to carry out tests of our artifacts in same conditions of production,
- A production server in order to deploy our web application which can be consumed.

#### Infrastructure Diagram


![infra](https://user-images.githubusercontent.com/58267422/81116041-004f9980-8f25-11ea-985b-349da4d1097c.png)



### Choice and description of tools

+ DevOps:
  + Infrastructure deployed on the AWS cloud provider thanks to CloudFormation in order to favor IaC.
  + Deployment of a containerized GitLab CE instance and activation of the container registry in order to maintain mastery and control of data.
  + Using Docker to containerize the database, web application as well as the frontend in two different containers to favor agility.
  + Use of Ansible to configure our infrastructure and provisione it.
  + Implementation of a Gitflow to respect good practices. Creation of two branches:
    + The “master” branch which will be used only to deploy our infrastructure and our application in production,
    + The “dev” branch which will be used to develop the functionalities and carry out the tests.
    + Pull request in order to merge the “dev” branch on the “master” branch
  + Use of Jenkins to orchestrate all stages and set up several pipelines.
  + Use notification space on the Slack collaborative platform and email to notify us of the state of the pipeline.
  + Generation of badges to inform employees

+ DevSecOps:
  + Clair Image Scanner:
    + Identify vulnerabilities in built images
  + Attack generation with Gauntlt:
    + Execute attack scenarios to identify vulnerabilities in our web application :
      + xss attack
      + curl attack

### Workflow

#### Description

Continuous Delivery on the **“dev”** branch:
+ Development code update via git,
  + Continuous Integration
    + Triggering of the first pipeline thanks to the push trigger and the webhook sent to Jenkins:
    + Analysis / linter and tests of the syntax of Pyhton and Dockerfile
    + Notification on Slack and by email of the result of this pipeline

+ If the pipeline is successful, triggering and automatic execution of another pipeline thanks to the success of the first:
  + Continuous Integration
     + Analysis / linter and tests of the syntax of mardown, bash, yml files and also of the Ansible syntax
     + Configuration of the environment on the build server, then build and test our artifacts (Docker images) on the server.
     + **Security** : Vulnerability scan of builder images with Clair
     + Push our images on our container registry GitLab. Cleaning up the build environment.
     
  + Continuous Deployment
     + On the staging server, configuration of the environment, recovery of the necessary sources and deployment of our application in an environment close to production,
     + **Security** : Generation of attackswith several scenarios:
       + xss attack
       + curl attack (curl and xss) 
     + Several tests of the proper functioning of the application (web services and database)
     + important several load test (web services and database)
     + Notification on Slack and by email of the result of this pipeline

+ If the pipeline is successful, set up a Pull Request for a manager to check all the pipelines and that they are working properly.
The manager decides to accept the Pull Request and therefore merge the "dev" branch on the "master" branch to deploy the application in production.

Continuous Delivery on the **”Master”** branch:
+ A new pipeline is triggered and executed automatically after the Merge Request:
  + Analysis / linter and tests of the syntax of mardown, bash, yml files and also of the Ansible syntax
  + The build and staging steps are voluntarily forgotten,
  + On the production server, configuration of the environment, recovery of the necessary sources and deployment of our application in the production environment
  + Several tests of the proper functioning (functional and load tests) of the application in production (web services and database)
  + Notification on Slack and by email of the result of this pipeline.

#### Workflow Diagram
------------

![workflow_devsecops](https://user-images.githubusercontent.com/58267422/81198717-195a5800-8fc2-11ea-845b-046d2e5616a8.png)


------------

### Technical word

Docker, docker-compose, Ansible, Tags, Playbooks, Roles, Galaxy, Jenkins, Shared-library, Pipeline, Notification, linter, DevSecOps, Clair, Gauntlt, Jmeter

### Reference repository

+ [Source code development](https://github.com/samiamoura/devsecops-phonebook/tree/master/phonebook-application "Source code development")
+ [Shared-library](https://github.com/samiamoura/devsecops-phonebook/tree/master/shared-library "Shared-library")
+ [Docker Jmeter](https://github.com/samiamoura/devsecops-phonebook/tree/master/docker-jmeter "Docker Jmeter")
