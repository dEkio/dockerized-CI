def githubApi = new URL("https://api.github.com/users/dEkio/repos")
def projects = new groovy.json.JsonSlurper().parse(githubApi.newReader())

projects.each {
  def jobName=initial
  def githubName=it.full_name
  def gitUrl=it.ssh_url
  println "Creating Job ${jobName} for ${gitUrl}"

  job("GitHub-${jobName}") {
    logRotator(-1, 10)
    scm {
        github(githubName, 'master')
    }
    triggers {
        githubPush()
    }
  }
  
  def jobName1=dev
  println "Creating Job ${jobName1} for ${gitUrl}"

  job("GitHub-${jobName}") {
    logRotator(-1, 10)
    scm {
        github(githubName, 'master')
    }
    triggers {
        githubPush()
    }
  }
  
  def jobName2=test
  println "Creating Job ${jobName2} for ${gitUrl}"

  job("GitHub-${jobName}") {
    logRotator(-1, 10)
    scm {
        github(githubName, 'master')
    }
    triggers {
        githubPush()
    }
  }

  listView(githubName) {
    description('')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex(/${githubName}-.*/)
    }
    columns {
        status()
        buildButton()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
    }
}
  
}