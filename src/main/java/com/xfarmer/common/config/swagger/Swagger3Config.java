package com.xfarmer.common.config.swagger;

import com.xfarmer.common.constant.SystemConst;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Swagger3Config {
    /**
     * 文档基本信息
     */
    private ApiInfo apiInfo() {
        System.out.println();
        return new ApiInfoBuilder()
                .title(SystemConst.swaggerTitle)
                .description(SystemConst.swaggerDescription)
                .version("3.0")
                .build();
    }


    /**
     * 全局通用属性（摘要）配置
     */
    @Bean
    public Docket api() {
        System.out.println();
        return new Docket(DocumentationType.OAS_30)
                //应用文档基本信息
                .apiInfo(apiInfo())
                .select()
                // swagger扫描路径
                .apis(RequestHandlerSelectors.basePackage(SystemConst.swaggerApiPath))
                // 应用于包下所有路径
                .paths(PathSelectors.any())
                .build()
                // 忽略此类型输入参数（viewResovler全局添加的）
//                .ignoredParameterTypes(User.class, AdminUser.class)
                // 设置全局通用参数
                .globalRequestParameters(getGlobalRequestParameters())
                ;
    }

    /**
     * 项目通用参数，添加全局参数——登录认证token（若无可省略）
     */
    private List<RequestParameter> getGlobalRequestParameters() {
        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(new RequestParameterBuilder()
                // 参数名
                .name("token")
                //参数类型
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                // 描述
                .description("登录认证token")
                //是否必传
                .required(false)
                //请求头中的参数，其它类型可以点进ParameterType类中查看
                .in(ParameterType.HEADER)
                .build());
        parameters.add(new RequestParameterBuilder().name("platFrom")
                .query(q -> q.model(m -> m.scalarModel(ScalarType.INTEGER)))
                .description("登录平台类型")
                .required(false)
                .in(ParameterType.HEADER)
                .build()
        );
        parameters.add(new RequestParameterBuilder().name("clientId")
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .description("登录平台类型")
                .required(false)
                .in(ParameterType.HEADER)
                .build()
        );
        return parameters;
    }

}
