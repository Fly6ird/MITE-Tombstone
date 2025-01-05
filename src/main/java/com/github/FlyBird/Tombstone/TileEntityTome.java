package com.github.FlyBird.Tombstone;


import net.minecraft.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TileEntityTome extends TileEntity implements IInventory {
    private ItemStack[] chestContents = new ItemStack[54];

    public int numUsingPlayers;

    private int ticksSinceSync;

    public float lidAngle;

    public TileEntityTome() {

    }
    public TileEntityTome(BlockTome blockTome) {
        setBlock(blockTome);
    }

    public int getSizeInventory() {
        return chestContents.length;
    }

    public ItemStack getStackInSlot(int par1) {
        return this.chestContents[par1];
    }

    public ItemStack decrStackSize(int par1, int par2) {
        if (this.chestContents[par1] != null) {
            if ((this.chestContents[par1]).stackSize <= par2) {
                ItemStack itemStack = this.chestContents[par1];
                this.chestContents[par1] = null;
                onInventoryChanged();
                return itemStack;
            }
            ItemStack var3 = this.chestContents[par1].splitStack(par2);
            if ((this.chestContents[par1]).stackSize == 0)
                this.chestContents[par1] = null;
            onInventoryChanged();
            return var3;
        }
        return null;
    }

    public ItemStack getStackInSlotOnClosing(int par1) {
        if (this.chestContents[par1] != null) {
            ItemStack var2 = this.chestContents[par1];
            this.chestContents[par1] = null;
            return var2;
        }
        return null;
    }

    public void setInventorySlotContents(int i, ItemStack par2ItemStack) {
        this.chestContents[i] = par2ItemStack;
        if (par2ItemStack != null && par2ItemStack.stackSize > getInventoryStackLimit())
            par2ItemStack.stackSize = getInventoryStackLimit();
        onInventoryChanged();
    }

    public String getUnlocalizedInvName() {
        return "container.tome";
    }

    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.chestContents = new ItemStack[getSizeInventory()];
        for (int var3 = 0; var3 < var2.tagCount(); var3++) {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            int var5 = var4.getByte("Slot") & 0xFF;
            if (var5 >= 0 && var5 < this.chestContents.length)
                this.chestContents[var5] = ItemStack.loadItemStackFromNBT(var4);
        }
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.chestContents.length; var3++) {
            if (this.chestContents[var3] != null) {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.chestContents[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        par1NBTTagCompound.setTag("Items", var2);
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
        return (this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D);
    }

    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
    }

    public void updateEntity() {
        super.updateEntity();
        this.ticksSinceSync++;
        if (!this.worldObj.isRemote && this.numUsingPlayers != 0 && (this.ticksSinceSync + this.xCoord + this.yCoord + this.zCoord) % 200 == 0) {
            this.numUsingPlayers = 0;
            float f = 5.0F;
            List var2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getAABBPool().getAABB((this.xCoord - f), (this.yCoord - f), (this.zCoord - f), ((this.xCoord + 1) + f), ((this.yCoord + 1) + f), ((this.zCoord + 1) + f)));
            Iterator<EntityPlayer> var3 = var2.iterator();
            while (var3.hasNext()) {
                EntityPlayer var4 = var3.next();
                if (var4.openContainer instanceof ContainerChest) {
                    IInventory var5 = ((ContainerChest)var4.openContainer).getLowerChestInventory();
                    if (var5 == this || (var5 instanceof InventoryLargeChest && ((InventoryLargeChest)var5).isPartOfLargeChest(this)))
                        this.numUsingPlayers++;
                }
            }
        }
        float var1 = 0.1F;
        if (this.numUsingPlayers > 0 && this.lidAngle == 0.0F) {
            double var8 = this.xCoord + 0.5D;
            double var11 = this.zCoord + 0.5D;
            this.worldObj.setBlockMetadataWithNotify(this.xCoord,this.yCoord,this.zCoord,BitHelper.flipBit(this.getBlockMetadata(), 8),2);
            this.worldObj.playSoundEffect(var8, this.yCoord + 0.5D, var11, "block.barrel.open", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }
        if ((this.numUsingPlayers == 0 && this.lidAngle > 0.0F) || (this.numUsingPlayers > 0 && this.lidAngle < 1.0F)) {
            float var9 = this.lidAngle;
            if (this.numUsingPlayers > 0) {
                this.lidAngle += var1;
            } else {
                this.lidAngle -= var1;
            }
            if (this.lidAngle > 1.0F)
                this.lidAngle = 1.0F;
            float var10 = 0.5F;
            if (this.lidAngle < var10 && var9 >= var10) {
                double var11 = this.xCoord + 0.5D;
                double var6 = this.zCoord + 0.5D;

                this.worldObj.setBlockMetadataWithNotify(this.xCoord,this.yCoord,this.zCoord,BitHelper.flipBit(this.getBlockMetadata(), 8),2);
                this.worldObj.playSoundEffect(var11, this.yCoord + 0.5D, var6, "block.barrel.close", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }
            if (this.lidAngle < 0.0F)
                this.lidAngle = 0.0F;
        }
    }
    public boolean receiveClientEvent(int par1, int par2) {
        if (par1 == 1) {
            this.numUsingPlayers = par2;
            return true;
        }
        return super.receiveClientEvent(par1, par2);
    }

    public void openChest() {
        this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, Blocks.tome.blockID);
        if (this.numUsingPlayers < 0)
            this.numUsingPlayers = 0;
        this.numUsingPlayers++;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, (getBlockType()).blockID, 1, this.numUsingPlayers);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, (getBlockType()).blockID);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, (getBlockType()).blockID);
    }

    public void closeChest() {
        this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, Blocks.tome.blockID);
        if (getBlockType() != null && getBlockType() instanceof BlockTome) {
            this.numUsingPlayers--;
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, (getBlockType()).blockID, 1, this.numUsingPlayers);
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, (getBlockType()).blockID);
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, (getBlockType()).blockID);
        }
    }

    //这是骗人的，这个方法压根不会被调用，垃圾的要死     再openGUI的时候传自己的container 在这个里面自己实现slot
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return (itemStack.getItem().itemID != Blocks.tome.blockID);
    }

    public void destroyInventory() {
        ItemStack[] item_stacks = this.chestContents;
        Arrays.fill(item_stacks, null);
    }

    public void invalidate() {
        super.invalidate();
        updateContainingBlockInfo();
    }
}
