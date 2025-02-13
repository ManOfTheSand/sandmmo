package com.sandcore.mmo.stats;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AdvancedStatsManager {

    private FileConfiguration config;
    private Map<String, StatFormula> statFormulas = new HashMap<>();

    public AdvancedStatsManager(File dataFolder) {
        File statsFile = new File(dataFolder, "stats.yml");
        if (!statsFile.exists()) {
            // Optionally copy a default file from resources if needed.
        }
        config = YamlConfiguration.loadConfiguration(statsFile);
        loadFormulas();
    }

    private void loadFormulas() {
        if (config.contains("stats")) {
            for (String statName : config.getConfigurationSection("stats").getKeys(false)) {
                double base = config.getDouble("stats." + statName + ".base", 0);
                double growth = config.getDouble("stats." + statName + ".growth", 0);
                String formula = config.getString("stats." + statName + ".formula", "base + level * growth");
                String lore = config.getString("stats." + statName + ".lore", "");
                statFormulas.put(statName, new StatFormula(statName, base, growth, formula, lore));
            }
        }
    }

    // For now, we use a simple linear evaluation.
    public double calculateStat(String statName, int level) {
        if (statFormulas.containsKey(statName)) {
            StatFormula formula = statFormulas.get(statName);
            try {
                javax.script.ScriptEngine engine = new javax.script.ScriptEngineManager().getEngineByName("JavaScript");
                // Provide variables that can be used in the formula
                engine.put("base", formula.getBase());
                engine.put("growth", formula.getGrowth());
                engine.put("level", level);
                // Evaluate the formula expression from the config.
                Object result = engine.eval(formula.getFormula());
                if (result instanceof Number) {
                    return ((Number) result).doubleValue();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public String getStatLore(String statName) {
        if (statFormulas.containsKey(statName)) {
            return statFormulas.get(statName).getLore();
        }
        return "";
    }

    public Map<String, StatFormula> getAllFormulas() {
        return statFormulas;
    }

    public static class StatFormula {
        private final String name;
        private final double base;
        private final double growth;
        private final String formula;
        private final String lore;

        public StatFormula(String name, double base, double growth, String formula, String lore) {
            this.name = name;
            this.base = base;
            this.growth = growth;
            this.formula = formula;
            this.lore = lore;
        }

        public String getName() { return name; }
        public double getBase() { return base; }
        public double getGrowth() { return growth; }
        public String getFormula() { return formula; }
        public String getLore() { return lore; }
    }
} 