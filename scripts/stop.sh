#!/bin/bash
PID_FILE="service.pid"

echo "Stopping current instance:"
if [ -f "${PID_FILE}" ];
then
   echo "    Killing running instance..."
   PID=$(cat ${PID_FILE})
   kill "${PID}"

   tail --pid="${PID}" -f /dev/null

   echo "    Current instance stopped."
else
   echo "    No instance is currently running."
fi
