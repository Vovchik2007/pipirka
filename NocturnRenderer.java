package com.nocturne.client.render;

import com.nocturne.client.model.NocturnModel;
import com.nocturne.entity.NocturnEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class NocturnRenderer extends GeoEntityRenderer<NocturnEntity> {
    public NocturnRenderer(EntityRendererProvider.Context context) {
        super(context, new NocturnModel());
        this.shadowRadius = 1.4F;
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
