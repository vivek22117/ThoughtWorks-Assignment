#!/bin/bash -xe

echo "Install java8"
sudo yum remove -y java
sudo yum install -y java-1.8.0-openjdk


sudo yum -y update
sudo yum -y install awscli ruby
wget -O /tmp/install-codedeploy-agent https://aws-codedeploy-us-east-1.s3.amazonaws.com/latest/install
chmod +x /tmp/install-codedeploy-agent
sudo /tmp/install-codedeploy-agent auto
rm /tmp/install-codedeploy-agent

sudo service codedeploy-agent start

echo "export Environment=${environment}" >> /etc/environment
echo "export LOG_DIR=/opt/equipment-iot/logs/" >> /etc/environment

sudo aws deploy create-deployment --application-name ${app_name} \
	--s3-location bucket="${deploy_bucket}",key="${app_key}",bundleType=zip \
	--deployment-group-name ${app_group_name}