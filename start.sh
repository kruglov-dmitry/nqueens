#!/bin/sh

JAR_FILE_NAME=build/libs/nqueens-1.0-SNAPSHOT.jar
MAIN_CLASS=NQueens

if [ ! -f ${JAR_FILE_NAME} ]; then
	echo "Jar file not present, you may need to run command 'gradle build' first"

	exit 1
fi

if [ "$#" -ne 1 ]; then
    echo "Illegal number of arguments - you must provide size of board"

    exit 1
fi

java -cp $JAR_FILE_NAME $MAIN_CLASS $1
