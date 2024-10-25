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

    private String baseLogDir;
    private final List<Log> logEntries = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void setBaseDir(String baseLogDir) {
        this.baseLogDir = baseLogDir;
    }

    public void log(LogType type, String msg) {
        Log l = new Log(msg, type);
        logEntries.add(l);
    }

    public void dumpToDisk() {
        String currentDate = LocalDate.now().format(formatter);
        File logFile = new File(baseLogDir + "/log["+currentDate+"].txt");

        try {
            if(logFile.createNewFile()) {
                log(LogType.INFO, "Log file: [" + currentDate + "] created.");
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            for(Log log : logEntries) {
                writer.write(log.toString());
                writer.newLine();
            }
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
