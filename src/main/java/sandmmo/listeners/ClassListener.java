package sandmmo.listeners;

import sandmmo.SandMMO;
import sandmmo.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ClassListener implements Listener {
    private final SandMMO plugin;
    private final PlayerManager playerManager;

    public ClassListener(SandMMO plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Choose Your Class")) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null) return;

            Player player = (Player) event.getWhoClicked();
            String className = plugin.getClassManager()
                    .getClassNameFromItem(event.getCurrentItem());

            if (className != null) {
                playerManager.setPlayerClass(player, className);
                player.sendMessage("§aYou've selected the " + className + " class!");
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerManager.loadPlayerData(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerManager.savePlayerData(event.getPlayer().getUniqueId());
    }
}