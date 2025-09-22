package com.flower.spirit.danmu.bilibili;
import java.io.*;
import java.util.*;

import com.flower.spirit.danmu.bilibili.DanmakuSeg.DanmakuElem;

/**
 * 弹幕转ASS转换器
 * 主要的转换逻辑类，整合所有组件
 */
public class DanmuToAssConverter {
    
    /**
     * 将弹幕列表转换为ASS文件
     * @param danmakuElems B站弹幕元素列表
     * @param outputFile 输出ASS文件
     * @param title ASS文件标题
     * @param config 画布配置（可选，使用默认配置如果为null）
     * @throws IOException IO异常
     */
    public static void convertToAss(List<DanmakuElem> danmakuElems, File outputFile, 
                                   String title, CanvasConfig config) throws IOException {
        if (config == null) {
            config = new CanvasConfig();
        }
        
//        System.out.println("开始转换弹幕，总数：" + danmakuElems.size());
        
        // 转换为Danmu对象并按时间排序
        List<Danmu> danmus = new ArrayList<>();
        for (DanmakuElem elem : danmakuElems) {
            Danmu danmu = Danmu.fromDanmakuElem(elem);
            if (danmu != null && danmu.getContent() != null && !danmu.getContent().trim().isEmpty()) {
                danmus.add(danmu);
            }
        }
        
        // 按时间排序
        danmus.sort(Comparator.comparingDouble(Danmu::getTimelineS));
//        System.out.println("有效弹幕数：" + danmus.size());
        
        // 创建画布和ASS写入器
        Canvas canvas = new Canvas(config);
        
        try (AssWriter assWriter = new AssWriter(outputFile, title, config)) {
            for (Danmu danmu : danmus) {
                Drawable drawable = canvas.draw(danmu);
                if (drawable != null) {
                    assWriter.write(drawable);
                }
            }
            assWriter.flush();
        }
    }
    
    /**
     * 使用默认配置转换弹幕
     * @param danmakuElems B站弹幕元素列表
     * @param outputFile 输出ASS文件
     * @param title ASS文件标题
     * @throws IOException IO异常
     */
    public static void convertToAss(List<DanmakuElem> danmakuElems, File outputFile, String title) 
            throws IOException {
        convertToAss(danmakuElems, outputFile, title, null);
    }
    
    /**
     * 创建默认的画布配置
     * @return 默认画布配置
     */
    public static CanvasConfig createDefaultConfig() {
        CanvasConfig config = new CanvasConfig();
        config.setDuration(8.0);
        config.setWidth(1920);
        config.setHeight(1080);
        config.setFont("Microsoft YaHei");
        config.setFontSize(36);
        config.setWidthRatio(1.0);
        config.setHorizontalGap(20.0);
        config.setLaneSize(40);
        config.setFloatPercentage(0.8);
        config.setBottomPercentage(0.2);
        config.setOpacity(255);
        config.setBold(false);
        config.setOutline(2.0);
        config.setTimeOffset(0.0);
        return config;
    }
    
    /**
     * 创建高清配置（1080p）
     * @return 高清画布配置
     */
    public static CanvasConfig createHDConfig() {
        CanvasConfig config = createDefaultConfig();
        config.setWidth(1920);
        config.setHeight(1080);
        config.setFontSize(36);
        config.setLaneSize(40);
        return config;
    }
    
    /**
     * 创建4K配置
     * @return 4K画布配置
     */
    public static CanvasConfig create4KConfig() {
        CanvasConfig config = createDefaultConfig();
        config.setWidth(3840);
        config.setHeight(2160);
        config.setFontSize(72);
        config.setLaneSize(80);
        return config;
    }
    
    /**
     * 创建移动端配置（720p）
     * @return 移动端画布配置
     */
    public static CanvasConfig createMobileConfig() {
        CanvasConfig config = createDefaultConfig();
        config.setWidth(1280);
        config.setHeight(720);
        config.setFontSize(24);
        config.setLaneSize(28);
        return config;
    }
}