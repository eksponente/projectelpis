# Overview

This repository contains a web application and setup files for a Raspberry Pi to run the web application.
The objective is to build a website which is meant to be accessed over Raspberry Pi's WiFi Access Point. This website should provide a way for people to access useful content offline.

Web application is built using Java's lightweight framework Pippo, the database engine used is H2 and the setup automation is achieved using Ansible.
 
This is a part of Project Elpis (http://www.elpismeanshope.com/). The webapp will be used where people do not have internet access and could benefit greatly from content such as educational, legal information.

# Raspberry Pi setup

This application is supported by Raspberry Pi 3. To set up a new Raspberry Pi's WiFi Access Point, do the following:
* Install Raspbian on Raspberry Pi 3
* Clone this repo onto your Raspberry
* Run the script elpis-pi-setup.sh in piSetup/ folder

The script will install some required packages and run an Ansible script (rpiSetup.yml) which will configure a Wifi Access Point to launch on every startup (ssid=Elpis, password=projectelpis).  

As of yet a web application is not run on startup, which will be implemented at a later stage.
 
# Web application

The webapp is built on Java's Pippo framework. To run it, you will need the following packages:
 
* Java 8 Development Kit (```sudo apt install oracle-java8-installer oracle-java8-set-default```)
* Maven (```sudo apt-get install maven```)

This is a Maven project, meaning to run the application use the following command while in the project folder:
    
    mvn compile exec:java -Dexec.mainClass="Elpis"
   
You can then access your application at http://localhost:8338/.

# Contribute

Contributions to the project are more than welcome. Feel free to fork the project and create pull requests. Take a look at the issues raised too and raise some of your own if you see the need for it.
 
 See some requirements below for better project understanding.
 
You can get in touch with me if you would like to over email: rugilena@gmail.com

# Docker

The project contains a `Dockerfile.template` which can be used to deploy this
project via resin.io. To build and test the image locally, run `make`. This will
create a docker image tagged `elpis`, which can be started by running:

```
docker run -p 8338:8338 elpsis
```

# Project requirements

The idea of this is create a way for people in need to access some content offline (could be pdf's, apps, or just plain text). 

The requirements for the webapp so far:
* Needs to contain a page with content items displayed (Item.java). 
It should be possible to select one of few categories (Health, Education, etc.) to view items in that category only.
Each content item will have a title, description and/or file(s) (not necessarily all of them).
Should also be paged.
* Needs to contain a page to add new items. This should be a form to fill in. Also, the choice to upload a file. Should be password protected.
* Needs to contain a way to edit existing items.
* Needs to be internationalised.
* The webserver (Jetty) also needs to be setup for proper use (security measures etc.)

The requirements for the setup:
* Has to be fairly reliable, as we will not have any access to it in case something crashes. Should restart frequently.
* Should initially pull content from a central unit (AWS). The idea is that a webapp will be deployed in AWS for people to upload content and with every new Raspberry setup the content should be pulled from there.
* Need to update the setup scripts to deploy the webapp and make it accessible over the WAP. 
