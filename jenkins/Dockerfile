FROM jenkins:1.642.4

USER root
RUN apt-get update \
      && apt-get install -y sudo \
      && rm -rf /var/lib/apt/lists/*
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers
USER jenkins

RUN sudo apt-get update		   
RUN sudo apt-get install software-properties-common -y
RUN	sudo apt-get install maven -y
RUN sudo apt-get install docker.io -y

COPY maven-settings.xml /usr/share/maven/conf/settings.xml

COPY plugins.txt /var/jenkins_home/plugins.txt
RUN /usr/local/bin/plugins.sh /var/jenkins_home/plugins.txt

# Adding default Jenkins Jobs
#COPY jobs/1-github-seed-job.xml /usr/share/jenkins/ref/jobs/1-github-seed-job/config.xml
#COPY jobs/5-docker-admin-seed-job.xml /usr/share/jenkins/ref/jobs/5-docker-admin-seed-job/config.xml
#COPY jobs/eureka-seed-job.xml /usr/share/jenkins/ref/jobs/eureka-seed-job/config.xml
COPY jobs/POC-seed-job.xml /usr/share/jenkins/ref/jobs/POC-seed-job/config.xml

############################################
# Configure Jenkins
############################################
# Jenkins settings
COPY config/config.xml /usr/share/jenkins/ref/config.xml
COPY config/admin /usr/share/jenkins/ref/users/admin

# Jenkins Settings, i.e. Maven, Groovy, ...
COPY config/hudson.tasks.Maven.xml /usr/share/jenkins/ref/hudson.tasks.Maven.xml
COPY config/hudson.plugins.sonar.SonarPublisher.xml /usr/share/jenkins/ref/hudson.plugins.sonar.SonarPublisher.xml
COPY config/hudson.plugins.sonar.SonarRunnerInstallation.xml /usr/share/jenkins/ref/hudson.plugins.sonar.SonarRunnerInstallation.xml
COPY config/hudson.plugins.groovy.Groovy.xml /usr/share/jenkins/ref/hudson.plugins.groovy.Groovy.xml
COPY config/maven-global-settings-files.xml /usr/share/jenkins/ref/maven-global-settings-files.xml

# SSH Keys & Credentials
COPY config/credentials.xml /usr/share/jenkins/ref/credentials.xml
COPY config/ssh-keys/cd-demo /usr/share/jenkins/ref/.ssh/id_rsa
COPY config/ssh-keys/cd-demo.pub /usr/share/jenkins/ref/.ssh/id_rsa.pub