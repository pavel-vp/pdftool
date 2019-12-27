package ld.fns.pdf.compress;

import com.itextpdf.text.DocumentException;
import org.apache.sanselan.ImageReadException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet("/compresspdf")
public class PdfCompressServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("begin");
        if (!"POST".equals(request.getMethod()))
            return;


        File inpFile = File.createTempFile("INP", ".pdf");
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

        ByteArrayOutputStream bosIText = new ByteArrayOutputStream();
        ByteArrayOutputStream bosGS = new ByteArrayOutputStream();

        ByteArrayOutputStream bos = null;
        try{
            PDFCompressITextUtil.manipulatePdf(new FileInputStream(new File(inpFileName)), bosIText);
            PDFCompressGS.compressPdf(new FileInputStream(new File(inpFileName)), bosGS);

            System.out.println("itext size = " + bosIText.size());
            System.out.println("gs size = " + bosGS.size());
            if (bosGS.size() > bosIText.size()) {
                bos = bosIText;
            } else {
                bos = bosGS;
            }
        } catch (  ImageReadException | DocumentException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition","attachment; filename=out.pdf");
        response.setContentLength(Long.valueOf(bos.size()).intValue());
        try {
            bos.writeTo(response.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServletException(ex);
        }
        System.out.println("end. size = "+bos.size());
    }

}
