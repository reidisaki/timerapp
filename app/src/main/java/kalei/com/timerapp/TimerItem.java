package kalei.com.timerapp;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by risaki on 9/24/17.
 */

public class TimerItem implements Serializable {

    String name;
    String category;
    boolean isEnabled;
    String type;
    String note;
    Date dateCreated;
    String dateString;
    String userId;

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

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

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

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(final Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(final String dateString) {
        this.dateString = dateString;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }
}
