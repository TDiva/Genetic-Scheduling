package tests;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by TDiva on 5/18/16.
 */
public class CSVResponseWriter implements Closeable {

    private int index = 0;
    private int MAX_TO_FLUSH = 100;

    private CSVWriter csvWriter;
    String[] headers;
    int length;

    public CSVResponseWriter(String filename, String headers[]) {
        try {
            csvWriter = new CSVWriter(new PrintWriter(filename));
            this.headers = headers;
            length = headers.length;
            csvWriter.writeNext(headers);
        } catch (FileNotFoundException e) {
            System.out.println("File '" + filename + "' was not found");
            throw new RuntimeException("Cannot save results due to " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public CSVResponseWriter(String filename, String headers[], int flush) {
        this(filename, headers);
        MAX_TO_FLUSH = flush;
    }

    public void writeLine(String[] line) {
        csvWriter.writeNext(line);
        index++;
        if (index > MAX_TO_FLUSH) {
            try {
                csvWriter.flush();
            } catch (IOException e) {
                System.out.println("Cannot flush data: " + e.getMessage() );
                close();
                throw new RuntimeException("Cannot flush data: " + e.getMessage() );
            }
            index = 0;
        }
    }

    public void writeLine(Map<String, String> values) {
        String[] line = new String[length];
        for (int i = 0; i<length; i++) {
            line[i] = values.get(headers[i]);
        }
        writeLine(line);
    }

    public void close() {
        if (csvWriter != null) {
            try {
                csvWriter.close();
            } catch (IOException e) {
                System.out.println("Cannot close the CSV Writer: " + e.getMessage());
            }
        }
    }
}
