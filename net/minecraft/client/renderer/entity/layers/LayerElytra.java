package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.src.Config;
import net.minecraft.src.CustomItems;
import net.minecraft.util.ResourceLocation;

public class LayerElytra implements LayerRenderer<AbstractClientPlayer>
{
    /** The basic Elytra texture. */
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");

    /** Instance of the player renderer. */
    private final RenderPlayer renderPlayer;

    /** The model used by the Elytra. */
    private final ModelElytra modelElytra = new ModelElytra();

    public LayerElytra(RenderPlayer renderPlayerIn)
    {
        this.renderPlayer = renderPlayerIn;
    }

    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

        if (itemstack != null && itemstack.getItem() == Items.ELYTRA)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();

            if (entitylivingbaseIn.isPlayerInfoSet() && entitylivingbaseIn.getLocationElytra() != null)
            {
                this.renderPlayer.bindTexture(entitylivingbaseIn.getLocationElytra());
            }
            else if (entitylivingbaseIn.hasElytraCape() && entitylivingbaseIn.hasPlayerInfo() && entitylivingbaseIn.getLocationCape() != null && entitylivingbaseIn.isWearing(EnumPlayerModelParts.CAPE))
            {
                this.renderPlayer.bindTexture(entitylivingbaseIn.getLocationCape());
            }
            else
            {
                ResourceLocation resourcelocation = TEXTURE_ELYTRA;

                if (Config.isCustomItems())
                {
                    resourcelocation = CustomItems.getCustomElytraTexture(itemstack, resourcelocation);
                }

                this.renderPlayer.bindTexture(resourcelocation);
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 0.125F);
            this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
            this.modelElytra.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            if (itemstack.isItemEnchanted())
            {
                LayerArmorBase.renderEnchantedGlint(this.renderPlayer, entitylivingbaseIn, this.modelElytra, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }

            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
