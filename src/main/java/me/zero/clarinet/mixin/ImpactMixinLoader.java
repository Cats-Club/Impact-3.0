package me.zero.clarinet.mixin;

import me.zero.clarinet.Impact;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@IFMLLoadingPlugin.Name(Impact.CLIENT_NAME + "MixinLoader")
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class ImpactMixinLoader implements IFMLLoadingPlugin {

    public ImpactMixinLoader() {

        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.impact.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        MixinEnvironment.getDefaultEnvironment().setOption(MixinEnvironment.Option.HOT_SWAP, true);
        Logger.getAnonymousLogger().info("Impact Mixins loaded!");

    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}