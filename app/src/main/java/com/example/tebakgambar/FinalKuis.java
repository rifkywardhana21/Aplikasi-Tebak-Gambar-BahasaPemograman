package com.example.tebakgambar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FinalKuis extends AppCompatActivity {

    private TextView waktu;
    private TextView resultTextView;
    private TextView questionTextView;
    private ImageView Question;
    private Button submitButton;
    private RadioGroup answersRadioGroup;
    private RadioButton answer1RadioButton;
    private RadioButton answer2RadioButton;
    private RadioButton answer3RadioButton;
    private RadioButton answer4RadioButton;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 90000;

    int benar, salah = 0;
    int nilai;
    int nomoreasy = 0;

    DatabaseHelper dbHelper;

    int Soalangka[] = new int[]{
            R.drawable.python,
            R.drawable.vscode,
            R.drawable.cpp,
            R.drawable.csharp,
            R.drawable.javascript,
            R.drawable.github,
            R.drawable.kotlin,
            R.drawable.laravel,
            R.drawable.ruby,
            R.drawable.tailwind,
    };

    String Soal[] = new String[]{
            "1. Apakah nama dari logo ini?",
            "2. Apakah nama dari logo ini?",
            "3. Apakah nama dari logo ini?",
            "4. Apakah nama dari logo ini?",
            "5. Apakah nama dari logo ini?",
            "6. Apakah nama dari logo ini?",
            "7. Apakah nama dari logo ini?",
            "8. Apakah nama dari logo ini?",
            "9. Apakah nama dari logo ini?",
            "10.Apakah nama dari logo ini?",
    };

    String Pilihanjawaban[] = new String[]{
            "Python", "Java", "C++", "Kotlin",
            "JetBrains", "VSCode", "NetBeans", "CodeBlocks",
            "C#", "JavaScript", "Java", "C++",
            "C", "C#", "Groovy", "Apex",
            "JavaScript", "Python", "Kotlin", "CSS",
            "GitHub", "Git", "Laragon", "Firebase",
            "Java", "Swift", "Dart", "Kotlin",
            "Tailwind", "Laravel", "PHP", "Java",
            "Ruby", "C", "Groovy", "Apex",
            "Laravel", "Tailwind", "Swift", "CSS",
    };

    String Jawaban[] = new String[]{
            "python",
            "vscode",
            "c++",
            "c#",
            "javascript",
            "github",
            "kotlin",
            "laravel",
            "ruby",
            "tailwind",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_final_kuis);

        waktu = findViewById(R.id.waktu);
        resultTextView = findViewById(R.id.resultTextView);
        questionTextView = findViewById(R.id.questionTextView);
        submitButton = findViewById(R.id.submitButton);
        Question = findViewById(R.id.Question);
        answersRadioGroup = findViewById(R.id.answersRadioGroup);
        answer1RadioButton = findViewById(R.id.answer1RadioButton);
        answer2RadioButton = findViewById(R.id.answer2RadioButton);
        answer3RadioButton = findViewById(R.id.answer3RadioButton);
        answer4RadioButton = findViewById(R.id.answer4RadioButton);

        dbHelper = new DatabaseHelper();
        nilai = getIntent().getIntExtra("nilai", 0);

        questionTextView.setText(Soal[nomoreasy]);
        Question.setImageResource(Soalangka[nomoreasy]);
        answersRadioGroup.check(0);
            answer1RadioButton.setText(Pilihanjawaban[0]);
            answer2RadioButton.setText(Pilihanjawaban[1]);
            answer3RadioButton.setText(Pilihanjawaban[2]);
            answer4RadioButton.setText(Pilihanjawaban[3]);

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                waktu.setText(millisUntilFinished / 1000 + "s");
            }
            @Override
            public void onFinish() {
                waktu.setText("Waktu Habis!!");
                nilai = benar * 10;
                dbHelper.updateNilai(1, nilai);
                Intent next = new Intent(getApplicationContext(), HasilKuis.class);
                next.putExtra("nilai", nilai);
                next.putExtra("benar", benar);
                next.putExtra("salah", salah);
                startActivity(next);
            }
        }.start();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer1RadioButton.isChecked() || answer2RadioButton.isChecked() || answer3RadioButton.isChecked() || answer4RadioButton.isChecked()) {

                    RadioButton Pilihan_User = findViewById(answersRadioGroup.getCheckedRadioButtonId());
                    String Jawaban_User = Pilihan_User.getText().toString();
                    answersRadioGroup.check(0);
                        if (Jawaban_User.equalsIgnoreCase(Jawaban[nomoreasy])) {
                            benar++;
                            resultTextView.setText("Excellent!");
                        } else {
                            salah++;
                            resultTextView.setText("Salah bro");
                        }
                        nomoreasy++;
                        if (nomoreasy < Soal.length) {
                            questionTextView.setText(Soal[nomoreasy]);
                            Question.setImageResource(Soalangka[nomoreasy]);

                            answer1RadioButton.setText(Pilihanjawaban[(nomoreasy * 4) + 0]);
                            answer2RadioButton.setText(Pilihanjawaban[(nomoreasy * 4) + 1]);
                            answer3RadioButton.setText(Pilihanjawaban[(nomoreasy * 4) + 2]);
                            answer4RadioButton.setText(Pilihanjawaban[(nomoreasy * 4) + 3]);
                        } else {
                            nilai = benar * 10;
                            dbHelper.updateNilai(1, nilai);
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }
                            Intent next = new Intent(getApplicationContext(), HasilKuis.class);
                            next.putExtra("nilai", nilai);
                            next.putExtra("benar", benar);
                            next.putExtra("salah", salah);
                            startActivity(next);
                        }
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}