package ld.fns.pdf.convert;

import org.jodconverter.JodConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.jodconverter.document.DocumentFormat;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class ConverterJod {

    public static void convertToPdf(InputStream input, String extension, OutputStream output) throws OfficeException {
        DocumentFormat documentFormat = DefaultDocumentFormatRegistry.getFormatByExtension(extension);
        if (documentFormat == null)
            throw new IllegalArgumentException("Неверное расширение файла - " + extension);
        final LocalOfficeManager officeManager = LocalOfficeManager.install();
        try {
            // Start an office process and connect to the started instance (on port 2002).
            officeManager.start();
            // Convert
            JodConverter
                    .convert(input)
                    .as(documentFormat)
                    .to(output)
                    .as(DefaultDocumentFormatRegistry.PDF)
                    .execute();
        } finally {
            // Stop the office process
            OfficeUtils.stopQuietly(officeManager);
        }
    }

    public static void main(String[] args) throws OfficeException {
        File inputFile = new File("/home/pasha/soft/pdf/document.html");
        File outputFile = new File("/home/pasha/soft/pdf/document.pdf");

// Create an office manager using the default configuration.
// The default port is 2002. Note that when an office manager
// is installed, it will be the one used by default when
// a converter is created.
        final LocalOfficeManager officeManager = LocalOfficeManager.install();
        try {

            // Start an office process and connect to the started instance (on port 2002).
            officeManager.start();

            // Convert
            JodConverter
                    .convert(inputFile)
                    .to(outputFile)
                    .execute();
        } finally {
            // Stop the office process
            OfficeUtils.stopQuietly(officeManager);
        }
    }
}
