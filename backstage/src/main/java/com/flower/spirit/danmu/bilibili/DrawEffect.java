package com.flower.spirit.danmu.bilibili;
/**
 * 绘制效果类
 * 实现来自 
 *    danmu2ass
 *    https://github.com/gwy15/danmu2ass
 */
public abstract class DrawEffect {
    
    /**
     * 移动效果
     */
    public static class Move extends DrawEffect {
        private final int[] start;
        private final int[] end;
        
        public Move(int[] start, int[] end) {
            this.start = start;
            this.end = end;
        }
        
        public Move(int startX, int startY, int endX, int endY) {
            this.start = new int[]{startX, startY};
            this.end = new int[]{endX, endY};
        }
        
        public int[] getStart() {
            return start;
        }
        
        public int[] getEnd() {
            return end;
        }
        
        /**
         * 转换为ASS格式的移动效果字符串
         * @return ASS移动效果字符串
         */
        @Override
        public String toAssString() {
            return String.format("\\move(%d, %d, %d, %d)", 
                start[0], start[1], end[0], end[1]);
        }
    }
    
    /**
     * 固定位置效果
     */
    public static class Fixed extends DrawEffect {
        private final int[] position;
        
        public Fixed(int[] position) {
            this.position = position;
        }
        
        public Fixed(int x, int y) {
            this.position = new int[]{x, y};
        }
        
        public int[] getPosition() {
            return position;
        }
        
        /**
         * 转换为ASS格式的固定位置效果字符串
         * @return ASS固定位置效果字符串
         */
        @Override
        public String toAssString() {
            return String.format("\\pos(%d, %d)", position[0], position[1]);
        }
    }
    
    /**
     * 转换为ASS格式的效果字符串
     * @return ASS效果字符串
     */
    public abstract String toAssString();
}