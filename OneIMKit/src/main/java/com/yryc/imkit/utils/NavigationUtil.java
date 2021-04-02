package com.yryc.imkit.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.yryc.imkit.ui.view.dialog.MapLocationChooseDialog;

/**
 * 导航工具类
 * Created by Administrator on 2018/7/26.
 */
public class NavigationUtil {

    public final static String BAIDU_PACKAGENAME = "com.baidu.BaiduMap";
    public final static String GAODE_PACKAGENAME = "com.autonavi.minimap";
    public final static String TENCENT_PACKAGENAME = "com.tencent.map";
    private static MapLocationChooseDialog mMapLocationChooseDialog;

    /**
     * 高德路线规划
     *
     * @param context
     * @param location location[0]纬度lat，location[1]经度lon
     */
    public static void gaodeRoute(Context context, double[] location, String destination) {
        if (isPackageInstalled(context, GAODE_PACKAGENAME)) {

            Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(
                    "amapuri://route/plan/?" +
                            "&dname=" + destination +
                            "&dlat=" + location[0] +
                            "&dlon=" + location[1] +
                            "&dev=0&t=0"));
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "您尚未安装高德地图", Toast.LENGTH_SHORT).show();
        }
    }

    public static void gaodeLocation(Context context, double[] location, String poiName) {
        if (isPackageInstalled(context, GAODE_PACKAGENAME)) {

            Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(
                    "androidamap://viewMap?" +
                            "sourceApplication=" + "“yryc”" +
                            "&poiname=" + poiName +
                            "&lat=" + location[0] +
                            "&lon=" + location[1] +
                            "&dev=0"));
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "您尚未安装高德地图", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 高德导航
     *
     * @param context
     * @param location location[0]纬度lat，location[1]经度lon
     */
    public static void gaodeGuide(Context context, double[] location, String destination) {
        if (isPackageInstalled(context, GAODE_PACKAGENAME)) {
            Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(
                    "androidamap://navi?sourceApplication=“yryc”" +
                            "&poiname=" + destination +
                            "&lat=" + location[0] +
                            "&lon=" + location[1] +
                            "&dev=0"));
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "您尚未安装高德地图", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 百度地图定位
     *
     * @param context
     * @param location
     */
    public static void baiduLocation(Context context, double[] location, String poiName) {
        double[] lat_lon = gaoDeToBaidu(location);
        if (isPackageInstalled(context, BAIDU_PACKAGENAME)) {
            String uri = "baidumap://map/marker?location=%s,%s&title=%s&content=%s&traffic=on&src=%s";
            uri = String.format(uri, lat_lon[0], lat_lon[1], poiName, poiName, "yryc");
            Intent intent = new Intent();
            intent.setData(Uri.parse(uri));
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "您尚未安装百度地图", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 百度路线规划
     *
     * @param context
     * @param location location[0]纬度lat，location[1]经度lon
     */
    public static void baiduRoute(Context context, double[] location, String destination) {

        double[] lat_lon = gaoDeToBaidu(location);
        if (isPackageInstalled(context, BAIDU_PACKAGENAME)) {
            Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(
                    "baidumap://map/direction?" +
                            "destination=" + lat_lon[0] + "," + lat_lon[1] +
                            "&coord_type=bd09ll&mode=driving"));
            context.startActivity(intent); //启动调用
        } else {
            Toast.makeText(context, "您尚未安装百度地图", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 百度导航
     *
     * @param context
     * @param location location[0]纬度lat，location[1]经度lon
     */
    public static void baiduGuide(Context context, double[] location, String destination) {

        double[] lat_lon = gaoDeToBaidu(location);
        if (isPackageInstalled(context, BAIDU_PACKAGENAME)) {
            Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(
                    "baidumap://map/navi?" +
                            "location=" + lat_lon[0] + "," + lat_lon[1] +
                            "&coord_type=bd09ll"));
            context.startActivity(intent); //启动调用
        } else {
            Toast.makeText(context, "您尚未安装百度地图", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 腾讯导航
     *
     * @param context
     * @param location location[0]纬度lat，location[1]经度lon
     */
    public static void tencentGuide(Context context, double[] location, String destination) {

        if (isPackageInstalled(context, TENCENT_PACKAGENAME)) {
            Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(
                    "qqmap://map/routeplan?type=drive&from=&fromcoord=&" +
                            "to=" + destination +
                            "&tocoord=" + location[0] + "," + location[1] +
                            "&policy=0&referer=appName"));
            context.startActivity(intent); //启动调用
        } else {
            Toast.makeText(context, "您尚未安装腾讯地图", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 定位
     *
     * @param context
     */
    public static void mapLocation(Context context, double[] location, String poiName) {
        boolean isGaodePackageInstalled = NavigationUtil.isPackageInstalled(context, NavigationUtil.GAODE_PACKAGENAME);
        boolean isBaiduPackageInstalled = NavigationUtil.isPackageInstalled(context, NavigationUtil.BAIDU_PACKAGENAME);
        if (!isGaodePackageInstalled && !isBaiduPackageInstalled) {
            Toast.makeText(context, "您尚未安装地图APP", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isGaodePackageInstalled && !isBaiduPackageInstalled) {
            NavigationUtil.gaodeLocation(context, location, poiName);
        } else if (!isGaodePackageInstalled && isBaiduPackageInstalled) {
            NavigationUtil.baiduLocation(context, location, poiName);
        } else {
            if (mMapLocationChooseDialog == null) {
                mMapLocationChooseDialog = new MapLocationChooseDialog(context);
            }
            mMapLocationChooseDialog.show(location, poiName);
        }
    }

    /**
     * 高德经纬度转百度经纬度
     */
    private static double[] gaoDeToBaidu(double[] location) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = location[1], y = location[0];
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.sin(theta) + 0.006;
        bd_lat_lon[1] = z * Math.cos(theta) + 0.0065;
        return bd_lat_lon;
    }


    public static boolean isPackageInstalled(Context mContext, String packagename) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = mContext.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        } finally {
            return packageInfo != null;
        }
    }
}
