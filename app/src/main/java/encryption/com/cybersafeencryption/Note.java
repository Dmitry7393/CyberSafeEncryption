package encryption.com.cybersafeencryption;

public class Note {
    private String titleNote;
    private String note;
    private String date;
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
}
