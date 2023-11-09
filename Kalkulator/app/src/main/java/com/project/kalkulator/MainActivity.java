package com.project.kalkulator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText editBil1, editBil2;
    private TextView textHasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editBil1 = (EditText) findViewById(R.id.editTextBil1);
        editBil2 = (EditText) findViewById(R.id.editTextBil2);
        textHasil = (TextView) findViewById(R.id.textViewHasil);
    }

    private void operasi (char op){
        float bil1, bil2, hasil =0;
        bil1 = Float.parseFloat(editBil1.getText().toString());
        bil2 = Float.parseFloat(editBil2.getText().toString());

        switch (op) {
            case '+': hasil = bil1 + bil2; break;
            case '-': hasil = bil1 - bil2; break;
            case '*': hasil = bil1 * bil2; break;
            case '/': hasil = bil1 / bil2; break;
        }

        textHasil.setText(bil1 + " " + op + " " + bil2 + " = " + hasil);
    }

    public void tambah(View v){ operasi('+'); }
    public void kurang(View v){ operasi('-'); }
    public void kali(View v){ operasi('*'); }
    public void bagi(View v){ operasi('/'); }

}