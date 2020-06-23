#!/bin/bash

set -e
EXIT_STATUS=0

start=`date +%s`

./gradlew clean test --console=plain || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

echo "Starting services"

if [[ -z "${TRAVIS}" ]]; then
  echo "Waiting 5 seconds for consul to start"
else
  echo "Waiting 60 seconds for consul to start"
fi

cd ../eureka

./gradlew bootRun & PID4=$!

if [[ -z "${TRAVIS}" ]]; then
  sleep 20
  echo "Waiting 5 seconds for microservices to start"
else
  sleep 60
  echo "Waiting 15 seconds for microservices to start"
fi

cd ../complete

if [[ -z "${TRAVIS}" ]]; then
  ./gradlew run -parallel --console=plain & PID1=$!
  sleep 5
else
  echo "Starting book inventory"
  ./gradlew bookinventory:run --console=plain & PID1=$!
  sleep 15
  echo "Starting book catalogue"
  ./gradlew bookcatalogue:run --console=plain & PID2=$!
  sleep 15
  echo "Starting book recommendation"
  ./gradlew bookrecommendation:run --console=plain & PID3=$!
  sleep 15
fi

./gradlew :acceptancetest:test --rerun-tasks --console=plain || EXIT_STATUS=$?

killall -9 java

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

end=`date +%s`
runtime=$((end-start))
echo "execution took $runtime seconds"

exit $EXIT_STATUS
