package org.example;

import org.example.entity.Node;
import org.example.repository.NodeRepository;
import org.example.utils.ArchiveReader;
import org.example.utils.OsmReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.InputStream;

@ComponentScan
@SpringBootApplication
public class OsmApp implements CommandLineRunner {
    @Autowired
    private NodeRepository nodeRepository;


    public static void main(String[] args) {
        SpringApplication.run(OsmApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        InputStream inputStream = ArchiveReader.getBz2InputStream(new FileInputStream(ResourceUtils.getFile("classpath:RU-NVS.osm.bz2")));
        OsmReader osmReader = new OsmReader(inputStream);

        long count = 0;
        generated.Node node = osmReader.readNextNode();
        do {
            nodeRepository.save(new Node(node));
            count++;

            node = osmReader.readNextNode();
        } while (node != null && count < 10000);
    }
}
