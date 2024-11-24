package com.infinityraider.maneuvergear.item;

import com.infinityraider.maneuvergear.handler.ConfigurationHandler;
import com.infinityraider.maneuvergear.handler.DartHandler;
import com.infinityraider.maneuvergear.init.ItemRegistry;
import com.infinityraider.maneuvergear.reference.Names;
import com.infinityraider.maneuvergear.render.item.RenderItemHandle;
import com.google.common.collect.Multimap;
import com.infinityraider.infinitylib.item.ICustomRenderedItem;
import com.infinityraider.infinitylib.item.ItemBase;
import com.infinityraider.infinitylib.modules.dualwield.IDualWieldedWeapon;
import com.infinityraider.infinitylib.render.item.IItemRenderingHandler;
import com.infinityraider.infinitylib.utility.IRecipeRegister;
import com.infinityraider.infinitylib.utility.TranslationHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@MethodsReturnNonnullByDefault
public class ItemManeuverGearHandle extends ItemBase implements IDualWieldedWeapon, IRecipeRegister, ICustomRenderedItem {
    public final int MAX_ITEM_DAMAGE;

    @SideOnly(Side.CLIENT)
    private IItemRenderingHandler renderer;

    public ItemManeuverGearHandle() {
        super(Names.Objects.MANEUVER_HANDLE);
        this.MAX_ITEM_DAMAGE = ConfigurationHandler.getInstance().durability;
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.setMaxStackSize(1);
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    /**
     * Checks if there is a sword blade present on the respective handle
     * @param stack the ItemStack holding this
     * @return if there is a blade present
     */
    public boolean hasSwordBlade(ItemStack stack) {
        if (!isValidManeuverGearHandleStack(stack)) {
            return false;
        }
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.getInteger(Names.NBT.DAMAGE) > 0;
    }

    public int getBladeDamage(ItemStack stack) {
        if(!isValidManeuverGearHandleStack(stack)) {
            return 0;
        }
        NBTTagCompound tag = stack.getTagCompound();
        if(tag == null) {
            return 0;
        }
        return tag.getInteger(Names.NBT.DAMAGE);
    }

    /**
     * Attempts to damage the sword blade
     * @param stack the ItemStack holding this
     */
    public void damageSwordBlade(EntityPlayer player, ItemStack stack) {
        if(!hasSwordBlade(stack)) {
            return;
        }
        NBTTagCompound tag = stack.getTagCompound();
        if(tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        int dmg = tag.getInteger(Names.NBT.DAMAGE);
        dmg = dmg -1;
        tag.setInteger(Names.NBT.DAMAGE, dmg);
        if(dmg == 0) {
            onSwordBladeBroken(player);
        }
    }

    /**
     * Tries to apply a sword blade from the holster, this can fail if the player is not wearing a holster, the respective holster is empty,
     * or there is already a blade on the handle
     * @param player the player wielding the gear
     * @param stack the ItemStack holding this
     * @param left to add a blade to the left or the right handle
     * @return if a blade was successfully applied
     */
    public boolean applySwordBlade(EntityPlayer player, ItemStack stack, boolean left) {
        if(!isValidManeuverGearHandleStack(stack)) {
            return false;
        }
        if(hasSwordBlade(stack)) {
            return false;
        }
        ItemStack maneuverGearStack = DartHandler.instance.getManeuverGear(player);
        ItemManeuverGear maneuverGear = (ItemManeuverGear) maneuverGearStack.getItem();
        if(maneuverGear.getBladeCount(maneuverGearStack, left) > 0) {
            maneuverGear.removeBlades(maneuverGearStack, 1, left);
            NBTTagCompound tag = stack.getTagCompound();
            tag = tag == null ? new NBTTagCompound() : tag;
            tag.setInteger(Names.NBT.DAMAGE, MAX_ITEM_DAMAGE);
            stack.setTagCompound(tag);
            return true;
        }
        return false;
    }

    private void onSwordBladeBroken(EntityPlayer player) {
        if(player != null && !player.getEntityWorld().isRemote) {
            SoundType type = Blocks.ANVIL.getSoundType();
            player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ, type.getPlaceSound(), SoundCategory.PLAYERS, (type.getVolume() + 1.0F) / 4.0F, type.getPitch() * 0.8F);
        }
    }

    /**
     * Checks if the stack is a valid stack containing Maneuver Gear
     * @param stack the stack to check
     * @return if the stack is valid
     */
    public boolean isValidManeuverGearHandleStack(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemManeuverGearHandle;
    }

    //@Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.WEB) {
            return 15.0F;
        } else {
            Material material = state.getMaterial();
            return material != Material.PLANTS
                    && material != Material.VINE
                    && material != Material.CORAL
                    && material != Material.LEAVES
                    && material != Material.GLASS
                    ? 1.0F : 1.5F;
        }
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving) {
        if ((double)blockIn.getBlockHardness(worldIn, pos) != 0.0D && this.hasSwordBlade(stack)) {
            stack.damageItem(2, entityLiving);
        }
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public void onItemUsed(ItemStack stack, EntityPlayer player, boolean shift, boolean ctrl, EnumHand hand) {
        if (stack.getItem() != this) {
            return;
        }
        if (!DartHandler.instance.isWearingGear(player)) {
            return;
        }
        if (!player.getEntityWorld().isRemote) {
            boolean left = hand == EnumHand.OFF_HAND;
            if (shift) {
                if(!DartHandler.instance.isWearingGear(player)) {
                    return;
                }
                if (left ? DartHandler.instance.hasLeftDart(player) : DartHandler.instance.hasRightDart(player)) {
                    DartHandler.instance.retractDart(player, left);
                } else {
                    DartHandler.instance.fireDart(player.getEntityWorld(), player, left);
                }
            } else if (ctrl) {
                if (!hasSwordBlade(stack)) {
                    applySwordBlade(player, stack, left);
                }
            }
        }
    }

    @Override
    public boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand) {
        if(ctrl || shift) {
            return true;
        }
        if(!this.hasSwordBlade(stack)) {
            return true;
        }
        if(!player.getEntityWorld().isRemote) {
            if (!player.capabilities.isCreativeMode) {
                this.damageSwordBlade(player, stack);
            }
        }
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if(slot == EntityEquipmentSlot.OFFHAND || slot == EntityEquipmentSlot.MAINHAND) {
            if (this.hasSwordBlade(stack)) {
                multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                        new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 3.0 + (ConfigurationHandler.getInstance().damage), 0));
            } else {
                multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                        new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 0, 0));
            }
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 1.8, 0));
        }
        return multimap;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
        if(stack != null) {
            list.add(TranslationHelper.translateToLocal("3DManeuverGear.ToolTip.handle"));
            list.add(TranslationHelper.translateToLocal("3DManeuverGear.ToolTip.damage") + ": " + this.getBladeDamage(stack) + "/" + this.MAX_ITEM_DAMAGE);
            list.add("");
            list.add(TranslationHelper.translateToLocal("3DManeuverGear.ToolTip.handleLeftNormal"));
            list.add(TranslationHelper.translateToLocal("3DManeuverGear.ToolTip.handleRightNormal"));
            list.add(TranslationHelper.translateToLocal("3DManeuverGear.ToolTip.handleLeftSneak"));
            list.add(TranslationHelper.translateToLocal("3DManeuverGear.ToolTip.handleRightSneak"));
            list.add(TranslationHelper.translateToLocal("3DManeuverGear.ToolTip.handleLeftSprint"));
            list.add(TranslationHelper.translateToLocal("3DManeuverGear.ToolTip.handleRightSprint"));
        }
    }

    @Override
    public void registerRecipes() {
        //this.getRecipes().forEach(GameRegistry::addRecipe);
    }
//
//    public List<IRecipe> getRecipes() {
//        List<IRecipe> list = new ArrayList<>();
//        list.add(new ShapedOreRecipe(ItemRegistry.getInstance().itemManeuverGearHandle, "ww ", "iib", "wwl",
//                'w', new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE),
//                'i', "ingotIron",
//                'b', new ItemStack(Blocks.IRON_BARS),
//                'l', new ItemStack(Blocks.LEVER)));
//        return list;
//    }

    @Override
    @SideOnly(Side.CLIENT)
    public IItemRenderingHandler getRenderer() {
        if(this.renderer == null) {
            this.renderer = new RenderItemHandle();
        }
        return this.renderer;
    }
}
