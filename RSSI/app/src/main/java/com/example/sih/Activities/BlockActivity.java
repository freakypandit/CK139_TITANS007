package com.example.sih.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.example.sih.R;
import com.google.android.material.snackbar.Snackbar;

public class BlockActivity extends AppCompatActivity {

    int flag;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);



        flag=2;
        image = findViewById(R.id.image);
        Button floor0 = findViewById(R.id.floor0);
        Button floor1 = findViewById(R.id.floor1);
        Button floor2 = findViewById(R.id.floor2);
        Button floor3 = findViewById(R.id.floor3);
        final Button navigate = findViewById(R.id.navigate);

        floor0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=0;


            }
        });

        floor1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                flag=1;

                image.setImageResource(R.drawable.floor2);
                image.setVisibility(View.VISIBLE);
                image.setOnTouchListener(new ImageMatrixTouchHandler(BlockActivity.this));
            }
        });

        floor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=2;

                image.setImageResource(R.drawable.floor3);
                image.setVisibility(View.VISIBLE);
                image.setOnTouchListener(new ImageMatrixTouchHandler(BlockActivity.this));
            }
        });

        floor3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flag=3;

                image.setImageResource(R.drawable.floor4);
                image.setVisibility(View.VISIBLE);
                image.setOnTouchListener(new ImageMatrixTouchHandler(BlockActivity.this));

            }
        });

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==3){
                    AlertDialog.Builder builder = new AlertDialog.Builder(BlockActivity.this);
                    builder.setTitle("Choose Destination");
                    String[] animals = {"Room 414","Room 418","Room 419","Room 410","Room 411","Room 412"};
                    builder.setItems(animals, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0: solve();
                                case 1: solve();
                                case 2: solve();
                                case 3: solve();
                                case 4: solve();
                                case 5: solve();
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                if(flag==2){
                    AlertDialog.Builder builder = new AlertDialog.Builder(BlockActivity.this);
                    builder.setTitle("Choose Destination");
                    String[] animals = {"Room 314","Room 318","Room 319","Room 310","Room 311","Room 312"};
                    builder.setItems(animals, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0: solve();
                                case 1: solve();
                                case 2: solve();
                                case 3: solve();
                                case 4: solve();
                                case 5: solve();
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                if(flag==1){
                    AlertDialog.Builder builder = new AlertDialog.Builder(BlockActivity.this);
                    builder.setTitle("Choose Destination");
                    String[] animals = {"Room 214","Room 218","Room 219","Room 210","Room 211","Room 212"};
                    builder.setItems(animals, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0: solve();
                                case 1: solve();
                                case 2: solve();
                                case 3: solve();
                                case 4: solve();
                                case 5: solve();
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.activity_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.wheel_chair:
                Toast.makeText(this,"Wheel chair",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(BlockActivity.this, ar.class);
                startActivity(intent);
                return true;

            case R.id.elevator:
                Toast.makeText(this,"Elevator",Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(BlockActivity.this, ar.class);
                startActivity(intent2);
                return true;

            case R.id.restroom:
                Toast.makeText(this,"Restroom",Toast.LENGTH_LONG).show();
                Intent intent3 = new Intent(BlockActivity.this, ar.class);
                startActivity(intent3);
                return true;

            case R.id.stair:
                Toast.makeText(this,"Stair",Toast.LENGTH_LONG).show();
                Intent intent4 = new Intent(BlockActivity.this, ar.class);
                startActivity(intent4);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void solve(){

        AlertDialog.Builder builder = new AlertDialog.Builder(BlockActivity.this);
        builder.setTitle("Are you visiting somebody or you want to navigate ?");

        builder.setNegativeButton("Visiting",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RelativeLayout layout = findViewById(R.id.block_layout);
                        Snackbar snackbar = Snackbar.make(layout, "Please ask the person to turn on his hotspot.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(BlockActivity.this, RssiActivity.class));
                            }
                        },2000);
                    }
                });

        builder.setPositiveButton("Navigate",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RelativeLayout layout = findViewById(R.id.block_layout);
                        Snackbar snackbar = Snackbar.make(layout, "You will be navigated to the room.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(BlockActivity.this, ar.class));
                            }
                        },2000);
                    }
                });

        builder.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
