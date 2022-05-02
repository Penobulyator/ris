package utils.osm;

import generated.Node;
import generated.Osm;
import generated.Tag;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OsmReader {
    public static OsmData readData(InputStream inputXMLStream) throws XMLStreamException, JAXBException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);

        XMLStreamReader reader = factory.createXMLStreamReader(inputXMLStream);

        Map<String, Integer> userToChangesCount = new HashMap<>();
        Map<String, Integer> tagToNodeCount = new HashMap<>();

        JAXBContext context = JAXBContext.newInstance(Osm.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType != XMLStreamConstants.START_ELEMENT)
                continue;
            String nodeName = reader.getName().getLocalPart();

            if (nodeName.equals("node")) {
                Node node = unmarshaller.unmarshal(reader, Node.class).getValue();

                String user = node.getUser();
                userToChangesCount.put(user, userToChangesCount.getOrDefault(user, 0) + 1);

                for (Tag tag : node.getTag()) {
                    tagToNodeCount.put(tag.getK(), tagToNodeCount.getOrDefault(tag.getK(), 0) + 1);
                }
            }
        }

        return new OsmData(sortMapByValue(userToChangesCount), sortMapByValue(tagToNodeCount));
    }

    private static List<Map.Entry<String, Integer>> sortMapByValue(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        return list;
    }
}
