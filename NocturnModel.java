package com.nocturne.client.model;

import com.nocturne.NocturneMod;
import com.nocturne.entity.NocturnEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.core.animation.AnimationState;

public class NocturnModel extends GeoModel<NocturnEntity> {
    @Override
    public void setCustomAnimations(NocturnEntity entity, long instanceId, AnimationState<NocturnEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);
        float power = (float) entity.getNightPower();
        // A clean one-block cobblestone cube by day. It shrinks away as the stone guardian emerges.
        getBone("stone_block").ifPresent(bone -> {
            float blockScale = Math.max(0.001F, 1.0F - power);
            bone.setScaleX(blockScale); bone.setScaleY(blockScale); bone.setScaleZ(blockScale);
            bone.setHidden(power >= 0.995F);
        });
        // Hide all creature parts during the dormant phase; at dusk reveal a massive gorilla silhouette.
        float reveal = Math.max(0.001F, power);
        getBone("body").ifPresent(b -> { b.setHidden(power < 0.015F); b.setScaleX(0.75F + 0.85F*reveal); b.setScaleY(0.75F + 0.65F*reveal); b.setScaleZ(0.8F + 0.8F*reveal); });
        getBone("head").ifPresent(b -> { b.setHidden(power < 0.015F); b.setScaleX(0.9F + 0.3F*reveal); b.setScaleY(0.9F + 0.3F*reveal); b.setScaleZ(0.9F + 0.3F*reveal); });
        for (String name : new String[] {"left_arm", "right_arm"}) getBone(name).ifPresent(b -> { b.setHidden(power < 0.015F); b.setScaleX(0.85F + 0.9F*reveal); b.setScaleY(0.9F + 0.75F*reveal); b.setScaleZ(0.9F + 0.8F*reveal); });
        for (String name : new String[] {"left_leg", "right_leg"}) getBone(name).ifPresent(b -> { b.setHidden(power < 0.015F); b.setScaleX(0.9F + 0.35F*reveal); b.setScaleY(0.8F + 0.15F*reveal); b.setScaleZ(0.9F + 0.35F*reveal); });
        getBone("back_spikes").ifPresent(b -> { b.setHidden(power < 0.08F); b.setScaleX(0.9F + 0.5F*reveal); b.setScaleY(0.9F + 0.5F*reveal); b.setScaleZ(0.9F + 0.5F*reveal); });
    }
    @Override public ResourceLocation getModelResource(NocturnEntity animatable) { return new ResourceLocation(NocturneMod.MOD_ID, "geo/nocturn.geo.json"); }
    @Override public ResourceLocation getTextureResource(NocturnEntity animatable) { return new ResourceLocation(NocturneMod.MOD_ID, "textures/entity/nocturn.png"); }
    @Override public ResourceLocation getAnimationResource(NocturnEntity animatable) { return new ResourceLocation(NocturneMod.MOD_ID, "animations/nocturn.animation.json"); }
}
