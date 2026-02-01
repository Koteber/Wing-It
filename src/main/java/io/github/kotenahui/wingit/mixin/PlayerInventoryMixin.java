package io.github.kotenahui.wingit.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Shadow
    public ItemStack[] armor;

    @Shadow
    public int selectedSlot;

    @Shadow
    public PlayerEntity player;

    @Inject(method = "inventoryTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;inventoryTick(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;IZ)V"))
    public void callOnArmor(CallbackInfo ci) {
        for(int var1 = 0; var1 < this.armor.length; ++var1) {
            if (this.armor[var1] != null) {
                this.armor[var1].inventoryTick(this.player.world, this.player, var1, this.selectedSlot == var1);
            }
        }
    }
}
