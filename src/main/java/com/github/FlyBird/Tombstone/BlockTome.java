package com.github.FlyBird.Tombstone;

import net.minecraft.*;
import net.xiaoyu233.fml.reload.utils.IdUtil;

import java.util.Random;

public class BlockTome extends BlockContainer {
    public NBTTagCompound stackTagCompound;

    public static int tomeRenderType=getNextRenderType();
    private final Random random = new Random();

    protected BlockTome(int blockid) {
        super(blockid, Material.stone, new BlockConstants().setNotAlwaysLegal().setNeverHidesAdjacentFaces());
        //setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public int getRenderType() {
        return tomeRenderType;
    }

    public static int getNextRenderType() {return IdUtil.getNextRenderType();}

    @Override
    public boolean updateTick(World world, int x, int y, int z, Random rand) {
        return super.updateTick(world, x, y, z, rand) && world.getBlock(x, y, z) != this;
    }

    @Override
    public boolean onNeighborBlockChange(World world, int x, int y, int z, int neighbor_block_id) {
        if (super.onNeighborBlockChange(world, x, y, z, neighbor_block_id)) {
            return true;
        } else {
            TileEntityTome chest_entity = (TileEntityTome) world.getBlockTileEntity(x, y, z);
            if (chest_entity != null) {
                chest_entity.updateContainingBlockInfo();
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
        TileEntityTome tileEntityTome = (TileEntityTome)par1World.getBlockTileEntity(par2, par3, par4);
        if (tileEntityTome != null) {
            for(int i = 0; i < tileEntityTome.getSizeInventory(); ++i) {
                ItemStack itemStack = tileEntityTome.getStackInSlot(i);
                if (itemStack != null) {
                    float var10 = this.random.nextFloat() * 0.8F + 0.1F;
                    float var11 = this.random.nextFloat() * 0.8F + 0.1F;

                    EntityItem entityItem;
                    for(float var12 = this.random.nextFloat() * 0.8F + 0.1F; itemStack.stackSize > 0; par1World.spawnEntityInWorld(entityItem)) {
                        int var13 = this.random.nextInt(21) + 10;
                        if (var13 > itemStack.stackSize) {
                            var13 = itemStack.stackSize;
                        }

                        itemStack.stackSize -= var13;
                        entityItem = new EntityItem(par1World, (float)par2 + var10, (float)par3 + var11, (float)par4 + var12, new ItemStack(itemStack.itemID, var13, itemStack.getItemSubtype()));
                        if (itemStack.isItemDamaged()) {
                            entityItem.getEntityItem().setItemDamage(itemStack.getItemDamage());
                        }

                        float var15 = 0.05F;
                        entityItem.motionX = (float)this.random.nextGaussian() * var15;
                        entityItem.motionY = (float)this.random.nextGaussian() * var15 + 0.2F;
                        entityItem.motionZ = (float)this.random.nextGaussian() * var15;
                        if (itemStack.getItem().hasQuality()) {
                            entityItem.getEntityItem().setQuality(itemStack.getQuality());
                        }

                        if (itemStack.hasTagCompound()) {
                            entityItem.getEntityItem().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
                        }
                    }
                }
            }

            par1World.func_96440_m(par2, par3, par4, par5);
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    @Override
    public int dropBlockAsEntityItem(BlockBreakInfo info) {
        return 0;
    }

    public boolean canOpenChest(EntityLivingBase entity_living_base) {
        return  !entity_living_base.hasCurse(Curse.cannot_open_chests, true);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, EnumFace face, float dx, float dy, float dz) {
        if (player.onServer()&&!player.isSneaking()) {
            if (this.canOpenChest(player)) {
                IInventory inventory = this.getInventory(world, x, y, z);
                if (inventory != null) {
                    player.displayGUIChest(x, y, z, inventory);
                }
            } else {
                world.playSoundAtBlock(x, y, z, "imported.random.chest_locked", 0.2F);
            }
        } else if (player.isSneaking()) {
            TileEntityTome entityTome=(TileEntityTome) player.getWorld().getBlockTileEntity(x, y, z);
            int i,armortype;
            for (i = 0; i < 45; ++i) {
                if (entityTome.getStackInSlot(i) == null) continue;
                player.inventory.setInventorySlotContents(i,entityTome.getStackInSlot(i));

            }
            for (i = 46; i < 54; ++i) {
                if (entityTome.getStackInSlot(i) == null) continue;
                armortype=entityTome.getStackInSlot(i).getItemAsArmor().armorType;
                //头盔为0
                switch (armortype) {
                    case 0:
                        player.inventory.setWornItem(3,entityTome.getStackInSlot(i));
                        break;
                    case 1:
                        player.inventory.setWornItem(2,entityTome.getStackInSlot(i));
                        break;
                    case 2:
                        player.inventory.setWornItem(1,entityTome.getStackInSlot(i));
                        break;
                    case 3:
                        player.inventory.setWornItem(0,entityTome.getStackInSlot(i));
                        break;
                }

            }
            for (int j = 0; j < entityTome.getSizeInventory(); j++) {
                entityTome.setInventorySlotContents(j,null);
            }
            world.setBlockToAir(x,y,z);
        }
        return true;
    }


    public IInventory getInventory(World par1World, int par2, int par3, int par4) {
        TileEntityTome barrelTileEntity = (TileEntityTome) par1World.getBlockTileEntity(par2, par3, par4);
        if (barrelTileEntity == null)
            return null;
        if (isOcelotBlockingChest(par1World, par2, par3, par4))
            return null;
        return barrelTileEntity;
    }

    @Override
    public TileEntity createNewTileEntity(World var1) {
        return new TileEntityTome(this);
    }



    @Override
    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return par5 == 1 ? this.isProvidingWeakPower(par1IBlockAccess, par2, par3, par4, par5) : 0;
    }

    private static boolean isOcelotBlockingChest(World par0World, int par1, int par2, int par3) {
        for (Object o : par0World.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getAABBPool().getAABB(par1, par2 + 1, par3, par1 + 1, par2 + 2, par3 + 1))) {
            EntityOcelot var6 = (EntityOcelot) o;
            if (var6.isSitting()) {
                return true;
            }
        }

        return false;
    }


    @Override
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
        return Container.calcRedstoneFromInventory(this.getInventory(par1World, par2, par3, par4));
    }

}
