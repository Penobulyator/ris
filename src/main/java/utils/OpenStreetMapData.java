package utils;

import java.util.List;
import java.util.Map;

public class OpenStreetMapData {
    public OpenStreetMapData(List<Map.Entry<String, Integer>> userToChangesCount, List<Map.Entry<String, Integer>> tagToNodeCount) {
        this.userToChangesCount = userToChangesCount;
        this.tagToNodeCount = tagToNodeCount;
    }

    public List<Map.Entry<String, Integer>> getUserToChangesCount() {
        return userToChangesCount;
    }

    public List<Map.Entry<String, Integer>> getTagToNodeCount() {
        return tagToNodeCount;
    }

    private final List<Map.Entry<String, Integer>> userToChangesCount;
    private final List<Map.Entry<String, Integer>> tagToNodeCount;
}
