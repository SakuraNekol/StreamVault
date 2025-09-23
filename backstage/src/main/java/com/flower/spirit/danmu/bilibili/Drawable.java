package com.flower.spirit.danmu.bilibili;
/**
 * 可绘制对象类
 * 实现来自 
 *    danmu2ass
 *    https://github.com/gwy15/danmu2ass
 */
public class Drawable {
    /**
     * 弹幕数据
     */
    private Danmu danmu;
    
    /**
     * 弹幕绘制持续时间
     */
    private double duration;
    
    /**
     * 弹幕样式名称
     */
    private String styleName;
    
    /**
     * 绘制效果
     */
    private DrawEffect effect;
    
    public Drawable(Danmu danmu, double duration, String styleName, DrawEffect effect) {
        this.danmu = danmu;
        this.duration = duration;
        this.styleName = styleName;
        this.effect = effect;
    }
    
    /**
     * 转换为ASS格式的对话行
     * @return ASS对话行字符串
     */
    public String toAssDialogue() {
        String startTime = formatTime(danmu.getTimelineS());
        String endTime = formatTime(danmu.getTimelineS() + duration);
        String effectStr = effect.toAssString();
        String colorStr = String.format("\\c&H%02x%02x%02x&", 
            danmu.getRgb()[2], danmu.getRgb()[1], danmu.getRgb()[0]); // BGR格式
        String escapedText = escapeText(danmu.getContent());
        
        return String.format(
            "Dialogue: 2,%s,%s,%s,,0,0,0,,{%s%s}%s",
            startTime, endTime, styleName, effectStr, colorStr, escapedText
        );
    }
    
    /**
     * 格式化时间为ASS时间格式
     * @param timeInSeconds 时间（秒）
     * @return ASS时间格式字符串 (H:MM:SS.CC)
     */
    private String formatTime(double timeInSeconds) {
        int totalSeconds = (int) Math.floor(timeInSeconds);
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        double seconds = timeInSeconds - (hours * 3600) - (minutes * 60);
        
        return String.format("%d:%02d:%05.2f", hours, minutes, seconds);
    }
    
    /**
     * 转义文本中的特殊字符
     * @param text 原始文本
     * @return 转义后的文本
     */
    private String escapeText(String text) {
        if (text == null) {
            return "";
        }
        text = text.trim();
        return text.replace("\n", "\\N");
    }
    
    // Getters and Setters
    public Danmu getDanmu() {
        return danmu;
    }
    
    public void setDanmu(Danmu danmu) {
        this.danmu = danmu;
    }
    
    public double getDuration() {
        return duration;
    }
    
    public void setDuration(double duration) {
        this.duration = duration;
    }
    
    public String getStyleName() {
        return styleName;
    }
    
    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }
    
    public DrawEffect getEffect() {
        return effect;
    }
    
    public void setEffect(DrawEffect effect) {
        this.effect = effect;
    }
}