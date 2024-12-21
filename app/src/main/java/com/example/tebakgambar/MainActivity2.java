package com.example.tebakgambar;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tebakgambar.DatabaseHelper;

public class MainActivity2 extends AppCompatActivity {

    private static final int GAME_REQUEST_CODE = 1;

    MediaPlayer audio;
    Button buttonquit;
    Button newgame;
    Button tutorial;
    Button minigame;
    private TextView poin;
    private boolean isBackPressedConfirmed = false;
    int nilai;
    String difficulty;  // Tambahkan variabel untuk menyimpan difficulty
    String minigamedifficulty;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        dbHelper = new DatabaseHelper();

        poin = findViewById(R.id.poin);
        newgame = findViewById(R.id.newgame);
//        tutorial = findViewById(R.id.tutorial);
//        minigame = findViewById(R.id.minigame);

        dbHelper.getNilai(1, new DatabaseHelper.DataCallback() {
            @Override
            public void onDataReceived(Nilai dataNilai) {
                if (dataNilai != null) {
                    nilai = dataNilai.getNilai();
                    poin.setText(String.valueOf(nilai));
                } else {
                    Log.e("Game", "Data tidak ditemukan untuk ID: 1");
                }
            }
        });

        minigamedifficulty = "minigamedifficulty";
        Log.d("Game", "Difficulty saat ini: " + difficulty);

        poin.setText(String.valueOf(nilai));

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity2.this, Game.class);
                    intent.putExtra("nilai", nilai);
                    intent.putExtra("difficulty", difficulty);
                    startActivityForResult(intent, GAME_REQUEST_CODE);
            }
        });

        newgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
                builder.setTitle("Reset Game");
                builder.setMessage("Apakah Anda yakin ingin Mengulang game? progres anda akan hilang!!");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        nilai = 0; // Set nilai ke 0
                        difficulty = "null"; // Reset difficulty ke default atau sesuai keinginan
                        dbHelper.updateNilai(1, nilai); // Simpan nilai dan difficulty ke database
                        poin.setText(String.valueOf(nilai)); // Perbarui tampilan poin
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        buttonquit = findViewById(R.id.buttonquit);
        buttonquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audio != null) {
                    audio.stop();
                    audio.release();
                }
                dbHelper.updateNilai(1, nilai); // Simpan nilai dan difficulty ke database sebelum keluar
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GAME_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                nilai = data.getIntExtra("nilai", 0);
                difficulty = data.getStringExtra("difficulty");
                poin.setText(String.valueOf(nilai));
                dbHelper.updateNilai(1, nilai); // Simpan nilai dan difficulty ke database setelah kembali dari Game.java
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbHelper.getNilai(1, new DatabaseHelper.DataCallback() {
            @Override
            public void onDataReceived(Nilai dataNilai) {
                if (dataNilai != null) {
                    nilai = dataNilai.getNilai();
                    poin.setText(String.valueOf(nilai));
                    if (nilai == 0) {
                        newgame.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("Game", "Data tidak ditemukan untuk ID: 1");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isBackPressedConfirmed) {
            super.onBackPressed();
        } else {
            showExitConfirmationDialog();
        }
    }

    private void showExitConfirmationDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
        builder.setTitle("Keluar Game");
        builder.setMessage("Apakah Anda yakin ingin keluar?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                isBackPressedConfirmed = true;
                dbHelper.updateNilai(1, nilai); // Simpan nilai dan difficulty ke database sebelum keluar
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}