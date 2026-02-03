#!/bin/bash

#
docker run --rm --user $(id -u):$(id -g) -v $(pwd):/workspace -e SNAPSHOT=true -e HOME=/workspace/.home  birt-cbi-aggregator