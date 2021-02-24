package com.example.pit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private Button addBtn;
    private Pit pitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pitView = new Pit(this);
        LinearLayout ll = findViewById(R.id.ll);
        ll.addView(pitView.getView());
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        addBtn = findViewById(R.id.button);
        addBtn.setOnClickListener(v -> pitView.addPoint());
    }
}