package com.example.myrecipes.model.firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myrecipes.RegisterActivity;
import com.example.myrecipes.model.recipe.Recipe;
import com.example.myrecipes.model.recipe.RecipeModel;
import com.example.myrecipes.model.user.User;
import com.example.myrecipes.model.user.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class FirebaseModel {
    FirebaseFirestore db;
    FirebaseStorage storage;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    public FirebaseModel(){
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
        storage = FirebaseStorage.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
    }

    public void registerUser(User user, UserModel.Listener<Task<AuthResult>> listener) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                listener.onComplete(task);
            }
        });
    }

    public void loginUser(User user, UserModel.Listener<Task<AuthResult>> listener) {
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                listener.onComplete(task);
            }
        });
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void getAllRecipesSince(Long since, RecipeModel.Listener<List<Recipe>> callback){
        db.collection(Recipe.COLLECTION)
                .whereGreaterThanOrEqualTo(Recipe.LAST_UPDATED, new Timestamp(since,0))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Recipe> list = new LinkedList<>();
                        if (task.isSuccessful()){
                            QuerySnapshot jsonsList = task.getResult();
                            for (DocumentSnapshot json: jsonsList){
                                Recipe recipe = Recipe.fromJson(json.getData());
                                list.add(recipe);
                            }
                        }
                        callback.onComplete(list);
                    }
                });
    }
}
