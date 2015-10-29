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

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.changhong.chcare.core.webapi.bean.ResponseBean;
import cn.changhong.chcare.core.webapi.handler.AsyncResponseCompletedHandler;
import cn.changhong.chcare.core.webapi.server.CHCareWebApiProvider;
import cn.changhong.chcare.core.webapi.server.ChCareWepApiServiceType;
import cn.changhong.chcare.core.webapi.server.IASAccountService;
import cn.changhong.chcare.core.webapi.server.CHCareWebApiProvider.WebApiServerType;

import com.aaa.db.AppDetail;
import com.aaa.db.AppDownloadState;
import com.aaa.util.MyTools;
import com.changhong.util.db.bean.CacheManager;
import com.changhong.util.download.DownLoadCallback;
import com.changhong.util.download.DownloadManager;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.llw.AppStore.R;
import com.nineoldandroids.view.ViewHelper;

public class FlexibleSpaceWithImageRecyclerViewFragment extends FlexibleSpaceWithImageBaseFragment<ObservableRecyclerView> {

	private int type;
	private String tag;
	private ArrayList<AppDownloadState> datas = new ArrayList<AppDownloadState>();
	private ArrayList<AppDetail> rawDatas;
	private ObservableRecyclerView recyclerView;
	private boolean hasInit = false;
	
	private IASAccountService accountService = (IASAccountService) CHCareWebApiProvider.Self
			.defaultInstance().getDefaultWebApiService(
					WebApiServerType.AS_ACCOUNT_SERVER);
	
	public FlexibleSpaceWithImageRecyclerViewFragment(int type){
		this.type = type;
		tag = MyTools.getTag(type);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	doGetData();
    	
        View view = inflater.inflate(R.layout.fragment_flexiblespacewithimagerecyclerview, container, false);

        recyclerView = (ObservableRecyclerView) view.findViewById(R.id.scroll);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        final View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.recycler_header, null);
        final int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        headerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, flexibleSpaceImageHeight));
        setDummyDataWithHeader(recyclerView, datas, headerView, type);

        // TouchInterceptionViewGroup should be a parent view other than ViewPager.
        // This is a workaround for the issue #117:
        // https://github.com/ksoichiro/Android-ObservableScrollView/issues/117
        recyclerView.setTouchInterceptionViewGroup((ViewGroup) view.findViewById(R.id.fragment_root));

        // Scroll to the specified offset after layout
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_SCROLL_Y)) {
            final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
            ScrollUtils.addOnGlobalLayoutListener(recyclerView, new Runnable() {
                @Override
                public void run() {
                    int offset = scrollY % flexibleSpaceImageHeight;
                    RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                    if (lm != null && lm instanceof LinearLayoutManager) {
                        ((LinearLayoutManager) lm).scrollToPositionWithOffset(0, -offset);
                    }
                }
            });
            updateFlexibleSpace(scrollY, view);
        } else {
            updateFlexibleSpace(0, view);
        }

        recyclerView.setScrollViewCallbacks(this);

        return view;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	if(hasInit){
    		refresh();
    	}else{
    		hasInit = true;
    	}
    }
    
	@Override
    public void setScrollY(int scrollY, int threshold) {
        View view = getView();
        if (view == null) {
            return;
        }
        ObservableRecyclerView recyclerView = (ObservableRecyclerView) view.findViewById(R.id.scroll);
        if (recyclerView == null) {
            return;
        }
        View firstVisibleChild = recyclerView.getChildAt(0);
        if (firstVisibleChild != null) {
            int offset = scrollY;
            int position = 0;
            if (threshold < scrollY) {
                int baseHeight = firstVisibleChild.getHeight();
                position = scrollY / baseHeight;
                offset = scrollY % baseHeight;
            }
            RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
            if (lm != null && lm instanceof LinearLayoutManager) {
                ((LinearLayoutManager) lm).scrollToPositionWithOffset(position, -offset);
            }
        }
    }

    @Override
    protected void updateFlexibleSpace(int scrollY, View view) {
        int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);

        View recyclerViewBackground = view.findViewById(R.id.list_background);

        // Translate list background
        ViewHelper.setTranslationY(recyclerViewBackground, Math.max(0, -scrollY + flexibleSpaceImageHeight));

        // Also pass this event to parent Activity
        MainActivity parentActivity = (MainActivity) getActivity();
        if (parentActivity != null) {
            parentActivity.onScrollChanged(scrollY, (ObservableRecyclerView) view.findViewById(R.id.scroll));
        }
    }
    
    public void refresh(){
    	if(MyRecyclerAdapter.class.isInstance(recyclerView.getAdapter())){
			((MyRecyclerAdapter)recyclerView.getAdapter()).refresh();
		}
    }
    
    public void refreshItem(int position){
    	if(MyRecyclerAdapter.class.isInstance(recyclerView.getAdapter())){
			((MyRecyclerAdapter)recyclerView.getAdapter()).refreshItem(position);
		}
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	DownloadManager.getDownloadManager().setDownLoadCallback(new DownLoadCallback()
 		{
 			@Override
 			public void onSuccess(String url) {
 				super.onSuccess(url);
 				refresh();
 			}
 			
 			@Override
 			public void onFailure(String url, String strMsg) {
 				super.onFailure(url, strMsg);
 				MyTools.toastLoadFailure(getActivity());
 			}
 			
 			@Override
 			public void onLoading(String url, long totalSize, long currentSize, long speed)
 			{
 				super.onLoading(url, totalSize, currentSize, speed);
 			}
 		});
    }
    
    private Handler handler = new Handler();
    private void doGetData() {
    	accountService.getApps(null, new AsyncResponseCompletedHandler<String>() {

			@Override
			public String doCompleted(ResponseBean<?> response, ChCareWepApiServiceType servieType) {
				if(response.getState() >= 0 && response.getData() != null){
					rawDatas = (ArrayList<AppDetail>) response.getData();
					AppDownloadState ads;
					for(AppDetail item : rawDatas){
						ads = new AppDownloadState(item);
						ads = MyTools.initAppDownloadState(getActivity(), ads);
						CacheManager.INSTANCE.putAppData(ads);
					}
					
					datas = CacheManager.INSTANCE.getAppData(tag);
					refresh();
				}else{
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							doGetData();
						}
					}, 2000);
				}
				return null;
			}
		});
    }
}
