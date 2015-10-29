/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aaa.activity.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;

import com.aaa.activity.admin.EditDetailActivity;
import com.aaa.activity.download.DownloadActivity;
import com.aaa.activity.me.MeActivity;
import com.aaa.db.AppDownloadState;
import com.aaa.util.AppType;
import com.aaa.util.Constant;
import com.aaa.util.MyTools;
import com.aaa.util.Role;
import com.changhong.util.db.bean.CacheManager;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.llw.AppStore.R;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class MainActivity extends AppCompatActivity {

    protected static final float MAX_TEXT_SCALE_DELTA = 0.25f;

    private ViewPager mPager;
    private NavigationAdapter mPagerAdapter;
    private SlidingTabLayout mSlidingTabLayout;
    private int mFlexibleSpaceHeight;
    private int mTabHeight;
    private int titleHeight;
    
    private  int flexibleSpaceImageHeight;
    private  int tabHeight;
    private  int titleInitialWidth;
    private  View imageView;
    private View overlayView;
    private EditText titleView;
    private ImageView rightImg;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexiblespacewithimagewithviewpagertab);
        initView();
    }

    @Override
    protected void onStart() {
    	super.onStart();
    	try {
    		initRoleStateView();
			Fragment f = mPagerAdapter.getItemAt(mPager.getCurrentItem());
			if(FlexibleSpaceWithImageRecyclerViewFragment.class.isInstance(f)){
				((FlexibleSpaceWithImageRecyclerViewFragment)f).refresh();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private void initView() {
    	rightImg = (ImageView) findViewById(R.id.download_manage);
    	
    	mPagerAdapter = new NavigationAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.primary_txt));
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mPager);
        
        mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
//        mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mTabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        titleHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        

        imageView = findViewById(R.id.image);
        overlayView = findViewById(R.id.overlay);
        titleView = (EditText) findViewById(R.id.title);
        titleView.setVisibility(View.GONE);
        
        // Initialize the first Fragment's state when layout is completed.
        ScrollUtils.addOnGlobalLayoutListener(mSlidingTabLayout, new Runnable() {
            @Override
            public void run() {
            	flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
            	tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
            	titleInitialWidth = getResources().getDimensionPixelSize(R.dimen.main_title_initial_width);
                translateTab(0, false);
            }
        });
        
        findViewById(R.id.me).setOnClickListener(new OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		Intent intent = new Intent(MainActivity.this, MeActivity.class);
        		startActivity(intent);
        	}
        });
        
//        titleView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(MainActivity.this, SearchActivity.class);
//				startActivity(intent);
//			}
//		});
    }
    
    private void initRoleStateView(){
    	if(CacheManager.INSTANCE.getCurrentUser() != null && CacheManager.INSTANCE.getCurrentUser().getRole() == Role.ADMIN){
    		rightImg.setImageResource(R.drawable.add);
    		rightImg.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				Intent intent = new Intent(MainActivity.this, EditDetailActivity.class);
    				startActivity(intent);
    			}
    		});
    	}else{
    		rightImg.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				Intent intent = new Intent(MainActivity.this, DownloadActivity.class);
    				startActivity(intent);
    			}
    		});
    	}
    }

	/**
     * Called by children Fragments when their scrollY are changed.
     * They all call this method even when they are inactive
     * but this Activity should listen only the active child,
     * so each Fragments will pass themselves for Activity to check if they are active.
     *
     * @param scrollY scroll position of Scrollable
     * @param s       caller Scrollable view
     */
    public void onScrollChanged(int scrollY, Scrollable s) {
        FlexibleSpaceWithImageBaseFragment fragment =
                (FlexibleSpaceWithImageBaseFragment) mPagerAdapter.getItemAt(mPager.getCurrentItem());
        if (fragment == null) {
            return;
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }
        Scrollable scrollable = (Scrollable) view.findViewById(R.id.scroll);
        if (scrollable == null) {
            return;
        }
        if (scrollable == s) {
            // This method is called by not only the current fragment but also other fragments
            // when their scrollY is changed.
            // So we need to check the caller(S) is the current fragment.
            int adjustedScrollY = Math.min(scrollY, mFlexibleSpaceHeight - mTabHeight - titleHeight);
            translateTab(adjustedScrollY, false);
            propagateScroll(adjustedScrollY);
        }
    }

    private void translateTab(int scrollY, boolean animated) {
        // Translate overlay and image
        float flexibleRange = flexibleSpaceImageHeight - titleHeight * 2;
//        float flexibleRange = flexibleSpaceImageHeight - getActionBarSize();
        int minOverlayTransitionY = tabHeight - overlayView.getHeight();
        ViewHelper.setTranslationY(overlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(imageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(overlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scaleLLw = (flexibleRange - scrollY) / flexibleRange;
        float scale = 1 + ScrollUtils.getFloat(scaleLLw * MAX_TEXT_SCALE_DELTA, 0, MAX_TEXT_SCALE_DELTA);
        int width = (int)(titleInitialWidth * scale);
        final LayoutParams lp = titleView.getLayoutParams();
    	lp.width = width;
    	titleView.setLayoutParams(lp);

        // Translate title text
        int maxTitleTranslationY = flexibleSpaceImageHeight - tabHeight;
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(titleView, titleTranslationY - titleHeight);

        // If tabs are moving, cancel it to start a new animation.
        ViewPropertyAnimator.animate(mSlidingTabLayout).cancel();
        // Tabs will move between the top of the screen to the bottom of the image.
        float translationY = ScrollUtils.getFloat(-scrollY + mFlexibleSpaceHeight - mTabHeight, 0, mFlexibleSpaceHeight - mTabHeight);
        if (animated) {
            // Animation will be invoked only when the current tab is changed.
            ViewPropertyAnimator.animate(mSlidingTabLayout)
                    .translationY(translationY)
                    .setDuration(200)
                    .start();
        } else {
            // When Fragments' scroll, translate tabs immediately (without animation).
            ViewHelper.setTranslationY(mSlidingTabLayout, translationY);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setPivotXToTitle(View view) {
        final EditText mTitleView = (EditText) view.findViewById(R.id.title);
        Configuration config = getResources().getConfiguration();
        if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT
                && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            ViewHelper.setPivotX(mTitleView, view.findViewById(android.R.id.content).getWidth());
        } else {
            ViewHelper.setPivotX(mTitleView, -1);
        }
    }

    private void propagateScroll(int scrollY) {
        // Set scrollY for the fragments that are not created yet
        mPagerAdapter.setScrollY(scrollY);

        // Set scrollY for the active fragments
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            // Skip current item
            if (i == mPager.getCurrentItem()) {
                continue;
            }

            // Skip destroyed or not created item
            FlexibleSpaceWithImageBaseFragment f =
                    (FlexibleSpaceWithImageBaseFragment) mPagerAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            View view = f.getView();
            if (view == null) {
                continue;
            }
            f.setScrollY(scrollY, mFlexibleSpaceHeight);
            f.updateFlexibleSpace(scrollY);
        }
    }

    /**
     * This adapter provides three types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private static final String[] TITLES = Constant.MAIN_VIEW_SLIDE_TITLES;

        private int mScrollY;

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        @Override
        protected Fragment createItem(int position) {
        	FlexibleSpaceWithImageBaseFragment f;
        	switch (position) {
			case 0:
				f = new FlexibleSpaceWithImageRecyclerViewFragment(Constant.RE_DIAN);
				break;
				
			case 1:
				f = new FlexibleSpaceWithImageRecyclerViewFragment(Constant.YOU_XI);
				break;
				
			case 2:
				f = new FlexibleSpaceWithImageRecyclerViewFragment(Constant.YING_YONG);
				break;
				
			case 3:
			default:
				f = new FlexibleSpaceWithImageRecyclerViewFragment(Constant.QI_TA);
				break;
			}
            f.setArguments(mScrollY);
            return f;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == Constant.REQUEST_LOGIN && resultCode == Constant.RESULT_LOGIN_SUCCEED){
    		AppDownloadState ads = (AppDownloadState) data.getExtras().getSerializable(Constant.APP_DOWNLOAD_STATE);
    		if(ads != null){
    			if(ads.getType() == AppType.APP || ads.getType() == AppType.GAME){
    				MyTools.startDownload(ads);
    				try {
    					Fragment f = mPagerAdapter.getItemAt(mPager.getCurrentItem());
    					if(FlexibleSpaceWithImageRecyclerViewFragment.class.isInstance(f)){
    						((FlexibleSpaceWithImageRecyclerViewFragment)f).refresh();
    					}
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    			}
    			else if(ads.getType() == AppType.WEB){
    				MyTools.openWeb(MainActivity.this, ads.getDownloadUrl());
    			}
    		}
    	}
    }
}
