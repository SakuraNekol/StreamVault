package com.flower.spirit.danmu.bilibili;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * ASS文件写入器
 */
public class AssWriter implements Closeable {
    private BufferedWriter writer;
    private String title;
    private CanvasConfig canvasConfig;
    
    /**
     * 构造函数
     * @param outputStream 输出流
     * @param title ASS文件标题
     * @param canvasConfig 画布配置
     * @throws IOException IO异常
     */
    public AssWriter(OutputStream outputStream, String title, CanvasConfig canvasConfig) throws IOException {
        // 使用大缓冲区提高性能，对于HDD、docker等场景，磁盘IO是瓶颈
        this.writer = new BufferedWriter(
            new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), 
            10 * 1024 * 1024 // 10MB缓冲区
        );
        this.title = title;
        this.canvasConfig = canvasConfig;
        
        initializeAssFile();
    }
    
    /**
     * 构造函数（文件输出）
     * @param file 输出文件
     * @param title ASS文件标题
     * @param canvasConfig 画布配置
     * @throws IOException IO异常
     */
    public AssWriter(File file, String title, CanvasConfig canvasConfig) throws IOException {
        this(new FileOutputStream(file), title, canvasConfig);
    }
    
    /**
     * 初始化ASS文件头部
     * @throws IOException IO异常
     */
    private void initializeAssFile() throws IOException {
        writer.write("[Script Info]\n");
        writer.write("Title: " + title + "\n");
        writer.write("ScriptType: v4.00+\n");
        writer.write("PlayResX: " + canvasConfig.getWidth() + "\n");
        writer.write("PlayResY: " + canvasConfig.getHeight() + "\n");
        writer.write("Aspect Ratio: " + canvasConfig.getWidth() + ":" + canvasConfig.getHeight() + "\n");
        writer.write("Collisions: Normal\n");
        writer.write("WrapStyle: 2\n");
        writer.write("ScaledBorderAndShadow: yes\n");
        writer.write("YCbCr Matrix: TV.601\n");
        writer.write("\n\n");
        
        writer.write("[V4+ Styles]\n");
        writer.write("Format: Name, Fontname, Fontsize, PrimaryColour, SecondaryColour, OutlineColour, BackColour, " +
                    "Bold, Italic, Underline, StrikeOut, ScaleX, ScaleY, Spacing, Angle, BorderStyle, " +
                    "Outline, Shadow, Alignment, MarginL, MarginR, MarginV, Encoding\n");
        
        // 写入样式
        String[] styles = canvasConfig.generateAssStyles();
        for (String style : styles) {
            writer.write(style + "\n");
        }
        writer.write("\n");
        
        writer.write("[Events]\n");
        writer.write("Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text\n");
        
//        System.out.println("ASS文件头部初始化完成");
    }
    
    /**
     * 写入一个可绘制对象到ASS文件
     * @param drawable 可绘制对象
     * @throws IOException IO异常
     */
    public void write(Drawable drawable) throws IOException {
        if (drawable == null) {
            return;
        }
        
        String dialogueLine = drawable.toAssDialogue();
        writer.write(dialogueLine + "\n");
    }
    
    /**
     * 批量写入可绘制对象列表
     * @param drawables 可绘制对象列表
     * @throws IOException IO异常
     */
    public void writeAll(Iterable<Drawable> drawables) throws IOException {
        for (Drawable drawable : drawables) {
            write(drawable);
        }
//        System.out.println("批量写入完成");
    }
    
    /**
     * 刷新缓冲区
     * @throws IOException IO异常
     */
    public void flush() throws IOException {
        writer.flush();
    }
    
    /**
     * 关闭写入器
     * @throws IOException IO异常
     */
    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.close();
//            System.out.println("ASS文件写入器已关闭");
        }
    }
    
    // Getters
    public String getTitle() {
        return title;
    }
    
    public CanvasConfig getCanvasConfig() {
        return canvasConfig;
    }
}