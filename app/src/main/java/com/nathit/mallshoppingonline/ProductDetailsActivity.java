package com.nathit.mallshoppingonline;

import static com.nathit.mallshoppingonline.MainActivity.showCart;
import static com.nathit.mallshoppingonline.RegisterActivity.setSignUpFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nathit.mallshoppingonline.adapter.MyRewardsAdapter;
import com.nathit.mallshoppingonline.adapter.ProductDetailsAdapter;
import com.nathit.mallshoppingonline.adapter.ProductImagesAdapter;
import com.nathit.mallshoppingonline.db.DBQueries;
import com.nathit.mallshoppingonline.fragment.SignInFragment;
import com.nathit.mallshoppingonline.fragment.SignUpFragment;
import com.nathit.mallshoppingonline.model.ProductSpecificationModel;
import com.nathit.mallshoppingonline.model.RewardModel;
import com.nathit.mallshoppingonline.model.WishlistModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {

    public static boolean running_wishlist_query = false;
    public static boolean running_rating_query = false;

    private ViewPager productImagesViewPager;
    private TabLayout viewpagerIndicator;
    private TextView productTitle;
    private TextView averageRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private TextView cuttedPrice;
    private Button codIndicator;
    private TextView tvCodIndicator;
    private TextView rewardTitle;
    private TextView rewardBody;

    private LinearLayout couponRedemptionLayout;
    private Button couponRedeemBtn;

    //////// Product description
    private LinearLayout productDetailsOnlyContainer;
    private LinearLayout productDetailsTabsContainer;
    private ViewPager productDetailsViewpager;
    private TabLayout productDetailsTabLayout;
    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();

    private TextView productOnlyDescriptionBody;
    private String productDescription;
    private String productOtherDetails;
    //////// Product description

    //////// rating layout
    public static int initialRating;
    private TextView totalRatings;
    public static LinearLayout rateNowLayout;
    private LinearLayout ratingsNoContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private TextView averageRatings;
    //////// rating layout

    private Button buyNowBtn;
    private LinearLayout addToCartBtn;

    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static FloatingActionButton addToWishListBtn;

    private FirebaseFirestore firebaseFirestore;

    //////// couponDialog
    public static TextView couponTitle;
    public static TextView couponExpiryDate;
    public static TextView couponBody;
    private static RecyclerView couponsRecyclerView;
    private static LinearLayout selectedCoupon;
    //////// couponDialog

    private Dialog signInDialog;
    private Dialog loadingDialog;
    private FirebaseUser currentUser;
    public static String productID;

    private DocumentSnapshot documentSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager = findViewById(R.id.product_images_viewpager);
        viewpagerIndicator = findViewById(R.id.viewpager_indicator);
        addToWishListBtn = findViewById(R.id.add_to_wishlist_btn);
        productDetailsViewpager = findViewById(R.id.product_details_viewpager);
        productDetailsTabLayout = findViewById(R.id.product_details_tabLayout);
        buyNowBtn = findViewById(R.id.buy_now_btn);
        couponRedeemBtn = findViewById(R.id.coupon_redemption_btn);
        productTitle = findViewById(R.id.product_title);
        averageRatingMiniView = findViewById(R.id.tv_product_rating_miniView);
        totalRatingMiniView = findViewById(R.id.total_ratings_miniView);
        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        tvCodIndicator = findViewById(R.id.tv_cod_indicator);
        codIndicator = findViewById(R.id.cod_indicator_btn);
        rewardTitle = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.reward_body);
        productDetailsTabsContainer = findViewById(R.id.products_details_tabs_container);
        productDetailsOnlyContainer = findViewById(R.id.products_details_container);
        productOnlyDescriptionBody = findViewById(R.id.product_details_body);
        totalRatings = findViewById(R.id.total_ratings);
        ratingsNoContainer = findViewById(R.id.ratings_number_container);
        ratingsProgressBarContainer = findViewById(R.id.ratings_progressbar_container);
        averageRatings = findViewById(R.id.average_rating);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        couponRedemptionLayout = findViewById(R.id.coupon_redemption_layout);

        initialRating = -1;

        /////// loading dialog
        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        /////// loading dialog

        firebaseFirestore = FirebaseFirestore.getInstance();

        final List<String> productImages = new ArrayList<>();
        productID = getIntent().getStringExtra("PRODUCT_ID");
        firebaseFirestore.collection("PRODUCTS").document("GSZxbgQsS71SXY3jrvGr").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();

                    for (int x = 1; x <= (long) documentSnapshot.get("no_of_product_images"); x++) {
                        productImages.add(documentSnapshot.get("product_image_" + x).toString());
                    }
                    ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                    productImagesViewPager.setAdapter(productImagesAdapter);

                    productTitle.setText(documentSnapshot.get("product_title").toString());

                    averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                    totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings") + ") คะแนน");
                    productPrice.setText(documentSnapshot.get("product_price").toString() + " บาท");

                    cuttedPrice.setText(documentSnapshot.get("cutted_price").toString() + " บาท");
                    if ((boolean) documentSnapshot.get("COD")) {
                        codIndicator.setVisibility(View.VISIBLE);
                        tvCodIndicator.setVisibility(View.VISIBLE);
                    } else {
                        codIndicator.setVisibility(View.INVISIBLE);
                        tvCodIndicator.setVisibility(View.INVISIBLE);
                    }
                    rewardTitle.setText((long) documentSnapshot.get("free_coupons") + documentSnapshot.get("free_coupon_title").toString());
                    rewardBody.setText(documentSnapshot.get("free_coupon_body").toString());

                    if ((boolean) documentSnapshot.get("use_tab_layout")) {
                        productDetailsTabsContainer.setVisibility(View.VISIBLE);
                        productDetailsOnlyContainer.setVisibility(View.GONE);
                        productDescription = documentSnapshot.get("product_description").toString();

                        productOtherDetails = documentSnapshot.get("product_other_details").toString();

                        for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {
                            productSpecificationModelList.add(new ProductSpecificationModel(0,
                                    documentSnapshot.get("spec_title_" + x).toString()));
                            for (long y = 1; y < (long) documentSnapshot.get("spec_title_" + x + "_total_fields") + 1; y++) {
                                productSpecificationModelList.add(new ProductSpecificationModel(1,
                                        documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(),
                                        documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()));
                            }

                        }
                    } else {

                        productDetailsTabsContainer.setVisibility(View.GONE);
                        productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                        productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                    }

                    totalRatings.setText((long) documentSnapshot.get("total_ratings") + " คะแนน");
                    for (int x = 0; x < 5; x++) {
                        TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                        rating.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));

                        ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                        int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
                        progressBar.setMax(maxProgress);
                        progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));

                    }
                    averageRatings.setText(documentSnapshot.get("average_rating").toString());
                    productDetailsViewpager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), productDetailsTabLayout.getTabCount(), productDescription, productOtherDetails, productSpecificationModelList));

                    if (currentUser != null) {
                        if (DBQueries.wishList.size() == 0) {
                            DBQueries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
                        } else {
                            loadingDialog.dismiss();
                        }
                    } else {
                        loadingDialog.dismiss();
                    }

                    if (DBQueries.wishList.contains(productID)) {
                        ALREADY_ADDED_TO_WISHLIST = true;
                        addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                    } else {
                        addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                        ALREADY_ADDED_TO_WISHLIST = false;
                    }

                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(ProductDetailsActivity.this, "อินเตอร์เน็ตมีปัญหา ไม่สามารถแสดงข้อมูลได้ กรุณาเช็คอินเตอร์เน็ตของท่าน",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewpagerIndicator.setupWithViewPager(productImagesViewPager, true);

        addToWishListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    addToWishListBtn.setEnabled(false);

                    if (ALREADY_ADDED_TO_WISHLIST) {
                        int index = DBQueries.wishList.indexOf(productID);
                        DBQueries.removeFromWishlist(index, ProductDetailsActivity.this);
                        ALREADY_ADDED_TO_WISHLIST = false;
                        addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                    } else {
                        addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.purple_200));
                        Map<String, Object> addProduct = new HashMap<>();
                        addProduct.put("product_ID_" + String.valueOf(DBQueries.wishList.size()), productID);

                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                .set(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Map<String, Object> updateListSize = new HashMap<>();
                                    updateListSize.put("list_size", (long) (DBQueries.wishList.size() + 1));

                                    firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                            .update(updateListSize).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                if (DBQueries.wishlistModelList.size() != 0) {
                                                    DBQueries.wishlistModelList.add(new WishlistModel(documentSnapshot.get("product_image_1").toString()
                                                            , documentSnapshot.get("product_full_title").toString()
                                                            , (long) documentSnapshot.get("free_coupons")
                                                            , documentSnapshot.get("average_rating").toString()
                                                            , (long) documentSnapshot.get("total_ratings")
                                                            , documentSnapshot.get("product_price").toString()
                                                            , documentSnapshot.get("cutted_price").toString()
                                                            , (boolean) documentSnapshot.get("COD")));
                                                }

                                                ALREADY_ADDED_TO_WISHLIST = true;
                                                addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                                                DBQueries.wishList.add(productID);
                                                Toast.makeText(ProductDetailsActivity.this, "เพิ่มสินค้าที่อยากได้เรียบร้อยแล้ว!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ProductDetailsActivity.this, "อินเตอร์เน็ตมีปัญหา ไม่สามารถแสดงข้อมูลได้ กรุณาเช็คอินเตอร์เน็ตของท่าน", Toast.LENGTH_SHORT).show();
                                            }
                                            addToWishListBtn.setEnabled(true);
                                        }
                                    });

                                } else {
                                    addToWishListBtn.setEnabled(true);
                                    Toast.makeText(ProductDetailsActivity.this, "อินเตอร์เน็ตมีปัญหา ไม่สามารถแสดงข้อมูลได้ กรุณาเช็คอินเตอร์เน็ตของท่าน", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        ALREADY_ADDED_TO_WISHLIST = true;
                        addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                    }
                }
            }
        });

        productDetailsViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTabLayout));
        productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //////// rating layout
        rateNowLayout = findViewById(R.id.rate_now_linear);

        for (int x = 0; x < rateNowLayout.getChildCount(); x++) {
            final int startPosition = x;
            rateNowLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {
                        setRating(startPosition);
                    }
                }
            });
        }
        //////// rating layout

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                    startActivity(deliveryIntent);
                }
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    //////todo:add to cart

                }
            }
        });

        ////// coupon dialog
        final Dialog checkCouponPriceDialog = new Dialog(ProductDetailsActivity.this);
        checkCouponPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
        checkCouponPriceDialog.setCancelable(true);
        checkCouponPriceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkCouponPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toggleRecyclerView = checkCouponPriceDialog.findViewById(R.id.toggle_recyclerview);
        couponsRecyclerView = checkCouponPriceDialog.findViewById(R.id.coupon_recyclerView);
        selectedCoupon = checkCouponPriceDialog.findViewById(R.id.selected_coupon);

        couponTitle = checkCouponPriceDialog.findViewById(R.id.coupen_title);
        couponExpiryDate = checkCouponPriceDialog.findViewById(R.id.coupen_validity);
        couponBody = checkCouponPriceDialog.findViewById(R.id.coupen_body);

        TextView originalPrice = checkCouponPriceDialog.findViewById(R.id.original_price);
        TextView discountedPrice = checkCouponPriceDialog.findViewById(R.id.discounted_price);

        couponsRecyclerView.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        couponsRecyclerView.setLayoutManager(linearLayoutManager);

        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 35% เฉพาะลูกค้าใหม่", "31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 35% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 15%", "31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 15% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 45%", "31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 45% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 65%", "31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 65% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 90% เฉพาะลูกค้าใหม่", "31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 90% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 80% เฉพาะลูกค้าใหม่", "31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 80% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 25%", "31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 25% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 10% เฉพาะลูกค้าใหม่", "31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 10% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 95%", "31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 95% ขั้นต่ำ 5,000 บาท สูงสุด"));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList, true);
        couponsRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecyclerView();
            }
        });

        couponRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCouponPriceDialog.show();
            }
        });

        ////// sign dialog
        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);

        Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });
        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
        ////// sign dialog


        ////// coupon dialog
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            couponRedemptionLayout.setVisibility(View.GONE);
        } else {
            couponRedemptionLayout.setVisibility(View.VISIBLE);
        }

        if (currentUser != null) {
            if (DBQueries.wishList.size() == 0) {
                DBQueries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
            } else {
                loadingDialog.dismiss();
            }
        } else {
            loadingDialog.dismiss();
        }

        if (DBQueries.wishList.contains(productID)) {
            ALREADY_ADDED_TO_WISHLIST = true;
            addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
        } else {
            ALREADY_ADDED_TO_WISHLIST = false;
        }
    }

    public static void showDialogRecyclerView() {
        if (couponsRecyclerView.getVisibility() == View.GONE) {
            couponsRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupon.setVisibility(View.GONE);
        } else {
            couponsRecyclerView.setVisibility(View.GONE);
            selectedCoupon.setVisibility(View.VISIBLE);
        }
    }

    private void setRating(int starPosition) {
        for (int x = 0; x < rateNowLayout.getChildCount(); x++) {
            ImageView starBtn = (ImageView) rateNowLayout.getChildAt(x);
            starBtn.setColorFilter(Color.parseColor("#C4C4C4"), PorterDuff.Mode.SRC_IN);
            if (x <= starPosition) {
                starBtn.setColorFilter(Color.parseColor("#FFEB3B"), PorterDuff.Mode.SRC_IN);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.main_search_icon) {
            return true;
        } else if (id == R.id.main_cart_icon) {
            if (currentUser == null) {
                signInDialog.show();
            } else {
                Intent CartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                showCart = true;
                startActivity(CartIntent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}