package com.example.kinomania.ui.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.kinomania.ui.activities.MainActivity;
import com.example.kinomania.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class FragmentProfile extends Fragment {

    MainActivity mainActivity;
    private FirebaseAuth mAuth;
    private Button goAuth;
    private EditText EmailEditText, PasswordEditText;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "EmailPassword";

    public FragmentProfile() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button_registration = view.findViewById(R.id.GoToRegistration);
        ImageButton button_registration_left = view.findViewById(R.id.left_pointer);
        PasswordEditText = view.findViewById(R.id.editTextPassword);
        EmailEditText = view.findViewById(R.id.editTextEmailAddress);
        goAuth = view.findViewById(R.id.Entry);

        button_registration.setOnClickListener(v-> {
            View mainContainer = mainActivity.findViewById(R.id.fragment_container_view);
            NavController navController = Navigation.findNavController(mainContainer);
            int currentDestination = navController.getCurrentDestination().getId();
            int destination = R.id.fragmentAuthorisation;
            if(currentDestination != destination){
                navController.navigate(R.id.action_fragmentProfile_to_fragmentAuthorisation);
            }
        });

        button_registration_left.setOnClickListener(v-> { // первая и вторая кнопка делают одно и то же действие
            View mainContainer = mainActivity.findViewById(R.id.fragment_container_view);
            NavController navController = Navigation.findNavController(mainContainer);
            int currentDestination = navController.getCurrentDestination().getId();
            int destination = R.id.fragmentAuthorisation;
            if(currentDestination != destination){
                navController.navigate(R.id.action_fragmentProfile_to_fragmentAuthorisation);
            }
        });

        goAuth.setOnClickListener(v -> {
            signIn(EmailEditText.getText().toString(), PasswordEditText.getText().toString());
        });
    }

    public void signIn(String email, String password){
        if (!isNetworkAvailable()) {
            Toast.makeText(getContext(), "Отсутствует подключение к интернету!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Авторизация успешна!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Авторизация провалена!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}