#!/bin/bash

set -e

export ROOT_FOLDER=$(pwd)
# cd https-only-tests-master

function main() {
    ls -lsha output-https-only-tests-master-deploy

    echo ""
    echo "targetEnv=${TARGET_ENV}"
    echo ""
    echo 'not implemented yet'

    # output convention: output-<repoName>-check
    cp -r ../output-https-only-tests-master-deploy ../output-https-only-tests-master-check

    ls -lsha ../output-https-only-tests-master-check
}