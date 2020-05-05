#!/usr/bin/env groovy

def call() {
  git branch: branchName,
  credentialsId: 'gitlab_connection_sami',
  url: 'http://ec2-52-87-161-2.compute-1.amazonaws.com/sami/docker-jmeter.git'
  sh "./run.sh -n -t jmeter_${branchName}.jmx -l results_${branchName}.jtl"
  sh "cat results_${branchName}.jtl"
}
