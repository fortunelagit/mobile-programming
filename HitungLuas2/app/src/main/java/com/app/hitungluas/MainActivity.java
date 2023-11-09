package com.app.hitungluas;

import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText edtPanjang, edtLebar;
    private Button btnHitung;
    private TextView txtLuas, txtKeliling;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().setTitle("Hitung Luas Persegi Panjang");

        edtPanjang = (EditText) findViewById(R.id.edt_panjang);
        edtLebar = (EditText) findViewById(R.id.edt_lebar);
        btnHitung = (Button) findViewById(R.id.btn_hitung);
        txtLuas = (TextView) findViewById(R.id.txt_luas);
        txtKeliling = (TextView) findViewById(R.id.txt_keliling);

        btnHitung.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String panjang = edtPanjang.getText().toString().trim();
                String lebar = edtLebar.getText().toString().trim();

                double p = Double.parseDouble(panjang);
                double l = Double.parseDouble(lebar);

                double luas = p * l;
                double keliling = 2 * (p + l);

                txtLuas.setText("Luas: " + String.format("%.2f", luas));
                txtKeliling.setText("Keliling: " + String.format("%.2f", keliling));
            }
        });
    }
}