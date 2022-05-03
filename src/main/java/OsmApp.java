import generated.Node;
import org.apache.log4j.Logger;
import utils.ArchiveReader;
import utils.db.BatchDBController;
import utils.db.DBController;
import utils.db.PreparedStatementDBController;
import utils.db.SimpleDBController;
import utils.osm.OsmStat;
import utils.osm.OsmReader;

import java.io.InputStream;
import java.util.List;

public class OsmApp {
    //Measurement results:
    //simple insert: ~4.54ms/row
    //prepared statement: ~3.88ms/row
    //batch: ~3.71
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(OsmApp.class);
        try {
            InputStream inputStream = ArchiveReader.getBz2InputStream(OsmApp.class.getResourceAsStream("RU-NVS.osm.bz2"));
            OsmReader osmReader = new OsmReader(inputStream);
            //OsmStat osmStat = new OsmStat();
            DBController dbController = new BatchDBController();

            long count = 0;
            for (Node node = osmReader.readNextNode(); node != null; node = osmReader.readNextNode()){
                //osmStat.addNode(node);
                dbController.writeNode(node);
                if (count++ == 50000)
                    break;
            }

            dbController.flush();
            System.out.println(dbController.getMsPerRow());

        } catch (Exception e) {
            logger.error("Excepiton: ", e);
        }
    }


}
