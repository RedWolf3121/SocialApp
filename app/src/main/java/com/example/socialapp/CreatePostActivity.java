package com.example.socialapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CreatePostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        findViewById(R.id.crear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.texto);
                String posttext = editText.getText().toString();
                if (posttext.isEmpty()){
                    editText.setError("Escribe algo");
                }else{
                    Post post = new Post();
                    post.content = posttext;
                    post.author = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                    post.authorsPhoto = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();

                    DatabaseReference dbrefPost = FirebaseDatabase.getInstance().getReference().child("posts");

                    String postId =
                            dbrefPost
                                    .child("posts/data/")
                                    .child("data")
                                    .push()
                                    .getKey();

                    dbrefPost
                            .child("posts/data" + postId)
                            .setValue(post);

                    dbrefPost
                            .child("posts/user-posts")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(postId)
                            .setValue(true);

                    dbrefPost
                            .child("all-posts")
                            .child(postId)
                            .setValue(true);


                    dbrefPost
                            .child("data")
                            .push()
                            .child("content")
                            .setValue(post);
                    finish();
                }
            }
        });
    }
}
