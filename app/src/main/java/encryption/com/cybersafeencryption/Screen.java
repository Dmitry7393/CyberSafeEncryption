package encryption.com.cybersafeencryption;

import android.graphics.Path;

import java.util.ArrayList;

public class Screen {
    public ArrayList<Stroke> strokes = new ArrayList<>();
    private int backgroundColor;
    public Screen(ArrayList<Stroke> strokes, int color) {
        this.strokes = strokes;
        backgroundColor = color;
    }
    public int getColor() {
        return backgroundColor;
    }
}
