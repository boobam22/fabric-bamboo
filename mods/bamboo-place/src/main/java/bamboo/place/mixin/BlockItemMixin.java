package bamboo.place.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.ChestType;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    private void getPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> cir) {
        if (context.getPlayer().isSneaking()) {
            return;
        }

        BlockState blockState = cir.getReturnValue();
        if (blockState == null) {
            return;
        }
        if (blockState.get(Properties.CHEST_TYPE, ChestType.SINGLE) != ChestType.SINGLE) {
            return;
        }

        boolean hasHorizontalFacing = false;
        boolean hasHopperFacing = false;
        Property<Direction> property;
        if (blockState.contains(Properties.FACING)) {
            property = Properties.FACING;
        } else if (blockState.contains(Properties.HORIZONTAL_FACING)) {
            hasHorizontalFacing = true;
            property = Properties.HORIZONTAL_FACING;
        } else if (blockState.contains(Properties.HOPPER_FACING)) {
            hasHopperFacing = true;
            property = Properties.HOPPER_FACING;
        } else {
            return;
        }

        Vec3d pos = context.getHitPos().subtract(new Vec3d(context.getBlockPos())).multiply(100);
        int x = Math.max(Math.min((int) Math.floor(pos.getX()), 100), 0);
        int y = Math.max(Math.min((int) Math.floor(pos.getY()), 100), 0);
        int z = Math.max(Math.min((int) Math.floor(pos.getZ()), 100), 0);

        Direction direction = null;
        if (check(x, y, z)) {
            direction = Direction.EAST;
        } else if (check(100 - x, y, z)) {
            direction = Direction.WEST;
        } else if (!hasHopperFacing && !hasHorizontalFacing && check(y, x, z)) {
            direction = Direction.UP;
        } else if (!hasHorizontalFacing && check(100 - y, x, z)) {
            direction = Direction.DOWN;
        } else if (check(z, x, y)) {
            direction = Direction.SOUTH;
        } else if (check(100 - z, x, y)) {
            direction = Direction.NORTH;
        }

        if (direction == null) {
            if (x == 0) {
                direction = Direction.EAST;
            } else if (x == 100) {
                direction = Direction.WEST;
            } else if (z == 0) {
                direction = Direction.SOUTH;
            } else if (z == 100) {
                direction = Direction.NORTH;
            } else if (hasHopperFacing) {
                direction = Direction.DOWN;
            }
        }

        if (direction == null) {
            return;
        }

        cir.setReturnValue(blockState.with(property, direction));
    }

    private static boolean inCenter(int x, int y, int z) {
        return x == 0 && y > 33 && y < 67 && z > 33 && z < 67;
    }

    private static boolean inEdge(int x, int y, int z) {
        return x != 0 && x <= 33 && Math.max(Math.min(y, 100 - y), Math.min(z, 100 - z)) >= x;
    }

    private static boolean check(int x, int y, int z) {
        return inCenter(x, y, z) || inEdge(100 - x, y, z);
    }
}
