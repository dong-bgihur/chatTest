package com.example.myapp110_2_2.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.example.myapp110_2_2.MainActivity;
import com.example.myapp110_2_2.R;
import com.example.myapp110_2_2._9;
import com.example.myapp110_2_2._group;
import com.example.myapp110_2_2.databinding.ActivityAgendaBinding;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class _agenda extends AppCompatActivity implements CalendarPickerController {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ActivityAgendaBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    boolean isOpen =false;  // for float btn
    Animation fabOpen , fabClose, rotateForward, rotateBackward;

    // for event
    ArrayList<CalendarEvent> eventList = new ArrayList<>();
    Calendar minDate = Calendar.getInstance();
    Calendar maxDate = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgendaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        /* float button hide , and showed when fab be click */
        binding.fab1.hide();
        binding.fab2.hide();
        /*button animation */
        {
            fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
            fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
            rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
            rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);
        }

        // minimum and maximum date of our calendar
        // 2 month behind, one year ahead, example: March 2015 <-> May 2015 <-> May 2016
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);


        mockList(eventList);

        binding.magendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), this);
        binding.magendaCalendarView.addEventRenderer(new DrawableEventRenderer());


        /* fab btn */
        binding.fab.setOnClickListener((view -> animanFab()));
        binding.fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animanFab();
                Intent it = new Intent(_agenda.this, _group.class);
                startActivityForResult(it,102);

            }
        });

        binding.fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animanFab();
                Intent it = new Intent(_agenda.this, _9.class);
                startActivityForResult(it,102);
                binding.magendaCalendarView.refreshDrawableState();
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.v("demo","activity is postResume");
        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        addEvent(eventList);
        binding.magendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


//        String[] event = data.getExtras().getStringArray("event");
//        if(requestCode == 102 && resultCode == RESULT_OK) {
//            Log.d("test",event[0]);
//            Log.d("test",event[1]);
//            Log.d("test",event[2]);
//            Log.d("test",event[3]);
//            Log.d("test",event[4]);
//            }
        }

    /*Generate*/
    @Override
    public void onDaySelected(DayItem dayItem) {
        Log.d(LOG_TAG, String.format("Selected day: %s", dayItem));
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        Log.d(LOG_TAG, String.format("Selected event: %s", event));
        Toast.makeText(_agenda.this, "Selected event:" + event , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }
    }

    /* insert new event >> some error in */
    private void addEvent(List<CalendarEvent> eventList) {
        Calendar startTime3 = Calendar.getInstance();
        Calendar endTime3 = Calendar.getInstance();
        startTime3.set(Calendar.HOUR_OF_DAY, 12);
        startTime3.set(Calendar.MINUTE, 30);
        endTime3.set(Calendar.HOUR_OF_DAY, 13);
        endTime3.set(Calendar.MINUTE, 0);
        DrawableCalendarEvent event3 = new DrawableCalendarEvent("TEST", "TEST", "TEST",
                ContextCompat.getColor(this, R.color.theme_light_primary), startTime3, endTime3, false,android.R.drawable.btn_default);
        eventList.add(event3);
//        Log.d("addEvent",endTime3.toString() + event3.toString() + " \t event");
    }

    private void mockList(List<CalendarEvent> eventList) {



//        Calendar startTime1 = Calendar.getInstance();
//        Calendar endTime1 = Calendar.getInstance();
//        endTime1.add(Calendar.DAY_OF_YEAR, 5);
//        BaseCalendarEvent event1 = new BaseCalendarEvent("出遊", "nice!", "花蓮",
//                ContextCompat.getColor(this, R.color.teal_200), startTime1, endTime1, true);
//        eventList.add(event1);

//        Calendar startTime2 = Calendar.getInstance();
//        startTime2.add(Calendar.DAY_OF_YEAR, 1);
//        Calendar endTime2 = Calendar.getInstance();
//        endTime2.add(Calendar.DAY_OF_YEAR, 7);
//        BaseCalendarEvent event2 = new BaseCalendarEvent("Visit to Dalvík", "A beautiful small town", "Dalvík",
//                ContextCompat.getColor(this, R.color.teal_700), startTime2, endTime2, true);
//        eventList.add(event2);

        db.collection("joinData/")
//                .document("YEJ2UIFM4hWPpUq4de5s")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc: task.getResult()){
                                String title = doc.getString("title");
                                Log.d("Demo",title);
                            }
                        }
                    }
                })
                .addOnFailureListener(v -> {
                   Log.d("f","fail");
                });

        Calendar startTime3 = Calendar.getInstance();
        Calendar endTime3 = Calendar.getInstance();
        startTime3.set(Calendar.HOUR_OF_DAY, 14);
        startTime3.set(Calendar.MINUTE, 30);
        endTime3.set(Calendar.HOUR_OF_DAY, 15);
        endTime3.set(Calendar.MINUTE, 0);
        DrawableCalendarEvent event3 = new DrawableCalendarEvent("去朋友家", "賽班", "花蓮",
                ContextCompat.getColor(this, R.color.theme_primary), startTime3, endTime3, false, android.R.drawable.btn_star);
        eventList.add(event3);

    }

    private void animanFab(){
        if(isOpen){
            binding.fab.setAnimation(rotateForward);
            binding.fab1.setAnimation(fabClose);
            binding.fab2.setAnimation(fabClose);
            binding.fab1.hide();
            binding.fab2.hide();
            binding.fab1.setClickable(false);
            binding.fab2.setClickable(false);
            isOpen=false;
        }else{
            binding.fab.setAnimation(rotateBackward);
            binding.fab1.setAnimation(fabOpen);
            binding.fab2.setAnimation(fabOpen);
            binding.fab1.show();
            binding.fab2.show();
            binding.fab1.setClickable(true);
            binding.fab2.setClickable(true);
            isOpen=true;
        }
    }


}