package com.xyfero.steammotion.client;

import com.xyfero.steammotion.entity.EntityHook;
import com.xyfero.steammotion.item.ItemHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderHook extends Render<EntityHook> {
    private final ResourceLocation texture;

    public RenderHook(final RenderManager renderManager, final ResourceLocation texture) {
        super(renderManager);
        this.texture = texture;
    }

    private ModelHook hookModel = new ModelHook();

    public void doRender(EntityHook entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        hookModel = new ModelHook();

        EntityLivingBase entityplayer = entity.getShooter();

        if (entityplayer != null && !this.renderOutlines)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x, (float)y, (float)z);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(1.2f, 1.2f, 1.2f);
            this.bindEntityTexture(entity);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();

            GlStateManager.rotate(-entity.rotationYaw, 0.0F, 1f ,0f);
            GlStateManager.rotate(entity.rotationPitch - 90f, 1.0F, 0.0F, 0.0F);

            if (this.renderOutlines)
            {
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(this.getTeamColor(entity));
            }

            hookModel.render(entity, 0f, 0f ,0f ,0f ,0f, 0.0625f);

            if (this.renderOutlines)
            {
                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();
            }

            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            int k = entityplayer.getPrimaryHand() == EnumHandSide.RIGHT ? 1 : -1;
            ItemStack itemstack = entityplayer.getHeldItemMainhand();

            if (!(itemstack.getItem() instanceof ItemHook))
            {
                k = -k;
            }

            float f7 = entityplayer.getSwingProgress(partialTicks);
            float f8 = MathHelper.sin(MathHelper.sqrt(f7) * (float)Math.PI);
            float f9 = (entityplayer.prevRenderYawOffset + (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * partialTicks) * 0.017453292F;
            double d0 = (double)MathHelper.sin(f9);
            double d1 = (double)MathHelper.cos(f9);
            double d2 = (double)k * 0.35D;

            Vec3d offset;

            if ((renderManager.options == null || renderManager.options.thirdPersonView <= 0) && entityplayer == Minecraft.getMinecraft().player)
            {
                float f10 = this.renderManager.options.fovSetting;
                f10 = f10 / 100.0F;
                offset = new Vec3d((double)k * -0.36D * (double)f10, -0.2D * (double)f10, 0.4D);
                offset = offset.rotatePitch(-(entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * partialTicks) * 0.017453292F);
                offset = offset.rotateYaw(-(entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * partialTicks) * 0.017453292F);
                offset = offset.rotateYaw(f8 * 0.5F);
                offset = offset.rotatePitch(-f8 * 0.7F);
            }
            else
            {
//                d5 = entityplayer.prevPosY + (double)entityplayer.getEyeHeight() + (entityplayer.posY - entityplayer.prevPosY) * (double)partialTicks - 0.45D;
                offset = new Vec3d(- d1 * d2 - d0 * 0.1D, - 1.0D + (entityplayer.isSneaking() ? -0.1875D : 0.0D), - d0 * d2 + d1 * 0.1D);
            }

            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);

            double epPosX = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double)partialTicks + offset.x;
            double epPosY = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double)partialTicks + entityplayer.getEyeHeight() + offset.y;
            double epPosZ = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double)partialTicks + offset.z;

            double ePosX = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
            double ePosY = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks;
            double ePosZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;

            bufferbuilder.pos(x, y, z).color(20,20,20,255).endVertex();
            bufferbuilder.pos(x + epPosX - ePosX, y + epPosY - ePosY, z + epPosZ - ePosZ).color(20,20,20,255).endVertex();

            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(final EntityHook entity) {
        return texture;
    }
}
