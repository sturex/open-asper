package dev.asper.common.util;

import java.io.*;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public enum Compress {
    ;

    public static byte[] zip(String inputString) {
        try {
            ByteArrayOutputStream obj = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(obj);
            gzip.write(inputString.getBytes());
            gzip.flush();
            gzip.close();
            return obj.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String unzip(byte[] bytes) {
        try {
            ByteArrayInputStream obj = new ByteArrayInputStream(bytes);
            GZIPInputStream gzip = new GZIPInputStream(obj);
            InputStreamReader reader = new InputStreamReader(gzip);
            BufferedReader in = new BufferedReader(reader);
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String compress(String input) {
        byte[] compressed = zip(input);
        return Base64.getEncoder().encodeToString(compressed);
    }

    public static String decompress(String input) {
        byte[] decoded = Base64.getDecoder().decode(input.getBytes());
        return unzip(decoded);
    }

}
