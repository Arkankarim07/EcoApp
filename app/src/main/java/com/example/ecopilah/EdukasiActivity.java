package com.example.ecopilah;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class EdukasiActivity extends AppCompatActivity {

    private static final String ORGANIK = "organik";
    private static final String ANORGANIK = "anorganik";
    private static final String B3 = "b3";

    private TextView tabOrganik;
    private TextView tabAnorganik;
    private TextView tabB3;
    private LinearLayout gridContainer;

    private final List<WasteInfo> wasteItems = new ArrayList<>();
    private String activeCategory = ORGANIK;

    private static class WasteInfo {
        private final String title;
        private final String category;
        private final String tag;
        private final String tip;
        private final int imageResId;
        private final int accentColorResId;

        private WasteInfo(String title, String category, String tag, String tip, int imageResId, int accentColorResId) {
            this.title = title;
            this.category = category;
            this.tag = tag;
            this.tip = tip;
            this.imageResId = imageResId;
            this.accentColorResId = accentColorResId;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edukasi);

        bindViews();
        initWasteItems();
        setupTabs();
        renderCategory(ORGANIK);
    }

    private void bindViews() {
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        tabOrganik = findViewById(R.id.tab_organik);
        tabAnorganik = findViewById(R.id.tab_anorganik);
        tabB3 = findViewById(R.id.tab_b3);
        gridContainer = findViewById(R.id.grid_container);
    }

    private void initWasteItems() {
        wasteItems.add(new WasteInfo(
                "Kulit Pisang",
                ORGANIK,
                "Mudah Terurai",
                "Jangan dibuang begitu saja! Kulit pisang sangat bagus dijadikan pupuk organik cair untuk tanaman hiasmu.",
                R.drawable.img_sampah_kulit_pisang,
                R.color.primary
        ));
        wasteItems.add(new WasteInfo(
                "Daun Kering",
                ORGANIK,
                "Mudah Terurai",
                "Daun kering bisa dikumpulkan dan dicampur tanah untuk membuat kompos rumahan yang ringan dan alami.",
                R.drawable.img_sampah_daun_kering,
                R.color.primary
        ));
        wasteItems.add(new WasteInfo(
                "Cangkang Telur",
                ORGANIK,
                "Mudah Terurai",
                "Remukkan cangkang telur hingga halus, lalu taburkan sedikit ke media tanam sebagai tambahan kalsium.",
                R.drawable.img_sampah_cangkang_telur,
                R.color.primary
        ));
        wasteItems.add(new WasteInfo(
                "Tulang Ayam",
                ORGANIK,
                "Mudah Terurai",
                "Pisahkan dari plastik atau tisu sebelum masuk wadah organik agar proses pengolahan lebih bersih.",
                R.drawable.img_sampah_tulang_ayam,
                R.color.primary
        ));
        wasteItems.add(new WasteInfo(
                "Botol Plastik",
                ANORGANIK,
                "Daur Ulang",
                "Bilas botol, keringkan, lalu pipihkan agar lebih mudah dikumpulkan untuk didaur ulang.",
                R.drawable.img_sampah_botol_plastik,
                R.color.secondary
        ));
        wasteItems.add(new WasteInfo(
                "Kaleng Soda",
                ANORGANIK,
                "Daur Ulang",
                "Pastikan kaleng kosong dan tidak tercampur sisa makanan sebelum masuk wadah anorganik.",
                R.drawable.img_sampah_kaleng_soda,
                R.color.secondary
        ));
        wasteItems.add(new WasteInfo(
                "Kardus Bekas",
                ANORGANIK,
                "Daur Ulang",
                "Lipat kardus agar hemat ruang, dan jauhkan dari sampah basah supaya tetap bernilai daur ulang.",
                R.drawable.img_sampah_kardus_bekas,
                R.color.secondary
        ));
        wasteItems.add(new WasteInfo(
                "Baterai Bekas",
                B3,
                "Perlu Khusus",
                "Simpan baterai bekas di wadah kering dan tertutup, lalu serahkan ke titik kumpul limbah B3.",
                R.drawable.img_sampah_baterai_bekas,
                R.color.error
        ));
        wasteItems.add(new WasteInfo(
                "Lampu Bohlam",
                B3,
                "Perlu Khusus",
                "Bungkus lampu rusak dengan aman agar pecahannya tidak melukai orang lain saat dikumpulkan.",
                R.drawable.img_sampah_lampu_bohlam,
                R.color.error
        ));
        wasteItems.add(new WasteInfo(
                "Masker Medis",
                B3,
                "Perlu Khusus",
                "Gunting tali masker, masukkan ke kantong tertutup, lalu buang sesuai aturan limbah medis rumah tangga.",
                R.drawable.img_sampah_masker_medis,
                R.color.error
        ));
    }

    private void setupTabs() {
        tabOrganik.setOnClickListener(v -> renderCategory(ORGANIK));
        tabAnorganik.setOnClickListener(v -> renderCategory(ANORGANIK));
        tabB3.setOnClickListener(v -> renderCategory(B3));
    }

    private void renderCategory(String category) {
        activeCategory = category;
        updateTabStyles();
        gridContainer.removeAllViews();

        LinearLayout currentRow = null;
        int itemInRow = 0;
        for (WasteInfo item : wasteItems) {
            if (!item.category.equals(category)) {
                continue;
            }

            if (itemInRow == 0) {
                currentRow = createRow();
                gridContainer.addView(currentRow);
            }

            currentRow.addView(createWasteCard(item));
            itemInRow++;

            if (itemInRow == 2) {
                itemInRow = 0;
            }
        }

        if (currentRow != null && itemInRow == 1) {
            View spacer = new View(this);
            currentRow.addView(spacer, createCardLayoutParams());
        }
    }

    private void updateTabStyles() {
        styleTab(tabOrganik, ORGANIK, "Organik", R.drawable.ic_leaf, R.color.primary);
        styleTab(tabAnorganik, ANORGANIK, "Anorganik", R.drawable.ic_recycle, R.color.secondary);
        styleTab(tabB3, B3, "B3", R.drawable.ic_hazard, R.color.error);
    }

    private void styleTab(TextView tab, String category, String label, int iconResId, int colorResId) {
        boolean active = activeCategory.equals(category);
        int color = ContextCompat.getColor(this, colorResId);
        tab.setText(label);
        tab.setTextColor(active ? color : ContextCompat.getColor(this, R.color.on_surface_variant));
        tab.setBackgroundResource(active ? R.drawable.bg_tab_active : R.drawable.bg_tab_inactive);
        tab.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
        tab.setCompoundDrawablePadding(dp(6));
        if (tab.getCompoundDrawables()[0] != null) {
            tab.getCompoundDrawables()[0].setTint(color);
        }
    }

    private LinearLayout createRow() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setBaselineAligned(false);
        row.setWeightSum(2f);
        row.setPadding(0, 0, 0, dp(14));
        row.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return row;
    }

    private View createWasteCard(WasteInfo item) {
        View card = LayoutInflater.from(this).inflate(R.layout.item_waste_card, gridContainer, false);
        card.setLayoutParams(createCardLayoutParams());
        card.setOnClickListener(v -> showWasteDialog(item));

        ImageView image = card.findViewById(R.id.img_waste);
        TextView title = card.findViewById(R.id.tv_waste_title);
        TextView tag = card.findViewById(R.id.tv_waste_tag);

        image.setImageResource(item.imageResId);
        image.setContentDescription(item.title);
        title.setText(item.title);
        tag.setText(item.tag);
        tag.setTextColor(ContextCompat.getColor(this, item.accentColorResId));

        return card;
    }

    private LinearLayout.LayoutParams createCardLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
        );
        params.setMargins(dp(6), 0, dp(6), 0);
        return params;
    }

    private void showWasteDialog(WasteInfo item) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_waste_tip);
        dialog.setCanceledOnTouchOutside(true);

        ImageView image = dialog.findViewById(R.id.img_dialog_waste);
        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        TextView tag = dialog.findViewById(R.id.tv_dialog_tag);
        TextView tip = dialog.findViewById(R.id.tv_dialog_tip);
        TextView close = dialog.findViewById(R.id.btn_dialog_close);

        image.setImageResource(item.imageResId);
        image.setContentDescription(item.title);
        title.setText(item.title);
        tag.setText(item.tag);
        tag.setTextColor(ContextCompat.getColor(this, item.accentColorResId));
        tip.setText(item.tip);
        close.setOnClickListener(v -> dialog.dismiss());

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();

        Window shownWindow = dialog.getWindow();
        if (shownWindow != null) {
            shownWindow.setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.9f),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
