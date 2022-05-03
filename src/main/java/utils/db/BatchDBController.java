package utils.db;

import generated.Node;
import generated.Tag;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BatchDBController extends PreparedStatementDBController {
    private static final int MAX_BATCH_SIZE = 1000;
    private int currentBatchSizeNodes = 0;
    private int currentBatchSizeTags = 0;

    public BatchDBController() throws SQLException {
        super();
    }

    @Override
    public void writeNode(Node node) throws SQLException {
        long startTime = System.currentTimeMillis();

        prepareStatement(node);
        preparedStatementNodes.addBatch();
        if (currentBatchSizeNodes++ == MAX_BATCH_SIZE) {
            safeUpdate(preparedStatementNodes);
            insertedRowsCount += currentBatchSizeNodes;
            currentBatchSizeNodes = 0;
        }

        for (Tag tag : node.getTag()) {
            prepareStatement(tag);
            preparedStatementTags.addBatch();

            if (currentBatchSizeTags++ == MAX_BATCH_SIZE) {
                safeUpdate(preparedStatementTags);
                insertedRowsCount += currentBatchSizeNodes;
                currentBatchSizeTags = 0;
            }
        }

        totalInsertTimeMs += System.currentTimeMillis() - startTime;
    }

    @Override
    public void flush() throws SQLException {
        long startTime = System.currentTimeMillis();

        insertedRowsCount += currentBatchSizeTags + currentBatchSizeNodes;
        currentBatchSizeTags = 0;
        currentBatchSizeNodes = 0;
        safeUpdate(preparedStatementNodes);
        safeUpdate(preparedStatementNodes);

        totalInsertTimeMs += System.currentTimeMillis() - startTime;
    }

    private void safeUpdate(PreparedStatement preparedStatement) throws SQLException {
        try {
            preparedStatement.executeBatch();
        } catch (Exception e) {
            Logger.getLogger(BatchDBController.class).warn(e.getMessage());
        }

        preparedStatement.clearBatch();
    }
}
