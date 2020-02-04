package ld.fns.pdf.compress;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;

public class PDFCompressITextUtil {

    private static float MIN_DPI = 150f;

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws
     */
    public static void manipulatePdf(InputStream src, OutputStream dest) throws IOException, ImageReadException, DocumentException {
        PdfName key = new PdfName("ITXT_SpecialId");
        PdfName value = new PdfName("123456789");
        // Read the file
        PdfReader reader = new PdfReader(src);
        int n = reader.getXrefSize();
        PdfObject object;
        PRStream stream;
        // Look for image and manipulate image stream
        for (int i = 0; i < n; i++) {
            object = reader.getPdfObject(i);
            if (object == null || !object.isStream())
                continue;
            stream = (PRStream)object;
            // if (value.equals(stream.get(key))) {
            PdfObject pdfsubtype = stream.get(PdfName.SUBTYPE);
            //System.out.println(stream.type());
            if (pdfsubtype != null && pdfsubtype.toString().equals(PdfName.IMAGE.toString())) {
                PdfImageObject image = new PdfImageObject(stream);
                BufferedImage bi = image.getBufferedImage();
                if (bi == null) continue;

                final ImageInfo imageInfo = Sanselan.getImageInfo(image.getImageAsBytes());
                final int physicalWidthDpi = imageInfo.getPhysicalWidthDpi();
                final int physicalHeightDpi = imageInfo.getPhysicalHeightDpi();
                System.out.println("physicalWidthDpi="+physicalWidthDpi);
                System.out.println("physicalHeightDpi="+physicalHeightDpi);
                if (physicalWidthDpi <= 0 || physicalHeightDpi <= 0)
                    continue;

                float downscaleFactorWidth = 1;
                if (physicalWidthDpi > MIN_DPI) {
                    downscaleFactorWidth = MIN_DPI / physicalWidthDpi;
                }
                float downscaleFactorHeight = 1;
                if (physicalHeightDpi > MIN_DPI) {
                    downscaleFactorHeight = MIN_DPI / physicalHeightDpi;
                }
                float downscaleFactor = Math.min(downscaleFactorWidth, downscaleFactorHeight);
                System.out.println("downscaleFactor="+downscaleFactor);
                if (!(downscaleFactor > 0 && downscaleFactor <= 1))
                    continue;

                int width = (int)(bi.getWidth() * downscaleFactor);
                int height = (int)(bi.getHeight() * downscaleFactor);
                BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);//TYPE_INT_RGB);
                AffineTransform at = AffineTransform.getScaleInstance(downscaleFactor, downscaleFactor);
                Graphics2D g = img.createGraphics();
                g.drawRenderedImage(bi, at);
                ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
                ImageIO.write(img, "JPG", imgBytes);
                stream.clear();
                stream.setData(imgBytes.toByteArray(), false, PRStream.BEST_COMPRESSION);
                stream.put(PdfName.TYPE, PdfName.XOBJECT);
                stream.put(PdfName.SUBTYPE, PdfName.IMAGE);
                stream.put(key, value);
                stream.put(PdfName.FILTER, PdfName.DCTDECODE);
                stream.put(PdfName.WIDTH, new PdfNumber(width));
                stream.put(PdfName.HEIGHT, new PdfNumber(height));
                stream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
                stream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
            }
        }
        // Save altered PDF
        PdfStamper stamper = new PdfStamper(reader, dest);
        stamper.setFullCompression();
        stamper.close();
        reader.close();
    }

}
