#1/bin/bash

pid=$(sudo lsof -t -i :8080)

if [ -n "$pid" ]; then
    sudo kill -9 $pid
fi