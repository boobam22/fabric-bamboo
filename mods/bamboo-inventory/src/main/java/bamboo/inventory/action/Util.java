package bamboo.inventory.action;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.entity.player.PlayerInventory;

import bamboo.inventory.mixin.HandledScreenAccessor;

public class Util {
    public static List<Slot> findPlayerInventory(List<Slot> slots) {
        int start = slots.stream()
                .filter(slot -> slot.inventory instanceof PlayerInventory && slot.getClass() == Slot.class)
                .findFirst().get().id;
        int end = start + 27;

        if (slots.size() >= end) {
            return slots.subList(start, end);
        }

        return List.of();
    }

    private static void clickSlot(Slot slot, int button, SlotActionType type) {
        ((HandledScreenAccessor) MinecraftClient.getInstance().currentScreen)
                .bamboo_onMouseClick(slot, slot.id, button, type);
    }

    public static void leftClick(Slot slot) {
        clickSlot(slot, GLFW.GLFW_MOUSE_BUTTON_LEFT, SlotActionType.PICKUP);
    }

    public static void rightClick(Slot slot) {
        clickSlot(slot, GLFW.GLFW_MOUSE_BUTTON_RIGHT, SlotActionType.PICKUP);
    }

    public static void quickMove(Slot slot) {
        clickSlot(slot, GLFW.GLFW_MOUSE_BUTTON_LEFT, SlotActionType.QUICK_MOVE);
    }

    public static void dropOne(Slot slot) {
        clickSlot(slot, 0, SlotActionType.THROW);
    }

    public static void dropOneStack(Slot slot) {
        clickSlot(slot, 1, SlotActionType.THROW);
    }
}
