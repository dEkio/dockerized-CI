def gitUrl="https://github.com/dEkio/microservicesLocalMonolith.git"

List<String> deployToContainerCommands = [ "sudo /usr/bin/docker stop eureka1 || echo 'no container to stop'", "sudo /usr/bin/docker rm eureka1 || echo 'no container to delete'", "cd eureka && sudo /usr/bin/docker build -t eureka .", "sudo /usr/bin/docker run -d --name eureka1 -p=18761:8761 eureka"]
List<String> deployToNexusCommands = [ "mvn -Drepository.address=dockerizedci_nexus_1:8081 -f ${WORKSPACE}/eureka/pom.xml clean deploy" ]
List<String> initial = [ "mvn  -f ${WORKSPACE}/eureka/pom.xml clean install"]

createInitial(gitUrl)
createNexus(gitUrl)
createContainer(gitUrl)

def createInitial(def gitRepository) {

  println "############################################################################################################"
  println "Creating Docker Job eureka-initial for gitRepository=${gitRepository}"
  println "############################################################################################################"

  job("eureka-initial") {
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
		shell("mvn  -f eureka/pom.xml clean install")
      }
    }
    publishers {
      chucknorris()
    }
  }
}

def createNexus(def gitRepository) {

  println "############################################################################################################"
  println "Creating Docker Job eureka-initial for gitRepository=${gitRepository}"
  println "############################################################################################################"

  job("eureka-deploy_artefact_nexus") {
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
		shell("mvn -Drepository.address=dockerizedci_nexus_1:8081 -f eureka/pom.xml clean deploy")
      }
    }
    publishers {
      chucknorris()
    }
  }
}

def createContainer(def gitRepository) {

  println "############################################################################################################"
  println "Creating Docker Job eureka-initial for gitRepository=${gitRepository}"
  println "############################################################################################################"

  job("eureka-deploy_jar_container") {
    logRotator {
        numToKeep(10)
    }
    if( "${gitRepository}".size() > 0 ) {
      scm {
        git {
          remote {
            url(gitRepository)
			credentials('admin')
          }
          createTag(false)
          clean()
        }
      }
    }
    steps {
      steps {
		shell("sudo /usr/bin/docker stop eureka1 || echo 'no container to stop'")
		shell("sudo /usr/bin/docker rm eureka1 || echo 'no container to delete'")
		shell("cd eureka && sudo /usr/bin/docker build -t eureka .")
		shell("sudo /usr/bin/docker run -d --name eureka1 -p=18761:8761 eureka")
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