
minimal spring-boot REST app build with maven
with concourse pipeline tasks
uploading and downloading to artifactory
using https://github.com/spring-io/artifactory-resource

using jcentral (bintray) not maven-central

see git@github.com:dhoffi/scratchconcourse.git

~/.m2/settings.xml

```
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
```

# Monitoring with Prometheus and Grafana

```
docker run -d -p 9090:9090 --name prometheus prom/prometheus
docker exec -it prometheus /bin/sh
cat <<EOT >>  /etc/prometheus/prometheus.yml
  - job_name: 'minimal_spring_prometheus'
    metrics_path: /actuator/prometheus
    scrape_interval: 5s
    static_configs:
    - targets: ['192.168.0.2:8080']
EOT
exit
docker restart prometheus
http://localhost:9090/targets
docker run -d -p 3000:3000 --name grafana grafana/grafana
Grafana prom data-source http://192.168.0.2:9090
http://localhost:3000
+ import
cat monitoring/grafana/minimal_spring_prometheus.json | pbcopy
```

