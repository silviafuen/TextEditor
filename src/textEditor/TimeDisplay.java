/* 11/5/17 */
package textEditor;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

public class TimeDisplay {

    public DateTimeFormatter date;
    String formatTimer;
    ZonedDateTime timeZone;

    public TimeDisplay() {
    }

    public String dateTime() {
        date = DateTimeFormatter.ofPattern("HH:mm z yyyy/MM/dd");
        timeZone = ZonedDateTime.now();
        String formatTime = date.format(timeZone);
        return formatTime;
    }
}
