package deusto.safebox.server.dao.sql;

/** SQL statement that can vary in each DBMS. */
interface SqlStatement {

    /**
     * Returns the SQL statement for the specified DBMS.
     *
     * @param database the DBMS
     * @return the SQL statement for the specified DBMS
     */
    String get(SqlDatabase database);
}
