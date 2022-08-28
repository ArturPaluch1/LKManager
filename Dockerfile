	FROM alpine
	ADD out/artifacts/LKManager_jar/LKManager.jar .
	EXPOSE 8000
	CMD java -jar out/artifacts/LKManager_jar/LKManager.jar