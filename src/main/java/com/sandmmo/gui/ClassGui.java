public class ClassGUI {
    private final Inventory inventory;
    private final ClassesConfig config;

    public ClassGUI(ClassesConfig config) {
        this.config = config;
        this.inventory = Bukkit.createInventory(null, 27,
                MiniMessage.miniMessage().deserialize("<gradient:#FFAA00:#FF5500>Class Selection</gradient>"));

        config.getClasses().forEach((id, mmoClass) -> {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();

            meta.displayName(MiniMessage.miniMessage().deserialize(mmoClass.displayName()));
            List<Component> lore = new ArrayList<>();
            mmoClass.description().forEach(line ->
                    lore.add(MiniMessage.miniMessage().deserialize(line)));

            meta.lore(lore);
            item.setItemMeta(meta);
            inventory.addItem(item);
        });
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }
}