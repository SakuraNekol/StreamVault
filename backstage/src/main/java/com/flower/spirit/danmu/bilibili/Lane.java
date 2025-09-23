package com.flower.spirit.danmu.bilibili;
/**
 * 用于弹幕碰撞检测
 * 实现来自 
 *    danmu2ass
 *    https://github.com/gwy15/danmu2ass
 */
public class Lane {
    /**
     * 上一条弹幕的发射时间
     */
    private double lastShootTime;
    
    /**
     * 上一条弹幕的长度
     */
    private double lastLength;
    
    public Lane(double lastShootTime, double lastLength) {
        this.lastShootTime = lastShootTime;
        this.lastLength = lastLength;
    }
    
    /**
     * 创建一个用于绘制弹幕的车道
     * @param danmu 弹幕对象
     * @param config 画布配置
     * @return 新的车道对象
     */
    public static Lane createForDraw(Danmu danmu, CanvasConfig config) {
        return new Lane(danmu.getTimelineS(), danmu.calculateLength(config));
    }
    
    /**
     * 创建一个用于固定弹幕的车道（如底部弹幕）
     * @param danmu 弹幕对象
     * @return 新的车道对象
     */
    public static Lane createForFixed(Danmu danmu) {
        return new Lane(danmu.getTimelineS(), 0.0);
    }
    
    /**
     * 检查这个车道是否可以发射另一条弹幕
     * @param other 要检查的弹幕
     * @param config 画布配置
     * @return 碰撞检测结果
     */
    public CollisionResult checkAvailableFor(Danmu other, CanvasConfig config) {
        double T = config.getDuration();
        double W = config.getWidth();
        double gap = config.getHorizontalGap();
        
        // 计算速度
        double t1 = this.lastShootTime;
        double t2 = other.getTimelineS();
        double l1 = this.lastLength;
        double l2 = other.calculateLength(config);
        
        double v1 = (W + l1) / T;
        double v2 = (W + l2) / T;
        
        double deltaT = t2 - t1;
        // 第一条弹幕右边到屏幕右边的距离
        double deltaX = v1 * deltaT - l1;
        
        // 没有足够的空间，必定碰撞
        if (deltaX < gap) {
            if (l2 <= l1) {
                // l2比l1短，因此比它慢
                // 只需要把l2安排在l1之后就可以避免碰撞
                return new CollisionResult.Collide((gap - deltaX) / v1);
            } else {
                // 需要延长额外的时间，使得当第一条消失的时候，第二条也有足够的距离
                double timeNeeded = (T - (W - gap) / v2) - deltaT;
                return new CollisionResult.Collide(timeNeeded);
            }
        } else {
            // 第一条已经发射
            if (l2 <= l1) {
                // 如果l2 < l1，则它永远追不上前者，可以发射
                return new CollisionResult.Separate(deltaX - gap);
            } else {
                // 需要算追击问题了
                // l1已经消失，但是l2可能追上，我们计算l1刚好消失的时候：
                // 此刻是t1 + T
                // l2的头部应该在距离起点v2 * (t1 + T - t2)处
                double pos = v2 * (T - deltaT);
                if (pos < (W - gap)) {
                    return new CollisionResult.NotEnoughTime((W - gap) - pos);
                } else {
                    // 需要额外的时间
                    return new CollisionResult.Collide((pos - (W - gap)) / v2);
                }
            }
        }
    }
    
    // Getters and Setters
    public double getLastShootTime() {
        return lastShootTime;
    }
    
    public void setLastShootTime(double lastShootTime) {
        this.lastShootTime = lastShootTime;
    }
    
    public double getLastLength() {
        return lastLength;
    }
    
    public void setLastLength(double lastLength) {
        this.lastLength = lastLength;
    }
    
    /**
     * 碰撞检测结果
     */
    public static abstract class CollisionResult {
        /**
         * 会越来越远，可以安全发射
         */
        public static class Separate extends CollisionResult {
            private final double closestDistance;
            
            public Separate(double closestDistance) {
                this.closestDistance = closestDistance;
            }
            
            public double getClosestDistance() {
                return closestDistance;
            }
        }
        
        /**
         * 时间够可以追上，但是时间不够
         */
        public static class NotEnoughTime extends CollisionResult {
            private final double closestDistance;
            
            public NotEnoughTime(double closestDistance) {
                this.closestDistance = closestDistance;
            }
            
            public double getClosestDistance() {
                return closestDistance;
            }
        }
        
        /**
         * 需要额外的时间才可以避免碰撞
         */
        public static class Collide extends CollisionResult {
            private final double timeNeeded;
            
            public Collide(double timeNeeded) {
                this.timeNeeded = timeNeeded;
            }
            
            public double getTimeNeeded() {
                return timeNeeded;
            }
        }
    }
}