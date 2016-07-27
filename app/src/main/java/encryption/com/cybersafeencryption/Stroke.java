package encryption.com.cybersafeencryption;

import android.graphics.Paint;
import android.graphics.Path;

public class Stroke {
    public Path path;
    public Paint paint;
    public Stroke(Paint p)
    {
        path = new Path();
        paint = p;
    }
    public Path getPath() {
        return path;
    }
    public Paint getPaint() {
        return paint;
    }
}