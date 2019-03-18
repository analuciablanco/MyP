package com.example.myp.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myp.R;

//Class to share the code
public class ShareCodeActivity extends AppCompatActivity {
    TextView    sharecode_textview_parents,
                sharecode_textview_teachers;


    String      sharecode_textview_insertcode_parents,
                sharecode_textview_insertcode_teachers;

    ImageButton sharecode_imagebutton_copy_parents,
                sharecode_imagebutton_copy_teachers;


    ImageButton sharecode_imagebutton_share_parents,
                sharecode_imagebutton_share_teachers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharecode);


        sharecode_textview_parents = findViewById(R.id.sharecode_textview_insertcode_parents);
        sharecode_imagebutton_copy_parents = findViewById(R.id.sharecode_imagebutton_copy_parents);


        sharecode_textview_teachers = findViewById(R.id.sharecode_textview_insertcode_teachers);
        sharecode_imagebutton_copy_teachers = findViewById(R.id.sharecode_imagebutton_copy_teachers);

        sharecode_imagebutton_share_parents = findViewById(R.id.sharecode_imagebutton_share_parents);
        sharecode_imagebutton_share_teachers = findViewById(R.id.sharecode_imagebutton_share_teachers);

        //Class to copy the parents' code
        sharecode_imagebutton_copy_parents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard_parents = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData_parents = ClipData.newPlainText(sharecode_textview_insertcode_parents, sharecode_textview_parents.getText().toString());
                clipboard_parents.setPrimaryClip(clipData_parents);
                Toast.makeText(ShareCodeActivity.this, getString(R.string.toast_clipboard), Toast.LENGTH_SHORT).show();
            }
        });


        //Class to copy the teachers' code
        sharecode_imagebutton_copy_teachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard_teachers = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData_teachers = ClipData.newPlainText(sharecode_textview_insertcode_teachers, sharecode_textview_teachers.getText().toString());
                clipboard_teachers.setPrimaryClip(clipData_teachers);
                Toast.makeText(ShareCodeActivity.this, getString(R.string.toast_clipboard), Toast.LENGTH_SHORT).show();
            }
        });

        //Share button for parents
        sharecode_imagebutton_share_parents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codeShared_parents = sharecode_textview_parents.getText().toString();

                Intent share_button = new Intent(Intent.ACTION_SEND);
                share_button.setType("text/plain");
                String shareBody =  "*PADRES Y MAESTROS*\nEste es tu codigo para ingresar al aula como _padre_: \n"
                                    + codeShared_parents;

                String shareSub = "Codigo para ingresar a un aula";
                share_button.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                share_button.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(share_button, "Share using"));
            }
        });

        //Share button for teachers
        sharecode_imagebutton_share_teachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codeShared_teachers = sharecode_textview_teachers.getText().toString();

                Intent share_button_teachers = new Intent(Intent.ACTION_SEND);
                share_button_teachers.setType("text/plain");
                //We should change this to call a XML string, for this time it works
                String shareBodys =  "*PADRES Y MAESTROS*\nEste es tu codigo para ingresar al aula como _maestro_: \n"
                                    + codeShared_teachers;

                String shareSubs = "Codigo para ingresar a un aula";
                share_button_teachers.putExtra(Intent.EXTRA_SUBJECT, shareSubs);
                share_button_teachers.putExtra(Intent.EXTRA_TEXT, shareBodys);
                startActivity(Intent.createChooser(share_button_teachers, "Share using"));
            }
        });

        // Button to navigate to classrooms list
        Button button_to_classrooms = findViewById(R.id.button_to_classrooms);
        button_to_classrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to Classrooms List
                Intent intent = new Intent(ShareCodeActivity.this, ClassroomsListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }




    // Function (different from finish()) to navigate back to parent layout.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

}