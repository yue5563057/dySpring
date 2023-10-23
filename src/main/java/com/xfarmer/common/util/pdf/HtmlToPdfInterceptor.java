package com.xfarmer.common.util.pdf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HtmlToPdfInterceptor extends Thread {
    private InputStream is;

    public HtmlToPdfInterceptor(InputStream is) {
        this.is = is;
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                // System.out.println(line.toString()); //输出内容
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
