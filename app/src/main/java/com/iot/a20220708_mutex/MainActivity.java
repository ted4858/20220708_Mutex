package com.iot.a20220708_mutex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.Semaphore;

public class MainActivity extends AppCompatActivity {

    private int number = 0;
    private TextView textView;
    private Semaphore semaphore = new Semaphore(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);

        new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    semaphore.acquire();
                    for (int i = 0; i<1000000000; i++) number++;
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            semaphore.acquire();
                            textView.setText(""+number);
                            semaphore.release();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    semaphore.acquire();
                    for (int i = 0; i<1000000000; i++) number--;
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            semaphore.acquire();
                            textView.setText(""+number);
                            semaphore.release();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }
}