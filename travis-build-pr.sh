#!/bin/bash
set -e

export EXIT_STATUS=0

echo "Executing tests for branch $TRAVIS_BRANCH"

cd complete

./test.sh || EXIT_STATUS=$?

if [[ $EXIT_STATUS -ne 0 ]]; then
  exit $EXIT_STATUS
fi

cd ..

exit $EXIT_STATUS
