package utils;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ArchiveReader {
    public static InputStream getBz2InputStream(InputStream archiveInputStream) throws IOException {
        InputStream inputStream = new BufferedInputStream(archiveInputStream);
        return new BZip2CompressorInputStream(inputStream);
    }
}
