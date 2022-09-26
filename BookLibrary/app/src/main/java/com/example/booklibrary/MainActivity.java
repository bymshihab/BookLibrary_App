package com.example.booklibrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.booklibrary.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    RecyclerView recyclerView;
    FloatingActionButton add_button;

    SwitchMaterial switch1;


    MyDatabaseHelper myDB;
    ArrayList<String> book_id, book_title, book_author, book_pages;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
       // setContentView(binding.getRoot());
        //replaceFragment(new supportFragment());
        binding.bottomNavigationView.setOnClickListener(iteam ->{

            switch (iteam.getId()){
                case R.id.support:
                    replaceFragment(new supportFragment());
                case R.id.info:
                    replaceFragment(new infoFragment());
                case R.id.settings:
                    replaceFragment(new settingdFragment());
            }

            return;
        });


        switch1 = findViewById(R.id.switch1);
        switch1.setChecked(false);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
         });

                recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(MainActivity.this);
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();

        storeDAtaInArrays();

        customAdapter = new CustomAdapter(MainActivity.this,this, book_id, book_title, book_author,
                book_pages);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


    }

  private  void replaceFragment(Fragment fragment){
      FragmentManager fragmentManager = getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.recyclerView,fragment);
      fragmentTransaction.commit();

  }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void storeDAtaInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount()== 0){
            Toast.makeText(this,"No Text",Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));
            }
        }
    }


}