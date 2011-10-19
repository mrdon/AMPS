package com.atlassian.sdk.accept;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ExecRunner
{
    private static final Log log = LogFactory.getLog(ExecRunner.class);

    public int run(File baseDir, List<String> command, Map<String, String> env)
    {
        int err = 0;

        try
        {
            // show the command
            log.info("Running '" + command + "'");

            // exec command on system runtime
            ProcessBuilder procBuilder = new ProcessBuilder(command).directory(baseDir);
            procBuilder.environment().putAll(env);
            Process proc = procBuilder.start();

            // copy input and error to the output stream
            StreamPumper inputPumper =
                    new StreamPumper(proc.getInputStream(), Level.INFO);
            StreamPumper errorPumper =
                    new StreamPumper(proc.getErrorStream(), Level.WARNING);

            // starts pumping away the generated output/error
            inputPumper.start();
            errorPumper.start();

            // Wait for everything to finish
            proc.waitFor();
            inputPumper.join();
            errorPumper.join();
            proc.destroy();

            // check its exit value
            err = proc.exitValue();
            if (err != 0)
            {
                throw new RuntimeException("Exec returned: " + err);
            }
        }
        catch (IOException ioe)
        {
            throw new RuntimeException("Error exec: " + command, ioe);
        }
        catch (InterruptedException ex)
        {
            //ignore
        }

        return err;

    }


    // Inner class for continually pumping the input stream during
    // Process's runtime.

    class StreamPumper extends Thread
    {
        private BufferedReader din;
        private Level messageLevel;
        private boolean endOfStream = false;
        private static final int SLEEP_TIME = 5;

        public StreamPumper(InputStream is, Level messageLevel)
        {
            this.din = new BufferedReader(new InputStreamReader(is));
            this.messageLevel = messageLevel;
        }

        public void pumpStream() throws IOException
        {
            if (!endOfStream)
            {
                String line = din.readLine();

                if (line != null)
                {
                    if (messageLevel == Level.INFO)
                    {
                        System.out.println("OUT: " + line);
                    }
                    else
                    {
                        System.out.println("ERR: " + line);
                    }

                }
                else
                {
                    endOfStream = true;
                }
            }
        }

        public void run()
        {
            try
            {
                try
                {
                    while (!endOfStream)
                    {
                        pumpStream();
                        sleep(SLEEP_TIME);
                    }
                }
                catch (InterruptedException ie)
                {
                    //ignore
                }
                din.close();
            }
            catch (IOException ioe)
            {
                // ignore
            }
        }
    }

}


