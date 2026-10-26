package com.nocturne.registry;

import com.nocturne.NocturneMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = NocturneMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NocturneMod.MOD_ID);
    public static final RegistryObject<Item> NOCTURN_SPAWN_EGG = ITEMS.register("nocturn_spawn_egg",
        () -> new ForgeSpawnEggItem(ModEntities.NOCTURN, 0x45494A, 0xB1A58A, new Item.Properties()));

    @SubscribeEvent
    public static void addCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == net.minecraft.world.item.CreativeModeTabs.SPAWN_EGGS) {
            event.accept(NOCTURN_SPAWN_EGG);
        }
    }
    private ModItems() {}
}
