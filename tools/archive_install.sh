while read p; do
  mysql -u root -proot coredb < ${p}.sql
done < emigo.lst

