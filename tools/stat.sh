set -e

TOKEN=`mysql -uroot -proot coredb -s -N -e "select str_value from config where config_id='server_stat_token'";`
HOST=`mysql -uroot -proot coredb -s -N -e "select str_value from config where config_id='server_host_port'";`

STORAGE=`df | grep "/$" | awk '{ print $4 }'`
MEM=`free -k | grep Mem | awk '{ print $4 }'`

CPU0=`cat /proc/stat | grep cpu0`
CPU0_USER=`echo $CPU0 | awk '{ print $2 }'`
CPU0_NICE=`echo $CPU0 | awk '{ print $3 }'`
CPU0_SYSTEM=`echo $CPU0 | awk '{ print $4 }'`
CPU0_IDLE=`echo $CPU0 | awk '{ print $5 }'`
CPU0_IOWAIT=`echo $CPU0 | awk '{ print $6 }'`
CPU0_IRQ=`echo $CPU0 | awk '{ print $7 }'`
CPU0_SOFTIRQ=`echo $CPU0 | awk '{ print $8 }'`
CPU0_STEAL=`echo $CPU0 | awk '{ print $9 }'`

CPU0_TOTAL=`expr $CPU0_USER + $CPU0_NICE + $CPU0_SYSTEM + $CPU0_IDLE + $CPU0_IOWAIT + $CPU0_IRQ + $CPU0_SOFTIRQ + $CPU0_STEAL`
CPU0_IDLE=`expr $CPU0_IDLE + $CPU0_IOWAIT`

LAST_CPU0_TOTAL=`cat /tmp/last_cpu_total 2> /dev/null || true`
LAST_CPU0_TOTAL=`expr $LAST_CPU0_TOTAL + 0 || true`
echo $CPU0_TOTAL > /tmp/last_cpu_total
LAST_CPU0_IDLE=`cat /tmp/last_cpu_idle true 2> /dev/null || true`
LAST_CPU0_IDLE=`expr $LAST_CPU0_IDLE + 0 || true`
echo $CPU0_IDLE > /tmp/last_cpu_idle

TOTAL=`expr $CPU0_TOTAL - $LAST_CPU0_TOTAL` || true
IDLE=`expr $CPU0_IDLE - $LAST_CPU0_IDLE` || true
CPU=`expr 100 \* $IDLE / $TOTAL` || true

curl -X POST "$HOST/admin/server/stats?token=$TOKEN&processor=$CPU&memory=$MEM&storage=$STORAGE"

