package umkc.elmp7.climacloset.Exceptions;

/**
 * Created by ericl on 4/6/2016.
 */
public class QueryException extends Exception{
    private String table;
    private String whereClause;

    public QueryException(String Table, String where)
    {
        this.table = Table;
        this.whereClause = where;
    }

    @Override
    public String getMessage(){
        return "Query returned no results with where clause: " + this.whereClause
                + " from the table " + this.table;
    }
}
