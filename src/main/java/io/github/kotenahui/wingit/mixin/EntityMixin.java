package io.github.kotenahui.wingit.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;

@SuppressWarnings("unused")
@Mixin(Entity.class)
public class EntityMixin {

    /**
     * @author Kote
     * @reason Adding this comment just so this piece of software fucks off
     */
    @Overwrite
    public boolean bypassesSteppingEffects() {
        Entity entity = Entity.class.cast(this);
        NbtCompound nbt = new NbtCompound();
        entity.write(nbt);
        return nbt.getBoolean("OnGround");
    }
}
