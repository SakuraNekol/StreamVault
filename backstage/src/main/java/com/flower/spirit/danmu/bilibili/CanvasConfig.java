package com.flower.spirit.danmu.bilibili;
/**
 * 画布配置类
 */
public class CanvasConfig {
    /**
     * 弹幕持续时间（秒）
     */
    private double duration = 8.0;
    
    /**
     * 画布宽度
     */
    private int width = 1920;
    
    /**
     * 画布高度
     */
    private int height = 1080;
    
    /**
     * 字体名称
     */
    private String font = "黑体";
    
    /**
     * 字体大小
     */
    private int fontSize = 36;
    
    /**
     * 宽度比例
     */
    private double widthRatio = 1.0;
    
    /**
     * 两条弹幕之间最小的水平距离
     */
    private double horizontalGap = 20.0;
    
    /**
     * 车道大小
     */
    private int laneSize = 40;
    
    /**
     * 屏幕上滚动弹幕最多高度百分比
     */
    private double floatPercentage = 0.8;
    
    /**
     * 屏幕上底部弹幕最多高度百分比
     */
    private double bottomPercentage = 0.2;
    
    /**
     * 透明度（0-255，255为完全不透明）
     */
    private int opacity = 255;
    
    /**
     * 是否加粗
     */
    private boolean bold = false;
    
    /**
     * 描边宽度
     */
    private double outline = 2.0;
    
    /**
     * 时间轴偏移
     */
    private double timeOffset = 0.0;
    
    public CanvasConfig() {
    }
    
    /**
     * 生成ASS样式字符串列表
     * 格式: Name, Fontname, Fontsize, PrimaryColour, SecondaryColour, OutlineColour, BackColour, 
     *       Bold, Italic, Underline, StrikeOut, ScaleX, ScaleY, Spacing, Angle, BorderStyle, 
     *       Outline, Shadow, Alignment, MarginL, MarginR, MarginV, Encoding
     * @return ASS样式字符串列表
     */
    public String[] generateAssStyles() {
        // 计算透明度值 (opacity范围0-255，转换为ASS格式的透明度)
        String primaryColour = String.format("&H%02xFFFFFF", 255 - opacity);
        // 描边颜色使用更柔和的透明度，减少"阴影"感
        String outlineColour = String.format("&H%02x000000", Math.min(255 - opacity + 50, 255));
        
        return new String[]{
            String.format(
                "Style: Float,%s,%d,%s,&H00FFFFFF,%s,&H00000000," +
                "%d, 0, 0, 0, 100, 100, 0.00, 0.00, 1, " +
                "%.1f, 0, 7, 0, 0, 0, 1",
                font, fontSize, primaryColour, outlineColour, bold ? 1 : 0, Math.max(outline * 0.8, 1.0)
            ),
            String.format(
                "Style: Bottom,%s,%d,%s,&H00FFFFFF,%s,&H00000000," +
                "%d, 0, 0, 0, 100, 100, 0.00, 0.00, 1, " +
                "%.1f, 0, 2, 0, 0, 0, 1",
                font, fontSize, primaryColour, outlineColour, bold ? 1 : 0, Math.max(outline * 0.8, 1.0)
            ),
            String.format(
                "Style: Top,%s,%d,%s,&H00FFFFFF,%s,&H00000000," +
                "%d, 0, 0, 0, 100, 100, 0.00, 0.00, 1, " +
                "%.1f, 0, 8, 0, 0, 0, 1",
                font, fontSize, primaryColour, outlineColour, bold ? 1 : 0, Math.max(outline * 0.8, 1.0)
            )
        };
    }
    
    
    // Getters and Setters
    public double getDuration() {
        return duration;
    }
    
    public void setDuration(double duration) {
        this.duration = duration;
    }
    
    public int getWidth() {
        return width;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public String getFont() {
        return font;
    }
    
    public void setFont(String font) {
        this.font = font;
    }
    
    public int getFontSize() {
        return fontSize;
    }
    
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
    
    public double getWidthRatio() {
        return widthRatio;
    }
    
    public void setWidthRatio(double widthRatio) {
        this.widthRatio = widthRatio;
    }
    
    public double getHorizontalGap() {
        return horizontalGap;
    }
    
    public void setHorizontalGap(double horizontalGap) {
        this.horizontalGap = horizontalGap;
    }
    
    public int getLaneSize() {
        return laneSize;
    }
    
    public void setLaneSize(int laneSize) {
        this.laneSize = laneSize;
    }
    
    public double getFloatPercentage() {
        return floatPercentage;
    }
    
    public void setFloatPercentage(double floatPercentage) {
        this.floatPercentage = floatPercentage;
    }
    
    public double getBottomPercentage() {
        return bottomPercentage;
    }
    
    public void setBottomPercentage(double bottomPercentage) {
        this.bottomPercentage = bottomPercentage;
    }
    
    public int getOpacity() {
        return opacity;
    }
    
    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }
    
    public boolean isBold() {
        return bold;
    }
    
    public void setBold(boolean bold) {
        this.bold = bold;
    }
    
    public double getOutline() {
        return outline;
    }
    
    public void setOutline(double outline) {
        this.outline = outline;
    }
    
    public double getTimeOffset() {
        return timeOffset;
    }
    
    public void setTimeOffset(double timeOffset) {
        this.timeOffset = timeOffset;
    }
}