package two.databases.dbs.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultiRoutingDataSource
        extends AbstractRoutingDataSource
{
    @Override
    protected Object determineCurrentLookupKey() {

        return
                DBContextHolder.getCurrentDb();

    }
}
