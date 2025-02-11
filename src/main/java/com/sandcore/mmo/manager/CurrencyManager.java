package com.sandcore.mmo.manager;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class CurrencyManager {

    private final Logger logger = Logger.getLogger(CurrencyManager.class.getName());
    // Map to store each player's currency balance.
    private final Map<UUID, Integer> playerBalances = new HashMap<>();

    /**
     * Creates a custom currency item based on the type.
     * @param type the type of currency (e.g., "gold", "diamond", etc.)
     * @param amount the amount for the item stack.
     * @return the created ItemStack representing the currency.
     */
    public ItemStack createCurrencyItem(String type, int amount) {
        Material material;
        // Determine the material based on the currency type.
        switch (type.toLowerCase()) {
            case "gold":
                material = Material.GOLD_NUGGET;
                break;
            case "diamond":
                material = Material.DIAMOND;
                break;
            default:
                material = Material.PAPER;
        }
        ItemStack currencyItem = new ItemStack(material, amount);
        ItemMeta meta = currencyItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(type.substring(0, 1).toUpperCase() + type.substring(1) + " Currency");
            currencyItem.setItemMeta(meta);
        }
        logger.info("Created currency item: type=" + type + ", amount=" + amount);
        return currencyItem;
    }

    /**
     * Deposits currency into a player's account.
     * @param player the player to deposit currency to
     * @param amount the amount to deposit
     */
    public void depositCurrency(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        int balance = playerBalances.getOrDefault(uuid, 0);
        balance += amount;
        playerBalances.put(uuid, balance);
        logger.info("Deposited " + amount + " currency to " + player.getName() + ". New balance: " + balance);
    }

    /**
     * Withdraws currency from a player's account.
     * @param player the player to withdraw currency from
     * @param amount the amount to withdraw
     * @return true if the withdrawal was successful; false if insufficient funds.
     */
    public boolean withdrawCurrency(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        int balance = playerBalances.getOrDefault(uuid, 0);
        if (balance < amount) {
            logger.warning("Insufficient funds for " + player.getName() + ". Attempted to withdraw " 
                           + amount + ", current balance: " + balance);
            return false;
        } else {
            balance -= amount;
            playerBalances.put(uuid, balance);
            logger.info("Withdrew " + amount + " currency from " + player.getName() + ". New balance: " + balance);
            return true;
        }
    }
} 