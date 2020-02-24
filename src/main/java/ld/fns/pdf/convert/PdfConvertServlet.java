package ld.fns.pdf.convert;

//import org.jodconverter.office.OfficeException;
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
        System.out.println("convert begin");
        if (!"POST".equals(request.getMethod()))
            return;

        String documentExtension = request.getHeader("Document-Extension");

        File inpFile = File.createTempFile("INP", "."+documentExtension);
        String inpFileName = inpFile.getAbsolutePath();
        System.out.println(inpFileName);
        OutputStream outStream = null;
        ByteArrayOutputStream byteStreamInput = new ByteArrayOutputStream();
        try{
            outStream= new FileOutputStream(inpFile);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = request.getInputStream().read(bytes)) != -1) {
                outStream.write(bytes, 0, read);
                byteStreamInput.write(bytes, 0, read);
            }
        }
        finally{
            if (outStream!=null)
                outStream.close();
        }

        ByteArrayOutputStream bosResult = new ByteArrayOutputStream();
        // JOD CONVERTER requires Java7
/*        try {
            ConverterJod.convertToPdf(new FileInputStream(new File(inpFileName)), documentExtension, bosResult);
        } catch (OfficeException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }*/

        /*
        LDConvertDocument ldConvertDocument = new LDConvertDocument();
        try {
            byte[] result = ldConvertDocument.ConvertToPdf(inpFile.getName(), byteStreamInput.toByteArray());
            bosResult.write(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }*/

        // CLI soffce.exe converter
        try {
            BatConvert.convertToPdf(new FileInputStream(new File(inpFileName)), bosResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition","attachment; filename=out.pdf");
        response.setContentLength(Long.valueOf(bosResult.size()).intValue());
        try {
            bosResult.writeTo(response.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServletException(ex);
        }
        System.out.println("convert end. size = "+bosResult.size());
    }

}
