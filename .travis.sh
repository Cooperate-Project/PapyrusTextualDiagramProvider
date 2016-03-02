#!/bin/bash

if [[ "$TRAVIS_BRANCH" = "master" && "$TRAVIS_PULL_REQUEST" = "false" ]]; then
	echo mvn $1 clean deploy sonar:sonar
	mvn clean deploy
elif [[ "$TRAVIS_PULL_REQUEST" = "false" ]]; then
	echo mvn $1 -Dsonar.branch="$TRAVIS_BRANCH" clean verify sonar:sonar
	mvn clean verify
else
	echo mvn $1 clean verify
	mvn clean verify
fi


