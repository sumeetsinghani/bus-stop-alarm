#!/bin/bash

# This script cleans previous coverage test resources and runs new tests
#
# author: David Nufer

print_usage() {
    echo -e "Usage: coverage-test.sh [command]"
    echo -e "\trun\tThis runs the coverage test, it is the default command"
    echo -e "\t\tand is run if no arguments are given."
    echo
    echo -e "\tsetup <android_sdk_tools>"
    echo -e "\t\tThis setups up the environment for coverage tests. You must"
    echo -e "\t\trun this before running coveragetests. <android_sdk_tools> is"
    echo -e "\t\tthe path to your %ANDROID_SDK%/tools directory."
    echo
    echo -e "\thelp\tPrints this message"
}

run() {
    ant clean
    ant coverage
}

if [ $# -lt 1 ]; then 
   run
elif [ $# -ge 1 ]; then
    if [ $1 = 'help' ]; then
        print_usage
    elif [ $1 = 'setup' ]; then
        "$2"/android update project -p ../..
        "$2"/android update test-project -p ./ -m ../..
    elif [ $1 = 'run' ]; then
      run  
    fi
else
   print_usage
fi

