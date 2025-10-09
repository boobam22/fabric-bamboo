# CHANGELOG

## [v1.1.0] - 2025-10-09

- bamboo-inventory v1.1.0
  - add new config `villagerMaxBooks`: when enabled, villager always sell max-level enchant books.
  - add new config `disableTradeLock`: when enabled, allow unlimited trade with villager.
  - support refresh trade at every level.
  - support restock when using item.
  - fix bug when sorting inventory.

- bamboo-pickaxe v1.1.0
  - change config `breakCooldown` from `{DEFAULT, ALWAYS, NEVER}` to boolean.

- bamboo-place v1.1.0
  - rename config `fastUse` to `useCooldown`.
  - rename config `fastUseDistance` to `scaffoldingDistance`.
  - add new config `usePerTick`.

- bamboo-lib v1.1.0
  - improve `ConfigEntry` and support to modify config by command.
  - add new config `commandKeybinds`: dynamic keybind to command.
  - allow `/reload` in integrated server.

- bamboo-start v1.1.0
  - add recipe of elytra-chestplate.
  - fix render error if elytra-chestplate has armor trim.

- bamboo-render v1.0.0
  - support disable render or highlight entity.
  - support disable render particle.

## [v1.0.0] - 2025-09-22

- initial version release
