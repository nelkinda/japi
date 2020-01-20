.PHONY: all
all:
	./mvnw verify

.PHONY: clean
clean::
	./mvnw clean

.PHONY: install
install:
	./mvnw install

.PHONY: package
package:
	./mvnw package

.PHONY: sonar
sonar:
	./mvnw verify sonar:sonar -Dsonar.login=admin -Dsonar.password=admin

.PHONY: updates
updates:
	./mvnw versions:display-dependency-updates | grep -- '->' | sort -u

.PHONY: sonard
sonard:
	docker run -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube

-include User.mk
-include ~/User.mk
