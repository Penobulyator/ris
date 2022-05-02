package utils;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.*;

public class OpenStreetMapReader {
    public static OpenStreetMapData readData(InputStream inputXMLStream) throws XMLStreamException {
        XMLEventReader eventReader = XMLInputFactory.newInstance().createXMLEventReader(inputXMLStream);

        Map<String, Integer> userToChangesCount = new HashMap<>();
        Map<String, Integer> tagToNodeCount = new HashMap<>();
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().equals("node")) {
                    //update tags count
                    Set<String> nodeTags = getNodeTags(eventReader);
                    for (String nodeTag : nodeTags) {
                        tagToNodeCount.put(nodeTag, tagToNodeCount.getOrDefault(nodeTag, 0) + 1);
                    }

                    //update user changes count
                    String user = startElement.getAttributeByName(new QName("user")).getValue();
                    userToChangesCount.put(user, userToChangesCount.getOrDefault(user, 0) + 1);
                }
            }
        }

        return new OpenStreetMapData(sortMapByValue(userToChangesCount), sortMapByValue(tagToNodeCount));
    }

    private static Set<String> getNodeTags(XMLEventReader eventReader) throws XMLStreamException {
        Set<String> tags = new HashSet<>();
        while (true) {
            XMLEvent xmlEvent = eventReader.nextEvent();
            if (xmlEvent.isStartElement()) {
                StartElement startElement = xmlEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals("tag")) {
                    tags.add(startElement.getAttributeByName(new QName("k")).getValue());
                }
            } else if (xmlEvent.isEndElement()) {
                if (xmlEvent.asEndElement().getName().getLocalPart().equals("node"))
                    return tags;
            }
        }
    }

    private static List<Map.Entry<String, Integer>> sortMapByValue(Map<String, Integer> map){
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        return list;
    }
}
