package org.sarrygeez.JWriter.Core.Utils.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    // Warning - refers to the possibility that an event might cause an error or issue somehow
    // Debug - An extra detail for an event or something for the dev
    // Event - Whenever an event happened e.g., File-Import, File-export, Document Saved
    // Fatal - an error so bad, it caused the application to crash or quit
    // Error - an issue that prevents parts of the program to not work as intended

    private final String dateTime;
    private final String message;
    private final LogType type;

    private static final DateTimeFormatter LOG_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MM-d-yyyy  [HH:mm:ss]");

    public Log(String message, LogType type) {
        this.message = message;
        this.type = type;

        dateTime = getTimeStamp();
    }

    @SuppressWarnings("unused")
    public String getDateTime() {
        return dateTime;
    }

    @SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }

    @SuppressWarnings("unused")
    public LogType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("%-9s %s - %s", "["+getType()+"]", getDateTime(), getMessage());
    }

    public static String getTimeStamp() {
        return LocalDateTime.now().format(LOG_DATE_FORMATTER);
    }
}
