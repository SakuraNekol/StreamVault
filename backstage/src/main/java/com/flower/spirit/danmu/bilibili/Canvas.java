package com.flower.spirit.danmu.bilibili;
import java.util.*;

/**
 * 画布类，负责弹幕布局和碰撞检测
 * 实现来自 
 *    danmu2ass
 *    https://github.com/gwy15/danmu2ass
 */
public class Canvas {
    private CanvasConfig config;
    private List<Lane> floatLanes;
    private List<Lane> bottomLanes;
    
    public Canvas(CanvasConfig config) {
        this.config = config;
        
        // 计算车道数量
        int floatLanesCount = (int) (config.getFloatPercentage() * config.getHeight() / config.getLaneSize());
        int bottomLanesCount = (int) (config.getBottomPercentage() * config.getHeight() / config.getLaneSize());
        
        // 初始化车道列表
        this.floatLanes = new ArrayList<>(Collections.nCopies(floatLanesCount, null));
        this.bottomLanes = new ArrayList<>(Collections.nCopies(bottomLanesCount, null));
    }
    
    /**
     * 绘制弹幕到画布上
     * @param danmu 要绘制的弹幕
     * @return 可绘制对象，如果无法绘制则返回null
     */
    public Drawable draw(Danmu danmu) {
        // 应用时间偏移
        danmu.setTimelineS(danmu.getTimelineS() + config.getTimeOffset());
        if (danmu.getTimelineS() < 0.0) {
            return null;
        }
        
        switch (danmu.getType()) {
            case FLOAT:
                return drawFloat(danmu);
            case BOTTOM:
            case TOP:
            case REVERSE:
                // 不喜欢底部弹幕，直接转成滚动弹幕
                // 这是feature不是bug
                danmu.setType(DanmuType.FLOAT);
                return drawFloat(danmu);
            default:
                return drawFloat(danmu);
        }
    }
    
    /**
     * 绘制滚动弹幕
     * @param danmu 弹幕对象
     * @return 可绘制对象，如果无法绘制则返回null
     */
    private Drawable drawFloat(Danmu danmu) {
        List<CollisionInfo> collisions = new ArrayList<>();
        
        // 检查每个车道
        for (int idx = 0; idx < floatLanes.size(); idx++) {
            Lane lane = floatLanes.get(idx);
            
            if (lane == null) {
                // 优先使用空车道
                return drawFloatInLane(danmu, idx);
            } else {
                Lane.CollisionResult collision = lane.checkAvailableFor(danmu, config);
                
                if (collision instanceof Lane.CollisionResult.Separate || 
                    collision instanceof Lane.CollisionResult.NotEnoughTime) {
                    return drawFloatInLane(danmu, idx);
                } else if (collision instanceof Lane.CollisionResult.Collide) {
                    Lane.CollisionResult.Collide collide = (Lane.CollisionResult.Collide) collision;
                    collisions.add(new CollisionInfo(collide.getTimeNeeded(), idx));
                }
            }
        }
        
        // 允许部分弹幕在延迟后填充
        if (!collisions.isEmpty()) {
            collisions.sort(Comparator.comparingDouble(c -> c.timeNeeded));
            CollisionInfo bestCollision = collisions.get(0);
            
            if (bestCollision.timeNeeded < 1.0) {
                // 只允许延迟1秒
                danmu.setTimelineS(danmu.getTimelineS() + bestCollision.timeNeeded + 0.01); // 间隔也不要太小了
                return drawFloatInLane(danmu, bestCollision.laneIndex);
            }
        }
        
        return null;
    }
    
    /**
     * 在指定车道绘制滚动弹幕
     * @param danmu 弹幕对象
     * @param laneIndex 车道索引
     * @return 可绘制对象
     */
    private Drawable drawFloatInLane(Danmu danmu, int laneIndex) {
        floatLanes.set(laneIndex, Lane.createForDraw(danmu, config));
        
        int y = laneIndex * config.getLaneSize();
        double length = danmu.calculateLength(config);
        
        DrawEffect.Move moveEffect = new DrawEffect.Move(
            config.getWidth(), y,
            -(int) length, y
        );
        
        return new Drawable(danmu, config.getDuration(), "Float", moveEffect);
    }
    
    /**
     * 碰撞信息内部类
     */
    private static class CollisionInfo {
        final double timeNeeded;
        final int laneIndex;
        
        CollisionInfo(double timeNeeded, int laneIndex) {
            this.timeNeeded = timeNeeded;
            this.laneIndex = laneIndex;
        }
    }
    
    // Getters
    public CanvasConfig getConfig() {
        return config;
    }
    
    public List<Lane> getFloatLanes() {
        return floatLanes;
    }
    
    public List<Lane> getBottomLanes() {
        return bottomLanes;
    }
}