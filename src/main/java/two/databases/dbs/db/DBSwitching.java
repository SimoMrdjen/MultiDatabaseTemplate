package two.databases.dbs.db;

public  interface DBSwitching {

   default void switchDB(Integer kvartal) {
       if ((kvartal == 4 || kvartal == 5)) {
           DBContextHolder.setCurrentDb(ClientNames.PGODINA);
       } else {
           DBContextHolder.setCurrentDb(ClientNames.APV);
       }
   }

}
