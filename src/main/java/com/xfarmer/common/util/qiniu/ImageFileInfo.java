package com.xfarmer.common.util.qiniu;

import lombok.Data;

@Data
public class ImageFileInfo {
    /**
     * 文件大小，单位：Bytes
     */
    private Long size;
    /**
     * 图片类型，如png、jpeg、gif、bmp等。
     */
    private String format;
    /**
     * 图片宽度，单位：像素(px)。
     */
    private Integer width;
    /**
     * 图片高度，单位：像素(px)。
     */
    private Integer height;
    /**
     * 彩色空间，如palette16、ycbcr等。
     */
    private String colorModel;
    /***/
    private String orientation;
    /**
     * 帧数，gif 图片会返回此项。
     */
    private String frameNumber;
}
