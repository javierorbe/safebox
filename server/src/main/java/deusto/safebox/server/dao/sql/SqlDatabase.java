package deusto.safebox.server.dao.sql;

/** Database managing system. */
enum SqlDatabase {
    SQLITE("jdbc:sqlite:%s"),
    MYSQL("jdbc:mysql://%s/%s"),
    POSTGRESQL("jdbc:postgresql://%s/%s")
    ;

    private final String jdbcUrl;

    SqlDatabase(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }
}
