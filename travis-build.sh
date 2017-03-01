#!/bin/bash
cd org.eclipse.gmf.runtime.releng
trap "cd .." EXIT
mvn package -DskipTests
