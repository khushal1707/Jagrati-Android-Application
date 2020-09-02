package com.example.jagratiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jagratiapp.model.Classes;
import com.example.jagratiapp.model.Groups;
import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.ui.ClassDiffUtil;
import com.example.jagratiapp.ui.ClassRecyclerAdapter;
import com.example.jagratiapp.ui.GroupDiffUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Classes_page extends AppCompatActivity {

    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText classname;
    private Button saveButton;

    private List<Classes> classesList;
    private RecyclerView recyclerView;
    private ClassRecyclerAdapter classRecyclerAdapter;



    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //TODO: Path add karna hai.
    private CollectionReference collectionReference = db.collection("Classes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes_page);

        fab = findViewById(R.id.fab_class_page);

        classesList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview_classes_page);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopup();
            }
        });


        //yaha hi add kiya hai onstart wala
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot classDocumentSnapshot : queryDocumentSnapshots) {
                        Classes classes = classDocumentSnapshot.toObject(Classes.class);
                        classes.setuId(classDocumentSnapshot.getId().toString());
                        classesList.add(classes);
                    }
                    classRecyclerAdapter = new ClassRecyclerAdapter(Classes_page.this, classesList);
                    recyclerView.setAdapter(classRecyclerAdapter);
                    //d if(recyclerView == null)
                    //classRecyclerAdapter.notifyDataSetChanged();



                } else {
                    Toast.makeText(Classes_page.this, "It's noting there", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        //yaha se khatam hai onStart wala
    }

    private void createPopup() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_classes,null);

        classname = view.findViewById(R.id.class_name_pop);
        saveButton = view.findViewById(R.id.saveClass_pop);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!classname.getText().toString().isEmpty()){
                    saveclass(view);
                }else
                {
                    Snackbar.make(view,"Empty Not Allowed",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveclass(View view) {
        final Classes classes = new Classes();
        classes.setClassName(classname.getText().toString().trim());
        collectionReference.add(classes).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                // to add delay
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<Classes> newClassList = classesList;
                        classes.setuId(documentReference.getId());
                        newClassList.add(classes);

                        ClassDiffUtil diffUtil = new ClassDiffUtil(classesList,newClassList);
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtil);
                        diffResult.dispatchUpdatesTo(classRecyclerAdapter);
                  dialog.dismiss();

                    }
                },800);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Classes_page.this,e.getMessage().toString().trim(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}