package org.naiximcn;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

public final class MiraiCustomHelp extends JavaPlugin {
    public static final MiraiCustomHelp INSTANCE = new MiraiCustomHelp();

    private MiraiCustomHelp() {
        super(new JvmPluginDescriptionBuilder("org.naiximcn.chelp", "1.0.0")
                .name("MiraiCustomHelp")
                .author("Naixi_nana")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("Plugin loaded!");
    }
}