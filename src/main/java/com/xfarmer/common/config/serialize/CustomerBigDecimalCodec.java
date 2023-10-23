package com.xfarmer.common.config.serialize;

import com.alibaba.fastjson.serializer.*;

import java.text.DecimalFormat;

/**
 * @author 东岳
 */
public class CustomerBigDecimalCodec extends BigDecimalCodec implements ContextObjectSerializer {

    public static final CustomerBigDecimalCodec BIG_DECIMAL_CODEC = new CustomerBigDecimalCodec();

    @Override
    public void write(JSONSerializer serializer, Object object, BeanContext context) {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeString("");
            return;
        }
        String format = context.getFormat();
        DecimalFormat decimalFormat = new DecimalFormat(format);
        out.writeString(decimalFormat.format(object));
    }
}
