	#FROM nginx:1.10.1-alpine
#FROM openjdk:8u191-jdk-alpine3.8
FROM openjdk:18
	#ADD out/artifacts/LKManager_jar/LKManager.jar .
	EXPOSE 8080

#############################
#	ADD lk-manager-data/target/lk-manager-data-0.0.1-SNAPSHOT-jar-with-dependencies.jar .
 #  ADD  	lk-manager-web/target/lk_manager-web-0.0.1-SNAPSHOT-jar-with-dependencies.jar .
 #   CMD java -jar lk_manager-web-0.0.1-SNAPSHOT-jar-with-dependencies.jar
  #####################################


  #  	ADD lk-manager-data/target/lk-manager-data-0.0.1-SNAPSHOT.jar .
   #    ADD  	lk-manager-web/target/lk_manager-web-0.0.1-SNAPSHOT.jar .

   #     CMD java -jar lk_manager-web-0.0.1-SNAPSHOT.jar

ADD  	lk-manager-web/target/lk_manager-web-0.0.1-SNAPSHOT.jar .
 CMD java -jar lk_manager-web-0.0.1-SNAPSHOT.jar







 #ENTRYPOINT ["java","-jar", "lk_manager-web-0.0.1-SNAPSHOT.jar"]
#RUN sh -c 'touch /lk_manager-web-0.0.1-SNAPSHOT-jar-with-dependencies.jar'




#ENTRYPOINT ["java","-jar","lk_manager-web-0.0.1-SNAPSHOT-jar-with-dependencies.jar"]

#CMD java -jar lk-manager-web/target/lk_manager-web-0.0.1-SNAPSHOT.jar.jar
#CMD java -jar out/artifacts/LKManager_jar/LKManager.jar
#	CMD ["nginx", "-g", "daemon off;"]
	#java -jar out/artifacts/LKManager_jar/LKManager.jar