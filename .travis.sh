#!/bin/bash

if [ "$TRAVIS_BRANCH" = "master" && "$TRAVIS_PULL_REQUEST" = "false"]; then
	mvn clean deploy
else
	mvn clean install
fi