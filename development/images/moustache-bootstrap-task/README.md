# moustache-bootstrap-task

Steps to build the image:

```shell
mkdir -p target
mvn dependency:get -DgroupId=com.backbase.stream -DartifactId=product-catalog-task -Dversion=2.81.0 -Dtransitive=false -Ddest=./target/product-catalog-task.jar
mvn dependency:get -DgroupId=com.backbase.stream -DartifactId=legal-entity-bootstrap-task -Dversion=2.81.0 -Dtransitive=false -Ddest=./target/legal-entity-bootstrap-task.jar

docker build -t moustache-bootstrap-task:latest .
```
