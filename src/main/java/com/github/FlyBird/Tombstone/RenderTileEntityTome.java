package com.github.FlyBird.Tombstone;

import net.minecraft.*;
import org.lwjgl.opengl.GL11;

public class RenderTileEntityTome extends TileEntitySpecialRenderer {

    private final ModelTome modelTome = new ModelTome();
    public static final ResourceLocation texturePath= new ResourceLocation("textures/blocks/tome.png"  );

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float g) {
        TileEntityTome tileEntityTome = (TileEntityTome)tileEntity;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);//bao
        if(tileEntityTome.getWorldObj()!=null)//跳过在背包仓库里面的实体，因为他的世界是null
        {
            EnumDirection direction= tileEntityTome.getBlockType().getDirectionFacing(tileEntity.getBlockMetadata());
            if(direction==EnumDirection.WEST)//  好像是反的
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            if(direction==EnumDirection.EAST)//  好像是反的
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            if(direction==EnumDirection.NORTH)//  好像是反的
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);

            //GL11.glScalef(2.0F, 1.0F, 2.0F);
        }else{
            GL11.glTranslatef((float)x-0.50f , (float)y , (float)z - 0.50F);
            GL11.glRotatef(180.0f, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(1.3125f, 1.3125f, 1.3125f);
        }

        TextureManager manager = Minecraft.getMinecraft().getTextureManager();
        manager.bindTexture(texturePath);
        this.modelTome.renderTome(0.0625f);//0.0625F
        GL11.glPopMatrix();
    }
}
