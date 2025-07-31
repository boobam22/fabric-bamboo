package bamboo.inventory.action;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.HopperScreen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.TradeOutputSlot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.ItemStack;

import bamboo.inventory.mixin.HandledScreenAccessor;

public class Util {
    private static Screen getScreen() {
        return MinecraftClient.getInstance().currentScreen;
    }

    public static boolean isInventoryScreen() {
        return getScreen() instanceof InventoryScreen;
    }

    public static boolean isChestScreen() {
        Screen screen = getScreen();
        return screen instanceof GenericContainerScreen || screen instanceof ShulkerBoxScreen;
    }

    public static boolean isContainerScreen() {
        Screen screen = getScreen();
        return isChestScreen() || screen instanceof Generic3x3ContainerScreen || screen instanceof HopperScreen;
    }

    public static boolean isCraftingScreen() {
        return getScreen() instanceof CraftingScreen;
    }

    public static boolean isMerchantScreen() {
        return getScreen() instanceof MerchantScreen;
    }

    public static Slot getFocusedSlot() {
        return ((HandledScreenAccessor) getScreen()).bamboo_getFocusedSlot();
    }

    public static boolean isCraftingResultSlot(Slot slot) {
        return slot instanceof CraftingResultSlot;
    }

    public static boolean isTradeOutputSlot(Slot slot) {
        return slot instanceof TradeOutputSlot;
    }

    public static ItemStack getCursorStack() {
        return ((HandledScreen<?>) getScreen()).getScreenHandler().getCursorStack();
    }

    private static void clickSlot(Slot slot, int button, SlotActionType type) {
        ((HandledScreenAccessor) getScreen()).bamboo_onMouseClick(slot, slot.id, button, type);
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
