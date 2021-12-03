set -e
cd ./ui
npm run-script build
cd ..
mkdir -p app/src/main/resources/static
rm -rf app/src/main/resources/static/*
mv ui/build/* app/src/main/resources/static/
cd ./app
mvn install -DskipTests
