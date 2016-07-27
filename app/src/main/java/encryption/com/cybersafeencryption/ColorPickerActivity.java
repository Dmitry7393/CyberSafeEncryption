package encryption.com.cybersafeencryption;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ColorPickerActivity extends AppCompatActivity implements
        ColorPicker.OnColorChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        this.activity = this;
    }
    @Override
    public void colorChanged(int color) {
        ColorPickerActivity.this.findViewById(android.R.id.content)
                .setBackgroundColor(color);
    }
    Activity activity;
    public void getColor(View v) {
        new ColorPicker(activity, ColorPickerActivity.this, Color.WHITE)
                .show();
    }
}
