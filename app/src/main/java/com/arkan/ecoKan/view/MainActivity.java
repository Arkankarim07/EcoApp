package com.arkan.ecoKan.view;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecopilah.R;

public class MainActivity extends AppCompatActivity {

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

        // Set tip text with bold "Tip:" label using HTML formatting
        TextView tvTips = findViewById(R.id.tv_tips_text);
        if (tvTips != null) {
            String htmlText = "<b>Tip:</b> Sampah plastik butuh hingga 500 tahun untuk terurai!";
            tvTips.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY));
        }

        // Add interactive actions for activity cards
        findViewById(R.id.card_learn).setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Membuka Modul Belajar...", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.card_game).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, com.example.ecopilah.GameActivity.class);
            startActivity(intent);
        });
    }
}
