package com.deloitte.clothes_shop.ui.home;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.deloitte.clothes_shop.R;
import com.deloitte.clothes_shop.base.BaseActivity;
import com.deloitte.clothes_shop.constants.AppConstants;
import com.deloitte.clothes_shop.databinding.ActivityMensWomenBinding;

public class HomeActivity extends BaseActivity implements CategoryFragment.ICategoryHost, ProductListFragment.ProductListHost, View.OnClickListener {

    private ActivityMensWomenBinding mBinding;
    private long back_pressed;
    private String headerText = "Category";

    @Override
    protected int getResourceId() {
        return R.layout.activity_mens_women;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_mens_women);
        initView();
        addInitialFragment();
    }

    private void initView() {
        mBinding.includeHeader.ivBack.setOnClickListener(this);
        mBinding.includeHeader.ivCart.setOnClickListener(this);
        mBinding.includeHeader.ivWishlist.setOnClickListener(this);
    }

    private void addInitialFragment() {
        mBinding.includeHeader.ivBack.setVisibility(View.GONE);
        addFragmentWithBackstack(R.id.fl_container, new CategoryFragment(), CategoryFragment.class.getSimpleName());
    }

    @Override
    public void openProductList(String categoryId) {
        mBinding.includeHeader.ivBack.setVisibility(View.VISIBLE);
        Bundle bundle = new Bundle();
        changeHeaderTitle(categoryId);
        bundle.putString(AppConstants.BUNDLE_DATA, categoryId);
        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(bundle);
        addFragmentWithBackstack(R.id.fl_container, fragment, ProductListFragment.class.getSimpleName());
    }

    @Override
    public void openProductDetails(String productId, String productName) {
        mBinding.includeHeader.ivBack.setVisibility(View.VISIBLE);
        changeHeaderTitle(productName);
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.BUNDLE_DATA, productId);
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);
        addFragmentWithBackstack(R.id.fl_container, fragment, ProductDetailFragment.class.getSimpleName());

    }

    private void changeHeaderTitle(String productName) {
        mBinding.includeHeader.tvTitle.setText(productName);
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() != null && ((getCurrentFragment() instanceof ProductListFragment) || getCurrentFragment() instanceof ProductDetailFragment)) {
            popFragment();
        } else {
            if (back_pressed + 1000 > System.currentTimeMillis()) {
                finish();
            } else {
                showToastShort(getString(R.string.exit_msg));
            }
            back_pressed = System.currentTimeMillis();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if ((getCurrentFragment() instanceof ProductListFragment)) {
                    mBinding.includeHeader.ivBack.setVisibility(View.GONE);
                    changeHeaderTitle(getResources().getString(R.string.category));
                }
                onBackPressed();
                break;
            case R.id.iv_cart:
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
                break;
            case R.id.iv_wishlist:
                startActivity(new Intent(HomeActivity.this, WishListActivity.class));
                break;
        }
    }
}
