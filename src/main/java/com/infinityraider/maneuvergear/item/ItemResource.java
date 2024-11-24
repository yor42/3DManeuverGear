package com.infinityraider.maneuvergear.item;

import com.infinityraider.maneuvergear.Tags;
import com.infinityraider.maneuvergear.handler.DartHandler;
import com.infinityraider.maneuvergear.init.ItemRegistry;
import com.infinityraider.maneuvergear.reference.Names;
import com.infinityraider.infinitylib.item.IItemWithModel;
import com.infinityraider.infinitylib.item.ItemBase;
import com.infinityraider.infinitylib.utility.IRecipeRegister;
import com.infinityraider.infinitylib.utility.TranslationHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemResource extends ItemBase implements IRecipeRegister, IItemWithModel {
    public ItemResource() {
        super(Names.Objects.RESOURCE);
        this.setCreativeTab(CreativeTabs.MISC);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(world.isRemote) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        }
        if(stack.getItem() != this) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        }
        if(stack.getItemDamage() == EnumSubItems.SWORD_BLADE.ordinal()) {
            ItemStack maneuverGear = DartHandler.instance.getManeuverGear(player);
            if(maneuverGear == null || !(maneuverGear.getItem() instanceof ItemManeuverGear)) {
                return new ActionResult<>(EnumActionResult.PASS, stack);
            }
            stack.setCount(((ItemManeuverGear) maneuverGear.getItem()).addBlades(maneuverGear, stack.getCount(), player.isSneaking()));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase attacker, EntityLivingBase attacked) {
        if(!attacker.getEntityWorld().isRemote
                && attacker instanceof EntityPlayer
                && stack != null
                && stack.getItemDamage() == EnumSubItems.SWORD_BLADE.ordinal())  {
            DamageSource source = DamageSource.causePlayerDamage((EntityPlayer) attacker);
            attacker.attackEntityFrom(source, 2.5F);
        }
        return false;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack)+"."+EnumSubItems.getNameForIndex(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if(!this.isInCreativeTab(tab)){
            return;
        }
        for(int i=0;i<EnumSubItems.values().length;i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
        if(stack.getItemDamage() == EnumSubItems.SWORD_BLADE.ordinal()) {
            list.add(TranslationHelper.translateToLocal("3DManeuverGear.ToolTip.swordBladeRight"));
            list.add(TranslationHelper.translateToLocal("3DManeuverGear.ToolTip.swordBladeLeft"));
        }
    }

    @Override
    public void registerRecipes() {
        //this.getRecipes().forEach(GameRegistry::addRecipe);
    }

    public List<IRecipe> getRecipes() {
        List<IRecipe> list = new ArrayList<>();
//        list.add(new ShapedOreRecipe(EnumSubItems.SWORD_BLADE.getStack(), "i", "i", "b",
//                'i', "ingotIron",
//                'b', new ItemStack(Blocks.IRON_BARS)));
//        list.add(new ShapedOreRecipe(EnumSubItems.GAS_CANISTER.getStack(), " l ", "isi", "ibi",
//                'l', new ItemStack(Blocks.LEVER),
//                'i', "ingotIron",
//                's', "slimeball",
//                'b', new ItemStack(Items.BUCKET)));
//        list.add(new ShapedOreRecipe(EnumSubItems.BLADE_HOLSTER.getStack(), "ibi", "i i", "iii",
//                'i', "ingotIron",
//                'b', new ItemStack(Blocks.IRON_BARS)));
//        list.add(new ShapedOreRecipe(EnumSubItems.BLADE_HOLSTER_ASSEMBLY.getStack(), " g ", "shs", " s ",
//                'g', EnumSubItems.GAS_CANISTER.getStack(),
//                's', new ItemStack(Items.STRING),
//                'h', EnumSubItems.BLADE_HOLSTER.getStack()));
//        list.add(new ShapedOreRecipe(EnumSubItems.BELT.getStack(), " l ", "l l", "lil",
//                'l', new ItemStack(Items.LEATHER),
//                'i', "ingotIron"));
//        list.add(new ShapedOreRecipe(EnumSubItems.GIRDLE.getStack(), "l l", "sjs", "lsl",
//                'l', new ItemStack(net.minecraft.init.Items.LEATHER),
//                's', new ItemStack(Items.STRING),
//                'j', new ItemStack(Items.LEATHER_CHESTPLATE)));
//        list.add(new ShapedOreRecipe(EnumSubItems.GAS_NOZZLE.getStack(), "ibi", "bsb", "ibi",
//                'i', "ingotIron",
//                'b', new ItemStack(Blocks.IRON_BARS),
//                's', new ItemStack(Blocks.IRON_BLOCK)));
//        list.add(new ShapedOreRecipe(EnumSubItems.CABLE_COIL.getStack(), "sss", "sis", "sss",
//                's', new ItemStack(Items.STRING),
//                'i', "ingotIron"));
//        list.add(new ShapedOreRecipe(EnumSubItems.GRAPPLE_LAUNCHER.getStack(), "iii", "sda", "iii",
//                'i', "ingotIron", 's', new ItemStack(Items.STRING),
//                'd', new ItemStack(Blocks.DISPENSER),
//                'a', new ItemStack(Items.ARROW)));
        return list;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        List<Tuple<Integer, ModelResourceLocation>> list = new ArrayList<>();
        for(EnumSubItems subItem : EnumSubItems.values()) {
            list.add(new Tuple<>(subItem.ordinal(), subItem.getModelResourceLocation()));
        }
        return list;
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    public enum EnumSubItems {
        SWORD_BLADE("sword_blade"),
        GAS_CANISTER("gas_canister"),
        BLADE_HOLSTER("blade_holster"),
        BLADE_HOLSTER_ASSEMBLY("blade_holster_assembly"),
        BELT("belt"),
        GIRDLE("girdle"),
        GAS_NOZZLE("gas_nozzle"),
        CABLE_COIL("cable_spool"),
        GRAPPLE_LAUNCHER("grapple_launcher");

        public final String name;

        EnumSubItems(String name) {
            this.name = name;
        }

        public ItemStack getStack() {
            return new ItemStack(ItemRegistry.getInstance().itemResource, 1, this.ordinal());
        }

        @SideOnly(Side.CLIENT)
        public ModelResourceLocation getModelResourceLocation() {
            return new ModelResourceLocation(Tags.MOD_ID.toLowerCase() + ":" + name, "inventory");
        }

        public static EnumSubItems getValue(int index) {
            return values()[correctIndex(index)];
        }

        public static String getNameForIndex(int index) {
            return getValue(index).name;
        }

        private static int correctIndex(int index) {
            return index <= 0 ? 0 : index >= values().length ? values().length-1 : index;
        }
    }
}
