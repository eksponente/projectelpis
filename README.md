#Overview

This repository contains a web application and setup files for a Raspberry Pi to run the web application.
The objective is to build a website which is meant to be accessed over Raspberry Pi's WiFi Access Point. This website should provide a way for people to access useful content offline.

Web application is built using Java's lightweight framework Pippo, the database engine used is H2 and the setup automation is achieved using Ansible.
 
This is a part of Project Elpis (http://www.elpismeanshope.com/).

#Raspberry Pi setup

This application is supported by Raspberry Pi 3. To set up a new Raspberry Pi's WiFi Access Point, do the following:
* Install Raspbian on Raspberry Pi 3
* Clone this repo onto your Raspberry
* Run the script elpis-pi-setup.sh in piSetup/ folder

The script will install some required packages and run an Ansible script (rpiSetup.yml) which will configure a Wifi Access Point to launch on every startup (ssid=Elpis, password=projectelpis).  

As of yet a web application is not run on startup, which will be implemented at a later stage.
 
#Web application

The webapp is built on Java's Pippo framework. To run it, you will need the following packages:
 
* Java 8 Development Kit (```sudo apt install oracle-java8-installer oracle-java8-set-default```)
* Maven (```sudo apt-get install maven```)

This is a Maven project, meaning to run the application use the following command while in the project folder:
    
    mvn compile exec:java -Dexec.mainClass="Elpis"
   
You can then access your application at http://localhost:8338/.

#Contribute

Contributions to the project are more than welcome. Feel free to fork the project and create pull requests. Take a look at the issues raised too and raise some of your own if you see the need for it.
 
You can get in touch with me if you would like to over email: rugilena@gmail.com 