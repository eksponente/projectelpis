#!/bin/bash
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt install -y python-pip
sudo apt-get install -y build-essentials
sudo apt-get install -y libssl-dev
sudo apt-get install -y python-dev
sudo apt-get install -y default-jdk
sudo pip install ansible
sudo pip install markupsafe
sudo ansible-playbook -i "localhost," -c local rpiSetup.yml

sudo apt install -y oracle-java8-installer
sudo apt install -y oracle-java8-set-default
sudo apt-get install -y maven