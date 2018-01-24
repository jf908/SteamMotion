package com.xyfero.steammotion.client;

import com.xyfero.steammotion.entity.EntityHook;
import com.xyfero.steammotion.item.ItemHook;
import com.xyfero.steammotion.item.SteamMotionItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
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

            GlStateManager.rotate(entity.rotationYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-entity.rotationPitch - 90f, 1.0F, 0.0F, 0.0F);

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
            double d3 = 0.8D;
            double d4;
            double d5;
            double d6;
            double d7;

            if ((this.renderManager.options == null || this.renderManager.options.thirdPersonView <= 0) && entityplayer == Minecraft.getMinecraft().player)
            {
                float f10 = this.renderManager.options.fovSetting;
                f10 = f10 / 100.0F;
                Vec3d vec3d = new Vec3d((double)k * -0.36D * (double)f10, -0.45D * (double)f10, 0.4D);
                vec3d = vec3d.rotatePitch(-(entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * partialTicks) * 0.017453292F);
                vec3d = vec3d.rotateYaw(-(entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * partialTicks) * 0.017453292F);
                vec3d = vec3d.rotateYaw(f8 * 0.5F);
                vec3d = vec3d.rotatePitch(-f8 * 0.7F);
                d4 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double)partialTicks + vec3d.x;
                d5 = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double)partialTicks + vec3d.y;
                d6 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double)partialTicks + vec3d.z;
                d7 = (double)entityplayer.getEyeHeight();
            }
            else
            {
                d4 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double)partialTicks - d1 * d2 - d0 * 0.8D;
                d5 = entityplayer.prevPosY + (double)entityplayer.getEyeHeight() + (entityplayer.posY - entityplayer.prevPosY) * (double)partialTicks - 0.45D;
                d6 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double)partialTicks - d0 * d2 + d1 * 0.8D;
                d7 = entityplayer.isSneaking() ? -0.1875D : 0.0D;
            }

            double d13 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
            double d8 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks;
            double d9 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
            double d10 = (double)((float)(d4 - d13));
            double d11 = (double)((float)(d5 - d8)) + d7;
            double d12 = (double)((float)(d6 - d9));
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            int l = 16;

            for (int i1 = 0; i1 <= 16; ++i1)
            {
                float f11 = (float)i1 / 16.0F;
                bufferbuilder.pos(x + d10 * (double)f11, y + d11 * (double)(f11 * f11 + f11) * 0.5D, z + d12 * (double)f11).color(20, 20, 20, 255).endVertex();
            }

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
