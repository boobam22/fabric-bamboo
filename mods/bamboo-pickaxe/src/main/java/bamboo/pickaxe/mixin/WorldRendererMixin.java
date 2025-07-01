package bamboo.pickaxe.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;

import bamboo.pickaxe.Pickaxe;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow
    private MinecraftClient client;
    @Shadow
    private BufferBuilderStorage bufferBuilders;

    @Inject(method = "renderMain", at = @At("TAIL"))
    private void rerenderMainnder(CallbackInfo ci) {
        Box box = Pickaxe.areaMine.getArea();
        if (box != null) {
            MatrixStack matricxStack = new MatrixStack();
            Camera camera = this.client.gameRenderer.getCamera();
            OutlineVertexConsumerProvider consumerProvider = this.bufferBuilders.getOutlineVertexConsumers();

            matricxStack.push();
            matricxStack.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);
            VertexConsumer vertexConsumer = consumerProvider.getBuffer(RenderLayer.getLines());
            VertexRendering.drawBox(matricxStack, vertexConsumer, box, 0.9F, 0.9F, 0.9F, 1.0F);
            matricxStack.pop();
        }
    }
}
