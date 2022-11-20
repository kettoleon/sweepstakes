#!/bin/bash

mkdir -p logs
LOG_FILE="logs/$(date +%Y.%m.%d_%H.%M.%S).log"

nohup java -Duser.timezone="Europe/Madrid" -jar sweepstakes-*.jar --server.port=8100 --spring.config.location=classpath:/application.properties,environment.properties &>> ${LOG_FILE} &

until [ -f "${LOG_FILE}" ]
do
     sleep 1
done
tail -f "${LOG_FILE}" &
( tail -f -n0 "${LOG_FILE}" & ) | grep -q -E "Started Sweepstakes"
kill -9 "$(pidof tail)"

if grep -q "Started Sweepstakes" "${LOG_FILE}"; then
    return 0
  else
    return 1
fi
