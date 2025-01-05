package com.github.FlyBird.Tombstone.mixins;

import com.github.FlyBird.Tombstone.BlockTome;
import com.github.FlyBird.Tombstone.TileEntityTome;
import net.minecraft.Block;
import net.minecraft.RenderBlocks;
import net.minecraft.TileEntityRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({RenderBlocks.class})
public class RenderBlocksMixin {
  @Inject(method = {"renderItemIn3d"}, at = {@At("HEAD")}, cancellable = true)
  private static void register(int renderType, CallbackInfoReturnable<Boolean> cir) {
    if (renderType == BlockTome.tomeRenderType)
      cir.setReturnValue(Boolean.valueOf(true)); 
  }
  
  @Inject(method = {"renderBlockAsItem"}, at = {@At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/Block;getRenderType()I")})
  private void register(Block par1Block, int par2, float par3, CallbackInfo ci) {
    int renderType = par1Block.getRenderType();
    if (renderType == BlockTome.tomeRenderType) {
      GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
      TileEntityRenderer.instance.renderTileEntityAt(new TileEntityTome(), 0.0D, 0.0D, 0.0D, 0.0F);
      GL11.glEnable(32826);
    } 
  }
}