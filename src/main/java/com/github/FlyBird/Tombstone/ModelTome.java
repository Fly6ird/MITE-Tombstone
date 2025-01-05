package com.github.FlyBird.Tombstone;

import net.minecraft.Entity;
import net.minecraft.ModelBase;
import net.minecraft.ModelRenderer;

public class ModelTome extends ModelBase {
    private final ModelRenderer tome;
    private final ModelRenderer group2;
    private final ModelRenderer group3;
    private final ModelRenderer group;

    public ModelTome() {
        textureWidth = 64;
        textureHeight = 64;

        tome = new ModelRenderer(this);
        tome.setRotationPoint(-5.0F, 12.0F, 0.0F);


        group2 = new ModelRenderer(this);
        group2.setRotationPoint(0.0F, 0.0F, 0.0F);
        tome.addChild(group2);
        this.group2.setTextureOffset(30, 0).addBox(7.5F, 0.0F, -3.0F, 3, 3, 6, 0.0F);
        this.group2.setTextureOffset(0, 0).addBox(7.5F, -3.0F, -6.0F, 3, 3, 12, 0.0F);
        this.group2.setTextureOffset(30, 18).addBox(7.5F, -6.0F, -1.5F, 3, 3, 3, 0.0F);

        group3 = new ModelRenderer(this);
        group3.setRotationPoint(0.0F, 0.0F, 0.0F);
        tome.addChild(group3);
        this.group3.setTextureOffset(0, 0).addBox(7.5F, 9.0F, -6.0F, 3, 3, 12, 0.0F);
        this.group3.setTextureOffset(30, 0).addBox(7.5F, 6.0F, -3.0F, 3, 3, 6, 0.0F);
        this.group3.setTextureOffset(30, 18).addBox(7.5F, 3.0F, -1.5F, 3, 3, 3, 0.0F);

        group = new ModelRenderer(this);
        group.setRotationPoint(9.0F, 12.0F, 0.0F);
        tome.addChild(group);
        this.group.setTextureOffset(0, 19).addBox(-10.5F, -2.0F, -3.0F, 9, 2, 6, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity,f, f1, f2, f3, f4, f5);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, f and f1 are used for animating the movement of arms
     * and legs, where f represents the time(so that arms and legs swing back and forth) and f1 represents how
     * "far" arms and legs can swing at most.
     */
    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {

    }

    /**
     *	Sets the rotation of a ModelRenderer. Only called if the ModelRenderer has a rotation
     */
    public void setRotation(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void renderTome(float rotation) {
        tome.render(rotation);
    }
}
