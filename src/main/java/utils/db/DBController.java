package utils.db;

import generated.Node;
import generated.Tag;
import org.apache.log4j.Logger;

import java.sql.*;

public abstract class DBController {
    private static final String SERVER_URL = "jdbc:mysql://localhost/";
    private static final String USER = "root";
    private static final String PASS = "root";
    private static final String DB_NAME = "osm";
    protected static final String NODES_TABLE = "nodes";
    protected static final String TAGS_TABLE = "tags";

    protected Connection connection;

    protected long totalInsertTimeMs = 0;
    protected long insertedRowsCount = 0;

    public DBController() throws SQLException {
        connection = DriverManager.getConnection(SERVER_URL, USER, PASS);

        if (!DbExists()) {
            CreateDb();
        }
        connection.close();
        connection = DriverManager.getConnection(SERVER_URL + DB_NAME, USER, PASS);

        if (!tablesExits())
            createTables();

        connection.setAutoCommit(true);
    }

    public abstract void writeNode(Node node) throws SQLException;
    public abstract void flush() throws SQLException;

    public double getMsPerRow() {
        if (insertedRowsCount == 0)
            return -1;
        else
            return (double)totalInsertTimeMs / insertedRowsCount;
    }

    private void CreateDb() throws SQLException {
        Statement stmt = connection.createStatement();
        String sql = "CREATE DATABASE " + DB_NAME;
        stmt.executeUpdate(sql);
    }

    private boolean DbExists() throws SQLException {
        ResultSet resultSet = connection.getMetaData().getCatalogs();
        while (resultSet.next()) {
            String databaseName = resultSet.getString(1);
            if (databaseName.equals(DB_NAME)) {
                return true;
            }
        }
        return false;
    }

    private boolean tablesExits() throws SQLException {
        ResultSet resultSet = connection.getMetaData().getTables(null, null, null, new String[] {"TABLE"});
        boolean nodesExists = false;
        boolean tagsExists = false;

        while (resultSet.next()) {
            String name = resultSet.getString("TABLE_NAME");
            if (name.equals(NODES_TABLE))
                nodesExists = true;
            else if (name.equals(TAGS_TABLE))
                tagsExists = true;

            if (nodesExists && tagsExists)
                return true;
        }

        return false;
    }

    private void createTables() throws SQLException {
        String sqlNodes = String.format("CREATE TABLE %s (" +
                "node_id BIGINT," +
                "user_name VARCHAR(50)" +
                ")", NODES_TABLE);
        String sqlTags = String.format("CREATE TABLE %s (" +
                "node_id BIGINT," +
                "name VARCHAR(50)" +
                ")", TAGS_TABLE);

        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlNodes);
        statement.executeUpdate(sqlTags);
    }
}
