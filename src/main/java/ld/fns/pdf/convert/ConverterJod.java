package ld.fns.pdf.convert;

import org.jodconverter.JodConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.jodconverter.document.DocumentFormat;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.jodconverter.office.OfficeUtils;

import java.io.*;

public class ConverterJod {

    public static void convertToPdf(InputStream input, String extension, OutputStream output) throws OfficeException {
        DocumentFormat documentFormat = DefaultDocumentFormatRegistry.getFormatByExtension(extension);
        if (documentFormat == null)
            throw new IllegalArgumentException("Неверное расширение файла - " + extension);

        OfficeManager officeManager = LocalOfficeManager.builder()
                .install()
                .officeHome("C:\\Program Files (x86)\\OpenOffice 4")
                .build();

        //final LocalOfficeManager officeManager = LocalOfficeManager.install();
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

    public static void main(String[] args) throws OfficeException, FileNotFoundException {
        File inputFile = new File("/home/pasha/soft/pdf/document.jpg");
        String fileName = inputFile.getName();
        String[] part = fileName.split("\\.");
        FileInputStream fis = new FileInputStream(inputFile);
        File outputFile = new File("/home/pasha/soft/pdf/document.pdf");
        FileOutputStream fos = new FileOutputStream(outputFile);

        convertToPdf(fis, part[1], fos);
    }
}
