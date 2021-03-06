package encryption.com.cybersafeencryption;

public class Note {
    private String titleNote;
    private String note;
    private String date;
    private boolean checkBox = false;

    public void setCheckBox() {
        checkBox = !checkBox;
    }
    public void setTitleNote(String s) {
        titleNote = s;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTitleNote() {
        return titleNote;
    }
    public String getNote() {
        return note;
    }
    public String getDate() {
        return date;
    }
    public boolean getCheckBox() {
        return checkBox;
    }
}
