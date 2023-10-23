package com.xfarmer.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.codec.binary.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author 东岳
 */
public class OtherUtil {
    private static String UNKNOWN = "unknown";

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String parseIp(String ip, String appCode) {
        String host = "https://queryip.market.alicloudapi.com";
        String path = "/lundroid/queryip";
        String method = "GET";
        /**
         * 重要提示如下:
         * HttpUtils请从
         * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
         * 下载
         *
         * 相应的依赖请参照
         * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
         */
        String doGet = HttpTool.doGet(host + path, Map.of("ip", ip), Map.of("Authorization", "APPCODE " + appCode));
        //获取response的body
        return doGet;
    }


    /**
     * 时间长度转时间格式
     *
     * @param time 时间格式
     * @return
     */
    public static String seconds2Day(long time) {
        String dateTimes = null;
        long days = time / (60 * 60 * 24);
        long hours = (time % (60 * 60 * 24)) / (60 * 60);
        long minutes = (time % (60 * 60)) / 60;
        long seconds = time % 60;
        if (days > 0) {
            dateTimes = days + "天" + hours + "小时" + minutes + "分"
                    + seconds + "秒";
        } else if (hours > 0) {
            dateTimes = hours + "小时" + minutes + "分"
                    + seconds + "秒";
        } else if (minutes > 0) {
            dateTimes = minutes + "分"
                    + seconds + "秒";
        } else {
            dateTimes = seconds + "秒";
        }
        return dateTimes;
    }

    public static File getFile(String url) {
        //对本地文件命名
        String fileName = url.substring(url.lastIndexOf("."), url.length());
        File file = null;

        URL urlfile;
        InputStream inStream = null;
        OutputStream os = null;
        try {
            int length = 8192;
            file = File.createTempFile("net_url", fileName);
            //下载
            urlfile = new URL(url);
            inStream = urlfile.openStream();
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[length];
            while ((bytesRead = inStream.read(buffer, 0, length)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return file;
    }


    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /**
     * 获取随机签名
     *
     * @return
     */
    public static String createNonceStr() {
        return UUID.randomUUID().toString();
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strUrl url地址
     * @return url请求参数部分
     * @author lzf
     */
    public static String truncateUrlPage(String strUrl) {
        String strAllParam = null;
        String[] arrSplit = null;
        strUrl = strUrl.trim().toLowerCase();
        arrSplit = strUrl.split("[?]");
        if (strUrl.length() > 1 && arrSplit.length > 1) {
            for (int i = 1; i < arrSplit.length; i++) {
                strAllParam = arrSplit[i];
            }
        }
        return strAllParam;
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param url url地址
     * @return url请求参数部分
     * @author lzf
     */
    public static Map<String, String> urlSplit(String url) {
        Map<String, String> mapRequest = new HashMap<>(8);
        String[] arrSplit;
        String strUrlParam = truncateUrlPage(url);
        if (strUrlParam == null) {
            return mapRequest;
        }
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (StringUtil.isNull(arrSplitEqual[0])) {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * 将图片转换成Base64编码
     *
     * @param imgFile 待处理图片
     * @return
     */
    public static String getImgStr(File imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(data);
    }


    public static String parseDoubleToDate(Double timer) {
        if (timer == null) {
            return "00:00";
        }
        Double timeMin = 10d;
        Double min = timer / 60;
        String minStr;
        if (min < timeMin) {
            minStr = "0" + min.intValue();
        } else {
            minStr = min.toString();
        }
        Double s = timer % 60;
        Long l = s.longValue();
        String seconds = "00";
        if (l < timeMin) {
            seconds = "0" + l;
        } else {
            seconds = l.toString();
        }
        return minStr + ":" + seconds;
    }


    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            int byteLength = 8192;
            byte[] buffer = new byte[byteLength];
            while ((bytesRead = ins.read(buffer, 0, byteLength)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 格式化时间 年月日
     *
     * @param pattern  格式
     * @param dateTime 时间
     * @return
     */
    public static String formatLocalDate(String pattern, LocalDate dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String format = dateTime.format(formatter);
        return format;
    }

    /**
     * 格式化时间 年月日时分秒
     *
     * @param pattern  格式
     * @param dateTime 时间
     * @return
     */
    public static String formatLocalDateTime(String pattern, LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String format = dateTime.format(formatter);
        return format;
    }

    /**
     * 获取当前年月
     *
     * @return 格式YYYYMM
     */
    public static String getYearMonth() {
        LocalDateTime localDateTime = LocalDateTime.now();
        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();
        return year + "" + month;
    }


    public static String getDateHourMin() {
        String code = StringUtil.get6Code();
        return getDateHourMinSecond() + code;
    }

    public static String getDateHourMinSecond() {
        LocalDateTime localDateTime = LocalDateTime.now();
        int day = localDateTime.getDayOfMonth();
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();
        int second = localDateTime.getSecond();
        int nano = localDateTime.getNano();
        return day + "" + hour + "" + minute + "" + second + nano;
    }

    public static String randomStr(int length) {

        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        //由Random生成随机数
        Random random = null;
        StringBuilder sb = new StringBuilder();
        try {
            random = SecureRandom.getInstanceStrong();
            for (int i = 0; i < length; ++i) {
                int number = random.nextInt(str.length());
                sb.append(str.charAt(number));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取中文的拼音
     *
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String getPinYin(String name) throws BadHanyuPinyinOutputFormatCombination {
        char[] charArray = name.toCharArray();
        StringBuilder pinyin = new StringBuilder();
        HanyuPinyinOutputFormat defaultFormat = getHanyuPinyinOutputFormat();
        for (int i = 0; i < charArray.length; i++) {
            //匹配中文,非中文转换会转换成null
            if (Character.toString(charArray[i]).matches("[\\u4E00-\\u9FA5]+")) {
                String[] hanyuPinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(charArray[i], defaultFormat);
                String string = hanyuPinyinStringArray[0];
                pinyin.append(string);
            } else {
                pinyin.append(charArray[i]);
            }
        }
        return pinyin.toString();
    }

    private static HanyuPinyinOutputFormat getHanyuPinyinOutputFormat() {
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 设置大小写格式
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        // 设置声调格式：
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        return defaultFormat;
    }

    public static String getPinYinFirstAll(String name) throws BadHanyuPinyinOutputFormatCombination {
        char[] charArray = name.toCharArray();
        StringBuilder pinyin = new StringBuilder();
        HanyuPinyinOutputFormat defaultFormat = getHanyuPinyinOutputFormat();
        for (int i = 0; i < charArray.length; i++) {
            //匹配中文,非中文转换会转换成null
            if (Character.toString(charArray[i]).matches("[\\u4E00-\\u9FA5]+")) {
                String[] hanyuPinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(charArray[i], defaultFormat);
                if (hanyuPinyinStringArray != null) {
                    pinyin.append(hanyuPinyinStringArray[0].charAt(0));
                }
            } else {
                pinyin.append(charArray[i]);
            }
        }
        return pinyin.toString();
    }


    /**
     * 获取中文的首字母
     */
    public static String getFirstPinYin(String name) throws BadHanyuPinyinOutputFormatCombination {
        String firstAll = getPinYinFirstAll(name);
        if (StringUtil.isNotNull(firstAll)) {
            String regex = "[a-zA-Z]+";
            if (firstAll.substring(0, 1).matches(regex)) {
                return firstAll.substring(0, 1).toUpperCase();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }


    /**
     * xml 转 map
     *
     * @param xmlStr xml字符串
     * @return map
     */
    public static Map<String, String> getMapFromXml(String xmlStr) {
        SortedMap<String, String> map = new TreeMap<>();
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        assert document != null;
        Element root = document.getRootElement();
        List<Element> list = root.elements();
        for (Element element : list) {
            String key = element.getName();
            String value = element.getText();
            map.put(key, value);
        }
        return map;
    }


    /**
     * map 对象转换成 xml 字符串
     **/
    public static String mapToXml(Map<String, String> map) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            xml.append("<").append(entry.getKey()).append(">");
            xml.append(entry.getValue());
            xml.append("</").append(entry.getKey()).append(">");
        }
        xml.append("</xml>");
        return xml.toString();
    }


    public static BigDecimal bigDecimalNullToZero(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return BigDecimal.ZERO;
        }
        return bigDecimal;
    }

    private static String xxx1(String url, String method) {
        String[] split = url.split("/");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(method.toLowerCase());
        for (String s : split) {
            if (StringUtil.isNotNull(s)) {
                String index = s.substring(0, 1);
                String i = index.toUpperCase();
                String end = s.substring(1);
                stringBuilder.append(i).append(end);
            }
        }
        return stringBuilder.toString();
    }


    private static void updateFileName() {
        File manfile = new File("C:\\Users\\东岳\\Desktop\\产品图片\\img");
        File[] manFileList = manfile.listFiles();
        for (int i = 0; i < manFileList.length; i++) {
            String name = manFileList[i].getName();
            String[] s = name.split(" ");
            String houzui = name.substring(name.lastIndexOf("."));
            String fileName = "53-" + s[0] + houzui;
            File dest = new File("C:\\Users\\东岳\\Desktop\\产品图片\\img\\out\\" + fileName);
            manFileList[i].renameTo(dest);
        }
    }

    public static String str2Hex(String str, String chartsetName) throws UnsupportedEncodingException {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes(chartsetName);
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    /**
     * 计算两个日期是否相差多少月
     *
     * @param date1     日期1
     * @param date2     日期2
     * @param different 相差月份
     */
    public static boolean dateDifferenceMonth(LocalDateTime date1, LocalDateTime date2, int different) {
        return ChronoUnit.MONTHS.between(date1.toLocalDate(), date2.toLocalDate()) == different;
    }

    /**
     * 计算两个日期是否相差多少天
     *
     * @param date1     日期1
     * @param date2     日期2
     * @param different 相差天数
     */
    public static boolean dateDifferenceDay(LocalDateTime date1, LocalDateTime date2, long different) {
        return ChronoUnit.DAYS.between(date1.toLocalDate(), date2.toLocalDate()) == different;
    }


    /**
     * 两位小数金额校验
     *
     * @param amount 金额
     * @return
     */
    public static boolean judgeTwoDecimal(BigDecimal amount) {
        boolean flag = false;
        try {
            if (amount != null) {
                String source = amount.toString();
                // 判断是否是整数或者是携带一位或者两位的小数
                Pattern pattern = compile("^[+]?([0-9]+(.[0-9]{1,2})?)$");
                if (pattern.matcher(source).matches()) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return flag;
    }

    /**
     * 将bigDecimal格式化 取整或保留小数
     * <p>
     * BigDecimal 10.00 --> Str 10
     * BigDecimal 10.01 --> Str 10.01
     *
     * @param plantArea
     * @return
     */
    public static String bigDecimalRoundOrFractional(BigDecimal plantArea) {
        if (plantArea == null) {
            return "0";
        }
        boolean ret;
        try {
            plantArea.toBigIntegerExact();
            ret = true;
        } catch (ArithmeticException ex) {
            ret = false;
        }
        return ret ? plantArea.setScale(0).toString() : plantArea.toString();

    }


    public static void main(String[] args) throws UnsupportedEncodingException {
//        LocalDateTime now = LocalDateTime.now();
//        //查询供应时间为15天以后的资源
//        LocalDateTime localDateTime = now.plusDays(-15);
//        String nowDate = OtherUtil.formatLocalDateTime("MM月dd日", localDateTime);
//        LocalDateTime now1 = LocalDateTime.now();
//        //查询供应时间为15天以后的资源
//        LocalDateTime localDateTime1 = now1.plusDays(15);
//        String nowDate1 = OtherUtil.formatLocalDateTime("MM月dd日", localDateTime1);

//        Integer idCount =0;
//        for (int i = 1; i < 113; i++) {
//            //System.out.println("INSERT INTO `xfarmer`.`t_file_info` (`id`, `name`, `url`, `type`, `user_type`, `create_time`, `status`, `create_user_uid`) VALUES ("+(-400-idCount)+", '/landmarkinit/51-"+i+".jpg', 'https://qiniu.xfarmer.com/landmarkinit/51-"+i+".jpg', 0, 0, '2023-01-04 11:32:04', 0, 1);");
//            idCount++;
//        }
//        for (int i = 1; i < 67; i++) {
//            //System.out.println("INSERT INTO `xfarmer`.`t_file_info` (`id`, `name`, `url`, `type`, `user_type`, `create_time`, `status`, `create_user_uid`) VALUES ("+(-400-idCount)+", '/landmarkinit/52-"+i+".jpg', 'https://qiniu.xfarmer.com/landmarkinit/52-"+i+".jpg', 0, 0, '2023-01-04 11:32:04', 0, 1);");
//            idCount++;
//        }
//        for (int i = 1; i < 11; i++) {
//            //System.out.println("INSERT INTO `xfarmer`.`t_file_info` (`id`, `name`, `url`, `type`, `user_type`, `create_time`, `status`, `create_user_uid`) VALUES ("+(-400-idCount)+", '/landmarkinit/53-"+i+".jpg', 'https://qiniu.xfarmer.com/landmarkinit/53-"+i+".jpg', 0, 0, '2023-01-04 11:32:04', 0, 1);");
//            idCount++;
//        }
//        List<String> months = List.of("1","4","5");
//        StringBuilder month = new StringBuilder();
//        List<String> xx = new ArrayList<>();
//        for (int i = 0; i < months.size(); i++) {
//            if(i==months.size()-1){
//                if(StringUtil.isNull(month.toString())){
//                    month.append(months.get(i));
//                    xx.add(month.toString());
//                }else if(StringUtil.isNotNull(month.toString())&&month.toString().contains("-")){
//                    month.append(months.get(i));
//                    xx.add(month.toString());
//                }else{
//                    xx.add(month.toString());
//                    month = new StringBuilder();
//                    month.append(months.get(i));
//                    xx.add(month.toString());
//                }
//            }else{
//                if(Integer.valueOf(months.get(i))+1!=Integer.valueOf(months.get(i+1))){
//                    month.append(months.get(i));
//                    xx.add(month.toString());
//                    month = new StringBuilder();
//                }else{
//                    if(StringUtil.isNotNull(month.toString())){
//                        if(!month.toString().contains("-")){
//                            month.append("-");
//                        }
//                    }else{
//                        month.append(months.get(i)).append("-");
//                    }
//                }
//            }
//        }
//        System.out.println(xx.toString());


        String pathBase = "C:\\Users\\东岳\\Desktop\\新建文件夹";
        File manfile = new File(pathBase);
        File[] manFileList = manfile.listFiles();
        for (File file : manFileList) {
            if (!file.isFile()) {
                String name = file.getName();
                File imgFile = new File(pathBase + "\\" + name);
                File[] imgFileList = imgFile.listFiles();
                Arrays.sort(imgFileList, (o1, o2) -> {
                    String[] o1name = o1.getName().split(" ");
                    String[] o2Name = o2.getName().split(" ");
                    return Integer.valueOf(o1name[0]).compareTo(Integer.valueOf(o2Name[0]));
                });
                for (int i = 0; i < imgFileList.length; i++) {
                    String imgName = imgFileList[i].getName();
                    String houzui = imgName.substring(imgName.lastIndexOf("."));
                    houzui = houzui.replaceAll(".jpeg", ".jpg");
                    String fileName = name + imgName.substring(0, imgName.lastIndexOf(".")) + houzui;
                    System.out.println("https://qiniu.xfarmer.com/landmark/230427/" + fileName);
//                    File dest = new File("d:/landmarkImg" + "/" + fileName);
//                    imgFileList[i].renameTo(dest);
//                    System.out.println(dest.getName());
                }
            }
        }
//
//
////        String xx = "102.112863,26.893439";
////        String[] split = xx.split(",");
////        String lng = split[0];
////        String lat = split[1];
////        Random random = new Random();
//////        for (int i = 0; i < 100; i++) {
////            Double lngD = Double.valueOf(lng);
////            Double latD = Double.valueOf(lat);
////            double v = random.nextDouble();
////            boolean b = random.nextBoolean();
//            if(b){
//                lngD = lngD+(v/10);
//            }else{
//                lngD = lngD-(v/10);
//            }
//            boolean b1 = random.nextBoolean();
//            if(b1){
//                latD = latD+(v/10);
//            }else{
//                latD = latD-(v/10);
//            }
////        }
//        String string = lngD.toString();
//        String string1 = latD.toString();
//        System.out.println(string+","+string1);

//        File womanfile = new File("d:/womanheadimg");
//
//
//        File file = new File("d:/headimg");
        //updateFileName();
    }


}
