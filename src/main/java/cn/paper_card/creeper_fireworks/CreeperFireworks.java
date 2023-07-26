package cn.paper_card.creeper_fireworks;

import org.bukkit.*;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public final class CreeperFireworks extends JavaPlugin implements Listener {

    private final Color[] colors = new Color[]{
            Color.BLACK, // 黑色
            Color.BLUE, // 蓝色
            Color.FUCHSIA, // 紫红色
            Color.GRAY, // 灰色
            Color.GREEN, // 绿色
            Color.LIME, // 淡黄绿色
            Color.MAROON, //褐红色
            Color.NAVY, // 深蓝色
            Color.OLIVE, // 黄绿色
            Color.ORANGE, // 橙色
            Color.PURPLE, // 紫色
            Color.RED, // 红色
            Color.SILVER, // 银色
            Color.TEAL, // 青色
            Color.WHITE, // 白色
            Color.YELLOW, // 黄色
    };

    private int typeIndex = 0;
    private int colorIndex = 0;


    private @NotNull Color getColor() {
        this.colorIndex %= this.colors.length;
        final Color color = this.colors[this.colorIndex];
        ++this.colorIndex;
        return color;
    }

    private @NotNull FireworkEffect.Type getType() {
        final FireworkEffect.Type[] values = FireworkEffect.Type.values();
        this.typeIndex %= values.length;
        final FireworkEffect.Type value = values[this.typeIndex];
        ++this.typeIndex;
        return value;
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }


    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent e) {

        // 如果是苦力怕爆炸
        if (e.getEntityType().equals(EntityType.CREEPER)) {

            e.setCancelled(true); // 取消事件

            final Creeper creeper = (Creeper) e.getEntity(); // 苦力怕

            // 这个位置大概是苦力怕的头部
            final Location location = creeper.getLocation().add(new Vector(0, 1, 0));

            // 产生烟花
            final Firework firework = (Firework) creeper.getWorld().spawnEntity(location, EntityType.FIREWORK);

            // 设置烟花样式
            final FireworkMeta meta = firework.getFireworkMeta();
            meta.setPower(0);
            meta.addEffect(FireworkEffect.builder()
                    .with(this.getType()) // 样式
                    .withColor(this.getColor()) // 颜色
                    .flicker(true) // 闪烁
                    .build());
            firework.setFireworkMeta(meta);

            // 播放爆炸音效
            location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 2, 1);

            this.getServer().getRegionScheduler().runDelayed(this, location, scheduledTask -> firework.detonate(), 1);
        }
    }
}
