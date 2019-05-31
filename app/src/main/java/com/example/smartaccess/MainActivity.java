package com.example.smartaccess;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ActionBar actionBar;
    int SERVERPORT ;
    String SERVER_IP;
    EditText t1 ;
    EditText t2 ;
    private ClientThread clientThread;
    private Thread thread;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6495ed")));
        handler = new Handler();
        t1 = findViewById(R.id.ip_text);
        t2 = findViewById(R.id.port_text);
    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.connect_btn){
            SERVER_IP = t1.getText().toString();
            SERVERPORT = Integer.parseInt(t2.getText().toString());

            clientThread = new ClientThread();
            thread  = new Thread(clientThread);
            thread.start();

            return;
        }
    }

    class ClientThread implements Runnable{

        private Socket socket;
        private BufferedReader input;

        @Override
        public void run(){

            try{
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVERPORT);

                while(!Thread.currentThread().isInterrupted()){

                    this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = input.readLine();
                    if(null == message || "Disconnect".contentEquals(message)){
                        Thread.interrupted();
                        message = "Server Disconnected";
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(MainActivity.this, "Server:"+ message, Toast.LENGTH_SHORT).show();
                }
            } catch(UnknownHostException e1){
                e1.printStackTrace();
            } catch(IOException e1){
                e1.printStackTrace();
            }
        }
    }

}