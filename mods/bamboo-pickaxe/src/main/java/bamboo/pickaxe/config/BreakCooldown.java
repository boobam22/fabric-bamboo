package bamboo.pickaxe.config;

public enum BreakCooldown {
    DEFAULT,
    NEVER,
    ALWAYS;

    public BreakCooldown next() {
        BreakCooldown[] values = BreakCooldown.values();
        int i = (this.ordinal() + 1) % values.length;
        return values[i];
    }
}
