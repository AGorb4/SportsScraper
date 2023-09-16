#!/bin/bash
ps aux | grep 'sports.scraper' | awk '{print $2}' | cut -f 1 -d ' ' | xargs kill
