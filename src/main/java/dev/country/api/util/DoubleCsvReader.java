package dev.country.api.util;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.stream.Collectors;

class DoubleCsvReader extends CSVReader {

    DoubleCsvReader(final Reader reader) {
        super(reader);
    }

    @Override
    public String[] readNext() throws IOException, CsvValidationException {
        final String[] result = super.readNext();
        if (result != null) {
            try {
                var joinedRemainingNumber = Arrays.stream(result).skip(2).collect(Collectors.joining(""));
                return new String[] {result[0], result[1], String.valueOf(DecimalFormat.getNumberInstance().parse(joinedRemainingNumber).doubleValue())};
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }else {
            return null;
        }

    }

}