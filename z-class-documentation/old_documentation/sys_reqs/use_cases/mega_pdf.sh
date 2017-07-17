#!/bin/bash

# Requires pdftk

# Remove the existing printed PDF directory
rm -rf ~/PDF

rm report.pdf

mkdir remove

cp needed/* remove/

cp expected/* remove/

cp useful/* remove/

# Print to PDF in PDF all the .odts
libreoffice --writer --pt PDF remove/*.odt

printf 'before ls'

ls ~/PDF/

printf 'printing to file takes a little while' 

# could be changed to use the until command, or can just manually edit this number. 
# until last file in, don't call pdftk. 
sleep 60

printf 'after ls'
ls ~/PDF/


#pdf stapler all of those files together into report.pdf
pdftk ~/PDF/*.pdf cat output ./report.pdf

printf 'sleep 10, because pdftk does weird shit.'

sleep 10

# remove the temp directory. 
rm -rf remove

# Printed pdf files will go here. 
rm -rf ~/PDF

# go ahead and open this bad boy up.
firefox report.pdf
