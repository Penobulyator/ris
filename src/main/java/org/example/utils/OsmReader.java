package org.example.utils;

import generated.Node;
import generated.Osm;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.*;

public class OsmReader {
    private XMLStreamReader reader;
    private Unmarshaller unmarshaller;

    public OsmReader(InputStream inputXMLStream) throws XMLStreamException, JAXBException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);

        reader = factory.createXMLStreamReader(inputXMLStream);

        JAXBContext context = JAXBContext.newInstance(Osm.class);
        unmarshaller = context.createUnmarshaller();
    }
    public Node readNextNode() throws XMLStreamException, JAXBException {
        List<Node> nodes = new LinkedList<>();

        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType != XMLStreamConstants.START_ELEMENT)
                continue;

            String nodeName = reader.getName().getLocalPart();

            if (nodeName.equals("node")) {
                return unmarshaller.unmarshal(reader, Node.class).getValue();
            }
        }

        return null;
    }
}
