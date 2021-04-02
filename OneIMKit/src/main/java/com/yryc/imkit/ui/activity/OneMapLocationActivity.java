package com.yryc.imkit.ui.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.yryc.imkit.R;
import com.yryc.imkit.base.BaseActivity;
import com.yryc.imkit.constant.MessageType;
import com.yryc.imkit.core.slim.SlimAdapter;
import com.yryc.imkit.core.slim.SlimInjector;
import com.yryc.imkit.core.slim.viewinjector.IViewInjector;
import com.yryc.imlib.model.chat.ChatLocation;
import com.yryc.imlib.model.net.LocationInfo;
import com.yryc.imlib.model.net.MapPoi;
import com.yryc.imkit.utils.CommonUtils;
import com.yryc.imkit.utils.PixelUtils;
import com.yryc.imkit.widget.xview.XLoadView;
import com.yryc.imlib.info.io.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2019/7/5 14:06
 * @describe :
 */


public class OneMapLocationActivity extends BaseActivity implements View.OnClickListener,
        GeocodeSearch.OnGeocodeSearchListener, AMap.OnCameraChangeListener, AMap.OnMapLoadedListener, AMapLocationListener, PoiSearch.OnPoiSearchListener {

    private TextView tvBack;
    private TextView tvTitle;
    private TextView tvSend;
    private TextureMapView mvMap;
    private RecyclerView rvLocation;
    private AMap mMap;
    private GeocodeSearch mGeocodeSearch;
    private AMapLocationClient mLocationClient;
    private ImageView ivLocationButton;
    private LocationInfo mCurrentLocation;
    private LocationInfo mCurrentMapCenterLocation = new LocationInfo();
    private List<Object> mData = new ArrayList<>();
    private MapPoi mCurrentChooseMapPoi;
    private SlimAdapter mAdapter;
    private XLoadView xlvContent;
    private Marker mScreenMarker;
    private ReentrantLock lock = new ReentrantLock();
    private boolean isReloadPoi = true;

    @Override
    protected void onResume() {
        mvMap.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mvMap.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mvMap.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void initOther(Bundle savedInstanceState) {
        mvMap.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mvMap.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map_location;
    }

    @Override
    protected void initView() {
        tvBack = findViewById(R.id.tv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvSend = findViewById(R.id.tv_send);
        mvMap = findViewById(R.id.tmv_map);
        xlvContent = findViewById(R.id.xlv_content);
        xlvContent.visibleEmptyView();
        ivLocationButton = findViewById(R.id.location_button);
        rvLocation = findViewById(R.id.rv_location);
        tvBack.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        ivLocationButton.setOnClickListener(this);
        mMap = mvMap.getMap();
        mMap.setOnCameraChangeListener(this);
        mMap.setOnMapLoadedListener(this);
        mGeocodeSearch = new GeocodeSearch(this);
        mGeocodeSearch.setOnGeocodeSearchListener(this);
        initRecycleView();
        initMap();
        initMapClient();
    }

    private void initRecycleView() {
        rvLocation.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = SlimAdapter.create().register(R.layout.item_near_address, new SlimInjector<MapPoi>() {
            @Override
            public void onInject(MapPoi mapPoi, IViewInjector iViewInjector) {
                showAoiItem(mapPoi, iViewInjector);
            }
        }).attachTo(rvLocation).updateData(mData);
    }

    private void showAoiItem(MapPoi mapPoi, IViewInjector iViewInjector) {
        PoiItem poiItem = mapPoi.getPoiItem();
        iViewInjector.text(R.id.tv_poi_name, mapPoi.getPoiItem().getTitle())
                .text(R.id.tv_address, poiItem.getProvinceName() + poiItem.getCityName()
                        + poiItem.getAdName() + poiItem.getSnippet() + poiItem.getTitle())
                .clicked(R.id.rl_root, v -> {
                    if (mCurrentChooseMapPoi != null) {
                        mCurrentChooseMapPoi.setChecked(false);
                    }
                    mapPoi.setChecked(true);
                    mCurrentChooseMapPoi = mapPoi;
                    isReloadPoi = false;
                    LatLng latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom));
                    mAdapter.notifyDataSetChanged();
                });
        if (mapPoi.isChecked()) {
            iViewInjector.visibility(R.id.iv_hook, View.VISIBLE);
        } else {
            iViewInjector.visibility(R.id.iv_hook, View.INVISIBLE);
        }
    }

    private void initMap() {
        UiSettings settings = mMap.getUiSettings();
        // 设置地图logo显示
        settings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
        settings.setLogoBottomMargin(-50);//隐藏logo
        // 设置缩放按钮
        settings.setZoomControlsEnabled(false);
        settings.setZoomGesturesEnabled(true);
        settings.setZoomInByScreenCenter(true);
        // 设置比例尺
        settings.setScaleControlsEnabled(false);
        // 设置地图是否可以倾斜
        settings.setTiltGesturesEnabled(false);
        settings.setRotateGesturesEnabled(false);
        //显示默认的定位按钮
        settings.setMyLocationButtonEnabled(false);
        //高德地图自定义样式
        //setMapCustomStyleFile(amap, Utils.getContext(), "amap_style_3.7.4_1.data");
        mMap.setMinZoomLevel(Float.valueOf(5));
        mMap.setMaxZoomLevel(Float.valueOf(20));
        mMap.setMyLocationEnabled(true);
        //可触发定位并显示当前位置
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.showMyLocation(true);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        BitmapDescriptor locDescriptor = BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_im_map_arrow));
        myLocationStyle.myLocationIcon(locDescriptor);
        mMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mMap.setOnMapTouchListener(motionEvent -> isReloadPoi = true);
    }

    private void initMapClient() {
        mLocationClient = new AMapLocationClient(this.getApplicationContext());
        mLocationClient.setLocationListener(this);
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否单次定位
        locationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
//        locationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
//        locationOption.setInterval(5000);
        //缓存机制
        locationOption.setLocationCacheEnable(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(locationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    protected void initData() {
        tvTitle.setText("位置");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_back) {
            finish();
        } else if (v.getId() == R.id.tv_send) {
            sendMapMessage();
        } else if (v.getId() == R.id.location_button) {
            if (mCurrentLocation != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 14));
            }
        }
    }

    /**
     * 发送地图坐标位置
     */
    private void sendMapMessage() {
        if (mCurrentMapCenterLocation != null && mCurrentChooseMapPoi != null) {
            ChatLocation.Body body = new ChatLocation.Body();
            body.setContent(mCurrentChooseMapPoi.getPoiItem().getTitle());
            body.setAddress(mCurrentChooseMapPoi.getPoiItem().getProvinceName() + mCurrentChooseMapPoi.getPoiItem().getCityName()
                    + mCurrentChooseMapPoi.getPoiItem().getAdName() + mCurrentChooseMapPoi.getPoiItem().getSnippet() + mCurrentChooseMapPoi.getPoiItem().getTitle());
            body.setType(MessageType.LOCATION.getType());
            body.setLocation(new Location(mCurrentMapCenterLocation.getLatitude(), mCurrentMapCenterLocation.getLongitude()));
            CommonUtils.sendMessage(this, MessageType.LOCATION, body);
            finish();
        } else {
            Toast.makeText(OneMapLocationActivity.this, "请先选择选择位置", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLonPoint latLonPoint = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
        mCurrentMapCenterLocation.setLatitude(cameraPosition.target.latitude);
        mCurrentMapCenterLocation.setLongitude(cameraPosition.target.longitude);
//        GeocodeSearch geocodeSearch = new GeocodeSearch(this);
//        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
//        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
//        geocodeSearch.setOnGeocodeSearchListener(this);
        if (isReloadPoi) {
            PoiSearch.Query query = new PoiSearch.Query("", "01|02|03|12|15|16|17|18|19|99");
            query.setPageNum(1);
            query.setPageSize(30);
            PoiSearch poiSearch = new PoiSearch(this, query);
            poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, 5000));
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();
        }
        startJumpAnimation();
    }

    @Override
    public void onMapLoaded() {
        addMarkerInScreenCenter();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == AMapLocation.LOCATION_SUCCESS) {
            mCurrentLocation = new LocationInfo();
            mCurrentLocation.setLongitude(aMapLocation.getLongitude());
            mCurrentLocation.setLatitude(aMapLocation.getLatitude());
            mCurrentLocation.setCity(aMapLocation.getCity());
            mCurrentLocation.setCityCode(aMapLocation.getCityCode());
            mCurrentLocation.setProvince(aMapLocation.getProvince());
            mCurrentLocation.setAoiName(aMapLocation.getAoiName());
            mCurrentLocation.setDistrict(aMapLocation.getDistrict());
            mCurrentLocation.setAddress(aMapLocation.getAddress());
            mCurrentLocation.setStreet(aMapLocation.getStreet());
            mCurrentLocation.setStreetNum(aMapLocation.getStreetNum());
            mCurrentLocation.setAddress(aMapLocation.getAddress());
            mCurrentLocation.setTime(System.currentTimeMillis());
            mCurrentLocation.setAdCode(aMapLocation.getAdCode());
            mCurrentLocation.setDescription(aMapLocation.getDescription());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 14));
        } else {
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        if (rCode == 1000) {
            List<PoiItem> aois = poiResult.getPois();
            if (aois != null && aois.size() > 0) {
                mData.clear();
                for (int i = 0; i < aois.size(); i++) {
                    MapPoi mapPoi = new MapPoi();
                    if (i == 0) {
                        mapPoi.setPoiItem(aois.get(i));
                        mapPoi.setChecked(true);
                        mCurrentChooseMapPoi = mapPoi;
                    } else {
                        mapPoi.setPoiItem(aois.get(i));
                        mapPoi.setChecked(false);
                    }
                    mData.add(mapPoi);
                }
                rvLocation.scrollToPosition(0);
                mAdapter.notifyDataSetChanged();
                xlvContent.visibleSuccessView();
            } else {
                xlvContent.visibleEmptyView();
            }
        } else {
            xlvContent.visibleEmptyView();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 在屏幕中心添加一个Marker
     */
    private void addMarkerInScreenCenter() {
        LatLng latLng = mMap.getCameraPosition().target;
        Point screenPosition = mMap.getProjection().toScreenLocation(latLng);
        mScreenMarker = mMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 1.0f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_im_map_lable)));
        //设置Marker在屏幕上,不跟随地图移动
        mScreenMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
    }

    /**
     * 屏幕中心marker 跳动
     */
    public void startJumpAnimation() {
        OneMapLocationActivity.this.runOnUiThread(() -> {
            if (mScreenMarker != null) {
                //根据屏幕距离计算需要移动的目标点
                final LatLng latLng = mScreenMarker.getPosition();
                Point point = mMap.getProjection().toScreenLocation(latLng);
                point.y -= PixelUtils.dp2px(this, 30);
                LatLng target = mMap.getProjection().fromScreenLocation(point);
                //使用TranslateAnimation,填写一个需要移动的目标点
                Animation animation = new TranslateAnimation(target);
                animation.setInterpolator(input -> {
                    // 模拟重加速度的interpolator
                    if (input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                    }
                });
                //整个移动所需要的时间
                animation.setDuration(500);
                //设置动画
                mScreenMarker.setAnimation(animation);
                //开始动画
                mScreenMarker.startAnimation();
            } else {
                Log.e("amap", "screenMarker is null");
            }
        });
    }

}
