package com.nocturne;

import com.nocturne.registry.ModEntities;
import com.nocturne.registry.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NocturneMod.MOD_ID)
public final class NocturneMod {
    public static final String MOD_ID = "nocturne";

    public NocturneMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITY_TYPES.register(bus);
        ModItems.ITEMS.register(bus);
    }
}
