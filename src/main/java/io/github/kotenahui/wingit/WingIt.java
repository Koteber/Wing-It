package io.github.kotenahui.wingit;

import io.github.kotenahui.wingit.item.FeatherBag;
import io.github.kotenahui.wingit.item.Wings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unused")
public class WingIt {
    @SuppressWarnings("UnstableApiUsage")
    public static final Namespace NAMESPACE = Namespace.resolve();

    public static final Logger LOGGER = NAMESPACE.getLogger();

    public static Item[] items;
    public static Item item_WINGS;
    public static Item item_FEATHER_BAG;
    public static Item item_DENSE_FEATHER;

    public static int texture_FEATHER_BAG_EMPTY;
    public static int texture_FEATHER_BAG_FEATHER_HALF;
    public static int texture_FEATHER_BAG_FEATHER_FULL;
    public static int texture_FEATHER_BAG_DENSE_FEATHER_HALF;
    public static int texture_FEATHER_BAG_DENSE_FEATHER_FULL;

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        item_WINGS = new Wings(NAMESPACE.id("wings"), 0, 2, 1).setTranslationKey(NAMESPACE, "wings");
        item_FEATHER_BAG = new FeatherBag(NAMESPACE.id("feather_bag")).setTranslationKey(NAMESPACE, "feather_bag");
        item_DENSE_FEATHER = new TemplateItem(NAMESPACE.id("dense_feather")).setTranslationKey(NAMESPACE, "dense_feather").setMaxCount(32);

        items = new Item[]{
                item_WINGS,
                item_FEATHER_BAG
        };
    }
    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        item_FEATHER_BAG.setTexture(NAMESPACE.id("item/feather_bag_empty"));

        //region Texture Id's

            //region Feather Bag
            texture_FEATHER_BAG_EMPTY = Atlases.getGuiItems().addTexture(NAMESPACE.id("item/feather_bag_empty")).index;
            texture_FEATHER_BAG_FEATHER_HALF = Atlases.getGuiItems().addTexture(NAMESPACE.id("item/feather_bag_feathers_half")).index;
            texture_FEATHER_BAG_FEATHER_FULL = Atlases.getGuiItems().addTexture(NAMESPACE.id("item/feather_bag_feathers_full")).index;
            texture_FEATHER_BAG_DENSE_FEATHER_HALF = Atlases.getGuiItems().addTexture(NAMESPACE.id("item/feather_bag_dense_feathers_half")).index;
            texture_FEATHER_BAG_DENSE_FEATHER_FULL = Atlases.getGuiItems().addTexture(NAMESPACE.id("item/feather_bag_dense_feathers_full")).index;
            //endregion

        //endregion
    }
    public static int indexOf(int itemId, PlayerInventory inventory) {
        for(int i = 0; i < inventory.main.length; i++) {
            if (inventory.main[i] != null && inventory.main[i].itemId == itemId) {
                return i;
            }
        }

        return -1;
    }
    public static int stackIndex(ItemStack stack, PlayerInventory inventory) {
        for(int i = 0; i < inventory.main.length; i++) {
            if (inventory.main[i] != null && inventory.main[i] == stack) {
                return i;
            }
        }
        return -1;
    }
    public static boolean removeAtSlot(int slot, PlayerInventory inventory) {
        if (inventory.main[slot] == null) {
            return false;
        }
        if (--inventory.main[slot].count <= 0) {
            inventory.main[slot] = null;
        }
        return true;
    }
    public static int[] indexesOf(int itemId, PlayerInventory inventory) {
        ArrayList<Integer> array0 = new ArrayList<>();
        for(int i = 0; i < inventory.main.length; i++) {
            if (inventory.main[i] != null && inventory.main[i].itemId == itemId) {
                array0.add(i);
            }
        }
        Integer[] array1 = array0.toArray(new Integer[array0.size()]);
        return Arrays.stream(array1).mapToInt(Integer::intValue).toArray();
    }
    public static ItemStack[] stacksOutOfIndexes(int[] indexes, PlayerInventory inventory) {
        ArrayList<ItemStack> stacks0 = new ArrayList<>();
        for (int i = 0; i < inventory.main.length; i++) {
            if (inventory.main[i] != null) {
                stacks0.add(inventory.main[i]);
            }
        }
        return stacks0.toArray(new ItemStack[stacks0.size()]);
    }
}
