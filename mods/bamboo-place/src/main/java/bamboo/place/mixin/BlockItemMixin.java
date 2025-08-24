package bamboo.place.mixin;

import java.util.List;

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

import static net.minecraft.util.math.Direction.UP;
import static net.minecraft.util.math.Direction.DOWN;
import static net.minecraft.util.math.Direction.EAST;
import static net.minecraft.util.math.Direction.WEST;
import static net.minecraft.util.math.Direction.NORTH;
import static net.minecraft.util.math.Direction.SOUTH;

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

        Vec3d pos = context.getHitPos().subtract(new Vec3d(context.getBlockPos()));
        Direction direction = context.getSide();

        double a, b;
        List<Direction> directions;
        switch (direction) {
            case UP, DOWN:
                a = pos.getX();
                b = pos.getZ();
                directions = List.of(WEST, NORTH, SOUTH, EAST);
                break;
            case EAST, WEST:
                a = pos.getZ();
                b = pos.getY();
                directions = List.of(NORTH, DOWN, UP, SOUTH);
                break;
            case NORTH, SOUTH:
                a = pos.getX();
                b = pos.getY();
                directions = List.of(WEST, DOWN, UP, EAST);
                break;
            default:
                throw new IllegalStateException();
        }

        if (Math.abs(a - 0.5) > 0.15 || Math.abs(b - 0.5) > 0.15) {
            direction = directions.get((a + b > 1 ? 0b10 : 0b00) + (a > b ? 0b01 : 0b00));
        }

        if (hasHopperFacing && direction == UP
                || hasHorizontalFacing && (direction == UP || direction == DOWN)) {
            return;
        }

        cir.setReturnValue(blockState.with(property, direction));
    }
}
