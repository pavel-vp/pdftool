package ld.fns.pdf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecUtil {
    public static int executeInTerminal(String command) throws IOException, InterruptedException {
        final String[] wrappedCommand;
        wrappedCommand = new String[]{ "cmd", "/c", "start", "/wait", "cmd.exe", "/c", command};

        Process process = new ProcessBuilder(wrappedCommand)
                .redirectErrorStream(true)
                .start();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Your superior logging approach here
            }
        }
        finally{
            if (reader!=null)
                reader.close();
        }
        //System.out.println("done");
        return process.waitFor();
    }

}
