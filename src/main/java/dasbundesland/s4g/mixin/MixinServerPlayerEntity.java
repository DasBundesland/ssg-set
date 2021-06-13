package dasbundesland.s4g.mixin;

import dasbundesland.s4g.S4G;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {
    /**
     * @author
     */
    @Overwrite
    private void moveToSpawn(ServerWorld world) {
        S4G.updateConfig();
        if(S4G.getEnabled().get()){
            BlockPos blockPos = world.getSpawnPos();
            ((ServerPlayerEntity)(Object) this).refreshPositionAndAngles(blockPos, 0.0F, 0.0F);
            while(!world.doesNotCollide((ServerPlayerEntity) (Object) this) && ((ServerPlayerEntity)(Object) this).getY() < 255.0D) {
                ((ServerPlayerEntity)(Object) this).updatePosition(((ServerPlayerEntity)(Object) this).getX(), ((ServerPlayerEntity)(Object) this).getY() + 1.0D, ((ServerPlayerEntity)(Object) this).getZ());
            }
        }

    }

}
