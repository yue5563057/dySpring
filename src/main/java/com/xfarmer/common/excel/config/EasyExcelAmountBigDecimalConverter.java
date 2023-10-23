package com.xfarmer.common.excel.config;

import com.alibaba.excel.converters.bigdecimal.BigDecimalNumberConverter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
/**
 * @author 东岳
 */
public class EasyExcelAmountBigDecimalConverter extends BigDecimalNumberConverter {

    @Override
    public BigDecimal convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return cellData.getNumberValue();
    }


    @Override
    public WriteCellData<?> convertToExcelData(BigDecimal value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        value = value.setScale(2, RoundingMode.HALF_UP);
        return NumberUtils.formatToCellData(value, contentProperty);
    }

}
