public class ClassManager {
    private final Map<UUID, PlayerClassData> playerData = new HashMap<>();
    private final ClassesConfig config;

    public ClassManager(ClassesConfig config) {
        this.config = config;
    }

    public void setPlayerClass(Player player, String classId) {
        MMOClass mmoClass = config.getClasses().get(classId);
        if (mmoClass == null) return;

        PlayerClassData data = new PlayerClassData(
                classId,
                1,
                0,
                calculateMaxExperience(1)
        );
        playerData.put(player.getUniqueId(), data);
        applyClassAttributes(player, mmoClass);
    }

    private void applyClassAttributes(Player player, MMOClass mmoClass) {
        double health = mmoClass.baseHealth() + (mmoClass.healthPerLevel() * getPlayerLevel(player));
        double damage = mmoClass.baseDamage() + (mmoClass.damagePerLevel() * getPlayerLevel(player));

        // Apply using Eco's attribute system
        Eco.get().getAttributeRegistry().getByID("generic.max_health").ifPresent(attr ->
                attr.setValue(player, health));