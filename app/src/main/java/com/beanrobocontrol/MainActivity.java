package com.beanrobocontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import nl.littlerobots.bean.Bean;
import nl.littlerobots.bean.BeanDiscoveryListener;
import nl.littlerobots.bean.BeanListener;
import nl.littlerobots.bean.BeanManager;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "BeanMain";
    private Bean mBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button forward = (Button)findViewById(R.id.forward);
        forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    mBean.setLed(0, 255, 0);
                    mBean.sendSerialMessage("F".getBytes());
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    mBean.setLed(0, 0, 0);
                    mBean.sendSerialMessage("S".getBytes());
                }
                return false;
            }
        });

        Button backward = (Button)findViewById(R.id.backward);
        backward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    mBean.setLed(0, 0, 255);
                    mBean.sendSerialMessage("B".getBytes());
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    mBean.setLed(0, 0, 0);
                    mBean.sendSerialMessage("S".getBytes());
                }
                return false;
            }
        });

        Button left = (Button)findViewById(R.id.left);
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    mBean.sendSerialMessage("L".getBytes());
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    mBean.setLed(0, 0, 0);
                    mBean.sendSerialMessage("S".getBytes());
                }
                return false;
            }
        });

        Button right = (Button)findViewById(R.id.right);
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    mBean.sendSerialMessage("R".getBytes());
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    mBean.setLed(0, 0, 0);
                    mBean.sendSerialMessage("S".getBytes());
                }
                return false;
            }
        });

        Button connect = (Button)findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeanManager.getInstance().startDiscovery(new BeanDiscoveryListener() {
                    @Override
                    public void onBeanDiscovered(final Bean bean) {
                        Toast.makeText(MainActivity.this, "Bean discovered: " + bean.toString(),
                                Toast.LENGTH_SHORT).show();
                        if(bean == null){
                            Toast.makeText(MainActivity.this, "No Beans discovered",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mBean = bean;
                        mBean.connect(MainActivity.this, new BeanListener() {
                            @Override
                            public void onConnected() {
                                Log.d(TAG, "Connected to bean");
                                Toast.makeText(MainActivity.this, "Connected to bean",
                                        Toast.LENGTH_SHORT).show();
                                mBean.setLed(255, 0 ,0);
                            }

                            @Override
                            public void onConnectionFailed() {
                                Log.d(TAG, "Connection failed");
                            }

                            @Override
                            public void onDisconnected() {
                                Log.d(TAG, "disconnected from bean");
                            }

                            @Override
                            public void onSerialMessageReceived(byte[] bytes) {

                            }

                            @Override
                            public void onScratchValueChanged(int i, byte[] bytes) {

                            }
                        });
                    }

                    @Override
                    public void onDiscoveryComplete() {
                    }
                });
            }
        });

        Button disconnect = (Button)findViewById(R.id.disconnect);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBean.disconnect();
            }
        });
    }
}
