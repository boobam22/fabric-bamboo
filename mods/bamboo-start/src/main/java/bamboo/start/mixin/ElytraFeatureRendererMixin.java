package bamboo.start.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.ItemStack;
import net.minecraft.component.DataComponentTypes;

@Mixin(ElytraFeatureRenderer.class)
public abstract class ElytraFeatureRendererMixin {
    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/equipment/EquipmentRenderer;render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;)V"))
    private void render(Args args) {
        ItemStack stack = args.get(3);
        if (stack.get(DataComponentTypes.GLIDER) != null) {
            args.set(1, EquipmentAssetKeys.ELYTRA);
        }
    }
}
