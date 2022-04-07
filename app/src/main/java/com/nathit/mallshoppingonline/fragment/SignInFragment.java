package com.nathit.mallshoppingonline.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nathit.mallshoppingonline.MainActivity;
import com.nathit.mallshoppingonline.R;

public class SignInFragment extends Fragment {

    private TextView tvNotAccount;
    private TextView forgotPassword;
    private FrameLayout parentFrameLayout;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private ImageButton closeBtn;
    private Button signInBtn;
    private FirebaseAuth firebaseAuth;
    private static final String SAVE_EMAIL = "EmailSave";
    private ProgressBar progressBar;
    private CheckBox checkBox;
    public static boolean onResetPasswordFragment = false;

    public static boolean disableCloseBtn = false;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        tvNotAccount = view.findViewById(R.id.tvNotAccount);
        parentFrameLayout = getActivity().findViewById(R.id.FrameLayout);
        tvNotAccount = view.findViewById(R.id.tvNotAccount);
        etEmail = view.findViewById(R.id.sign_in_email);
        etPassword = view.findViewById(R.id.sign_in_password);
        closeBtn = view.findViewById(R.id.sign_in_close_btn);
        signInBtn = view.findViewById(R.id.sign_in_login_btn);
        checkBox = view.findViewById(R.id.sign_in_remember);
        progressBar = view.findViewById(R.id.sign_in_progressBar);
        forgotPassword = view.findViewById(R.id.sign_in_forgot_password);
        firebaseAuth = FirebaseAuth.getInstance();

        if (disableCloseBtn) {
            closeBtn.setVisibility(View.GONE);
        } else {
            closeBtn.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNotAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetPasswordFragment = true;
                setFragment(new ResetPasswordFragment());
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SAVE_EMAIL, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        String textEmail = sharedPreferences.getString("textEmail", "");
        if (sharedPreferences.contains("checked") && sharedPreferences.getBoolean("checked", false) == true) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        etEmail.setText(textEmail);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///todo : data to firebase
                String textEmail = etEmail.getText().toString().trim();
                String textPassword = etPassword.getText().toString().trim();
                String checkPassword = "(?=\\S+$).{8,20}$";

                if (checkBox.isChecked()) {
                    editor.putBoolean("checked", true);
                    editor.apply();
                    StoreDataUsingSharedPref(textEmail, textPassword);

                    if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                        etEmail.setError("กรุณากรอกอีเมลให้ถูกต้อง");
                        etEmail.requestFocus();
                    } else if (TextUtils.isEmpty(textPassword)) {
                        etPassword.setError("กรุณากรอกรหัสผ่าน");
                        etPassword.requestFocus();
                    } else if (!textPassword.matches(checkPassword)) {
                        etPassword.setError("กรุณากรอกรหัสผ่านอย่างน้อย 8 ตัวขึ้นไป");
                        etPassword.requestFocus();
                    } else {
                        checkEmailAndPassword(textEmail, textPassword);
                    }
                } else {
                    editor.putBoolean("checked", false);
                    editor.apply();
                    if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                        etEmail.setError("กรุณากรอกอีเมลให้ถูกต้อง");
                        etEmail.requestFocus();
                    } else if (TextUtils.isEmpty(textPassword)) {
                        etPassword.setError("กรุณากรอกรหัสผ่าน");
                        etPassword.requestFocus();
                    } else if (!textPassword.matches(checkPassword)) {
                        etPassword.setError("กรุณากรอกรหัสผ่านอย่างน้อย 8 ตัวขึ้นไป");
                        etPassword.requestFocus();
                    } else {
                        getActivity().getSharedPreferences(SAVE_EMAIL, MODE_PRIVATE).edit().clear().commit();
                        checkEmailAndPassword(textEmail, textPassword);
                    }
                }
            }
        });


    }

    private void checkEmailAndPassword(String textEmail, String textPassword) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(textEmail,textPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mainIntent();
                            Toast.makeText(getActivity(), "เข้าสู่ระบบแล้ว", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getActivity(), "เข้าสู่ระบบล้มเหลว!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void StoreDataUsingSharedPref(String textEmail, String textPassword) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(SAVE_EMAIL, MODE_PRIVATE).edit();
        editor.putString("textEmail", textEmail);
        editor.apply();
    }

    private void mainIntent() {
        if (disableCloseBtn) {
            disableCloseBtn = false;
        } else {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
        getActivity().finish();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}