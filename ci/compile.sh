#!/bin/bash

set -e

export ROOT_FOLDER=$(pwd)
cd https-only-tests-master

function main() {
    if [ -d "../artifactory" ]; then
      echo "../artifactory"
      ls -lsha ../artifactory
      echo ""
    fi
    
    echo "https-only-tests-master:"
    ls -lsha

    echo ""
    echo "targetEnv=${TARGET_ENV}"
    echo ""
    # ./mvnw package
    ./mvnw install
    rm target/*.jar
    rm target/*.jar.original
    mkdir -p target/m2/com/example/minimal_spring
    cp -rf ~/.m2/repository/com/example/minimal_spring/* target/m2/com/example/minimal_spring

    echo "https-only-tests-master:"
    ls -lsha
    echo "https-only-tests-master/target:"
    ls -lsha target

    # convention: output-<repoName>-<taskname>
    cp -r . ../output-https-only-tests-master-compile


    echo "output-https-only-tests-master-compile:"
    ls -lsha ../output-https-only-tests-master-compile
    echo "output-https-only-tests-master-compile/target:"
    ls -lsha ../output-https-only-tests-master-compile/target
}
 
M2_HOME="${HOME}/.m2"
M2_CACHE="${ROOT_FOLDER}/maven"
GRADLE_HOME="${HOME}/.gradle"
GRADLE_CACHE="${ROOT_FOLDER}/gradle"
 
echo "Generating symbolic links for caches"
set -x
[[ -d "${M2_CACHE}" && ! -d "${M2_HOME}" ]] && ln -s "${M2_CACHE}" "${M2_HOME}"
[[ -d "${GRADLE_CACHE}" && ! -d "${GRADLE_HOME}" ]] && ln -s "${GRADLE_CACHE}" "${GRADLE_HOME}"
set +x

cat <<EOT > ${HOME}/.m2/settings.xml
<?xml version='1.0' encoding='UTF-8'?>
<settings xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd'
          xmlns='http://maven.apache.org/SETTINGS/1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>
    <profiles>
        <profile>
            <repositories>
                <repository>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                    <id>central</id>
                    <name>bintray</name>
                    <url>http://jcenter.bintray.com</url>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                    <id>central</id>
                    <name>bintray-plugins</name>
                    <url>http://jcenter.bintray.com</url>
                </pluginRepository>
            </pluginRepositories>
            <id>bintray</id>
        </profile>
    </profiles>
    <activeProfiles>
        <activeProfile>bintray</activeProfile>
    </activeProfiles>
</settings>
EOT

main
