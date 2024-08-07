NAME = 'my-maven'
DSL = '''pipeline {
  agent any
  environment {
    MAVEN_OPTS = '-Dmaven.ext.class.path=otel.jar'
    OTEL_VERSION = '1.7.0-alpha'
  }
  stages {
    stage('checkout') {
      steps {
        git(url: 'https://github.com/elastic/opentelemetry-maven-extension', branch: 'main')
      }
    }
    stage('prepare') {
      steps {
        sh (label: 'fetch opentelemetry-maven-extension',
            script: 'curl -s https://repo.maven.apache.org/maven2/io/opentelemetry/contrib/opentelemetry-maven-extension/\${OTEL_VERSION}/opentelemetry-maven-extension-\${OTEL_VERSION}.jar > otel.jar')
      }
    }
    stage('compile') {
      steps {
        script {
            withEnv(["HOME=\${env.WORKSPACE}"]) {
              sh(label: 'mvn compile', script: './mvnw -B \${MAVEN_OPTS} clean compile')
            }
        }

      }
    }
    stage('test') {
      steps {
        script {
            withEnv(["HOME=\${env.WORKSPACE}"]) {
              sh(label: 'mvn test', script: './mvnw -B \${MAVEN_OPTS} test')
            }
        }
      }
      post {
        always {
          junit 'target/surefire-reports/*.xml'
        }
      }
    }
  }
}'''

pipelineJob(NAME) {
  definition {
    cps {
      script(DSL.stripIndent())
    }
  }
}
