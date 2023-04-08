package me.zero.clarinet.mixin.mixins.minecraft.entity;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author Doogie13
 * @since 04/04/2023
 */
@Mixin(Entity.class)
public interface IEntity {

    @Invoker void callCopyDataFromOld(Entity entity);

}
