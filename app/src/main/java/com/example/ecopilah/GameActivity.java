package com.example.ecopilah;

import android.content.ClipData;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final String PREF_NAME = "EcoPilahPrefs";
    private static final String KEY_HIGH_SCORE = "HIGH_SCORE";
    private static final long START_TIME_MS = 10_000L;
    private static final long TIME_BONUS_MS = 1_000L;
    private static final long TIME_PENALTY_MS = 1_000L;

    private final Random random = new Random();
    private final List<WasteItem> wasteItems = new ArrayList<>();

    private TextView tvScore;
    private TextView tvTimer;
    private TextView tvWasteName;
    private ImageView imgSampah;
    private ImageView tongOrganik;
    private ImageView tongAnorganik;
    private ImageView tongB3;

    private CountDownTimer countDownTimer;
    private WasteItem currentItem;
    private int score = 0;
    private long timeRemainingMs = START_TIME_MS;
    private boolean gameFinished = false;

    private static class WasteItem {
        private final String name;
        private final String category;
        private final int drawableResId;

        private WasteItem(String name, String category, int drawableResId) {
            this.name = name;
            this.category = category;
            this.drawableResId = drawableResId;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        bindViews();
        initWasteItems();
        setupActions();
        updateScoreText();
        loadNextWaste();
        startTimer(timeRemainingMs);
    }

    private void bindViews() {
        tvScore = findViewById(R.id.tv_score);
        tvTimer = findViewById(R.id.tv_timer);
        tvWasteName = findViewById(R.id.tv_nama_sampah);
        imgSampah = findViewById(R.id.img_sampah);
        tongOrganik = findViewById(R.id.tong_organik);
        tongAnorganik = findViewById(R.id.tong_anorganik);
        tongB3 = findViewById(R.id.tong_b3);
        Button btnSurrender = findViewById(R.id.btn_surrender);
        btnSurrender.setOnClickListener(v -> gameOver());
    }

    private void initWasteItems() {
        wasteItems.add(new WasteItem("Kulit Pisang", "organik", R.drawable.img_sampah_kulit_pisang));
        wasteItems.add(new WasteItem("Daun Kering", "organik", R.drawable.img_sampah_daun_kering));
        wasteItems.add(new WasteItem("Cangkang Telur", "organik", R.drawable.img_sampah_cangkang_telur));
        wasteItems.add(new WasteItem("Botol Plastik", "anorganik", R.drawable.img_sampah_botol_plastik));
        wasteItems.add(new WasteItem("Kaleng Soda", "anorganik", R.drawable.img_sampah_kaleng_soda));
        wasteItems.add(new WasteItem("Kardus Bekas", "anorganik", R.drawable.img_sampah_kardus_bekas));
        wasteItems.add(new WasteItem("Baterai Bekas", "b3", R.drawable.img_sampah_baterai_bekas));
        wasteItems.add(new WasteItem("Lampu Bohlam", "b3", R.drawable.img_sampah_lampu_bohlam));
        wasteItems.add(new WasteItem("Masker Medis", "b3", R.drawable.img_sampah_masker_medis));
    }

    private void setupActions() {
        imgSampah.setOnTouchListener((view, event) -> {
            if (gameFinished || currentItem == null) {
                return false;
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("category", currentItem.category);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(data, shadowBuilder, currentItem, 0);
                return true;
            }
            return false;
        });

        View.OnDragListener binDragListener = (view, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    view.animate().scaleX(1.08f).scaleY(1.08f).setDuration(100).start();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                case DragEvent.ACTION_DRAG_ENDED:
                    view.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    return true;
                case DragEvent.ACTION_DROP:
                    view.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    handleDrop(getCategoryForBin(view.getId()));
                    return true;
                default:
                    return false;
            }
        };

        tongOrganik.setOnDragListener(binDragListener);
        tongAnorganik.setOnDragListener(binDragListener);
        tongB3.setOnDragListener(binDragListener);
    }

    private String getCategoryForBin(int viewId) {
        if (viewId == R.id.tong_organik) {
            return "organik";
        }
        if (viewId == R.id.tong_anorganik) {
            return "anorganik";
        }
        if (viewId == R.id.tong_b3) {
            return "b3";
        }
        return "";
    }

    private void handleDrop(String selectedCategory) {
        if (gameFinished || currentItem == null) {
            return;
        }

        if (currentItem.category.equals(selectedCategory)) {
            score += 10;
            timeRemainingMs += TIME_BONUS_MS;
            Toast.makeText(this, "Benar! +10 poin, +1 detik", Toast.LENGTH_SHORT).show();
        } else {
            timeRemainingMs = Math.max(0L, timeRemainingMs - TIME_PENALTY_MS);
            Toast.makeText(this, "Salah! -1 detik", Toast.LENGTH_SHORT).show();
        }

        updateScoreText();
        if (timeRemainingMs <= 0L) {
            gameOver();
            return;
        }

        restartTimer();
        loadNextWaste();
    }

    private void loadNextWaste() {
        currentItem = wasteItems.get(random.nextInt(wasteItems.size()));
        tvWasteName.setText(currentItem.name);
        imgSampah.setImageResource(currentItem.drawableResId);
        imgSampah.setContentDescription(currentItem.name);
    }

    private void startTimer(long durationMs) {
        cancelTimer();
        timeRemainingMs = durationMs;
        updateTimerText();

        countDownTimer = new CountDownTimer(timeRemainingMs, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingMs = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timeRemainingMs = 0L;
                updateTimerText();
                gameOver();
            }
        }.start();
    }

    private void restartTimer() {
        startTimer(timeRemainingMs);
    }

    private void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void updateScoreText() {
        tvScore.setText("Skor: " + formatNumber(score));
    }

    private void updateTimerText() {
        long seconds = (long) Math.ceil(timeRemainingMs / 1000.0);
        tvTimer.setText("Waktu: " + seconds + "s");
    }

    private void gameOver() {
        if (gameFinished) {
            return;
        }

        gameFinished = true;
        cancelTimer();
        saveHighScoreIfNeeded();

        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Game Over! Skor Akhir Anda: " + formatNumber(score) + " Poin")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .show();
    }

    private void saveHighScoreIfNeeded() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int highScore = prefs.getInt(KEY_HIGH_SCORE, 0);
        if (score > highScore) {
            prefs.edit().putInt(KEY_HIGH_SCORE, score).apply();
        }
    }

    private String formatNumber(int value) {
        return NumberFormat.getNumberInstance(new Locale("id", "ID")).format(value);
    }

    @Override
    protected void onDestroy() {
        cancelTimer();
        super.onDestroy();
    }
}
