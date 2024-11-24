package com.infinityraider.maneuvergear.utility;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class BaublesWrapper {
    private static final BaublesWrapper INSTANCE = new BaublesWrapper();

    public static final int BELT_SLOT = 3;

    public static BaublesWrapper getInstance() {
        return INSTANCE;
    }

    private BaublesWrapper() {}

    public IBaublesItemHandler getBaubles(EntityPlayer player) {
        return BaublesApi.getBaublesHandler(player);
    }

    public ItemStack getBauble(EntityPlayer player, int slot) {
        IBaublesItemHandler inventory = getBaubles(player);
        if(slot < 0 || slot >= inventory.getSlots()) {
            return null;
        }
        return BaublesApi.getBaublesHandler(player).getStackInSlot(slot);
    }
}
