package com.xyfero.steammotion.client;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

public class RenderPack extends ModelBiped {

    private ModelPack model = new ModelPack();

    public RenderPack() {
        super();
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
//        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        model.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }
}
