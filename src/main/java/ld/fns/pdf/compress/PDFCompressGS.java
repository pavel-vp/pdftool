package ld.fns.pdf.compress;

import ld.fns.pdf.ExecUtil;

import java.io.*;
import java.util.UUID;

public class PDFCompressGS {


    static void compressPdf(InputStream input, OutputStream output) throws IOException {

        File inpFile = File.createTempFile("INP", ".pdf");
        String inpFileName = inpFile.getAbsolutePath();
        System.out.println(inpFileName);
        OutputStream outStream = null;
        try{
            outStream= new FileOutputStream(inpFile);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = input.read(bytes)) != -1) {
                outStream.write(bytes, 0, read);
            }
        }
        finally{
            if (outStream!=null)
                outStream.close();
        }
        File outFile = File.createTempFile("OUT", ".pdf");
        String outFileName = outFile.getAbsolutePath();
        System.out.println(outFileName);
        try {
            // todo 64
            String command = "gswin64c -sDEVICE=pdfwrite -sProcessColorModel=DeviceGray -sColorConversionStrategy=Gray -dOverrideICC -dPDFSETTINGS=/screen -dEmbedAllFonts=true -dSubsetFonts=true -dColorImageDownsampleType=/Bicubic -dColorImageResolution=150 -dGrayImageDownsampleType=/Bicubic -dGrayImageResolution=150 -dMonoImageDownsampleType=/Bicubic -dMonoImageResolution=150 -o \""+outFileName+"\" -f \""+inpFileName+"\"";
            System.out.println(command);
            ExecUtil.executeInTerminal(command);
            //executeInTerminal("ping -n 3 google.com");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        OutputStream out = null;
        try{
            out = output;
            System.out.println(outFile.getAbsolutePath());
            InputStream in = null;
            try{
                in = new FileInputStream(outFile);
                int read = 0;
                byte[] bytes = new byte[1024];
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
            }
            finally{
                if (in!=null){
                    in.close();
                }
            }

        }
        finally{
            if (out!=null)
                out.close();
        }
        inpFile.delete();
        outFile.delete();
    }


}
