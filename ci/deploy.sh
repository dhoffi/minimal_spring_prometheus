#!/bin/bash

set -e

export ROOT_FOLDER=$(pwd)
# cd https-only-tests-master

function main() {
    ls -lsha output-https-only-tests-master-compile

    echo ""
    echo "targetEnv=${TARGET_ENV}"
    echo ""
    echo 'not implemented yet'

    # output convention: output-<repoName>-deploy
    cp -r ../output-https-only-tests-master-compile ../output-https-only-tests-master-deploy

    ls -lsha ../output-https-only-tests-master-deploy
}