package utils.osm;

import generated.Node;
import generated.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OsmStat {
    private final Map<String, Integer> userToChangesCount = new HashMap<>();
    private final Map<String, Integer> tagToNodeCount = new HashMap<>();

    public void addNode(Node node){
        String user = node.getUser();
        userToChangesCount.put(user, userToChangesCount.getOrDefault(user, 0) + 1);

        for (Tag tag : node.getTag()) {
            tagToNodeCount.put(tag.getK(), tagToNodeCount.getOrDefault(tag.getK(), 0) + 1);
        }
    }

    public void print(){
        System.out.println("Users changes:");
        for (var entry : sortMapByValue(userToChangesCount)) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        System.out.println("Tags entries:");
        for (var entry : sortMapByValue(tagToNodeCount)) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }


    private static List<Map.Entry<String, Integer>> sortMapByValue(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        return list;
    }
}
