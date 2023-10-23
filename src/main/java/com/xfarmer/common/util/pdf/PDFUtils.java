package com.xfarmer.common.util.pdf;


import com.lowagie.text.pdf.BaseFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author 东岳
 */
public class PDFUtils {

    private static final Logger logger = LoggerFactory.getLogger(PDFUtils.class);

    /**
     * 把URL转换为PDF
     *
     * @return
     * @throws Exception
     */
    public static File htmlToPdf2(String outputFile, String url)
            throws Exception {
        File outFile = new File(outputFile);
        if (!outFile.exists()) {
            outFile.getParentFile().mkdirs();
        }
        OutputStream os = new FileOutputStream(outputFile);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(url);
        String fontPath = PDFUtils.class.getClassLoader().getResource("") + "simsun.ttc";
        // 解决中文支持问题
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.layout();
        renderer.createPDF(os);
        os.flush();
        os.close();

        return outFile;
    }

    private static final String TO_PDF_TOOL = "D:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltopdf.exe";

    private static final String CONVERSION_PLUGSTOOL_PATH_LINUX = "sudo /usr/local/bin/wkhtmltopdf";

    private static final String OS_NAME = "os.name";
    private static final String OS_NAME_WINDOWS = "Windows";

    public static File htmlToPdf(String srcPath, String destPath) {
        File file = new File(destPath);
        File parent = file.getParentFile();
        // 如果pdf保存路径不存在，则创建路径
        if (!parent.exists()) {
            parent.mkdirs();
        }
        String toolPath = "";
        StringBuilder cmd = new StringBuilder();
        if (!System.getProperty(OS_NAME).contains(OS_NAME_WINDOWS)) {
            // 非windows 系统
            toolPath = CONVERSION_PLUGSTOOL_PATH_LINUX;
        } else {
            toolPath = TO_PDF_TOOL;
        }
        cmd.append(toolPath);
        cmd.append(" ");
        // 设置页面上边距 (default 10mm)
        cmd.append("  --margin-top 3cm ");
        //cmd.append(" --header-html  file:///" + "https://blog.csdn.net/x6582026/article/details/53835835");// (添加一个HTML页眉,后面是网址)
        // (设置页眉和内容的距离,默认0)
        cmd.append(" --header-spacing 5 ");
        //设置在中心位置的页脚内容
        cmd.append(" --footer-center 第[page]页／共[topage]页");
        //cmd.append(" --footer-html  file:///" + "https://blog.csdn.net/x6582026/article/details/53835835");// (添加一个HTML页脚,后面是网址)
        // * 显示一条线在页脚内容上)
        cmd.append(" --footer-line");
        // (设置页脚和内容的距离)
        cmd.append(" --footer-spacing 5 ");
        // (设置页脚和内容的距离)
//        cmd.append(" --page-width 800px ");
        cmd.append(srcPath);
        cmd.append(" ");

        cmd.append(destPath);
        boolean result = true;
        try {
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            try {
                InputStreamReader isr = new InputStreamReader(proc.getErrorStream(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    // System.out.println(line.toString()); //输出内容
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                InputStreamReader isr = new InputStreamReader(proc.getInputStream(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    // System.out.println(line.toString()); //输出内容
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            proc.waitFor();
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return file;
    }


}
