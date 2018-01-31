package com.xyfero.steammotion.client;

import com.xyfero.steammotion.SteamMotion;
import com.xyfero.steammotion.tileentity.TileEntityBoiler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GLSync;

public class RenderBoiler extends TileEntitySpecialRenderer<TileEntityBoiler> {
    private static ModelPack model = new ModelPack();
    private static ResourceLocation rl;

    public RenderBoiler() {
        rl = new ResourceLocation(SteamMotion.MODID, "textures/models/armor/steam_pack.png");
    }

    @Override
    public void render(TileEntityBoiler te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        ItemStack slotIn = te.getStackInSlot(0);
        if(slotIn.isEmpty()) return;
        GlStateManager.pushMatrix();
        bindTexture(rl);

        GlStateManager.translate(0.5, 1, 0.5);
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180, 1, 0, 0);
        model.render(null, 0, 0, 0, 0, 0, .08f);

        GlStateManager.translate(0, 0.5, -0.45);
        double sf = 0.01;
        GlStateManager.scale(sf, sf, sf);
        int progress = (int)(double)100 * (slotIn.getMaxDamage() - slotIn.getMetadata())/slotIn.getMaxDamage();
        String str = Integer.toString(progress) + "%";
        getFontRenderer().drawString(str, -getFontRenderer().getStringWidth(str)/2, 0, 0);
        GlStateManager.popMatrix();
    }
}
