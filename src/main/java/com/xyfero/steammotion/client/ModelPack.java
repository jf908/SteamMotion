package com.xyfero.steammotion.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * pack - Xyfero
 * Created using Tabula 7.0.0
 */
public class ModelPack extends ModelBase {
    public ModelRenderer shape1;
    public ModelRenderer shape2;
    public ModelRenderer shape3;
    public ModelRenderer shape5;
    public ModelRenderer shape4;
    public ModelRenderer shape8;
    public ModelRenderer shape9;
    public ModelRenderer shape10;
    public ModelRenderer shape11;
    public ModelRenderer shape9_1;
    public ModelRenderer shape8_1;
    public ModelRenderer shape10_1;

    public ModelPack() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.shape8 = new ModelRenderer(this, 35, 0);
        this.shape8.setRotationPoint(-5.0F, 10.0F, -3.0F);
        this.shape8.addBox(0.0F, 0.0F, 0.0F, 1, 5, 1, 0.0F);
        this.setRotateAngle(shape8, 1.5707963267948966F, 0.0F, 0.0F);
        this.shape3 = new ModelRenderer(this, 26, 0);
        this.shape3.setRotationPoint(6.0F, 1.0F, 6.5F);
        this.shape3.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.setRotateAngle(shape3, -1.0471975511965976F, 1.3962634015954636F, 0.0F);
        this.shape8_1 = new ModelRenderer(this, 35, 0);
        this.shape8_1.setRotationPoint(4.0F, 10.0F, -3.0F);
        this.shape8_1.addBox(0.0F, 0.0F, 0.0F, 1, 5, 1, 0.0F);
        this.setRotateAngle(shape8_1, 1.5707963267948966F, 0.0F, 0.0F);
        this.shape2 = new ModelRenderer(this, 0, 14);
        this.shape2.setRotationPoint(0.0F, 7.0F, 5.0F);
        this.shape2.addBox(-4.0F, 0.0F, 0.0F, 8, 3, 3, 0.0F);
        this.shape11 = new ModelRenderer(this, 2, 2);
        this.shape11.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape11.addBox(-5.0F, -1.0F, 2.0F, 2, 1, 1, 0.0F);
        this.shape9 = new ModelRenderer(this, 34, 0);
        this.shape9.setRotationPoint(4.3F, 0.0F, -2.95F);
        this.shape9.addBox(0.0F, 0.0F, 0.0F, 1, 13, 1, 0.0F);
        this.setRotateAngle(shape9, 0.0F, 0.0F, 0.7853981633974483F);
        this.shape4 = new ModelRenderer(this, 26, 0);
        this.shape4.setRotationPoint(-6.0F, 1.0F, 6.5F);
        this.shape4.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.setRotateAngle(shape4, -1.0471975511965976F, -1.3962634015954636F, 0.0F);
        this.shape1 = new ModelRenderer(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, -1.0F, 2.0F);
        this.shape1.addBox(-5.0F, 0.0F, 0.0F, 10, 11, 3, 0.0F);
        this.shape5 = new ModelRenderer(this, 22, 14);
        this.shape5.setRotationPoint(-2.0F, 1.0F, 5.0F);
        this.shape5.addBox(0.0F, 0.0F, 0.0F, 4, 6, 2, 0.0F);
        this.shape10_1 = new ModelRenderer(this, 34, 0);
        this.shape10_1.setRotationPoint(-5.0F, 1.0F, -3.0F);
        this.shape10_1.addBox(0.0F, 0.0F, 0.0F, 2, 5, 2, 0.0F);
        this.setRotateAngle(shape10_1, 1.5707963267948966F, 0.0F, 0.0F);
        this.shape9_1 = new ModelRenderer(this, 34, 0);
        this.shape9_1.setRotationPoint(-5.0F, 0.7F, -2.9F);
        this.shape9_1.addBox(0.0F, 0.0F, 0.0F, 1, 13, 1, 0.0F);
        this.setRotateAngle(shape9_1, 0.0F, 0.0F, -0.7853981633974483F);
        this.shape10 = new ModelRenderer(this, 34, 0);
        this.shape10.setRotationPoint(3.0F, 1.0F, -3.0F);
        this.shape10.addBox(0.0F, 0.0F, 0.0F, 2, 5, 2, 0.0F);
        this.setRotateAngle(shape10, 1.5707963267948966F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.shape8.render(f5);
        this.shape3.render(f5);
        this.shape8_1.render(f5);
        this.shape2.render(f5);
        this.shape11.render(f5);
        this.shape9.render(f5);
        this.shape4.render(f5);
        this.shape1.render(f5);
        this.shape5.render(f5);
        this.shape10_1.render(f5);
        this.shape9_1.render(f5);
        this.shape10.render(f5);
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
