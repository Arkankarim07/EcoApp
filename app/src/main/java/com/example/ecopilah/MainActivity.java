package com.example.ecopilah;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvScore = findViewById(R.id.tv_score);

        TextView tvTips = findViewById(R.id.tv_tips_text);
        if (tvTips != null) {
            String htmlText = "<b>Tip:</b> Sampah plastik butuh hingga 500 tahun untuk terurai!";
            tvTips.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY));
        }

        findViewById(R.id.card_learn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EdukasiActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.card_game).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAndDisplayHighScore();
    }

    private void loadAndDisplayHighScore() {
        SharedPreferences prefs = getSharedPreferences("EcoPilahPrefs", MODE_PRIVATE);
        int highScore = prefs.getInt("HIGH_SCORE", 0);

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("id", "ID"));
        String formattedScore = nf.format(highScore) + " Poin";

        if (tvScore != null) {
            tvScore.setText(formattedScore);
        }
    }
}
