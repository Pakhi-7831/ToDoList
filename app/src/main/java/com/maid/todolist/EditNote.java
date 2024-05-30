package com.maid.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditNote extends AppCompatActivity {

    EditText edtTitle, edtDescription;

    Button btncancel, btnSave;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note2);

        Intent intent=getIntent();

        edtDescription=findViewById(R.id.edt_edit_description);
        edtTitle=findViewById(R.id.edt_edit_text);

        edtDescription.setText(intent.getStringExtra("description"));
        edtTitle.setText(intent.getStringExtra("title"));
        linearLayout=findViewById(R.id.btn_holder);


        btncancel=findViewById(R.id.btncancel);
        btnSave=findViewById(R.id.btnsave);

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                btnSave.setVisibility(View.GONE);
                btncancel.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(linearLayout);

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note=new Note(edtTitle.getText().toString(),edtDescription.getText().toString());
                note.setId(intent.getIntExtra("id",1));
                if(new NoteHandler(EditNote.this).update(note))
                    Toast.makeText(EditNote.this,"Note updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(EditNote.this,"failed updating", Toast.LENGTH_SHORT).show();
                btnSave.setVisibility(View.GONE);
                btncancel.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(linearLayout);
                onBackPressed();
            }
        });

    }
}