package io.github.kotenahui.wingit.item;

import io.github.kotenahui.wingit.WingIt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.ArmorTextureProvider;
import net.modificationstation.stationapi.api.template.item.TemplateArmorItem;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import org.lwjgl.util.vector.Vector3f;

public class Wings extends TemplateArmorItem implements ArmorTextureProvider
{

    int flapTick;
    public boolean canFlap;
    public boolean holdingJump;
    public Wings(Identifier identifier, int j, int k, int slot) {
        super(identifier, j, k, slot);
        flapTick = 0;
    }
    @Override
    public Identifier getTexture(ArmorItem armorItem) {
        return WingIt.NAMESPACE.id("wing_layer");
    }
    Vector3f featherVel = new Vector3f(0.3f, 0.75f, 0.3f);
    Vector3f denseFeatherVel = new Vector3f(0.9f, 0.6f, 0.9f);
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        if (flapTick != 0) {
            flapTick--;
        }
        if (((ClientPlayerEntity)entity).input.jumping && (entity.onGround || entity.checkWaterCollisions())) {
            holdingJump = true;
            canFlap = false;
        }
        if (((ClientPlayerEntity)entity).input.jumping && !holdingJump) {
            canFlap = true;
            holdingJump = true;
        } else if (!((ClientPlayerEntity)entity).input.jumping) {
            holdingJump = false;
        }
        if (!holdingJump) return;
        if (entity.checkWaterCollisions()) return;
        if (((PlayerEntity)entity).inventory.armor[2] == null) return;
        if (((PlayerEntity)entity).inventory.getArmorStack(2).itemId != WingIt.item_WINGS.id) return;

        //if (holdingJump) hover(entity);

        if (canFlap && flapTick == 0) flap(entity, world);
    }
    public void flap(Entity entity, World world) {
        String str = tryConsumeFeather(entity);
        WingIt.LOGGER.info(str);
        if (str != null) {
            canFlap = false;
            flapTick = 8;
            Vector3f inputVel = str.equals("feather") ? featherVel : denseFeatherVel;

            double velocityX = (double)(-MathHelper.sin(entity.yaw / 180.0F * (float)Math.PI) * MathHelper.cos(entity.pitch / 180.0F * (float)Math.PI));
            double velocityZ = (double)(MathHelper.cos(entity.yaw / 180.0F * (float)Math.PI) * MathHelper.cos(entity.pitch / 180.0F * (float)Math.PI));
            double velocityY = (double)(-MathHelper.sin(entity.pitch / 180.0F * (float)Math.PI));
            entity.addVelocity((inputVel.x / 3) * velocityX, inputVel.y, (inputVel.z / 3) * velocityZ);

            NbtCompound nbt = new NbtCompound();
            entity.write(nbt);
            nbt.putFloat("FallDistance", (float)entity.velocityY - 2f);
            entity.read(nbt);

            world.playSound(entity.x, entity.y, entity.z, "wingit:mecha_wing_flap", 1, str.equals("feather") ? 1 : 1.3f);
        }
    }
    public void hover(Entity entity) {
        if (entity.velocityY < 0) {
            entity.addVelocity(0, 0.025  , 0);
        }
    }

    public String tryConsumeFeather(Entity entity) {
        ItemStack selectedItem = ((ClientPlayerEntity) entity).inventory.getSelectedItem();

        if (selectedItem == null) return null;

        if (selectedItem.getItem() == WingIt.item_FEATHER_BAG) {
            FeatherBag bag = (FeatherBag) selectedItem.getItem(); //Selected feather

            if (selectedItem.getDamage() < 128) {
                if (bag.GetState(selectedItem).equals("feather")) {
                    selectedItem.damage(1, entity);
                    return "feather";
                } else if (bag.GetState(selectedItem).equals("dense_feather")) {
                    selectedItem.damage(2, entity);
                    return "dense_feather";
                }
            }
        } else if (selectedItem.getItem() == Item.FEATHER) { //Selected feather
            ((ClientPlayerEntity) entity).inventory.removeStack(((ClientPlayerEntity) entity).inventory.selectedSlot, 1);
            return "feather";
        } else if (selectedItem.getItem() == WingIt.item_DENSE_FEATHER) { //Selected dense feather
            ((ClientPlayerEntity) entity).inventory.removeStack(((ClientPlayerEntity) entity).inventory.selectedSlot, 1);
            return "dense_feather";
        }
        else if (((ClientPlayerEntity)entity).isSneaking){ //Feather
                if (((ClientPlayerEntity) entity).inventory.remove(Item.FEATHER)) {
                        return "feather"
                }
        }
        else if (!((ClientPlayerEntity)entity).isSneaking) { //Dense feather
                if (((ClientPlayerEntity) entity).inventory.remove(WingIt.item_DENSE_FEATHER)) {
                        return "dense_feather"
                }
        }
        return null;
    }
}
