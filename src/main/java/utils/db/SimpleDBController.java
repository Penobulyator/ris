package utils.db;

import generated.Node;
import generated.Tag;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.sql.Statement;

public class SimpleDBController extends DBController {
    private final Statement statement;

    public SimpleDBController() throws SQLException {
        super();
        statement = connection.createStatement();
    }

    @Override
    public void writeNode(Node node) throws SQLException {
        long startTime = System.currentTimeMillis();

        String sqlInsertNode = String.format(
                "INSERT INTO %s VALUES (%s, '%s')"
                , NODES_TABLE, node.getId(), node.getUser());

        safeUpdate(sqlInsertNode, statement);

        for (Tag tag : node.getTag()) {
            String sqlInsertTag = String.format(
                    "INSERT INTO %s VALUES (%s, '%s')"
                    , TAGS_TABLE, node.getId(), tag.getK());
            safeUpdate(sqlInsertTag, statement);
        }

        totalInsertTimeMs += System.currentTimeMillis() - startTime;

    }

    @Override
    public void flush() throws SQLException {
    }


    private void safeUpdate(String sql, Statement statement){
        try {
            statement.executeUpdate(sql);
        } catch (Exception e) {
            Logger.getLogger(DBController.class).warn(String.format("""
                    Unable to execute query :
                    %s
                    Message: %s""", sql, e.getMessage()));
        }

        insertedRowsCount++;
    }
}
