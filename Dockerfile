	FROM nginx:1.10.1-alpine
	COPY LKManager/lk-manager-data /usr/share/nginx/lk-manager-data
	COPY LKManager/lk-manager-web /usr/share/nginx/lk-manager-web
	ADD out/artifacts/LKManager_jar/LKManager.jar .
	#EXPOSE 80
	CMD ["nginx", "-g", "daemon off;"]
	#java -jar out/artifacts/LKManager_jar/LKManager.jar