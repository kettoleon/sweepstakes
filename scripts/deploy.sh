#!/bin/bash

GITHUB_USER=abcd
GITHUB_PASS=abcd

MAVEN_REPO_URL=https://maven.pkg.github.com/kettoleon/sweepstakes/com/github/kettoleon/sweepstakes

# Get latest available version
rm -f maven-metadata.xml
wget --user ${GITHUB_USER} --password ${GITHUB_PASS} ${MAVEN_REPO_URL}/maven-metadata.xml

if [ $? -ne 0 ]; then
  echo "Could not resolve maven metadata!"
  exit 0;
fi

LATEST=$(xmlstarlet sel -t -v "//latest" maven-metadata.xml)
rm -f maven-metadata.xml

# If current version is not latest, download the new, delete the old, and restart.
#https://maven.pkg.github.com/kettoleon/sweepstakes/com/github/kettoleon/sweepstakes/0.0.1/sweepstakes-0.0.1.jar

if [ -f "sweepstakes-${LATEST}.jar" ]; then
    echo "Already with latest version."
else
    ./stop.sh
    rm -f sweepstakes-*.jar
    echo "Downloading new version..."
    wget --user ${GITHUB_USER} --password ${GITHUB_PASS} "${MAVEN_REPO_URL}/${LATEST}/sweepstakes-${LATEST}.jar"
    ./start.sh
fi