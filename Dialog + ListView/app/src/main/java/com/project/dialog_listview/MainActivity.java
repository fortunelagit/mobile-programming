package com.project.dialog_listview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView lvContact;
    private ImageView addContact;
    private contactAdapter cAdapter;
    private SQLiteDatabase dbku;
    private SQLiteOpenHelper dbopen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvContact = (ListView) findViewById(R.id.lvData);
        addContact = (ImageView) findViewById(R.id.btnAdd);
        addContact.setOnClickListener(operasi);

        ArrayList<contact> listContact = new ArrayList<contact>();
        cAdapter = new contactAdapter(this, 0, listContact);
        lvContact.setAdapter(cAdapter);

        dbopen = new SQLiteOpenHelper(this, "contact.db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };

        dbku = dbopen.getWritableDatabase();
        dbku.execSQL("create table if not exists contact(name TEXT, phoneNumber TEXT);");
        show_data();
    }

    //function to decide what form function to show based on the button
    View.OnClickListener operasi= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btnAdd){ add_data();}
            else if(v.getId() == R.id.btnEdit){ }
            else if(v.getId() == R.id.btnDelete){ }
            else if(v.getId() == R.id.btnSearch){ }
        }
    };


    //function to show the new item on list view and
    // add the new item to database
    private void add_item(String name, String phoneNumber){
        ContentValues datanya = new ContentValues();
        datanya.put("name", name);
        datanya.put("phoneNumber", phoneNumber);
        dbku.insert("contact", null, datanya);

        contact newContact = new contact(name, phoneNumber);
        cAdapter.add(newContact);
    }

    //function to show the form dialog
    private void add_data(){
        AlertDialog.Builder buat = new AlertDialog.Builder(this);
        buat.setTitle("Add Contact");

        View vAdd = LayoutInflater.from(this).inflate(R.layout.input_dialog, null);
        final EditText name = (EditText) vAdd.findViewById(R.id.etName);
        final EditText phoneNumber = (EditText) vAdd.findViewById(R.id.etPhoneNumber);

        buat.setView(vAdd);
        buat.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                add_item(name.getText().toString(), phoneNumber.getText().toString());
                Toast.makeText(getBaseContext(), "Contacts Added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        buat.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        buat.show();
    }

    //function to (show) add the data to the list view from database
    private void show_data(){
        Cursor cur = dbku.rawQuery("select * from contact", null);
        Toast.makeText(this, "Terdapat sejumlah " + cur.getCount(), Toast.LENGTH_LONG).show();
        int i=0;

        int nameIndex = cur.getColumnIndex("name");
        int phoneIndex = cur.getColumnIndex("phoneNumber");

        if(cur.getCount() > 0) cur.moveToFirst();
        while(i<cur.getCount()){
            add_item(cur.getString(nameIndex),cur.getString(phoneIndex));
            cur.moveToNext();
            i++;

        }
    }

}