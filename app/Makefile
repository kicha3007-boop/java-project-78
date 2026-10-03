.DEFAULT_GOAL := build

setup:
	./gradlew wrapper --gradle-version 8.14.3

clean:
	./gradlew clean

build:
	./gradlew clean build

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew spotlessCheck

lint-fix:
	./gradlew spotlessApply

check-deps:
	./gradlew dependencyUpdates -Drevision=release

.PHONY: build test lint
