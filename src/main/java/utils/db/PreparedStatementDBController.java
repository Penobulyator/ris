package utils.db;

import generated.Node;
import generated.Tag;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementDBController extends DBController {
    protected PreparedStatement preparedStatementNodes;
    protected PreparedStatement preparedStatementTags;
    public PreparedStatementDBController() throws SQLException {
        super();

        preparedStatementNodes = connection.prepareStatement(String.format("INSERT INTO %s VALUES (?, ?)", NODES_TABLE));
        preparedStatementTags = connection.prepareStatement(String.format("INSERT INTO %s VALUES (?, ?)", TAGS_TABLE));
    }

    @Override
    public void writeNode(Node node) throws SQLException {
        long startTime = System.currentTimeMillis();
        prepareStatement(node);
        safeUpdate(preparedStatementNodes);


        for (Tag tag : node.getTag()){
            prepareStatement(tag);
            safeUpdate(preparedStatementTags);
        }

        totalInsertTimeMs += System.currentTimeMillis() - startTime;
    }

    @Override
    public void flush() throws SQLException {
    }

    protected void prepareStatement(Node node) throws SQLException {
        preparedStatementNodes.setBigDecimal(1, new BigDecimal(node.getId()));
        preparedStatementNodes.setString(2, node.getUser());

        preparedStatementTags.setBigDecimal(1, new BigDecimal(node.getId()));
    }

    protected void prepareStatement(Tag tag) throws SQLException {
        preparedStatementTags.setString(2, tag.getK());
    }

    private void safeUpdate(PreparedStatement preparedStatement){
        try {
            preparedStatement.executeUpdate();
        } catch (Exception e){
            Logger.getLogger(PreparedStatementDBController.class).warn(e.getMessage());
        }

        insertedRowsCount++;
    }
}
