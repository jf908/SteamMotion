package com.xyfero.steammotion.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * pack - Xyfero
 * Created using Tabula 7.0.0
 */
public class ModelPack_hmm extends ModelBase {
    public ModelRenderer shape1;
    public ModelRenderer shape2;
    public ModelRenderer shape3;
    public ModelRenderer shape5;
    public ModelRenderer shape4;

    public ModelPack_hmm() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.shape1 = new ModelRenderer(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.shape1.addBox(-5.0F, 0.0F, 0.0F, 10, 10, 3, 0.0F);
        this.shape5 = new ModelRenderer(this, 22, 13);
        this.shape5.setRotationPoint(-2.0F, 1.0F, 5.0F);
        this.shape5.addBox(0.0F, 0.0F, 0.0F, 4, 6, 2, 0.0F);
        this.shape2 = new ModelRenderer(this, 0, 13);
        this.shape2.setRotationPoint(0.0F, 7.0F, 5.0F);
        this.shape2.addBox(-4.0F, 0.0F, 0.0F, 8, 3, 3, 0.0F);
        this.shape4 = new ModelRenderer(this, 26, 0);
        this.shape4.mirror = true;
        this.shape4.setRotationPoint(-6.0F, 1.0F, 6.5F);
        this.shape4.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.setRotateAngle(shape4, -1.0471975511965976F, -1.3962634015954636F, 0.0F);
        this.shape3 = new ModelRenderer(this, 26, 0);
        this.shape3.setRotationPoint(6.0F, 1.0F, 6.5F);
        this.shape3.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.setRotateAngle(shape3, -1.0471975511965976F, 1.3962634015954636F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.shape1.render(f5);
        this.shape5.render(f5);
        this.shape2.render(f5);
        this.shape4.render(f5);
        this.shape3.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
