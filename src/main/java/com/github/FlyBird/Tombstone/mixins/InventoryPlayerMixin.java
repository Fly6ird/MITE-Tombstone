package com.github.FlyBird.Tombstone.mixins;

import com.github.FlyBird.Tombstone.Blocks;
import com.github.FlyBird.Tombstone.TileEntityTome;
import net.minecraft.EntityPlayer;
import net.minecraft.InventoryPlayer;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InventoryPlayer.class)
public class InventoryPlayerMixin {
    @Shadow public ItemStack[] mainInventory = new ItemStack[36];
    @Shadow public ItemStack[] armorInventory = new ItemStack[4];
    @Shadow public EntityPlayer player;

    /**
     * @author FlyBird
     * @reason not reason
     */
    @Overwrite
    public void dropAllItems() {
        this.player.getWorld().setBlock(this.player.getBlockPosX(),  this.player.getBlockPosY(), this.player.getBlockPosZ()+1, Blocks.tome.blockID);
        TileEntityTome entityTome=(TileEntityTome) this.player.getWorld().getBlockTileEntity(this.player.getBlockPosX(),  this.player.getBlockPosY(), this.player.getBlockPosZ()+1);
        int i;
        for (i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] == null) continue;
            entityTome.setInventorySlotContents(i,this.mainInventory[i]);
        }
        for (i = 0; i < this.armorInventory.length; ++i) {
            if (this.armorInventory[i] == null) continue;
            entityTome.setInventorySlotContents(46+i,this.armorInventory[i]);
        }
    }
}
