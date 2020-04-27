echo "Waiting for deployment ..."
sleep 180
java -jar -Dspring.profiles.active=prod app.jar