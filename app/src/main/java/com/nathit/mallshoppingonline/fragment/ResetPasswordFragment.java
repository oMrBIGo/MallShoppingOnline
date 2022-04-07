package com.nathit.mallshoppingonline.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.nathit.mallshoppingonline.R;


public class ResetPasswordFragment extends Fragment {

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    private TextInputEditText registeredEmail;
    private Button resetPasswordBtn;
    private TextView goBack;
    private CardView btn_cardView_reset_password;
    private FrameLayout parentFrameLayout;
    private LinearLayout emailIconLinear;
    private ImageView emailIcon;
    private TextView emailIconText;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        registeredEmail = view.findViewById(R.id.forgot_password_email);
        resetPasswordBtn = view.findViewById(R.id.reset_password_btn);
        goBack = view.findViewById(R.id.tv_forgot_password_go_back);
        emailIconLinear = view.findViewById(R.id.for_got_linear);
        emailIcon = view.findViewById(R.id.forgot_password_email_icon);
        emailIconText = view.findViewById(R.id.forgot_password_email_textView);
        progressBar = view.findViewById(R.id.forgot_password_progressbar);
        parentFrameLayout = getActivity().findViewById(R.id.FrameLayout);
        firebaseAuth = FirebaseAuth.getInstance();
        btn_cardView_reset_password = view.findViewById(R.id.btn_cardview_reset_password);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registeredEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(emailIconLinear);
                progressBar.setVisibility(View.VISIBLE);
                emailIconText.setVisibility(View.GONE);
                resetPasswordBtn.setEnabled(false);
                btn_cardView_reset_password.setCardBackgroundColor(Color.rgb(230, 230, 230));
                firebaseAuth.sendPasswordResetEmail(registeredEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    TransitionManager.beginDelayedTransition(emailIconLinear);
                                    emailIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_email_read));
                                    emailIconText.setVisibility(View.VISIBLE);
                                    emailIconText.setText("ส่งอีเมลเรียบร้อยแล้ว! ตรวจสอบกล่องจดหมายของคุณ");
                                    emailIconText.setTextColor(getResources().getColor(R.color.green));
                                    emailIcon.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    TransitionManager.beginDelayedTransition(emailIconLinear);
                                    emailIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_email));
                                    emailIconText.setVisibility(View.VISIBLE);
                                    emailIconText.setText("ไม่สามารถรีเซ็ตรหัสผ่านได้! โปรดตรวจสอบอีเมลของคุณ");
                                    emailIconText.setTextColor(getResources().getColor(R.color.red));
                                    emailIcon.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                                resetPasswordBtn.setEnabled(true);
                                btn_cardView_reset_password.setCardBackgroundColor(Color.rgb(99, 156, 255));
                            }
                        });
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });
    }

    private void checkInputs() {
        if (TextUtils.isEmpty(registeredEmail.getText())) {
            resetPasswordBtn.setEnabled(false);
            btn_cardView_reset_password.setCardBackgroundColor(Color.rgb(230, 230, 230));
        } else {
            resetPasswordBtn.setEnabled(true);
            btn_cardView_reset_password.setCardBackgroundColor(Color.rgb(99, 156, 255));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

}