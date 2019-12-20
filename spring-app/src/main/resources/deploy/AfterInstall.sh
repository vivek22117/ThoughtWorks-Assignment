#!/bin/bash

echo 'after install script starting....'

mkdir /opt/equipment-iot/logs
chmod +rw /opt/equipment-iot/logs
touch /opt/equipment-iot/logs/stdout.log
touch /opt/equipment-iot/logs/stderr.log

chown -R ec2-user:ec2-user /opt/equipment-iot

mv /opt/equipment-iot/ThoughtWorks-*.jar /opt/equipment-iot/lib/ThoughtWorks-Assignment-1.0-webapp.jar

