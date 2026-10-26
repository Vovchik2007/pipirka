package com.nocturne.registry;

import com.nocturne.NocturneMod;
import com.nocturne.entity.NocturnEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = NocturneMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, NocturneMod.MOD_ID);

    public static final RegistryObject<EntityType<NocturnEntity>> NOCTURN = ENTITY_TYPES.register("nocturn",
        () -> EntityType.Builder.of(NocturnEntity::new, MobCategory.MONSTER)
            .sized(1.15F, 2.85F)
            .clientTrackingRange(10)
            .build("nocturne:nocturn"));

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(NOCTURN.get(), NocturnEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(NOCTURN.get(), SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
            SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    private ModEntities() {}
}
