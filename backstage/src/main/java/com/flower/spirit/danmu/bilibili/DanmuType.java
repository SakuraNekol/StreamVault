package com.flower.spirit.danmu.bilibili;
/**
 * 弹幕类型枚举
 * 实现来自 
 *    danmu2ass
 *    https://github.com/gwy15/danmu2ass
 */
public enum DanmuType {
    /**
     * 滚动弹幕（从右到左）
     */
    FLOAT(1),
    
    /**
     * 顶部弹幕
     */
    TOP(5),
    
    /**
     * 底部弹幕
     */
    BOTTOM(4),
    
    /**
     * 反向弹幕（从左到右）
     */
    REVERSE(6);
    
    private final int xmlValue;
    
    DanmuType(int xmlValue) {
        this.xmlValue = xmlValue;
    }
    
    /**
     * 从XML中的数字值转换为弹幕类型
     * @param xmlValue XML中的弹幕类型数字
     * @return 对应的弹幕类型，如果不支持则返回null
     */
    public static DanmuType fromXmlValue(int xmlValue) {
        for (DanmuType type : values()) {
            if (type.xmlValue == xmlValue) {
                return type;
            }
        }
        return null;
    }
    
    public int getXmlValue() {
        return xmlValue;
    }
}