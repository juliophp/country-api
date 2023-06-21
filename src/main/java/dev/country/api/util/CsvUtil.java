package dev.country.api.util;



import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil<T> {
    private final List<T> result = new ArrayList<T>();

    public List<T>  readFile(Resource file, Class<T> tClass) {
        try{
            String base64 = new String(file.getInputStream().readAllBytes());
            final DoubleCsvReader reader = new DoubleCsvReader(new StringReader(base64));
            return new CsvToBeanBuilder<T>(reader)
                    .withSeparator(',')
                    .withType(tClass)
                    .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build()
                    .parse();

        }catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
