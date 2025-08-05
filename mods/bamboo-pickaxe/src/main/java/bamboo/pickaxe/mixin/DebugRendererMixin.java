package bamboo.pickaxe.mixin;

import org.lwjgl.opengl.GL11;
import com.mojang.blaze3d.systems.RenderSystem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;

import bamboo.pickaxe.ClientPickaxe;

@Mixin(DebugRenderer.class)
public abstract class DebugRendererMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void render(MatrixStack matrices, Frustum frustum, Immediate vertexConsumers,
            double x, double y, double z, CallbackInfo ci) {
        Box box = ClientPickaxe.areaMine.getArea();
        if (box != null) {
            matrices.translate(box.minX - x, box.minY - y, box.minZ - z);
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLines());

            RenderSystem.assertOnRenderThread();
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            matrices.push();
            VertexRendering.drawBox(matrices, vertexConsumer,
                    0, 0, 0, box.maxX - box.minX, box.maxY - box.minY, box.maxZ - box.minZ,
                    0.9F, 0.9F, 0.9F, 1.0F, 0.5F, 0.5F, 0.5F);
            matrices.pop();

            vertexConsumers.drawCurrentLayer();
            GL11.glEnable((GL11.GL_DEPTH_TEST));
        }
    }
}
