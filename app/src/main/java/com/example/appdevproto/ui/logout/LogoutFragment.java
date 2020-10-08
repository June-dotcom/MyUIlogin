package com.example.appdevproto.ui.logout;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appdevproto.SigninActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutFragment extends Fragment {
    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getContext(), SigninActivity.class));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }
}