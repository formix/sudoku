#!/bin/bash

echo ******** forms ********
mvn install:install-file \
  -Dfile=forms-1.3.0.jar \
  -Dsources=forms-1.3.0-src.zip \
  -DgroupId=com.jgoodies -DartifactId=forms -Dversion=1.3.0 \
  -Dpackaging=jar
