package org.sarrygeez.JWriter.Core.Utils.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger {

    // Write logs by default
    private boolean writeToDisc = true;
    private String baseLogDir;
    private final List<Log> logEntries = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void setWriteToDisc(boolean writeToDisc) {
        this.writeToDisc = writeToDisc;
    }

    public boolean isWriteToDisc() {
        return writeToDisc;
    }

    public void setBaseDir(String baseLogDir) {
        this.baseLogDir = baseLogDir;
    }

    public void log(LogType type, String msg) {
        Log l = new Log(msg, type);
        logEntries.add(l);
    }

    public void log(LogType type, String msg, Throwable e) {
        Log l = new Log(msg + " >> " + e.getMessage(), type);
        logEntries.add(l);
    }

    public void dumpToDisk() {
        if(!isWriteToDisc()) {
            System.out.println("Logging disabled");
            return;
        }

        String currentDate = LocalDate.now().format(formatter);
        File logFile = new File(baseLogDir + "/log["+currentDate+"].log");

        try {
            // FileWriter will create the log file, if it doesn't exist yet
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            for(Log log : logEntries) {
                writer.write(log.toString());
                writer.newLine();
            }
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    public List<Log> getLogEntries() {
        return logEntries;
    }
}
