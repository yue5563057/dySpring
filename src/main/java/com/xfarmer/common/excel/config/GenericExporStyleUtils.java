package com.xfarmer.common.excel.config;

import com.alibaba.excel.constant.OrderConstant;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出样式
 */
@Data
public class GenericExporStyleUtils implements CellWriteHandler {

    @Override
    public int order() {
        return OrderConstant.DEFINE_STYLE;
    }

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        setColumnWidth(context);
        if (context.getHead() == null) {
            return;
        }
        if (Boolean.TRUE.equals(context.getHead())) {
            setHeadCellStyle(context);
        } else {
            setContentCellStyle(context);
        }
    }

    private static final int MAX_COLUMN_WIDTH = 255;

    private final Map<Integer, Map<Integer, Integer>> cache = MapUtils.newHashMapWithExpectedSize(8);

    protected void setColumnWidth(CellWriteHandlerContext context) {
        setColumnWidth(context.getWriteSheetHolder(), context.getCellDataList(), context.getCell()
                , context.getHead());
    }

    /**
     * 自动宽度
     *
     **/
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList,
                                  Cell cell, Boolean isHead) {
        boolean needSetWidth = isHead || (cellDataList!=null&&!cellDataList.isEmpty());
        if (!needSetWidth) {
            return;
        }
        Map<Integer, Integer> maxColumnWidthMap = cache.computeIfAbsent(writeSheetHolder.getSheetNo(), k -> new HashMap<>(16));
        Integer columnWidth = dataLength(cellDataList, cell, isHead);
        if (columnWidth < 0) {
            return;
        }
        if (columnWidth > MAX_COLUMN_WIDTH) {
            columnWidth = MAX_COLUMN_WIDTH;
        }
        Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
        if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
            maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
            writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
        }
    }

    private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        if (Boolean.TRUE.equals(isHead)) {
            return cell.getStringCellValue().getBytes().length;
        }
        WriteCellData<?> cellData = cellDataList.get(0);
        CellDataTypeEnum type = cellData.getType();
        if (type == null) {
            return -1;
        }
        switch (type) {
            case STRING:
                return cellData.getStringValue().getBytes().length;
            case BOOLEAN:
                return cellData.getBooleanValue().toString().getBytes().length;
            case NUMBER:
                return cellData.getNumberValue().toString().getBytes().length;
            default:
                return -1;
        }
    }

    /**
     * 头的策略
     *
     **/
    protected void setHeadCellStyle(CellWriteHandlerContext context) {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为红色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        WriteFont headWriteFont = new WriteFont();
        //字体大小
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteCellStyle.setWriteFont(headWriteFont);
        //设置文本是否应换行
        headWriteCellStyle.setWrapped(false);
        //如果文本太长，控制单元格是否应自动调整大小以缩小以适应
        headWriteCellStyle.setShrinkToFit(true);

        if (context.getFirstCellData() == null) {
            return;
        }
        WriteCellData<?> cellData = context.getFirstCellData();
        WriteCellStyle.merge(headWriteCellStyle, cellData.getOrCreateStyle());
    }

    /**
     * 内容的策略
     *
     **/
    protected void setContentCellStyle(CellWriteHandlerContext context) {
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
//        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND)
//        // 背景绿色
//        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex())
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 14);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        //设置文本是否应换行
        contentWriteCellStyle.setWrapped(false);
        //如果文本太长，控制单元格是否应自动调整大小以缩小以适应
        contentWriteCellStyle.setShrinkToFit(true);
        List<WriteCellStyle> contentWriteCellStyleList = ListUtils.newArrayList(contentWriteCellStyle);
        if (context.getFirstCellData() == null ||  (contentWriteCellStyleList!=null&&!contentWriteCellStyleList.isEmpty())) {
            return;
        }
        WriteCellData<?> cellData = context.getFirstCellData();
        if (context.getRelativeRowIndex() == null || context.getRelativeRowIndex() <= 0) {
            WriteCellStyle.merge(contentWriteCellStyleList.get(0), cellData.getOrCreateStyle());
        } else {
            WriteCellStyle.merge(
                    contentWriteCellStyleList.get(context.getRelativeRowIndex() % contentWriteCellStyleList.size()),
                    cellData.getOrCreateStyle());
        }
    }
}
