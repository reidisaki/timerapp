package kalei.com.timerapp;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by risaki on 9/24/17.
 */

public class TimerItem implements Serializable {

    String name;
    String category;

    public TimerItem() {

    }

    public TimerItem(String category) {
        this.category = category;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(final boolean enabled) {
        isEnabled = enabled;
    }

    boolean isEnabled;

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    String type;

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    String note;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(final String dateString) {
        this.dateString = dateString;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    Date date;
    String dateString;
    int id;
}
