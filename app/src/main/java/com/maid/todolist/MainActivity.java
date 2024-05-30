package com.maid.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageButton imageButton;
    ArrayList<Note>notes;
    RecyclerView recyclerView;
    NoteAdaptor noteAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater=(LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput =inflater.inflate(R.layout.note_input,null,false);

                final EditText editTitle=viewInput.findViewById(R.id.edit_title);
                final EditText editDescription=viewInput.findViewById(R.id.edit_description);

                new AlertDialog.Builder(MainActivity.this)
                        .setView(viewInput)
                        .setTitle("Add note")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String title=editTitle.getText().toString();
                                String description=editDescription.getText().toString();
                                Note note=new Note(title,description);

                                boolean isInserted=new NoteHandler(MainActivity.this).create(note);

                                if(isInserted){
                                    Toast.makeText(MainActivity.this,"Note saved",Toast.LENGTH_SHORT).show();
                                    loadNotes();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Unable to save the note", Toast.LENGTH_SHORT).show();
                                }
                                dialogInterface.cancel();

                            }
                        }).show();

            }
        });
        recyclerView=findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        ItemTouchHelper.SimpleCallback itemTouchCallback= new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new NoteHandler(MainActivity.this).delete(notes.get(viewHolder.getAdapterPosition()).getId());
                notes.remove(viewHolder.getAdapterPosition());
                noteAdaptor.notifyItemRemoved(viewHolder.getAdapterPosition());

            }
        };

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        loadNotes();

    }

    public ArrayList<Note> readNotes(){
        ArrayList<Note> notes=new NoteHandler(this).readNote();
        return notes;
    }

    public void loadNotes(){
        notes=readNotes();
        noteAdaptor=new NoteAdaptor(notes, this, new NoteAdaptor.ItemClicked() {
            @Override
            public void onClick(int position, View view) {
                editNote(notes.get(position).getId(), view);
            }
        });
        recyclerView.setAdapter(noteAdaptor);
    }
    private void editNote(int noteId, View view)
    {
        NoteHandler noteHandler=new NoteHandler(this);
        Note note=noteHandler.readSingleNote(noteId);
        Intent intent = new Intent(this, EditNote.class);
        intent.putExtra("title",note.getTitle().toString());
        intent.putExtra("description",note.getDescription().toString());
        intent.putExtra("id",note.getId());

        ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(this,view, ViewCompat.getTransitionName(view));
        startActivityForResult(intent,1,optionsCompat.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1)
            loadNotes();
    }
}