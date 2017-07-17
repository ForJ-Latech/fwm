#!/bin/bash
rm big_report.pdf
pdftk *.pdf cat output ./big_report.pdf
