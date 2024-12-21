package com.example.tebakgambar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Game extends AppCompatActivity {

    private TextView poin;
    int nilai;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        dbHelper = new DatabaseHelper();

        poin = findViewById(R.id.poin);

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

        poin.setText(String.valueOf(nilai));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupButton(R.id.buttonboss, FinalKuis.class, 0);
    }

    private void setupButton(int buttonId, Class<?> targetActivity, int minValue) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getNilai(1, new DatabaseHelper.DataCallback() {
                    @Override
                    public void onDataReceived(Nilai dataNilai) {
                        if (dataNilai != null) {
                            nilai = dataNilai.getNilai();
                            poin.setText(String.valueOf(nilai));
                            if (nilai >= minValue) {
                                Intent intent = new Intent(Game.this, targetActivity);
                                intent.putExtra("nilai", nilai);
                                startActivityForResult(intent, minValue);
                            } else {
                                Toast.makeText(Game.this, "Poin anda tidak cukup.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("Game", "Data tidak ditemukan untuk ID: 1");
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            nilai = data.getIntExtra("nilai", nilai);
            poin.setText(String.valueOf(nilai));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Ambil nilai dari database setiap kali Activity dilanjutkan
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Game.this, MainActivity.class);
        intent.putExtra("nilai", nilai);
        setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.updateNilai(1, nilai); // Simpan nilai ke database sebelum keluar
    }
}