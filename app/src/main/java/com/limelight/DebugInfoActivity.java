package com.limelight;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.limelight.utils.DeviceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DebugInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tx_gamepad_info;
    private Vibrator vibrator;
    private Button bt_vibrator;
    private List<InputDevice> ids = new ArrayList<>();
    private Vibrator vibratorOnline;
    private Button bt_vibrator_value;
    private int simulatedAmplitude = 220;
    private TextView tx_input_event_log;
    private final StringBuilder inputEventLog = new StringBuilder();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_axitest);

        tx_gamepad_info = findViewById(R.id.tx_game_pad_info);
        TextView tx_content = findViewById(R.id.tx_content);
        bt_vibrator = findViewById(R.id.bt_vibrator);
        bt_vibrator_value = findViewById(R.id.bt_vibrator_value);
        tx_input_event_log = findViewById(R.id.tx_input_event_log);

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        String kernelVersion = System.getProperty("os.version");
        StringBuffer sb = new StringBuffer();
        sb.append(getString(R.string.debug_info_android_version) + DeviceUtils.getSDKVersionName());
        sb.append("\t" + getString(R.string.debug_info_api_version) + Build.VERSION.SDK_INT);
        sb.append("\n" + getString(R.string.debug_info_kernel_version) + kernelVersion);
        sb.append("\n" + getString(R.string.debug_info_brand_model) + DeviceUtils.getManufacturer() + "\t-\t" + DeviceUtils.getModel());
        tx_content.setText(sb.toString());

        boolean hasVibrator = ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).hasVibrator();
        String content = hasVibrator ? getString(R.string.debug_info_has_vibration_motor) : getString(R.string.debug_info_no_vibration_motor);
        bt_vibrator.setText(getString(R.string.debug_info_test_device_vibration, content));

        showSimlateAmp();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        appendInputEvent(formatKeyEvent(event));
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        appendInputEvent(formatMotionEvent(event));
        return super.dispatchGenericMotionEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        appendInputEvent(formatMotionEvent(event));
        return super.dispatchTouchEvent(event);
    }

    private void appendInputEvent(String line) {
        if (tx_input_event_log == null || line == null) {
            return;
        }

        inputEventLog.insert(0, line + "\n");
        if (inputEventLog.length() > 8000) {
            inputEventLog.setLength(8000);
        }
        tx_input_event_log.setText(inputEventLog.toString());
    }

    private String formatKeyEvent(KeyEvent event) {
        InputDevice device = event.getDevice();
        return getString(R.string.debug_input_key_event,
                keyActionToString(event.getAction()),
                KeyEvent.keyCodeToString(event.getKeyCode()),
                event.getScanCode(),
                event.getMetaState(),
                event.getRepeatCount(),
                event.getDeviceId(),
                device != null ? device.getName() : "unknown");
    }

    private String keyActionToString(int action) {
        if (action == KeyEvent.ACTION_DOWN) {
            return "ACTION_DOWN";
        }
        else if (action == KeyEvent.ACTION_UP) {
            return "ACTION_UP";
        }
        else if (action == KeyEvent.ACTION_MULTIPLE) {
            return "ACTION_MULTIPLE";
        }
        else {
            return Integer.toString(action);
        }
    }

    private String formatMotionEvent(MotionEvent event) {
        InputDevice device = event.getDevice();
        return getString(R.string.debug_input_motion_event,
                MotionEvent.actionToString(event.getActionMasked()),
                sourceToString(event.getSource()),
                event.getButtonState(),
                event.getToolType(0),
                event.getPointerCount(),
                event.getX(),
                event.getY(),
                event.getAxisValue(MotionEvent.AXIS_HSCROLL),
                event.getAxisValue(MotionEvent.AXIS_VSCROLL),
                event.getAxisValue(MotionEvent.AXIS_RELATIVE_X),
                event.getAxisValue(MotionEvent.AXIS_RELATIVE_Y),
                event.getDeviceId(),
                device != null ? device.getName() : "unknown");
    }

    private String sourceToString(int source) {
        StringBuilder sb = new StringBuilder();
        if ((source & InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE) {
            sb.append("MOUSE ");
        }
        if ((source & InputDevice.SOURCE_MOUSE_RELATIVE) == InputDevice.SOURCE_MOUSE_RELATIVE) {
            sb.append("MOUSE_REL ");
        }
        if ((source & InputDevice.SOURCE_TOUCHPAD) == InputDevice.SOURCE_TOUCHPAD) {
            sb.append("TOUCHPAD ");
        }
        if ((source & InputDevice.SOURCE_TOUCHSCREEN) == InputDevice.SOURCE_TOUCHSCREEN) {
            sb.append("TOUCHSCREEN ");
        }
        if ((source & InputDevice.SOURCE_KEYBOARD) == InputDevice.SOURCE_KEYBOARD) {
            sb.append("KEYBOARD ");
        }
        if (sb.length() == 0) {
            sb.append("0x").append(Integer.toHexString(source));
        }
        return sb.toString().trim();
    }

    private void showSimlateAmp() {
        bt_vibrator_value.setText(getString(R.string.debug_info_vibration_amplitude, simulatedAmplitude));
    }

    private void cancleRumble() {
        if (vibratorOnline != null) {
            vibratorOnline.cancel();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_vibrator_cancle) {
            cancleRumble();
            return;
        }
        if (v.getId() == R.id.bt_clear_input_log) {
            inputEventLog.setLength(0);
            if (tx_input_event_log != null) {
                tx_input_event_log.setText("");
            }
            return;
        }
        if (v.getId() == R.id.bt_share_input_log) {
            shareInputEventLog();
            return;
        }
        // Device Vibration
        if (v.getId() == R.id.bt_vibrator) {
            String[] titles = new String[]{getString(R.string.debug_info_simple_vibration), getString(R.string.debug_info_continuous_hd_vibration)};
            new AlertDialog.Builder(this).setItems(titles, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    switch (which) {
                        case 0:
                            vibrator.vibrate(1000);
                            break;
                        case 1:
                            rumble(vibrator);
                            break;
                    }
                }
            }).setTitle(getString(R.string.debug_info_please_choose)).create().show();
            return;
        }

        // Gamepad Vibration
        if (v.getId() == R.id.bt_vibrator_gamepad) {
            if (ids.isEmpty()) {
                Toast.makeText(DebugInfoActivity.this, getString(R.string.debug_info_no_gamepad_detected), Toast.LENGTH_LONG).show();
                return;
            }
            String[] strings = new String[ids.size()];
            for (int i = 0; i < ids.size(); i++) {
                strings[i] = ids.get(i).getName();
            }
            new AlertDialog.Builder(this).setItems(strings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (ids.get(which).getVibrator().hasVibrator()) {
                        String[] titles = new String[]{getString(R.string.debug_info_simple_vibration), getString(R.string.debug_info_continuous_hd_vibration)};
                        new AlertDialog.Builder(DebugInfoActivity.this).setItems(titles, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which2) {
                                dialog.dismiss();
                                switch (which2) {
                                    case 0:
                                        ids.get(which).getVibrator().vibrate(1000);
                                        break;
                                    case 1:
                                        cancleRumble();
                                        vibratorOnline = ids.get(which).getVibrator();
                                        rumble(vibratorOnline);
                                        break;
                                }
                            }
                        }).setTitle(getString(R.string.debug_info_please_choose)).create().show();
                    } else {
                        Toast.makeText(DebugInfoActivity.this, getString(R.string.debug_info_no_vibrator), Toast.LENGTH_SHORT).show();
                    }
                }
            }).setTitle(getString(R.string.debug_info_please_choose)).create().show();
            return;
        }

        // Refresh Gamepad Info
        if (v.getId() == R.id.bt_update_gamepad) {
            updateGamePad();
            return;
        }

        if (v.getId() == R.id.bt_vibrator_value) {
            SeekBar mSeekBar = getSeekBar();
            AlertDialog.Builder editDialog = new AlertDialog.Builder(this);
            editDialog.setTitle(getString(R.string.debug_info_set_amplitude));
            editDialog.setView(mSeekBar);
            editDialog.create().show();
        }
    }

    private void shareInputEventLog() {
        try {
            File dir = new File(getCacheDir(), "input-diagnostics");
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IOException("Unable to create diagnostics directory");
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(new Date());
            File logFile = new File(dir, "artemis-input-events-" + timestamp + ".txt");
            String logText = inputEventLog.length() > 0
                    ? inputEventLog.toString()
                    : getString(R.string.debug_input_log_empty);

            StringBuilder body = new StringBuilder();
            body.append("Artemis input diagnostics\n");
            body.append("Android API: ").append(Build.VERSION.SDK_INT).append("\n");
            body.append("Device: ")
                    .append(DeviceUtils.getManufacturer())
                    .append(" ")
                    .append(DeviceUtils.getModel())
                    .append("\n\n");
            if (tx_gamepad_info != null && tx_gamepad_info.getText() != null &&
                    tx_gamepad_info.getText().length() > 0) {
                body.append("Gamepad info:\n").append(tx_gamepad_info.getText()).append("\n\n");
            }
            body.append("Input events newest first:\n").append(logText).append("\n");

            try (FileOutputStream outputStream = new FileOutputStream(logFile)) {
                outputStream.write(body.toString().getBytes(StandardCharsets.UTF_8));
            }

            Uri uri = FileProvider.getUriForFile(this,
                    getPackageName() + ".fileprovider",
                    logFile);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra(Intent.EXTRA_TEXT, body.toString());
            startActivity(Intent.createChooser(intent, getString(R.string.debug_input_share_title)));
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.debug_input_share_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private SeekBar getSeekBar() {
        SeekBar mSeekBar = new SeekBar(this);
        mSeekBar.setMax(255);
        mSeekBar.setProgress(simulatedAmplitude);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                simulatedAmplitude = progress;
                showSimlateAmp();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        return mSeekBar;
    }

    private void rumble(Vibrator vibrator) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(new long[]{1000}, new int[]{simulatedAmplitude}, 0));
        } else {
            long pwmPeriod = 20;
            long onTime = (long) ((simulatedAmplitude / 255.0) * pwmPeriod);
            long offTime = pwmPeriod - onTime;
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            vibrator.vibrate(new long[]{0, onTime, offTime}, 0, audioAttributes);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vibratorOnline != null) {
            vibratorOnline.cancel();
        }
    }

    private void updateGamePad() {
        ids.clear();
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                    || ((sources & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK)) {
                if (getMotionRangeForJoystickAxis(dev, MotionEvent.AXIS_X) != null &&
                        getMotionRangeForJoystickAxis(dev, MotionEvent.AXIS_Y) != null) {
                    // This is a gamepad
                    ids.add(dev);
                    sb.append(getString(R.string.debug_info_name) + dev.getName());
                    sb.append("\n");
                    sb.append(getString(R.string.debug_info_sensors));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        String sensor = "";
                        if (dev.getSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
                            sensor += getString(R.string.debug_info_accelerometer);
                        }
                        if (dev.getSensorManager().getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
                            sensor += getString(R.string.debug_info_gyroscope);
                        }
                        if (sensor.length() == 0) {
                            sb.append(getString(R.string.debug_info_no_relevant_driver));
                        } else {
                            sb.append(sensor);
                        }
                        sb.append("\n");
                    } else {
                        sb.append(getString(R.string.debug_info_no_api_below_android12));
                        sb.append("\n");
                    }
                    sb.append(getString(R.string.debug_info_vid_pid) + dev.getVendorId() + "_" + dev.getProductId()
                            + "\t    [" + String.format("%04x", dev.getVendorId()) + "_" + String.format("%04x", dev.getProductId()) + "]");
                    sb.append("\n");
                    sb.append(getString(R.string.debug_info_vibration) + (dev.getVibrator().hasVibrator() ? getString(R.string.debug_info_supported) : getString(R.string.debug_info_not_supported)));
                    sb.append("\n");
                    sb.append(getString(R.string.debug_info_details) + "\n");
                    sb.append(dev.toString());
                    sb.append("\n");
                }
            }
        }
        tx_gamepad_info.setText(getString(R.string.debug_info_number_of_gamepads) + ids.size() + "\n" + sb.toString());
    }

    private static InputDevice.MotionRange getMotionRangeForJoystickAxis(InputDevice dev, int axis) {
        InputDevice.MotionRange range;

        // First get the axis for SOURCE_JOYSTICK
        range = dev.getMotionRange(axis, InputDevice.SOURCE_JOYSTICK);
        if (range == null) {
            // Now try the axis for SOURCE_GAMEPAD
            range = dev.getMotionRange(axis, InputDevice.SOURCE_GAMEPAD);
        }

        return range;
    }
}
