#!/bin/bash
set -e

echo "bind 'set mark-symlinked-directories on'" >> /root/.bashrc

/etc/init.d/ssh start

while [ true ]; do
  sleep 1;
done

