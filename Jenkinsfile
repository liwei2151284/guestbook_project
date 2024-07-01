node {
    
    
    stage('SCM'){
        git 'https:/github.com/liwei2151284/guestbook_project.git'
        sh "sed -i \"s#BUILDNUM#${env.BUILD_NUMBER}#g\"  ./gateway-service/pom.xml"
    }
    
    stage('Build') {
        dir('.') {
            //set repo for maven resolve and deploy
            sh "jf  mvnc --repo-resolve-releases=maven-org-remote --repo-resolve-snapshots=maven-org-remote --repo-deploy-releases=weilife-maven-dev-local --repo-deploy-snapshots=weilife-maven-dev-local "
            //mvn build
            sh "jf  mvn clean install -f ./gateway-service/pom.xml --build-name=${env.JOB_NAME} --build-number=${env.BUILD_NUMBER} --project=weilife "
        }
    }
    
    //stage('JIRA integration') {
    //    sh "sed -i \"s#arti_serverid#demojfrogchina#g\"  ./config.cfg"
    //   sh "jf rt bag ${env.JOB_NAME} ${env.BUILD_NUMBER} ./ --config ./config.cfg --project=weilife"
    //}
    
    stage('Docker build'){
        dir ('gateway-service') {
            def pom = readMavenPom file:'pom.xml'
                print pom.version
                env.version = pom.version
                docker.build "demo.jfrogchina.com/weilife-docker-dev-local/gateway-service:${env.version}"
                sh "jf docker push demo.jfrogchina.com/weilife-docker-dev-local/gateway-service:${env.version} --build-name=${env.JOB_NAME} --build-number=${env.BUILD_NUMBER}  --project=weilife"
            
        }
    }
    

    
    stage('Scan') {
        //scan build
        sh "jf rt bs ${env.JOB_NAME} ${env.BUILD_NUMBER} --fail=false"
        
    }
    
    stage('docker image delete') {
        sh "docker rmi demo.jfrogchina.com/weilife-docker-dev-local/gateway-service:${env.version}"
    }
    
    
    stage('set integration version'){
        
        def discovery_version = '1.0.1'
            env.discovery_version = "${discovery_version}"
        def replicaCount = 2
        dir('kube-deploy/charts/guestbook'){
            sh "sed -i 's/podCount/${replicaCount}/g' values.yaml"
            sh "sed -i 's/Rigistry_URL/demo.jfrogchina.com/g' values.yaml" 
            sh "sed -i 's/Rigistry_URL/demo.jfrogchina.com/g' values.yaml" 
            sh "sed -i 's/Docker_Repo/weilife-docker-dev-local/g' values.yaml"

            sh "sed -i 's/chartversion/${env.version}/g' Chart.yaml"

            sh "sed -i 's/Gateway_Repo_Name/gateway-service/g' values.yaml"            
            sh "sed -i 's/gateway_version/${env.version}/g' values.yaml"
            
            sh "sed -i 's/Discovery_Repo_Name/discovery-service/g' values.yaml"
            sh "sed -i 's/discovery_version/${discovery_version}/g' values.yaml"
        }
    }
    
    stage('helm package'){
        dir('kube-deploy/charts'){
            sh 'helm package guestbook'
            //deploy helm chart
            sh "jf rt u \"guestbook-${env.version}.tgz\" weilife-helm-dev-local/guestbook/${env.version}/ --build-name=${env.JOB_NAME} --build-number=${env.BUILD_NUMBER} --project=weilife "
        }
    }
    
    stage('Docs upload'){
        dir('.'){
            sh "touch \"Doc-${env.version}.txt\";echo \"${env.version}\" > \"Doc-${env.version}.txt\""
            //deploy Docs
            sh "jf rt u \"Doc-${env.version}.txt\"  weilife-generic-dev-local/${env.version}/ --build-name=${env.JOB_NAME} --build-number=${env.BUILD_NUMBER} --project=weilife"
        }
    }
    
    stage('Publish') {
        dir('.') {
            //collect env to build info
            sh "jf rt bce ${env.JOB_NAME} ${env.BUILD_NUMBER} --project=weilife "
            //publish buildinfo to artifactory
            sh "jf rt bp ${env.JOB_NAME} ${env.BUILD_NUMBER} --project=weilife"
        }
    }
    
    stage('Release bundle'){
        dir('kube-deploy'){
        //    sh "sed -i 's/Gateway_Version/${env.version}/g' rb-spec.json"
        //    sh "sed -i 's/Discovery_Version/${discovery_version}/g' rb-spec.json"
        //    sh "sed -i 's/Guest_Chart_Version/${env.version}/g' rb-spec.json"
        //    sh "jf ds rbc --spec=\"rb-spec.json\" --sign=true  --passphrase=Jfr0gchina! guestbook ${env.version} --server-id=trainingcamp"
        }
    }
    
    stage('Create Release bundle V2'){
        dir('.'){
            sh "sed -i 's/build-name/${env.JOB_NAME}/g' rbv2.json"
            sh "sed -i 's/build-number/${env.BUILD_NUMBER}/g' rbv2.json"
            sh "sed -i 's/project-key/weilife/g' rbv2.json"
            //create release bundle v2
            sh "jf rbc --spec=./rbv2.json --signing-key=slashGPG --sync=true --project=weilife ${env.JOB_NAME} ${env.BUILD_NUMBER} "
        //    sh "sed -i 's/Guest_Chart_Version/${env.version}/g' rb-spec.json"
        //    sh "jf ds rbc --spec=\"rb-spec.json\" --sign=true  --passphrase=Jfr0gchina! guestbook ${env.version} --server-id=trainingcamp"
        }
    }
    
    stage('Distribution to Edge'){
        dir('kube-deploy'){
        //    sh "jf ds rbd --dist-rules=distribute-rules.json guestbook ${env.version} --server-id=trainingcamp"
        }
    }
    
    //stage('Promote') {
    //    sh "jf rt dp  gateway-service:${env.version} demo-docker-dev-local demo-docker-prod-local"
    //    //sh "jf rt bpr ${env.JOB_NAME} ${env.BUILD_NUMBER} demo-docker-prod-local --status=Released --comment=approved --include-dependencies=false --copy=true  --props=\"Released=approve;test=ok\""
    //}

}
