#!/bin/sh

NTP_CONF_FILE=/tmp/ntp.conf

echo "server time.google.com true" >> ${NTP_CONF_FILE} 
echo "server time1.xfinity.com true" >> ${NTP_CONF_FILE}
echo "interface listen 127.0.0.1" >> ${NTP_CONF_FILE}
echo "interface listen erouter0"  >> ${NTP_CONF_FILE}
mount-copybind /tmp/dropbear /etc/dropbear/

#added a delay to start the dropbear
sleep 20
systemctl start ntpd

# Start dropbear with read-write mount of /et/dropbear and listen on all interfaces

dropbear -v -R -B -p :22
