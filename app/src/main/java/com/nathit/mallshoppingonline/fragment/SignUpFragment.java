package com.nathit.mallshoppingonline.fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nathit.mallshoppingonline.MainActivity;
import com.nathit.mallshoppingonline.R;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    private TextView tvAlreadyAccount;
    private FrameLayout parentFrameLayout;
    private TextInputEditText email;
    private TextInputEditText fullName;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private ImageButton closeBtn;
    private Button signUpBtn;
    private CheckBox checkBox;
    private CardView sign_up_card_view_btn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public static boolean disableCloseBtn = false;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        tvAlreadyAccount = view.findViewById(R.id.tvAlreadyAccount);
        parentFrameLayout = getActivity().findViewById(R.id.FrameLayout);
        email = view.findViewById(R.id.sign_up_email);
        fullName = view.findViewById(R.id.sign_up_name);
        password = view.findViewById(R.id.sign_up_password);
        confirmPassword = view.findViewById(R.id.sign_up_confirm_password);
        closeBtn = view.findViewById(R.id.sign_up_close_btn);
        signUpBtn = view.findViewById(R.id.sign_up_btn);
        checkBox = view.findViewById(R.id.sign_up_agreement);
        sign_up_card_view_btn = view.findViewById(R.id.sign_up_card_view_btn);
        progressBar = view.findViewById(R.id.sign_up_progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

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
        tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                ///todo : send data to firebase
                String textEmail = email.getText().toString().trim();
                String textFullName = fullName.getText().toString().trim();
                String textPassword = password.getText().toString().trim();
                String textConfirmPassword = confirmPassword.getText().toString().trim();
                String checkPassword = "(?=\\S+$).{8,20}$";

                if (checkBox.isChecked()) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                        email.setError("กรุณากรอกอีเมลให้ถูกต้อง");
                        email.requestFocus();
                    } else if (TextUtils.isEmpty(textFullName)) {
                        fullName.setError("กรุณาชื่อ-นามสกุล");
                        fullName.requestFocus();
                    } else if (TextUtils.isEmpty(textPassword)) {
                        password.setError("กรุณากรอกรหัสผ่าน");
                        password.requestFocus();
                    } else if (!textPassword.matches(checkPassword)) {
                        password.setError("กรุณากรอกรหัสผ่านอย่างน้อย 8 ตัวขึ้นไป");
                        password.requestFocus();
                    } else if (TextUtils.isEmpty(textConfirmPassword)) {
                        confirmPassword.setError("กรุณากรอกรหัสผ่านอีกครั้ง");
                        confirmPassword.requestFocus();
                    } else if (!textPassword.matches(textConfirmPassword)) {
                        confirmPassword.setError("รหัสผ่านไม่ตรงกัน!");
                        confirmPassword.requestFocus();
                    } else {
                        checkEmailAndPassword(textEmail, textPassword);
                    }
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                        email.setError("กรุณากรอกอีเมลให้ถูกต้อง");
                        email.requestFocus();
                    } else if (TextUtils.isEmpty(textFullName)) {
                        fullName.setError("กรุณาชื่อ-นามสกุล");
                        fullName.requestFocus();
                    } else if (TextUtils.isEmpty(textPassword)) {
                        password.setError("กรุณากรอกรหัสผ่าน");
                        password.requestFocus();
                    } else if (!textPassword.matches(checkPassword)) {
                        password.setError("กรุณากรอกรหัสผ่านอย่างน้อย 8 ตัวขึ้นไป");
                        password.requestFocus();
                    } else if (TextUtils.isEmpty(textConfirmPassword)) {
                        confirmPassword.setError("กรุณากรอกรหัสผ่านอีกครั้ง");
                        confirmPassword.requestFocus();
                    } else if (!textPassword.matches(textConfirmPassword)) {
                        confirmPassword.setError("รหัสผ่านไม่ตรงกัน!");
                        confirmPassword.requestFocus();
                    } else {
                        Toast.makeText(getActivity(), "กรุณากดยอมรับเงื่อนไข", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void checkEmailAndPassword(String textEmail, String textPassword) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(textEmail, textPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Map<String,Object> userData = new HashMap<>();
                            userData.put("fullname", fullName.getText().toString());
                            firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                    .set(userData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Map<String,Object> listSize = new HashMap<>();
                                                listSize.put("list_size", (long) 0);
                                                firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                                        .set(listSize).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            mainIntent();
                                                        } else {
                                                            Toast.makeText(getActivity(), "ไม่สามารถสมัครสมาชิกได้ กรุณาลองใหม่ภายหลัง!", Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                        }
                                                    }
                                                });

                                                Toast.makeText(getActivity(), "สมัครสมาชิกเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            } else {
                                                Toast.makeText(getActivity(), "ไม่สามารถสมัครสมาชิกได้ กรุณาลองใหม่ภายหลัง!", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                password.setError("รหัสผ่านของคุณอ่อนแอเกินไป");
                                password.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                email.setError("อีเมลของคุณไม่ถูกต้องหรือมีการใช้งานอยู่แล้ว");
                                email.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                email.setError("ผู้ใช้ได้ลงทะเบียนกับอีเมลนี้แล้ว กรุณาลงทะเบียนด้วยอีเมลอื่น");
                                email.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, "onComplete: " + e.getMessage());
                                Toast.makeText(getActivity(), "ไม่สามารถสมัครสมาชิกได้ กรุณาลองใหม่ภายหลัง!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                });
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
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

}