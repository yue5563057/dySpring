package com.xfarmer.common.util.qiniu;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.xfarmer.common.constant.RedisKeyConst;
import com.xfarmer.common.constant.SystemConst;
import com.xfarmer.common.constant.URLCodeConst;
import com.xfarmer.common.util.ApiResponse;
import com.xfarmer.common.util.HttpTool;
import com.xfarmer.common.util.OtherUtil;
import com.xfarmer.common.util.StringUtil;
import com.xfarmer.common.util.qiniu.model.QiniuUpLoadDto;
import com.xfarmer.common.util.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


@Component
public class QiniuUtil {
    @Value("${spring.dyBase.qiniu.accessKey}")
    private String accessKey;

    @Value("${spring.dyBase.qiniu.secretKey}")
    private String secretKey;

    @Value("${spring.dyBase.qiniu.bucket}")
    private String bucket;

    @Value("${spring.dyBase.qiniu.path}")
    private String path;

    public static final Integer FILE_TYPE_IMAGE = 1;
    public static final Integer FILE_TYPE_VIDEO = 2;
    public static final Integer FILE_TYPE_OTHER = 3;


    public String getPath() {
        return this.path;
    }

    /**
     * 将图片上传到七牛云
     *
     * @param file
     * @param fileName 保存在空间中的名字，如果为空会使用文件的hash值为文件名
     * @return
     */
    public String uploadImg(InputStream file, String fileName, String bucket) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration();
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(file, fileName, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
                String returnPath = path + "/" + putRet.key;
                return returnPath;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "";
    }


    /**
     * 将图片上传到七牛云
     *
     * @param file
     * @param fileName 保存在空间中的名字，如果为空会使用文件的hash值为文件名
     * @return
     */
    public String uploadImg(File file, String fileName, String bucket) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration();
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(file, fileName, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
                String returnPath = path + "/" + putRet.key;
                return returnPath;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "";
    }


    public FileInfo findInfo(String key) {
        Configuration cfg = new Configuration(Region.region0());
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            FileInfo fileInfo = bucketManager.stat(bucket, key);
            return fileInfo;
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
        return null;
    }


    public boolean isAppid(String appid) {
        return this.accessKey.equals(appid);
    }

    public ImageFileInfo findImageInfo(String fileUrl) {
        String s = HttpTool.doGet(fileUrl + "?imageInfo", new HashMap<>());
        if (StringUtil.isNull(s)) {
            return findImageInfo(fileUrl);
        }
        ImageFileInfo imageFileInfo = JSON.parseObject(s, ImageFileInfo.class);
        return imageFileInfo;
    }

    public VideoFileInfo findVideoInfo(String fileUrl) {
        String s = HttpTool.doGet(fileUrl + "?avinfo", new HashMap<>());
        if (StringUtil.isNull(s)) {
            return findVideoInfo(fileUrl);
        }
        JSONObject jsonObject = JSON.parseObject(s);
        JSONObject format = jsonObject.getJSONObject("format");
        Long size = format.getLong("size");
        Double duration = format.getDouble("duration");
        VideoFileInfo videoFileInfo = new VideoFileInfo();
        videoFileInfo.setSize(size);
        videoFileInfo.setDuration(duration);
        return videoFileInfo;
    }

    @Autowired
    RedisUtil redisUtil;

    public ApiResponse<String> findToken(String accessKey) {
        if (StringUtil.isNull(accessKey)) {
            return new ApiResponse<>(URLCodeConst.PARAM_ERROR, "key不能为空！");
        }
        if (!accessKey.equals(this.accessKey)) {
            return new ApiResponse<>(URLCodeConst.PARAM_ERROR, "key错误");
        }
        String token = (String) redisUtil.get("qiniuKey:" + accessKey);
        if (StringUtil.isNull(token)) {
            Auth auth = Auth.create(this.accessKey, secretKey);
            token = auth.uploadToken(bucket);
            redisUtil.set("qiniuKey:" + accessKey, token, RedisKeyConst.MESSAGE_CODE_TIME);
        }
        return new ApiResponse<>(token);
    }

    public ApiResponse<QiniuUpLoadDto> uploadSessionSinge(Integer type, Integer userId, String bucket, Integer fileType) {
        Auth auth = Auth.create(accessKey, secretKey);
        StringMap putPolicy = new StringMap();
        putPolicy.put("callbackUrl", SystemConst.API_HOST + "/qiniu/upload/callbackSession");
//		if(type.equals())
        String newFileName = OtherUtil.getYearMonth() + "/" + SystemConst.PROFILES + "/"
                + userId + OtherUtil.getDateHourMin();
        Map<String, String> param = getBaseParam();
        param.put("userSession", "$(x:userToken)");
        return push7NiuSing(param, fileType, putPolicy, auth, newFileName);
    }

    private Map<String, String> getBaseParam() {
        Map<String, String> param = new HashMap<>();
        param.put("key", "${key}");
        param.put("hash", "$(etag)");
        param.put("bucket", "$(bucket)");
        param.put("fsize", "$(avinfo.format.size)");
        param.put("fileType", "$(x:fileType)");
        param.put("labelId", "$(x:labelId)");
        return param;
    }


    private ApiResponse<QiniuUpLoadDto> push7NiuSing(Map<String, String> param, Integer fileType,
                                                     StringMap putPolicy, Auth auth, String newFileName) {
        if (FILE_TYPE_IMAGE.equals(fileType)) {
            //图片
            param.put("height", "$(imageInfo.height)");
            param.put("width", "$(imageInfo.width)");
            putPolicy.put("callbackBody", JSON.toJSONString(param));
        } else if (FILE_TYPE_VIDEO.equals(fileType)) {
            //视频
            param.put("height", "$(avinfo.video.height)");
            param.put("width", "$(avinfo.video.width)");
            param.put("duration", "$(avinfo.format.duration)");
//			PARAM.put("")
            putPolicy.put("callbackBody", JSON.toJSONString(param));
        } else if (FILE_TYPE_OTHER.equals(fileType)) {
            //文件
            putPolicy.put("callbackBody", JSON.toJSONString(param));
        } else {
            return new ApiResponse<>(URLCodeConst.PARAM_ERROR, "文件类型错误！");
        }
        putPolicy.put("callbackBodyType", "application/json");
        long expireSeconds = 3600;
        String upToken = auth.uploadToken(bucket, null, expireSeconds, putPolicy);
        QiniuUpLoadDto uploadDto = new QiniuUpLoadDto();
        uploadDto.setToken(upToken);
        uploadDto.setFileName(newFileName);
        return new ApiResponse<>(uploadDto);
    }

    public ApiResponse<QiniuUpLoadDto> findSinge(String userId, String bucket, Integer fileType) {

        Auth auth = Auth.create(accessKey, secretKey);
        StringMap putPolicy = new StringMap();
        putPolicy.put("callbackUrl", SystemConst.API_HOST + "/qiniu/upload/callback");
        String newFileName = OtherUtil.getYearMonth() + "/" + SystemConst.PROFILES + "/" + userId + OtherUtil.getDateHourMin();
        Map<String, String> param = getBaseParam();
        param.put("userToken", "$(x:userToken)");
        param.put("userType", "$(x:userType)");
        return push7NiuSing(param, fileType, putPolicy, auth, newFileName);
    }


}
