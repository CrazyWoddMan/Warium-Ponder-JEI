package crazywoddman.warium_ponder.mixin;

import com.almostreliable.ponderjs.PonderJS;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PonderJS.class, remap = false)
public class PonderJSMixin {

    @Shadow
    private static boolean initialized;
    
    @Inject(
        method = "init",
        at = @At("HEAD"),
        require = 0,
        cancellable = true
    )
    private static void skipResourceReload(CallbackInfo ci) {
        if (!initialized)
            initialized = true;
        ci.cancel();
    }
}