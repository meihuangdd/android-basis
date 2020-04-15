package simcpux.sourceforge.net.color_select;

import android.content.Intent;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.madrapps.pikolo.ColorPicker;

import simcpux.sourceforge.net.color_select.view.ColorPickerView;

public class MainActivity extends AppCompatActivity {
    ColorPickerView pickerView;
    private ColorPicker hslColorPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickerView = findViewById(R.id.select_color);
        ColorPickerView.windowManager = getWindowManager();
        ColorPickerView.activity = this;
//        hslColorPicker = findViewById(R.id.hsl_color_picker);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
