def gitUrl="git@github.com:dEkio/microservicesLocalMonolith.git"

List<String> deployToContainerCommands = [ "sudo /usr/bin/docker stop eureka1 || echo 'no container to stop'", "sudo /usr/bin/docker rm eureka1 || echo 'no container to delete'", "cd eureka && sudo /usr/bin/docker build -t eureka .", "sudo /usr/bin/docker run -d --name eureka1 -p=18761:8761 eureka"]
List<String> deployToNexusCommands = [ "mvn -Drepository.address=dockerizedci_nexus_1:8081 -f ${WORKSPACE}/eureka/pom.xml clean deploy" ]
List<String> initial = [ "mvn  -f ${WORKSPACE}/eureka/pom.xml clean install"]

createDockerJob("eureka-initial", initial, gitUrl)
createDockerJob("eureka-deploy_artefact_nexus", deployToNexusCommands, gitUrl)
createDockerJob("eureka-deploy_jar_container", deployToContainerCommands, gitUrl)

def createDockerJob(def jobName, def shellCommand, def gitRepository) {

  println "############################################################################################################"
  println "Creating Docker Job ${jobName} for gitRepository=${gitRepository}"
  println "############################################################################################################"

  job(jobName) {
    logRotator {
        numToKeep(10)
    }
    if( "${gitRepository}".size() > 0 ) {
      scm {
        git {
          remote {
            url(gitRepository)
          }
          createTag(false)
          clean()
        }
      }
    }
    steps {
      steps {
		shellCommand.each{
			shell(shellCommand)
		}
      }
    }
    publishers {
      chucknorris()
    }
  }
}

listView('eureka service') {
  description('')
  filterBuildQueue()
  filterExecutors()
  jobs {
    regex(/eureka-.*/)
  }
  columns {
    buildButton()
    status()
    weather()
    name()
    lastSuccess()
    lastFailure()
    lastDuration()
  }
}