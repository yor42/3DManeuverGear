package com.infinityraider.maneuvergear.network;

import baubles.api.cap.IBaublesItemHandler;
import com.infinityraider.maneuvergear.utility.BaublesWrapper;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageEquipManeuverGear extends MessageBase<MessageManeuverGearEquipped> {
    EnumHand hand;

    public MessageEquipManeuverGear() {}

    public MessageEquipManeuverGear(EnumHand hand) {
        this();
        this.hand = hand;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.SERVER;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().player;
        ItemStack stack = player.getHeldItem(this.hand);
        if (!stack.isEmpty()) {
            IBaublesItemHandler baubles = BaublesWrapper.getInstance().getBaubles(player);
            ItemStack belt = baubles.getStackInSlot(BaublesWrapper.BELT_SLOT);
            belt = belt.copy();
            baubles.setStackInSlot(BaublesWrapper.BELT_SLOT, stack.copy());
            player.inventory.setInventorySlotContents(player.inventory.currentItem, belt);
        }
    }


    @Override
    protected MessageManeuverGearEquipped getReply(MessageContext ctx) {
        return new MessageManeuverGearEquipped();
    }
}
