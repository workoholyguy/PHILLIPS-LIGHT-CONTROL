package com.example.huelight2;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;
import android.content.Context;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private final String BRIDGE_IP = "192.168.1.107";
    private final String USERNAME = "weY2TE0-QJlR3i2NWzZ6nXFmDQtZKms2UD6QGdxp";
    private final OkHttpClient client = new OkHttpClient();

//    BRIGHTNESS CONTROLL
    private SeekBar brightnessSlider;

//    SCHEDULE
    private TimePicker timePicker;
    private Button setScheduleButton;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

//    SCHEDULE 2
    private Button scheduleTurnOnButton;
    private Button scheduleTurnOffButton;
    private ListView scheduleListView;
    private ArrayAdapter<String> scheduleAdapter;
    private ArrayList<String> scheduleList;

    private int requestCode = 0;

//    REMOVE THE SCHEDULE
    private HashMap<Integer, String> scheduleMap = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonOn = findViewById(R.id.buttonOn);
        Button buttonOff = findViewById(R.id.buttonOff);

        buttonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOnOff(true);
            }
        });

        buttonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOnOff(false);
            }
        });

//        CONTROLL THE BRIGHTNESS
        brightnessSlider = findViewById(R.id.brightnessSlider);

        brightnessSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    setBrightness(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Implement if needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: Implement if needed
            }
        });

//        SCHEDULE CONTROLL
        timePicker = findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

//        SCHEDULE CONTROL 2
        scheduleTurnOnButton = findViewById(R.id.scheduleTurnOnButton);
        scheduleTurnOffButton = findViewById(R.id.scheduleTurnOffButton);
        scheduleListView = findViewById(R.id.scheduleListView);

        scheduleList = new ArrayList<>();
        scheduleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scheduleList);
        scheduleListView.setAdapter(scheduleAdapter);

        scheduleTurnOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLightSchedule(true);
            }
        });

        scheduleTurnOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLightSchedule(false);
            }
        });

        // Set up ListView item click listener for removal
        scheduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find which schedule was clicked
                int requestCodeToRemove = (int) scheduleMap.keySet().toArray()[position];
                removeScheduledAction(requestCodeToRemove);
            }
        });
    }

//    ON AND OFF
    private void turnOnOff(boolean isOn) {
        String url = "http://" + BRIDGE_IP + "/api/" + USERNAME + "/lights/1/state";
        String jsonBody = "{\"on\":" + isOn + "}";

        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Response Received", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

//    CONTROLL BRIGHTNESS
    private void setBrightness(int brightness) {
        String url = "http://" + BRIDGE_IP + "/api/" + USERNAME + "/lights/1/state";
        String jsonBody = "{\"bri\":" + brightness + "}";

        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle failure
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle success
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(MainActivity.this, "Response Received", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

//    SCHEDULE CONTROL
    @SuppressLint("ScheduleExactAlarm")
    private void setLightSchedule(boolean turnOn) {
        Calendar calendar = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= 23) {
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        }
        calendar.set(Calendar.SECOND, 0);

        // Ensure the scheduled time is in the future
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            Toast.makeText(this, "Please select a future time", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("turnOn", turnOn);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        // Format the schedule time
        String formattedTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        String scheduleTime = (turnOn ? "Turn On: " : "Turn Off: ") + formattedTime;

        // Store the schedule details
        scheduleMap.put(requestCode, scheduleTime);
        requestCode++;

        // Update the ListView
        updateScheduleList();

        Toast.makeText(this, "Schedule Set for " + scheduleTime, Toast.LENGTH_SHORT).show();
    }

    // Method to remove a scheduled action
    private void removeScheduledAction(int requestCode) {
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
        scheduleMap.remove(requestCode);
        updateScheduleList();
        Toast.makeText(this, "Schedule Removed", Toast.LENGTH_SHORT).show();
    }

    // Method to update ListView with current schedules
    private void updateScheduleList() {
        scheduleList.clear();
        scheduleList.addAll(scheduleMap.values());
        scheduleAdapter.notifyDataSetChanged();
    }

}

