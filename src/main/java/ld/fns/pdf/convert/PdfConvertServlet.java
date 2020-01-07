package ld.fns.pdf.convert;

import org.jodconverter.office.OfficeException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet("/convertpdf")
public class PdfConvertServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("begin");
        if (!"POST".equals(request.getMethod()))
            return;

        String documentExtension = request.getHeader("Document-Extension");

        File inpFile = File.createTempFile("INP", "."+documentExtension);
        String inpFileName = inpFile.getAbsolutePath();
        System.out.println(inpFileName);
        OutputStream outStream = null;
        try{
            outStream= new FileOutputStream(inpFile);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = request.getInputStream().read(bytes)) != -1) {
                outStream.write(bytes, 0, read);
            }
        }
        finally{
            if (outStream!=null)
                outStream.close();
        }

        ByteArrayOutputStream bosJod = new ByteArrayOutputStream();
        try {
            ConverterJod.convertToPdf(new FileInputStream(new File(inpFileName)), documentExtension, bosJod);
        } catch (OfficeException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition","attachment; filename=out.pdf");
        response.setContentLength(Long.valueOf(bosJod.size()).intValue());
        try {
            bosJod.writeTo(response.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServletException(ex);
        }
        System.out.println("end. size = "+bosJod.size());
    }

}
