package com.nathit.mallshoppingonline;

import static com.nathit.mallshoppingonline.RegisterActivity.setSignUpFragment;
import static com.nathit.mallshoppingonline.db.DBQueries.currentUser;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nathit.mallshoppingonline.fragment.HomeFragment;
import com.nathit.mallshoppingonline.fragment.MyAccountFragment;
import com.nathit.mallshoppingonline.fragment.MyCartFragment;
import com.nathit.mallshoppingonline.fragment.MyOrdersFragment;
import com.nathit.mallshoppingonline.fragment.MyRewardskFragment;
import com.nathit.mallshoppingonline.fragment.MyWishlistFragment;
import com.nathit.mallshoppingonline.fragment.SignInFragment;
import com.nathit.mallshoppingonline.fragment.SignUpFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;
    private static final int ORDERS_FRAGMENT = 2;
    private static final int WISHLIST_FRAGMENT = 3;
    private static final int REWARD_FRAGMENT = 4;
    private static final int ACCOUNT_FRAGMENT = 5;
    public static Boolean showCart = false;

    private int currentFragment = -1;
    private FirebaseAuth firebaseAuth;
    private FrameLayout frameLayout;
    private ImageView actionBarLogo;
    private TextView tv_cart;
    private NavigationView navigationView;
    public static DrawerLayout drawerLayout;
    private long backPressedTime;

    private Window window;
    private Toolbar toolbar;
    private Dialog signInDialog;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        actionBarLogo = findViewById(R.id.actionbar_logo);
        setSupportActionBar(toolbar);


        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_cart = findViewById(R.id.tv_cart);
        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        frameLayout = findViewById(R.id.main_framelayout);

        if (showCart) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            myCart();
        } else {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            setFrameLayout(new HomeFragment(), HOME_FRAGMENT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        } else {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentFragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_search_icon) {
            return true;
        } else if (id == R.id.main_notification_icon) {
            return true;
        } else if (id == R.id.main_cart_icon) {
            if (currentUser == null) {
                signInDialog.show();
            } else {
                myCart();
            }

            return true;
        } else if (id == android.R.id.home) {
            if (showCart) {
                showCart = false;
                finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void myMall() {
        tv_cart.setVisibility(View.GONE);
        actionBarLogo.setVisibility(View.VISIBLE);
        invalidateOptionsMenu();
        setFrameLayout(new HomeFragment(), HOME_FRAGMENT);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }


    private void myOrder() {
        tv_cart.setVisibility(View.VISIBLE);
        tv_cart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_my_order, 0);
        actionBarLogo.setVisibility(View.GONE);
        invalidateOptionsMenu();
        setFrameLayout(new MyOrdersFragment(), ORDERS_FRAGMENT);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_cart.setText("คำสั่งซื้อของฉัน");
    }

    private void myRewards() {
        tv_cart.setVisibility(View.VISIBLE);
        tv_cart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_rewards, 0);
        actionBarLogo.setVisibility(View.GONE);
        invalidateOptionsMenu();
        setFrameLayout(new MyRewardskFragment(), REWARD_FRAGMENT);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_cart.setText("คูปองส่วนลดทั้งหมด");
    }

    private void myCart() {
        tv_cart.setVisibility(View.VISIBLE);
        tv_cart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cart_white, 0);
        actionBarLogo.setVisibility(View.GONE);
        invalidateOptionsMenu();
        setFrameLayout(new MyCartFragment(), CART_FRAGMENT);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_cart.setText("รถเข็น");
    }

    private void myWishList() {
        tv_cart.setVisibility(View.VISIBLE);
        tv_cart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_wishlist, 0);
        actionBarLogo.setVisibility(View.GONE);
        invalidateOptionsMenu();
        setFrameLayout(new MyWishlistFragment(), WISHLIST_FRAGMENT);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_cart.setText("สิ่งที่อยากได้");
    }

    private void myAccount() {
        tv_cart.setVisibility(View.VISIBLE);
        tv_cart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_account, 0);
        actionBarLogo.setVisibility(View.GONE);
        invalidateOptionsMenu();
        setFrameLayout(new MyAccountFragment(), ACCOUNT_FRAGMENT);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_cart.setText("บัญชีของฉัน");
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (currentUser != null) {
            switch (item.getItemId()) {
                case R.id.nav_my_mall:
                    myMall();
                    break;
                case R.id.nav_my_orders:
                    myOrder();
                    break;
                case R.id.nav_my_rewards:
                    myRewards();
                    break;
                case R.id.nav_my_cart:
                    myCart();
                    break;
                case R.id.nav_my_wishlist:
                    myWishList();
                    break;
                case R.id.nav_my_account:
                    myAccount();
                    break;
                case R.id.nav_logout:
                    Toast.makeText(this, "ออกจากระบบเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                    finish();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else {
            drawerLayout.closeDrawer(GravityCompat.START);
            signInDialog.show();
            return false;
        }
    }

    private void setFrameLayout(Fragment fragment, int fragmentNo) {
        if (fragmentNo != currentFragment) {
            if (fragmentNo == REWARD_FRAGMENT) {
                window.setStatusBarColor(getResources().getColor(R.color.purple_200));
                toolbar.setBackgroundColor(getResources().getColor(R.color.purple_200));
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.purple_200));
                toolbar.setBackgroundColor(getResources().getColor(R.color.purple_200));
            }
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }

        signInDialog = new Dialog(MainActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableCloseBtn = true;
                SignInFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });
        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableCloseBtn = true;
                SignInFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
    }

    // onBackPressed
    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment == HOME_FRAGMENT) {
                currentFragment = -1;
            } else {
                if (showCart) {
                    showCart = false;
                    finish();
                } else {
                    myMall();
                    navigationView.getMenu().getItem(0).setChecked(true);
                    if (backPressedTime + 2000 > System.currentTimeMillis()) {
                        moveTaskToBack(true);
                        finish();
                    } else {
                        Toast.makeText(this, "กดอีกครั้งเพื่อออก", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }

        backPressedTime = System.currentTimeMillis();
    }

}