package com.project.entryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.content.ContentValues;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;


public class MainActivity extends AppCompatActivity {

    private EditText nrp, nama;
    private TextView data;
    private Button simpan, ambildata, update, delete, tampilkan;
    private SQLiteDatabase dbku;
    private SQLiteOpenHelper OpenDB;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nrp = (EditText) findViewById(R.id.etNrp);
        nama = (EditText) findViewById(R.id.etNama);
//        data = (TextView) findViewById((R.id.tvData));

        simpan = (Button) findViewById(R.id.btnSimpan);
        ambildata = (Button) findViewById(R.id.btnAmbildata);
        update = (Button) findViewById(R.id.btnUpdate);
        delete = (Button) findViewById(R.id.btnDelete);
        tampilkan = (Button) findViewById(R.id.btnTampilkan);

        simpan.setOnClickListener(operasi);
        ambildata.setOnClickListener(operasi);
        update.setOnClickListener(operasi);
        delete.setOnClickListener(operasi);
        tampilkan.setOnClickListener(operasi);

        OpenDB = new SQLiteOpenHelper(this, "db.sql", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            }
        };
        dbku = OpenDB.getWritableDatabase();
        dbku.execSQL("create table if not exists mhs(nrp TEXT, nama TEXT);");
    }

    @Override
    protected void onStop() {
        dbku.close();
        OpenDB.close();
        super.onStop();
    }

    View.OnClickListener operasi = new View.OnClickListener() {

        public void onClick(View v) {
            if(v.getId() == R.id.btnSimpan){simpan();}
            else if(v.getId() == R.id.btnAmbildata){ambildata();}
            else if(v.getId() == R.id.btnUpdate){update();}
            else if(v.getId() == R.id.btnDelete){delete();}
            else if(v.getId() == R.id.btnTampilkan){tampilkan();}
        }
    };

    private void simpan()
    {
        ContentValues dataku = new ContentValues();

        dataku.put("nrp", nrp.getText().toString());
        dataku.put("nama", nama.getText().toString());
        dbku.insert("mhs",null,dataku);
        Toast.makeText(this,"Data Tersimpan",Toast.LENGTH_LONG).show();
    }

    private void ambildata(){
        Cursor cur = dbku.rawQuery("select * from mhs where nrp='" +
                nrp.getText().toString()+ "'",null);

        if(cur.getCount() >0)
        {
            Toast.makeText(this,"Data Ditemukan",Toast.LENGTH_LONG).show();
            cur.moveToFirst();

            nama.setText(cur.getString(1));
        }
        else
            Toast.makeText(this,"Data Tidak Ditemukan",Toast.LENGTH_LONG).show();
    }

    private void update()
    {
        ContentValues dataku = new ContentValues();

        dataku.put("nrp",nrp.getText().toString());
        dataku.put("nama",nama.getText().toString());
        dbku.update("mhs",dataku,"nrp='"+nrp.getText().toString()+"'",null);
        Toast.makeText(this,"Data Terupdate",Toast.LENGTH_LONG).show();
    }

    private void delete()
    {
//        dbku.delete("mhs","nrp='"+nrp.getText().toString()+"'",null);
//        Toast.makeText(this,"Data Terhapus",Toast.LENGTH_LONG).show();

        int rowsDeleted = dbku.delete("mhs", "nrp='" + nrp.getText().toString() + "'", null);
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Data Terhapus", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Proses Hapus Gagal", Toast.LENGTH_LONG).show();
        }
    }

//    private void tampilkan() {
//        Cursor cur = dbku.rawQuery("select * from mhs", null);
//
//        if (cur.getCount() > 0) {
//            StringBuilder allRowsData = new StringBuilder();
//
//            while (cur.moveToNext()) {
//                StringBuilder rowData = new StringBuilder();
//                for (int i = 0; i < cur.getColumnCount(); i++) {
//                    String columnName = cur.getColumnName(i);
//                    String columnValue = cur.getString(i);
//                    rowData.append(columnName).append(": ").append(columnValue).append("\n");
//                }
//                allRowsData.append(rowData.toString()).append("\n");
//            }
//            data.setText("");
//            data.setText(allRowsData.toString());
//        } else {
//            Toast.makeText(this, "Tidak ada data", Toast.LENGTH_LONG).show();
//        }
//    }

    private void tampilkan() {
        Cursor cur = dbku.rawQuery("select * from mhs", null);

        if (cur.getCount() > 0) {
            TableLayout tableLayout = findViewById(R.id.tableLayout);

            // Create a row for column headers
            TableRow headerRow = new TableRow(this);

            // Create TextViews for column headers and set their text
            TextView headerNamaTextView = new TextView(this);
            headerNamaTextView.setText("Nama");

            TextView headerNrpTextView = new TextView(this);
            headerNrpTextView.setText("NRP");

            // Add padding to column headers
            headerNamaTextView.setPadding(20, 0, 20, 0); // Left and right padding
            headerNrpTextView.setPadding(20, 0, 20, 0);   // Left and right padding

            // Add column header TextViews to the header row
            headerRow.addView(headerNamaTextView);
            headerRow.addView(headerNrpTextView);

            // Add the header row to the table layout
            tableLayout.addView(headerRow);

            while (cur.moveToNext()) {
                String namaValue = cur.getString(0);
                String nrpValue = cur.getString(1);

                TableRow tableRow = new TableRow(this);

                TextView namaTextView = new TextView(this);
                namaTextView.setText(namaValue);

                TextView nrpTextView = new TextView(this);
                nrpTextView.setText(nrpValue);

                // Add padding to data columns
                namaTextView.setPadding(20, 0, 20, 0); // Left and right padding
                nrpTextView.setPadding(20, 0, 20, 0);   // Left and right padding

                tableRow.addView(namaTextView);
                tableRow.addView(nrpTextView);

                tableLayout.addView(tableRow);
            }
        } else {
            Toast.makeText(this, "Tidak ada data", Toast.LENGTH_LONG).show();
        }
    }









}


