package dasbundesland.s4g.mixin;

import dasbundesland.s4g.S4G;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.ServerWorldProperties;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Shadow @Final private static Logger LOGGER;

    /**
     * @author
     */
    @Inject(method = "Lnet/minecraft/server/MinecraftServer;setupSpawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/level/ServerWorldProperties;ZZZ)V", at = @At("TAIL"))
    private static void setupSpawn(ServerWorld serverWorld, ServerWorldProperties serverWorldProperties, boolean bl, boolean bl2, boolean bl3, CallbackInfo ci) {
        S4G.updateConfig();
        if(S4G.getEnabled().get() && S4G.getSetSpawn(serverWorld.getSeed()) != null ){
            serverWorldProperties.setSpawnPos(S4G.getSetSpawn(serverWorld.getSeed()));
        }


    }
}
