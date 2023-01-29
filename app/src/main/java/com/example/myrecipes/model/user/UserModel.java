package com.example.myrecipes.model.user;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.myrecipes.model.firebase.FirebaseModel;
import com.example.myrecipes.model.localDB.AppLocalDb;
import com.example.myrecipes.model.localDB.AppLocalDbRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserModel {
    private static final UserModel _instance = new UserModel();

    private FirebaseModel firebaseModel = new FirebaseModel();

    public static UserModel instance(){
        return _instance;
    }
    private UserModel(){
    }

    public interface Listener<T>{
        void onComplete(T data);
    }

    public enum LoadingState{
        LOADING,
        NOT_LOADING
    }
    final public MutableLiveData<LoadingState> EventUsersListLoadingState = new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);

    public void registerUser(User user, Listener<Task<Void>> listener){
        firebaseModel.registerUser(user,(task)->{
            listener.onComplete(task);
        });
    }

    public void loginUser(User user, Listener<Task<AuthResult>> listener){
        firebaseModel.loginUser(user,(task)->{
            listener.onComplete(task);
        });
    }

    public boolean isUserLoggedIn(){
      return firebaseModel.isUserLoggedIn();
    }

    public User getUserProfileDetails() {
        return firebaseModel.getUserProfileDetails();
    }

    public void updateUserProfile(User user, Bitmap bitmap, Listener<Task<Void>> listener){
        firebaseModel.updateUserProfile(user, bitmap, (task)->{
            listener.onComplete(task);
        });
    }
}
