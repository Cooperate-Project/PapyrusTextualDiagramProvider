#!/bin/bash

if [[ "$TRAVIS_BRANCH" = "master" && "$TRAVIS_PULL_REQUEST" = "false" ]]; then
	echo mvn clean deploy
	mvn clean deploy
else
	echo mvn clean install
	mvn clean install
fi
