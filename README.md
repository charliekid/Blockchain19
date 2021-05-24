# Blockchain19
[![Build Status](https://travis-ci.com/charliekid/Blockchain19.svg?branch=main)](https://travis-ci.com/charliekid/Blockchain19)

This project is implement a blockchain system that will decentralize and help track the integrity and process of the COVID19 vaccine.

[Source Code](https://github.com/charliekid/Blockchain19)


## Project Mission / Motivation

There is limited visibility within the medical supply chain when it comes to receiving medications or vaccinations. From manufacturers, logistics, hospitals/clinics, and recipients there is a lack of transparency with the process that causes errors, waste of resources (money and time), and are more susceptible to data breaches.

## Project Objective
We will work with Jonathan and Matt from the DMDC to make a program that will track and verify the status and process of a COVID-19 vaccination in a decentralized manner utilizing some form of Blockchain by the end of this semester.

## Project Approach
- We would need to do research on the process of a COVID-19 vaccination from start to finish. Then decide on which portion of that supply chain we wish to implement the blockchain technology to.
- Afterwards we will apply the agile development and create user stories. This will help divide up the work of the project.
- For this project we will be using Java. Our team is most familiar with Java. Since blockchain is a new concept for all of us, we decided to choose a language that we would not struggle with.
- We will also be using Google cloud platform. Using a platform is common in the industry and we wanted to apply this to our project.

## Dependencies / Risks
- Dependencies:
  - The usage of SHA-256, RSA encryption scheme
  - Understanding the blockchain model.
  - Learning and refreshing on java and the Google cloud platform we would be using.
- Risk:
  - Blockchain not working as expected, or potentially less efficient than existing databases and tracking methods.

## Project Deliverables
- Blockchain-powered tracker for supply chain management
- A document comparing the performance of blockchain compared to other databases


# Running this Project
Go thorough the following to 

### Corda

Our project is using an open source blockchain called Corda.

### What you need
- Java 8 (if you have anything else the project might not run. My team had to all unistall Java 11).
- IntelliJ or any IDE. 
- 8gb+ of RAM (I was running on a 2019 Macbook Air 8gb. I ran so poorly).
- You will need this [project](https://github.com/charliekid/Blockchain19Web) for the frontend. 

### To Run Corda
Do the following in your command prompt or terminal. (I'm on a Mac, so I did it right in IntelliJ's IDE terminal.)
* We need to prepare our nodes for our system. 
  * Windows:   `gradlew.bat clean deployNodes`
  * macOS:     `./gradlew clean deployNodes`
  * *This line will also clean anything in the vault. So if you have data in the nodes before. It will be gone.*
  

* Start the nodes by running the following commands
  * Windows:   `build\nodes\runnodes.bat`
  * macOS:     `build/nodes/runnodes`
    
  * *I don't know why, but when I run this line the first time not all the nodes will build. I usually will just close all tabs and run the line again*
  * Wait for all nodes to show p2pmessage in the cilent to continue. 
  

* Start the Spring Boot server with the following command
  * Windows:    `gradlew.bat runTemplateServer`
  * macOS:      `./gradlew runTemplateServer`
  

* Now you just need this [project](https://github.com/charliekid/Blockchain19Web) to run the website.

### Interacting with the Shell
After running the nodes with the build command you can interact with the nodes through the shell.
* Go to the terminal of Patient1 (not the notary!) and run the following command to send information to Doctor1:

  ```
  flow start PatientSendInfoInitiator firstName: "Charlie", lastName: "Nguyen", dose: "0", approvedForVaccination: "false", firstDoseDate: "0000-00-00", firstDoseLot: "none", firstDoseManufacturer: "none", secondDoseDate: "0000-00-00", secondDoseLot: "none", secondDoseManufacturer: "none", vaccinationProcessComplete: "false", patientFullName: Patient1, doctor: Doctor1, patientEmployer: Employer1, clinicAdmin: clinicAdmin1
  ```
* To check to ensure the transaction went through run the following in any node but the notary:
  
  ```
  run vaultQuery contractStateType: com.template.states.PatientInfoState
  ```



# Working with this project
Here I'll explain a few parts of the project in case you wanted to modify it for yourself.

### Webserver

`clients/src/main/java/com/template/webserver/` defines a simple Spring webserver that connects to a node via RPC and
allows you to interact with the node over HTTP.

The API endpoints are defined here:

     clients/src/main/java/com/template/webserver/Controller.java

And a static webpage is defined here:

     clients/src/main/resources/static/


# Project Plan and Idea
Below are our ideas for this project. 

## User Stories
https://bit.ly/3dP3OBZ



## Mockups
![mockup1](img/mockup-1.png)
![mockup2](img/mockup-2.png)
![mockup3](img/mockup-3.png)

## UMLs and ERDs
![uml](img/uml1.png)