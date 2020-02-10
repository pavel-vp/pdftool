package ld.fns.pdf.convert;

import ld.fns.pdf.ExecUtil;

import java.io.*;

public class BatConvert {
    public static void convertToPdf(InputStream input, OutputStream output) throws IOException, InterruptedException {

        File inpFile = File.createTempFile("INP", ".pdf");
        String inpFileName = inpFile.getAbsolutePath();
        System.out.println("imputfile = " + inpFileName);
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

        try {
            String command = "c:\\pdftool\\lo_convert.bat \""+inpFileName+"\"";
            System.out.println("convert command = " + command);
            ExecUtil.executeInTerminal(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int posSlash = inpFileName.lastIndexOf("\\");
        int posExt = inpFileName.lastIndexOf(".");
        File outFile = new File("c:\\pdftool\\temp\\" + inpFileName.substring(posSlash+1, posExt) + ".pdf");
        System.out.println("outputfile = " + outFile.getAbsolutePath());

        OutputStream out = null;
        try{
            out = output;
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

    public static void main(String[] args) {
        String inpFileName = "C:\\aaa\\sss\\dddd.pdf";
        int posSlash = inpFileName.lastIndexOf("\\");
        int posExt = inpFileName.lastIndexOf(".");
        String outFile = "c:\\pdftool\\temp\\" + inpFileName.substring(posSlash+1, posExt) + ".pdf";
        System.out.println(outFile);

    }

}