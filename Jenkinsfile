 node {
 
   stage('Git checkout') {
       git 'https://github.com/fcaudillo/gateway-tpv.git'
   }
   
   stage('Construyendo imagen') {
       sh ('ls')
       sh ('docker build -t fcaudillo/gateway-tpv -f Dockerfile-gateway . ') 
   }
  
   stage('Subiendo la imagen.') {
        withDockerRegistry([ credentialsId: "my_crends_docker", url: '' ]) {   
           sh "docker push fcaudillo/gateway-tpv  "    
           
       }    
   }
   
   stage('Deploy a produccion') {
       sh "cd  main && docker-compose up -d --no-deps --build gateway-tpv"
   }

}