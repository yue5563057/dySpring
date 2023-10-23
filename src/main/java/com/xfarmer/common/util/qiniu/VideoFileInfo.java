package com.xfarmer.common.util.qiniu;

import lombok.Data;

@Data
public class VideoFileInfo {


    /**
     * 视频宽度
     */
    private Integer width;
    /**
     * 视频高度
     */
    private Integer height;
    /***/
    private Integer codecType;
    /**
     * 文件总时间
     */
    private Double duration;
    /**
     * 文件大小
     */
    private Long size;

}
