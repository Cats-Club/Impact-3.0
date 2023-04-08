package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemPotion extends net.minecraft.item.Item
{
    public ItemPotion()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.BREWING);
    }

    @Nullable

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    public net.minecraft.item.ItemStack onItemUseFinish(net.minecraft.item.ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        EntityPlayer entityplayer = entityLiving instanceof EntityPlayer ? (EntityPlayer)entityLiving : null;

        if (entityplayer == null || !entityplayer.capabilities.isCreativeMode)
        {
            --stack.stackSize;
        }

        if (!worldIn.isRemote)
        {
            for (PotionEffect potioneffect : PotionUtils.getEffectsFromStack(stack))
            {
                entityLiving.addPotionEffect(new PotionEffect(potioneffect));
            }
        }

        if (entityplayer != null)
        {
            entityplayer.addStat(StatList.getObjectUseStats(this));
        }

        if (entityplayer == null || !entityplayer.capabilities.isCreativeMode)
        {
            if (stack.stackSize <= 0)
            {
                return new net.minecraft.item.ItemStack(Items.GLASS_BOTTLE);
            }

            if (entityplayer != null)
            {
                entityplayer.inventory.addItemStackToInventory(new net.minecraft.item.ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return stack;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(net.minecraft.item.ItemStack stack)
    {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public net.minecraft.item.EnumAction getItemUseAction(net.minecraft.item.ItemStack stack)
    {
        return EnumAction.DRINK;
    }

    public ActionResult<net.minecraft.item.ItemStack> onItemRightClick(net.minecraft.item.ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        playerIn.setActiveHand(hand);
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    public String getItemStackDisplayName(net.minecraft.item.ItemStack stack)
    {
        return I18n.translateToLocal(PotionUtils.getPotionFromItem(stack).getNamePrefixed("potion.effect."));
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(net.minecraft.item.ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
    }

    public boolean hasEffect(net.minecraft.item.ItemStack stack)
    {
        return !PotionUtils.getEffectsFromStack(stack).isEmpty();
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(Item itemIn, CreativeTabs tab, List<net.minecraft.item.ItemStack> subItems)
    {
        for (PotionType potiontype : PotionType.REGISTRY)
        {
            subItems.add(PotionUtils.addPotionToItemStack(new ItemStack(itemIn), potiontype));
        }
    }
}
