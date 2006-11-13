#!/bin/bash


echo $1 
echo $2
echo $3
echo $4

sed "s/$1/$2/g" $3 > $4

