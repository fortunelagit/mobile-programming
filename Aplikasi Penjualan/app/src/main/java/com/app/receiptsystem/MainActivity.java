package com.app.receiptsystem;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private EditText edtNamaPembeli, edtBarang, edtJumlahBarang, edtHargaBarang, edtBayar;
    private Button btnProses, btnHapus, btnKeluar;
    private TextView txtNamaPembeli, txtBarang, txtJumlahBarang, txtHargaBarang, txtBayar, txtBonus, txtTotal, txtKembalian, txtKeterangan;

    private LinearLayout liReceipt;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().setTitle("ReceiptSys");

        edtNamaPembeli= (EditText) findViewById(R.id.etNamaPembeli);
        edtBarang = (EditText) findViewById(R.id.etBarang);
        edtJumlahBarang = (EditText) findViewById(R.id.etJumlahBarang);
        edtHargaBarang = (EditText) findViewById(R.id.etHargaBarang);
        edtBayar = (EditText) findViewById(R.id.etBayar);

        btnProses = (Button) findViewById(R.id.btnProses);
        btnHapus = (Button) findViewById(R.id.btnHapus);
        btnKeluar = (Button) findViewById(R.id.btnKeluar);

        txtNamaPembeli = (TextView) findViewById(R.id.tvNamaPembeli);
        txtBarang = (TextView) findViewById(R.id.tvBarang);
        txtJumlahBarang = (TextView) findViewById(R.id.tvJumlahBarang);
        txtHargaBarang = (TextView) findViewById(R.id.tvHargaBarang);
        txtBayar = (TextView) findViewById(R.id.tvBayar);
        txtBonus = (TextView) findViewById(R.id.tvBonus);
        txtTotal = (TextView) findViewById(R.id.tvTotal);
        txtKembalian = (TextView) findViewById(R.id.tvKembalian);
        txtKeterangan = (TextView) findViewById(R.id.tvKeterangan);

        liReceipt = (LinearLayout)findViewById(R.id.ll_Receipt);

        btnProses.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String nama_pembeli = edtNamaPembeli.getText().toString().trim();
                String barang = edtBarang.getText().toString().trim();
                String jumlah_barang = edtJumlahBarang.getText().toString().trim();
                String harga_barang = edtHargaBarang.getText().toString().trim();
                String bayar = edtBayar.getText().toString().trim();

                int jb = Integer.parseInt(jumlah_barang);
                int hb = Integer.parseInt(harga_barang);
                int by = Integer.parseInt(bayar);

                int total_bayar = (jb * hb);
                int kembalian = (by - total_bayar);

                if(kembalian<0){
                    liReceipt.setBackgroundColor(Color.parseColor("#F8E7E7"));

                    txtNamaPembeli.setText("Nama Pembeli : " + nama_pembeli);
                    txtBarang.setText("Barang : " + barang);
                    txtJumlahBarang.setText("Jumlah Barang : "+ jumlah_barang);
                    txtHargaBarang.setText("Harga Barang : "+ harga_barang);
                    txtBayar.setText("Bayar : "+ bayar);
                    txtTotal.setText("Total : "+ total_bayar);
                    txtKembalian.setText("Kembalian : -");
                    txtBonus.setText("Bonus : -");
                    txtKeterangan.setText("Keterangan : Pembayaran gagal karena uang pembayaran kurang" );
                    Toast.makeText(getApplicationContext(),"Pembayaran Gagal", Toast.LENGTH_LONG).show();
                }

                else{
                    liReceipt.setBackgroundColor(Color.parseColor("#90EE90"));
                    txtNamaPembeli.setText("Nama Pembeli : " + nama_pembeli);
                    txtBarang.setText("Barang : " + barang);
                    txtJumlahBarang.setText("Jumlah Barang : "+ jumlah_barang);
                    txtHargaBarang.setText("Harga Barang : "+ harga_barang);
                    txtBayar.setText("Bayar : "+ bayar);
                    txtTotal.setText("Total : "+ total_bayar);
                    txtKembalian.setText("Kembalian : "+ kembalian);
                    txtBonus.setText("Bonus : -");
                    txtKeterangan.setText("Keterangan : Pembayaran berhasil" );
                    Toast.makeText(getApplicationContext(),"Pembayaran Berhasil", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                liReceipt.setBackgroundColor(Color.parseColor("#FFFFFF"));

                txtNamaPembeli.setText(" ");
                txtBarang.setText(" ");
                txtJumlahBarang.setText(" ");
                txtHargaBarang.setText(" ");
                txtBayar.setText(" ");
                txtTotal.setText(" ");
                txtKembalian.setText(" ");
                txtBonus.setText(" ");
                txtKeterangan.setText(" ");

                Toast.makeText(getApplicationContext(),"Data sudah direset", Toast.LENGTH_LONG).show();
            }
        });

        btnKeluar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                moveTaskToBack(true);
            }
        });
    }
}