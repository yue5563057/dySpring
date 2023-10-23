package com.xfarmer.common.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.xfarmer.common.constant.URLCodeConst;
import com.xfarmer.common.util.secret.Md5Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地图相关工具类
 */
@Component
public class MapUtil {


    /**腾讯地图KEY*/
    private static String TX_MAP_KEY;
    /**腾讯地图SK*/
    private static String TX_MAP_SK;


    private static synchronized void setInit(String txMapKey,String txMapSk){
        TX_MAP_KEY = txMapKey;
        TX_MAP_SK = txMapSk;
    }

    @Value("${spring.dyBase.txMap.key}")
    private String txKey;
    @Value("${spring.dyBase.txMap.sk}")
    private String sk;

    @PostConstruct
    public void init() {
        setInit(txKey,sk);
    }



    private MapUtil(){

    }
    private MapUtil instance ;

    public synchronized MapUtil getInstance(){
        if(instance==null){
            instance = new MapUtil();
        }
        return instance;
    }

    /**
     * 赤道半径
     **/
    private static final double EARTH_RADIUS = 6378137;

    /**
     * 转化为弧度(rad)
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算两点的距离
     *
     * @param lng1 经度1
     * @param lat1 纬度1
     * @param lng2 经度2
     * @param lat2 纬度2
     * @return 两点的距离 KM
     */
    public static double calculationDistanceKm(Double lng1, Double lat1, Double lng2, Double lat2) {
        return calculationDistance(lng1, lat1, lng2, lat2) / 1000;
    }
    /**
     * 计算两点的距离
     *
     * @param lng1 经度1
     * @param lat1 纬度1
     * @param lng2 经度2
     * @param lat2 纬度2
     * @return 两点的距离
     */
    public static double calculationDistance(String lng1, String lat1, String lng2, String lat2) {
        try {
            Double lngDouBle = Double.valueOf(lng1);
            Double latDouBle = Double.valueOf(lat1);
            Double lngDouBle1 = Double.valueOf(lng2);
            Double latDouBle1 = Double.valueOf(lat2);
            return calculationDistance(lngDouBle, latDouBle, lngDouBle1, latDouBle1);
        } catch (Exception e){
            return 0d;
        }

    }

    /**
     * 计算两点的距离
     *
     * @param lng1 经度1
     * @param lat1 纬度1
     * @param lng2 经度2
     * @param lat2 纬度2
     * @return 两点的距离Km
     */
    public static double calculationDistanceKm(String lng1, String lat1, String lng2, String lat2) {
        try {
            Double lngDouBle = Double.valueOf(lng1);
            Double latDouBle = Double.valueOf(lat1);
            Double lngDouBle1 = Double.valueOf(lng2);
            Double latDouBle1 = Double.valueOf(lat2);
            return calculationDistance(lngDouBle, latDouBle, lngDouBle1, latDouBle1)/1000;
        } catch (Exception e){
            return 0d;
        }

    }

    /**
     * 计算两点的距离
     *
     * @param lng1 经度1
     * @param lat1 纬度1
     * @param lng2 经度2
     * @param lat2 纬度2
     * @return 两点的距离 KM
     */
    public static double calculationDistance(Double lng1, Double lat1, Double lng2, Double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;
    }

    /**
     * 计算两点之间步行所需要的时间
     *
     * @param lng1 经度1
     * @param lat1 纬度1
     * @param lng2 经度2
     * @param lat2 纬度2
     * @return 两点之间所需要的时间(步行) 秒
     */
    public static Long calculationDistanceTime(Double lng1, Double lat1, Double lng2, Double lat2) {
        double distance = calculationDistance(lng1, lat1, lng2, lat2);
        Double v = distance * 1000 / 1;
        return v.longValue();
    }

    /**
     * 计算两点之间步行所需要的时间
     *
     * @param lng1 经度1
     * @param lat1 纬度1
     * @param lng2 经度2
     * @param lat2 纬度2
     * @return 两点之间所需要的时间(步行) 秒
     */
    public static Long calculationDistanceTime(String lng1, String lat1, Double lng2, Double lat2) {
        double distance = calculationDistanceKm(lng1, lat1, lng2, lat2);
        Double v = distance * 1000 / 1;
        return v.longValue();
    }

    /**
     * 计算两点的距离
     *
     * @param lng1 经度1
     * @param lat1 纬度1
     * @param lng2 经度2
     * @param lat2 纬度2
     * @return 两点的距离 KM
     */
    public static double calculationDistanceKm(String lng1, String lat1, Double lng2, Double lat2) {
        if (StringUtil.isNull(lng1) || StringUtil.isNull(lat1) || lng2 == null || lat2 == null) {
            return 0;
        }
        return calculationDistanceKm(Double.valueOf(lng1), Double.valueOf(lat1), lng2, lat2);
    }


    /**
     * 坐标是否在指定区域内
     *
     * @param doubles 区域内的坐标集合
     * @param x       坐标的经度
     * @param y       坐标的纬度
     * @return 是否包含在区域内
     */
    public static boolean polygonJudgment(List<Double[]> doubles, double x, double y) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (Double[] aDouble : doubles) {
            Coordinate coord = new Coordinate(aDouble[0], aDouble[1]);
            coordinates.add(coord);
        }
        Coordinate coord = new Coordinate(doubles.get(0)[0], doubles.get(0)[1]);
        coordinates.add(coord);
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] array = coordinates.toArray(new Coordinate[1]);
        Polygon polygon = geometryFactory.createPolygon(array);
        Point point = geometryFactory.createPoint(new Coordinate(x, y));
        return polygon.contains(point);
    }

    /**
     * 坐标是否在指定区域内
     *
     * @param doubles 区域内的坐标集合
     * @param x       坐标的经度
     * @param y       坐标的纬度
     * @return 是否包含在区域内
     */
    public static boolean polygonJudgmentStr(List<String[]> doubles, double x, double y) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (String[] aDouble : doubles) {
            Coordinate coord = new Coordinate(Double.valueOf(aDouble[0]), Double.valueOf(aDouble[1]));
            coordinates.add(coord);
        }
        Coordinate coord = new Coordinate(Double.valueOf(doubles.get(0)[0]), Double.valueOf(doubles.get(0)[1]));
        coordinates.add(coord);
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] array = coordinates.toArray(new Coordinate[1]);
        Polygon polygon = geometryFactory.createPolygon(array);
        Point point = geometryFactory.createPoint(new Coordinate(x, y));
        return polygon.contains(point);
    }

    /**
     * 坐标是否在指定区域内
     *
     * @param doubles 区域内的坐标集合
     * @param x       坐标的经度
     * @param y       坐标的纬度
     * @return 是否包含在区域内
     */
    public static boolean polygonJudgment(List<Double[]> doubles, String x, String y) {
        double x1 = Double.parseDouble(x);
        double y1 = Double.parseDouble(y);
        return polygonJudgment(doubles, x1, y1);
    }

    /**
     * 坐标是否在指定区域内
     *
     * @param doubles 区域内的坐标集合
     * @param x       坐标的经度
     * @param y       坐标的纬度
     * @return 是否包含在区域内
     */
    public static boolean polygonJudgmentStr(List<String[]> doubles, String x, String y) {
        double x1 = Double.parseDouble(x);
        double y1 = Double.parseDouble(y);
        return polygonJudgmentStr(doubles, x1, y1);
    }



    /**坐标转换为地址*/
    public static ApiResponse<JSONObject> polygonToAddr(String lat, String lng){
        String url="https://apis.map.qq.com/ws/geocoder/v1/?key="+ TX_MAP_KEY +"&location="+lat+","+lng+"";
        String sige = Md5Util.encrypt32("/ws/geocoder/v1/?key="+ TX_MAP_KEY +"&location="+lat+","+lng+"" + TX_MAP_SK);
        url = url+"&sig="+sige;
        String s = HttpTool.doGet(url,new HashMap<>());
        JSONObject object = JSON.parseObject(s);
        if(object==null){
            return new ApiResponse<>(URLCodeConst.PARAM_ERROR,"地址解析失败");
        }
        return new ApiResponse<>(object);
    }

    /**
     * 地址转换成坐标
     * @param address
     */
    public static JSONObject addrToPolygon(String address) {
        String url = "https://apis.map.qq.com/ws/geocoder/v1/";
        String s = HttpTool.doGet(url, Map.of("address",address,"key", TX_MAP_KEY));
        JSONObject object = JSON.parseObject(s);
        String statusStr = "status";
        if(object!=null&&object.getInteger(statusStr)==0){
            JSONObject result = object.getJSONObject("result");
            return result.getJSONObject("location");
        }else{
            return null;
        }
    }

}
