pipeline {
  options {
    timeout(time: 60, unit: 'MINUTES')
    buildDiscarder(logRotator(numToKeepStr:'10'))
    disableConcurrentBuilds(abortPrevious: true)
  }

  agent {
    label "centos-latest"
  }

  tools {
    maven 'apache-maven-latest'
    jdk 'temurin-jdk17-latest'
  }

  environment {
    CHECKOUT = 'true'
    CLONE_URL = 'https://github.com/eclipse-birt/birt'
    CLONE_BRANCH = 'master'
  }

  parameters {
    choice(
      name: 'BUILD_TYPE',
      choices: ['nightly', 'milestone', 'release'],
      description: '''
      Choose the type of build.
      Note that a release build will not promote the build, but rather will promote the most recent milestone build.
      '''
    )

    booleanParam(
      name: 'PROMOTE',
      defaultValue: false,
      description: 'Whether to promote the build to the download server.'
    )
  }

  stages {
    stage('Display Parameters') {
        steps {
            echo "BUILD_TYPE=${params.BUILD_TYPE}"
            echo "PROMOTE=${params.PROMOTE}"
            echo "BRANCH_NAME=${env.BRANCH_NAME}"
            script {
                env.BUILD_TYPE = params.BUILD_TYPE
                if (env.BRANCH_NAME == 'master' || env.BRANCH_NAME == null) {
                  env.WITH_CREDENTIALS = true
                  if (params.PROMOTE) {
                    env.MAVEN_PROFILES = "-Pbuild-server -Ppromote"
                  } else {
                    env.MAVEN_PROFILES = "-Pbuild-server"
                  }
                } else {
                  env.WITH_CREDENTIALS = false
                  env.MAVEN_PROFILES = ""
                }
            }
        }
    }

    stage('Git Checkout') {
      when {
        environment name: 'CHECKOUT', value: 'true'
      }
      steps {
        script {
          def gitVariables = checkout(
            poll: false,
            scm: [
              $class: 'GitSCM',
              branches: [[name: '*' + "${env.CLONE_BRANCH}"]],
              doGenerateSubmoduleConfigurations: false,
              submoduleCfg: [],
              userRemoteConfigs: [[url: "${env.CLONE_URL}.git" ]]
            ]
          )

          echo "$gitVariables"
          env.GIT_COMMIT = gitVariables.GIT_COMMIT

          env.WITH_CREDENTIALS = true
          if (params.PROMOTE) {
            env.MAVEN_PROFILES = "-Pbuild-server -Ppromote"
           } else {
             env.MAVEN_PROFILES = "-Pbuild-server"
           }
        }
      }
    }

    stage('Build') {
      steps {
        script {
          if (env.WITH_CREDENTIALS) {
            sshagent (['projects-storage.eclipse.org-bot-ssh']) {
              mvn()
            }
          } else {
            mvn()
          }
        }
      }

      post {
        always {
          archiveArtifacts artifacts: '**/target/repository/**/*,**/target/*.zip,**/target/work/data/.metadata/.log'
          junit '**/target/surefire-reports/TEST-*.xml'
        }
      }
    }
  }
}

def void mvn() {
  wrap([$class: 'Xvnc', useXauthority: true]) {
    sh '''
      mvn \
      clean \
      verify \
      -B \
      $MAVEN_PROFILES \
      -Dmaven.repo.local=$WORKSPACE/.m2/repository \
      -Dorg.eclipse.justj.p2.manager.build.url=$JOB_URL \
      -Dbuild.type=$BUILD_TYPE \
      -Dgit.commit=$GIT_COMMIT \
      -Dgit.commit.${CLONE_URL}/commit/ \
      -Dorg.eclipse.storage.user=genie.birt \
      -Dorg.eclipse.justj.p2.manager.relative=updates-tmp
    '''
  }
}
