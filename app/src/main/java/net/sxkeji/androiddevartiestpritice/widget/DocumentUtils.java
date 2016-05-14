package net.sxkeji.androiddevartiestpritice.widget;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * 导出文件工具
 * Created by zhangshixin on 5/6/2016.
 */
public class DocumentUtils {
    private final String TAG = "DocumentUtils";
    private Configuration configuration = null;

    public DocumentUtils() {
        configuration = new Configuration(Configuration.VERSION_2_3_24);
        configuration.setDefaultEncoding("utf-8");
    }

    public void createDoc() {
        //要填入模本的数据文件
        Map dataMap = new HashMap();
        getData(dataMap);
        //设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
        //这里我们的模板是放在com.havenliu.document.template包下面
        configuration.setClassForTemplateLoading(this.getClass(), "../../../../../main/assets");
        //设置对象包装器
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        //设置异常处理器
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        Template t = null;
        try {
//test.ftl为要装载的模板
            t = configuration.getTemplate("diarymodel.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
//输出文档路径及名称
        File outFile = new File("/sdcard/365diary/outFile.doc");
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            t.process(dataMap, out);
            Log.e(TAG, "tempate out success");
        } catch (TemplateException e) {
            Log.e(TAG, "tempate out failed " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "tempate out failed " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 注意dataMap里存放的数据Key值要与模板中的参数相对应
     *
     * @param dataMap
     */
    private void getData(Map dataMap) {
        dataMap.put("date", "2222-02-02");
        dataMap.put("title", "zsx");
        dataMap.put("content", "zsxshuai");
    }
}