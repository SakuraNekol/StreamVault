package com.flower.spirit.danmu.bilibili;

import com.flower.spirit.danmu.bilibili.DanmakuSeg.DanmakuElem;

/**
 * 弹幕数据模型
 * 实现来自 
 *    danmu2ass
 *    https://github.com/gwy15/danmu2ass
 */
public class Danmu {
    /**
     * 弹幕出现的时间点（秒）
     */
    private double timelineS;
    
    /**
     * 弹幕内容
     */
    private String content;
    
    /**
     * 弹幕类型
     */
    private DanmuType type;
    
    /**
     * 字体大小（实际使用画布配置中的字体大小）
     */
    private int fontsize;
    
    /**
     * RGB颜色值
     */
    private int[] rgb;
    
    public Danmu() {
        this.type = DanmuType.FLOAT;
        this.rgb = new int[]{255, 255, 255}; // 默认白色
        this.content = "";
        this.fontsize = 25;
    }
    
    public Danmu(double timelineS, String content, DanmuType type, int fontsize, int[] rgb) {
        this.timelineS = timelineS;
        this.content = content;
        this.type = type;
        this.fontsize = fontsize;
        this.rgb = rgb;
    }
    
    /**
     * 计算弹幕的像素长度
     * 汉字算一个全宽，英文算2/3宽
     * @param config 画布配置
     * @return 弹幕的像素长度
     */
    public double calculateLength(CanvasConfig config) {
        int charWidthSum = 0;
        for (char ch : content.toCharArray()) {
            if (ch <= 127) { // ASCII字符
                charWidthSum += 2;
            } else { // 非ASCII字符（如中文）
                charWidthSum += 3;
            }
        }
        
        double pts = (double) config.getFontSize() * charWidthSum / 3.0;
        return pts * config.getWidthRatio();
    }
    
    /**
     * 从B站弹幕的DanmakuElem转换
     * @param elem B站弹幕元素
     * @return 转换后的弹幕对象
     */
    public static Danmu fromDanmakuElem(DanmakuElem elem) {
        Danmu danmu = new Danmu();
        danmu.timelineS = elem.getProgress() / 1000.0; // 毫秒转秒
        danmu.content = elem.getContent();
        danmu.type = DanmuType.fromXmlValue(elem.getMode());
        if (danmu.type == null) {
            danmu.type = DanmuType.FLOAT; // 默认为滚动弹幕
        }
        danmu.fontsize = elem.getFontsize();
        
        // 解析颜色
        int color = (int) elem.getColor();
        danmu.rgb = new int[]{
            (color >> 16) & 0xFF, // R
            (color >> 8) & 0xFF,  // G
            color & 0xFF          // B
        };
        
        return danmu;
    }
    
    // Getters and Setters
    public double getTimelineS() {
        return timelineS;
    }
    
    public void setTimelineS(double timelineS) {
        this.timelineS = timelineS;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public DanmuType getType() {
        return type;
    }
    
    public void setType(DanmuType type) {
        this.type = type;
    }
    
    public int getFontsize() {
        return fontsize;
    }
    
    public void setFontsize(int fontsize) {
        this.fontsize = fontsize;
    }
    
    public int[] getRgb() {
        return rgb;
    }
    
    public void setRgb(int[] rgb) {
        this.rgb = rgb;
    }
}