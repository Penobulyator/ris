import org.apache.log4j.Logger;
import utils.ArchiveReader;
import utils.OpenStreetMapData;
import utils.OpenStreetMapReader;

import java.io.InputStream;

public class XMLApp {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(XMLApp.class);
        try {
            InputStream inputStream = ArchiveReader.getBz2InputStream(XMLApp.class.getResourceAsStream("RU-NVS.osm.bz2"));

            OpenStreetMapData data = OpenStreetMapReader.readData(inputStream);

            printResult(data);
        } catch (Exception e) {
            logger.error(e.toString());
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
