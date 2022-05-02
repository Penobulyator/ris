import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.log4j.Logger;
import utils.ArchiveReader;
import utils.OpenStreetMapData;
import utils.OpenStreetMapReader;

import javax.xml.stream.XMLStreamException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class XMLApp {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(XMLApp.class);
        try {
            InputStream inputStream = ArchiveReader.getBz2InputStream(XMLApp.class.getResourceAsStream("RU-NVS.osm.bz2"));

            OpenStreetMapData data = OpenStreetMapReader.readData(inputStream);

            printResult(data);

        } catch (IOException | XMLStreamException e) {
            logger.error(e);
        }
    }

    private static void printResult(OpenStreetMapData data){
        System.out.println("Users changes:");
        for (var entry : data.getUserToChangesCount()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        System.out.println("Tags entries:");
        for (var entry : data.getTagToNodeCount()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }



    }


}
