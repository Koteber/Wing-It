package io.github.kotenahui.wingit.item;

import io.github.kotenahui.wingit.WingIt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.item.Items;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Set;

public class FeatherBag extends TemplateItem {
    public FeatherBag(Identifier identifier) {
        super(identifier);
        this.maxCount = 1;
        this.setMaxDamage(128);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        //updateTexture(stack);
        if (GetState(stack) == null || stack.getDamage() >= 128) {
            stack.setDamage(128);
            SetState("empty", stack);
            this.setTextureId(getTextureId(stack));
        }
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        stack.setDamage(128);
        SetState("empty", stack);
        this.setTextureId(getTextureId(stack));
    }

    @Override
    public ItemStack use(ItemStack stack, World world, PlayerEntity user){
        if (user.isSneaking()) {                                        //Take out
            if (stack.getDamage() < 128) {
                takeOut(stack, world, user);
            }
        }
        else {                                                          //Put in
            if (stack.getDamage() > 0) {
                putIn(stack, world, user);
            }
        }
        updateTexture(stack);
        return stack;
    }

    public void takeOut(ItemStack stack, World world, PlayerEntity user) {

        if (GetState(stack).equals("feather")) { // Feather
            ItemStack feather = new ItemStack(Item.FEATHER);

            feather.count += 127 - stack.getDamage();
            stack.damage(128 - stack.getDamage(), user);

            this.setTextureId(getTextureId(stack));
        }
        else if (GetState(stack).equals("dense_feather")) { // Dense Feather
            ItemStack feather = new ItemStack(WingIt.item_DENSE_FEATHER);

            feather.count += (127 - stack.getDamage()) / 2;
            stack.damage(128 - stack.getDamage(), user);

            user.inventory.addStack(feather);
        }
        SetState("empty", stack);
        this.setTextureId(getTextureId(stack));
    }
    public void putIn(ItemStack stack, World world, PlayerEntity user) {
        WingIt.LOGGER.info("putIn start state: " +GetState(stack));
        if (GetState(stack).equals("empty")) {                      // Empty
            if (user.inventory.remove(WingIt.item_DENSE_FEATHER.id)) {
                SetState("dense_feather", stack);
                stack.setDamage(stack.getDamage() - 2);
                this.setTextureId(getTextureId(stack));
            }
            else if (user.inventory.remove(Item.FEATHER.id)) {
                SetState("feather", stack);
                stack.setDamage(stack.getDamage() - 1);
                this.setTextureId(getTextureId(stack));
            }
        }

        else if (GetState(stack).equals("feather")) {               // Feather
            if (user.inventory.remove(Item.FEATHER.id)) {
                stack.setDamage(stack.getDamage() - 1);
            }
        }

        else if (GetState(stack).equals("dense_feather")) {         // Dense Feather
            if (user.inventory.remove(WingIt.item_DENSE_FEATHER.id)) {
                WingIt.LOGGER.info("inside inside df");
                stack.setDamage(stack.getDamage() - 2);
            }
        }

        /*for (int i = 0; i < stack.getDamage(); i++) {
            if (user.inventory.remove(Item.FEATHER.id)) {
                stack.setDamage(stack.getDamage() - 1);
            }
        }*/
    }
    /*public void updateTextureold(ItemStack stack) {
        if (GetState(stack).equals("empty")) {                                               // Empty
            this.setTexture(WingIt.NAMESPACE.id("item/feather_bag_empty"));
        }

        else if (GetState(stack).equals("feather")) {                                        // Feather
            if (stack.getDamage() > 64) {
                this.setTexture(WingIt.NAMESPACE.id("item/feather_bag_feathers_half"));
            }
            else {
                this.setTexture(WingIt.NAMESPACE.id("item/feather_bag_feathers_full"));
            }
        }

        else if (GetState(stack).equals("dense_feather")) {                                  // Dense Feather
            if (stack.getDamage() > 64) {
                this.setTexture(WingIt.NAMESPACE.id("item/feather_bag_dense_feathers_half"));
            }
            else {
                this.setTexture(WingIt.NAMESPACE.id("item/feather_bag_dense_feathers_full"));
            }
        }
    } */

    public void updateTexture(ItemStack stack) {
        if (GetState(stack).equals("empty")) {                                               // Empty
            //stack.getItem().setTextureId(WingIt.texture_FEATHER_BAG_EMPTY);
            //this.setTextureId(WingIt.texture_FEATHER_BAG_EMPTY);
            this.setTextureId(getTextureId(stack));
        }

        else if (GetState(stack).equals("feather")) {                                        // Feather
            if (stack.getDamage() > 64) {
                //stack.getItem().setTextureId(WingIt.texture_FEATHER_BAG_FEATHER_HALF);
                //this.setTextureId(WingIt.texture_FEATHER_BAG_FEATHER_HALF);
            }
            else {
                //stack.getItem().setTextureId(WingIt.texture_FEATHER_BAG_FEATHER_FULL);
                //this.setTextureId(WingIt.texture_FEATHER_BAG_FEATHER_FULL);
            }
        }

        else if (GetState(stack).equals("dense_feather")) {                                  // Dense Feather
            if (stack.getDamage() > 64) {
                //stack.getItem().setTextureId(WingIt.texture_FEATHER_BAG_DENSE_FEATHER_HALF);
                //this.setTextureId(WingIt.texture_FEATHER_BAG_DENSE_FEATHER_HALF);
            }
            else {
                //stack.getItem().setTextureId(WingIt.texture_FEATHER_BAG_DENSE_FEATHER_FULL);
                //this.setTextureId(WingIt.texture_FEATHER_BAG_DENSE_FEATHER_FULL);
            }
        }
    }

    public void SetState(String state, ItemStack stack) {
        stack.getStationNbt().putString("state", state);
    }

    public String GetState(ItemStack stack) {
        return stack.getStationNbt().getString("state");
    }
    public int getTextureId(ItemStack stack) {
        if (GetState(stack).equals("empty")) {
            return WingIt.texture_FEATHER_BAG_EMPTY;
        }
        else if (GetState(stack).equals("feather")) {
            if (stack.getDamage() < 64) {
                return WingIt.texture_FEATHER_BAG_FEATHER_FULL;
            }
            else {
                return WingIt.texture_FEATHER_BAG_FEATHER_HALF;
            }
        }
        else if (GetState(stack).equals("dense_feather")) {
            if (stack.getDamage() < 64) {
                return WingIt.texture_FEATHER_BAG_DENSE_FEATHER_FULL;
            }
            else {
                return WingIt.texture_FEATHER_BAG_DENSE_FEATHER_HALF;
            }
        }
        return WingIt.texture_FEATHER_BAG_EMPTY;
    }
}
