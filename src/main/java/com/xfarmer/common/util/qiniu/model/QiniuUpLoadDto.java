package com.xfarmer.common.util.qiniu.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "七牛上传返回")
public class QiniuUpLoadDto {

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "自定义文件名称")
    private String fileName;

}
