package com.stickuniversal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.ShulkerBox;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Bat;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Trident;
import org.bukkit.entity.Warden;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class StickmanUniversal extends JavaPlugin implements CommandExecutor, TabCompleter {

    /**
     * Every module the jar knows about. Round 1 adds StickTeams, StickAdminEssentials, StickmanWand and StickCombat here.
     * Until then this list is empty and the jar only loads the framework.
     */
    private static final List<StickModule> REGISTERED = new ArrayList<>();

    static {
        REGISTERED.add(new LoginModule());
        REGISTERED.add(new TeamsModule());
        REGISTERED.add(new AdminEssentialsModule());
        REGISTERED.add(new WandModule());
        REGISTERED.add(new CombatModule());
        REGISTERED.add(new DisguisesModule());
        REGISTERED.add(new CreaturesModule());
        REGISTERED.add(new DungeonsModule());
        REGISTERED.add(new ItemsModule());
    }

    private final Map<String, StickModule> active = new LinkedHashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getConfig().setDefaults(YamlConfiguration.loadConfiguration(new java.io.InputStreamReader(getResource("config.yml"), java.nio.charset.StandardCharsets.UTF_8)));
        getConfig().options().copyDefaults(true);
        saveConfig();
        for (StickModule module : REGISTERED) {
            if (getConfig().getBoolean("modules." + module.name() + ".enabled", false)) {
                try {
                    module.enable(this);
                    active.put(module.name(), module);
                    getLogger().info("Module enabled: " + module.name());
                } catch (RuntimeException ex) {
                    getLogger().severe("Module " + module.name() + " failed to start: " + ex.getMessage());
                }
            }
        }
        getCommand("stickmanuniversal").setExecutor(this);

        // /stickman is shared: "registration" goes to the login module, everything else to the items module.
        getCommand("stickman").setExecutor((sender, cmd, label, args) -> {
            StickModule target = routeStickman(args);
            if (target instanceof CommandExecutor ex) return ex.onCommand(sender, cmd, label, args);
            sender.sendMessage(ChatColor.RED + "That feature is not running. Enable its module in config.yml.");
            return true;
        });
        getCommand("stickman").setTabCompleter((sender, cmd, alias, args) -> {
            StickModule target = routeStickman(args);
            if (target instanceof TabCompleter tc) return tc.onTabComplete(sender, cmd, alias, args);
            return List.of();
        });
        getCommand("stickmanuniversal").setTabCompleter(this);
        getLogger().info("StickmanUniversal started with " + active.size() + " module(s).");
    }

    private StickModule routeStickman(String[] args) {
        boolean registration = args.length > 0 && args[0].equalsIgnoreCase("registration");
        return activeModule(registration ? "login" : "items");
    }

    /** Returns a running module by name, or null if it is off. Lets modules call each other directly. */
    public StickModule activeModule(String name) {
        return active.get(name);
    }

    /** Returns a module's settings: the top-level section named after the module, copied into a fresh config. */
    public FileConfiguration moduleConfig(String name) {
        YamlConfiguration y = new YamlConfiguration();
        ConfigurationSection src = getConfig().getConfigurationSection(name);
        if (src != null) {
            for (String k : src.getKeys(true)) {
                if (!src.isConfigurationSection(k)) y.set(k, src.get(k));
            }
        }
        return y;
    }

    /** Writes a module's settings back into the one config.yml and saves it. */
    public void saveModuleConfig(String name, FileConfiguration cfg) {
        for (String k : cfg.getKeys(true)) {
            if (!cfg.isConfigurationSection(k)) getConfig().set(name + "." + k, cfg.get(k));
        }
        saveConfig();
    }

    @Override
    public void onDisable() {
        for (StickModule module : active.values()) {
            try {
                module.disable();
            } catch (RuntimeException ex) {
                getLogger().warning("Module " + module.name() + " did not shut down cleanly: " + ex.getMessage());
            }
        }
        active.clear();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("stickmanuniversal.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
            return true;
        }
        String sub = args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : "help";
        switch (sub) {
            case "modules" -> {
                sender.sendMessage(ChatColor.GOLD + "StickmanUniversal modules:");
                if (REGISTERED.isEmpty()) {
                    sender.sendMessage(ChatColor.GRAY + " No modules are built into this jar yet.");
                }
                for (StickModule m : REGISTERED) {
                    boolean on = getConfig().getBoolean("modules." + m.name() + ".enabled", false);
                    sender.sendMessage(ChatColor.GRAY + " - " + m.name() + ": "
                            + (on ? ChatColor.GREEN + "on" : ChatColor.RED + "off")
                            + (active.containsKey(m.name()) ? ChatColor.GRAY + " (running)" : ""));
                }
            }
            case "enable", "disable" -> {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /stickmanuniversal " + sub + " <module>");
                    return true;
                }
                String name = args[1].toLowerCase(Locale.ROOT);
                boolean known = REGISTERED.stream().anyMatch(m -> m.name().equals(name));
                if (!known) {
                    sender.sendMessage(ChatColor.RED + "Unknown module: " + name);
                    return true;
                }
                getConfig().set("modules." + name + ".enabled", sub.equals("enable"));
                saveConfig();
                sender.sendMessage(ChatColor.GREEN + "Module " + name + " set to " + sub + ". Restart the server to apply.");
            }
            default -> {
                sender.sendMessage(ChatColor.GOLD + "StickmanUniversal");
                sender.sendMessage(ChatColor.YELLOW + "/stickmanuniversal modules" + ChatColor.GRAY + " - list modules");
                sender.sendMessage(ChatColor.YELLOW + "/stickmanuniversal enable|disable <module>" + ChatColor.GRAY + " - switch a module (restart to apply)");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) return List.of("modules", "enable", "disable", "help");
        if (args.length == 2 && (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("disable"))) {
            return REGISTERED.stream().map(StickModule::name).toList();
        }
        return List.of();
    }

    // ===== StickModule contract =====
    /**
     * One feature inside StickmanUniversal. Each old plugin becomes a module.
     * A module gets its own config file and data folder from the host, and is switched on or off in config.yml.
     */
    public interface StickModule {

        /** Short lowercase name used in config.yml and commands, for example "teams". */
        String name();

        /** Called when the module starts. Register listeners and commands here through the host. */
        void enable(StickmanUniversal host);

        /** Called when the server stops or the module is switched off. */
        void disable();
    }

    // ===== AdminEssentialsModule.java =====
    public static class AdminEssentialsModule implements StickModule, Listener, CommandExecutor, TabCompleter {

        private StickmanUniversal host;
        private FileConfiguration cfg;

        @Override
        public String name() {
            return "adminessentials";
        }

        @Override
        public void enable(StickmanUniversal h) {
            host = h;
            cfg = host.moduleConfig("adminessentials");

            
            loadBans();
            host.getServer().getPluginManager().registerEvents(this, host);
            for (String name : List.of("banitem", "unbanitem", "banlist", "stickadmin", "chest")) {
                host.getCommand(name).setExecutor(this);
                host.getCommand(name).setTabCompleter(this);
            }
            long seconds = Math.max(1, cfg.getLong("scan-seconds", 1));
            Bukkit.getScheduler().runTaskTimer(host, this::scan, 40L, 20L * seconds);
            host.getLogger().info("StickAdminEssentials loaded " + banned.size() + " banned items.");
        }

        @Override
        public void disable() {
        }


        private final Set<Material> banned = EnumSet.noneOf(Material.class);

        

        // ---------- Ban list storage ----------

        private void loadBans() {
            banned.clear();
            for (String s : cfg.getStringList("banned-items")) {
                Material m = Material.matchMaterial(s);
                if (m != null && m.isItem()) banned.add(m);
                else host.getLogger().warning("Unknown item in banned-items: " + s);
            }
        }

        private void saveBans() {
            List<String> names = new ArrayList<>();
            for (Material m : banned) names.add(m.name());
            names.sort(String::compareTo);
            cfg.set("banned-items", names);
            host.saveModuleConfig("adminessentials", cfg);
        }

        private boolean isBanned(ItemStack it) {
            return it != null && !it.getType().isAir() && banned.contains(it.getType());
        }

        private boolean exempt(Player p) {
            return false; // banned items apply to everyone, including operators
        }

        private void notifyBlocked(Player p, ItemStack it) {
            if (cfg.getBoolean("notify-on-block", true)) {
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Banned item: " + ChatColor.RESET + ChatColor.RED + pretty(it.getType()));
            }
        }

        private static String pretty(Material m) {
            StringBuilder sb = new StringBuilder();
            for (String word : m.name().toLowerCase(Locale.ROOT).split("_")) {
                if (sb.length() > 0) sb.append(' ');
                sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
            }
            return sb.toString();
        }

        // ---------- Periodic cleanup ----------

        /** Removes banned items from an array, including inside shulker boxes. Returns how many were removed. */
        private int purge(ItemStack[] items, List<String> found) {
            int changed = 0;
            for (int i = 0; i < items.length; i++) {
                ItemStack it = items[i];
                if (it == null || it.getType().isAir()) continue;
                if (banned.contains(it.getType())) {
                    found.add(pretty(it.getType()));
                    items[i] = null;
                    changed++;
                    continue;
                }
                if (it.getItemMeta() instanceof BlockStateMeta bsm && bsm.getBlockState() instanceof ShulkerBox sb) {
                    ItemStack[] inside = sb.getInventory().getContents();
                    int inner = purge(inside, found);
                    if (inner > 0) {
                        sb.getInventory().setContents(inside);
                        bsm.setBlockState(sb);
                        it.setItemMeta(bsm);
                        changed += inner;
                    }
                }
            }
            return changed;
        }

        private void scan() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                List<String> found = new ArrayList<>();
                PlayerInventory inv = p.getInventory();

                ItemStack[] contents = inv.getContents();
                if (purge(contents, found) > 0) inv.setContents(contents);

                ItemStack[] armor = inv.getArmorContents();
                if (purge(armor, found) > 0) inv.setArmorContents(armor);

                ItemStack[] off = {inv.getItemInOffHand()};
                if (purge(off, found) > 0) inv.setItemInOffHand(off[0]);

                ItemStack[] cursor = {p.getItemOnCursor()};
                if (purge(cursor, found) > 0) p.setItemOnCursor(cursor[0]);

                ItemStack[] ender = p.getEnderChest().getContents();
                if (purge(ender, found) > 0) p.getEnderChest().setContents(ender);

                for (String name : found) {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + name + " is not allowed on this server. It has been deleted.");
                }
            }
            if (cfg.getBoolean("remove-ground-items", true)) {
                for (World w : Bukkit.getWorlds()) {
                    for (Item item : w.getEntitiesByClass(Item.class)) {
                        if (isBanned(item.getItemStack())) item.remove();
                    }
                }
            }
        }

        // ---------- Blocking events ----------

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onItemSpawn(ItemSpawnEvent e) {
            if (isBanned(e.getEntity().getItemStack())) e.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onPlayerPickup(PlayerPickupItemEvent e) {
            if (!isBanned(e.getItem().getItemStack())) return;
            if (exempt(e.getPlayer())) return;
            e.setCancelled(true);
            e.getItem().remove();
            notifyBlocked(e.getPlayer(), e.getItem().getItemStack());
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onMobPickup(EntityPickupItemEvent e) {
            if (isBanned(e.getItem().getItemStack())) {
                e.setCancelled(true);
                e.getItem().remove();
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onDrop(PlayerDropItemEvent e) {
            if (isBanned(e.getItemDrop().getItemStack()) && !exempt(e.getPlayer())) {
                e.setCancelled(true);
                notifyBlocked(e.getPlayer(), e.getItemDrop().getItemStack());
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onClick(InventoryClickEvent e) {
            if (!(e.getWhoClicked() instanceof Player p) || exempt(p)) return;
            if (isBanned(e.getCurrentItem()) || isBanned(e.getCursor())) {
                e.setCancelled(true);
                notifyBlocked(p, isBanned(e.getCurrentItem()) ? e.getCurrentItem() : e.getCursor());
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onDrag(InventoryDragEvent e) {
            if (!(e.getWhoClicked() instanceof Player p) || exempt(p)) return;
            if (isBanned(e.getOldCursor())) {
                e.setCancelled(true);
                notifyBlocked(p, e.getOldCursor());
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onCraft(CraftItemEvent e) {
            if (!(e.getWhoClicked() instanceof Player p) || exempt(p)) return;
            if (e.getRecipe() != null && isBanned(e.getRecipe().getResult())) {
                e.setCancelled(true);
                notifyBlocked(p, e.getRecipe().getResult());
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onInteract(PlayerInteractEvent e) {
            if (exempt(e.getPlayer())) return;
            if (isBanned(e.getItem())) {
                e.setCancelled(true);
                notifyBlocked(e.getPlayer(), e.getItem());
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onPlace(BlockPlaceEvent e) {
            if (isBanned(e.getItemInHand()) && !exempt(e.getPlayer())) {
                e.setCancelled(true);
                notifyBlocked(e.getPlayer(), e.getItemInHand());
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onBucket(PlayerBucketEmptyEvent e) {
            if (isBanned(e.getItemStack()) && !exempt(e.getPlayer())) {
                e.setCancelled(true);
                notifyBlocked(e.getPlayer(), e.getItemStack());
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onConsume(PlayerItemConsumeEvent e) {
            if (isBanned(e.getItem()) && !exempt(e.getPlayer())) {
                e.setCancelled(true);
                notifyBlocked(e.getPlayer(), e.getItem());
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onHopper(InventoryMoveItemEvent e) {
            if (isBanned(e.getItem())) e.setCancelled(true);
        }

        // ---------- Commands ----------

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            switch (cmd.getName().toLowerCase(Locale.ROOT)) {
                case "banitem" -> {
                    if (!admin(sender)) return true;
                    Material m = resolve(sender, args);
                    if (m == null) return true;
                    if (banned.contains(m)) {
                        sender.sendMessage(ChatColor.YELLOW + pretty(m) + " is already banned.");
                        return true;
                    }
                    banned.add(m);
                    saveBans();
                    sender.sendMessage(ChatColor.GREEN + "Banned " + pretty(m) + ". Copies will be removed.");
                }
                case "unbanitem" -> {
                    if (!admin(sender)) return true;
                    Material m = resolve(sender, args);
                    if (m == null) return true;
                    if (!banned.remove(m)) {
                        sender.sendMessage(ChatColor.YELLOW + pretty(m) + " is not banned.");
                        return true;
                    }
                    saveBans();
                    sender.sendMessage(ChatColor.GREEN + "Unbanned " + pretty(m) + ".");
                }
                case "banlist" -> {
                    if (!admin(sender)) return true;
                    if (banned.isEmpty()) {
                        sender.sendMessage(ChatColor.GRAY + "No items are banned.");
                        return true;
                    }
                    sender.sendMessage(ChatColor.GOLD + "Banned items (" + banned.size() + "):");
                    List<String> names = new ArrayList<>();
                    for (Material m : banned) names.add(m.name());
                    names.sort(String::compareTo);
                    sender.sendMessage(ChatColor.GRAY + String.join(", ", names));
                }
                case "stickadmin" -> {
                    if (!admin(sender)) return true;
                    String sub = args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : "help";
                    switch (sub) {
                        case "reload" -> {
                            cfg = host.moduleConfig("adminessentials");
                            loadBans();
                            sender.sendMessage(ChatColor.GREEN + "Reloaded. " + banned.size() + " banned items.");
                        }
                        case "scan" -> {
                            scan();
                            sender.sendMessage(ChatColor.GREEN + "Scan complete.");
                        }
                        default -> {
                            sender.sendMessage(ChatColor.GOLD + "StickAdminEssentials");
                            sender.sendMessage(ChatColor.YELLOW + "/banitem [item|hand]" + ChatColor.GRAY + " - ban an item (default: held item)");
                            sender.sendMessage(ChatColor.YELLOW + "/unbanitem [item|hand]" + ChatColor.GRAY + " - unban an item");
                            sender.sendMessage(ChatColor.YELLOW + "/banlist" + ChatColor.GRAY + " - show banned items");
                            sender.sendMessage(ChatColor.YELLOW + "/stickadmin reload|scan" + ChatColor.GRAY + " - reload config or run an immediate scan");
                        }
                    }
                }
                case "chest" -> {
                    if (!admin(sender)) return true;
                    if (args.length >= 1 && args[0].equalsIgnoreCase("lookup")) {
                        chestLookup(sender, args);
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + "/chest lookup <item>" + ChatColor.GRAY + " - find chests, barrels and shulker boxes holding an item");
                    }
                }
                default -> { }
            }
            return true;
        }

        /** Counts an item in a list of stacks, including inside shulker boxes. */
        private int countItem(ItemStack[] items, Material target) {
            int total = 0;
            for (ItemStack it : items) {
                if (it == null || it.getType().isAir()) continue;
                if (it.getType() == target) total += it.getAmount();
                if (it.getItemMeta() instanceof BlockStateMeta bsm && bsm.getBlockState() instanceof ShulkerBox sb) {
                    total += countItem(sb.getInventory().getContents(), target);
                }
            }
            return total;
        }

        private void chestLookup(CommandSender sender, String[] args) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /chest lookup <item>");
                return;
            }
            Material target = Material.matchMaterial(args[1]);
            if (target == null || !target.isItem()) {
                sender.sendMessage(ChatColor.RED + "Unknown item: " + args[1]);
                return;
            }
            sender.sendMessage(ChatColor.GOLD + "Searching loaded chunks for " + pretty(target) + "...");
            int containers = 0;
            int total = 0;
            for (World w : Bukkit.getWorlds()) {
                for (Chunk ch : w.getLoadedChunks()) {
                    for (BlockState bs : ch.getTileEntities(false)) {
                        if (!(bs instanceof Container box)) continue;
                        int count = countItem(box.getInventory().getContents(), target);
                        if (count <= 0) continue;
                        containers++;
                        total += count;
                        if (containers <= 25) {
                            sender.sendMessage(ChatColor.YELLOW + pretty(bs.getType()) + ChatColor.GRAY + " at "
                                    + ChatColor.WHITE + w.getName() + " " + bs.getX() + ", " + bs.getY() + ", " + bs.getZ()
                                    + ChatColor.GRAY + " (" + count + "x)");
                        }
                    }
                }
            }
            if (containers == 0) {
                sender.sendMessage(ChatColor.GREEN + "No loaded chests, barrels or shulker boxes hold " + pretty(target) + ".");
            } else {
                sender.sendMessage(ChatColor.GOLD + "Found " + total + "x " + pretty(target) + " in " + containers + " container(s)."
                        + (containers > 25 ? " Showing the first 25." : ""));
            }
            sender.sendMessage(ChatColor.GRAY + "Only loaded chunks are searched. Walk near other areas to load them.");
        }

        private boolean admin(CommandSender s) {
            if (!s.hasPermission("stickadmin.admin")) {
                s.sendMessage(ChatColor.RED + "You do not have permission.");
                return false;
            }
            return true;
        }

        private Material resolve(CommandSender sender, String[] args) {
            Material m;
            if (args.length == 0 || args[0].equalsIgnoreCase("hand")) {
                if (!(sender instanceof Player p)) {
                    sender.sendMessage(ChatColor.RED + "Give an item name from the console.");
                    return null;
                }
                m = p.getInventory().getItemInMainHand().getType();
                if (m.isAir()) {
                    sender.sendMessage(ChatColor.RED + "Hold an item or give an item name.");
                    return null;
                }
            } else {
                m = Material.matchMaterial(args[0]);
                if (m == null || !m.isItem()) {
                    sender.sendMessage(ChatColor.RED + "Unknown item: " + args[0]);
                    return null;
                }
            }
            return m;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            if (cmd.getName().equalsIgnoreCase("chest")) {
                if (args.length == 1) return List.of("lookup").stream().filter(s -> s.startsWith(args[0].toLowerCase(Locale.ROOT))).toList();
                if (args.length == 2 && args[0].equalsIgnoreCase("lookup")) {
                    String prefix = args[1].toUpperCase(Locale.ROOT);
                    List<String> items = new ArrayList<>();
                    for (Material m : Material.values()) {
                        if (m.isItem() && m.name().startsWith(prefix) && items.size() < 50) items.add(m.name().toLowerCase(Locale.ROOT));
                    }
                    return items;
                }
                return List.of();
            }
            if (args.length != 1) return List.of();
            String prefix = args[0].toUpperCase(Locale.ROOT);
            List<String> out = new ArrayList<>();
            if (cmd.getName().equalsIgnoreCase("stickadmin")) {
                out.addAll(List.of("reload", "scan"));
                return out.stream().filter(s -> s.startsWith(args[0].toLowerCase(Locale.ROOT))).toList();
            }
            if (cmd.getName().equalsIgnoreCase("unbanitem")) {
                for (Material m : banned) out.add(m.name().toLowerCase(Locale.ROOT));
                out.add("hand");
                return out.stream().filter(s -> s.startsWith(args[0].toLowerCase(Locale.ROOT))).toList();
            }
            if (cmd.getName().equalsIgnoreCase("banitem")) {
                out.add("hand");
                for (Material m : Material.values()) {
                    if (m.isItem() && m.name().startsWith(prefix) && out.size() < 50) out.add(m.name().toLowerCase(Locale.ROOT));
                }
            }
            return out;
        }
    }

    // ===== CombatModule.java =====
    public static class CombatModule implements StickModule, Listener, CommandExecutor, TabCompleter {

        private StickmanUniversal host;
        private FileConfiguration cfg;

        @Override
        public String name() {
            return "combat";
        }

        @Override
        public void enable(StickmanUniversal h) {
            host = h;
            cfg = host.moduleConfig("combat");

            
            reloadAll();
            host.getServer().getPluginManager().registerEvents(this, host);
            host.getCommand("stickcombat").setExecutor(this);
            host.getCommand("stickcombat").setTabCompleter(this);
            long refresh = Math.max(5, cfg.getLong("safezones.refresh-seconds", 30)) * 20L;
            Bukkit.getScheduler().runTaskTimer(host, this::loadSafezones, refresh, refresh);
            Bukkit.getScheduler().runTaskTimer(host, this::removeBannedItems, 20L, 20L);
            host.getLogger().info("StickCombat loaded " + rules.size() + " rules and " + safezones.size() + " safezones.");
        }

        @Override
        public void disable() {
        }


        /** One rule. Matches a material, optionally only items whose name contains a word. */
        record Rule(String key, Material mat, String nameContains, long cooldownMs,
                    boolean cooldownOnlyInCombat, boolean disabledInCombat, boolean banned) {}

        /** A safezone read from StickmanWand's zones file. */
        record SafeZone(String world, int x1, int y1, int z1, int x2, int y2, int z2) {
            boolean contains(Location l) {
                if (l == null || l.getWorld() == null || !l.getWorld().getName().equals(world)) return false;
                int x = l.getBlockX(), y = l.getBlockY(), z = l.getBlockZ();
                return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
            }

            double centerX() { return (x1 + x2 + 1) / 2.0; }

            double centerZ() { return (z1 + z2 + 1) / 2.0; }
        }

        /** Materials whose use is handled by a specific event instead of right-click. */
        static final Set<Material> PROJECTILES = EnumSet.of(Material.ENDER_PEARL, Material.SNOWBALL, Material.EGG,
                Material.EXPERIENCE_BOTTLE, Material.SPLASH_POTION, Material.LINGERING_POTION, Material.TRIDENT,
                Material.FIREWORK_ROCKET);

        private final List<Rule> rules = new ArrayList<>();
        private final List<SafeZone> safezones = new ArrayList<>();
        private final Map<UUID, Long> combatUntil = new HashMap<>();
        private final Map<String, Long> cooldownUntil = new HashMap<>();
        private final Map<UUID, Long> lastNotice = new HashMap<>();
        private File zonesFile;

        

        private void reloadAll() {
            cfg = host.moduleConfig("combat");
            loadRules();
            loadSafezones();
        }

        // ---------- Config ----------

        private void loadRules() {
            rules.clear();
            for (Map<?, ?> raw : cfg.getMapList("rules")) {
                Object k = raw.get("key");
                if (k == null) continue;
                Material mat = Material.matchMaterial(String.valueOf(k));
                if (mat == null) {
                    host.getLogger().warning("Unknown material in rules: " + k);
                    continue;
                }
                Object nameObj = raw.get("name-contains");
                String nameContains = nameObj == null ? null : String.valueOf(nameObj);
                double seconds = number(raw.get("cooldown-seconds"));
                rules.add(new Rule(String.valueOf(k), mat, nameContains, (long) (seconds * 1000),
                        flag(raw, "cooldown-only-in-combat"), flag(raw, "disabled-in-combat"), flag(raw, "banned")));
            }
        }

        private static double number(Object o) {
            if (o == null) return 0;
            try {
                return Double.parseDouble(String.valueOf(o));
            } catch (NumberFormatException ex) {
                return 0;
            }
        }

        private static boolean flag(Map<?, ?> m, String key) {
            Object v = m.get(key);
            return v != null && Boolean.parseBoolean(String.valueOf(v));
        }

        private void loadSafezones() {
            safezones.clear();
            String path = cfg.getString("safezones.zones-file", "zones.yml");
            try {
                zonesFile = new File(host.getDataFolder(), path).getCanonicalFile();
            } catch (IOException ex) {
                host.getLogger().log(Level.WARNING, "Bad safezones.zones-file path", ex);
                return;
            }
            if (!zonesFile.exists()) return;
            YamlConfiguration y = YamlConfiguration.loadConfiguration(zonesFile);
            ConfigurationSection sec = y.getConfigurationSection("zones");
            if (sec == null) return;
            for (String name : sec.getKeys(false)) {
                ConfigurationSection z = sec.getConfigurationSection(name);
                if (z == null || !"safezone".equalsIgnoreCase(z.getString("mode", ""))) continue;
                List<Integer> mn = z.getIntegerList("min");
                List<Integer> mx = z.getIntegerList("max");
                if (mn.size() < 3 || mx.size() < 3) continue;
                safezones.add(new SafeZone(z.getString("world", "world"),
                        mn.get(0), mn.get(1), mn.get(2), mx.get(0), mx.get(1), mx.get(2)));
            }
        }

        private String msg(String key, String... pairs) {
            String text = cfg.getString("messages." + key, key);
            for (int i = 0; i + 1 < pairs.length; i += 2) text = text.replace(pairs[i], pairs[i + 1]);
            return ChatColor.translateAlternateColorCodes('&', text);
        }

        // ---------- Helpers ----------

        private boolean bypass(Player p) {
            return p.hasPermission("stickcombat.bypass");
        }

        private boolean inCombat(UUID id) {
            return combatUntil.getOrDefault(id, 0L) > System.currentTimeMillis();
        }

        private void tag(Player p) {
            boolean wasIn = inCombat(p.getUniqueId());
            combatUntil.put(p.getUniqueId(), System.currentTimeMillis() + cfg.getLong("combat.duration-seconds", 15) * 1000L);
            if (!wasIn && cfg.getBoolean("combat.start-message", true)) {
                p.sendMessage(msg("combat-start", "%seconds%", String.valueOf(cfg.getLong("combat.duration-seconds", 15))));
            }
        }

        private void notice(Player p, String text) {
            long now = System.currentTimeMillis();
            if (now - lastNotice.getOrDefault(p.getUniqueId(), 0L) < 1000L) return;
            lastNotice.put(p.getUniqueId(), now);
            p.sendMessage(text);
        }

        private String plainName(ItemStack it) {
            if (it == null || !it.hasItemMeta() || !it.getItemMeta().hasDisplayName()) return "";
            return ChatColor.stripColor(it.getItemMeta().getDisplayName());
        }

        /** The most specific rule for this item: a name match first, then a plain material rule. */
        private Rule ruleFor(ItemStack it) {
            if (it == null || it.getType().isAir()) return null;
            String name = plainName(it).toLowerCase(Locale.ROOT);
            Rule general = null;
            for (Rule r : rules) {
                if (r.mat() != it.getType()) continue;
                if (r.nameContains() == null) {
                    if (general == null) general = r;
                } else if (name.contains(r.nameContains().toLowerCase(Locale.ROOT))) {
                    return r;
                }
            }
            return general;
        }

        private String label(Rule r) {
            if (r.nameContains() != null) return r.nameContains();
            return r.mat().name().toLowerCase(Locale.ROOT).replace('_', ' ');
        }

        private String cooldownKey(Player p, Rule r) {
            return p.getUniqueId() + "|" + r.key() + "|" + (r.nameContains() == null ? "" : r.nameContains().toLowerCase(Locale.ROOT));
        }

        /**
         * Checks a rule for an item the player is about to use. Returns true when the action must be stopped.
         * A passing check starts the cooldown.
         */
        private boolean gate(Player p, ItemStack it) {
            Rule r = ruleFor(it);
            if (r == null || bypass(p)) return false;
            if (r.banned()) {
                notice(p, msg("banned-item", "%item%", label(r)));
                return true;
            }
            if (r.disabledInCombat() && inCombat(p.getUniqueId())) {
                notice(p, msg("disabled-in-combat", "%item%", label(r)));
                return true;
            }
            if (r.cooldownMs() > 0 && (!r.cooldownOnlyInCombat() || inCombat(p.getUniqueId()))) {
                String key = cooldownKey(p, r);
                long now = System.currentTimeMillis();
                long until = cooldownUntil.getOrDefault(key, 0L);
                if (until > now) {
                    long secs = (until - now + 999) / 1000;
                    notice(p, msg("cooldown", "%item%", label(r), "%seconds%", String.valueOf(secs)));
                    return true;
                }
                cooldownUntil.put(key, now + r.cooldownMs());
            }
            return false;
        }

        /** Removes banned items from inventories every second. */
        private void removeBannedItems() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (bypass(p)) continue;
                PlayerInventory inv = p.getInventory();
                ItemStack[] contents = inv.getContents();
                for (int i = 0; i < contents.length; i++) {
                    Rule r = ruleFor(contents[i]);
                    if (r != null && r.banned()) {
                        p.sendMessage(msg("banned-removed", "%item%", label(r)));
                        contents[i] = null;
                    }
                }
                inv.setContents(contents);
                ItemStack off = inv.getItemInOffHand();
                Rule offRule = ruleFor(off);
                if (offRule != null && offRule.banned()) {
                    p.sendMessage(msg("banned-removed", "%item%", label(offRule)));
                    inv.setItemInOffHand(null);
                }
            }
        }

        private SafeZone zoneAt(Location l) {
            for (SafeZone z : safezones) if (z.contains(l)) return z;
            return null;
        }

        private Player playerSource(org.bukkit.entity.Entity damager) {
            if (damager instanceof Player p) return p;
            if (damager instanceof Projectile pr && pr.getShooter() instanceof Player sp) return sp;
            return null;
        }

        // ---------- Commands ----------

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!sender.hasPermission("stickcombat.admin")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission.");
                return true;
            }
            String sub = args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : "help";
            switch (sub) {
                case "reload" -> {
                    reloadAll();
                    sender.sendMessage(msg("reloaded", "%rules%", String.valueOf(rules.size()), "%zones%", String.valueOf(safezones.size())));
                }
                case "status" -> {
                    sender.sendMessage(ChatColor.GOLD + "StickCombat: " + rules.size() + " rules, " + safezones.size() + " safezones loaded.");
                    sender.sendMessage(ChatColor.GRAY + "Zones file: " + (zonesFile == null ? "not set" : zonesFile.getPath()));
                    int inCombat = 0;
                    for (UUID id : combatUntil.keySet()) if (inCombat(id)) inCombat++;
                    sender.sendMessage(ChatColor.GRAY + "Players in combat now: " + inCombat);
                }
                case "rules" -> {
                    sender.sendMessage(ChatColor.GOLD + "Rules:");
                    for (Rule r : rules) {
                        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + label(r) + ChatColor.GRAY
                                + " (" + r.mat().name() + ")"
                                + (r.banned() ? " BANNED" : "")
                                + (r.cooldownMs() > 0 ? " cooldown " + (r.cooldownMs() / 1000) + "s" : "")
                                + (r.cooldownOnlyInCombat() ? " (combat only)" : "")
                                + (r.disabledInCombat() ? " disabled in combat" : ""));
                    }
                }
                default -> {
                    sender.sendMessage(ChatColor.GOLD + "StickCombat");
                    sender.sendMessage(ChatColor.YELLOW + "/stickcombat reload" + ChatColor.GRAY + " - reload config, rules and safezones");
                    sender.sendMessage(ChatColor.YELLOW + "/stickcombat status" + ChatColor.GRAY + " - loaded rules and zones");
                    sender.sendMessage(ChatColor.YELLOW + "/stickcombat rules" + ChatColor.GRAY + " - list every rule");
                }
            }
            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            if (args.length == 1) return List.of("reload", "status", "rules").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase(Locale.ROOT))).toList();
            return List.of();
        }

        // ---------- Combat tagging and item gates ----------

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onHit(EntityDamageByEntityEvent e) {
            if (e.getDamager() instanceof Player p && gate(p, p.getInventory().getItemInMainHand())) {
                e.setCancelled(true);
                return;
            }
            Player attacker = playerSource(e.getDamager());
            if (e.getEntity() instanceof Player victim) {
                if (attacker != null && !attacker.equals(victim) && cfg.getBoolean("combat.tag-on-player-hit", true)) {
                    tag(victim);
                    tag(attacker);
                } else if (attacker == null && e.getDamager() instanceof LivingEntity
                        && cfg.getBoolean("combat.tag-on-mob-hit", false)) {
                    tag(victim);
                }
            }
        }

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onBowShot(EntityShootBowEvent e) {
            if (e.getEntity() instanceof Player p && gate(p, e.getBow())) e.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onThrow(ProjectileLaunchEvent e) {
            if (!(e.getEntity().getShooter() instanceof Player p)) return;
            ItemStack main = p.getInventory().getItemInMainHand();
            ItemStack off = p.getInventory().getItemInOffHand();
            ItemStack use = ruleFor(main) != null ? main : off;
            if (gate(p, use)) e.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onConsume(PlayerItemConsumeEvent e) {
            if (gate(e.getPlayer(), e.getItem())) e.setCancelled(true);
        }

        /** Right-click use for everything that is not a bow, projectile or food. Those are gated by their own events. */
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onRightClick(PlayerInteractEvent e) {
            if (e.getHand() != EquipmentSlot.HAND) return;
            if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
            ItemStack it = e.getItem();
            if (it == null) return;
            Material m = it.getType();
            if (m == Material.BOW || m == Material.CROSSBOW || m.isEdible() || PROJECTILES.contains(m)) return;
            if (e.getClickedBlock() != null && e.getClickedBlock().getType().isInteractable()) return;
            if (gate(e.getPlayer(), it)) e.setCancelled(true);
        }

        // ---------- Safezone barrier ----------

        private boolean barrierApplies(Player p) {
            if (!cfg.getBoolean("barrier.enabled", true) || bypass(p)) return false;
            return !cfg.getBoolean("barrier.only-in-combat", false) || inCombat(p.getUniqueId());
        }

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onMove(PlayerMoveEvent e) {
            if (e.getFrom().getBlockX() == e.getTo().getBlockX()
                    && e.getFrom().getBlockY() == e.getTo().getBlockY()
                    && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
            Player p = e.getPlayer();
            if (!barrierApplies(p)) return;
            SafeZone into = zoneAt(e.getTo());
            if (into == null || zoneAt(e.getFrom()) != null) return;
            Location stay = e.getFrom().clone();
            stay.setYaw(e.getTo().getYaw());
            stay.setPitch(e.getTo().getPitch());
            e.setTo(stay);
            repel(p, into, e.getFrom());
        }

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onTeleport(PlayerTeleportEvent e) {
            Player p = e.getPlayer();
            if (!barrierApplies(p)) return;
            if (zoneAt(e.getTo()) != null && zoneAt(e.getFrom()) == null) {
                e.setCancelled(true);
                notice(p, msg("barrier-repel"));
            }
        }

        private void repel(Player p, SafeZone zone, Location from) {
            double strength = cfg.getDouble("barrier.repel-strength", 0.9);
            Vector away = new Vector(from.getX() - zone.centerX(), 0, from.getZ() - zone.centerZ());
            if (away.lengthSquared() < 0.0001) away = new Vector(1, 0, 0);
            p.setVelocity(away.normalize().multiply(strength).setY(0.25));
            notice(p, msg("barrier-repel"));
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent e) {
            lastNotice.remove(e.getPlayer().getUniqueId());
        }
    }

    // ===== CreaturesModule.java =====
    public static class CreaturesModule implements StickModule, Listener, CommandExecutor, TabCompleter {

        private StickmanUniversal host;
        private FileConfiguration cfg;

        @Override
        public String name() {
            return "creatures";
        }

        @Override
        public void enable(StickmanUniversal h) {
            host = h;
            cfg = host.moduleConfig("creatures");

            ownerKey = new NamespacedKey(host, "creature-owner");
            host.getServer().getPluginManager().registerEvents(this, host);
            host.getCommand("creature").setExecutor(this);
            host.getCommand("creature").setTabCompleter(this);
            Bukkit.getScheduler().runTaskTimer(host, this::tick, 10L, 10L);
            host.getLogger().info("StickCreatures loaded " + CREATURES.size() + " creatures.");
        }


        static final long HIT_COOLDOWN_MS = 1500L;
        static final long ATTACKER_MEMORY_MS = 10000L;
        static final double MEDIUM_SCALE = 1.4;

        record Creature(String id, String name, EntityType type, String color, String ability, boolean fast) {}

        record Hostile(UUID attacker, long time) {}

        static final class Companion {
            final UUID owner;
            final Creature def;
            final UUID entityId;
            long lastHit;

            Companion(UUID owner, Creature def, UUID entityId) {
                this.owner = owner;
                this.def = def;
                this.entityId = entityId;
            }
        }

        static final List<Creature> CREATURES = List.of(
                new Creature("sparkit", "Sparkit", EntityType.AXOLOTL, "#FFEB3B", "LIGHTNING", true),
                new Creature("cindermutt", "Cindermutt", EntityType.WOLF, "#FF6D00", "FIRE", false),
                new Creature("frostlet", "Frostlet", EntityType.FOX, "#81D4FA", "FREEZE", false),
                new Creature("stingle", "Stingle", EntityType.BEE, "#FDD835", "POISON", false),
                new Creature("thornhop", "Thornhop", EntityType.RABBIT, "#66BB6A", "KNOCK", true),
                new Creature("gusty", "Gusty", EntityType.PARROT, "#4DB6AC", "LEVITATE", false),
                new Creature("boulder", "Boulder", EntityType.PANDA, "#8D6E63", "STUN", false),
                new Creature("shellfin", "Shellfin", EntityType.TURTLE, "#A5D6A7", "SHIELD_OWNER", false),
                new Creature("lunar", "Lunar", EntityType.CAT, "#B39DDB", "BLIND", true),
                new Creature("glimmer", "Glimmer", EntityType.ALLAY, "#80DEEA", "HEAL_OWNER", false),
                new Creature("dune", "Dune", EntityType.CAMEL, "#D7CCC8", "BLIND", false),
                new Creature("cobalt", "Cobalt", EntityType.DOLPHIN, "#42A5F5", "TIDAL", true),
                new Creature("hush", "Hush", EntityType.SNIFFER, "#A1887F", "REVEAL", false),
                new Creature("emberail", "Emberail", EntityType.STRIDER, "#FF3D00", "LAVA", false),
                new Creature("tumbleroll", "Tumbleroll", EntityType.ARMADILLO, "#BCAAA4", "KNOCK", false),
                new Creature("gustwisp", "Gustwisp", EntityType.BREEZE, "#B2EBF2", "WIND", false),
                new Creature("hornback", "Hornback", EntityType.GOAT, "#EEEEEE", "STUN", false),
                new Creature("fleecebolt", "Fleecebolt", EntityType.SHEEP, "#FFF59D", "LIGHTNING", false),
                new Creature("peckbrood", "Peckbrood", EntityType.CHICKEN, "#FFCC80", "WIND", false),
                new Creature("hogrush", "Hogrush", EntityType.PIG, "#F48FB1", "SPEED_OWNER", true),
                new Creature("spitback", "Spitback", EntityType.LLAMA, "#CE93D8", "NAUSEA", false),
                new Creature("hopper", "Hopper", EntityType.FROG, "#9CCC65", "LEVITATE", true),
                new Creature("shadowpaw", "Shadowpaw", EntityType.OCELOT, "#616161", "POISON", false),
                new Creature("mooncow", "Mooncow", EntityType.COW, "#E1F5FE", "HEAL_OWNER", false),
                new Creature("gigglepaw", "Gigglepaw", EntityType.PANDA, "#FFAB91", "NAUSEA", false),
                new Creature("honeyscorch", "Honeyscorch", EntityType.BEE, "#FFB300", "DRAIN", false),
                new Creature("wavebolt", "Wavebolt", EntityType.DOLPHIN, "#26C6DA", "PUSH", true),
                new Creature("ghostpup", "Ghostpup", EntityType.WOLF, "#E0E0E0", "REVEAL", false),
                new Creature("frostpaw", "Frostpaw", EntityType.CAT, "#B3E5FC", "FREEZE", false),
                new Creature("dustroller", "Dustroller", EntityType.CAMEL, "#E6AE6B", "DRAIN", false));

        static final Map<String, String> ABILITY_TEXT = Map.ofEntries(
                Map.entry("LIGHTNING", "Calls a lightning strike onto the target for 6 damage."),
                Map.entry("FIRE", "Sets the target on fire for 5 seconds and deals 4 damage."),
                Map.entry("LAVA", "Sets the target on fire for 8 seconds and deals 4 damage."),
                Map.entry("FREEZE", "Slows the target for 4 seconds and deals 3 damage."),
                Map.entry("POISON", "Poisons the target for 6 seconds and deals 2 damage."),
                Map.entry("KNOCK", "Knocks the target back and deals 3 damage."),
                Map.entry("TIDAL", "Sweeps the target away with a wave and deals 4 damage."),
                Map.entry("PUSH", "Shoves the target hard away and deals 2 damage."),
                Map.entry("LEVITATE", "Lifts the target into the air for 1 second and deals 3 damage."),
                Map.entry("STUN", "Heavily slows the target for 1.5 seconds and deals 5 damage."),
                Map.entry("BLIND", "Blinds the target for 3 seconds and deals 2 damage."),
                Map.entry("NAUSEA", "Makes the target nauseous for 4 seconds and deals 2 damage."),
                Map.entry("REVEAL", "Makes the target glow for 6 seconds and deals 3 damage."),
                Map.entry("WIND", "Blasts everything near the target with a gust for 2 damage."),
                Map.entry("DRAIN", "Deals 4 damage and heals you for 2 hearts."),
                Map.entry("HEAL_OWNER", "Deals 2 damage and heals you for 2 hearts."),
                Map.entry("SHIELD_OWNER", "Deals 2 damage and gives you 2 hearts of absorption for 10 seconds."),
                Map.entry("SPEED_OWNER", "Deals 2 damage and gives you Speed II for 3 seconds."));

        private final Map<UUID, Companion> companions = new HashMap<>();
        private final Map<UUID, Hostile> lastAttackers = new HashMap<>();
        private NamespacedKey ownerKey;

        

        @Override
        public void disable() {
            for (UUID owner : new ArrayList<>(companions.keySet())) dismiss(owner);
        }

        // ---------- Helpers ----------

        private Creature find(String id) {
            for (Creature c : CREATURES) if (c.id().equalsIgnoreCase(id)) return c;
            return null;
        }

        private String colored(Creature c) {
            return net.md_5.bungee.api.ChatColor.of(c.color()) + "" + ChatColor.BOLD + c.name();
        }

        private void dismiss(UUID owner) {
            Companion c = companions.remove(owner);
            if (c == null) return;
            Entity e = Bukkit.getEntity(c.entityId);
            if (e != null && e.isValid()) e.remove();
        }

        private void summon(Player owner, Creature def) {
            dismiss(owner.getUniqueId());
            Location spawn = owner.getLocation().clone().add(2, 0, 2);
            Entity e = spawn.getWorld().spawnEntity(spawn, def.type());
            if (!(e instanceof LivingEntity le)) {
                e.remove();
                owner.sendMessage(ChatColor.RED + "That creature could not be spawned.");
                return;
            }
            le.setCustomName(colored(def));
            le.setCustomNameVisible(true);
            le.setPersistent(true);
            if (le instanceof Mob m) m.setRemoveWhenFarAway(false);

            AttributeInstance scale = le.getAttribute(Attribute.GENERIC_SCALE);
            if (scale != null) scale.setBaseValue(MEDIUM_SCALE);

            AttributeInstance maxHp = le.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHp != null) maxHp.setBaseValue(30);
            le.setHealth(30);

            if (def.fast()) {
                AttributeInstance speed = le.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
                if (speed != null) speed.setBaseValue(speed.getBaseValue() * 1.35);
            }

            le.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING, owner.getUniqueId().toString());
            companions.put(owner.getUniqueId(), new Companion(owner.getUniqueId(), def, le.getUniqueId()));
            owner.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Summoned " + ChatColor.RESET + colored(def)
                    + ChatColor.GREEN + ". " + ChatColor.GRAY + ABILITY_TEXT.get(def.ability()));
        }

        private void damage(LivingEntity target, double amount, Player owner) {
            target.damage(amount, owner);
        }

        private void knock(LivingEntity target, Location from, double horizontal, double up) {
            Vector out = target.getLocation().toVector().subtract(from.toVector()).setY(0);
            if (out.lengthSquared() < 0.01) out = new Vector(0.01, 0, 0);
            target.setVelocity(out.normalize().multiply(horizontal).setY(up));
        }

        private void healOwner(Player owner, double hearts) {
            owner.setHealth(Math.min(owner.getMaxHealth(), owner.getHealth() + hearts));
        }

        /** Each creature's single ability, fired when it lands a hit. */
        private void useAbility(String ability, LivingEntity target, Player owner) {
            Location at = target.getLocation();
            switch (ability) {
                case "LIGHTNING" -> {
                    at.getWorld().strikeLightningEffect(at);
                    damage(target, 6, owner);
                }
                case "FIRE" -> {
                    target.setFireTicks(100);
                    damage(target, 4, owner);
                }
                case "LAVA" -> {
                    target.setFireTicks(160);
                    damage(target, 4, owner);
                }
                case "FREEZE" -> {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 80, 2));
                    damage(target, 3, owner);
                }
                case "POISON" -> {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 120, 0));
                    damage(target, 2, owner);
                }
                case "KNOCK" -> {
                    damage(target, 3, owner);
                    knock(target, owner.getLocation(), 0.9, 0.5);
                }
                case "TIDAL" -> {
                    damage(target, 4, owner);
                    knock(target, owner.getLocation(), 1.4, 0.2);
                }
                case "PUSH" -> {
                    damage(target, 2, owner);
                    knock(target, owner.getLocation(), 1.8, 0.4);
                }
                case "LEVITATE" -> {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 0));
                    damage(target, 3, owner);
                }
                case "STUN" -> {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 30, 4));
                    damage(target, 5, owner);
                }
                case "BLIND" -> {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                    damage(target, 2, owner);
                }
                case "NAUSEA" -> {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 80, 0));
                    damage(target, 2, owner);
                }
                case "REVEAL" -> {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 120, 0));
                    damage(target, 3, owner);
                }
                case "WIND" -> {
                    for (Entity en : at.getWorld().getNearbyEntities(at, 3, 2, 3)) {
                        if (!(en instanceof LivingEntity le) || en.equals(owner)) continue;
                        damage(le, 2, owner);
                        knock(le, at, 1.0, 0.3);
                    }
                }
                case "DRAIN" -> {
                    damage(target, 4, owner);
                    healOwner(owner, 2);
                }
                case "HEAL_OWNER" -> {
                    damage(target, 2, owner);
                    healOwner(owner, 2);
                }
                case "SHIELD_OWNER" -> {
                    damage(target, 2, owner);
                    owner.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 1));
                }
                case "SPEED_OWNER" -> {
                    damage(target, 2, owner);
                    owner.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
                }
                default -> damage(target, 3, owner);
            }
            target.getWorld().spawnParticle(Particle.CRIT, target.getLocation().add(0, 1, 0), 15, 0.4, 0.5, 0.4, 0.1);
        }

        private LivingEntity findTarget(Player owner) {
            Hostile h = lastAttackers.get(owner.getUniqueId());
            if (h != null && System.currentTimeMillis() - h.time() < ATTACKER_MEMORY_MS) {
                Player attacker = Bukkit.getPlayer(h.attacker());
                if (attacker != null && attacker.isOnline() && attacker.getWorld() == owner.getWorld()
                        && attacker.getLocation().distance(owner.getLocation()) <= 16) {
                    return attacker;
                }
            }
            LivingEntity best = null;
            double bestDist = 10;
            for (Entity en : owner.getNearbyEntities(10, 4, 10)) {
                if (!(en instanceof Monster m)) continue;
                double d = m.getLocation().distance(owner.getLocation());
                if (d < bestDist) {
                    bestDist = d;
                    best = m;
                }
            }
            return best;
        }

        // ---------- Companion loop ----------

        private void tick() {
            for (Companion c : new ArrayList<>(companions.values())) {
                Player owner = Bukkit.getPlayer(c.owner);
                if (owner == null || !owner.isOnline()) {
                    dismiss(c.owner);
                    continue;
                }
                Entity ent = Bukkit.getEntity(c.entityId);
                if (!(ent instanceof LivingEntity le) || !le.isValid() || le.isDead()) {
                    companions.remove(c.owner);
                    continue;
                }
                if (le.getWorld() != owner.getWorld() || le.getLocation().distance(owner.getLocation()) > 20) {
                    le.teleport(owner.getLocation().clone().add(2, 0, 2));
                    continue;
                }
                LivingEntity target = findTarget(owner);
                if (target == null) {
                    if (le instanceof Mob m && le.getLocation().distance(owner.getLocation()) > 4) {
                        m.getPathfinder().moveTo(owner.getLocation());
                    }
                    continue;
                }
                if (le instanceof Mob m) m.getPathfinder().moveTo(target.getLocation());
                if (le.getLocation().distance(target.getLocation()) <= 2.5
                        && System.currentTimeMillis() - c.lastHit >= HIT_COOLDOWN_MS) {
                    c.lastHit = System.currentTimeMillis();
                    useAbility(c.def.ability(), target, owner);
                }
            }
        }

        // ---------- Events ----------

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onDamage(EntityDamageByEntityEvent e) {
            Player attacker = null;
            if (e.getDamager() instanceof Player dp) attacker = dp;
            else if (e.getDamager() instanceof Projectile pr && pr.getShooter() instanceof Player sp) attacker = sp;

            if (e.getEntity() instanceof Player victim && attacker != null && !attacker.equals(victim)) {
                lastAttackers.put(victim.getUniqueId(), new Hostile(attacker.getUniqueId(), System.currentTimeMillis()));
            }
            // Owners cannot hurt their own creature.
            String victimOwner = e.getEntity().getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
            if (victimOwner != null && attacker != null && victimOwner.equals(attacker.getUniqueId().toString())) {
                e.setCancelled(true);
                return;
            }
            // A creature cannot hurt its own owner.
            String attackerOwner = e.getDamager().getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
            if (attackerOwner != null && e.getEntity() instanceof Player v && attackerOwner.equals(v.getUniqueId().toString())) {
                e.setCancelled(true);
            }
        }

        @EventHandler
        public void onCreatureDeath(EntityDeathEvent e) {
            String ownerTag = e.getEntity().getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
            if (ownerTag == null) return;
            e.getDrops().clear();
            e.setDroppedExp(0);
            UUID ownerId = UUID.fromString(ownerTag);
            companions.remove(ownerId);
            Player owner = Bukkit.getPlayer(ownerId);
            if (owner != null) owner.sendMessage(ChatColor.RED + "Your creature fainted. Summon it again with /creature summon.");
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent e) {
            dismiss(e.getPlayer().getUniqueId());
        }

        // ---------- Commands ----------

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (args.length == 0) {
                help(sender);
                return true;
            }
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "help" -> help(sender);
                case "list" -> {
                    sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "StickCreatures (" + CREATURES.size() + ")");
                    for (Creature c : CREATURES) {
                        sender.sendMessage(ChatColor.GRAY + " - " + colored(c) + ChatColor.GRAY + " (" + c.id() + ") "
                                + (c.fast() ? "fast" : ""));
                    }
                }
                case "info" -> {
                    if (args.length < 2) { sender.sendMessage(ChatColor.RED + "Usage: /creature info <id>"); return true; }
                    Creature c = find(args[1]);
                    if (c == null) { sender.sendMessage(ChatColor.RED + "Unknown creature."); return true; }
                    sender.sendMessage(colored(c) + ChatColor.GRAY + " is a medium " + c.type().name().toLowerCase(Locale.ROOT)
                            + (c.fast() ? ", fast," : ",") + " with one ability:");
                    sender.sendMessage(ChatColor.YELLOW + ABILITY_TEXT.get(c.ability()));
                }
                case "summon" -> {
                    if (args.length < 2) { sender.sendMessage(ChatColor.RED + "Usage: /creature summon <id> [player]"); return true; }
                    Creature c = find(args[1]);
                    if (c == null) { sender.sendMessage(ChatColor.RED + "Unknown creature."); return true; }
                    Player target;
                    if (args.length >= 3) {
                        if (!sender.hasPermission("stickcreatures.admin")) { sender.sendMessage(ChatColor.RED + "You cannot summon for other players."); return true; }
                        target = Bukkit.getPlayerExact(args[2]);
                        if (target == null) { sender.sendMessage(ChatColor.RED + "That player is not online."); return true; }
                    } else if (sender instanceof Player p) {
                        if (!p.hasPermission("stickcreatures.use")) { p.sendMessage(ChatColor.RED + "You cannot summon creatures."); return true; }
                        target = p;
                    } else {
                        sender.sendMessage(ChatColor.RED + "Give a player name from the console.");
                        return true;
                    }
                    summon(target, c);
                }
                case "dismiss" -> {
                    if (!(sender instanceof Player p)) { sender.sendMessage(ChatColor.RED + "Players only."); return true; }
                    dismiss(p.getUniqueId());
                    p.sendMessage(ChatColor.GRAY + "Your creature was returned.");
                }
                default -> sender.sendMessage(ChatColor.RED + "Unknown subcommand. Try /creature help");
            }
            return true;
        }

        private void help(CommandSender s) {
            s.sendMessage(ChatColor.GOLD + "StickCreatures");
            s.sendMessage(ChatColor.YELLOW + "/creature list" + ChatColor.GRAY + " - all 30 creatures");
            s.sendMessage(ChatColor.YELLOW + "/creature info <id>" + ChatColor.GRAY + " - ability and details");
            s.sendMessage(ChatColor.YELLOW + "/creature summon <id> [player]" + ChatColor.GRAY + " - bring a creature out");
            s.sendMessage(ChatColor.YELLOW + "/creature dismiss" + ChatColor.GRAY + " - send your creature away");
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            if (args.length == 1) {
                return List.of("list", "info", "summon", "dismiss", "help").stream()
                        .filter(s -> s.startsWith(args[0].toLowerCase(Locale.ROOT))).toList();
            }
            if (args.length == 2 && (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("summon"))) {
                return CREATURES.stream().map(Creature::id)
                        .filter(s -> s.startsWith(args[1].toLowerCase(Locale.ROOT))).toList();
            }
            return List.of();
        }
    }

    // ===== DisguisesModule.java =====
    public static class DisguisesModule implements StickModule, Listener, CommandExecutor, TabCompleter {

        private StickmanUniversal host;
        private FileConfiguration cfg;

        @Override
        public String name() {
            return "disguises";
        }

        @Override
        public void enable(StickmanUniversal h) {
            host = h;
            cfg = host.moduleConfig("disguises");

            host.getServer().getPluginManager().registerEvents(this, host);
            disguiseKey = new NamespacedKey(host, "disguise-owner");
            Bukkit.getScheduler().runTaskTimer(host, this::syncMobs, 1L, 1L);
            host.getCommand("disguise").setExecutor(this);
            host.getCommand("disguise").setTabCompleter(this);
            host.getLogger().info("StickDisguises loaded " + MOBS.size() + " mobs.");
        }

        @Override
        public void disable() {
        }


        static final int PAGE_SIZE = 45;
        static final long BOOM_COOLDOWN_MS = 3000L;
        static final double BOOM_RANGE = 15.0;
        static final double BOOM_DAMAGE = 10.0;

        /** Stats copied from the vanilla mob. fist = bare-hand melee damage. ability is "NONE" when not implemented yet. */
        static final class MobStats {
            final EntityType type;
            final double health;
            final double fist;
            final double scale;
            final String ability;

            MobStats(EntityType type, double health, double fist, double scale, String ability) {
                this.type = type;
                this.health = health;
                this.fist = fist;
                this.scale = scale;
                this.ability = ability;
            }
        }

        static final List<MobStats> MOBS = List.of(
                new MobStats(EntityType.ZOMBIE, 20, 3, 1.0, "NONE"),
                new MobStats(EntityType.SKELETON, 20, 2, 1.0, "NONE"),
                new MobStats(EntityType.CREEPER, 20, 1, 1.0, "NONE"),
                new MobStats(EntityType.SPIDER, 16, 2, 1.0, "NONE"),
                new MobStats(EntityType.ENDERMAN, 40, 7, 1.3, "NONE"),
                new MobStats(EntityType.WARDEN, 500, 30, 1.6, "SONIC_BOOM"),
                new MobStats(EntityType.BLAZE, 20, 6, 1.1, "NONE"),
                new MobStats(EntityType.GHAST, 10, 0, 2.0, "NONE"),
                new MobStats(EntityType.WITHER_SKELETON, 20, 6, 1.1, "NONE"),
                new MobStats(EntityType.PIGLIN, 16, 7, 1.0, "NONE"),
                new MobStats(EntityType.ZOMBIFIED_PIGLIN, 20, 5, 1.0, "NONE"),
                new MobStats(EntityType.DROWNED, 20, 3, 1.0, "NONE"),
                new MobStats(EntityType.HUSK, 20, 4, 1.0, "NONE"),
                new MobStats(EntityType.STRAY, 20, 4, 1.0, "NONE"),
                new MobStats(EntityType.VINDICATOR, 24, 13, 1.0, "NONE"),
                new MobStats(EntityType.EVOKER, 24, 6, 1.0, "NONE"),
                new MobStats(EntityType.WITCH, 26, 6, 1.0, "NONE"),
                new MobStats(EntityType.PHANTOM, 20, 6, 1.0, "NONE"),
                new MobStats(EntityType.VEX, 14, 9, 0.8, "NONE"),
                new MobStats(EntityType.RAVAGER, 100, 12, 1.5, "NONE"),
                new MobStats(EntityType.IRON_GOLEM, 100, 15, 1.4, "NONE"),
                new MobStats(EntityType.ELDER_GUARDIAN, 80, 6, 1.6, "NONE"),
                new MobStats(EntityType.GUARDIAN, 30, 6, 1.0, "NONE"),
                new MobStats(EntityType.SHULKER, 30, 4, 1.0, "NONE"),
                new MobStats(EntityType.MAGMA_CUBE, 16, 6, 1.0, "NONE"),
                new MobStats(EntityType.SLIME, 16, 4, 1.0, "NONE"),
                new MobStats(EntityType.BREEZE, 30, 5, 1.0, "NONE"),
                new MobStats(EntityType.PILLAGER, 24, 8, 1.0, "NONE"),
                new MobStats(EntityType.SNIFFER, 14, 3, 1.2, "NONE"),
                new MobStats(EntityType.ARMADILLO, 12, 2, 0.8, "NONE"));

        /** Menu holder so we can recognise our own inventories. */
        static final class MenuHolder implements InventoryHolder {
            final int page;
            Inventory inv;

            MenuHolder(int page) {
                this.page = page;
            }

            @Override
            public Inventory getInventory() {
                return inv;
            }
        }

        private final Map<UUID, MobStats> disguises = new HashMap<>();
        private final Map<UUID, Double> originalHealth = new HashMap<>();
        private final Map<UUID, Double> originalScale = new HashMap<>();
        private final Map<UUID, Long> boomReady = new HashMap<>();
        /** The visible mob that other players see in place of each disguised player. */
        private final Map<UUID, LivingEntity> mobs = new HashMap<>();
        private NamespacedKey disguiseKey;

        

        // ---------- Helpers ----------

        private MobStats findMob(String name) {
            for (MobStats m : MOBS) if (m.type.name().equalsIgnoreCase(name)) return m;
            return null;
        }

        private String prettyName(EntityType t) {
            String[] words = t.name().toLowerCase(Locale.ROOT).split("_");
            StringBuilder sb = new StringBuilder();
            for (String w : words) {
                if (sb.length() > 0) sb.append(' ');
                sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1));
            }
            return sb.toString();
        }

        /** Heads for mobs that have one, otherwise the spawn egg. */
        private ItemStack iconFor(MobStats m) {
            Material mat = Material.matchMaterial(m.type.name() + "_HEAD");
            if (mat == null) mat = Material.matchMaterial(m.type.name() + "_SKULL");
            if (mat == null) mat = Material.matchMaterial(m.type.name() + "_SPAWN_EGG");
            if (mat == null) mat = Material.BARRIER;
            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + prettyName(m.type));
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Health: " + ChatColor.WHITE + (int) m.health);
            lore.add(ChatColor.GRAY + "Fist damage: " + ChatColor.WHITE + (int) m.fist);
            lore.add(ChatColor.GRAY + "Size: " + ChatColor.WHITE + m.scale + "x");
            lore.add(ChatColor.GRAY + "Ability: " + ChatColor.WHITE
                    + (m.ability.equals("SONIC_BOOM") ? "Sonic boom (right-click with empty hand)" : "Stats only for now"));
            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        }

        private void openMenu(Player p, int page) {
            int pages = (MOBS.size() + PAGE_SIZE - 1) / PAGE_SIZE;
            page = Math.max(0, Math.min(page, pages - 1));
            MenuHolder holder = new MenuHolder(page);
            Inventory inv = Bukkit.createInventory(holder, 54, ChatColor.DARK_PURPLE + "Disguise: page " + (page + 1) + "/" + pages);
            holder.inv = inv;
            int start = page * PAGE_SIZE;
            for (int i = 0; i < PAGE_SIZE && start + i < MOBS.size(); i++) {
                inv.setItem(i, iconFor(MOBS.get(start + i)));
            }
            if (page > 0) inv.setItem(45, named(Material.ARROW, ChatColor.YELLOW + "Previous page"));
            inv.setItem(49, named(Material.BARRIER, ChatColor.RED + "Stop disguising"));
            if (page < pages - 1) inv.setItem(53, named(Material.ARROW, ChatColor.YELLOW + "Next page"));
            p.openInventory(inv);
        }

        private ItemStack named(Material mat, String name) {
            ItemStack it = new ItemStack(mat);
            ItemMeta meta = it.getItemMeta();
            meta.setDisplayName(name);
            it.setItemMeta(meta);
            return it;
        }

        private void applyDisguise(Player p, MobStats m) {
            UUID id = p.getUniqueId();
            clearMob(id);
            AttributeInstance hp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            AttributeInstance sc = p.getAttribute(Attribute.GENERIC_SCALE);
            if (!disguises.containsKey(id)) {
                originalHealth.put(id, hp == null ? 20.0 : hp.getBaseValue());
                originalScale.put(id, sc == null ? 1.0 : sc.getBaseValue());
            }
            if (hp != null) hp.setBaseValue(m.health);
            if (sc != null) sc.setBaseValue(m.scale);
            p.setHealth(m.health);
            disguises.put(id, m);
            spawnMob(p, m);
            hideFromOthers(p);
            p.sendMessage(ChatColor.GREEN + "You are now disguised as a " + prettyName(m.type) + ".");
        }

        private void removeDisguise(Player p) {
            UUID id = p.getUniqueId();
            if (!disguises.containsKey(id)) {
                p.sendMessage(ChatColor.YELLOW + "You are not disguised.");
                return;
            }
            clearMob(id);
            showToAll(p);
            AttributeInstance hp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            AttributeInstance sc = p.getAttribute(Attribute.GENERIC_SCALE);
            if (hp != null) hp.setBaseValue(originalHealth.getOrDefault(id, 20.0));
            if (sc != null) sc.setBaseValue(originalScale.getOrDefault(id, 1.0));
            if (p.getHealth() > p.getMaxHealth()) p.setHealth(p.getMaxHealth());
            disguises.remove(id);
            originalHealth.remove(id);
            originalScale.remove(id);
            p.sendMessage(ChatColor.GREEN + "Disguise removed.");
        }

        // ---------- Visual disguise (Bukkit only, no ProtocolLib) ----------

        /** Spawns the mob that other players see. It stands still, cannot be killed, and passes its damage to the player. */
        private void spawnMob(Player p, MobStats m) {
            Entity e = p.getWorld().spawnEntity(p.getLocation(), m.type);
            if (!(e instanceof LivingEntity le)) {
                e.remove();
                return;
            }
            le.setPersistent(true);
            if (le instanceof Mob mob) mob.setRemoveWhenFarAway(false);
            le.setAI(false);
            le.setInvulnerable(true);
            le.setCollidable(false);
            le.setSilent(true);
            le.setCustomName(ChatColor.WHITE + p.getName());
            le.setCustomNameVisible(true);
            AttributeInstance sc = le.getAttribute(Attribute.GENERIC_SCALE);
            if (sc != null) sc.setBaseValue(m.scale);
            le.getPersistentDataContainer().set(disguiseKey, PersistentDataType.STRING, p.getUniqueId().toString());
            mobs.put(p.getUniqueId(), le);
        }

        private void clearMob(UUID id) {
            LivingEntity le = mobs.remove(id);
            if (le != null && le.isValid()) le.remove();
        }

        private void hideFromOthers(Player p) {
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (!other.equals(p)) other.hidePlayer(host, p);
            }
        }

        private void showToAll(Player p) {
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (!other.equals(p)) other.showPlayer(host, p);
            }
        }

        /** Keeps each mob exactly where its player is, every tick. */
        private void syncMobs() {
            for (Map.Entry<UUID, LivingEntity> en : new ArrayList<>(mobs.entrySet())) {
                Player p = Bukkit.getPlayer(en.getKey());
                LivingEntity le = en.getValue();
                if (p == null || !p.isOnline() || !le.isValid()) {
                    if (le.isValid()) le.remove();
                    mobs.remove(en.getKey());
                    continue;
                }
                Location target = p.getLocation();
                Location here = le.getLocation();
                if (!here.getWorld().equals(target.getWorld()) || here.distanceSquared(target) > 0.0001
                        || Math.abs(here.getYaw() - target.getYaw()) > 2f) {
                    le.teleport(target);
                }
            }
        }

        /** Warden sonic boom: a sonic wave up to 15 blocks that hits everything in its path for 10 damage and knocks it back. */
        private void sonicBoom(Player p) {
            UUID id = p.getUniqueId();
            long now = System.currentTimeMillis();
            if (boomReady.getOrDefault(id, 0L) > now) {
                long secs = (boomReady.get(id) - now + 999) / 1000;
                p.sendMessage(ChatColor.RED + "Sonic boom recharging: " + secs + "s");
                return;
            }
            boomReady.put(id, now + BOOM_COOLDOWN_MS);

            Location eye = p.getEyeLocation();
            Vector dir = eye.getDirection().normalize();
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 3f, 1f);

            Set<LivingEntity> hit = new LinkedHashSet<>();
            for (double d = 1; d <= BOOM_RANGE; d += 0.5) {
                Location point = eye.clone().add(dir.clone().multiply(d));
                p.getWorld().spawnParticle(Particle.SONIC_BOOM, point, 1, 0, 0, 0, 0);
                for (Entity en : p.getWorld().getNearbyEntities(point, 0.9, 0.9, 0.9)) {
                    if (en instanceof LivingEntity le && !en.equals(p)) hit.add(le);
                }
            }
            // Damage goes through Bukkit's damage events, so team and trust protections still apply.
            DamageSource source = DamageSource.builder(DamageType.SONIC_BOOM)
                    .withCausingEntity(p)
                    .withDirectEntity(p)
                    .build();
            for (LivingEntity le : hit) {
                le.damage(BOOM_DAMAGE, source);
                le.setVelocity(dir.clone().multiply(1.2).setY(0.3));
            }
        }

        // ---------- Commands ----------

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage(ChatColor.RED + "Players only.");
                return true;
            }
            if (args.length == 0) {
                openMenu(p, 0);
                return true;
            }
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "off" -> removeDisguise(p);
                case "info" -> {
                    MobStats m = disguises.get(p.getUniqueId());
                    p.sendMessage(m == null ? ChatColor.YELLOW + "You are not disguised." : ChatColor.GOLD + "Disguised as " + prettyName(m.type) + ".");
                }
                default -> {
                    MobStats m = findMob(args[0]);
                    if (m == null) {
                        p.sendMessage(ChatColor.RED + "Unknown mob. Use /disguise to open the menu.");
                        return true;
                    }
                    applyDisguise(p, m);
                }
            }
            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            if (args.length == 1) {
                List<String> names = new ArrayList<>(List.of("off", "info"));
                for (MobStats m : MOBS) names.add(m.type.name().toLowerCase(Locale.ROOT));
                return names.stream().filter(s -> s.startsWith(args[0].toLowerCase(Locale.ROOT))).toList();
            }
            return List.of();
        }

        // ---------- Events ----------

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onMenuClick(InventoryClickEvent e) {
            if (!(e.getView().getTopInventory().getHolder() instanceof MenuHolder menu)) return;
            e.setCancelled(true);
            if (!(e.getWhoClicked() instanceof Player p)) return;
            int slot = e.getRawSlot();
            if (slot < 0 || slot >= 54) return;
            if (slot == 45 && menu.page > 0) { openMenu(p, menu.page - 1); return; }
            if (slot == 53) { openMenu(p, menu.page + 1); return; }
            if (slot == 49) { removeDisguise(p); p.closeInventory(); return; }
            if (slot >= PAGE_SIZE) return;
            int index = menu.page * PAGE_SIZE + slot;
            if (index >= MOBS.size()) return;
            applyDisguise(p, MOBS.get(index));
            p.closeInventory();
        }

        /** Warden fist damage: bare hands deal exactly the Warden's melee damage while disguised. */
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onFist(EntityDamageByEntityEvent e) {
            if (!(e.getDamager() instanceof Player p)) return;
            MobStats m = disguises.get(p.getUniqueId());
            if (m == null) return;
            ItemStack hand = p.getInventory().getItemInMainHand();
            if (hand != null && !hand.getType().isAir()) return;
            e.setDamage(m.fist);
        }

        /** Right-click with empty hand fires the Warden sonic boom. */
        @EventHandler
        public void onRightClick(PlayerInteractEvent e) {
            if (e.getHand() == null || e.getHand() != org.bukkit.inventory.EquipmentSlot.HAND) return;
            if (!(e.getAction().isRightClick())) return;
            Player p = e.getPlayer();
            MobStats m = disguises.get(p.getUniqueId());
            if (m == null || !m.ability.equals("SONIC_BOOM")) return;
            ItemStack hand = p.getInventory().getItemInMainHand();
            if (hand != null && !hand.getType().isAir()) return;
            if (e.getClickedBlock() != null && e.getClickedBlock().getType().isInteractable()) return;
            e.setCancelled(true);
            sonicBoom(p);
        }

        /** Passes damage dealt to a disguise mob on to the disguised player. */
        @EventHandler(priority = EventPriority.HIGH)
        public void onMobHit(EntityDamageEvent e) {
            String owner = e.getEntity().getPersistentDataContainer().get(disguiseKey, PersistentDataType.STRING);
            if (owner == null) return;
            e.setCancelled(true);
            Player p = Bukkit.getPlayer(UUID.fromString(owner));
            if (p == null || !p.isOnline()) return;
            double dmg = e.getDamage();
            if (e instanceof EntityDamageByEntityEvent de) {
                if (!de.getDamager().equals(p)) p.damage(dmg, de.getDamager());
            } else {
                p.damage(dmg);
            }
        }

        /** Anyone joining later must not see disguised players. */
        @EventHandler
        public void onJoin(PlayerJoinEvent e) {
            for (UUID id : disguises.keySet()) {
                Player disguised = Bukkit.getPlayer(id);
                if (disguised != null && !disguised.equals(e.getPlayer())) e.getPlayer().hidePlayer(host, disguised);
            }
        }

        @EventHandler
        public void onDeath(PlayerDeathEvent e) {
            if (disguises.containsKey(e.getEntity().getUniqueId())) removeDisguise(e.getEntity());
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent e) {
            UUID id = e.getPlayer().getUniqueId();
            clearMob(id);
            disguises.remove(id);
            originalHealth.remove(id);
            originalScale.remove(id);
            boomReady.remove(id);
        }
    }

    // ===== DungeonsModule.java =====
    public static class DungeonsModule implements StickModule, Listener, CommandExecutor, TabCompleter {

        private StickmanUniversal host;
        private FileConfiguration cfg;

        @Override
        public String name() {
            return "dungeons";
        }

        @Override
        public void enable(StickmanUniversal h) {
            host = h;
            cfg = host.moduleConfig("dungeons");

            dungeonWorld = ensureWorld();
            if (dungeonWorld == null) {
                host.getLogger().severe("Could not create the dungeon world. Dungeons are disabled.");
            }
            loadDungeons();
            host.getServer().getPluginManager().registerEvents(this, host);
            host.getCommand("dungeon").setExecutor(this);
            host.getCommand("dungeon").setTabCompleter(this);
            Bukkit.getScheduler().runTaskTimer(host, this::pumpBuildQueue, 1L, 1L);
            host.getLogger().info("StickManDungeonsBeta loaded " + dungeons.size() + " dungeons.");
        }


        static final String WORLD_NAME = "stickman_dungeons";
        static final int BASE_Y = 64;
        static final int SPACING = 200;
        static final int WAVES = 3;
        static final long RUN_LIMIT_MS = 20L * 60L * 1000L;
        static final String[] ITEM_REWARDS = {"kaboom", "shakalaka", "thunderstick", "vampirefang", "frostbite", "winddash", "infernowand", "groundbreaker", "phoenixblade", "gauntlet", "grenade", "magnetrod", "blizzardorb", "healingcharm", "shadowcloak", "golembell", "sonichorn", "stormheart", "phasefruit", "voidscythe", "grapple", "starfall", "venom", "glacier", "tidal", "reapersigil", "arccaster", "shadowdagger", "solarlance", "gravitywell", "aegistotem", "unoreverse", "chronohourglass", "bloodstone", "quakehammer", "venomsickle", "crystalpick", "rewindcompass", "spoutshell", "honeybomb", "cloudbottle", "meteor", "emberblade", "thornrapier", "moonsplitter", "frostfang", "hailstorm", "sparkline", "ravenshot", "whirlwind", "sunfall", "gravebow", "voidcutter", "bloodletter", "sunder", "ironfang", "pyre", "echo", "stormbow", "chainshot", "ghostmark", "dawnbow", "hexbone", "cursedcoin", "spectralquill", "rottenoffering", "eyeofthecrypt", "ghostlantern"};
        static final String[] SMALL_MOBS = {"ZOMBIE", "SKELETON", "SPIDER", "CAVE_SPIDER", "HUSK", "STRAY", "ZOMBIE_VILLAGER",
                "WITCH", "DROWNED", "ENDERMAN", "PHANTOM", "BLAZE", "SILVERFISH"};
        static final String[] BOSSES = {"WARDEN", "WITHER_SKELETON", "EVOKER", "VINDICATOR", "BLAZE", "GHAST",
                "ELDER_GUARDIAN", "BREEZE", "RAVAGER", "PIGLIN_BRUTE", "VEX", "SPIDER"};
        static final String[] PREFIXES = {"Hollow", "Cursed", "Whispering", "Forgotten", "Weeping", "Sunken", "Bone",
                "Ashen", "Rotting", "Silent", "Crimson", "Pale"};
        static final String[] SUFFIXES = {"Crypt", "Pit", "Shrine", "Catacomb", "Ruins", "Sanctum", "Warren"};
        static final String[] FLOORS = {"DEEPSLATE_BRICKS", "CRACKED_STONE_BRICKS", "MUD_BRICKS", "NETHER_BRICKS",
                "BLACKSTONE", "SCULK", "PACKED_MUD", "POLISHED_BLACKSTONE_BRICKS"};
        static final String[] WALLS = {"DEEPSLATE_TILES", "STONE_BRICKS", "MUD_BRICKS", "RED_NETHER_BRICKS",
                "POLISHED_BLACKSTONE", "SCULK_CATALYST", "BONE_BLOCK", "CHISELED_DEEPSLATE"};
        static final String[] LAYOUTS = {"CIRCLE", "PIT", "SHRINE", "CATACOMB", "SPIRAL", "HALL"};
        static final String[] SIZES = {"SMALL", "MEDIUM", "LARGE"};
        static final int[] RADII = {10, 16, 26};
        static final String[] DEFAULT_KIT = {"BREAD:16", "GOLDEN_APPLE:4", "COOKED_BEEF:16", "IRON_SWORD:1", "SHIELD:1"};

        static final class Dungeon {
            final String id;
            final int index;
            boolean enabled;
            String allowedTeam;
            String display;
            Biome biome;
            String layout;
            String size;
            EntityType boss;
            String bossName;
            List<EntityType> mobs = new ArrayList<>();
            int mobsPerWave;
            String reward;
            Material floor;
            Material wall;
            boolean built;
            Run run;
            Location origin;

            Dungeon(String id, int index) {
                this.id = id;
                this.index = index;
            }

            int radius() {
                return RADII[Math.max(0, indexOf(SIZES, size))];
            }

            Location spawnPoint() {
                return origin.clone().add(0, 1, -radius() + 1);
            }
        }

        static final class Run {
            final UUID player;
            final Dungeon dungeon;
            final Location returnLoc;
            final ItemStack[] savedContents;
            final ItemStack[] savedArmor;
            final List<UUID> mobs = new ArrayList<>();
            final long started = System.currentTimeMillis();
            int wave = 0;
            long nextWaveAt = 0;
            UUID boss;
            double bossMax = 1;
            BossBar bar;
            BukkitTask ticker;
            boolean finished;
            int ticks = 0;

            Run(UUID player, Dungeon dungeon, Location returnLoc, ItemStack[] contents, ItemStack[] armor) {
                this.player = player;
                this.dungeon = dungeon;
                this.returnLoc = returnLoc;
                this.savedContents = contents;
                this.savedArmor = armor;
            }
        }

        private World dungeonWorld;
        private final Map<String, Dungeon> dungeons = new LinkedHashMap<>();
        private final Map<UUID, Run> runs = new HashMap<>();
        private final Map<UUID, Run> pendingRestore = new HashMap<>();
        private final Map<UUID, Run> pendingRespawn = new HashMap<>();
        private final Deque<Runnable> buildQueue = new ArrayDeque<>();
        private final Random rnd = new Random();

        

        @Override
        public void disable() {
            for (Run r : new ArrayList<>(runs.values())) endRun(r, false);
            host.saveModuleConfig("dungeons", cfg);
        }

        // ---------- World and config ----------

        private World ensureWorld() {
            World w = Bukkit.getWorld(WORLD_NAME);
            if (w == null) {
                WorldCreator c = new WorldCreator(WORLD_NAME);
                c.type(WorldType.FLAT);
                c.generatorSettings("{\"biome\":\"minecraft:the_void\",\"layers\":[],\"structures\":{\"structures\":{}}}");
                w = c.createWorld();
            }
            return w;
        }

        private void loadDungeons() {
            if (cfg.getConfigurationSection("dungeons") == null) generateDefaults();
            ConfigurationSection root = cfg.getConfigurationSection("dungeons");
            dungeons.clear();
            int idx = 0;
            for (String id : root.getKeys(false)) {
                ConfigurationSection s = root.getConfigurationSection(id);
                if (s != null) dungeons.put(id, readDungeon(id, s, idx));
                idx++;
            }
            host.saveModuleConfig("dungeons", cfg);
        }

        private Dungeon readDungeon(String id, ConfigurationSection s, int index) {
            Dungeon d = new Dungeon(id, index);
            d.enabled = s.getBoolean("enabled", true);
            d.allowedTeam = s.getString("allowed-team", null);
            if (d.allowedTeam != null && d.allowedTeam.isBlank()) d.allowedTeam = null;
            d.display = s.getString("display", id);
            d.biome = biomeOf(s.getString("biome", "plains"));
            d.layout = s.getString("layout", "CIRCLE").toUpperCase(Locale.ROOT);
            d.size = s.getString("size", "MEDIUM").toUpperCase(Locale.ROOT);
            d.boss = entityOf(s.getString("boss", "ZOMBIE"), EntityType.ZOMBIE);
            d.bossName = s.getString("boss-name", "The Keeper of " + d.display);
            for (String m : s.getStringList("mobs")) d.mobs.add(entityOf(m, EntityType.ZOMBIE));
            if (d.mobs.isEmpty()) d.mobs.add(EntityType.ZOMBIE);
            d.mobsPerWave = Math.max(1, s.getInt("mobs-per-wave", 5));
            d.reward = s.getString("reward", ITEM_REWARDS[index % ITEM_REWARDS.length]);
            d.floor = matOf(s.getString("floor", "STONE_BRICKS"));
            d.wall = matOf(s.getString("wall", "STONE_BRICKS"));
            d.origin = new Location(dungeonWorld, 100 + index * SPACING + 0.5, BASE_Y, 0.5);
            return d;
        }

        private void generateDefaults() {
            List<String> biomes = new ArrayList<>();
            for (Biome b : Registry.BIOME) biomes.add(b.getKey().getKey());
            if (biomes.isEmpty()) biomes.add("plains");
            for (int i = 0; i < 58; i++) {
                String display = PREFIXES[i % 12] + " " + SUFFIXES[(i / 12) % 7];
                String id = (PREFIXES[i % 12] + SUFFIXES[(i / 12) % 7]).toLowerCase(Locale.ROOT);
                String base = "dungeons." + id + ".";
                cfg.set(base + "enabled", true);
                cfg.set(base + "display", display);
                cfg.set(base + "biome", biomes.get(i % biomes.size()));
                cfg.set(base + "layout", LAYOUTS[i % LAYOUTS.length]);
                cfg.set(base + "size", SIZES[i % SIZES.length]);
                cfg.set(base + "boss", BOSSES[i % BOSSES.length]);
                cfg.set(base + "boss-name", "The Keeper of " + display);
                cfg.set(base + "mobs", List.of(SMALL_MOBS[i % SMALL_MOBS.length],
                        SMALL_MOBS[(i + 4) % SMALL_MOBS.length], SMALL_MOBS[(i + 8) % SMALL_MOBS.length]));
                cfg.set(base + "mobs-per-wave", 4 + (i % 4));
                cfg.set(base + "reward", ITEM_REWARDS[i % ITEM_REWARDS.length]);
                cfg.set(base + "floor", FLOORS[i % FLOORS.length]);
                cfg.set(base + "wall", WALLS[(i + 3) % WALLS.length]);
            }
            cfg.set("starter-kit", List.of(DEFAULT_KIT));
            host.saveModuleConfig("dungeons", cfg);
        }

        private Biome biomeOf(String name) {
            Biome b = Registry.BIOME.get(NamespacedKey.minecraft(name.toLowerCase(Locale.ROOT)));
            return b != null ? b : Biome.PLAINS;
        }

        private static Material matOf(String name) {
            Material m = Material.matchMaterial(name);
            return m != null ? m : Material.STONE_BRICKS;
        }

        private static EntityType entityOf(String name, EntityType fallback) {
            try {
                return EntityType.valueOf(name.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                return fallback;
            }
        }

        private static int indexOf(String[] arr, String value) {
            for (int i = 0; i < arr.length; i++) if (arr[i].equalsIgnoreCase(value)) return i;
            return -1;
        }

        // ---------- Building ----------

        private void queueBuild(Dungeon d, Runnable done) {
            if (dungeonWorld == null) return;
            World w = dungeonWorld;
            int R = d.radius();
            int x0 = d.origin.getBlockX();
            for (int dx = -R - 3; dx <= R + 3; dx++) {
                for (int dz = -R - 3; dz <= R + 3; dz++) {
                    final int x = x0 + dx;
                    final int z = dz;
                    w.getChunkAt(x >> 4, z >> 4);
                    for (int y = 56; y <= 90; y += 4) {
                        final int by = y;
                        buildQueue.add(() -> w.setBiome(x, by, z, d.biome));
                    }
                    for (int dy = 0; dy <= 10; dy++) {
                        final int yy = BASE_Y + dy;
                        final Material m = materialAt(d, dx, dy, dz);
                        buildQueue.add(() -> w.getBlockAt(x, yy, z).setType(m == null ? Material.AIR : m, false));
                    }
                }
            }
            buildQueue.add(() -> {
                d.built = true;
                if (done != null) done.run();
            });
        }

        private void pumpBuildQueue() {
            int budget = 3000;
            while (budget-- > 0 && !buildQueue.isEmpty()) {
                try {
                    buildQueue.poll().run();
                } catch (Throwable t) {
                    host.getLogger().log(Level.SEVERE, "Build error", t);
                }
            }
        }

        private Material materialAt(Dungeon d, int dx, int dy, int dz) {
            int R = d.radius();
            double r = Math.sqrt(dx * dx + dz * dz);
            boolean inside = r <= R;
            boolean outerWall = r > R && r <= R + 1.0;
            String L = d.layout;
            if (dy == 0) {
                if (r > R + 1.0) return null;
                if (L.equals("PIT") && r < R * 0.45) return null;
                return d.floor;
            }
            if (outerWall && dy <= 6) return d.wall;
            if (!inside) return null;
            switch (L) {
                case "CIRCLE":
                    if (dy <= 6 && Math.abs(r - R * 0.6) < 0.8 && nearAngle(dx, dz, 45)) return d.wall;
                    return null;
                case "PIT":
                    if (r >= R * 0.45 && r <= R * 0.5 && dy <= 2) return d.wall;
                    return null;
                case "SHRINE": {
                    int s = Math.max(3, R / 2);
                    if (Math.abs(Math.abs(dx) - s) == 0 && Math.abs(Math.abs(dz) - s) == 0 && dy <= 5) return d.wall;
                    if (dy == 1 && r < R * 0.3) return d.floor;
                    return null;
                }
                case "CATACOMB":
                    if (dy <= 5 && (Math.abs(dx) % 7 == 0 || Math.abs(dz) % 7 == 0) && (Math.abs(dx) + Math.abs(dz)) % 14 != 0) return d.wall;
                    return null;
                case "SPIRAL":
                    if (dy <= 4 && (int) r % 6 == 0 && r > 1 && !inGap(dx, dz)) return d.wall;
                    return null;
                case "HALL": {
                    int wdt = Math.max(3, R / 3);
                    if (dy <= 5 && Math.abs(dx) == wdt && Math.abs(dz) <= R) return d.wall;
                    if (dy == 6 && Math.abs(dx) <= wdt) return d.wall;
                    return null;
                }
                default:
                    return null;
            }
        }

        private static boolean nearAngle(int dx, int dz, int step) {
            double deg = Math.toDegrees(Math.atan2(dz, dx));
            if (deg < 0) deg += 360;
            double m = deg % step;
            return m < 6 || step - m < 6;
        }

        private static boolean inGap(int dx, int dz) {
            double deg = Math.toDegrees(Math.atan2(dz, dx));
            if (deg < 0) deg += 360;
            return ((int) (deg / 45)) % 3 == 0;
        }

        // ---------- StickTeams link ----------

        /** Team lookup. Calls the teams module directly. Returns null when the teams module is off or the player has no team. */
        private String teamKeyOf(UUID id) {
            StickModule m = host.activeModule("teams");
            if (!(m instanceof TeamsModule tm)) return null;
            return tm.teamKeyOf(id);
        }

        // ---------- Chat ----------

        private void chat(Player p, String msg) {
            if (p != null) p.sendMessage(msg);
        }

        private void broadcast(String msg) {
            if (cfg.getBoolean("broadcast-chat", true)) Bukkit.broadcastMessage(msg);
        }

        // ---------- Runs ----------

        private void startRun(Player p, Dungeon d) {
            if (dungeonWorld == null) {
                p.sendMessage(ChatColor.RED + "Dungeons are unavailable right now.");
                return;
            }
            if (!d.enabled) {
                p.sendMessage(ChatColor.RED + d.display + " is disabled.");
                return;
            }
            if (d.allowedTeam != null && !p.isOp()) {
                String mine = teamKeyOf(p.getUniqueId());
                if (!d.allowedTeam.equals(mine)) {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + d.display + " is members only.");
                    p.sendMessage(ChatColor.GRAY + "Only members of team " + d.allowedTeam + " can enter. Ask an admin to check your team.");
                    return;
                }
            }
            if (runs.containsKey(p.getUniqueId())) {
                p.sendMessage(ChatColor.RED + "You are already in a dungeon. Use /dungeon leave.");
                return;
            }
            if (d.run != null) {
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + d.display + " is FULL. Only one player can be inside at a time. Try again later.");
                return;
            }
            if (!d.built) {
                p.sendMessage(ChatColor.YELLOW + "Building " + d.display + ". Try again in a few seconds.");
                queueBuild(d, null);
                return;
            }
            ItemStack[] contents = cloneItems(p.getInventory().getContents());
            ItemStack[] armor = cloneItems(p.getInventory().getArmorContents());
            Run run = new Run(p.getUniqueId(), d, p.getLocation().clone(), contents, armor);
            d.run = run;
            runs.put(p.getUniqueId(), run);
            p.getInventory().clear();
            p.getInventory().setArmorContents(new ItemStack[4]);
            giveStarterKit(p);
            p.teleport(d.spawnPoint());
            run.bar = Bukkit.createBossBar(ChatColor.DARK_PURPLE + d.display, BarColor.PURPLE, BarStyle.SEGMENTED_10);
            run.bar.addPlayer(p);
            run.nextWaveAt = System.currentTimeMillis() + 3000;
            run.ticker = Bukkit.getScheduler().runTaskTimer(host, () -> tickRun(run), 20L, 10L);
            p.sendTitle(ChatColor.DARK_PURPLE + d.display, ChatColor.GRAY + "Survive " + WAVES + " waves", 10, 60, 20);
            chat(p, ChatColor.DARK_PURPLE + "You entered " + d.display + ". Your inventory has been saved and you have been given a starter kit.");
            chat(p, ChatColor.GRAY + "The dungeon is locked to you until the run ends. Use /dungeon leave to exit early.");
            broadcast(ChatColor.DARK_PURPLE + p.getName() + ChatColor.GRAY + " entered " + d.display + ". The dungeon is now FULL.");
        }

        private void giveStarterKit(Player p) {
            List<String> kit = cfg.getStringList("starter-kit");
            if (kit.isEmpty()) kit = List.of(DEFAULT_KIT);
            for (String line : kit) {
                String[] parts = line.split(":");
                Material m = Material.matchMaterial(parts[0]);
                if (m == null) continue;
                int amount = parts.length > 1 ? Math.max(1, Integer.parseInt(parts[1].trim())) : 1;
                p.getInventory().addItem(new ItemStack(m, amount));
            }
        }

        private void tickRun(Run run) {
            if (run.finished) return;
            run.ticks++;
            Player p = Bukkit.getPlayer(run.player);
            if (p == null || !p.isOnline()) {
                endRun(run, false);
                return;
            }
            if (System.currentTimeMillis() - run.started > RUN_LIMIT_MS) {
                p.sendMessage(ChatColor.RED + "Time is up. You were removed from the dungeon.");
                endRun(run, false);
                return;
            }
            if (run.boss != null) {
                Entity b = Bukkit.getEntity(run.boss);
                if (b == null || !b.isValid() || b.isDead()) {
                    completeRun(run);
                    return;
                }
                if (b instanceof LivingEntity lb) {
                    run.bar.setProgress(Math.max(0, Math.min(1, lb.getHealth() / run.bossMax)));
                }
            } else {
                run.mobs.removeIf(id -> {
                    Entity e = Bukkit.getEntity(id);
                    return e == null || !e.isValid() || e.isDead();
                });
                if (run.mobs.isEmpty()) {
                    if (run.wave < WAVES) {
                        if (System.currentTimeMillis() >= run.nextWaveAt) {
                            spawnWave(run);
                            run.nextWaveAt = System.currentTimeMillis() + 3000;
                        }
                    } else {
                        spawnBoss(run);
                    }
                }
                run.bar.setProgress(run.wave == 0 ? 1 : Math.max(0.05, (double) run.mobs.size() / Math.max(1, run.dungeon.mobsPerWave)));
                run.bar.setTitle(ChatColor.DARK_PURPLE + run.dungeon.display + ChatColor.GRAY + "  Wave " + run.wave + "/" + WAVES);
            }
            if (run.ticks % 6 == 0) p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 0.6f, 0.7f);
        }

        private Location randomSpotInside(Dungeon d) {
            double angle = rnd.nextDouble() * Math.PI * 2;
            double radius = rnd.nextDouble() * (d.radius() - 3) + 2;
            return d.origin.clone().add(Math.cos(angle) * radius, 1, Math.sin(angle) * radius);
        }

        private void spawnWave(Run run) {
            Dungeon d = run.dungeon;
            run.wave++;
            for (int i = 0; i < d.mobsPerWave; i++) {
                EntityType type = d.mobs.get(rnd.nextInt(d.mobs.size()));
                spawnMob(run, type, null, false);
            }
            Player p = Bukkit.getPlayer(run.player);
            chat(p, ChatColor.GOLD + "Wave " + run.wave + " of " + WAVES + ": " + ChatColor.WHITE + d.mobsPerWave + ChatColor.GOLD + " enemies incoming.");
        }

        private void spawnBoss(Run run) {
            Dungeon d = run.dungeon;
            Location c = d.origin.clone().add(0, 1, 0);
            Entity e;
            try {
                e = c.getWorld().spawnEntity(c, d.boss);
            } catch (IllegalArgumentException ex) {
                e = c.getWorld().spawnEntity(c, EntityType.ZOMBIE);
            }
            if (!(e instanceof LivingEntity le)) return;
            AttributeInstance hp = le.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (hp != null) hp.setBaseValue(200);
            le.setHealth(200);
            run.bossMax = 200;
            le.setCustomName(ChatColor.DARK_RED + "" + ChatColor.BOLD + d.bossName);
            le.setCustomNameVisible(true);
            le.setPersistent(true);
            run.boss = le.getUniqueId();
            run.mobs.add(le.getUniqueId());
            Player p = Bukkit.getPlayer(run.player);
            chat(p, ChatColor.DARK_RED + "" + ChatColor.BOLD + d.bossName + " awakens. Defeat it to clear " + d.display + ".");
            broadcast(ChatColor.DARK_RED + d.bossName + ChatColor.GRAY + " has awakened in " + d.display + ".");
            c.getWorld().playSound(c, Sound.ENTITY_WARDEN_EMERGE, 2f, 0.6f);
        }

        private void spawnMob(Run run, EntityType type, String name, boolean persistent) {
            Location loc = randomSpotInside(run.dungeon);
            try {
                Entity e = loc.getWorld().spawnEntity(loc, type);
                if (e instanceof Mob m) m.setPersistent(persistent);
                run.mobs.add(e.getUniqueId());
            } catch (IllegalArgumentException ignored) {
                // this mob type can't be spawned on this server version
            }
        }

        private void completeRun(Run run) {
            if (run.finished) return;
            Dungeon d = run.dungeon;
            Player p = Bukkit.getPlayer(run.player);
            String reward = d.reward;
            String who = p != null ? p.getName() : "A player";
            endRun(run, true);
            broadcast(ChatColor.GREEN + who + ChatColor.GRAY + " cleared " + d.display + "!");
            if (p != null && p.isOnline()) {
                if (host.activeModule("items") != null) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stickmanitems give " + reward + " " + p.getName());
                    p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + d.display + " cleared! Reward: " + reward);
                } else {
                    p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 4));
                    p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + d.display + " cleared! Reward: 4 golden apples.");
                }
            }
        }

        private void endRun(Run run, boolean success) {
            if (run.finished) return;
            run.finished = true;
            if (run.ticker != null) run.ticker.cancel();
            if (run.bar != null) run.bar.removeAll();
            for (UUID id : run.mobs) {
                Entity e = Bukkit.getEntity(id);
                if (e != null && e.isValid()) e.remove();
            }
            run.mobs.clear();
            run.dungeon.run = null;
            runs.remove(run.player);
            Player p = Bukkit.getPlayer(run.player);
            if (p != null && p.isOnline()) {
                restoreInventory(p, run);
                p.teleport(run.returnLoc);
                if (!success) p.sendMessage(ChatColor.GRAY + "You left " + run.dungeon.display + ".");
            } else {
                pendingRestore.put(run.player, run);
            }
        }

        private void restoreInventory(Player p, Run run) {
            p.getInventory().clear();
            p.getInventory().setContents(cloneItems(run.savedContents));
            p.getInventory().setArmorContents(cloneItems(run.savedArmor));
        }

        private static ItemStack[] cloneItems(ItemStack[] items) {
            ItemStack[] out = new ItemStack[items.length];
            for (int i = 0; i < items.length; i++) out[i] = items[i] == null ? null : items[i].clone();
            return out;
        }

        // ---------- Safety events ----------

        @EventHandler
        public void onDeath(PlayerDeathEvent e) {
            Run run = runs.get(e.getEntity().getUniqueId());
            if (run == null) return;
            e.setKeepInventory(true);
            e.getDrops().clear();
            chat(e.getEntity(), ChatColor.RED + "You were defeated in " + run.dungeon.display + ". Your items will be returned.");
            broadcast(ChatColor.RED + e.getEntity().getName() + ChatColor.GRAY + " was defeated in " + run.dungeon.display + ". The dungeon is open again.");
            e.setKeepLevel(true);
            pendingRespawn.put(run.player, run);
        }

        @EventHandler
        public void onRespawn(PlayerRespawnEvent e) {
            Run run = pendingRespawn.remove(e.getPlayer().getUniqueId());
            if (run == null) return;
            e.setRespawnLocation(run.returnLoc);
            Bukkit.getScheduler().runTaskLater(host, () -> endRun(run, false), 2L);
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent e) {
            Run run = runs.get(e.getPlayer().getUniqueId());
            if (run != null) endRun(run, false);
        }

        @EventHandler
        public void onJoin(PlayerJoinEvent e) {
            Run run = pendingRestore.remove(e.getPlayer().getUniqueId());
            if (run == null) return;
            Bukkit.getScheduler().runTaskLater(host, () -> {
                if (e.getPlayer().isOnline()) {
                    restoreInventory(e.getPlayer(), run);
                    e.getPlayer().teleport(run.returnLoc);
                }
            }, 20L);
        }

        @EventHandler
        public void onBreak(BlockBreakEvent e) {
            if (dungeonWorld != null && e.getBlock().getWorld().equals(dungeonWorld) && !e.getPlayer().isOp()) e.setCancelled(true);
        }

        @EventHandler
        public void onPlace(BlockPlaceEvent e) {
            if (dungeonWorld != null && e.getBlock().getWorld().equals(dungeonWorld) && !e.getPlayer().isOp()) e.setCancelled(true);
        }

        // ---------- Commands ----------

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (args.length == 0) {
                help(sender);
                return true;
            }
            String sub = args[0].toLowerCase(Locale.ROOT);
            switch (sub) {
                case "help" -> help(sender);
                case "list" -> {
                    sender.sendMessage(ChatColor.GOLD + "Dungeons (" + dungeons.size() + "):");
                    for (Dungeon d : dungeons.values()) {
                        sender.sendMessage(ChatColor.GRAY + " - " + d.id + " | " + d.biome.getKey().getKey() + " | " + d.layout
                                + " | " + d.size + (d.enabled ? "" : " | DISABLED") + (d.run != null ? " | BUSY" : ""));
                    }
                }
                case "info" -> {
                    Dungeon d = need(sender, args, 1);
                    if (d == null) return true;
                    sender.sendMessage(ChatColor.GOLD + d.display + " (" + d.id + ")");
                    sender.sendMessage(ChatColor.GRAY + "Biome: " + d.biome.getKey().getKey() + "  Layout: " + d.layout + "  Size: " + d.size);
                    sender.sendMessage(ChatColor.GRAY + "Boss: " + d.boss + " named " + d.bossName);
                    sender.sendMessage(ChatColor.GRAY + "Mobs: " + d.mobs + " x" + d.mobsPerWave + " per wave");
                    sender.sendMessage(ChatColor.GRAY + "Reward: " + d.reward + "  Built: " + d.built + "  Enabled: " + d.enabled);
                    sender.sendMessage(ChatColor.GRAY + "Access: " + (d.allowedTeam == null ? "everyone" : "team " + d.allowedTeam + " only"));
                }
                case "leave" -> {
                    if (!(sender instanceof Player p)) { sender.sendMessage(ChatColor.RED + "Players only."); return true; }
                    Run run = runs.get(p.getUniqueId());
                    if (run == null) sender.sendMessage(ChatColor.RED + "You are not in a dungeon.");
                    else endRun(run, false);
                }
                case "enable", "disable" -> {
                    if (!op(sender)) return true;
                    Dungeon d = need(sender, args, 1);
                    if (d == null) return true;
                    d.enabled = sub.equals("enable");
                    cfg.set("dungeons." + d.id + ".enabled", d.enabled);
                    host.saveModuleConfig("dungeons", cfg);
                    sender.sendMessage(ChatColor.GREEN + d.display + " is now " + (d.enabled ? "enabled" : "disabled") + ".");
                }
                case "build" -> {
                    if (!op(sender)) return true;
                    Dungeon d = need(sender, args, 1);
                    if (d == null) return true;
                    queueBuild(d, () -> sender.sendMessage(ChatColor.GREEN + d.display + " is built."));
                    sender.sendMessage(ChatColor.YELLOW + "Building " + d.display + "...");
                }
                case "rebuild" -> {
                    if (!op(sender)) return true;
                    Dungeon d = need(sender, args, 1);
                    if (d == null) return true;
                    if (d.run != null) endRun(d.run, false);
                    d.built = false;
                    queueBuild(d, () -> sender.sendMessage(ChatColor.GREEN + d.display + " is rebuilt."));
                    sender.sendMessage(ChatColor.YELLOW + "Rebuilding " + d.display + "...");
                }
                case "reset" -> {
                    if (!op(sender)) return true;
                    Dungeon d = need(sender, args, 1);
                    if (d == null) return true;
                    if (d.run != null) endRun(d.run, false);
                    sender.sendMessage(ChatColor.GREEN + d.display + " has been reset.");
                }
                case "set" -> {
                    if (!op(sender)) return true;
                    if (args.length < 4) { sender.sendMessage(ChatColor.RED + "Usage: /dungeon set <name> <size|layout|boss|reward|mobs-per-wave> <value>"); return true; }
                    Dungeon d = dungeons.get(args[1].toLowerCase(Locale.ROOT));
                    if (d == null) { sender.sendMessage(ChatColor.RED + "Unknown dungeon."); return true; }
                    String key = args[2].toLowerCase(Locale.ROOT);
                    String value = args[3];
                    switch (key) {
                        case "size" -> {
                            d.size = value.toUpperCase(Locale.ROOT);
                            d.built = false;
                        }
                        case "layout" -> {
                            d.layout = value.toUpperCase(Locale.ROOT);
                            d.built = false;
                        }
                        case "boss" -> d.boss = entityOf(value, d.boss);
                        case "reward" -> d.reward = value;
                        case "mobs-per-wave" -> d.mobsPerWave = Math.max(1, Integer.parseInt(value));
                        default -> { sender.sendMessage(ChatColor.RED + "Unknown setting."); return true; }
                    }
                    cfg.set("dungeons." + d.id + "." + (key.equals("mobs-per-wave") ? "mobs-per-wave" : key),
                            key.equals("mobs-per-wave") ? d.mobsPerWave : value);
                    host.saveModuleConfig("dungeons", cfg);
                    sender.sendMessage(ChatColor.GREEN + d.display + " " + key + " set to " + value
                            + (d.built ? "" : ". Run /dungeon rebuild " + d.id + " to apply the change."));
                }
                case "kit" -> {
                    if (!op(sender)) return true;
                    if (args.length >= 2 && args[1].equalsIgnoreCase("set")) {
                        List<String> kit = new ArrayList<>(Arrays.asList(args).subList(2, args.length));
                        cfg.set("starter-kit", kit);
                        host.saveModuleConfig("dungeons", cfg);
                        sender.sendMessage(ChatColor.GREEN + "Starter kit set to " + kit);
                    } else {
                        sender.sendMessage(ChatColor.GRAY + "Starter kit: " + cfg.getStringList("starter-kit"));
                        sender.sendMessage(ChatColor.GRAY + "Change it with /dungeon kit set BREAD:16 GOLDEN_APPLE:4 ...");
                    }
                }
                case "allow", "disallow" -> {
                    if (!op(sender)) return true;
                    Dungeon d = need(sender, args, 1);
                    if (d == null) return true;
                    if (sub.equals("allow")) {
                        String team;
                        if (args.length >= 3) {
                            team = args[2].toLowerCase(Locale.ROOT);
                        } else if (sender instanceof Player ap) {
                            team = teamKeyOf(ap.getUniqueId());
                            if (team == null) {
                                sender.sendMessage(ChatColor.RED + "You are not in a team. Use /dungeon allow <dungeon> <team>.");
                                return true;
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Give a team name from the console: /dungeon allow <dungeon> <team>.");
                            return true;
                        }
                        d.allowedTeam = team;
                        cfg.set("dungeons." + d.id + ".allowed-team", team);
                        host.saveModuleConfig("dungeons", cfg);
                        sender.sendMessage(ChatColor.GREEN + d.display + " is now open to members of team " + team + " only.");
                    } else {
                        d.allowedTeam = null;
                        cfg.set("dungeons." + d.id + ".allowed-team", null);
                        host.saveModuleConfig("dungeons", cfg);
                        sender.sendMessage(ChatColor.GREEN + d.display + " is now open to everyone.");
                    }
                }
                case "reload" -> {
                    if (!op(sender)) return true;
                    for (Run r : new ArrayList<>(runs.values())) endRun(r, false);
                    cfg = host.moduleConfig("dungeons");
                    loadDungeons();
                    sender.sendMessage(ChatColor.GREEN + "Reloaded " + dungeons.size() + " dungeons.");
                }
                default -> {
                    Dungeon d = dungeons.get(sub);
                    if (d == null) { sender.sendMessage(ChatColor.RED + "Unknown dungeon or command. Try /dungeon help"); return true; }
                    if (args.length >= 2 && (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("disable"))) {
                        if (!op(sender)) return true;
                        d.enabled = args[1].equalsIgnoreCase("enable");
                        cfg.set("dungeons." + d.id + ".enabled", d.enabled);
                        host.saveModuleConfig("dungeons", cfg);
                        sender.sendMessage(ChatColor.GREEN + d.display + " is now " + (d.enabled ? "enabled" : "disabled") + ".");
                        return true;
                    }
                    if (!(sender instanceof Player p)) { sender.sendMessage(ChatColor.RED + "Players only."); return true; }
                    startRun(p, d);
                }
            }
            return true;
        }

        private void help(CommandSender s) {
            s.sendMessage(ChatColor.GOLD + "StickManDungeonsBeta");
            s.sendMessage(ChatColor.YELLOW + "/dungeon <name>" + ChatColor.GRAY + " - enter a dungeon");
            s.sendMessage(ChatColor.YELLOW + "/dungeon list | info <name> | leave");
            s.sendMessage(ChatColor.YELLOW + "/dungeon enable|disable|build|rebuild|reset <name>" + ChatColor.GRAY + " (operators)");
            s.sendMessage(ChatColor.YELLOW + "/dungeon set <name> <size|layout|boss|reward|mobs-per-wave> <value>" + ChatColor.GRAY + " (operators)");
            s.sendMessage(ChatColor.YELLOW + "/dungeon kit [set ...] | reload" + ChatColor.GRAY + " (operators)");
            s.sendMessage(ChatColor.YELLOW + "/dungeon allow <name> [team] | disallow <name>" + ChatColor.GRAY + " (operators)");
        }

        private boolean op(CommandSender s) {
            if (!s.isOp()) {
                s.sendMessage(ChatColor.RED + "Only operators can do that.");
                return false;
            }
            return true;
        }

        private Dungeon need(CommandSender s, String[] args, int idx) {
            if (args.length <= idx) {
                s.sendMessage(ChatColor.RED + "Give a dungeon name.");
                return null;
            }
            Dungeon d = dungeons.get(args[idx].toLowerCase(Locale.ROOT));
            if (d == null) s.sendMessage(ChatColor.RED + "Unknown dungeon.");
            return d;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            if (args.length == 1) {
                List<String> out = new ArrayList<>(dungeons.keySet());
                out.addAll(List.of("list", "info", "leave", "enable", "disable", "build", "rebuild", "reset", "set", "kit", "reload", "help"));
                return out.stream().filter(s -> s.startsWith(args[0].toLowerCase(Locale.ROOT))).toList();
            }
            if (args.length == 2 && List.of("info", "enable", "disable", "build", "rebuild", "reset", "set").contains(args[0].toLowerCase(Locale.ROOT))) {
                return dungeons.keySet().stream().filter(s -> s.startsWith(args[1].toLowerCase(Locale.ROOT))).toList();
            }
            if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
                return List.of("size", "layout", "boss", "reward", "mobs-per-wave");
            }
            if (args.length == 4 && args[0].equalsIgnoreCase("set")) {
                if (args[2].equalsIgnoreCase("size")) return List.of(SIZES).stream().map(String::toLowerCase).toList();
                if (args[2].equalsIgnoreCase("layout")) return List.of(LAYOUTS).stream().map(String::toLowerCase).toList();
                if (args[2].equalsIgnoreCase("reward")) return List.of(ITEM_REWARDS);
            }
            return List.of();
        }
    }

    // ===== ItemsModule.java =====
    public static class ItemsModule implements StickModule, Listener, CommandExecutor, TabCompleter {

        private StickmanUniversal host;
        private FileConfiguration cfg;

        @Override
        public String name() {
            return "items";
        }

        @Override
        public void enable(StickmanUniversal h) {
            host = h;
            cfg = host.moduleConfig("items");

            itemKey = new NamespacedKey(host, "item");
            projKey = new NamespacedKey(host, "proj");
            ownerKey = new NamespacedKey(host, "owner");
            ultKey = new NamespacedKey(host, "ult");
            registerItems();
            registerGems();
            loadShrines();
            host.getServer().getPluginManager().registerEvents(this, host);
            host.getCommand("stickmanitems").setExecutor(this);
            host.getCommand("stickmanitems").setTabCompleter(this);
            Bukkit.getScheduler().runTaskTimer(host, this::tick, 1L, 1L);
            host.getLogger().info("StickmanItems " + VERSION + " enabled.");
        }


        private static final String VERSION = "4.0.0";

        private record ItemDef(String id, String name, Material mat, String hex, String lore) {}

        private static final class PhoenixState {
            int phase; // 0 = rising, 1 = diving, 2 = just landed
            int ticks;
            double peak;
            boolean call;
        }

        private final Map<String, ItemDef> items = new LinkedHashMap<>();
        private final Map<String, Long> cooldowns = new HashMap<>();
        private final Map<UUID, Integer> jetFuel = new HashMap<>();
        private final Map<UUID, PhoenixState> phoenix = new HashMap<>();
        private final Map<UUID, UUID> holds = new HashMap<>();
        private final Map<UUID, UUID> golems = new HashMap<>();
        private final Map<UUID, Integer> fallGuard = new HashMap<>();
        private final Random rnd = new Random();
        private final Set<UUID> noCooldown = new HashSet<>();
        private int hudTick = 0;
        private int gemTick = 0;
        private NamespacedKey ownerKey;
        private NamespacedKey ultKey;
        private final Map<String, ItemDef> gems = new LinkedHashMap<>();
        private final Set<UUID> gemNoCd = new HashSet<>();
        private final Map<UUID, Set<UUID>> trusts = new HashMap<>();
        private final Map<UUID, Long> snowUntil = new HashMap<>();
        private final Map<UUID, Long> nightmareUntil = new HashMap<>();
        private final Map<UUID, Long> witherTouchUntil = new HashMap<>();
        private final Map<UUID, Long> subzeroUntil = new HashMap<>();
        private final Map<UUID, Deque<EntityType>> lastKills = new HashMap<>();
        private final Map<String, Integer> snowHits = new HashMap<>();
        private final List<BarrierState> barriers = new ArrayList<>();
        private final Map<UUID, Integer> abilityCount = new HashMap<>();
        private final Set<UUID> ultReady = new HashSet<>();
        private final List<UUID> followers = new ArrayList<>();
        private final Map<UUID, double[]> charging = new HashMap<>();
        private final Map<UUID, Set<UUID>> voidHits = new HashMap<>();
        private final Map<UUID, Deque<EntityType>> unoStore = new HashMap<>();

        private static final Map<String, String> ABILITY_NAMES = Map.ofEntries(
                Map.entry("g_lift", "Frost Lift"), Map.entry("g_barrier", "Ice Barrier"), Map.entry("g_snow", "Snow Storm"),
                Map.entry("g_subzero", "Subzero"), Map.entry("g_rain", "Trident Rain"), Map.entry("g_terror", "Night Terror"),
                Map.entry("g_punish", "Nightmare Punishment"), Map.entry("g_abyss", "Abyssal Sight"), Map.entry("g_creak", "Nightmare Phantom"),
                Map.entry("g_legion", "Wither Legion"), Map.entry("g_recall", "Fallen Recall"), Map.entry("g_touch", "Wither Touch"),
                Map.entry("g_giant", "Hollow Colossus"), Map.entry("g_tstrike", "Thunder Strike"), Map.entry("g_chain", "Chain Lightning"),
                Map.entry("g_surge", "Overcharge"), Map.entry("g_zeus", "Zeus"), Map.entry("g_gust", "Gust"), Map.entry("g_updraft", "Updraft"),
                Map.entry("g_tornado", "Tornado"), Map.entry("g_breeze", "Berserk Breeze"), Map.entry("g_meteor", "Meteor Shower"),
                Map.entry("g_campfire", "Cozy Campfire"), Map.entry("g_charge", "Chargeable Fireball"), Map.entry("g_blaze", "Blaze"),
                Map.entry("g_atomic", "Atomic Blast"), Map.entry("g_cloud", "Radiation Cloud"), Map.entry("g_shield", "Fallout Shield"),
                Map.entry("g_meltdown", "Meltdown"), Map.entry("g_sonic", "Sonic Wave"), Map.entry("g_echo", "Echo Pulse"),
                Map.entry("g_fist", "Warden's Fist"), Map.entry("g_wardenult", "Warden Monster"), Map.entry("g_yin", "Yin Strike"),
                Map.entry("g_yang", "Yang Strike"), Map.entry("g_balance", "Balance"), Map.entry("g_taijitu", "Taijitu"), Map.entry("reaper_soul", "Soul Drain"), Map.entry("reaper_mark", "Death Mark"),
                Map.entry("g_pad", "Jump Pad"), Map.entry("g_wburst", "Wind Burst"), Map.entry("g_pulse", "Void Pulse"),
                Map.entry("g_step", "Shadow Step"), Map.entry("g_umbral", "Umbral Shield"), Map.entry("g_eclipse", "Total Eclipse"),
                Map.entry("g_soldiers", "Summon Soldiers"), Map.entry("g_rally", "Rally"), Map.entry("g_curse", "Curse of Iron"),
                Map.entry("g_darklegion", "Dark Legion"));

        private static final Map<String, List<String>> GEM_ABILITIES = Map.of(
                "thunder", List.of("g_tstrike", "g_chain", "g_surge", "g_zeus"),
                "wind", List.of("g_gust", "g_updraft", "g_tornado", "g_breeze"),
                "inferno", List.of("g_meteor", "g_campfire", "g_charge", "g_blaze"),
                "blizzard", List.of("g_lift", "g_barrier", "g_snow", "g_subzero"),
                "nightmare", List.of("g_terror", "g_punish", "g_abyss", "g_creak"),
                "undead", List.of("g_legion", "g_recall", "g_touch", "g_giant"),
                "atomic", List.of("g_atomic", "g_cloud", "g_shield", "g_meltdown"),
                "warden", List.of("g_sonic", "g_echo", "g_fist", "g_wardenult"),
                "yinyang", List.of("g_yin", "g_yang", "g_balance", "g_taijitu"),
                "eclipse", List.of("g_pulse", "g_step", "g_umbral", "g_eclipse"));
        private static final Set<EntityType> NO_SUMMON = EnumSet.of(
                EntityType.WARDEN, EntityType.WITHER, EntityType.ENDER_DRAGON,
                EntityType.ELDER_GUARDIAN, EntityType.GIANT, EntityType.IRON_GOLEM);
        private static final List<String> STICKMAN_SUBS = List.of("givegem", "nocooldowngem", "trust", "summon", "help", "cooldowns", "gems", "reload", "clearcd", "heal", "feed", "followers", "cleargems", "trustlist", "dismount", "killmounts", "time", "weather", "stats", "menu", "killfollowers", "lightning", "bring", "tp", "fly", "invis", "crowncheck", "debug");
        private final Map<String, ShrineHolder> shrines = new HashMap<>();
        private final Map<UUID, List<ItemStack>> deathKeep = new HashMap<>();
        private final Map<UUID, Integer> boltCharges = new HashMap<>();
        private final Map<UUID, Integer> surgeHits = new HashMap<>();
        private final Map<UUID, Long> zeusUntil = new HashMap<>();
        private boolean inThunderProc = false;
        private final Map<UUID, Long> windBerserkUntil = new HashMap<>();
        private final Map<UUID, Long> blazeUntil = new HashMap<>();
        private final Set<UUID> evapOn = new HashSet<>();
        private final Map<UUID, Map<Block, Material>> evapSaved = new HashMap<>();
        private final Map<String, UUID> pads = new HashMap<>();
        private final Map<UUID, Long> launchedUntil = new HashMap<>();
        private final Map<UUID, Long> shieldUntil = new HashMap<>();
        private final Map<UUID, Long> meltdownUntil = new HashMap<>();

        private NamespacedKey itemKey;
        private NamespacedKey projKey;

        

        private void def(String id, String name, Material mat, String hex, String lore) {
            items.put(id, new ItemDef(id, name, mat, hex, lore));
            modelIds.put(id, 1000 + items.size());
        }

        private void giveOrDrop(Player p, ItemStack it) {
            for (ItemStack rest : p.getInventory().addItem(it).values()) {
                p.getWorld().dropItemNaturally(p.getLocation(), rest);
            }
        }

        private boolean hasGem(Player p) {
            for (ItemStack it : p.getInventory().getContents()) {
                if (isGem(it)) return true;
            }
            return false;
        }

        private void registerItems() {
            def("kaboom", "Kaboom Kachow", Material.BOW, "#FF3D00", "Every arrow shot from this bow explodes on impact.");
            def("shakalaka", "Shakalaka Bow", Material.BOW, "#00FF9C", "Each arrow that lands triggers 3 random effects: lightning, explosion or fireball.");
            def("thunderstick", "Thunderstick", Material.NETHERITE_SWORD, "#FFEA00", "Hits strike lightning. Right-click to call lightning where you look.");
            def("vampirefang", "Vampire Fang", Material.NETHERITE_SWORD, "#FF1744", "Heals you for 40% of melee damage. Right-click for a 4-heart heal.");
            def("frostbite", "Frostbite", Material.NETHERITE_SWORD, "#00E5FF", "Hits slow targets and add extra damage. Right-click for a frost nova.");
            def("winddash", "Wind Shard", Material.BREEZE_ROD, "#B3E5FC", "Right-click to dash forward. No cooldown. No fall damage from the dash.");
            def("infernowand", "Inferno Wand", Material.BLAZE_ROD, "#FF9100", "Right-click to launch a fireball. Fireballs do not break blocks.");
            def("groundbreaker", "Groundbreaker", Material.MACE, "#FFB300", "Right-click for a shockwave that damages and knocks back nearby enemies. Breaks no blocks.");
            def("phoenixblade", "Phoenix Blade", Material.MACE, "#FF2D75", "Right-click to leap into the air, then dive and smash. Harder falls hit harder.");
            def("gauntlet", "Control Orb", Material.PRISMARINE_SHARD, "#7C4DFF", "Right-click a mob or player to take control of it. Right-click again to hurl it forward.");
            def("grenade", "Grenade", Material.EGG, "#76FF03", "Right-click to throw. Explodes on impact without breaking blocks or hurting you.");
            def("magnetrod", "Magnet Rod", Material.IRON_NUGGET, "#D500F9", "Right-click to pull nearby mobs toward you, hard.");
            def("blizzardorb", "Blizzard Orb", Material.SNOWBALL, "#18FFFF", "Right-click to freeze everything nearby and slow it hard.");
            def("healingcharm", "Healing Charm", Material.GHAST_TEAR, "#FF4081", "Right-click to heal 8 hearts, gain regeneration and remove negative effects.");
            def("shadowcloak", "Shadow Cloak", Material.PHANTOM_MEMBRANE, "#6200EA", "Right-click for 20 seconds of invisibility and Speed II.");
            def("golembell", "Golem Call", Material.GOAT_HORN, "#FFC400", "Blow the horn to summon a friendly iron golem for 90 seconds.");
            def("sonichorn", "Sonic Horn", Material.GOAT_HORN, "#FFAB40", "Right-click for a sonic boom that hits hard and knocks back everything in its path.");
            def("stormheart", "Storm Heart", Material.HEART_OF_THE_SEA, "#2962FF", "Right-click to call lightning on up to 5 nearby mobs.");
            def("phasefruit", "Phase Fruit", Material.CHORUS_FRUIT, "#00E676", "Right-click to blink forward up to 12 blocks, then get Speed II. Never puts you inside a block.");
            def("voidscythe", "Void Slasher", Material.NETHERITE_SWORD, "#651FFF", "Right-click for a wide void slash that hits everything in front of you.");
            def("grapple", "Grapple Hook", Material.LEAD, "#8D6E63", "Right-click to hook a block or mob and pull yourself toward it. Pulls harder.");
            def("starfall", "Starfall Staff", Material.STICK, "#FFF59D", "Right-click to call 5 falling stars onto the spot you look at.");
            def("venom", "Venom Dagger", Material.NETHERITE_SWORD, "#64DD17", "Poisons normal mobs and players. Heals undead mobs. Right-click to lunge.");
            def("glacier", "Phoenix Call", Material.MACE, "#FF6E40", "Right-click to leap, then slam with fire and ice. The higher you fall, the harder it hits.");
            def("tidal", "Tidal Sword", Material.NETHERITE_SWORD, "#0091EA", "Right-click to fire a water orb that blasts everything near its impact.");
            def("reapersigil", "Reaper's Sigil", Material.WITHER_SKELETON_SKULL, "#2962FF", "Right-click: Blue Wither Skull, a blue skull that withers and damages whatever it hits. Shift + right-click: Soul Drain, 8 damage to nearby enemies and heals you per hit. Sprint + right-click: Death Mark, marks the nearest enemy for 10 seconds for +50% damage. Offhand: lifesteal, you heal the damage you deal to players.");
            def("arccaster", "Arc Caster", Material.BOW, "#E1BEE7", "A bow. Arrows stun or freeze whatever they hit.");
            def("shadowdagger", "Shadow Dagger", Material.NETHERITE_SWORD, "#7E57C2", "Right-click to teleport behind the nearest enemy and strike for 10 damage.");
            def("solarlance", "Solar Lance", Material.NETHERITE_SWORD, "#FFD180", "Right-click for an instant beam that hits everything in a 25-block line for 14 damage.");
            def("gravitywell", "Gravity Well", Material.MAGMA_CREAM, "#AB47BC", "Right-click to place a gravity well for 6 seconds. It pulls and damages nearby mobs.");
            def("aegistotem", "Aegis Totem", Material.TOTEM_OF_UNDYING, "#66BB6A", "Right-click for 15 seconds of Resistance and Regeneration for you and your trusted players nearby.");
            def("unoreverse", "Uno Reverse Blade", Material.NETHERITE_SWORD, "#00E5FF", "Saves every projectile that hits you (up to 30). Right-click to release them back, newest first.");
            def("chronohourglass", "Chrono Hourglass", Material.CLOCK, "#FFD700", "Right-click to freeze every enemy within 10 blocks for 4 seconds. Trusted players and your mobs are not affected.");
            def("bloodstone", "Bloodstone Mace", Material.MACE, "#B71C1C", "Hits heal you for 10% of the damage dealt. Right-click for a blood nova that damages and slows nearby enemies.");
            def("quakehammer", "Quake Hammer", Material.MACE, "#8D6E63", "Right-click to slam the ground. Everything within 6 blocks takes 18 damage and is thrown into the air.");
            def("venomsickle", "Venom Sickle", Material.NETHERITE_SWORD, "#33691E", "Right-click to release a poison cloud for 8 seconds. Poisons normal mobs and players, heals undead.");
            def("crystalpick", "Crystal Pick", Material.NETHERITE_PICKAXE, "#E1F5FE", "Right-click to fire 5 crystal shards in a spread. Each does 7 damage in a small blast.");
            def("rewindcompass", "Rewind Compass", Material.RECOVERY_COMPASS, "#00BFA5", "Right-click to teleport back to where you were 10 seconds ago. 30-second cooldown.");
            def("spoutshell", "Nautilus Spout", Material.NAUTILUS_SHELL, "#0288D1", "Right-click to raise a water spout where you aim. Enemies near it are launched 2.5 blocks up and take 8 damage. 12-second cooldown.");
            def("honeybomb", "Honey Bomb", Material.HONEY_BOTTLE, "#FFB300", "Right-click to throw a honey bomb. On impact enemies nearby are heavily slowed and take 4 damage. 6-second cooldown.");
            def("cloudbottle", "Cloud Bottle", Material.GLASS_BOTTLE, "#B3E5FC", "Right-click to conjure a cloud that lifts you about 8 blocks and lowers you gently back down. 20-second cooldown.");
            def("meteor", "Meteor", Material.DRAGON_BREATH, "#E040FB", "Right-click to call 10 to 20 dragon fireballs down on random spots around where you aim. 90-second cooldown.");
            def("emberblade", "Ember Blade", Material.NETHERITE_SWORD, "#FF7043", "Ability 1 (right-click): Ember slash hits every enemy in a wide arc in front of you for 8 damage and sets them on fire for 4 seconds. 5-second cooldown.");
            def("thornrapier", "Thorn Rapier", Material.NETHERITE_SWORD, "#66BB6A", "Ability 1 (right-click): Lunge forward and stab the first enemy you reach for 10 damage and 5 seconds of Poison. 6-second cooldown.");
            def("moonsplitter", "Moonsplitter", Material.NETHERITE_SWORD, "#B39DDB", "Ability 1 (right-click): Crescent slash hits every enemy within 5 blocks for 9 damage and knocks them back. 7-second cooldown.");
            def("frostfang", "Frostfang Greatsword", Material.NETHERITE_SWORD, "#81D4FA", "Ability 1 (right-click): Frost wave in a cone 8 blocks long deals 6 damage and slows enemies for 5 seconds. 9-second cooldown.");
            def("hailstorm", "Hailstorm Crossbow", Material.CROSSBOW, "#B3E5FC", "Every shot also fires 4 spread ice bolts that deal 4 damage each. The extra bolts recharge every 4 seconds.");
            def("sparkline", "Sparkline Crossbow", Material.CROSSBOW, "#FFF176", "Every bolt explodes on impact, dealing 6 damage in a 2.5 block radius without breaking blocks.");
            def("ravenshot", "Ravenshot Crossbow", Material.CROSSBOW, "#37474F", "Bolts pierce up to 3 targets. The first enemy hit takes 10 damage and is slowed for 3 seconds.");
            def("whirlwind", "Whirlwind Bow", Material.BOW, "#4DD0E1", "Arrows that hit a player spin them like a tornado for 3 seconds, lifting them and dealing 1 damage every half second.");
            def("sunfall", "Sunfall Bow", Material.BOW, "#FFCA28", "Arrows that land rain 8 arrows down on the impact area.");
            def("gravebow", "Gravebow", Material.BOW, "#6D4C41", "Arrows pull every enemy within 5 blocks of the impact point toward it.");
            def("voidcutter", "Void Cutter", Material.NETHERITE_SWORD, "#7E57C2", "Ability 1 (right-click): Blink 6 blocks forward and slash everything within 2.5 blocks of where you land for 10 damage. 8-second cooldown.");
            def("bloodletter", "Bloodletter", Material.NETHERITE_SWORD, "#B71C1C", "Ability 1 (right-click): Spend 2 hearts to deal 12 damage to every enemy within 3 blocks. You need more than 3 hearts to use it. 10-second cooldown.");
            def("sunder", "Sunder Blade", Material.NETHERITE_SWORD, "#8D6E63", "Ability 1 (right-click): Slam the ground, dealing 7 damage to enemies within 4 blocks and knocking them back. 6-second cooldown.");
            def("ironfang", "Ironfang Crossbow", Material.CROSSBOW, "#90A4AE", "Every bolt deals 5 damage and slows the target for 2 seconds.");
            def("pyre", "Pyre Crossbow", Material.CROSSBOW, "#FF5722", "Every bolt deals 3 damage and sets the target on fire for 5 seconds.");
            def("echo", "Echo Crossbow", Material.CROSSBOW, "#26C6DA", "Each bolt deals 6 damage, then bounces to the nearest other enemy within 6 blocks for another 6 damage.");
            def("stormbow", "Stormbow", Material.BOW, "#FFEB3B", "Arrows call lightning where they land, dealing 6 damage to enemies within 2.5 blocks.");
            def("chainshot", "Chainshot Bow", Material.BOW, "#78909C", "An arrow that hits an enemy pulls them toward you.");
            def("ghostmark", "Ghostmark Bow", Material.BOW, "#E0E0E0", "An arrow that hits an enemy deals 3 damage and makes them glow for 8 seconds.");
            def("dawnbow", "Dawnbow", Material.BOW, "#FFF59D", "An arrow that hits an enemy deals 4 damage and heals you for 2 hearts.");
            def("soulreaver", "Soulreaver", Material.NETHERITE_SWORD, "#4A148C", "Ability 1 (right-click): Deals 6 hearts to every enemy within 4 blocks and heals you for half of the total, up to 6 hearts. 12-second cooldown.");
            def("stormfang", "Stormfang", Material.NETHERITE_SWORD, "#29B6F6", "Ability 1 (right-click): Chain lightning strikes the 3 nearest enemies within 8 blocks for 6 damage each. 9-second cooldown.");
            def("venomtooth", "Venomtooth", Material.NETHERITE_SWORD, "#7CB342", "Ability 1 (right-click): Releases a poison cloud around you for 6 seconds. Enemies within 3 blocks are poisoned and take damage each second. 14-second cooldown.");
            def("bladestorm", "Bladestorm", Material.NETHERITE_SWORD, "#90A4AE", "Ability 1 (right-click): Spins twice, hitting every enemy within 3 blocks for 5 damage each time. 8-second cooldown.");
            def("frostbolt", "Frostbite Crossbow", Material.CROSSBOW, "#80DEEA", "Every bolt deals 4 damage and slows the target for 3 seconds.");
            def("thunderbolt", "Thunderbolt Crossbow", Material.CROSSBOW, "#FFF176", "Every bolt calls a lightning strike where it lands, dealing 5 damage to enemies within 2 blocks.");
            def("rocketbolt", "Rocket Crossbow", Material.CROSSBOW, "#FF7043", "Every bolt explodes on impact, dealing 8 damage in a 3 block radius.");
            def("vinebow", "Vinebow", Material.BOW, "#558B2F", "An arrow that hits an enemy deals 3 damage and roots them in place for 2 seconds.");
            def("gale", "Gale Bow", Material.BOW, "#B2EBF2", "Arrows blast enemies within 3 blocks of the impact point away from it.");
            def("ironbow", "Ironbow", Material.BOW, "#B0BEC5", "Arrows pierce up to 2 targets and deal 6 damage to each one they hit.");
            def("hexbone", "Hex Bone", Material.BONE, "#CFD8DC", "Right-click to curse the ground around you for 15 seconds. Enemies within 6 blocks take 3 damage a second and are slowed. 25-second cooldown.");
            def("cursedcoin", "Cursed Coin", Material.GOLD_NUGGET, "#FFB74D", "Right-click to flip a cursed coin. It can heal you, blind nearby enemies, or wail so loudly that nearby enemies are slowed. 15-second cooldown.");
            def("spectralquill", "Spectral Quill", Material.FEATHER, "#E1F5FE", "Right-click to write a curse on the nearest enemy within 15 blocks. It takes 12 damage and gains Wither and Darkness. 12-second cooldown.");
            def("rottenoffering", "Rotten Offering", Material.ROTTEN_FLESH, "#558B2F", "Right-click to raise 3 zombie servants for 60 seconds. They follow you and attack whatever you attack. 60-second cooldown.");
            def("eyeofthecrypt", "Eye of the Crypt", Material.SPIDER_EYE, "#7E57C2", "Right-click to reveal every invisible or hidden creature within 20 blocks for 8 seconds. 20-second cooldown.");
            def("ghostlantern", "Ghost Lantern", Material.GLOW_INK_SAC, "#B388FF", "Right-click for 10 seconds of orbiting spirits that zap nearby enemies. Never hits your golems.");
        }

        private void tick() {
            try {
                tickInner();
            } catch (Throwable t) {
                host.getLogger().log(Level.SEVERE, "Tick error", t);
            }
        }

        private void tickInner() {
            tickPhoenix();
            tickJetpack();
            tickGem();
            tickFallGuard();
            tickGems();
            tickAxolotlRide();
            tickEvaporation();
            tickParrotRide();
            tickGiantRide();
            if (++hudTick % 5 == 0) updateHud();
        }

        // Action bar sits directly above the hotbar.
        private void updateHud() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                double[] cs = charging.get(p.getUniqueId());
                if (cs != null) {
                    p.sendActionBar(ChatColor.GOLD + "Charge: " + (int) cs[0] + "%  (press F to release)");
                    continue;
                }
                String gem = gemOf(p);
                if (gem != null && gems.containsKey(gem)) {
                    p.sendActionBar(gemTable(p, gem));
                    continue;
                }
                String id = idOf(p.getInventory().getItemInMainHand());
                if (id == null) id = idOf(p.getInventory().getItemInOffHand());
                if (id == null) continue;
                if (id.equals("kaboom") || id.equals("shakalaka") || id.equals("arccaster") || id.equals("jetpack")) continue;
                ItemDef d = items.get(id);
                if (d == null) continue;
                String title = hexColor(d.hex()) + d.name();
                String text;
                if (noCooldown.contains(p.getUniqueId())) {
                    text = title + ChatColor.AQUA + " - NO COOLDOWN";
                } else {
                    long left = cdLeft(p, id);
                    text = left > 0
                            ? title + ChatColor.GRAY + " - " + ChatColor.RED + String.format(Locale.ROOT, "%.1fs", left / 1000.0)
                            : title + ChatColor.GREEN + " - READY";
                }
                p.sendActionBar(text);
            }
        }

        private String gemTable(Player p, String gem) {
            ItemDef d = gems.get(gem);
            StringBuilder sb = new StringBuilder(hexColor(d.hex()) + ChatColor.BOLD + d.name());
            int slot = 0;
            for (String ab : GEM_ABILITIES.getOrDefault(gem, List.of())) {
                slot++;
                long left = cdLeft(p, ab);
                sb.append(ChatColor.GRAY).append(" | ").append(left > 0 ? ChatColor.RED : ChatColor.GREEN).append("A").append(slot);
                if (left > 0) sb.append(ChatColor.RED).append(" ").append((left / 1000) + 1).append("s");
            }
            return sb.toString();
        }

        // ---------- Helpers ----------

        private void venomTouch(LivingEntity le) {
            if (isUndead(le)) {
                le.setHealth(Math.min(le.getMaxHealth(), le.getHealth() + 4));
            } else {
                le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
            }
        }

        private void cleanse(Player p) {
            for (PotionEffectType t : List.of(PotionEffectType.WITHER, PotionEffectType.POISON,
                    PotionEffectType.SLOWNESS, PotionEffectType.WEAKNESS, PotionEffectType.NAUSEA,
                    PotionEffectType.BLINDNESS, PotionEffectType.DARKNESS, PotionEffectType.HUNGER)) {
                p.removePotionEffect(t);
            }
        }

        // Ult Ready: 10 ability uses unlock the ultimate for the next cast.
        private void countAbility(Player p) {
        }

        private boolean ultGate(Player p) {
            return true;
        }

        private void addFollower(Entity e) {
            followers.add(e.getUniqueId());
        }

        // Summoned undead and creakings walk back to their owner like a dog.
        private void tickFollowers() {
            Iterator<UUID> it = followers.iterator();
            while (it.hasNext()) {
                Entity mob = Bukkit.getEntity(it.next());
                if (mob == null || !mob.isValid()) { it.remove(); continue; }
                UUID own = ownerOf(mob);
                Player owner = own == null ? null : Bukkit.getPlayer(own);
                if (owner == null || !owner.isOnline()) continue;
                if (mob.getWorld() != owner.getWorld()) {
                    mob.teleport(owner.getLocation());
                    continue;
                }
                double d = mob.getLocation().distance(owner.getLocation());
                if (d > 25) {
                    mob.teleport(owner.getLocation().add(2, 0, 2));
                } else if (d > 4 && mob instanceof Mob m) {
                    m.getPathfinder().moveTo(owner.getLocation(), 1.2);
                }
            }
        }

        // Stops any follower from holding its owner or trusted players as a target, including sonic and vibration AI.
        private void tickFollowerTargets() {
            for (UUID fid : followers) {
                Entity f = Bukkit.getEntity(fid);
                if (!(f instanceof Mob m) || !m.isValid()) continue;
                UUID own = ownerOf(m);
                if (own == null) continue;
                LivingEntity t = m.getTarget();
                if (t instanceof Player tp && (tp.getUniqueId().equals(own) || isTrusted(own, tp.getUniqueId()))) {
                    m.setTarget(null);
                }
            }
        }

        private EntityType creakingType() {
            try {
                return EntityType.valueOf("CREAKING");
            } catch (IllegalArgumentException ex) {
                return null;
            }
        }

        private Entity spawnCreaking(Location loc, EntityType type, UUID owner, double scale, String tag) {
            Entity c = loc.getWorld().spawnEntity(loc, type);
            setOwner(c, owner);
            c.getPersistentDataContainer().set(ultKey, PersistentDataType.STRING, tag);
            if (c instanceof LivingEntity le) {
                AttributeInstance sc = le.getAttribute(Attribute.GENERIC_SCALE);
                if (sc != null) sc.setBaseValue(scale);
            }
            addFollower(c);
            return c;
        }

        private void burstFor(Player p, String id) {
            Color c = colorFor(id);
            for (Map.Entry<String, List<String>> en : GEM_ABILITIES.entrySet()) {
                if (en.getValue().contains(id) && gems.containsKey(en.getKey())) {
                    c = colorFor(en.getKey());
                    break;
                }
            }
            Location at = p.getLocation().add(0, 1, 0);
            p.getWorld().spawnParticle(Particle.DUST, at, 60, 0.8, 0.9, 0.8, 0, new Particle.DustOptions(c, 1.6f));
            p.getWorld().spawnParticle(Particle.END_ROD, at, 25, 0.8, 0.9, 0.8, 0.08);
            for (int deg = 0; deg < 360; deg += 30) {
                double r = Math.toRadians(deg);
                p.getWorld().spawnParticle(Particle.DUST, at.clone().add(Math.cos(r) * 1.5, -0.8, Math.sin(r) * 1.5),
                        2, 0, 0, 0, 0, new Particle.DustOptions(c, 1.2f));
            }
        }

        private String hexColor(String hex) {
            return net.md_5.bungee.api.ChatColor.of(hex).toString();
        }

        private String idOf(ItemStack it) {
            if (it == null || !it.hasItemMeta()) return null;
            PersistentDataContainer c = it.getItemMeta().getPersistentDataContainer();
            return c.get(itemKey, PersistentDataType.STRING);
        }

        // Bliss SMP style lore: grey description, gold activation line, bold headings, bullet passives.
        private List<String> wrapText(String text, int width) {
            List<String> lines = new ArrayList<>();
            StringBuilder cur = new StringBuilder();
            for (String word : text.split(" ")) {
                if (cur.length() > 0 && cur.length() + word.length() + 1 > width) {
                    lines.add(cur.toString());
                    cur.setLength(0);
                }
                if (cur.length() > 0) cur.append(' ');
                cur.append(word);
            }
            if (cur.length() > 0) lines.add(cur.toString());
            return lines;
        }

        private static final Map<String, String> ITEM_PASSIVES = Map.ofEntries(
                Map.entry("thunderstick", "Melee hits strike lightning and add 6 damage."),
                Map.entry("vampirefang", "Heals you for 40% of the melee damage you deal."),
                Map.entry("frostbite", "Melee hits slow the target and add 4 damage."),
                Map.entry("venom", "Melee hits poison normal mobs and players, and heal undead mobs."),
                Map.entry("reapersigil", "Offhand: lifesteal, you heal the damage you deal to players. Passive: Harvest, every kill heals you 4 hearts while it is in your offhand."),
                Map.entry("bloodstone", "Melee hits heal you for 10% of the damage dealt."),
                Map.entry("arccaster", "Each arrow stuns or freezes the target it hits."),
                Map.entry("unoreverse", "Saves every projectile that hits you while you hold it, up to 30."),
                Map.entry("winddash", "No fall damage from the dash."));

        private static final Set<String> BOWS = Set.of("kaboom", "shakalaka", "arccaster");

        private String keybindName(String label) {
            return switch (label) {
                case "Right-click" -> "Right-click";
                case "Shift+right-click" -> "Shift + Right-click";
                case "Sprint+F" -> "Sprint + F";
                case "Shift+F" -> "Shift + F";
                case "F" -> "F";
                default -> label;
            };
        }

        private List<String> loreFor(ItemDef d) {
            List<String> out = new ArrayList<>();
            if (gems.containsKey(d.id())) {
                String text = d.lore();
                java.util.regex.Matcher mt = java.util.regex.Pattern.compile(
                        "(Right-click|Shift\\+right-click|Sprint\\+F|Shift\\+F|F|Passives|The Light Before the Dark):").matcher(text);
                List<String[]> segs = new ArrayList<>();
                int last = -1;
                String lastLabel = null;
                while (mt.find()) {
                    if (lastLabel != null) segs.add(new String[]{lastLabel, text.substring(last, mt.start()).trim()});
                    lastLabel = mt.group(1);
                    last = mt.end();
                }
                if (lastLabel != null) segs.add(new String[]{lastLabel, text.substring(last).trim()});

                int abilityNo = 0;
                String ultKey = null;
                String ultText = null;
                for (String[] seg : segs) {
                    String label = seg[0];
                    if (label.equals("Passives") || label.equals("The Light Before the Dark")) continue;
                    if (seg[1].contains("ultimate")) {
                        ultKey = label;
                        ultText = seg[1];
                        continue;
                    }
                    abilityNo++;
                    out.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Ability " + abilityNo + " (" + keybindName(label) + ")");
                    for (String ln : wrapText(seg[1], 40)) out.add(ChatColor.GRAY + ln);
                    out.add("");
                }

                out.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Passive");
                for (String[] seg : segs) {
                    if (seg[0].equals("Passives")) {
                        for (String part : seg[1].split("\\. ")) {
                            String t = part.trim().replaceAll("\\.$", "");
                            if (!t.isEmpty()) out.add(ChatColor.GRAY + "- " + t);
                        }
                    } else if (seg[0].equals("The Light Before the Dark")) {
                        out.add(ChatColor.GRAY + "- The Light Before the Dark: " + seg[1].replaceAll("\\.$", ""));
                    }
                }
                out.add("");

                if (ultText != null) {
                    out.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Ult (" + keybindName(ultKey) + ")");
                    for (String ln : wrapText(ultText, 40)) out.add(ChatColor.GRAY + ln);
                }
                out.add("");
                out.add(ChatColor.YELLOW + "Works from your main or offhand. Cooldowns show above the hotbar.");
                return out;
            }
            if (d.id().equals("cursedcrown")) {
                out.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Ability 1 (Right-click)");
                for (String ln : wrapText("Summons 4 soldiers that follow you and fight whatever you attack. Up to 8 at once, for 2 minutes.", 40)) out.add(ChatColor.GRAY + ln);
                out.add("");
                out.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Ability 2 (Shift + Right-click)");
                for (String ln : wrapText("Rally: teleports all your soldiers to you and gives them Strength II.", 40)) out.add(ChatColor.GRAY + ln);
                out.add("");
                out.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Ability 3 (F)");
                for (String ln : wrapText("Curse of Iron: enemies within 8 blocks get Weakness II and Slowness II.", 40)) out.add(ChatColor.GRAY + ln);
                out.add("");
                out.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Passive");
                out.add(ChatColor.GRAY + "- Your soldiers deal 25% more damage.");
                out.add(ChatColor.GRAY + "- You take 10% more damage while holding or wearing the crown.");
                out.add(ChatColor.GRAY + "- Sprinting leaves a yellow trail.");
                out.add("");
                out.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Ult (Shift + F)");
                for (String ln : wrapText("Dark Legion: summons 8 wither skeleton soldiers for 3 minutes and gives you Resistance II.", 40)) out.add(ChatColor.GRAY + ln);
                out.add("");
                out.add(ChatColor.YELLOW + "Works from your main hand, offhand, or head.");
                return out;
            }
            String keybind = BOWS.contains(d.id()) ? "Shoot" : "Right-click";
            String desc = d.lore().replaceAll("\\s*[0-9]+-second cooldown\\.", "").trim();
            out.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Ability 1 (" + keybind + ")");
            for (String ln : wrapText(desc, 40)) out.add(ChatColor.GRAY + ln);
            out.add("");
            String passive = ITEM_PASSIVES.get(d.id());
            if (passive != null) {
                out.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Passive");
                for (String ln : wrapText(passive, 40)) out.add(ChatColor.GRAY + ln);
                out.add("");
            }
            out.add(ChatColor.YELLOW + "Works from your main hand or offhand.");
            return out;
        }

        private ItemStack make(ItemDef d) {
            ItemStack it = new ItemStack(d.mat());
            ItemMeta m = it.getItemMeta();
            m.setDisplayName(hexColor(d.hex()) + ChatColor.BOLD + d.name());
            Integer model = modelIds.get(d.id());
            if (model != null) m.setCustomModelData(model);
            m.setLore(loreFor(d));
            m.setEnchantmentGlintOverride(true);
            if (d.id().equals("cursedcrown")) {
                m.setUnbreakable(true);
                m.addEnchant(Enchantment.PROTECTION, 10, true);
                m.addEnchant(Enchantment.BINDING_CURSE, 1, true);
            }
            m.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, d.id());
            it.setItemMeta(m);
            return it;
        }

        private boolean ready(Player p, String id, long ms) {
            if (noCooldown.contains(p.getUniqueId()) || (id.startsWith("g_") && gemNoCd.contains(p.getUniqueId()))) return true;
            String path = "cooldowns." + id;
            if (!cfg.contains(path)) cfg.set(path, ms / 1000.0);
            long cdMs = Math.round(cfg.getDouble(path, ms / 1000.0) * 1000);
            long now = System.currentTimeMillis();
            String key = p.getUniqueId() + ":" + id;
            Long expiry = cooldowns.get(key);
            if (expiry != null && now < expiry) {
                dbg(p, id + " is on cooldown");
                return false;
            }
            cooldowns.put(key, now + cdMs);
            dbg(p, id + " fired (cooldown " + (cdMs / 1000) + "s)");
            String name = ABILITY_NAMES.getOrDefault(id, items.containsKey(id) ? items.get(id).name() : id);
            burstFor(p, id);
            p.playSound(p.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 0.6f, 1.4f);
            return true;
        }

        private long cdLeft(Player p, String id) {
            if (noCooldown.contains(p.getUniqueId())) return 0;
            Long expiry = cooldowns.get(p.getUniqueId() + ":" + id);
            if (expiry == null) return 0;
            return Math.max(0, expiry - System.currentTimeMillis());
        }

        private void guard(Player p, int ticks) {
            fallGuard.put(p.getUniqueId(), Math.max(fallGuard.getOrDefault(p.getUniqueId(), 0), ticks));
        }

        private void hit(LivingEntity le, double dmg, Entity src) {
            double out = dmg;
            if (src instanceof Player sp && gemOf(sp) != null) {
                out *= cfg.getDouble("damage.gem-multiplier", 1.5);
            }
            if (src != null) le.damage(out, src);
            else le.damage(out);
        }

        private void damageArea(Location c, double r, double dmg, Entity src) {
            for (Entity en : c.getWorld().getNearbyEntities(c, r, r, r)) {
                if (!(en instanceof LivingEntity le) || en.equals(src)) continue;
                if (le.getLocation().distance(c) > r) continue;
                hit(le, dmg, src);
            }
        }

        // Visual explosion that never breaks blocks and never damages the source.
        private void boom(Location c, double r, double dmg, Entity src) {
            World w = c.getWorld();
            w.spawnParticle(Particle.EXPLOSION, c, 2, 0.4, 0.4, 0.4, 0);
            w.spawnParticle(Particle.FLAME, c, 25, r / 2, 0.3, r / 2, 0.05);
            w.playSound(c, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
            damageArea(c, r, dmg, src);
        }

        private void knockArea(Location c, double r, double up, Entity src) {
            for (Entity en : c.getWorld().getNearbyEntities(c, r, r, r)) {
                if (!(en instanceof LivingEntity le) || en.equals(src)) continue;
                Vector out = le.getLocation().toVector().subtract(c.toVector()).setY(0);
                if (out.lengthSquared() == 0) out = new Vector(0, 0, 0.01);
                le.setVelocity(out.normalize().multiply(0.8).setY(up));
            }
        }

        private LivingEntity nearestEnemy(Location loc, double radius, Player exclude) {
            LivingEntity best = null;
            double bestDist = radius;
            for (Entity en : loc.getWorld().getNearbyEntities(loc, radius, radius, radius)) {
                if (!(en instanceof LivingEntity le) || en.equals(exclude)) continue;
                if (en instanceof Player tp && exclude != null
                        && (tp.getUniqueId().equals(exclude.getUniqueId()) || isTrusted(exclude.getUniqueId(), tp.getUniqueId()))) continue;
                UUID own = ownerOf(le);
                if (own != null && exclude != null && (own.equals(exclude.getUniqueId()) || isTrusted(own, exclude.getUniqueId()))) continue;
                double d = le.getLocation().distance(loc);
                if (d < bestDist) {
                    bestDist = d;
                    best = le;
                }
            }
            return best;
        }

        private boolean isClear(Location feet) {
            Block body = feet.getBlock();
            Block head = feet.clone().add(0, 1, 0).getBlock();
            Block below = feet.clone().add(0, -1, 0).getBlock();
            return body.isPassable() && head.isPassable() && below.getType().isSolid();
        }

        private Location toLoc(Vector v, World w) {
            return new Location(w, v.getX(), v.getY(), v.getZ());
        }

        // Eye-level ray hitting blocks, as a Location; falls back to max distance.
        private Location aimPoint(Player p, double dist) {
            Location eye = p.getEyeLocation();
            Vector dir = eye.getDirection().normalize();
            RayTraceResult r = p.getWorld().rayTraceBlocks(eye, dir, dist, FluidCollisionMode.NEVER, true);
            if (r != null && r.getHitPosition() != null) return toLoc(r.getHitPosition(), p.getWorld());
            return eye.clone().add(dir.multiply(dist));
        }

        // ---------- Right-click handling ----------

        @EventHandler
        public void onInteract(PlayerInteractEvent e) {
            try {
                onInteractInner(e);
            } catch (Throwable t) {
                host.getLogger().log(Level.SEVERE, "Click error", t);
                e.getPlayer().sendMessage(ChatColor.RED + "Click error: " + t);
            }
        }

        private void onInteractInner(PlayerInteractEvent e) {
            dbg(e.getPlayer(), "click action=" + e.getAction() + " hand=" + e.getHand() + " item=" + idOf(e.getItem()) + " block=" + (e.getClickedBlock() == null ? "none" : e.getClickedBlock().getType().name()));
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK
                    && (e.getHand() == null || e.getHand() == EquipmentSlot.HAND)
                    && e.getClickedBlock() != null
                    && shrines.containsKey(keyOf(e.getClickedBlock().getLocation()))) {
                e.setCancelled(true);
                e.getPlayer().openInventory(shrines.get(keyOf(e.getClickedBlock().getLocation())).inv);
                return;
            }
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null
                    && e.getClickedBlock().getType().isInteractable()) {
                return;
            }
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && idOf(e.getItem()) != null) {
                e.setUseItemInHand(Event.Result.DENY);
            }
            Action a = e.getAction();
            if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK) return;

            Player p = e.getPlayer();
            String id = idOf(e.getItem());
            if (id == null) return;

            // Bows must still be drawable with right-click
            if (id.equals("kaboom") || id.equals("shakalaka") || id.equals("arccaster")) return;

            e.setUseItemInHand(Event.Result.DENY);
            if (a == Action.RIGHT_CLICK_BLOCK) e.setUseInteractedBlock(Event.Result.DENY);

            if (gems.containsKey(id)) {
                handleGemRight(p, id);
                return;
            }

            World w = p.getWorld();
            Location eyeLoc = p.getEyeLocation();
            Vector look = eyeLoc.getDirection().normalize();

            switch (id) {
                case "thunderstick" -> {
                    if (!ready(p, id, 5000)) return;
                    Location t = aimPoint(p, 40);
                    w.strikeLightningEffect(t);
                    damageArea(t, 4, 12, p);
                }
                case "vampirefang" -> {
                    if (!ready(p, id, 20000)) return;
                    p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + 8));
                    w.spawnParticle(Particle.HEART, p.getLocation().add(0, 1, 0), 5, 0.4, 0.4, 0.4, 0);
                }
                case "frostbite" -> {
                    if (!ready(p, id, 8000)) return;
                    w.spawnParticle(Particle.SNOWFLAKE, p.getLocation(), 60, 4, 1, 4, 0.05);
                    w.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 1.4f);
                    for (Entity en : w.getNearbyEntities(p.getLocation(), 7, 3, 7)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p)) continue;
                        le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 3));
                        hit(le, 5, p);
                    }
                }
                case "winddash" -> {
                    p.setVelocity(look.clone().multiply(2.0));
                    guard(p, 100);
                    w.playSound(p.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1f, 1f);
                }
                case "infernowand" -> {
                    if (!ready(p, id, 1000)) return;
                    Fireball f = p.launchProjectile(Fireball.class);
                    f.setYield(0f); // no block damage
                    f.setIsIncendiary(true);
                }
                case "groundbreaker" -> {
                    if (!ready(p, id, 4000)) return;
                    Location loc = p.getLocation();
                    w.spawnParticle(Particle.CLOUD, loc, 40, 2, 0.2, 2, 0.05);
                    w.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1.2f);
                    damageArea(loc, 6, 18, p);
                    knockArea(loc, 6, 0.5, p);
                }
                case "phoenixblade" -> {
                    if (!ready(p, id, 6000)) return;
                    p.setVelocity(new Vector(0, 1.4, 0));
                    p.setFallDistance(0f);
                    phoenix.put(p.getUniqueId(), new PhoenixState());
                    w.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1f, 1.2f);
                }
                case "grenade" -> {
                    if (!ready(p, id, 1000)) return;
                    Snowball s = p.launchProjectile(Snowball.class);
                    s.getPersistentDataContainer().set(projKey, PersistentDataType.STRING, "grenade");
                }
                case "shadowdagger" -> {
                    if (!ready(p, id, 8000)) return;
                    LivingEntity target = nearestEnemy(p.getLocation(), 12, p);
                    if (target == null) {
                        p.sendMessage(ChatColor.RED + "No enemy within 12 blocks.");
                        return;
                    }
                    Location from = p.getLocation().clone();
                    Location behind = target.getLocation().clone().add(target.getLocation().getDirection().setY(0).normalize().multiply(-1.5));
                    behind.setDirection(target.getLocation().toVector().subtract(behind.toVector()));
                    p.teleport(behind);
                    hit(target, 10, p);
                    for (int k = 0; k <= 8; k++) {
                        Location tp = from.clone().add(behind.clone().toVector().subtract(from.toVector()).multiply(k / 8.0));
                        w.spawnParticle(Particle.DUST, tp.add(0, 1, 0), 3, 0.1, 0.2, 0.1, 0, new Particle.DustOptions(colorFor(id), 1.2f));
                    }
                    w.playSound(behind, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1.4f);
                }
                case "solarlance" -> {
                    if (!ready(p, id, 4000)) return;
                    Set<UUID> beamHit = new HashSet<>();
                    for (double d = 1; d <= 25; d += 0.5) {
                        Location pt = eyeLoc.clone().add(look.clone().multiply(d));
                        w.spawnParticle(Particle.DUST, pt, 2, 0.05, 0.05, 0.05, 0, new Particle.DustOptions(colorFor(id), 1.5f));
                        for (Entity en : w.getNearbyEntities(pt, 1, 1, 1)) {
                            if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le) || beamHit.contains(le.getUniqueId())) continue;
                            beamHit.add(le.getUniqueId());
                            hit(le, 30, p);
                        }
                    }
                    w.playSound(eyeLoc, Sound.ENTITY_BLAZE_SHOOT, 1.5f, 1.8f);
                }
                case "gravitywell" -> {
                    if (!ready(p, id, 20000)) return;
                    Location center = aimPoint(p, 20);
                    final int[] runs = {0};
                    final BukkitTask[] holder = new BukkitTask[1];
                    holder[0] = Bukkit.getScheduler().runTaskTimer(host, () -> {
                        runs[0]++;
                        if (runs[0] > 12 || !p.isOnline()) {
                            holder[0].cancel();
                            return;
                        }
                        for (Entity en : w.getNearbyEntities(center, 6, 6, 6)) {
                            if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                            Vector pull = center.toVector().subtract(le.getLocation().toVector());
                            if (pull.lengthSquared() > 0) le.setVelocity(pull.normalize().multiply(0.6));
                            if (runs[0] % 2 == 0) hit(le, 2, p);
                        }
                        particleRing(center.clone().add(0, 0.2, 0), 2 + (runs[0] % 4), Particle.DRAGON_BREATH);
                    }, 0L, 10L);
                    w.playSound(center, Sound.BLOCK_PORTAL_TRAVEL, 1f, 0.8f);
                }
                case "aegistotem" -> {
                    if (!ready(p, id, 40000)) return;
                    final int[] runs = {0};
                    final BukkitTask[] holder = new BukkitTask[1];
                    holder[0] = Bukkit.getScheduler().runTaskTimer(host, () -> {
                        runs[0]++;
                        if (runs[0] > 15 || !p.isOnline()) {
                            holder[0].cancel();
                            return;
                        }
                        for (Entity en : p.getWorld().getNearbyEntities(p.getLocation(), 8, 4, 8)) {
                            if (!(en instanceof Player ally)) continue;
                            if (!ally.equals(p) && !isTrusted(p.getUniqueId(), ally.getUniqueId())) continue;
                            ally.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 40, 0));
                            ally.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 0));
                        }
                        particleRing(p.getLocation().add(0, 0.2, 0), 3, Particle.TOTEM_OF_UNDYING);
                    }, 0L, 20L);
                    w.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1f, 1f);
                }
                case "unoreverse" -> releaseProjectile(p);
                case "chronohourglass" -> {
                    if (!ready(p, id, 30000)) return;
                    int frozen = 0;
                    for (Entity en : w.getNearbyEntities(p.getLocation(), 10, 10, 10)) {
                        if (en.equals(p)) continue;
                        UUID own = ownerOf(en);
                        if (own != null && (own.equals(p.getUniqueId()) || isTrusted(p.getUniqueId(), own))) continue;
                        if (en instanceof Player pl && isTrusted(p.getUniqueId(), pl.getUniqueId())) continue;
                        if (en instanceof Mob m) {
                            m.setAI(false);
                            Bukkit.getScheduler().runTaskLater(host, () -> {
                                if (m.isValid()) m.setAI(true);
                            }, 80L);
                            frozen++;
                        } else if (en instanceof LivingEntity le) {
                            le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 80, 9));
                            frozen++;
                        }
                    }
                    particleRing(p.getLocation().add(0, 0.2, 0), 10, Particle.END_ROD);
                    w.playSound(p.getLocation(), Sound.BLOCK_BELL_RESONATE, 2f, 0.5f);
                    p.sendMessage(ChatColor.GOLD + "Time stops. " + frozen + " frozen.");
                }
                case "bloodstone" -> {
                    if (!ready(p, id, 8000)) return;
                    Location c = p.getLocation();
                    for (Entity en : w.getNearbyEntities(c, 5, 3, 5)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                        hit(le, 8, p);
                        le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 1));
                    }
                    particleRing(c.clone().add(0, 0.2, 0), 5, Particle.DUST);
                    w.playSound(c, Sound.ENTITY_PLAYER_ATTACK_CRIT, 2f, 0.6f);
                }
                case "quakehammer" -> {
                    if (!ready(p, id, 12000)) return;
                    Location c = p.getLocation();
                    w.spawnParticle(Particle.CLOUD, c, 80, 4, 0.3, 4, 0.1);
                    w.playSound(c, Sound.ENTITY_GENERIC_EXPLODE, 2f, 0.6f);
                    for (Entity en : w.getNearbyEntities(c, 6, 3, 6)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                        hit(le, 18, p);
                        le.setVelocity(new Vector(0, 0.9, 0));
                    }
                    for (double r = 2; r <= 6; r += 2) particleRing(c.clone().add(0, 0.2, 0), r, Particle.CLOUD);
                }
                case "venomsickle" -> {
                    if (!ready(p, id, 15000)) return;
                    final Location cloud = p.getLocation();
                    final int[] runs = {0};
                    final BukkitTask[] holder = new BukkitTask[1];
                    holder[0] = Bukkit.getScheduler().runTaskTimer(host, () -> {
                        runs[0]++;
                        if (runs[0] > 8) {
                            holder[0].cancel();
                            return;
                        }
                        particleRing(cloud.clone().add(0, 0.2, 0), 4, Particle.COMPOSTER);
                        for (Entity en : cloud.getWorld().getNearbyEntities(cloud, 4, 3, 4)) {
                            if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                            venomTouch(le);
                            hit(le, 2, p);
                        }
                    }, 0L, 20L);
                }
                case "crystalpick" -> {
                    if (!ready(p, id, 6000)) return;
                    Vector shardDir = eyeLoc.getDirection().normalize();
                    for (int i = -2; i <= 2; i++) {
                        Vector v = shardDir.clone().rotateAroundY(Math.toRadians(i * 5)).multiply(1.8);
                        Snowball shard = p.launchProjectile(Snowball.class, v);
                        shard.getPersistentDataContainer().set(projKey, PersistentDataType.STRING, "crystal");
                    }
                    w.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.5f, 1f);
                }
                case "rewindcompass" -> {
                    Deque<Location> q = posHistory.get(p.getUniqueId());
                    if (q == null || q.isEmpty()) {
                        p.sendMessage(ChatColor.RED + "No history yet. Wait a few seconds.");
                        return;
                    }
                    if (!ready(p, id, 30000)) return;
                    Location dest = q.peekFirst().clone();
                    dest.setYaw(p.getLocation().getYaw());
                    dest.setPitch(p.getLocation().getPitch());
                    w.spawnParticle(Particle.REVERSE_PORTAL, p.getLocation().add(0, 1, 0), 60, 0.4, 0.8, 0.4, 0.1);
                    p.teleport(dest);
                    w.spawnParticle(Particle.REVERSE_PORTAL, dest.clone().add(0, 1, 0), 60, 0.4, 0.8, 0.4, 0.1);
                    w.playSound(dest, Sound.BLOCK_PORTAL_TRAVEL, 1f, 1.5f);
                }
                case "spoutshell" -> {
                    if (!ready(p, id, 12000)) return;
                    Location aim = aimPoint(p, 15);
                    for (int k = 0; k < 15; k++) {
                        final int step = k;
                        Bukkit.getScheduler().runTaskLater(host, () ->
                                w.spawnParticle(Particle.SPLASH, aim.clone().add(0, step * 0.6, 0), 12, 0.3, 0.2, 0.3, 0.05), k * 2L);
                    }
                    for (Entity en : w.getNearbyEntities(aim, 3, 4, 3)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                        hit(le, 8, p);
                        le.setVelocity(new Vector(0, 2.5, 0));
                    }
                    w.playSound(aim, Sound.ENTITY_GENERIC_SPLASH, 2f, 0.8f);
                }
                case "honeybomb" -> {
                    if (!ready(p, id, 6000)) return;
                    Snowball bomb = p.launchProjectile(Snowball.class);
                    bomb.getPersistentDataContainer().set(projKey, PersistentDataType.STRING, "honey");
                    w.playSound(p.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1f, 0.5f);
                }
                case "cloudbottle" -> {
                    if (!ready(p, id, 20000)) return;
                    p.setVelocity(new Vector(0, 1.7, 0));
                    p.setFallDistance(0f);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 200, 0));
                    guard(p, 200);
                    w.spawnParticle(Particle.CLOUD, p.getLocation(), 60, 0.8, 0.2, 0.8, 0.05);
                    w.playSound(p.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1f, 1.4f);
                }
                case "meteor" -> {
                    if (!ready(p, id, 90000)) return;
                    Location aim = aimPoint(p, 40);
                    int count = 10 + rnd.nextInt(11);
                    int spawned = 0;
                    for (int i = 0; i < count; i++) {
                        Location spot = aim.clone().add(rand(-12, 12), 0, rand(-12, 12));
                        try {
                            DragonFireball fb = w.spawn(spot.clone().add(0, 25, 0), DragonFireball.class);
                            fb.setShooter(p);
                            fb.setVelocity(new Vector(0, -1.4, 0));
                            meteorOwners.put(p.getUniqueId(), System.currentTimeMillis());
                            spawned++;
                        } catch (Exception ex) {
                            p.sendMessage(ChatColor.RED + "Meteor could not spawn a fireball: " + ex.getClass().getSimpleName());
                            break;
                        }
                    }
                    w.playSound(aim, Sound.ENTITY_ENDER_DRAGON_SHOOT, 2f, 1f);
                    p.sendMessage(ChatColor.LIGHT_PURPLE + "Meteor: " + spawned + " dragon fireballs incoming.");
                }
                case "hexbone" -> {
                    if (!ready(p, id, 25000)) return;
                    Location c = p.getLocation();
                    w.playSound(c, Sound.AMBIENT_CAVE, 2f, 0.7f);
                    final int[] runs = {0};
                    final BukkitTask[] holder = new BukkitTask[1];
                    holder[0] = Bukkit.getScheduler().runTaskTimer(host, () -> {
                        runs[0]++;
                        if (runs[0] > 15 || !p.isOnline()) {
                            holder[0].cancel();
                            return;
                        }
                        for (Entity en : c.getWorld().getNearbyEntities(c, 6, 3, 6)) {
                            if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                            hit(le, 3, p);
                            le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 30, 1));
                        }
                        particleRing(c.clone().add(0, 0.2, 0), 6, Particle.SOUL);
                    }, 0L, 20L);
                }
                case "cursedcoin" -> {
                    if (!ready(p, id, 15000)) return;
                    Location c = p.getLocation();
                    int flip = rnd.nextInt(3);
                    if (flip == 0) {
                        p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + 6));
                        p.sendMessage(ChatColor.GREEN + "The coin lands heads. You are healed.");
                    } else if (flip == 1) {
                        for (Entity en : w.getNearbyEntities(c, 8, 4, 8)) {
                            if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                            le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
                        }
                        p.sendMessage(ChatColor.DARK_GRAY + "The coin lands tails. Nearby enemies are blinded.");
                    } else {
                        for (Entity en : w.getNearbyEntities(c, 8, 4, 8)) {
                            if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                            le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 1));
                        }
                        p.sendMessage(ChatColor.DARK_PURPLE + "The coin wails. Nearby enemies are slowed.");
                    }
                    w.playSound(c, Sound.ENTITY_GHAST_SCREAM, 1.5f, 0.6f);
                }
                case "spectralquill" -> {
                    LivingEntity target = nearestEnemy(p.getLocation(), 15, p);
                    if (target == null) {
                        p.sendMessage(ChatColor.RED + "No enemy within 15 blocks.");
                        return;
                    }
                    if (!ready(p, id, 12000)) return;
                    hit(target, 12, p);
                    target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 120, 0));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 120, 0));
                    target.getWorld().spawnParticle(Particle.SOUL, target.getLocation().add(0, 1, 0), 30, 0.4, 0.6, 0.4, 0.02);
                    w.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_STARE, 1.5f, 0.5f);
                }
                case "rottenoffering" -> {
                    if (!ready(p, id, 60000)) return;
                    for (int i = 0; i < 3; i++) {
                        Zombie z = w.spawn(p.getLocation().add(rand(-2, 2), 0, rand(-2, 2)), Zombie.class);
                        setOwner(z, p.getUniqueId());
                        z.getPersistentDataContainer().set(ultKey, PersistentDataType.STRING, "servant");
                        addFollower(z);
                        Bukkit.getScheduler().runTaskLater(host, () -> {
                            if (z.isValid()) z.remove();
                        }, 1200L);
                    }
                    w.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 2f, 0.6f);
                }
                case "eyeofthecrypt" -> {
                    if (!ready(p, id, 20000)) return;
                    Location c = p.getLocation();
                    int revealed = 0;
                    for (Entity en : w.getNearbyEntities(c, 20, 20, 20)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p)) continue;
                        if (!le.isInvisible() && !le.hasPotionEffect(PotionEffectType.INVISIBILITY)) continue;
                        le.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 160, 0));
                        revealed++;
                    }
                    w.playSound(c, Sound.AMBIENT_CAVE, 2f, 1f);
                    p.sendMessage(ChatColor.DARK_PURPLE + "Eye of the Crypt revealed " + revealed + " hidden creature(s).");
                }
                case "tidal" -> {
                    if (!ready(p, id, 4000)) return;
                    Snowball orb = p.launchProjectile(Snowball.class);
                    orb.getPersistentDataContainer().set(projKey, PersistentDataType.STRING, "tidal");
                    w.playSound(p.getLocation(), Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED, 1f, 0.8f);
                }
                case "reapersigil" -> {
                    if (p.isSneaking()) {
                        soulDrain(p);
                        return;
                    }
                    if (p.isSprinting()) {
                        deathMark(p);
                        return;
                    }
                    if (!ready(p, id, 6000)) return;
                    WitherSkull skull = p.launchProjectile(WitherSkull.class);
                    if (skull instanceof Fireball fb) fb.setYield(0f);
                    skull.getPersistentDataContainer().set(projKey, PersistentDataType.STRING, "reaperskull");
                    w.playSound(p.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1f, 1.2f);
                }
                case "emberblade" -> {
                    Vector fwd = p.getLocation().getDirection().setY(0);
                    if (fwd.lengthSquared() < 0.01) return;
                    fwd = fwd.normalize();
                    if (!ready(p, id, 5000)) return;
                    for (Entity en : p.getWorld().getNearbyEntities(p.getLocation(), 6, 3, 6)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                        Vector to = le.getLocation().toVector().subtract(p.getLocation().toVector()).setY(0);
                        if (to.lengthSquared() == 0 || to.clone().normalize().dot(fwd) < 0.5) continue;
                        hit(le, 8, p);
                        le.setFireTicks(80);
                    }
                    p.getWorld().spawnParticle(Particle.FLAME, p.getLocation().add(fwd.clone().multiply(2)).add(0, 1, 0), 30, 1.5, 0.5, 1.5, 0.05);
                    p.getWorld().playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1f, 1f);
                }
                case "thornrapier" -> {
                    if (!ready(p, id, 6000)) return;
                    final Player lp = p;
                    Vector dir = p.getLocation().getDirection().normalize();
                    p.setVelocity(dir.multiply(1.2).setY(0.1));
                    Bukkit.getScheduler().runTaskLater(host, () -> {
                        for (Entity en : lp.getWorld().getNearbyEntities(lp.getLocation(), 2.5, 2.5, 2.5)) {
                            if (!(en instanceof LivingEntity le) || en.equals(lp) || isFriendlyTo(lp, le)) continue;
                            hit(le, 10, lp);
                            le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
                            break;
                        }
                    }, 6L);
                }
                case "moonsplitter" -> {
                    if (!ready(p, id, 7000)) return;
                    for (Entity en : p.getWorld().getNearbyEntities(p.getLocation(), 5, 3, 5)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                        hit(le, 9, p);
                        Vector out = le.getLocation().toVector().subtract(p.getLocation().toVector()).setY(0.4);
                        if (out.lengthSquared() > 0) le.setVelocity(out.normalize().multiply(0.9));
                    }
                    p.getWorld().spawnParticle(Particle.SWEEP_ATTACK, p.getLocation().add(0, 1, 0), 20, 3, 0.5, 3, 0);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.8f);
                }
                case "frostfang" -> {
                    Vector fwd = p.getLocation().getDirection().setY(0);
                    if (fwd.lengthSquared() < 0.01) return;
                    fwd = fwd.normalize();
                    if (!ready(p, id, 9000)) return;
                    for (Entity en : p.getWorld().getNearbyEntities(p.getLocation(), 8, 3, 8)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                        Vector to = le.getLocation().toVector().subtract(p.getLocation().toVector()).setY(0);
                        if (to.lengthSquared() == 0 || to.clone().normalize().dot(fwd) < 0.6) continue;
                        hit(le, 6, p);
                        le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 2));
                    }
                    p.getWorld().spawnParticle(Particle.SNOWFLAKE, p.getLocation().add(fwd.clone().multiply(4)).add(0, 1, 0), 60, 2.5, 0.8, 2.5, 0.05);
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 0.6f);
                }
                case "voidcutter" -> {
                    if (!ready(p, id, 8000)) return;
                    Vector fwd = p.getLocation().getDirection().normalize();
                    Location start = p.getLocation().clone();
                    Location dest = start.clone().add(fwd.clone().multiply(6));
                    if (!isClear(dest)) dest = start;
                    p.getWorld().spawnParticle(Particle.PORTAL, start.clone().add(0, 1, 0), 40, 0.5, 1, 0.5, 0.5);
                    p.teleport(dest);
                    for (Entity en : p.getWorld().getNearbyEntities(dest, 2.5, 2.5, 2.5)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                        hit(le, 10, p);
                    }
                    p.getWorld().playSound(dest, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.8f);
                }
                case "bloodletter" -> {
                    if (p.getHealth() <= 6) {
                        p.sendMessage(ChatColor.RED + "You need more than 3 hearts to use Bloodletter.");
                        return;
                    }
                    if (!ready(p, id, 10000)) return;
                    p.setHealth(p.getHealth() - 4);
                    for (Entity en : p.getWorld().getNearbyEntities(p.getLocation(), 3, 2, 3)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                        hit(le, 12, p);
                    }
                    p.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, p.getLocation().add(0, 1, 0), 20, 1, 0.8, 1, 0.1);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT, 1f, 0.7f);
                }
                case "sunder" -> {
                    if (!ready(p, id, 6000)) return;
                    Location c = p.getLocation().clone();
                    for (Entity en : p.getWorld().getNearbyEntities(c, 4, 2, 4)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                        hit(le, 7, p);
                        Vector out = le.getLocation().toVector().subtract(c.toVector()).setY(0.6);
                        if (out.lengthSquared() > 0) le.setVelocity(out.normalize().multiply(0.7).setY(0.6));
                    }
                    p.getWorld().spawnParticle(Particle.EXPLOSION, c, 3, 1, 0.2, 1, 0);
                    p.getWorld().playSound(c, Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.7f);
                }
                case "soulreaver" -> {
                    if (!ready(p, id, 12000)) return;
                    int drained = 0;
                    for (Entity en : p.getWorld().getNearbyEntities(p.getLocation(), 4, 3, 4)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                        hit(le, 12, p);
                        drained++;
                    }
                    if (drained > 0) p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + Math.min(12, drained * 6)));
                    p.getWorld().spawnParticle(Particle.SOUL, p.getLocation().add(0, 1, 0), 40, 2, 1, 2, 0.05);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1f, 1.2f);
                }
                case "stormfang" -> {
                    if (!ready(p, id, 9000)) return;
                    java.util.List<LivingEntity> foes = new java.util.ArrayList<>();
                    for (Entity en : p.getWorld().getNearbyEntities(p.getLocation(), 8, 4, 8)) {
                        if (en instanceof LivingEntity le && !en.equals(p) && !isFriendlyTo(p, le)) foes.add(le);
                    }
                    foes.sort(java.util.Comparator.comparingDouble(le -> le.getLocation().distanceSquared(p.getLocation())));
                    for (int i = 0; i < Math.min(3, foes.size()); i++) {
                        LivingEntity target = foes.get(i);
                        target.getWorld().strikeLightningEffect(target.getLocation());
                        hit(target, 6, p);
                    }
                }
                case "venomtooth" -> {
                    if (!ready(p, id, 14000)) return;
                    final Player vp = p;
                    final Location cloud = p.getLocation().clone();
                    for (int t = 0; t < 6; t++) {
                        Bukkit.getScheduler().runTaskLater(host, () -> {
                            for (Entity en : cloud.getWorld().getNearbyEntities(cloud, 3, 2, 3)) {
                                if (!(en instanceof LivingEntity le) || en.equals(vp) || isFriendlyTo(vp, le)) continue;
                                le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 0));
                                hit(le, 1, vp);
                            }
                            cloud.getWorld().spawnParticle(Particle.SPORE_BLOSSOM_AIR, cloud.clone().add(0, 0.5, 0), 25, 2.5, 0.4, 2.5, 0.01);
                        }, t * 20L);
                    }
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITCH_AMBIENT, 1f, 0.8f);
                }
                case "bladestorm" -> {
                    if (!ready(p, id, 8000)) return;
                    final Player bp = p;
                    for (int pulse = 0; pulse < 2; pulse++) {
                        Bukkit.getScheduler().runTaskLater(host, () -> {
                            for (Entity en : bp.getWorld().getNearbyEntities(bp.getLocation(), 3, 2, 3)) {
                                if (!(en instanceof LivingEntity le) || en.equals(bp) || isFriendlyTo(bp, le)) continue;
                                hit(le, 5, bp);
                            }
                            bp.getWorld().spawnParticle(Particle.SWEEP_ATTACK, bp.getLocation().add(0, 1, 0), 15, 2, 0.5, 2, 0);
                        }, pulse * 10L);
                    }
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.9f);
                }
                case "arccaster" -> {
                    if (!ready(p, id, 6000)) return;
                    List<LivingEntity> chain = new ArrayList<>();
                    LivingEntity cur = nearestEnemy(p.getLocation(), 12, p);
                    while (cur != null && chain.size() < 4) {
                        chain.add(cur);
                        cur = nextChain(cur, chain);
                    }
                    for (int i = 0; i < chain.size(); i++) {
                        final LivingEntity target = chain.get(i);
                        final Location from = (i == 0) ? p.getEyeLocation() : chain.get(i - 1).getLocation().add(0, 1, 0);
                        Bukkit.getScheduler().runTaskLater(host, () -> {
                            if (!target.isValid()) return;
                            Vector span = target.getLocation().add(0, 1, 0).toVector().subtract(from.toVector());
                            for (int s = 1; s <= 6; s++) {
                                Location pt = from.clone().add(span.clone().multiply(s / 6.0));
                                w.spawnParticle(Particle.CRIT, pt, 3, 0.1, 0.1, 0.1, 0);
                            }
                            w.strikeLightningEffect(target.getLocation());
                            hit(target, 10, p);
                        }, i * 4L);
                    }
                }
                case "gauntlet" -> {
                    if (holds.containsKey(p.getUniqueId())) {
                        release(p, true);
                        return;
                    }
                    RayTraceResult ent = w.rayTraceEntities(eyeLoc, look, 12,
                            en -> en instanceof LivingEntity && !en.equals(p));
                    if (ent == null || !(ent.getHitEntity() instanceof LivingEntity le)) {
                        p.sendMessage(ChatColor.RED + "No mob in range.");
                        return;
                    }
                    grab(p, le);
                }
                case "jetpack" -> {
                    if (jetFuel.remove(p.getUniqueId()) != null) {
                        p.sendMessage(ChatColor.YELLOW + "Jetpack off.");
                    } else {
                        jetFuel.put(p.getUniqueId(), 12000);
                        p.sendMessage(ChatColor.GREEN + "Jetpack on.");
                    }
                }
                case "magnetrod" -> {
                    if (!ready(p, id, 3000)) return;
                    for (Entity en : w.getNearbyEntities(p.getLocation(), 12, 5, 12)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p)) continue;
                        Vector pull = p.getLocation().toVector().subtract(le.getLocation().toVector());
                        if (pull.lengthSquared() > 0) le.setVelocity(pull.normalize().multiply(1.8));
                    }
                }
                case "blizzardorb" -> {
                    if (!ready(p, id, 12000)) return;
                    Location c = p.getLocation();
                    w.spawnParticle(Particle.SNOWFLAKE, c, 120, 5, 1.5, 5, 0.05);
                    w.playSound(c, Sound.BLOCK_GLASS_BREAK, 1.5f, 0.6f);
                    for (Entity en : w.getNearbyEntities(c, 9, 4, 9)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                        le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 120, 9));
                        le.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 120, 2));
                        hit(le, 4, p);
                    }
                    particleRing(c.clone().add(0, 0.2, 0), 9, Particle.SNOWFLAKE);
                }
                case "healingcharm" -> {
                    if (!ready(p, id, 60000)) return;
                    p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + 16));
                    cleanse(p);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 2));
                    w.spawnParticle(Particle.HEART, p.getLocation().add(0, 1, 0), 8, 0.5, 0.5, 0.5, 0);
                }
                case "shadowcloak" -> {
                    if (!ready(p, id, 60000)) return;
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 400, 0));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 1));
                    p.sendMessage(ChatColor.DARK_GRAY + "Shadow Cloak: 20 seconds of invisibility.");
                }
                case "golembell" -> {
                    if (!ready(p, id, 120000)) return;
                    UUID old = golems.remove(p.getUniqueId());
                    if (old != null) {
                        Entity oldGolem = Bukkit.getEntity(old);
                        if (oldGolem != null) oldGolem.remove();
                    }
                    IronGolem g = w.spawn(p.getLocation(), IronGolem.class, ig -> ig.setPlayerCreated(true));
                    setOwner(g, p.getUniqueId());
                    golems.put(p.getUniqueId(), g.getUniqueId());
                    Bukkit.getScheduler().runTaskLater(host, () -> {
                        if (g.isValid()) g.remove();
                        golems.remove(p.getUniqueId(), g.getUniqueId());
                    }, 1800L);
                    w.playSound(p.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0, 2f, 0.8f);
                    p.sendMessage(ChatColor.GREEN + "Golem summoned. Hit something to send it in.");
                }
                case "sonichorn" -> {
                    if (!ready(p, id, 10000)) return;
                    w.playSound(p.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 3f, 1f);
                    final Vector fwd = look.clone();
                    final Location origin = eyeLoc.clone();
                    final Set<UUID> hitIds = new HashSet<>();
                    for (int step = 1; step <= 20; step++) {
                        final int s = step;
                        Bukkit.getScheduler().runTaskLater(host, () -> {
                            Location front = origin.clone().add(fwd.clone().multiply(s * 1.2));
                            w.spawnParticle(Particle.SONIC_BOOM, front, 1, 0, 0, 0, 0);
                            for (Entity en : w.getNearbyEntities(front, 1.8, 1.8, 1.8)) {
                                if (!(en instanceof LivingEntity hitMob) || en.equals(p) || hitIds.contains(en.getUniqueId())) continue;
                                hitIds.add(en.getUniqueId());
                                hit(hitMob, 150, p);
                                hitMob.setVelocity(fwd.clone().multiply(2.4).setY(0.6));
                            }
                        }, step);
                    }
                }
                case "stormheart" -> {
                    if (!ready(p, id, 20000)) return;
                    List<LivingEntity> targets = new ArrayList<>();
                    for (Entity en : w.getNearbyEntities(p.getLocation(), 25, 25, 25)) {
                        if (en instanceof LivingEntity le && !(en instanceof Player)) targets.add(le);
                    }
                    Collections.shuffle(targets, rnd);
                    for (int i = 0; i < Math.min(5, targets.size()); i++) {
                        Location tl = targets.get(i).getLocation();
                        w.strikeLightningEffect(tl);
                        hit(targets.get(i), 20, p);
                    }
                }
                case "phasefruit" -> {
                    if (!ready(p, id, 3000)) return;
                    Vector flat = eyeLoc.getDirection().setY(0);
                    if (flat.lengthSquared() == 0) return;
                    flat.normalize();
                    Location chest = p.getLocation().add(0, 1, 0);
                    RayTraceResult r = w.rayTraceBlocks(chest, flat, 12, FluidCollisionMode.NEVER, true);
                    double maxD = (r != null && r.getHitPosition() != null)
                            ? chest.toVector().distance(r.getHitPosition()) - 0.7
                            : 12;
                    Location dest = null;
                    for (double d = maxD; d >= 0.5; d -= 0.5) {
                        Location cand = p.getLocation().add(flat.clone().multiply(d));
                        if (isClear(cand)) {
                            dest = cand;
                            break;
                        }
                    }
                    if (dest == null) {
                        p.sendMessage(ChatColor.RED + "No safe spot to blink to.");
                        return;
                    }
                    dest.setYaw(p.getLocation().getYaw());
                    dest.setPitch(p.getLocation().getPitch());
                    w.spawnParticle(Particle.PORTAL, p.getLocation().add(0, 1, 0), 30, 0.3, 0.6, 0.3, 0.1);
                    p.teleport(dest);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1));
                    guard(p, 40);
                    w.spawnParticle(Particle.PORTAL, dest.clone().add(0, 1, 0), 30, 0.3, 0.6, 0.3, 0.1);
                }
                case "voidscythe" -> {
                    if (!ready(p, id, 1000)) return;
                    Vector fwd = p.getLocation().getDirection().setY(0);
                    if (fwd.lengthSquared() == 0) fwd = new Vector(0, 0, 1);
                    final Vector f = fwd.normalize();
                    final Location origin = p.getLocation().clone();
                    int hits = 0;
                    for (Entity en : w.getNearbyEntities(origin, 8, 4, 8)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                        Vector to = le.getLocation().toVector().subtract(origin.toVector()).setY(0);
                        if (to.lengthSquared() == 0) continue;
                        if (to.normalize().dot(f) > 0.2) {
                            le.damage(20, p);
                            le.setVelocity(f.clone().multiply(0.6).setY(0.3));
                            hits++;
                        }
                    }
                    for (int k = 0; k < 6; k++) {
                        final int step = k;
                        Bukkit.getScheduler().runTaskLater(host, () -> {
                            double ang = Math.toRadians(-80 + step * 32);
                            Vector dir = f.clone().rotateAroundY(ang);
                            for (double r = 2; r <= 8; r += 1.0) {
                                w.spawnParticle(Particle.DRAGON_BREATH, origin.clone().add(0, 1, 0).add(dir.clone().multiply(r)), 3, 0.05, 0.05, 0.05, 0.01);
                            }
                        }, k);
                    }
                    w.playSound(origin, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.5f, 0.8f);
                    p.sendMessage(ChatColor.DARK_PURPLE + "Void Slasher hit " + hits + " target(s).");
                }
                case "grapple" -> {
                    if (!ready(p, id, 1500)) return;
                    RayTraceResult blk = w.rayTraceBlocks(eyeLoc, look, 35, FluidCollisionMode.NEVER, true);
                    RayTraceResult ent = w.rayTraceEntities(eyeLoc, look, 35);
                    Vector target = null;
                    if (ent != null && ent.getHitEntity() != null && !ent.getHitEntity().equals(p) && ent.getHitPosition() != null) {
                        target = ent.getHitPosition();
                    } else if (blk != null && blk.getHitPosition() != null) {
                        target = blk.getHitPosition();
                    }
                    if (target == null) {
                        p.sendMessage(ChatColor.RED + "Nothing to hook.");
                        return;
                    }
                    Location start = eyeLoc.clone().add(look.clone().multiply(0.5));
                    Location end = toLoc(target, w);
                    // Animated rope: 8 frames extending from the hand to the target.
                    for (int f = 1; f <= 8; f++) {
                        final double frac = f / 8.0;
                        Bukkit.getScheduler().runTaskLater(host, () -> {
                            Location pt = start.clone().add(end.clone().subtract(start).multiply(frac));
                            w.spawnParticle(Particle.CRIT, pt, 3, 0.05, 0.05, 0.05, 0);
                        }, f * 1L);
                    }
                    Vector pull = target.clone().subtract(p.getLocation().toVector());
                    double dist = pull.length();
                    if (dist < 1.5) return;
                    p.setVelocity(pull.normalize().multiply(Math.min(2.6, dist * 0.3)).setY(0.6));
                    guard(p, 100);
                    w.playSound(p.getLocation(), Sound.ENTITY_FISHING_BOBBER_THROW, 1f, 1f);
                }
                case "starfall" -> {
                    if (!ready(p, id, 8000)) return;
                    Location center = aimPoint(p, 20);
                    for (int i = 0; i < 5; i++) {
                        final Location strike = center.clone().add(rand(-3, 3), 0, rand(-3, 3));
                        long base = i * 6L;
                        // Falling star streak
                        for (int k = 0; k < 12; k++) {
                            final int step = k;
                            Bukkit.getScheduler().runTaskLater(host, () ->
                                    w.spawnParticle(Particle.END_ROD, strike.clone().add(0, 12 - step * 1.1, 0), 4, 0.1, 0.1, 0.1, 0.02),
                                    base + k);
                        }
                        Bukkit.getScheduler().runTaskLater(host, () -> boom(strike, 3, 12, p), base + 12);
                    }
                }
                case "venom" -> {
                    if (!ready(p, id, 4000)) return;
                    p.setVelocity(look.clone().multiply(1.2).setY(0.2));
                    guard(p, 30);
                    for (Entity en : w.getNearbyEntities(p.getLocation(), 2.5, 2, 2.5)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p)) continue;
                        venomTouch(le);
                        hit(le, 6, p);
                    }
                    w.spawnParticle(Particle.SWEEP_ATTACK, p.getLocation().add(0, 1, 0), 3, 0.5, 0.5, 0.5, 0);
                }
                case "glacier" -> {
                    if (!ready(p, id, 8000)) return;
                    PhoenixState call = new PhoenixState();
                    call.call = true;
                    p.setVelocity(new Vector(0, 1.4, 0));
                    p.setFallDistance(0f);
                    phoenix.put(p.getUniqueId(), call);
                    w.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1f, 0.8f);
                    p.sendMessage(ChatColor.GOLD + "Phoenix Call!");
                }
                case "ghostlantern" -> {
                    if (!ready(p, id, 30000)) return;
                    final BukkitTask[] holder = new BukkitTask[1];
                    final int[] t = {0};
                    holder[0] = Bukkit.getScheduler().runTaskTimer(host, () -> {
                        if (!p.isOnline() || t[0] > 200) {
                            holder[0].cancel();
                            return;
                        }
                        for (int i = 0; i < 3; i++) {
                            double ang = t[0] * 0.15 + i * 2 * Math.PI / 3;
                            Location orb = p.getLocation().add(Math.cos(ang) * 1.6, 1.2 + Math.sin(t[0] * 0.1 + i) * 0.3, Math.sin(ang) * 1.6);
                            p.getWorld().spawnParticle(Particle.SOUL, orb, 1, 0, 0, 0, 0);
                        }
                        if (t[0] % 20 == 0) {
                            LivingEntity target = nearestEnemy(p.getLocation(), 12, p);
                            if (target != null) {
                                p.getWorld().strikeLightningEffect(target.getLocation());
                                hit(target, 7, p);
                            }
                        }
                        t[0]++;
                    }, 0L, 1L);
                }
                default -> { }
            }
        }

        @EventHandler
        public void onInteractEntity(PlayerInteractEntityEvent e) {
            Player p = e.getPlayer();
            if (!"gauntlet".equals(idOf(p.getInventory().getItem(e.getHand())))) return;
            e.setCancelled(true);
            if (holds.containsKey(p.getUniqueId())) {
                release(p, true);
            } else if (e.getRightClicked() instanceof LivingEntity le && !le.equals(p)) {
                grab(p, le);
            }
        }

        // ---------- Ticking effects ----------

        private void tickPhoenix() {
            Iterator<Map.Entry<UUID, PhoenixState>> it = phoenix.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<UUID, PhoenixState> en = it.next();
                Player p = Bukkit.getPlayer(en.getKey());
                if (p == null || !p.isOnline()) { it.remove(); continue; }
                PhoenixState s = en.getValue();
                s.ticks++;
                if (s.phase == 0) {
                    s.peak = Math.max(s.peak, p.getLocation().getY());
                    if (s.ticks >= 8 || p.getVelocity().getY() <= 0) {
                        p.setVelocity(new Vector(0, -2.5, 0));
                        s.phase = 1;
                        s.ticks = 0;
                    }
                } else if (s.phase == 1) {
                    if (s.ticks > 2 && p.isOnGround()) {
                        smash(p, s);
                        s.phase = 2;
                        s.ticks = 0;
                    } else if (s.ticks > 400) {
                        it.remove();
                    }
                } else if (s.ticks >= 3) {
                    it.remove();
                }
            }
        }

        private void smash(Player p, PhoenixState s) {
            Location loc = p.getLocation();
            World w = p.getWorld();
            double drop = Math.max(0, s.peak - loc.getY());
            double dmg = 14 + Math.min(40, drop * 2);
            w.spawnParticle(Particle.EXPLOSION, loc, 1);
            w.spawnParticle(Particle.CLOUD, loc, 60, 3, 0.2, 3, 0.1);
            if (s.call) {
                w.spawnParticle(Particle.FLAME, loc, 80, 3, 0.3, 3, 0.1);
                w.spawnParticle(Particle.SNOWFLAKE, loc, 80, 3, 0.3, 3, 0.1);
            }
            w.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
            damageArea(loc, 6, dmg, p);
            knockArea(loc, 6, 0.6, p);
            p.setFallDistance(0f);
            p.sendMessage(ChatColor.GOLD + "Impact: " + String.format(Locale.ROOT, "%.0f", dmg)
                    + " damage from " + String.format(Locale.ROOT, "%.1f", drop) + " blocks up.");
        }

        private void tickJetpack() {
            Iterator<Map.Entry<UUID, Integer>> it = jetFuel.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<UUID, Integer> en = it.next();
                Player p = Bukkit.getPlayer(en.getKey());
                int fuel = en.getValue();
                if (p == null || !p.isOnline()) { it.remove(); continue; }
                if (fuel <= 0) {
                    p.sendMessage(ChatColor.RED + "Jetpack out of fuel.");
                    it.remove();
                    continue;
                }
                Vector v = p.getVelocity();
                double vy = v.getY() < 0.3 ? 0.35 : v.getY();
                p.setVelocity(new Vector(v.getX(), vy, v.getZ()));
                p.setFallDistance(0f);
                guard(p, 10);
                p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 3, 0.1, 0, 0.1, 0.01);
                en.setValue(fuel - 1);
            }
        }

        private void grab(Player p, LivingEntity mob) {
            holds.put(p.getUniqueId(), mob.getUniqueId());
            if (mob instanceof Player target) guard(target, 60);
            p.sendMessage(ChatColor.GREEN + "Grabbed " + (mob instanceof Player pl ? pl.getName() : "mob")
                    + ". Right-click again to hurl it.");
        }

        private void release(Player p, boolean hurl) {
            UUID mobId = holds.remove(p.getUniqueId());
            if (mobId == null) return;
            Entity mob = Bukkit.getEntity(mobId);
            if (hurl && mob != null) {
                Vector look = p.getEyeLocation().getDirection().normalize();
                if (mob instanceof Player target) guard(target, 60);
                mob.setVelocity(look.multiply(2.2).setY(0.3));
                p.sendMessage(ChatColor.YELLOW + "Hurled!");
            } else {
                p.sendMessage(ChatColor.YELLOW + "Released.");
            }
        }

        // Holds the grabbed mob in front of the player by teleporting it each tick.
        private void tickGem() {
            Iterator<Map.Entry<UUID, UUID>> it = holds.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<UUID, UUID> en = it.next();
                Player p = Bukkit.getPlayer(en.getKey());
                Entity mob = Bukkit.getEntity(en.getValue());
                if (p == null || !p.isOnline() || mob == null || !mob.isValid()) {
                    it.remove();
                    continue;
                }
                if (!holdsItem(p, "gauntlet")
                        || mob.getWorld() != p.getWorld()
                        || mob.getLocation().distance(p.getLocation()) > 40) {
                    it.remove();
                    p.sendMessage(ChatColor.YELLOW + "Released.");
                    continue;
                }
                Location eye = p.getEyeLocation();
                Location hold = eye.clone().add(eye.getDirection().normalize().multiply(3.0));
                if (mob instanceof Player target) {
                    // Players can't be teleported every tick without stuttering, so steer them.
                    Vector toHold = hold.clone().subtract(0, 1.6, 0).toVector().subtract(target.getLocation().toVector());
                    if (toHold.length() > 1.5) toHold.normalize().multiply(1.5);
                    target.setVelocity(toHold.multiply(0.6));
                    target.setFallDistance(0f);
                    guard(target, 10);
                } else {
                    hold.setYaw(p.getLocation().getYaw());
                    hold.setPitch(0f);
                    mob.teleport(hold);
                    mob.setVelocity(new Vector(0, 0, 0));
                    mob.setFallDistance(0f);
                }
            }
        }

        private void tickFallGuard() {
            Iterator<Map.Entry<UUID, Integer>> it = fallGuard.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<UUID, Integer> en = it.next();
                Player p = Bukkit.getPlayer(en.getKey());
                if (p == null || !p.isOnline()) { it.remove(); continue; }
                if (!p.isOnGround()) {
                    p.setFallDistance(0f);
                    continue;
                }
                int left = en.getValue() - 1;
                if (left <= 0) it.remove();
                else en.setValue(left);
            }
        }

        // ---------- Damage / projectile handling ----------

        @EventHandler
        public void onDamage(EntityDamageByEntityEvent e) {
            if (e.getDamager() instanceof Player attacker
                    && e.getEntity() instanceof LivingEntity victim
                    && !victim.equals(attacker)) {
                UUID gid = golems.get(attacker.getUniqueId());
                if (gid != null) {
                    Entity g = Bukkit.getEntity(gid);
                    if (g instanceof IronGolem ig && ig.isValid() && !victim.equals(ig)) ig.setTarget(victim);
                }
            }
            if (!(e.getDamager() instanceof Player p)) return;
            String id = idOf(p.getInventory().getItemInMainHand());
            if (id == null) return;
            switch (id) {
                case "frostbite" -> {
                    if (e.getEntity() instanceof LivingEntity le) {
                        le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 1));
                        e.setDamage(e.getDamage() + 4);
                    }
                }
                case "thunderstick" -> {
                    if (e.getEntity() instanceof LivingEntity le) {
                        p.getWorld().strikeLightningEffect(le.getLocation());
                        e.setDamage(e.getDamage() + 6);
                    }
                }
                case "vampirefang" -> {
                    double heal = e.getDamage() * 0.4;
                    p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + heal));
                }
                case "venom" -> {
                    if (e.getEntity() instanceof LivingEntity le) {
                        venomTouch(le);
                        e.setDamage(e.getDamage() + 3);
                    }
                }
                default -> { }
            }
        }

        @EventHandler
        public void onWindFall(EntityDamageEvent e) {
            if (e.getCause() != EntityDamageEvent.DamageCause.FALL) return;
            if (e.getEntity() instanceof Player p && holdsItem(p, "wind")) {
                e.setCancelled(true);
                return;
            }
            if (e.getEntity() instanceof LivingEntity le) {
                Long until = launchedUntil.get(le.getUniqueId());
                if (until == null) return;
                if (System.currentTimeMillis() < until) {
                    e.setDamage(Math.min(e.getDamage(), 8));
                } else {
                    launchedUntil.remove(le.getUniqueId());
                }
            }
        }

        @EventHandler
        public void onFall(EntityDamageEvent e) {
            if (e.getCause() != EntityDamageEvent.DamageCause.FALL) return;
            if (!(e.getEntity() instanceof Player p)) return;
            if (phoenix.containsKey(p.getUniqueId()) || fallGuard.containsKey(p.getUniqueId())) {
                e.setCancelled(true);
            }
        }

        @EventHandler
        public void onShoot(EntityShootBowEvent e) {
            if (!(e.getEntity() instanceof Player shooterP)) return;
            String id = idOf(e.getBow());
            if (id != null && (id.equals("kaboom") || id.equals("shakalaka") || id.equals("arccaster"))) {
                e.getProjectile().getPersistentDataContainer().set(projKey, PersistentDataType.STRING, id);
                return;
            }
            if (id != null && SHOT_TAGS.containsKey(id)) {
                e.getProjectile().getPersistentDataContainer().set(projKey, PersistentDataType.STRING, SHOT_TAGS.get(id));
                if (id.equals("ravenshot") && e.getProjectile() instanceof org.bukkit.entity.AbstractArrow aa) aa.setPierceLevel(3);
                if (id.equals("ironbow") && e.getProjectile() instanceof org.bukkit.entity.AbstractArrow ib) ib.setPierceLevel(2);
                if (id.equals("hailstorm") && ready(shooterP, id, 4000)) {
                    Vector base = e.getProjectile().getVelocity();
                    double speed = Math.max(2.0, base.length());
                    Vector dir = base.lengthSquared() > 0 ? base.clone().normalize() : shooterP.getLocation().getDirection();
                    for (int i = 0; i < 4; i++) {
                        org.bukkit.entity.Arrow extra = shooterP.getWorld().spawnArrow(shooterP.getEyeLocation(), dir, (float) speed, 12f);
                        extra.setShooter(shooterP);
                        extra.getPersistentDataContainer().set(projKey, PersistentDataType.STRING, "hailbolt");
                    }
                }
                return;
            }
            if (holdsItem(shooterP, "blizzard") && rnd.nextDouble() < 0.30) {
                e.getProjectile().getPersistentDataContainer().set(projKey, PersistentDataType.STRING, "frozen");
            }
        }

        @EventHandler
        public void onProjectileHit(ProjectileHitEvent e) {
            Projectile pr = e.getEntity();
            String tag = pr.getPersistentDataContainer().get(projKey, PersistentDataType.STRING);
            if (tag == null) return;

            Location loc = pr.getLocation();
            World w = loc.getWorld();
            ProjectileSource src = pr.getShooter();
            Entity shooter = src instanceof Entity en ? en : null;

            switch (tag) {
                case "hailbolt" -> {
                    if (e.getHitEntity() instanceof LivingEntity hb && shooter instanceof Player hp && !isFriendlyTo(hp, hb)) hit(hb, 4, hp);
                }
                case "sparkbolt" -> boom(loc, 2.5, 6, shooter);
                case "frostbolt" -> {
                    if (e.getHitEntity() instanceof LivingEntity fb && shooter instanceof Player fp && !isFriendlyTo(fp, fb)) {
                        hit(fb, 4, fp);
                        fb.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 1));
                    }
                }
                case "crossthunder" -> {
                    loc.getWorld().strikeLightningEffect(loc);
                    damageArea(loc, 2, 5, shooter);
                }
                case "rocketbolt" -> boom(loc, 3, 8, shooter);
                case "vinebow" -> {
                    if (e.getHitEntity() instanceof LivingEntity vb && shooter instanceof Player vp2 && !isFriendlyTo(vp2, vb)) {
                        hit(vb, 3, vp2);
                        vb.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 9));
                    }
                }
                case "gale" -> {
                    for (Entity en : loc.getWorld().getNearbyEntities(loc, 3, 3, 3)) {
                        if (!(en instanceof LivingEntity ge) || en.equals(shooter)) continue;
                        Vector out = ge.getLocation().toVector().subtract(loc.toVector()).setY(0.3);
                        if (out.lengthSquared() > 0) ge.setVelocity(out.normalize().multiply(1.5));
                    }
                }
                case "ironbow" -> {
                    if (e.getHitEntity() instanceof LivingEntity ib2 && shooter instanceof Player ip2 && !isFriendlyTo(ip2, ib2)) hit(ib2, 6, ip2);
                }
                case "ironbolt" -> {
                    if (e.getHitEntity() instanceof LivingEntity ib && shooter instanceof Player ip && !isFriendlyTo(ip, ib)) {
                        hit(ib, 5, ip);
                        ib.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 1));
                    }
                }
                case "pyrebolt" -> {
                    if (e.getHitEntity() instanceof LivingEntity pb && shooter instanceof Player pp && !isFriendlyTo(pp, pb)) {
                        hit(pb, 3, pp);
                        pb.setFireTicks(100);
                    }
                }
                case "echobolt" -> {
                    if (e.getHitEntity() instanceof LivingEntity eb && shooter instanceof Player ep && !isFriendlyTo(ep, eb)) {
                        hit(eb, 6, ep);
                        for (Entity en : eb.getWorld().getNearbyEntities(eb.getLocation(), 6, 3, 6)) {
                            if (!(en instanceof LivingEntity el) || en.equals(eb) || en.equals(ep) || isFriendlyTo(ep, el)) continue;
                            hit(el, 6, ep);
                            break;
                        }
                    }
                }
                case "stormbolt" -> {
                    loc.getWorld().strikeLightningEffect(loc);
                    damageArea(loc, 2.5, 6, shooter);
                }
                case "chainbolt" -> {
                    if (e.getHitEntity() instanceof LivingEntity cb && shooter instanceof Player cp && !isFriendlyTo(cp, cb)) {
                        Vector pull = cp.getLocation().toVector().subtract(cb.getLocation().toVector());
                        if (pull.lengthSquared() > 0) cb.setVelocity(pull.normalize().multiply(1.1).setY(0.3));
                    }
                }
                case "ghostmark" -> {
                    if (e.getHitEntity() instanceof LivingEntity gm && shooter instanceof Player gp && !isFriendlyTo(gp, gm)) {
                        hit(gm, 3, gp);
                        gm.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 160, 0));
                    }
                }
                case "dawnbolt" -> {
                    if (e.getHitEntity() instanceof LivingEntity db && shooter instanceof Player dp2 && !isFriendlyTo(dp2, db)) {
                        hit(db, 4, dp2);
                        dp2.setHealth(Math.min(dp2.getMaxHealth(), dp2.getHealth() + 4));
                    }
                }
                case "ravenbolt" -> {
                    if (e.getHitEntity() instanceof LivingEntity rb && shooter instanceof Player rp && !isFriendlyTo(rp, rb)) {
                        hit(rb, 10, rp);
                        rb.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 1));
                    }
                }
                case "whirl" -> {
                    if (e.getHitEntity() instanceof Player victim && shooter instanceof Player wp && !isFriendlyTo(wp, victim)) startTornado(victim);
                }
                case "sunfall" -> {
                    for (int i = 0; i < 8; i++) {
                        Location top = loc.clone().add(rand(-3, 3), 12, rand(-3, 3));
                        org.bukkit.entity.Arrow rain = w.spawnArrow(top, new Vector(0, -1, 0), 1.6f, 0f);
                        if (shooter instanceof Player sp) rain.setShooter(sp);
                    }
                }
                case "gravepull" -> {
                    for (Entity en : w.getNearbyEntities(loc, 5, 5, 5)) {
                        if (!(en instanceof LivingEntity gl) || en.equals(shooter)) continue;
                        Vector pull = loc.toVector().subtract(gl.getLocation().toVector()).setY(0.2);
                        if (pull.lengthSquared() > 0) gl.setVelocity(pull.normalize().multiply(0.9));
                    }
                }
                case "kaboom" -> boom(loc, 4, 12, shooter);
                case "grenade" -> boom(loc, 3.5, 12, shooter);
                case "thunderbolt" -> {
                    if (shooter instanceof Player tp) {
                        if (e.getHitEntity() instanceof LivingEntity stunned && !isFriendlyTo(tp, stunned)) {
                            stun(tp, stunned);
                        } else {
                            w.strikeLightningEffect(loc);
                            damageArea(loc, 2, 6, tp);
                        }
                    }
                }
                case "meteor" -> boom(loc, 3, 10, shooter);
                case "blazeball" -> boom(loc, 3.5, 14, shooter);
                case "charge1", "charge2", "charge3" -> {
                    int lvl = tag.charAt(6) - '0';
                    boom(loc, 1.5 + lvl, 4 + lvl * 4, shooter);
                }
                case "subzero" -> {
                    damageArea(loc, 3, 10, shooter);
                    w.spawnParticle(Particle.CLOUD, loc, 30, 1, 1, 1, 0.05);
                }
                case "honey" -> {
                    for (Entity en : w.getNearbyEntities(loc, 3, 3, 3)) {
                        if (!(en instanceof LivingEntity le) || en.equals(shooter) || isFriendlyTo(shooter, le)) continue;
                        le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 2));
                        hit(le, 4, shooter);
                    }
                    w.spawnParticle(Particle.DUST, loc, 40, 1, 0.6, 1, 0, new Particle.DustOptions(Color.fromRGB(0xFFB300), 1.6f));
                }
                case "crystal" -> {
                    damageArea(loc, 2, 14, shooter);
                    w.spawnParticle(Particle.END_ROD, loc, 12, 0.3, 0.3, 0.3, 0.05);
                }
                case "voidslash" -> {
                    damageArea(loc, 3, 14, shooter);
                    knockArea(loc, 3, 0.5, shooter);
                    w.spawnParticle(Particle.DRAGON_BREATH, loc, 40, 1, 1, 1, 0.05);
                }
                case "reaperskull" -> {
                    damageArea(loc, 2.5, 12, shooter);
                    for (Entity en : w.getNearbyEntities(loc, 2.5, 2.5, 2.5)) {
                        if (en instanceof LivingEntity wl && !en.equals(shooter)) {
                            wl.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));
                        }
                    }
                    w.spawnParticle(Particle.SMOKE, loc, 30, 0.8, 0.8, 0.8, 0.05);
                }
                case "gust" -> {
                    damageArea(loc, 3, 5, shooter);
                    knockArea(loc, 3, 0.7, shooter);
                    particleRing(loc.clone().add(0, 0.5, 0), 2.0, Particle.CLOUD);
                }
                case "arccaster" -> {
                    if (shooter instanceof Player ap && e.getHitEntity() instanceof LivingEntity arcHit && !isFriendlyTo(ap, arcHit)) {
                        if (rnd.nextBoolean()) {
                            stun(ap, arcHit);
                        } else {
                            arcHit.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 80, 9));
                            ap.sendMessage(ChatColor.AQUA + "Frozen!");
                        }
                    }
                }
                case "frozen" -> {
                    if (e.getHitEntity() instanceof LivingEntity frozenMob && !isFriendlyTo(shooter, frozenMob)) {
                        frozenMob.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 9));
                    }
                }
                case "snow" -> {
                    if (shooter instanceof Player sp && e.getHitEntity() instanceof LivingEntity snowed
                            && !isFriendlyTo(sp, snowed)) {
                        hit(snowed, 7, sp);
                        Vector kb = pr.getVelocity();
                        if (kb.lengthSquared() > 0) snowed.setVelocity(kb.normalize().multiply(1.2).setY(0.4));
                        String key = sp.getUniqueId() + ":" + snowed.getUniqueId();
                        int count = snowHits.merge(key, 1, Integer::sum);
                        if (count >= 3) {
                            snowed.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 140, 9));
                            snowHits.remove(key);
                            sp.sendMessage(ChatColor.AQUA + "Frozen!");
                        }
                        if (isActive(subzeroUntil, sp)) boom(loc, 2.5, 8, sp);
                    }
                }
                case "iceshard" -> {
                    damageArea(loc, 2, 6, shooter);
                    for (Entity en : w.getNearbyEntities(loc, 2, 2, 2)) {
                        if (en instanceof LivingEntity hitMob && !en.equals(shooter)) {
                            hitMob.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 1));
                        }
                    }
                    w.spawnParticle(Particle.SNOWFLAKE, loc, 20, 0.5, 0.5, 0.5, 0.05);
                }
                case "tidal" -> {
                    boom(loc, 3, 12, shooter);
                    w.spawnParticle(Particle.SPLASH, loc, 60, 1.5, 0.5, 1.5, 0.1);
                    knockArea(loc, 3, 0.8, shooter);
                }
                case "shakalaka" -> {
                    for (int i = 0; i < 3; i++) {
                        Location spot = loc.clone().add(rand(-2, 2), 0, rand(-2, 2));
                        shakalakaRoll(spot, shooter);
                    }
                }
                default -> { }
            }
            pr.remove();
        }

        private void shakalakaRoll(Location loc, Entity shooter) {
            World w = loc.getWorld();
            switch (rnd.nextInt(3)) {
                case 0 -> {
                    w.strikeLightningEffect(loc);
                    damageArea(loc, 3, 9, shooter);
                }
                case 1 -> boom(loc, 3.5, 9, shooter);
                default -> {
                    Fireball f = w.spawn(loc.clone().add(0, 8, 0), Fireball.class);
                    f.setDirection(new Vector(0, -1, 0));
                    f.setYield(0f);
                    f.setIsIncendiary(true);
                }
            }
        }

        private boolean holdsItem(Player p, String id) {
            if (id.equals("cursedcrown") && id.equals(idOf(p.getInventory().getHelmet()))) return true;
            return id.equals(idOf(p.getInventory().getItemInMainHand()))
                    || id.equals(idOf(p.getInventory().getItemInOffHand()));
        }

        private void ring(Location c, double radius) {
            for (int deg = 0; deg < 360; deg += 10) {
                double rad = Math.toRadians(deg);
                c.getWorld().spawnParticle(Particle.SNOWFLAKE,
                        c.clone().add(Math.cos(rad) * radius, 0.2, Math.sin(rad) * radius), 1, 0, 0, 0, 0);
            }
        }

        // ===================== GEMS =====================

        private void registerGems() {
            gemDef("blizzard", "Blizzard Gem", Material.AMETHYST_SHARD, "#80DEEA",
                    "Right-click: Frost Lift (freezes and lifts nearby enemies while arrows rain in). Shift+right-click: Ice Barrier. F: Snow Storm (15s). Shift+F: Subzero (ultimate). Passives: arrows can freeze, immune to snow, lava and magma.");
            gemDef("nightmare", "Nightmare Gem", Material.AMETHYST_SHARD, "#263238",
                    "Right-click: Night Terror. Shift+right-click: Nightmare Punishment (5s). F: Abyssal Sight. Shift+F: Nightmare Phantom (ultimate). Passives: tame bats and creakings with Echo Shards, Echo Location.");
            gemDef("thunder", "Thunder Master Gem", Material.AMETHYST_SHARD, "#FFEB3B", "Right-click: Thunder Strike (lightning at your aim, 12 damage and a stun). Shift+right-click: Chain Lightning (up to 5 foes). F: Overcharge (next 3 hits +10 damage). Shift+F: Zeus (ultimate, 20 seconds of lightning storm). Passives: 20% lightning on hit, immune to lightning, slowness and weakness.");
            gemDef("wind", "Wind Master Gem", Material.AMETHYST_SHARD, "#B2EBF2", "Right-click: Gust (pushes and damages nearby foes). Shift+right-click: Updraft (launch yourself up, no fall damage). F: Tornado (orbits foes within 12 blocks). Sprint+F: Berserk Breeze (ultimate, 30 seconds of gusts on every right-click). Passives: no fall damage, stronger bow, 5% chance to launch players on hit.");
            gemDef("inferno", "Inferno Gem", Material.AMETHYST_SHARD, "#FF6D00",
                    "Right-click: Meteor Shower. Shift+right-click: Cozy Campfire. F: Chargeable Fireball (press F to start charging, F again to release, charge rises to 100% then drains). Shift+F: Blaze (ultimate, explosive fireballs with no cooldown for 30 seconds). Sprint+F: Evaporation (toggle, water around you turns to air). Passives: fire immunity, fire damage on swords.");
            gemDef("atomic", "Atomic Gem", Material.AMETHYST_SHARD, "#76FF03",
                    "Right-click: Atomic Blast (shockwave, heavy damage and knockback). Shift+right-click: Radiation Cloud (15s wither and nausea nearby). F: Fallout Shield (10s, half damage, attackers blinded). Shift+F: Meltdown (ultimate, 20s glow, radiation damage nearby). Passives: immune to wither and poison, slow regeneration.");
            gemDef("warden", "Warden Keeper Gem", Material.AMETHYST_SHARD, "#0D47A1", "Right-click: Sonic Wave (a cone sonic boom). Shift+right-click: Echo Pulse (reveals enemies). F: Warden's Fist (warden-strength slam). Shift+F: Warden Monster (ultimate, a warden ally for 5 minutes). Passives: immune to fire, lava, falls, poison, wither, blindness, darkness, slowness and nausea.");
            gemDef("yinyang", "Yin Yang Gem", Material.AMETHYST_SHARD, "#EEEEEE", "Right-click: Yin Strike (heal you, damage nearby). Shift+right-click: Yang Strike (burn nearby, hurt you). F: Balance (swap health with nearest enemy for 5 seconds). Shift+F: Taijitu (ultimate, 60-second ring that pushes enemies and heals allies). Passives: half of damage taken heals you back, slow regeneration.");
            gemDef("eclipse", "Solar Eclipse Gem", Material.AMETHYST_SHARD, "#4A148C", "Right-click: Eclipse Pulse (12 damage, blind, slow and weaken nearby). Shift+right-click: Shadow Lunge (teleport behind the nearest enemy, 15 damage). F: Eclipse Ward (10 hearts of absorption, attackers blinded, 25% of their damage reflected). Shift+F: Total Eclipse (ultimate, 30 seconds of darkness, invisibility and Strength II). Passives: blocks darkness and blindness, takes 15% less damage at night, dark aura. The Light Before the Dark: below 5 hearts you get Resistance, a black campfire aura and slow healing.");
            gemDef("undead", "Undead Legion Gem", Material.AMETHYST_SHARD, "#A5D6A7",
                    "Right-click: Wither Legion. Shift+right-click: Fallen Recall. F: Wither Touch. Shift+F: Giant Wither Skeleton (ultimate). Passives: heal and extra damage vs undead.");
        }

        private void gemDef(String id, String name, Material mat, String hex, String lore) {
            gems.put(id, new ItemDef(id, name, mat, hex, lore));
            modelIds.put(id, 2000 + gems.size());
        }

        private boolean isActive(Map<UUID, Long> map, Player p) {
            Long until = map.get(p.getUniqueId());
            return until != null && System.currentTimeMillis() < until;
        }

        private String gemOf(Player p) {
            String main = idOf(p.getInventory().getItemInMainHand());
            if (main != null && gems.containsKey(main)) return main;
            String off = idOf(p.getInventory().getItemInOffHand());
            if (off != null && gems.containsKey(off)) return off;
            return null;
        }

        private boolean holdsMaterial(Player p, Material m) {
            return p.getInventory().getItemInMainHand().getType() == m
                    || p.getInventory().getItemInOffHand().getType() == m;
        }

        private void setOwner(Entity e, UUID owner) {
            e.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING, owner.toString());
        }

        private UUID ownerOf(Entity e) {
            if (e == null) return null;
            if (e instanceof Player p) return p.getUniqueId();
            if (e instanceof Projectile pr && pr.getShooter() instanceof Player sp) return sp.getUniqueId();
            String s = e.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
            return s == null ? null : UUID.fromString(s);
        }

        private boolean isTrusted(UUID owner, UUID other) {
            Set<UUID> set = trusts.get(owner);
            return set != null && set.contains(other);
        }

        // True when the source (player, mob or projectile) belongs to someone who owns or trusts the victim.
        private boolean isFriendlyTo(Entity source, Entity victim) {
            UUID owner = ownerOf(source);
            if (owner == null) return false;
            if (!(victim instanceof Player vp)) return false;
            return vp.getUniqueId().equals(owner) || isTrusted(owner, vp.getUniqueId());
        }

        private boolean handHasItem(PlayerInteractEntityEvent e) {
            ItemStack hand = e.getPlayer().getInventory().getItem(e.getHand());
            return hand != null && !hand.getType().isAir();
        }

        private boolean isMount(Entity en) {
            String t = en.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING);
            return "giantmob".equals(t) || "giantparrot".equals(t) || "giantaxo".equals(t);
        }

        @EventHandler
        public void onMountNoSuffocate(EntityDamageEvent e) {
            if (e.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION) return;
            Entity v = e.getEntity();
            if (isMount(v) || (v instanceof Player pl && pl.getVehicle() != null && isMount(pl.getVehicle()))) {
                e.setCancelled(true);
            }
        }

        private boolean isUndead(LivingEntity t) {
            return t instanceof AbstractSkeleton || t instanceof Zombie || t instanceof Phantom;
        }

        private void damageArmor(Player p, int amount) {
            ItemStack[] armor = p.getInventory().getArmorContents();
            for (ItemStack piece : armor) {
                if (piece == null) continue;
                ItemMeta m = piece.getItemMeta();
                if (m instanceof Damageable dmg) {
                    dmg.setDamage(dmg.getDamage() + amount);
                    piece.setItemMeta(dmg);
                }
            }
            p.getInventory().setArmorContents(armor);
        }

        private void applyKnownEffects(LivingEntity target, PotionEffect... effects) {
            for (PotionEffect fx : effects) target.addPotionEffect(fx);
        }

        // ----- Right-click (ability 1 = right-click, ability 2 = shift + right-click) -----

        private void handleGemRight(Player p, String gem) {
            countAbility(p);
            boolean shift = p.isSneaking();
            switch (gem) {
                case "blizzard" -> {
                    if (shift) blizzardBarrier(p);
                    else if (isActive(snowUntil, p)) fireSnow(p);
                    else if (isActive(subzeroUntil, p)) tridentRain(p);
                    else blizzardLift(p);
                }
                case "thunder" -> {
                    if (shift) chainLightning(p);
                    else thunderStrike(p);
                }
                case "nightmare" -> {
                    if (shift) nightmarePunish(p);
                    else nightTerror(p);
                }
                case "undead" -> {
                    if (shift) fallenRecall(p);
                    else witherLegion(p);
                }
                case "eclipse" -> {
                    if (shift) shadowStep(p);
                    else voidPulse(p);
                }
                case "warden" -> {
                    if (shift) echoPulse(p);
                    else sonicWave(p);
                }
                case "yinyang" -> {
                    if (shift) yangStrike(p);
                    else yinStrike(p);
                }
                case "atomic" -> {
                    if (shift) radiationCloud(p);
                    else atomicBlast(p);
                }
                case "wind" -> {
                    if (shift) updraft(p);
                    else if (isActive(windBerserkUntil, p)) fireGust(p);
                    else gust(p);
                }
                case "inferno" -> {
                    if (shift) campfire(p);
                    else if (isActive(blazeUntil, p)) fireBlazeBall(p);
                    else meteorShower(p);
                }
                default -> { }
            }
        }

        // ----- F key (ability 3 = F, ultimate = shift + F) -----

        @EventHandler
        public void onGemF(PlayerSwapHandItemsEvent e) {
            Player p = e.getPlayer();
            try {
                onGemFInner(e);
                String g = gemOf(p);
                if (g != null && GEM_ABILITIES.containsKey(g) && !charging.containsKey(p.getUniqueId())) {
                    boolean ult = g.equals("wind") ? p.isSprinting() : p.isSneaking();
                    List<String> ab = GEM_ABILITIES.get(g);
                    long left = cdLeft(p, ult ? ab.get(3) : ab.get(2));
                    if (left > 0) p.sendTitle("", ChatColor.RED + "Cooldown " + ((left / 1000) + 1) + "s", 0, 25, 5);
                }
            } catch (Throwable t) {
                host.getLogger().log(Level.SEVERE, "F key error", t);
                p.sendMessage(ChatColor.RED + "F key error: " + t);
            }
        }

        private void onGemFInner(PlayerSwapHandItemsEvent e) {
            Player p = e.getPlayer();
            String gem = gemOf(p);
            if (gem == null) return;
            e.setCancelled(true);
            boolean shift = p.isSneaking();
            boolean sprint = p.isSprinting();
            boolean ult = gem.equals("wind") ? sprint : shift;
            dbg(p, "F press gem=" + gem + " ult=" + ult + " sneaking=" + shift + " sprinting=" + sprint);
            if (ult) {
                if (!ultGate(p)) return;
                switch (gem) {
                    case "blizzard" -> subzero(p);
                    case "thunder" -> zeus(p);
                    case "nightmare" -> giantCreaking(p);
                    case "undead" -> giantWither(p);
                    case "wind" -> windBerserk(p);
                    case "inferno" -> blazeMode(p);
                    case "eclipse" -> totalEclipse(p);
                    case "warden" -> wardenMonster(p);
                    case "yinyang" -> taijitu(p);
                    case "atomic" -> meltdown(p);
                    default -> { }
                }
                return;
            }
            countAbility(p);
            switch (gem) {
                case "blizzard" -> snowStorm(p);
                case "thunder" -> surge(p);
                case "nightmare" -> abyssalSight(p);
                case "undead" -> witherTouch(p);
                case "wind" -> tornado(p);
                case "inferno" -> {
                    if (sprint) toggleEvaporation(p);
                    else chargeFireball(p);
                }
                case "eclipse" -> umbralShield(p);
                case "warden" -> wardenFist(p);
                case "yinyang" -> balance(p);
                case "atomic" -> falloutShield(p);
                default -> { }
            }
        }

        // ----- Blizzard -----

        private void blizzardLift(Player p) {
            World w = p.getWorld();
            List<Player> victims = new ArrayList<>();
            for (Entity en : w.getNearbyEntities(p.getLocation(), 8, 4, 8)) {
                if (!(en instanceof Player v)) continue;
                if (isFriendlyTo(p, v)) continue;
                victims.add(v);
            }
            if (victims.isEmpty()) {
                p.sendMessage(ChatColor.RED + "No enemy players nearby.");
                return;
            }
            if (!ready(p, "g_lift", 120000)) return;
            Map<UUID, Double> liftY = new HashMap<>();
            for (Player v : victims) liftY.put(v.getUniqueId(), v.getLocation().getY() + 4);
            w.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 1.5f, 0.8f);

            final int[] runs = {0};
            final BukkitTask[] holder = new BukkitTask[1];
            holder[0] = gemTimer(p, () -> {
                runs[0]++;
                if (runs[0] > 60) {
                    holder[0].cancel();
                    return;
                }
                for (Player v : victims) {
                    if (!v.isOnline()) continue;
                    double dy = liftY.get(v.getUniqueId()) - v.getLocation().getY();
                    v.setVelocity(new Vector(0, Math.max(-0.5, Math.min(0.5, dy * 0.5)), 0));
                    v.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 10, 9));
                    v.setFallDistance(0f);
                    guard(v, 20);
                }
            }, 0L, 1L);

            for (int t = 0; t < 15; t++) {
                Bukkit.getScheduler().runTaskLater(host, () -> {
                    for (Player v : victims) {
                        if (!v.isOnline()) continue;
                        for (int k = 0; k < 2; k++) {
                            double ang = rand(0, Math.PI * 2);
                            Location from = v.getLocation().add(Math.cos(ang) * 8, rand(3, 7), Math.sin(ang) * 8);
                            Vector dir = v.getLocation().add(0, 1, 0).toVector().subtract(from.toVector()).normalize();
                            Arrow ar = w.spawnArrow(from, dir, 1.6f, 0f);
                            ar.setShooter(p);
                            ar.setDamage(1.5);
                        }
                    }
                }, t * 4L);
            }
            p.sendMessage(ChatColor.AQUA + "Frost Lift! " + victims.size() + " target(s) frozen in the air.");
        }

        private static final class BarrierState {
            final UUID owner;
            final Map<Block, Material> placed = new HashMap<>();
            long expiry;

            BarrierState(UUID owner, long expiry) {
                this.owner = owner;
                this.expiry = expiry;
            }
        }

        private void blizzardBarrier(Player p) {
            Vector horiz = p.getEyeLocation().getDirection().setY(0);
            if (horiz.lengthSquared() == 0) return;
            if (!ready(p, "g_barrier", 60000)) return;
            horiz.normalize();
            Vector perp = new Vector(-horiz.getZ(), 0, horiz.getX());
            Location center = p.getLocation().add(horiz.clone().multiply(4)).add(0, 4, 0);
            BarrierState bs = new BarrierState(p.getUniqueId(), System.currentTimeMillis() + 15000);
            for (int u = -4; u <= 4; u++) {
                for (int v = -4; v <= 4; v++) {
                    if (u * u + v * v > 16) continue;
                    Block b = center.clone().add(perp.clone().multiply(u)).add(0, v, 0).getBlock();
                    if (b.getType().isAir()) {
                        bs.placed.put(b, Material.AIR);
                        b.setType(Material.ICE);
                    }
                }
            }
            barriers.add(bs);
            for (int deg = 0; deg < 360; deg += 15) {
                double rad = Math.toRadians(deg);
                Location rim = center.clone().add(perp.clone().multiply(Math.cos(rad) * 4.6)).add(0, Math.sin(rad) * 4.6, 0);
                p.getWorld().spawnParticle(Particle.SNOWFLAKE, rim, 2, 0.1, 0.1, 0.1, 0.01);
                p.getWorld().spawnParticle(Particle.END_ROD, rim, 1, 0, 0, 0, 0);
            }
            p.getWorld().playSound(center, Sound.BLOCK_GLASS_PLACE, 1.5f, 0.7f);
        }

        private void placeRing(BarrierState bs, Location center) {
            for (int deg = 0; deg < 360; deg += 10) {
                double rad = Math.toRadians(deg);
                for (int h = 0; h <= 2; h++) {
                    Block b = center.clone().add(Math.cos(rad) * 2.5, h, Math.sin(rad) * 2.5).getBlock();
                    if (b.getType().isAir() && !bs.placed.containsKey(b)) {
                        bs.placed.put(b, Material.AIR);
                        b.setType(Material.ICE);
                    }
                }
            }
        }

        private void restoreRing(BarrierState bs) {
            for (Map.Entry<Block, Material> en : bs.placed.entrySet()) en.getKey().setType(en.getValue());
            bs.placed.clear();
        }

        private void tickBarriers() {
            long now = System.currentTimeMillis();
            Iterator<BarrierState> it = barriers.iterator();
            while (it.hasNext()) {
                BarrierState bs = it.next();
                if (now >= bs.expiry) {
                    restoreRing(bs);
                    it.remove();
                    continue;
                }
                for (Player v : Bukkit.getOnlinePlayers()) {
                    if (v.getUniqueId().equals(bs.owner) || isTrusted(bs.owner, v.getUniqueId())) continue;
                    Location vl = v.getLocation().add(0, 1, 0);
                    for (Block b : bs.placed.keySet()) {
                        Location bl = b.getLocation().add(0.5, 0.5, 0.5);
                        if (bl.distance(vl) < 1.4) {
                            Vector out = vl.toVector().subtract(bl.toVector()).setY(0);
                            if (out.lengthSquared() > 0) v.setVelocity(out.normalize().multiply(1.2).setY(0.3));
                            v.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 1));
                            break;
                        }
                    }
                }
            }
        }

        private void snowStorm(Player p) {
            if (!ready(p, "g_snow", 90000)) return;
            snowUntil.put(p.getUniqueId(), System.currentTimeMillis() + 15000);
            p.sendMessage(ChatColor.AQUA + "Snow Storm active for 15 seconds. Right-click fires snowballs.");
        }

        private void fireSnow(Player p) {
            Vector look = p.getEyeLocation().getDirection().normalize();
            for (int i = -1; i <= 1; i++) {
                Vector v = look.clone().rotateAroundY(Math.toRadians(i * 6)).multiply(1.4);
                Snowball s = p.launchProjectile(Snowball.class, v);
                s.getPersistentDataContainer().set(projKey, PersistentDataType.STRING, "snow");
            }
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1f, 0.8f);
        }

        private void subzero(Player p) {
            if (!ready(p, "g_subzero", 300000)) return;
            subzeroUntil.put(p.getUniqueId(), System.currentTimeMillis() + 60000);
            final boolean prevFly = p.getAllowFlight();
            p.setAllowFlight(true);
            p.setFlying(true);
            p.sendMessage(ChatColor.AQUA + "Subzero: flying for 60 seconds. Right-click to rain tridents.");
            final BukkitTask[] h = new BukkitTask[1];
            h[0] = gemTimer(p, () -> {
                if (!p.isOnline() || !isActive(subzeroUntil, p)) {
                    subzeroUntil.remove(p.getUniqueId());
                    if (p.isOnline()) {
                        p.setFlying(false);
                        p.setAllowFlight(prevFly);
                    }
                    h[0].cancel();
                    return;
                }
                if (!p.isFlying()) p.setFlying(true);
                p.getWorld().spawnParticle(Particle.SNOWFLAKE, p.getLocation().add(0, 0.2, 0), 6, 0.4, 0.1, 0.4, 0.02);
            }, 0L, 5L);
        }

        private void tridentRain(Player p) {
            if (!ready(p, "g_rain", 3000)) return;
            Location aim = aimPoint(p, 40);
            World w = p.getWorld();
            for (int i = 0; i < 10; i++) {
                Location from = aim.clone().add(rand(-3, 3), 12, rand(-3, 3));
                Trident t = w.spawn(from, Trident.class);
                t.setShooter(p);
                t.setVelocity(new Vector(0, -1.8, 0));
                t.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                t.getPersistentDataContainer().set(projKey, PersistentDataType.STRING, "subzero");
            }
            w.playSound(aim, Sound.ITEM_TRIDENT_THUNDER, 2f, 1f);
        }

        private void tickBlizzardPassive() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!holdsItem(p, "blizzard")) continue;
                Block below = p.getLocation().subtract(0, 1, 0).getBlock();
                if (below.getType() == Material.LAVA) below.setType(Material.OBSIDIAN);
            }
        }

        @EventHandler
        public void onBlizzardImmunity(EntityDamageEvent e) {
            if (!(e.getEntity() instanceof Player p)) return;
            if (!holdsItem(p, "blizzard")) return;
            EntityDamageEvent.DamageCause c = e.getCause();
            if (c == EntityDamageEvent.DamageCause.FREEZE
                    || c == EntityDamageEvent.DamageCause.HOT_FLOOR
                    || c == EntityDamageEvent.DamageCause.LAVA) {
                e.setCancelled(true);
            }
        }

        // ----- Nightmare -----

        private void nightTerror(Player p) {
            if (!ready(p, "g_terror", 60000)) return;
            int count = 0;
            for (Entity en : p.getWorld().getNearbyEntities(p.getLocation(), 50, 50, 50)) {
                if (!(en instanceof Player v)) continue;
                if (isFriendlyTo(p, v)) continue;
                v.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 400, 0));
                v.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 400, 1));
                v.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 400, 0));
                v.playSound(v.getLocation(), Sound.ENTITY_WARDEN_AMBIENT, 2f, 0.5f);
                v.sendMessage(ChatColor.DARK_PURPLE + "Something is watching you...");
                count++;
            }
            p.sendMessage(ChatColor.DARK_PURPLE + "Night Terror cast on " + count + " player(s).");
        }

        private void nightmarePunish(Player p) {
            if (!ready(p, "g_punish", 60000)) return;
            nightmareUntil.put(p.getUniqueId(), System.currentTimeMillis() + 5000);
            p.sendTitle(ChatColor.DARK_RED + "NIGHTMARE", ChatColor.RED + "Punishment active for 5 seconds", 5, 80, 10);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WARDEN_ROAR, 2f, 0.6f);
        }

        private void abyssalSight(Player p) {
            if (!ready(p, "g_abyss", 90000)) return;
            int revealed = 0;
            for (Player v : Bukkit.getOnlinePlayers()) {
                if (v.equals(p) || isFriendlyTo(p, v)) continue;
                int surface = v.getWorld().getHighestBlockYAt(v.getLocation());
                if (v.getLocation().getBlockY() < surface - 4) {
                    v.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 0));
                    revealed++;
                }
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 0));
            p.sendMessage(ChatColor.DARK_PURPLE + "Abyssal Sight: " + revealed + " underground player(s) revealed.");
        }

        private void giantCreaking(Player p) {
            if (!ready(p, "g_creak", 180000)) return;
            Entity phantom = spawnCreaking(p.getLocation().add(2, 0, 2), EntityType.PHANTOM, p.getUniqueId(), 2.0, "nmult");
            phantom.setCustomName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Nightmare Phantom");
            phantom.setCustomNameVisible(true);
            p.sendMessage(ChatColor.DARK_PURPLE + "A Nightmare Phantom follows you for 120 seconds.");
            final int[] runs = {0};
            final BukkitTask[] holder = new BukkitTask[1];
            holder[0] = gemTimer(p, () -> {
                runs[0]++;
                if (!phantom.isValid()) {
                    holder[0].cancel();
                    return;
                }
                if (runs[0] > 24) {
                    phantom.remove();
                    p.sendMessage(ChatColor.DARK_GRAY + "The Nightmare Phantom has faded.");
                    holder[0].cancel();
                    return;
                }
                phantom.getWorld().playSound(phantom.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, 2f, 0.5f);
                phantom.getWorld().spawnParticle(Particle.SMOKE, phantom.getLocation().add(0, 1, 0), 15, 0.6, 0.6, 0.6, 0.02);
            }, 0L, 100L);
        }

        private void tickEcho() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!holdsItem(p, "nightmare")) continue;
                for (Entity en : p.getWorld().getNearbyEntities(p.getLocation(), 5, 5, 5)) {
                    if (!(en instanceof Player hidden)) continue;
                    if (hidden.equals(p) || isFriendlyTo(p, hidden)) continue;
                    if (!hidden.isSneaking() && !hidden.hasPotionEffect(PotionEffectType.INVISIBILITY)) continue;
                    double d = hidden.getLocation().distance(p.getLocation());
                    hidden.getWorld().spawnParticle(Particle.SCULK_SOUL, hidden.getLocation().add(0, 1, 0), 8, 0.3, 0.5, 0.3, 0.02);
                }
            }
        }

        @EventHandler
        public void onGemTame(PlayerInteractEntityEvent e) {
            if (e.getHand() != EquipmentSlot.HAND) return;
            Player p = e.getPlayer();
            ItemStack main = p.getInventory().getItemInMainHand();
            if (main.getType() != Material.ECHO_SHARD || !holdsItem(p, "nightmare")) return;
            Entity target = e.getRightClicked();
            boolean tameable = target instanceof Bat || target.getType().name().equals("CREAKING");
            if (!tameable) return;
            e.setCancelled(true);
            if (ownerOf(target) != null) {
                p.sendMessage(ChatColor.RED + "It's already tamed.");
                return;
            }
            if (main.getAmount() <= 1) p.getInventory().setItemInMainHand(null);
            else main.setAmount(main.getAmount() - 1);
            setOwner(target, p.getUniqueId());
            addFollower(target);
            p.sendMessage(ChatColor.DARK_PURPLE + "Fed an Echo Shard. It will follow you now.");
        }

        // ----- Undead Legion -----

        private void witherLegion(Player p) {
            if (!ready(p, "g_legion", 60000)) return;
            World w = p.getWorld();
            int n = 3 + rnd.nextInt(5);
            for (int i = 0; i < n; i++) {
                Location loc = p.getLocation().add(rand(-3, 3), 0, rand(-3, 3));
                WitherSkeleton ws = w.spawn(loc, WitherSkeleton.class);
                setOwner(ws, p.getUniqueId());
                addFollower(ws);
                ws.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1200, 1));
                ws.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 1200, 1));
                Bukkit.getScheduler().runTaskLater(host, () -> {
                    if (ws.isValid()) ws.remove();
                }, 1200L);
            }
            p.sendMessage(ChatColor.DARK_GREEN + "" + n + " buffed wither skeletons summoned for 60 seconds.");
        }

        private void fallenRecall(Player p) {
            Deque<EntityType> kills = lastKills.get(p.getUniqueId());
            if (kills == null || kills.isEmpty()) {
                p.sendMessage(ChatColor.RED + "You have no recent kills to recall.");
                return;
            }
            if (!ready(p, "g_recall", 60000)) return;
            World w = p.getWorld();
            int summoned = 0;
            for (EntityType type : kills) {
                if (!type.isAlive() || NO_SUMMON.contains(type)) continue;
                try {
                    Entity m = w.spawnEntity(p.getLocation().add(rand(-3, 3), 0, rand(-3, 3)), type);
                    setOwner(m, p.getUniqueId());
                    addFollower(m);
                    summoned++;
                    Bukkit.getScheduler().runTaskLater(host, () -> {
                        if (m.isValid()) m.remove();
                    }, 1200L);
                } catch (IllegalArgumentException ignored) {
                    // type can't be spawned on this server
                }
            }
            p.sendMessage(ChatColor.DARK_GREEN + "Recalled " + summoned + " fallen mob(s) for 60 seconds.");
        }

        private void witherTouch(Player p) {
            if (!ready(p, "g_touch", 40000)) return;
            witherTouchUntil.put(p.getUniqueId(), System.currentTimeMillis() + 20000);
            p.sendMessage(ChatColor.DARK_GREEN + "Wither Touch active for 20 seconds.");
        }

        private void giantWither(Player p) {
            if (!ready(p, "g_giant", 300000)) return;
            World w = p.getWorld();
            WitherSkeleton big = w.spawn(p.getLocation().add(2, 0, 2), WitherSkeleton.class);
            setOwner(big, p.getUniqueId());
            big.getPersistentDataContainer().set(ultKey, PersistentDataType.STRING, "undeadult");
            big.setCustomName(ChatColor.BLACK + "" + ChatColor.BOLD + "Hollow Colossus");
            big.setCustomNameVisible(true);
            AttributeInstance sc = big.getAttribute(Attribute.GENERIC_SCALE);
            if (sc != null) sc.setBaseValue(0.7);
            AttributeInstance mh = big.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (mh != null) {
                mh.setBaseValue(400);
                big.setHealth(400);
            }
            AttributeInstance ad = big.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            if (ad != null) {
                ad.setBaseValue(16);
            }
            Bukkit.getScheduler().runTaskLater(host, () -> {
                if (big.isValid()) big.remove();
            }, 2400L);
            p.sendMessage(ChatColor.DARK_GREEN + "The Hollow Colossus rises for 120 seconds.");
        }

        @EventHandler
        public void onKillTrack(EntityDeathEvent e) {
            LivingEntity dead = e.getEntity();
            if (dead instanceof Player || NO_SUMMON.contains(dead.getType())) return;
            Player killer = dead.getKiller();
            if (killer == null) return;
            Deque<EntityType> q = lastKills.computeIfAbsent(killer.getUniqueId(), k -> new ArrayDeque<>());
            q.addFirst(dead.getType());
            while (q.size() > 3) q.removeLast();
        }

        // ----- Gem damage rules: trust, passives, and effects -----

        @EventHandler
        public void onGemDamage(EntityDamageByEntityEvent e) {
            Entity damager = e.getDamager();
            Entity victim = e.getEntity();

            // Trust: your abilities, mobs, projectiles and lightning never hurt you or your trusted players.
            if (isFriendlyTo(damager, victim)) {
                e.setCancelled(true);
                return;
            }
            if ("nmult".equals(victim.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING))) {
                EntityType ct = EntityType.PHANTOM;
                UUID own = ownerOf(victim);
                Location at = victim.getLocation();
                if (ct != null && own != null) {
                    Bukkit.getScheduler().runTaskLater(host, () -> {
                        for (int i = 0; i < 2; i++) {
                            spawnCreaking(at.clone().add(rand(-1.5, 1.5), 0, rand(-1.5, 1.5)), ct, own, 0.4, "nmchild");
                        }
                    }, 1L);
                }
            }
            if (!(victim instanceof LivingEntity target)) return;
            Player owner = null;
            if (damager instanceof Player op) owner = op;
            else if (damager instanceof Projectile shot && shot.getShooter() instanceof Player sp) owner = sp;
            if (owner != null) {
                for (UUID fid : followers) {
                    Entity f = Bukkit.getEntity(fid);
                    if (f instanceof Mob m && m.isValid() && owner.getUniqueId().equals(ownerOf(f))) m.setTarget(target);
                }
            }

            if (damager instanceof Player attacker) {
                if (holdsItem(attacker, "undead") && isUndead(target)) {
                    e.setDamage(e.getDamage() * 1.5);
                    attacker.setHealth(Math.min(attacker.getMaxHealth(), attacker.getHealth() + 2));
                }
                if (isActive(witherTouchUntil, attacker)) {
                    applyKnownEffects(target,
                            new PotionEffect(PotionEffectType.WITHER, 100 + rnd.nextInt(61), 0),
                            new PotionEffect(PotionEffectType.SLOWNESS, 100, 1));
                }
            }

            // Giant wither skeleton hits
            String tag = damager.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING);
            if ("undeadult".equals(tag)) {
                applyKnownEffects(target,
                        new PotionEffect(PotionEffectType.SLOWNESS, 100, 1),
                        new PotionEffect(PotionEffectType.POISON, 100, 0),
                        new PotionEffect(PotionEffectType.NAUSEA, 100, 0),
                        new PotionEffect(PotionEffectType.WITHER, 100, 0));
            }

            // Nightmare Punishment: anyone who hits a guarded user gets punished.
            if (victim instanceof Player guarded && isActive(nightmareUntil, guarded)) {
                LivingEntity attackerLiving = null;
                if (damager instanceof LivingEntity la) attackerLiving = la;
                else if (damager instanceof Projectile pj && pj.getShooter() instanceof LivingEntity sl) attackerLiving = sl;
                if (attackerLiving != null && !attackerLiving.equals(guarded)) {
                    applyKnownEffects(attackerLiving,
                            new PotionEffect(PotionEffectType.DARKNESS, 100, 0),
                            new PotionEffect(PotionEffectType.BLINDNESS, 100, 0),
                            new PotionEffect(PotionEffectType.NAUSEA, 100, 0),
                            new PotionEffect(PotionEffectType.WITHER, 100, 0),
                            new PotionEffect(PotionEffectType.SLOWNESS, 100, 1));
                    if (attackerLiving instanceof Player ap) damageArmor(ap, 30);
                }
            }
        }

        @EventHandler
        public void onMinionTarget(EntityTargetLivingEntityEvent e) {
            UUID owner = ownerOf(e.getEntity());
            if (owner == null) return;
            if (!(e.getTarget() instanceof Player targetPlayer)) return;
            if (targetPlayer.getUniqueId().equals(owner) || isTrusted(owner, targetPlayer.getUniqueId())) {
                e.setCancelled(true);
            }
        }

        // ----- Gem timers -----

        private Color colorFor(String id) {
            if (id.equals("reaperskull")) return Color.fromRGB(0x2962FF);
            ItemDef d = items.get(id);
            if (d == null) d = gems.get(id);
            if (d == null) return Color.WHITE;
            return Color.fromRGB(Integer.parseInt(d.hex().substring(1), 16));
        }

        // Every projectile we fire leaves a trail in its item's colour.
        private void tickTrails() {
            for (World w : Bukkit.getWorlds()) {
                for (Projectile pr : w.getEntitiesByClass(Projectile.class)) {
                    String tag = pr.getPersistentDataContainer().get(projKey, PersistentDataType.STRING);
                    if (tag == null) continue;
                    w.spawnParticle(Particle.DUST, pr.getLocation(), 3, 0.1, 0.1, 0.1, 0, new Particle.DustOptions(colorFor(tag), 1.2f));
                    if (tag.equals("voidslash")) voidSlashTick(pr);
                }
            }
        }

        private void voidSlashTick(Projectile pr) {
            Set<UUID> already = voidHits.computeIfAbsent(pr.getUniqueId(), k -> new HashSet<>());
            Entity shooter = pr.getShooter() instanceof Entity se ? se : null;
            for (Entity en : pr.getNearbyEntities(2, 2, 2)) {
                if (!(en instanceof LivingEntity le) || en.equals(shooter) || already.contains(le.getUniqueId())) continue;
                if (isFriendlyTo(shooter, le)) continue;
                already.add(le.getUniqueId());
                hit(le, 8, shooter);
            }
            if (!pr.isValid()) voidHits.remove(pr.getUniqueId());
        }

        // Hitting something with one of our items sends out a spark in that item's colour.
        @EventHandler
        public void onItemSpark(EntityDamageByEntityEvent e) {
            if (e.isCancelled() || !(e.getDamager() instanceof Player p)) return;
            if (!(e.getEntity() instanceof LivingEntity victim)) return;
            String id = idOf(p.getInventory().getItemInMainHand());
            if (id == null) return;
            victim.getWorld().spawnParticle(Particle.DUST, victim.getLocation().add(0, 1, 0), 14, 0.3, 0.4, 0.3, 0,
                    new Particle.DustOptions(colorFor(id), 1.4f));
        }

        private void tickGems() {
            gemTick++;
            if (gemTick % 2 == 0) tickTrails();
            if (gemTick % 4 == 0) tickGemAuraAll();
            if (gemTick % 20 == 0) tickPosHistory();
            tickBlizzardPassive();
            if (gemTick % 5 == 0) tickBarriers();
            if (gemTick % 40 == 0) tickEcho();
            if (gemTick % 10 == 0) tickFollowers();
            if (gemTick % 40 == 0) tickAtomicRegen();
            if (gemTick % 40 == 0) tickYinyangRegen();
            if (gemTick % 10 == 0) tickFollowerTargets();
            if (gemTick % 5 == 0) tickThunderAura();
            if (gemTick % 5 == 0) tickGemAuras();
            if (gemTick % 5 == 0) tickEclipseAura();
            if (gemTick % 5 == 0) cleanupGemTasks();
        }

        // ===================== ATOMIC =====================

        private void atomicBlast(Player p) {
            if (!ready(p, "g_atomic", 8000)) return;
            Location c = p.getLocation();
            damageArea(c, 6, 12, p);
            knockArea(c, 6, 0.6, p);
            p.getWorld().playSound(c, Sound.ENTITY_GENERIC_EXPLODE, 2f, 1.4f);
            for (int r = 2; r <= 6; r += 2) {
                final double radius = r;
                Bukkit.getScheduler().runTaskLater(host, () -> particleRing(c.clone().add(0, 0.3, 0), radius, Particle.HAPPY_VILLAGER), r / 2L);
            }
            p.sendMessage(ChatColor.GREEN + "Atomic Blast!");
        }

        private void radiationCloud(Player p) {
            if (!ready(p, "g_cloud", 30000)) return;
            p.sendMessage(ChatColor.GREEN + "Radiation Cloud for 15 seconds.");
            final int[] runs = {0};
            final BukkitTask[] holder = new BukkitTask[1];
            holder[0] = gemTimer(p, () -> {
                runs[0]++;
                if (runs[0] > 15 || !p.isOnline()) {
                    holder[0].cancel();
                    return;
                }
                Location c = p.getLocation();
                p.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, c.clone().add(0, 1, 0), 25, 3, 1, 3, 0);
                for (Entity en : p.getWorld().getNearbyEntities(c, 6, 3, 6)) {
                    if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                    applyKnownEffects(le,
                            new PotionEffect(PotionEffectType.WITHER, 60, 0),
                            new PotionEffect(PotionEffectType.NAUSEA, 60, 0));
                }
            }, 0L, 20L);
        }

        private void falloutShield(Player p) {
            if (!ready(p, "g_shield", 25000)) return;
            shieldUntil.put(p.getUniqueId(), System.currentTimeMillis() + 10000);
            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.5f, 1.6f);
            p.sendMessage(ChatColor.GREEN + "Fallout Shield up for 10 seconds.");
        }

        private void meltdown(Player p) {
            if (!ready(p, "g_meltdown", 300000)) return;
            meltdownUntil.put(p.getUniqueId(), System.currentTimeMillis() + 20000);
            p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 400, 0));
            p.sendTitle(ChatColor.GREEN + "MELTDOWN", ChatColor.WHITE + "Radiation is spreading", 5, 60, 10);
            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 2f, 0.8f);
            final int[] runs = {0};
            final BukkitTask[] holder = new BukkitTask[1];
            holder[0] = gemTimer(p, () -> {
                runs[0]++;
                if (runs[0] > 20 || !p.isOnline()) {
                    meltdownUntil.remove(p.getUniqueId());
                    holder[0].cancel();
                    return;
                }
                Location c = p.getLocation();
                particleRing(c.clone().add(0, 0.2, 0), 10, Particle.HAPPY_VILLAGER);
                for (Entity en : p.getWorld().getNearbyEntities(c, 10, 5, 10)) {
                    if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                    hit(le, 3, p);
                    if (le instanceof Player victim) {
                        if (runs[0] % 5 == 1) victim.sendMessage(ChatColor.GREEN + "You are being melted from the radiation");
                    }
                }
            }, 0L, 20L);
        }

        @EventHandler
        public void onAtomicDefense(EntityDamageEvent e) {
            if (!(e.getEntity() instanceof Player p) || !isActive(shieldUntil, p) || !holdsItem(p, "atomic")) return;
            e.setDamage(e.getDamage() * 0.5);
        }

        @EventHandler
        public void onAtomicBlindAttackers(EntityDamageByEntityEvent e) {
            if (!(e.getEntity() instanceof Player p) || !isActive(shieldUntil, p) || !holdsItem(p, "atomic")) return;
            if (e.getDamager() instanceof LivingEntity attacker && !attacker.equals(p)) {
                attacker.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
            }
        }

        @EventHandler
        public void onAtomicImmune(EntityPotionEffectEvent e) {
            if (!(e.getEntity() instanceof Player p) || !holdsItem(p, "atomic")) return;
            if (e.getAction() != EntityPotionEffectEvent.Action.ADDED
                    && e.getAction() != EntityPotionEffectEvent.Action.CHANGED) return;
            PotionEffect incoming = e.getNewEffect();
            if (incoming == null) return;
            if (incoming.getType().equals(PotionEffectType.WITHER) || incoming.getType().equals(PotionEffectType.POISON)) {
                e.setCancelled(true);
            }
        }

        // ===================== WIND MASTER =====================

        private void gust(Player p) {
            if (!ready(p, "g_gust", 5000)) return;
            Location c = p.getLocation();
            World w = p.getWorld();
            for (Entity en : w.getNearbyEntities(c, 6, 3, 6)) {
                if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                Vector out = le.getLocation().toVector().subtract(c.toVector()).setY(0);
                if (out.lengthSquared() == 0) out = new Vector(0, 0, 1);
                le.setVelocity(out.normalize().multiply(2.0).setY(0.5));
                hit(le, 6, p);
            }
            for (double r = 2; r <= 6; r += 2) particleRing(c.clone().add(0, 1, 0), r, Particle.CLOUD);
            w.playSound(c, Sound.ENTITY_BREEZE_WIND_BURST, 2f, 1.2f);
            p.sendMessage(ChatColor.AQUA + "Gust!");
        }

        private void tornado(Player p) {
            if (!ready(p, "g_tornado", 20000)) return;
            World w = p.getWorld();
            Location center = p.getLocation();
            List<LivingEntity> victims = new ArrayList<>();
            for (Entity en : w.getNearbyEntities(center, 12, 6, 12)) {
                if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                if (!(le instanceof Player) && NO_SUMMON.contains(le.getType())) continue;
                victims.add(le);
            }
            if (victims.isEmpty()) {
                p.sendMessage(ChatColor.RED + "No enemies within 12 blocks.");
                return;
            }
            final double[] spin = {0};
            final int[] runs = {0};
            final BukkitTask[] holder = new BukkitTask[1];
            holder[0] = gemTimer(p, () -> {
                runs[0]++;
                spin[0] += 0.3;
                if (runs[0] > 100 || !p.isOnline()) {
                    holder[0].cancel();
                    return;
                }
                for (int i = 0; i < victims.size(); i++) {
                    LivingEntity v = victims.get(i);
                    if (!v.isValid() || v.isDead()) continue;
                    double a = spin[0] + i * (2 * Math.PI / victims.size());
                    double h = 2.5 + Math.sin(spin[0] * 2) * 1.2;
                    Location orbit = center.clone().add(Math.cos(a) * 4, h, Math.sin(a) * 4);
                    if (v instanceof Player pv) {
                        Vector pull = orbit.toVector().subtract(pv.getLocation().toVector());
                        if (pull.length() > 2) pull = pull.normalize().multiply(2);
                        pv.setVelocity(pull.multiply(0.5));
                        guard(pv, 20);
                    } else {
                        v.teleport(orbit);
                        v.setVelocity(new Vector(0, 0, 0));
                    }
                    v.setFallDistance(0f);
                }
                for (int k = 0; k < 3; k++) {
                    double a = spin[0] * 1.5 + k * (2 * Math.PI / 3);
                    w.spawnParticle(Particle.CLOUD, center.clone().add(Math.cos(a) * 3, (runs[0] % 20) * 0.3, Math.sin(a) * 3), 2, 0.1, 0.1, 0.1, 0.02);
                }
            }, 0L, 1L);
            w.playSound(center, Sound.ENTITY_BREEZE_WIND_BURST, 2f, 0.8f);
            p.sendMessage(ChatColor.AQUA + "Tornado: " + victims.size() + " target(s) caught for 5 seconds.");
        }

        private void updraft(Player p) {
            if (!ready(p, "g_updraft", 15000)) return;
            p.setVelocity(new Vector(0, 2.2, 0));
            p.setFallDistance(0f);
            guard(p, 120);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 120, 0));
            Location c = p.getLocation();
            particleRing(c.clone().add(0, 0.2, 0), 1.5, Particle.CLOUD);
            p.getWorld().playSound(c, Sound.ENTITY_BREEZE_SHOOT, 2f, 0.8f);
            p.sendMessage(ChatColor.AQUA + "Updraft!");
        }

        private void jumpPad(Player p) {
            if (!ready(p, "g_pad", 30000)) return;
            Block target = p.getTargetBlockExact(6);
            if (target == null) {
                p.sendMessage(ChatColor.RED + "Look at a block within 6 blocks.");
                return;
            }
            Block pad = target.getRelative(BlockFace.UP);
            if (!pad.getType().isAir()) {
                p.sendMessage(ChatColor.RED + "No space for the jump pad there.");
                return;
            }
            pad.setType(Material.SLIME_BLOCK);
            String key = keyOf(pad.getLocation());
            pads.put(key, p.getUniqueId());
            Bukkit.getScheduler().runTaskLater(host, () -> {
                if (pads.remove(key) != null && pad.getType() == Material.SLIME_BLOCK) pad.setType(Material.AIR);
            }, 600L);
            p.getWorld().playSound(pad.getLocation(), Sound.BLOCK_SLIME_BLOCK_PLACE, 1f, 1f);
            p.sendMessage(ChatColor.GREEN + "Jump pad placed for 30 seconds.");
        }

        @EventHandler
        public void onPadBounce(PlayerMoveEvent e) {
            if (pads.isEmpty()) return;
            Player p = e.getPlayer();
            if (p.getVelocity().getY() >= 0) return;
            Block below = p.getLocation().subtract(0, 0.1, 0).getBlock();
            if (below.getType() != Material.SLIME_BLOCK) return;
            UUID owner = pads.get(keyOf(below.getLocation()));
            if (owner == null || !owner.equals(p.getUniqueId())) return;
            p.setVelocity(new Vector(p.getVelocity().getX(), 1.9, p.getVelocity().getZ()));
            guard(p, 40);
            p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 20, 0.4, 0.1, 0.4, 0.05);
        }

        private void windBurst(Player p) {
            if (!ready(p, "g_wburst", 40000)) return;
            Location eye = p.getEyeLocation();
            for (int i = 0; i < 8; i++) {
                double a = Math.toRadians(i * 45);
                Vector dir = new Vector(Math.cos(a), 0.25, Math.sin(a)).normalize();
                spawnWindCharge(eye.clone().add(dir.clone().multiply(1.5)), dir.multiply(1.1));
            }
            particleRing(p.getLocation().add(0, 1, 0), 3, Particle.CLOUD);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST, 2f, 1f);
        }

        private Entity spawnWindCharge(Location loc, Vector vel) {
            try {
                Entity wc = loc.getWorld().spawnEntity(loc, EntityType.valueOf("WIND_CHARGE"));
                wc.setVelocity(vel);
                return wc;
            } catch (IllegalArgumentException ex) {
                return null;
            }
        }

        private void windBerserk(Player p) {
            if (!ready(p, "g_breeze", 300000)) return;
            windBerserkUntil.put(p.getUniqueId(), System.currentTimeMillis() + 30000);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 1));
            p.sendTitle(ChatColor.AQUA + "BERSERK BREEZE", ChatColor.WHITE + "Gusts on every right-click for 30 seconds", 5, 60, 10);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 2f, 0.6f);
        }

        private void fireGust(Player p) {
            Vector look = p.getEyeLocation().getDirection().normalize();
            Snowball g = p.launchProjectile(Snowball.class, look.multiply(1.8));
            g.getPersistentDataContainer().set(projKey, PersistentDataType.STRING, "gust");
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BREEZE_SHOOT, 1f, 1.2f);
        }

        @EventHandler
        public void onWindBow(EntityShootBowEvent e) {
            if (!(e.getEntity() instanceof Player p) || !holdsItem(p, "wind")) return;
            if (e.getProjectile() instanceof AbstractArrow arrow) {
                arrow.setKnockbackStrength(arrow.getKnockbackStrength() + 2);
                arrow.setDamage(arrow.getDamage() * 1.25 + 1);
            }
        }

        @EventHandler
        public void onWindHitLaunch(EntityDamageByEntityEvent e) {
            if (e.isCancelled() || inThunderProc) return;
            if (!(e.getDamager() instanceof Player attacker) || !holdsItem(attacker, "wind")) return;
            if (!(e.getEntity() instanceof Player victim)) return;
            if (rnd.nextDouble() < 0.05) victim.setVelocity(new Vector(0, 1.3, 0));
        }

        // ===================== INFERNO =====================

        private final Map<UUID, Long> meteorOwners = new HashMap<>();

        /** Which projectile tag each crossbow or bow gives its shots. */
        private static final Map<String, String> SHOT_TAGS = Map.ofEntries(
                Map.entry("hailstorm", "hailbolt"),
                Map.entry("sparkline", "sparkbolt"),
                Map.entry("ravenshot", "ravenbolt"),
                Map.entry("whirlwind", "whirl"),
                Map.entry("sunfall", "sunfall"),
                Map.entry("gravebow", "gravepull"),
                Map.entry("ironfang", "ironbolt"),
                Map.entry("pyre", "pyrebolt"),
                Map.entry("echo", "echobolt"),
                Map.entry("stormbow", "stormbolt"),
                Map.entry("chainshot", "chainbolt"),
                Map.entry("ghostmark", "ghostmark"),
                Map.entry("dawnbow", "dawnbolt"),
                Map.entry("frostbolt", "frostbolt"),
                Map.entry("thunderbolt", "crossthunder"),
                Map.entry("rocketbolt", "rocketbolt"),
                Map.entry("vinebow", "vinebow"),
                Map.entry("gale", "gale"),
                Map.entry("ironbow", "ironbow"));

        /** Spins a player like a tornado for 3 seconds: they rotate, lift and take light damage. */
        private void startTornado(Player victim) {
            final int[] ticks = {0};
            Bukkit.getScheduler().runTaskTimer(host, task -> {
                ticks[0]++;
                if (!victim.isOnline() || ticks[0] > 60) {
                    task.cancel();
                    return;
                }
                victim.setRotation(victim.getLocation().getYaw() + 35f, 0f);
                if (ticks[0] <= 25) victim.setVelocity(new Vector(0, 0.35, 0));
                if (ticks[0] % 10 == 0) victim.damage(1.0);
                victim.getWorld().spawnParticle(Particle.CLOUD, victim.getLocation().add(0, 1, 0), 6, 0.4, 0.5, 0.4, 0.02);
            }, 0L, 1L);
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onMeteorSelfHarm(EntityDamageEvent e) {
            if (!(e.getEntity() instanceof Player victim)) return;
            Long cast = meteorOwners.get(victim.getUniqueId());
            if (cast == null || System.currentTimeMillis() - cast > 20000L) return;
            EntityDamageEvent.DamageCause cause = e.getCause();
            if (cause != EntityDamageEvent.DamageCause.MAGIC
                    && cause != EntityDamageEvent.DamageCause.PROJECTILE
                    && cause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return;
            if (e instanceof EntityDamageByEntityEvent de
                    && !(de.getDamager() instanceof DragonFireball)
                    && !(de.getDamager() instanceof Fireball)) return;
            e.setCancelled(true);
        }

        private void meteorShower(Player p) {
            if (!ready(p, "g_meteor", 20000)) return;
            Location aim = aimPoint(p, 25);
            World w = p.getWorld();
            for (int i = 0; i < 12; i++) {
                Bukkit.getScheduler().runTaskLater(host, () -> {
                    if (!p.isOnline()) return;
                    Location from = aim.clone().add(rand(-4, 4), 18, rand(-4, 4));
                    Fireball f = w.spawn(from, Fireball.class);
                    f.setDirection(new Vector(0, -1, 0));
                    f.setYield(0f);
                    f.setIsIncendiary(false);
                    f.setShooter(p);
                    f.getPersistentDataContainer().set(projKey, PersistentDataType.STRING, "meteor");
                }, i * 5L);
            }
            p.sendMessage(ChatColor.GOLD + "Meteor Shower!");
        }

        private void campfire(Player p) {
            if (!ready(p, "g_campfire", 40000)) return;
            p.sendMessage(ChatColor.GOLD + "Cozy Campfire lit for 15 seconds.");
            final int[] runs = {0};
            final BukkitTask[] holder = new BukkitTask[1];
            holder[0] = gemTimer(p, () -> {
                runs[0]++;
                if (runs[0] > 15 || !p.isOnline()) {
                    holder[0].cancel();
                    return;
                }
                p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + 2));
                Location c = p.getLocation().add(0, 1, 0);
                p.getWorld().spawnParticle(Particle.FLAME, c, 4, 0.5, 0.2, 0.5, 0.01);
                p.getWorld().spawnParticle(Particle.SMOKE, c.clone().add(0, 1, 0), 3, 0.3, 0.3, 0.3, 0.01);
                for (Entity en : p.getWorld().getNearbyEntities(c, 5, 3, 5)) {
                    if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                    hit(le, 2, p);
                }
            }, 0L, 20L);
        }

        private void blazeMode(Player p) {
            if (!ready(p, "g_blaze", 300000)) return;
            blazeUntil.put(p.getUniqueId(), System.currentTimeMillis() + 30000);
            p.sendTitle(ChatColor.GOLD + "BLAZE", ChatColor.YELLOW + "Explosive fireballs, no cooldown", 5, 60, 10);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 2f, 0.6f);
        }

        private void fireBlazeBall(Player p) {
            Fireball f = p.launchProjectile(Fireball.class);
            f.setYield(0f);
            f.setIsIncendiary(true);
            f.getPersistentDataContainer().set(projKey, PersistentDataType.STRING, "blazeball");
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1f, 1.2f);
        }

        private void chargeFireball(Player p) {
            UUID id = p.getUniqueId();
            if (charging.containsKey(id)) {
                releaseCharge(p);
                return;
            }
            if (!ready(p, "g_charge", 3000)) return;
            double[] state = {0, 1}; // [charge percent, direction: 1 rising, -1 draining]
            charging.put(id, state);
            p.sendMessage(ChatColor.YELLOW + "Charging. Press F to release.");
            final BukkitTask[] holder = new BukkitTask[1];
            holder[0] = gemTimer(p, () -> {
                if (!charging.containsKey(id) || !p.isOnline()) {
                    holder[0].cancel();
                    return;
                }
                double[] s = charging.get(id);
                if (s[1] > 0) {
                    s[0] += 2;
                    if (s[0] >= 100) {
                        s[0] = 100;
                        s[1] = -1;
                    }
                } else {
                    s[0] -= 1;
                    if (s[0] <= 0) {
                        charging.remove(id);
                        p.sendActionBar(ChatColor.RED + "Charge destroyed.");
                        holder[0].cancel();
                        return;
                    }
                }
                p.sendActionBar(ChatColor.GOLD + "Charge: " + (int) s[0] + "%");
                Location eye = p.getEyeLocation();
                p.getWorld().spawnParticle(Particle.FLAME, eye.clone().add(eye.getDirection()),
                        2 + (int) (s[0] / 25), 0.2, 0.2, 0.2, 0.01);
            }, 0L, 1L);
        }

        private void releaseCharge(Player p) {
            double[] s = charging.remove(p.getUniqueId());
            if (s == null) return;
            int lvl = s[0] < 34 ? 1 : (s[0] < 67 ? 2 : 3);
            Fireball f = p.launchProjectile(Fireball.class);
            f.setYield(0f);
            f.setIsIncendiary(lvl >= 3);
            f.getPersistentDataContainer().set(projKey, PersistentDataType.STRING, "charge" + lvl);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1.5f);
            p.sendMessage(ChatColor.GOLD + "Released at " + (int) s[0] + "%.");
        }

        private void toggleEvaporation(Player p) {
            UUID id = p.getUniqueId();
            if (evapOn.remove(id)) {
                restoreWater(id);
                p.sendMessage(ChatColor.GRAY + "Evaporation off. Water restored.");
            } else {
                evapOn.add(id);
                p.sendMessage(ChatColor.AQUA + "Evaporation on. Water around you evaporates.");
            }
        }

        private void restoreWater(UUID id) {
            Map<Block, Material> saved = evapSaved.remove(id);
            if (saved == null) return;
            for (Map.Entry<Block, Material> en : saved.entrySet()) {
                if (en.getKey().getType().isAir()) en.getKey().setType(en.getValue());
            }
        }

        private void tickEvaporation() {
            for (UUID id : evapOn) {
                Player p = Bukkit.getPlayer(id);
                if (p == null || !p.isOnline()) continue;
                Location c = p.getLocation();
                Map<Block, Material> saved = evapSaved.computeIfAbsent(id, k -> new HashMap<>());
                for (int x = -4; x <= 4; x++) {
                    for (int y = -2; y <= 4; y++) {
                        for (int z = -4; z <= 4; z++) {
                            Block b = c.clone().add(x, y, z).getBlock();
                            if (b.getType() == Material.WATER) {
                                saved.put(b, Material.WATER);
                                b.setType(Material.AIR);
                            }
                        }
                    }
                }
                p.getWorld().spawnParticle(Particle.CLOUD, c.clone().add(0, 0.1, 0), 4, 0.5, 0.05, 0.5, 0.01);
            }
        }

        @EventHandler
        public void onInfernoFireImmune(EntityDamageEvent e) {
            if (!(e.getEntity() instanceof Player p) || !holdsItem(p, "inferno")) return;
            EntityDamageEvent.DamageCause c = e.getCause();
            if (c == EntityDamageEvent.DamageCause.FIRE || c == EntityDamageEvent.DamageCause.FIRE_TICK
                    || c == EntityDamageEvent.DamageCause.LAVA || c == EntityDamageEvent.DamageCause.HOT_FLOOR) {
                e.setCancelled(true);
            }
        }

        @EventHandler
        public void onInfernoFireAspect(EntityDamageByEntityEvent e) {
            if (e.isCancelled() || !(e.getDamager() instanceof Player p) || !holdsItem(p, "inferno")) return;
            if (!p.getInventory().getItemInMainHand().getType().name().endsWith("_SWORD")) return;
            if (e.getEntity() instanceof LivingEntity le) le.setFireTicks(Math.max(le.getFireTicks(), 160));
        }

        private void tickGemAuras() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (holdsItem(p, "wind")) {
                    double base = gemTick * 0.3;
                    for (int k = 0; k < 3; k++) {
                        double ang = base + k * 2 * Math.PI / 3;
                        p.getWorld().spawnParticle(Particle.CLOUD,
                                p.getLocation().add(Math.cos(ang) * 1.1, 0.3 + k * 0.6, Math.sin(ang) * 1.1), 1, 0, 0, 0, 0);
                    }
                }
                if (holdsItem(p, "inferno")) {
                    double a = gemTick * 0.25;
                    p.getWorld().spawnParticle(Particle.FLAME,
                            p.getLocation().add(Math.cos(a) * 0.9, 0.5, Math.sin(a) * 0.9), 1, 0, 0.05, 0, 0.01);
                    p.getWorld().spawnParticle(Particle.SMOKE,
                            p.getLocation().add(-Math.cos(a) * 0.9, 1.4, -Math.sin(a) * 0.9), 1, 0, 0, 0, 0.01);
                }
            }
        }

        private void tickAtomicRegen() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (holdsItem(p, "atomic") && p.getHealth() < p.getMaxHealth()) {
                    p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + 1));
                }
            }
        }

        // ===================== WARDEN KEEPER =====================

        private void sonicWave(Player p) {
            if (!ready(p, "g_sonic", 10000)) return;
            World w = p.getWorld();
            Location eye = p.getEyeLocation();
            Vector fwd = eye.getDirection().normalize();
            w.playSound(p.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 3f, 1f);
            Set<UUID> hitIds = new HashSet<>();
            for (int step = 1; step <= 20; step++) {
                final int s = step;
                Bukkit.getScheduler().runTaskLater(host, () -> {
                    Location front = eye.clone().add(fwd.clone().multiply(s * 1.2));
                    w.spawnParticle(Particle.SONIC_BOOM, front, 1, 0, 0, 0, 0);
                    for (Entity en : w.getNearbyEntities(front, 1.8, 1.8, 1.8)) {
                        if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le) || hitIds.contains(en.getUniqueId())) continue;
                        hitIds.add(en.getUniqueId());
                        hit(le, 40, p);
                        le.setVelocity(fwd.clone().multiply(1.6).setY(0.5));
                    }
                }, step);
            }
        }

        private void echoPulse(Player p) {
            if (!ready(p, "g_echo", 20000)) return;
            int n = 0;
            for (Entity en : p.getWorld().getNearbyEntities(p.getLocation(), 20, 20, 20)) {
                if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                le.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 0));
                hit(le, 2, p);
                n++;
            }
            particleRing(p.getLocation().add(0, 0.2, 0), 6, Particle.SCULK_SOUL);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, 2f, 1f);
            p.sendMessage(ChatColor.DARK_AQUA + "Echo Pulse revealed " + n + " target(s).");
        }

        private void wardenFist(Player p) {
            if (!ready(p, "g_fist", 15000)) return;
            World w = p.getWorld();
            Vector fwd = p.getLocation().getDirection().setY(0).normalize();
            Location spot = p.getLocation().add(fwd.clone().multiply(3));
            for (Entity en : w.getNearbyEntities(spot, 4, 3, 4)) {
                if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                hit(le, 30, p);
                le.setVelocity(fwd.clone().multiply(0.8).setY(0.8));
            }
            particleRing(spot.clone().add(0, 0.2, 0), 3, Particle.CLOUD);
            particleRing(spot.clone().add(0, 0.2, 0), 1.5, Particle.SCULK_SOUL);
            w.playSound(spot, Sound.ENTITY_WARDEN_ATTACK_IMPACT, 2f, 1f);
        }

        private void wardenMonster(Player p) {
            if (!ready(p, "g_wardenult", 300000)) return;
            World w = p.getWorld();
            Warden warden = w.spawn(p.getLocation().add(2, 0, 2), Warden.class);
            setOwner(warden, p.getUniqueId());
            warden.getPersistentDataContainer().set(ultKey, PersistentDataType.STRING, "wardenult");
            warden.setCustomName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Warden Monster");
            warden.setCustomNameVisible(true);
            addFollower(warden);
            final int[] runs = {0};
            final BukkitTask[] holder = new BukkitTask[1];
            holder[0] = gemTimer(p, () -> {
                runs[0]++;
                if (!warden.isValid() || runs[0] > 600 || !p.isOnline()) {
                    if (warden.isValid()) warden.remove();
                    holder[0].cancel();
                    return;
                }
                Location c = warden.getLocation().add(0, 1, 0);
                w.spawnParticle(Particle.SCULK_SOUL, c, 4, 0.4, 0.4, 0.4, 0.01);
                if (runs[0] % 4 == 0) {
                    LivingEntity target = nearestEnemy(warden.getLocation(), 16, p);
                    if (target != null) {
                        Vector span = target.getLocation().add(0, 1, 0).toVector().subtract(c.toVector());
                        for (int k = 1; k <= 6; k++) {
                            w.spawnParticle(Particle.SONIC_BOOM, c.clone().add(span.clone().multiply(k / 6.0)), 1, 0, 0, 0, 0);
                        }
                        hit(target, 20, p);
                        w.playSound(c, Sound.ENTITY_WARDEN_SONIC_BOOM, 2f, 1f);
                    }
                }
            }, 0L, 10L);
            p.sendMessage(ChatColor.DARK_AQUA + "Warden Monster summoned for 5 minutes.");
        }

        @EventHandler
        public void onWardenImmuneDamage(EntityDamageEvent e) {
            if (!(e.getEntity() instanceof Player p) || !holdsItem(p, "warden")) return;
            EntityDamageEvent.DamageCause c = e.getCause();
            if (c == EntityDamageEvent.DamageCause.FIRE || c == EntityDamageEvent.DamageCause.FIRE_TICK
                    || c == EntityDamageEvent.DamageCause.LAVA || c == EntityDamageEvent.DamageCause.HOT_FLOOR
                    || c == EntityDamageEvent.DamageCause.FALL) {
                e.setCancelled(true);
            }
        }

        @EventHandler
        public void onWardenImmuneEffects(EntityPotionEffectEvent e) {
            if (!(e.getEntity() instanceof Player p) || !holdsItem(p, "warden")) return;
            if (e.getAction() != EntityPotionEffectEvent.Action.ADDED
                    && e.getAction() != EntityPotionEffectEvent.Action.CHANGED) return;
            PotionEffect in = e.getNewEffect();
            if (in == null) return;
            PotionEffectType t = in.getType();
            if (t.equals(PotionEffectType.POISON) || t.equals(PotionEffectType.WITHER)
                    || t.equals(PotionEffectType.BLINDNESS) || t.equals(PotionEffectType.DARKNESS)
                    || t.equals(PotionEffectType.SLOWNESS) || t.equals(PotionEffectType.NAUSEA)) {
                e.setCancelled(true);
            }
        }

        // ===================== YIN YANG =====================

        private void yinYangRing(Location c, double r) {
            for (int deg = 0; deg < 360; deg += 15) {
                double rad = Math.toRadians(deg);
                Color col = deg < 180 ? Color.BLACK : Color.WHITE;
                c.getWorld().spawnParticle(Particle.DUST, c.clone().add(Math.cos(rad) * r, 0.2, Math.sin(rad) * r),
                        1, 0, 0, 0, 0, new Particle.DustOptions(col, 1.3f));
            }
        }

        private void yinStrike(Player p) {
            if (!ready(p, "g_yin", 8000)) return;
            p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + 8));
            Location c = p.getLocation();
            for (Entity en : c.getWorld().getNearbyEntities(c, 4, 3, 4)) {
                if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                hit(le, 8, p);
            }
            yinYangRing(c.clone().add(0, 0.2, 0), 4);
            p.sendMessage(ChatColor.WHITE + "Yin Strike!");
        }

        private void yangStrike(Player p) {
            if (!ready(p, "g_yang", 8000)) return;
            p.damage(4);
            Location c = p.getLocation();
            for (Entity en : c.getWorld().getNearbyEntities(c, 4, 3, 4)) {
                if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                hit(le, 8, p);
                le.setFireTicks(Math.max(le.getFireTicks(), 80));
            }
            particleRing(c.clone().add(0, 0.2, 0), 4, Particle.FLAME);
            p.sendMessage(ChatColor.RED + "Yang Strike!");
        }

        private void balance(Player p) {
            if (!ready(p, "g_balance", 30000)) return;
            LivingEntity target = nearestEnemy(p.getLocation(), 10, p);
            if (target == null) {
                p.sendMessage(ChatColor.RED + "No enemy within 10 blocks.");
                return;
            }
            double mine = p.getHealth();
            double theirs = target.getHealth();
            p.setHealth(Math.min(p.getMaxHealth(), theirs));
            target.setHealth(Math.min(target.getMaxHealth(), mine));
            p.sendMessage(ChatColor.GRAY + "Balance! Health swapped for 5 seconds.");
            Bukkit.getScheduler().runTaskLater(host, () -> {
                if (p.isOnline()) p.setHealth(Math.min(p.getMaxHealth(), mine));
                if (target.isValid()) target.setHealth(Math.min(target.getMaxHealth(), theirs));
            }, 100L);
        }

        private void taijitu(Player p) {
            if (!ready(p, "g_taijitu", 300000)) return;
            final int[] runs = {0};
            final BukkitTask[] holder = new BukkitTask[1];
            holder[0] = gemTimer(p, () -> {
                runs[0]++;
                if (runs[0] > 120 || !p.isOnline()) {
                    holder[0].cancel();
                    return;
                }
                Location c = p.getLocation();
                yinYangRing(c.clone().add(0, 0.2, 0), 6);
                for (Entity en : c.getWorld().getNearbyEntities(c, 6, 3, 6)) {
                    if (en.equals(p)) continue;
                    if (en instanceof Player pl && (pl.equals(p) || isTrusted(p.getUniqueId(), pl.getUniqueId()))) {
                        pl.setHealth(Math.min(pl.getMaxHealth(), pl.getHealth() + 1));
                        continue;
                    }
                    if (!(en instanceof LivingEntity le) || isFriendlyTo(p, le)) continue;
                    Vector out = le.getLocation().toVector().subtract(c.toVector()).setY(0);
                    if (out.lengthSquared() > 0) le.setVelocity(out.normalize().multiply(0.8).setY(0.2));
                }
            }, 0L, 10L);
            p.sendMessage(ChatColor.WHITE + "Taijitu: 60 seconds of balance.");
        }

        @EventHandler
        public void onYinHalfHeal(EntityDamageEvent e) {
            if (!(e.getEntity() instanceof Player p) || !holdsItem(p, "yinyang")) return;
            double heal = e.getFinalDamage() * 0.5;
            Bukkit.getScheduler().runTaskLater(host, () -> {
                if (p.isOnline() && !p.isDead()) p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + heal));
            }, 1L);
        }

        private void tickYinyangRegen() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (holdsItem(p, "yinyang") && p.getHealth() < p.getMaxHealth()) {
                    p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + 1));
                }
            }
        }

        // ===================== UNO REVERSE BLADE =====================

        private String prettyType(EntityType t) {
            return t.name().toLowerCase(Locale.ROOT).replace('_', ' ');
        }

        @EventHandler
        public void onUnoSave(EntityDamageByEntityEvent e) {
            if (!(e.getEntity() instanceof Player victim) || !(e.getDamager() instanceof Projectile pr)) return;
            if (!"unoreverse".equals(idOf(victim.getInventory().getItemInMainHand()))
                    && !"unoreverse".equals(idOf(victim.getInventory().getItemInOffHand()))) return;
            Deque<EntityType> store = unoStore.computeIfAbsent(victim.getUniqueId(), k -> new ArrayDeque<>());
            if (store.size() >= 30) {
                victim.sendMessage(ChatColor.RED + "You reached the projectile save limit (30). You can't save more projectiles.");
                return;
            }
            store.addLast(pr.getType());
            victim.sendMessage(ChatColor.AQUA + "Saved " + prettyType(pr.getType()) + " (" + store.size() + "/30)");
        }

        private void releaseProjectile(Player p) {
            Deque<EntityType> store = unoStore.get(p.getUniqueId());
            if (store == null || store.isEmpty()) {
                p.sendMessage(ChatColor.RED + "No saved projectiles.");
                return;
            }
            EntityType type = store.pollLast();
            Location eye = p.getEyeLocation();
            Vector dir = eye.getDirection().normalize();
            Entity shot;
            try {
                shot = p.getWorld().spawnEntity(eye.clone().add(dir.clone().multiply(1.2)), type);
            } catch (IllegalArgumentException ex) {
                p.sendMessage(ChatColor.RED + "Can't release that projectile.");
                return;
            }
            if (shot instanceof Projectile pr) pr.setShooter(p);
            shot.setVelocity(dir.clone().multiply(2.0));
            p.getWorld().playSound(eye, Sound.ITEM_TRIDENT_RIPTIDE_1, 1f, 1.2f);
            p.sendMessage(ChatColor.AQUA + "Reversed! Released " + prettyType(type) + " (" + store.size() + " left).");
        }

        // ===================== SOLAR ECLIPSE =====================

        private final Map<UUID, Long> umbralUntil = new HashMap<>();
        private final Map<UUID, UUID> deathMarkTarget = new HashMap<>();
        private final Map<UUID, Long> deathMarkUntil = new HashMap<>();
        private final Map<UUID, Deque<Location>> posHistory = new HashMap<>();
        private final Map<UUID, List<BukkitTask>> gemTasks = new HashMap<>();
        private final Set<UUID> debugOn = new HashSet<>();
        private final Map<String, Integer> modelIds = new HashMap<>();

        private void dbg(Player p, String msg) {
            if (debugOn.contains(p.getUniqueId())) p.sendMessage(ChatColor.DARK_GRAY + "[dbg] " + msg);
        }

        // Repeating gem ability task, tracked so it stops as soon as the player no longer holds a gem or the crown.
        private BukkitTask gemTimer(Player p, Runnable r, long delay, long period) {
            BukkitTask t = Bukkit.getScheduler().runTaskTimer(host, r, delay, period);
            gemTasks.computeIfAbsent(p.getUniqueId(), k -> new ArrayList<>()).add(t);
            return t;
        }

        private void cleanupGemTasks() {
            Iterator<Map.Entry<UUID, List<BukkitTask>>> it = gemTasks.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<UUID, List<BukkitTask>> en = it.next();
                Player p = Bukkit.getPlayer(en.getKey());
                boolean holding = p != null && (gemOf(p) != null || holdsItem(p, "cursedcrown"));
                if (!holding) {
                    for (BukkitTask t : en.getValue()) t.cancel();
                    it.remove();
                } else {
                    en.getValue().removeIf(BukkitTask::isCancelled);
                }
            }
        }
        private boolean reflectBusy = false;

        private void tickPosHistory() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                Deque<Location> q = posHistory.computeIfAbsent(p.getUniqueId(), k -> new ArrayDeque<>());
                q.addLast(p.getLocation().clone());
                while (q.size() > 10) q.pollFirst();
            }
        }

        // Yellow trail for anyone sprinting while wearing or holding the Cursed Crown.
        private void tickGemAuraAll() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                String g = gemOf(p);
                if (g == null || !gems.containsKey(g)) continue;
                Color c = colorFor(g);
                double base = gemTick * 0.15;
                for (int k = 0; k < 3; k++) {
                    double a = base + k * 2 * Math.PI / 3;
                    double h = 0.4 + ((gemTick + k * 7) % 20) * 0.08;
                    p.getWorld().spawnParticle(Particle.DUST, p.getLocation().add(Math.cos(a) * 1.0, h, Math.sin(a) * 1.0),
                            1, 0, 0, 0, 0, new Particle.DustOptions(c, 1.3f));
                }
            }
        }

        private void tickCrownTrail() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.isSprinting()) continue;
                boolean crown = holdsItem(p, "cursedcrown") || "cursedcrown".equals(idOf(p.getInventory().getHelmet()));
                if (!crown) continue;
                Vector back = p.getLocation().getDirection().setY(0);
                if (back.lengthSquared() > 0) back.normalize().multiply(-0.6);
                Location at = p.getLocation().add(back).add(0, 0.3, 0);
                p.getWorld().spawnParticle(Particle.DUST, at, 4, 0.25, 0.2, 0.25, 0, new Particle.DustOptions(Color.YELLOW, 1.4f));
            }
        }

        private void voidPulse(Player p) {
            if (!ready(p, "g_pulse", 10000)) return;
            Location c = p.getLocation();
            for (Entity en : c.getWorld().getNearbyEntities(c, 10, 5, 10)) {
                if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                hit(le, 12, p);
                le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 2));
                le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 1));
            }
            for (int r = 2; r <= 10; r += 4) {
                final double rr = r;
                Bukkit.getScheduler().runTaskLater(host, () -> particleRing(c.clone().add(0, 0.2, 0), rr, Particle.SMOKE), r / 2L);
            }
            c.getWorld().playSound(c, Sound.ENTITY_WITHER_AMBIENT, 1.5f, 0.5f);
        }

        private void shadowStep(Player p) {
            LivingEntity target = nearestEnemy(p.getLocation(), 20, p);
            if (target == null) {
                p.sendMessage(ChatColor.RED + "No enemy within 20 blocks.");
                return;
            }
            if (!ready(p, "g_step", 10000)) return;
            Location from = p.getLocation().clone();
            Location behind = target.getLocation().clone().add(target.getLocation().getDirection().setY(0).normalize().multiply(-1.5));
            behind.setDirection(target.getLocation().toVector().subtract(behind.toVector()));
            p.teleport(behind);
            hit(target, 15, p);
            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
            for (int k = 0; k <= 10; k++) {
                Location tp = from.clone().add(behind.clone().toVector().subtract(from.toVector()).multiply(k / 10.0));
                p.getWorld().spawnParticle(Particle.SMOKE, tp.add(0, 1, 0), 4, 0.1, 0.2, 0.1, 0);
            }
        }

        private void umbralShield(Player p) {
            if (!ready(p, "g_umbral", 25000)) return;
            umbralUntil.put(p.getUniqueId(), System.currentTimeMillis() + 15000);
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 300, 4));
            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.5f, 1.4f);
            p.sendMessage(ChatColor.DARK_PURPLE + "Eclipse Ward: 10 hearts and reflection for 15 seconds.");
        }

        private void totalEclipse(Player p) {
            if (!ready(p, "g_eclipse", 300000)) return;
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 600, 1));
            final Location center = p.getLocation().clone();
            final int[] runs = {0};
            final BukkitTask[] holder = new BukkitTask[1];
            holder[0] = gemTimer(p, () -> {
                runs[0]++;
                if (runs[0] > 15 || !p.isOnline()) {
                    holder[0].cancel();
                    return;
                }
                particleRing(center.clone().add(0, 0.2, 0), 12, Particle.SMOKE);
                for (Entity en : center.getWorld().getNearbyEntities(center, 12, 6, 12)) {
                    if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                    le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                    le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 1));
                    hit(le, 6, p);
                }
            }, 0L, 40L);
            p.sendMessage(ChatColor.DARK_PURPLE + "Total Eclipse: 30 seconds of darkness.");
        }

        @EventHandler
        public void onUmbralBlind(EntityDamageByEntityEvent e) {
            if (!(e.getEntity() instanceof Player p) || !isActive(umbralUntil, p)) return;
            if (e.getDamager() instanceof LivingEntity attacker && !attacker.equals(p)) {
                attacker.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                if (!reflectBusy) {
                    reflectBusy = true;
                    try {
                        hit(attacker, e.getDamage() * 0.25, p);
                    } finally {
                        reflectBusy = false;
                    }
                }
            }
        }

        @EventHandler
        public void onEclipseImmune(EntityPotionEffectEvent e) {
            if (!(e.getEntity() instanceof Player p) || !holdsItem(p, "eclipse")) return;
            if (e.getAction() != EntityPotionEffectEvent.Action.ADDED && e.getAction() != EntityPotionEffectEvent.Action.CHANGED) return;
            PotionEffect in = e.getNewEffect();
            if (in != null && (in.getType().equals(PotionEffectType.DARKNESS) || in.getType().equals(PotionEffectType.BLINDNESS))) {
                e.setCancelled(true);
                p.getWorld().spawnParticle(Particle.SMOKE, p.getLocation().add(0, 1, 0), 10, 0.4, 0.6, 0.4, 0.02);
            }
        }

        private void tickEclipseAura() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!holdsItem(p, "eclipse")) continue;
                double a = gemTick * 0.2;
                for (int k = 0; k < 2; k++) {
                    double ang = a + k * Math.PI;
                    p.getWorld().spawnParticle(Particle.SMOKE,
                            p.getLocation().add(Math.cos(ang) * 0.9, 0.4 + k * 0.9, Math.sin(ang) * 0.9), 1, 0, 0, 0, 0.01);
                }
                // The Light Before the Dark: below 5 hearts, Resistance, black campfire aura and slow healing.
                if (p.getHealth() < 10) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 40, 0));
                    p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + 0.25));
                    Location base = p.getLocation().add(0, 0.2, 0);
                    for (int k = 0; k < 4; k++) {
                        double ang = gemTick * 0.3 + k * Math.PI / 2;
                        p.getWorld().spawnParticle(Particle.DUST, base.clone().add(Math.cos(ang) * 0.6, 0.2, Math.sin(ang) * 0.6),
                                2, 0.05, 0.1, 0.05, 0, new Particle.DustOptions(Color.BLACK, 1.5f));
                    }
                    p.getWorld().spawnParticle(Particle.SMOKE, base.clone().add(0, 0.6, 0), 3, 0.2, 0.3, 0.2, 0.01);
                    p.getWorld().spawnParticle(Particle.FLAME, base.clone().add(0, 0.2, 0), 2, 0.2, 0.05, 0.2, 0.005);
                }
            }
        }

        @EventHandler
        public void onEclipseNight(EntityDamageEvent e) {
            if (!(e.getEntity() instanceof Player p) || !holdsItem(p, "eclipse")) return;
            long t = p.getWorld().getTime();
            if (t >= 13000 && t < 23000) e.setDamage(e.getDamage() * 0.85);
        }

        // ===================== CURSED CROWN =====================

        private static final List<EntityType> SOLDIER_TYPES = List.of(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.WITHER_SKELETON);

        private int ownedSoldiers(Player p) {
            int n = 0;
            for (UUID fid : followers) {
                Entity f = Bukkit.getEntity(fid);
                if (f != null && f.isValid() && p.getUniqueId().equals(ownerOf(f))
                        && "soldier".equals(f.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING))) {
                    n++;
                }
            }
            return n;
        }

        private void spawnSoldier(Player p, EntityType t, Location loc, long lifeTicks) {
            Entity s;
            try {
                s = p.getWorld().spawnEntity(loc, t);
            } catch (IllegalArgumentException ex) {
                return;
            }
            setOwner(s, p.getUniqueId());
            s.getPersistentDataContainer().set(ultKey, PersistentDataType.STRING, "soldier");
            if (s instanceof LivingEntity ls) {
                ls.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, (int) lifeTicks, 0));
            }
            addFollower(s);
            Bukkit.getScheduler().runTaskLater(host, () -> {
                if (s.isValid()) s.remove();
            }, lifeTicks);
        }

        private void summonSoldiers(Player p) {
            int have = ownedSoldiers(p);
            if (have >= 8) {
                p.sendMessage(ChatColor.RED + "You already have 8 soldiers.");
                return;
            }
            if (!ready(p, "g_soldiers", 60000)) return;
            int n = Math.min(3 + rnd.nextInt(5), 8 - have);
            for (int i = 0; i < n; i++) {
                EntityType t = SOLDIER_TYPES.get(rnd.nextInt(SOLDIER_TYPES.size()));
                spawnSoldier(p, t, p.getLocation().add(rand(-3, 3), 0, rand(-3, 3)), 2400L);
            }
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EVOKER_PREPARE_SUMMON, 2f, 0.8f);
            p.sendMessage(ChatColor.GOLD + "" + n + " soldier(s) rise for 2 minutes.");
        }

        private void rally(Player p) {
            if (!ready(p, "g_rally", 45000)) return;
            int n = 0;
            for (UUID fid : followers) {
                Entity f = Bukkit.getEntity(fid);
                if (f == null || !f.isValid() || !p.getUniqueId().equals(ownerOf(f))) continue;
                if (!"soldier".equals(f.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING))) continue;
                f.teleport(p.getLocation().add(rand(-2, 2), 0, rand(-2, 2)));
                if (f instanceof LivingEntity ls) ls.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 400, 1));
                n++;
            }
            p.getWorld().playSound(p.getLocation(), Sound.EVENT_RAID_HORN, 2f, 1f);
            p.sendMessage(ChatColor.GOLD + "Rally! " + n + " soldier(s) called to you.");
        }

        private void curseOfIron(Player p) {
            if (!ready(p, "g_curse", 30000)) return;
            Location c = p.getLocation();
            for (Entity en : c.getWorld().getNearbyEntities(c, 8, 4, 8)) {
                if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 1));
                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 1));
            }
            particleRing(c.clone().add(0, 0.2, 0), 8, Particle.SMOKE);
            c.getWorld().playSound(c, Sound.ENTITY_WITHER_SHOOT, 1.5f, 0.7f);
        }

        private void darkLegion(Player p) {
            if (!ready(p, "g_darklegion", 300000)) return;
            int legion = 3 + rnd.nextInt(5);
            for (int i = 0; i < legion; i++) {
                spawnSoldier(p, EntityType.WITHER_SKELETON, p.getLocation().add(rand(-4, 4), 0, rand(-4, 4)), 3600L);
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 3600, 1));
            p.sendTitle(ChatColor.DARK_PURPLE + "DARK LEGION", ChatColor.GRAY + "Eight wither soldiers rise", 5, 60, 10);
        }

        @EventHandler
        public void onSoldierDamage(EntityDamageByEntityEvent e) {
            Entity d = e.getDamager();
            if ("soldier".equals(d.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING))) {
                e.setDamage(e.getDamage() * 1.25);
            }
        }

        @EventHandler
        public void onCrownCurse(EntityDamageEvent e) {
            if (e.getEntity() instanceof Player p && holdsItem(p, "cursedcrown")) {
                e.setDamage(e.getDamage() * 1.1);
            }
        }

        // ===================== REAPER SIGIL =====================

        private void soulDrain(Player p) {
            if (!ready(p, "reaper_soul", 12000)) return;
            World w = p.getWorld();
            Location c = p.getLocation();
            int hit = 0;
            for (Entity en : w.getNearbyEntities(c, 6, 3, 6)) {
                if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                hit(le, 8, p);
                le.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 0));
                if (hit < 4) p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + 2));
                hit++;
            }
            particleRing(c.clone().add(0, 0.2, 0), 6, Particle.SOUL);
            w.playSound(c, Sound.ENTITY_WITHER_AMBIENT, 1.5f, 1f);
            p.sendMessage(ChatColor.DARK_AQUA + "Soul Drain hit " + hit + " target(s).");
        }

        private void deathMark(Player p) {
            LivingEntity target = nearestEnemy(p.getLocation(), 20, p);
            if (target == null) {
                p.sendMessage(ChatColor.RED + "No enemy within 20 blocks.");
                return;
            }
            if (!ready(p, "reaper_mark", 25000)) return;
            deathMarkTarget.put(p.getUniqueId(), target.getUniqueId());
            deathMarkUntil.put(p.getUniqueId(), System.currentTimeMillis() + 10000);
            target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 0));
            p.sendMessage(ChatColor.DARK_RED + "Death Mark set on a " + target.getType().name().toLowerCase(Locale.ROOT).replace('_', ' ') + " for 10 seconds.");
        }

        // Offhand lifesteal: you heal by the health the victim loses.
        @EventHandler
        public void onReaperLifesteal(EntityDamageByEntityEvent e) {
            if (e.isCancelled() || !(e.getDamager() instanceof Player p) || !(e.getEntity() instanceof Player)) return;
            if (!"reapersigil".equals(idOf(p.getInventory().getItemInOffHand()))) return;
            p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + e.getFinalDamage()));
        }

        @EventHandler
        public void onDeathMarkDamage(EntityDamageByEntityEvent e) {
            if (!(e.getDamager() instanceof Player p) || !(e.getEntity() instanceof LivingEntity v)) return;
            UUID target = deathMarkTarget.get(p.getUniqueId());
            if (target != null && target.equals(v.getUniqueId()) && isActive(deathMarkUntil, p)) {
                e.setDamage(e.getDamage() * 1.5);
            }
        }

        // Passive Harvest: every kill heals 4 hearts while the sigil is in your offhand.
        @EventHandler
        public void onReaperHarvest(EntityDeathEvent e) {
            Player killer = e.getEntity().getKiller();
            if (killer == null || !"reapersigil".equals(idOf(killer.getInventory().getItemInOffHand()))) return;
            killer.setHealth(Math.min(killer.getMaxHealth(), killer.getHealth() + 8));
        }

        // ===================== GUI MENU =====================

        private static final class MenuHolder implements InventoryHolder {
            final String type;
            final int page;
            final Map<Integer, String> actions = new HashMap<>();
            Inventory inv;

            MenuHolder(String type, int page) {
                this.type = type;
                this.page = page;
            }

            @Override
            public Inventory getInventory() {
                return inv;
            }
        }

        private ItemStack icon(Material m, String name, String... lore) {
            ItemStack it = new ItemStack(m);
            ItemMeta meta = it.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            it.setItemMeta(meta);
            return it;
        }

        private void fillMenu(Inventory inv) {
            ItemStack pane = icon(Material.GRAY_STAINED_GLASS_PANE, " ");
            for (int i = 0; i < inv.getSize(); i++) {
                if (inv.getItem(i) == null) inv.setItem(i, pane);
            }
        }

        private void openMain(Player p) {
            MenuHolder h = new MenuHolder("main", 0);
            h.inv = Bukkit.createInventory(h, 27, ChatColor.GOLD + "" + ChatColor.BOLD + "StickmanItems");
            h.inv.setItem(10, icon(Material.CHEST, ChatColor.GREEN + "" + ChatColor.BOLD + "All Items", ChatColor.GRAY + "Browse and get every item."));
            h.inv.setItem(12, icon(Material.AMETHYST_SHARD, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "All Gems", ChatColor.GRAY + "Get a gem (one at a time)."));
            h.inv.setItem(14, icon(Material.COMMAND_BLOCK, ChatColor.AQUA + "" + ChatColor.BOLD + "Commands", ChatColor.GRAY + "Quick command buttons."));
            h.inv.setItem(16, icon(Material.CLOCK, ChatColor.YELLOW + "" + ChatColor.BOLD + "Cooldown Editor", ChatColor.GRAY + "Change cooldowns live."));
            h.inv.setItem(22, icon(Material.BARRIER, ChatColor.RED + "" + ChatColor.BOLD + "Close"));
            fillMenu(h.inv);
            p.openInventory(h.inv);
        }

        private void openItems(Player p, int page) {
            List<ItemDef> list = new ArrayList<>(items.values());
            int pages = Math.max(1, (list.size() + 44) / 45);
            MenuHolder h = new MenuHolder("items", page);
            h.inv = Bukkit.createInventory(h, 54, ChatColor.GREEN + "Items " + (page + 1) + "/" + pages);
            for (int i = 0; i < 45; i++) {
                int idx = page * 45 + i;
                if (idx >= list.size()) break;
                h.inv.setItem(i, make(list.get(idx)));
            }
            if (page > 0) h.inv.setItem(45, icon(Material.ARROW, ChatColor.YELLOW + "Previous page"));
            h.inv.setItem(47, icon(Material.HOPPER, ChatColor.GREEN + "Give every item"));
            h.inv.setItem(49, icon(Material.BARRIER, ChatColor.RED + "Back"));
            if ((page + 1) * 45 < list.size()) h.inv.setItem(53, icon(Material.ARROW, ChatColor.YELLOW + "Next page"));
            fillMenu(h.inv);
            p.openInventory(h.inv);
        }

        private void openGems(Player p) {
            MenuHolder h = new MenuHolder("gems", 0);
            h.inv = Bukkit.createInventory(h, 54, ChatColor.LIGHT_PURPLE + "Gems");
            int i = 0;
            for (ItemDef d : gems.values()) {
                if (i >= 45) break;
                h.inv.setItem(i++, make(d));
            }
            h.inv.setItem(49, icon(Material.BARRIER, ChatColor.RED + "Back"));
            h.inv.setItem(53, icon(Material.LAVA_BUCKET, ChatColor.RED + "Remove my gems"));
            fillMenu(h.inv);
            p.openInventory(h.inv);
        }

        private void button(MenuHolder h, int slot, Material m, String name, String command, String... lore) {
            h.inv.setItem(slot, icon(m, name, lore));
            if (command != null) h.actions.put(slot, command);
        }

        private void openCommands(Player p) {
            MenuHolder h = new MenuHolder("commands", 0);
            h.inv = Bukkit.createInventory(h, 36, ChatColor.AQUA + "Commands");
            button(h, 0, Material.GOLDEN_APPLE, ChatColor.GREEN + "Heal", "stickman heal");
            button(h, 1, Material.COOKED_BEEF, ChatColor.GREEN + "Feed", "stickman feed");
            button(h, 2, Material.CLOCK, ChatColor.YELLOW + "Clear my cooldowns", "stickman clearcd");
            button(h, 3, Material.LAVA_BUCKET, ChatColor.RED + "Remove my gems", "stickman cleargems");
            button(h, 4, Material.SADDLE, ChatColor.GOLD + "Kill my giant mounts", "stickman killmounts");
            button(h, 5, Material.LEAD, ChatColor.GOLD + "Dismount", "stickman dismount");
            button(h, 6, Material.SUNFLOWER, ChatColor.YELLOW + "Set day", "stickman time day");
            button(h, 7, Material.CLOCK, ChatColor.BLUE + "Set night", "stickman time night");
            button(h, 8, Material.WHITE_WOOL, ChatColor.WHITE + "Clear weather", "stickman weather clear");
            button(h, 9, Material.WATER_BUCKET, ChatColor.AQUA + "Rain", "stickman weather rain");
            button(h, 10, Material.LIGHTNING_ROD, ChatColor.YELLOW + "Thunder", "stickman weather thunder");
            button(h, 11, Material.BOOK, ChatColor.WHITE + "Stats", "stickman stats");
            button(h, 12, Material.AMETHYST_CLUSTER, ChatColor.LIGHT_PURPLE + "Summon shrine (look at a block)", "stickman summon shrine");
            button(h, 13, Material.PARROT_SPAWN_EGG, ChatColor.GREEN + "Summon giant parrot", "stickman summon giantparrot");
            button(h, 14, Material.AXOLOTL_BUCKET, ChatColor.LIGHT_PURPLE + "Summon axolotl giant", "stickman summon axolotlgiant");
            button(h, 15, Material.RAVAGER_SPAWN_EGG, ChatColor.GOLD + "Summon giant ravager", "stickman summon giantmob ravager");
            button(h, 16, Material.HORSE_SPAWN_EGG, ChatColor.GOLD + "Summon giant horse", "stickman summon giantmob horse");
            button(h, 17, Material.GHAST_SPAWN_EGG, ChatColor.GOLD + "Summon giant ghast", "stickman summon giantmob ghast");
            button(h, 18, Material.WRITABLE_BOOK, ChatColor.WHITE + "Trust list", "stickman trustlist");
            button(h, 19, Material.IRON_SWORD, ChatColor.WHITE + "Follower count", "stickman followers");
            button(h, 20, Material.BELL, ChatColor.YELLOW + "Reload config.yml", "stickman reload");
            button(h, 21, Material.DIAMOND, ChatColor.AQUA + "Gem cooldowns OFF", "stickman nocooldowngem true");
            button(h, 22, Material.COAL, ChatColor.GRAY + "Gem cooldowns ON", "stickman nocooldowngem false");
            button(h, 23, Material.ENDER_PEARL, ChatColor.AQUA + "Item cooldowns OFF", "stickmanitems nocd");
            button(h, 24, Material.REDSTONE, ChatColor.RED + "Item cooldowns ON", "stickmanitems cd");
            button(h, 25, Material.CLOCK, ChatColor.YELLOW + "Cooldown editor", null);
            button(h, 31, Material.BARRIER, ChatColor.RED + "Back", null);
            fillMenu(h.inv);
            p.openInventory(h.inv);
        }

        private void openCooldowns(Player p, int page) {
            ConfigurationSection sec = cfg.getConfigurationSection("cooldowns");
            List<String> keys = sec == null ? new ArrayList<>() : new ArrayList<>(sec.getKeys(false));
            Collections.sort(keys);
            int pages = Math.max(1, (keys.size() + 44) / 45);
            MenuHolder h = new MenuHolder("cooldowns", page);
            h.inv = Bukkit.createInventory(h, 54, ChatColor.YELLOW + "Cooldowns " + (page + 1) + "/" + pages);
            for (int i = 0; i < 45; i++) {
                int idx = page * 45 + i;
                if (idx >= keys.size()) break;
                String key = keys.get(idx);
                String name = ABILITY_NAMES.getOrDefault(key, items.containsKey(key) ? items.get(key).name() : key);
                double secs = cfg.getDouble("cooldowns." + key, 0);
                h.inv.setItem(i, icon(Material.PAPER, ChatColor.YELLOW + name,
                        ChatColor.GRAY + "Current: " + ChatColor.WHITE + secs + "s",
                        ChatColor.GREEN + "Left click: +5s  " + ChatColor.RED + "Right click: -5s",
                        ChatColor.GRAY + "Shift: +/-30s"));
            }
            if (keys.isEmpty()) {
                h.inv.setItem(22, icon(Material.BOOK, ChatColor.GRAY + "No cooldowns yet",
                        ChatColor.GRAY + "Use an ability once and it appears here."));
            }
            if (page > 0) h.inv.setItem(45, icon(Material.ARROW, ChatColor.YELLOW + "Previous page"));
            h.inv.setItem(49, icon(Material.BARRIER, ChatColor.RED + "Back"));
            if ((page + 1) * 45 < keys.size()) h.inv.setItem(53, icon(Material.ARROW, ChatColor.YELLOW + "Next page"));
            fillMenu(h.inv);
            p.openInventory(h.inv);
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onMenuClick(InventoryClickEvent e) {
            Inventory top = e.getView().getTopInventory();
            if (!(top.getHolder() instanceof MenuHolder mh)) return;
            e.setCancelled(true);
            if (!(e.getWhoClicked() instanceof Player p)) return;
            if (e.getClickedInventory() == null || !e.getClickedInventory().equals(top)) return;
            if (!p.isOp()) {
                p.closeInventory();
                return;
            }
            int slot = e.getSlot();
            switch (mh.type) {
                case "main" -> {
                    switch (slot) {
                        case 10 -> openItems(p, 0);
                        case 12 -> openGems(p);
                        case 14 -> openCommands(p);
                        case 16 -> openCooldowns(p, 0);
                        case 22 -> p.closeInventory();
                        default -> { }
                    }
                }
                case "items" -> {
                    List<ItemDef> list = new ArrayList<>(items.values());
                    if (slot < 45) {
                        int idx = mh.page * 45 + slot;
                        if (idx < list.size()) {
                            giveOrDrop(p, make(list.get(idx)));
                            p.sendMessage(ChatColor.GREEN + "Got " + list.get(idx).name());
                        }
                    } else if (slot == 45 && mh.page > 0) {
                        openItems(p, mh.page - 1);
                    } else if (slot == 47) {
                        for (ItemDef d : items.values()) giveOrDrop(p, make(d));
                        p.sendMessage(ChatColor.GREEN + "Got every item.");
                    } else if (slot == 49) {
                        openMain(p);
                    } else if (slot == 53 && (mh.page + 1) * 45 < list.size()) {
                        openItems(p, mh.page + 1);
                    }
                }
                case "gems" -> {
                    List<ItemDef> list = new ArrayList<>(gems.values());
                    if (slot < 45 && slot < list.size()) {
                        p.performCommand("stickman givegem " + list.get(slot).id());
                    } else if (slot == 49) {
                        openMain(p);
                    } else if (slot == 53) {
                        p.performCommand("stickman cleargems");
                    }
                }
                case "commands" -> {
                    String cmd = mh.actions.get(slot);
                    if (cmd != null) {
                        p.performCommand(cmd);
                    } else if (slot == 25) {
                        openCooldowns(p, 0);
                    } else if (slot == 31) {
                        openMain(p);
                    }
                }
                case "cooldowns" -> {
                    ConfigurationSection sec = cfg.getConfigurationSection("cooldowns");
                    List<String> keys = sec == null ? new ArrayList<>() : new ArrayList<>(sec.getKeys(false));
                    Collections.sort(keys);
                    if (slot < 45) {
                        int idx = mh.page * 45 + slot;
                        if (idx < keys.size()) {
                            String key = keys.get(idx);
                            double step = e.isShiftClick() ? 30 : 5;
                            double delta = e.isRightClick() ? -step : step;
                            double cur = cfg.getDouble("cooldowns." + key, 0);
                            cfg.set("cooldowns." + key, Math.max(1, cur + delta));
                            host.saveModuleConfig("items", cfg);
                            openCooldowns(p, mh.page);
                        }
                    } else if (slot == 45 && mh.page > 0) {
                        openCooldowns(p, mh.page - 1);
                    } else if (slot == 49) {
                        openMain(p);
                    } else if (slot == 53 && (mh.page + 1) * 45 < keys.size()) {
                        openCooldowns(p, mh.page + 1);
                    }
                }
                default -> { }
            }
        }

        // ===================== GIANT RIDEABLE MOBS =====================

        private static final Set<EntityType> FLYERS = Set.of(EntityType.PARROT, EntityType.BAT, EntityType.PHANTOM,
                EntityType.GHAST, EntityType.BLAZE, EntityType.ALLAY, EntityType.VEX, EntityType.ENDER_DRAGON,
                EntityType.BEE, EntityType.WITHER);

        private boolean summonGiantMob(Player p, String typeName) {
            EntityType t;
            try {
                t = EntityType.valueOf(typeName.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                p.sendMessage(ChatColor.RED + "Unknown mob type: " + typeName);
                return false;
            }
            Entity g;
            try {
                g = p.getWorld().spawnEntity(p.getLocation().add(3, 0, 0), t);
            } catch (IllegalArgumentException ex) {
                p.sendMessage(ChatColor.RED + "That mob can't be spawned: " + typeName);
                return false;
            }
            if (!(g instanceof LivingEntity gl)) {
                g.remove();
                p.sendMessage(ChatColor.RED + "That isn't a living mob: " + typeName);
                return false;
            }
            setOwner(g, p.getUniqueId());
            g.getPersistentDataContainer().set(ultKey, PersistentDataType.STRING, "giantmob");
            AttributeInstance sc = gl.getAttribute(Attribute.GENERIC_SCALE);
            if (sc != null) sc.setBaseValue(3.0);
            AttributeInstance hp = gl.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (hp != null) {
                hp.setBaseValue(400);
                gl.setHealth(400);
            }
            gl.setPersistent(true);
            gl.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Giant " + t.name().replace('_', ' '));
            gl.setCustomNameVisible(true);
            addFollower(g);
            p.sendMessage(ChatColor.GOLD + "Giant " + t.name().toLowerCase(Locale.ROOT).replace('_', ' ')
                    + " summoned. Right-click it to ride. Sneak to get off.");
            return true;
        }

        @EventHandler
        public void onGiantMount(PlayerInteractEntityEvent e) {
            if (e.getHand() != EquipmentSlot.HAND) return;
            if (!(e.getRightClicked() instanceof LivingEntity mount)) return;
            if (!"giantmob".equals(mount.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING))) return;
            Player p = e.getPlayer();
            if (handHasItem(e)) return;
            e.setCancelled(true);
            if (!mount.getPassengers().isEmpty()) return;
            mount.addPassenger(p);
            if (FLYERS.contains(mount.getType())) mount.setGravity(false);
            p.sendMessage(ChatColor.GOLD + "Mounted. Look where you want to go.");
        }

        @EventHandler
        public void onGiantDismount(EntityDismountEvent e) {
            if (e.getDismounted() instanceof LivingEntity mount
                    && "giantmob".equals(mount.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING))) {
                mount.setGravity(true);
            }
        }

        private void tickGiantRide() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!(p.getVehicle() instanceof LivingEntity mount)) continue;
                if (!"giantmob".equals(mount.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING))) continue;
                if (p.isSneaking()) {
                    p.leaveVehicle();
                    continue;
                }
                Vector dir = p.getLocation().getDirection().normalize();
                if (FLYERS.contains(mount.getType())) {
                    mount.setVelocity(dir.multiply(1.05));
                } else {
                    Vector h = dir.clone().setY(0);
                    if (h.lengthSquared() > 0) h.normalize().multiply(0.5);
                    mount.setVelocity(new Vector(h.getX(), mount.getVelocity().getY(), h.getZ()));
                }
                mount.setRotation(p.getYaw(), 0f);
                p.setFallDistance(0f);
            }
        }

        // ===================== GIANT PARROT =====================

        private void summonGiantParrot(Player p) {
            Parrot parrot = p.getWorld().spawn(p.getLocation().add(3, 0, 0), Parrot.class);
            setOwner(parrot, p.getUniqueId());
            parrot.getPersistentDataContainer().set(ultKey, PersistentDataType.STRING, "giantparrot");
            parrot.setPersistent(true);
            AttributeInstance sc = parrot.getAttribute(Attribute.GENERIC_SCALE);
            if (sc != null) sc.setBaseValue(3.0);
            AttributeInstance php = parrot.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (php != null) {
                php.setBaseValue(300);
                parrot.setHealth(300);
            }
            parrot.setCustomName(ChatColor.GREEN + "" + ChatColor.BOLD + "Giant Parrot");
            parrot.setCustomNameVisible(true);
            p.sendMessage(ChatColor.GREEN + "Giant Parrot summoned. Right-click it to ride. Sneak to get off.");
        }

        @EventHandler
        public void onParrotMount(PlayerInteractEntityEvent e) {
            if (e.getHand() != EquipmentSlot.HAND) return;
            if (!(e.getRightClicked() instanceof Parrot par)) return;
            if (!"giantparrot".equals(par.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING))) return;
            Player p = e.getPlayer();
            if (handHasItem(e)) return;
            e.setCancelled(true);
            if (!par.getPassengers().isEmpty()) return;
            par.addPassenger(p);
            par.setGravity(false);
            p.sendMessage(ChatColor.GREEN + "Flying. Look where you want to go.");
        }

        @EventHandler
        public void onParrotDismount(EntityDismountEvent e) {
            if (e.getDismounted() instanceof Parrot par
                    && "giantparrot".equals(par.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING))) {
                par.setGravity(true);
            }
        }

        private void tickParrotRide() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!(p.getVehicle() instanceof Parrot par)) continue;
                if (!"giantparrot".equals(par.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING))) continue;
                if (p.isSneaking()) {
                    p.leaveVehicle();
                    continue;
                }
                par.setVelocity(p.getLocation().getDirection().normalize().multiply(1.05));
                par.setRotation(p.getYaw(), p.getPitch());
                p.setFallDistance(0f);
            }
        }

        // ===================== BLOODSTONE =====================

        @EventHandler
        public void onBloodstoneLifesteal(EntityDamageByEntityEvent e) {
            if (e.isCancelled() || !(e.getDamager() instanceof Player p)) return;
            if (!"bloodstone".equals(idOf(p.getInventory().getItemInMainHand()))) return;
            p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + e.getDamage() * 0.10));
        }

        // ===================== AXOLOTL GIANT (mountable) =====================

        @EventHandler
        public void onAxolotlMount(PlayerInteractEntityEvent e) {
            if (e.getHand() != EquipmentSlot.HAND) return;
            if (!(e.getRightClicked() instanceof Axolotl ax)) return;
            if (!"giantaxo".equals(ax.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING))) return;
            Player p = e.getPlayer();
            if (handHasItem(e)) return;
            e.setCancelled(true);
            if (!ax.getPassengers().isEmpty()) return;
            ax.addPassenger(p);
            ax.setGravity(false);
            p.sendMessage(ChatColor.AQUA + "Riding the Axolotl Giant. Look where you want to fly. Sneak to get off.");
        }

        @EventHandler
        public void onAxolotlDismount(EntityDismountEvent e) {
            if (e.getDismounted() instanceof Axolotl ax
                    && "giantaxo".equals(ax.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING))) {
                ax.setGravity(true);
            }
        }

        private void tickAxolotlRide() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!(p.getVehicle() instanceof Axolotl ax)) continue;
                if (!"giantaxo".equals(ax.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING))) continue;
                if (p.isSneaking()) {
                    p.leaveVehicle();
                    continue;
                }
                ax.setVelocity(p.getLocation().getDirection().normalize().multiply(0.6));
                p.setFallDistance(0f);
            }
        }

        @EventHandler
        public void onEvapQuit(PlayerQuitEvent e) {
            UUID id = e.getPlayer().getUniqueId();
            evapOn.remove(id);
            restoreWater(id);
        }

        // ===================== THUNDER MASTER =====================

        private void thunderStrike(Player p) {
            if (!ready(p, "g_tstrike", 5000)) return;
            World w = p.getWorld();
            Location from = p.getEyeLocation();
            Location t = aimPoint(p, 30);
            Vector span = t.toVector().subtract(from.toVector());
            for (int k = 1; k <= 6; k++) {
                w.spawnParticle(Particle.CRIT, from.clone().add(span.clone().multiply(k / 6.0)), 3, 0.1, 0.1, 0.1, 0);
            }
            w.strikeLightningEffect(t);
            damageArea(t, 3, 12, p);
            Bukkit.getScheduler().runTaskLater(host, () -> {
                w.strikeLightningEffect(t);
                damageArea(t, 3, 12, p);
                particleRing(t.clone().add(0, 0.2, 0), 3, Particle.CRIT);
            }, 10L);
            for (Entity en : w.getNearbyEntities(t, 3, 3, 3)) {
                if (!(en instanceof LivingEntity le) || en.equals(p) || isFriendlyTo(p, le)) continue;
                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 9));
            }
            w.playSound(t, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 2f, 1f);
            p.sendMessage(ChatColor.YELLOW + "Thunder Strike!");
        }

        // Stun for 8 seconds with 3 lightning strikes spread across it.
        private void stun(Player owner, LivingEntity target) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 160, 9));
            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 160, 1));
            target.getWorld().strikeLightningEffect(target.getLocation());
            hit(target, 6, owner);
            for (int d : new int[]{30, 90, 150}) {
                Bukkit.getScheduler().runTaskLater(host, () -> {
                    if (target.isValid() && !target.isDead()) {
                        target.getWorld().strikeLightningEffect(target.getLocation());
                        hit(target, 6, owner);
                    }
                }, d);
            }
            owner.sendMessage(ChatColor.YELLOW + "Stunned!");
        }

        private void chainLightning(Player p) {
            List<LivingEntity> chain = new ArrayList<>();
            LivingEntity cur = nearestEnemy(p.getLocation(), 12, p);
            while (cur != null && chain.size() < 5) {
                chain.add(cur);
                cur = nextChain(cur, chain);
            }
            if (chain.isEmpty()) {
                p.sendMessage(ChatColor.RED + "No enemies nearby to chain to.");
                return;
            }
            if (!ready(p, "g_chain", 10000)) return;
            for (int i = 0; i < chain.size(); i++) {
                final LivingEntity target = chain.get(i);
                final Location from = (i == 0) ? p.getEyeLocation() : chain.get(i - 1).getLocation().add(0, 1, 0);
                Bukkit.getScheduler().runTaskLater(host, () -> {
                    if (!target.isValid()) return;
                    Vector span = target.getLocation().add(0, 1, 0).toVector().subtract(from.toVector());
                    for (int s = 1; s <= 6; s++) {
                        from.getWorld().spawnParticle(Particle.CRIT, from.clone().add(span.clone().multiply(s / 6.0)), 3, 0.1, 0.1, 0.1, 0);
                    }
                    target.getWorld().strikeLightningEffect(target.getLocation());
                    hit(target, 10, p);
                }, i * 4L);
            }
            p.sendMessage(ChatColor.YELLOW + "Chain Lightning hit " + chain.size() + " target(s).");
        }

        private void surge(Player p) {
            if (!ready(p, "g_surge", 20000)) return;
            surgeHits.put(p.getUniqueId(), 3);
            for (int k = 0; k < 20; k++) {
                final int step = k;
                Bukkit.getScheduler().runTaskLater(host, () -> {
                    double ang = Math.toRadians(step * 18);
                    double h = 0.2 + step * 0.09;
                    Location pt = p.getLocation().add(Math.cos(ang) * 0.8, h, Math.sin(ang) * 0.8);
                    p.getWorld().spawnParticle(Particle.CRIT, pt, 3, 0.05, 0.05, 0.05, 0);
                }, k);
            }
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.5f, 1.3f);
            p.sendMessage(ChatColor.YELLOW + "Overcharge: your next 3 hits are empowered.");
        }

        private void zeus(Player p) {
            if (!ready(p, "g_zeus", 300000)) return;
            zeusUntil.put(p.getUniqueId(), System.currentTimeMillis() + 20000);
            p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 400, 1));
            p.sendTitle(ChatColor.GOLD + "ZEUS", ChatColor.YELLOW + "The sky answers you", 5, 60, 10);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 2f, 0.8f);
            final int[] runs = {0};
            final BukkitTask[] holder = new BukkitTask[1];
            holder[0] = gemTimer(p, () -> {
                runs[0]++;
                if (!p.isOnline() || runs[0] > 10) {
                    zeusUntil.remove(p.getUniqueId());
                    holder[0].cancel();
                    return;
                }
                List<LivingEntity> targets = new ArrayList<>();
                for (Entity en : p.getWorld().getNearbyEntities(p.getLocation(), 20, 10, 20)) {
                    if (en instanceof LivingEntity le && !en.equals(p) && !isFriendlyTo(p, le)) targets.add(le);
                }
                Collections.shuffle(targets, rnd);
                for (int i = 0; i < Math.min(3, targets.size()); i++) {
                    LivingEntity t = targets.get(i);
                    p.getWorld().strikeLightningEffect(t.getLocation());
                    hit(t, 10, p);
                }
                particleRing(p.getLocation().add(0, 1, 0), 2.0, Particle.END_ROD);
            }, 0L, 40L);
        }

        private void particleRing(Location c, double r, Particle pt) {
            for (int deg = 0; deg < 360; deg += 20) {
                double rad = Math.toRadians(deg);
                Location at = c.clone().add(Math.cos(rad) * r, 0.1, Math.sin(rad) * r);
                if (pt == Particle.DUST) {
                    c.getWorld().spawnParticle(pt, at, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB(0x8B0000), 1.3f));
                } else {
                    c.getWorld().spawnParticle(pt, at, 1, 0, 0, 0, 0);
                }
            }
        }

        // Passive aura for anyone holding the Thunder Master Gem.
        private void tickThunderAura() {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!holdsItem(p, "thunder")) continue;
                boolean zeus = isActive(zeusUntil, p);
                double radius = zeus ? 1.2 : 0.7;
                Location base = p.getLocation().add(0, 0.2, 0);
                for (int i = 0; i < (zeus ? 4 : 2); i++) {
                    double ang = rnd.nextDouble() * Math.PI * 2;
                    Location pt = base.clone().add(Math.cos(ang) * radius, rnd.nextDouble() * 2.2, Math.sin(ang) * radius);
                    p.getWorld().spawnParticle(Particle.END_ROD, pt, 1, 0, 0, 0, 0);
                }
                if (zeus) p.getWorld().spawnParticle(Particle.CRIT, base.clone().add(0, 1, 0), 4, 0.4, 0.8, 0.4, 0.05);
            }
        }

        @EventHandler
        public void onThunderDamage(EntityDamageByEntityEvent e) {
            if (inThunderProc || e.isCancelled()) return;
            Player attacker = null;
            if (e.getDamager() instanceof Player ap) attacker = ap;
            else if (e.getDamager() instanceof Projectile pj && pj.getShooter() instanceof Player sp) attacker = sp;
            if (attacker == null || !holdsItem(attacker, "thunder")) return;
            if (!(e.getEntity() instanceof LivingEntity victim) || victim.equals(attacker)) return;

            UUID aid = attacker.getUniqueId();
            int charges = surgeHits.getOrDefault(aid, 0);
            if (charges > 0) {
                e.setDamage(e.getDamage() + 10);
                if (charges - 1 <= 0) surgeHits.remove(aid);
                else surgeHits.put(aid, charges - 1);
                victim.getWorld().spawnParticle(Particle.CRIT, victim.getLocation().add(0, 1, 0), 30, 0.4, 0.6, 0.4, 0.2);
                victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1f, 1.5f);
                LivingEntity chainTo = nearestEnemy(victim.getLocation(), 6, attacker);
                if (chainTo != null) {
                    inThunderProc = true;
                    try {
                        victim.getWorld().strikeLightningEffect(chainTo.getLocation());
                        hit(chainTo, 5, attacker);
                    } finally {
                        inThunderProc = false;
                    }
                }
            }

            if (victim instanceof Player && rnd.nextDouble() < 0.20) {
                inThunderProc = true;
                try {
                    victim.getWorld().strikeLightningEffect(victim.getLocation());
                    hit(victim, 6, attacker);
                } finally {
                    inThunderProc = false;
                }
            }
        }

        @EventHandler
        public void onThunderLightningImmune(EntityDamageEvent e) {
            if (e.getCause() != EntityDamageEvent.DamageCause.LIGHTNING) return;
            if (e.getEntity() instanceof Player p && holdsItem(p, "thunder")) e.setCancelled(true);
        }

        @EventHandler
        public void onThunderEffectImmune(EntityPotionEffectEvent e) {
            if (!(e.getEntity() instanceof Player p) || !holdsItem(p, "thunder")) return;
            if (e.getAction() != EntityPotionEffectEvent.Action.ADDED
                    && e.getAction() != EntityPotionEffectEvent.Action.CHANGED) return;
            PotionEffect incoming = e.getNewEffect();
            if (incoming == null) return;
            if (incoming.getType().equals(PotionEffectType.SLOWNESS) || incoming.getType().equals(PotionEffectType.WEAKNESS)) {
                e.setCancelled(true);
            }
        }

        @EventHandler
        public void onThunderHungerImmune(FoodLevelChangeEvent e) {
            if (!(e.getEntity() instanceof Player p) || !holdsItem(p, "thunder")) return;
            if (e.getFoodLevel() < p.getFoodLevel()) e.setCancelled(true);
        }

        // ===================== CRAFTING, PROTECTION, SHRINES =====================

        private static final class ShrineHolder implements InventoryHolder {
            final Location loc;
            final String key;
            final Inventory inv;

            ShrineHolder(Location loc) {
                this.loc = loc;
                this.key = keyOf(loc);
                this.inv = Bukkit.createInventory(this, 27, ChatColor.GOLD + "Shrine");
            }

            @Override
            public Inventory getInventory() {
                return inv;
            }
        }

        private static String keyOf(Location l) {
            return l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
        }

        private boolean isOurs(ItemStack it) {
            return idOf(it) != null;
        }

        private boolean isGem(ItemStack it) {
            String id = idOf(it);
            return id != null && gems.containsKey(id);
        }

        private boolean isEmpty(ItemStack it) {
            return it == null || it.getType().isAir();
        }

        private boolean isBundle(ItemStack it) {
            return it != null && it.getType().name().endsWith("BUNDLE");
        }

        // Crafting: none of our items can be used in any recipe.
        @EventHandler
        public void onCraftPrep(PrepareItemCraftEvent e) {
            for (ItemStack it : e.getInventory().getMatrix()) {
                if (isOurs(it)) {
                    e.getInventory().setResult(null);
                    return;
                }
            }
        }

        @EventHandler
        public void onCraft(CraftItemEvent e) {
            for (ItemStack it : e.getInventory().getMatrix()) {
                if (isOurs(it)) {
                    e.setCancelled(true);
                    return;
                }
            }
        }

        // Gems can't be dropped.
        @EventHandler
        public void onGemDrop(PlayerDropItemEvent e) {
            if (!isGem(e.getItemDrop().getItemStack())) return;
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "Gems can't be dropped.");
        }

        // Gems are kept on death and returned on respawn.
        @EventHandler
        public void onGemDeath(PlayerDeathEvent e) {
            List<ItemStack> keep = new ArrayList<>();
            e.getDrops().removeIf(stack -> {
                if (!isGem(stack)) return false;
                keep.add(stack.clone());
                return true;
            });
            if (!keep.isEmpty()) deathKeep.put(e.getEntity().getUniqueId(), keep);
        }

        @EventHandler
        public void onGemRespawn(PlayerRespawnEvent e) {
            returnKeptGems(e.getPlayer());
        }

        @EventHandler
        public void onGemJoin(PlayerJoinEvent e) {
            returnKeptGems(e.getPlayer());
        }

        private void returnKeptGems(Player p) {
            List<ItemStack> keep = deathKeep.remove(p.getUniqueId());
            if (keep == null) return;
            Bukkit.getScheduler().runTaskLater(host, () -> {
                for (ItemStack gem : keep) p.getInventory().addItem(gem);
            }, 5L);
        }

        // Gems can't go into chests, barrels, bundles or any other container.
        @EventHandler
        public void onGemContainer(InventoryClickEvent e) {
            if (!(e.getWhoClicked() instanceof Player p)) return;
            Inventory top = e.getView().getTopInventory();

            if (top.getHolder() instanceof ShrineHolder) {
                if (p.isOp()) return;
                boolean placingIn = e.getClickedInventory() != null
                        && e.getClickedInventory().equals(top) && !isEmpty(e.getCursor());
                boolean shiftIn = e.getClickedInventory() != null
                        && !e.getClickedInventory().equals(top)
                        && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY;
                boolean hotbarIn = e.getAction() == InventoryAction.HOTBAR_SWAP;
                if (placingIn || shiftIn || hotbarIn) {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "Only operators can put items in shrines.");
                }
                return;
            }

            boolean intoContainer = !(top.getType() == InventoryType.PLAYER || top.getType() == InventoryType.CRAFTING);
            if (intoContainer && (isGem(e.getCursor()) || isGem(e.getCurrentItem()))) {
                e.setCancelled(true);
                return;
            }
            if (isGem(e.getCursor()) && isBundle(e.getCurrentItem())) {
                e.setCancelled(true);
                return;
            }
            if (intoContainer && e.getClick() == ClickType.NUMBER_KEY
                    && isGem(p.getInventory().getItem(e.getHotbarButton()))) {
                e.setCancelled(true);
            }
        }

        @EventHandler
        public void onGemDrag(InventoryDragEvent e) {
            if (!(e.getWhoClicked() instanceof Player p)) return;
            Inventory top = e.getView().getTopInventory();
            if (top.getHolder() instanceof ShrineHolder && !p.isOp()) {
                e.setCancelled(true);
                return;
            }
            if (!(top.getType() == InventoryType.PLAYER || top.getType() == InventoryType.CRAFTING) && isGem(e.getOldCursor())) {
                e.setCancelled(true);
            }
        }

        @EventHandler
        public void onGemHopper(InventoryMoveItemEvent e) {
            if (isGem(e.getItem())) e.setCancelled(true);
        }

        @EventHandler
        public void onGemFrame(PlayerInteractEntityEvent e) {
            if (!(e.getRightClicked() instanceof ItemFrame)) return;
            if (isGem(e.getPlayer().getInventory().getItem(e.getHand()))) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "Gems can't be placed in item frames.");
            }
        }

        @EventHandler
        public void onShrineClose(InventoryCloseEvent e) {
            if (e.getInventory().getHolder() instanceof ShrineHolder) saveShrines();
        }

        @EventHandler
        public void onShrineBreak(BlockBreakEvent e) {
            Block b = e.getBlock();
            String key = keyOf(b.getLocation());
            ShrineHolder h = shrines.get(key);
            if (h == null) return;
            if (!e.getPlayer().isOp()) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "Only operators can break shrines.");
                return;
            }
            for (ItemStack it : h.inv.getContents()) {
                if (it != null) b.getWorld().dropItemNaturally(b.getLocation(), it);
            }
            shrines.remove(key);
            saveShrines();
        }

        private void summonShrine(Player p) {
            Block target = p.getTargetBlockExact(5);
            if (target == null) {
                p.sendMessage(ChatColor.RED + "Look at a block within 5 blocks.");
                return;
            }
            Block place = target.getRelative(BlockFace.UP);
            if (!place.getType().isAir()) {
                p.sendMessage(ChatColor.RED + "There needs to be empty space above the block you're looking at.");
                return;
            }
            place.setType(Material.LODESTONE);
            ShrineHolder h = new ShrineHolder(place.getLocation());
            shrines.put(h.key, h);
            saveShrines();
            p.sendMessage(ChatColor.GREEN + "Shrine placed. Right-click it to open. Only operators can add items.");
        }

        private void summonAxolotlGiant(Player p) {
            Axolotl ax = p.getWorld().spawn(p.getLocation().add(3, 0, 0), Axolotl.class);
            setOwner(ax, p.getUniqueId());
            ax.getPersistentDataContainer().set(ultKey, PersistentDataType.STRING, "giantaxo");
            ax.setPersistent(true);
            AttributeInstance sc = ax.getAttribute(Attribute.GENERIC_SCALE);
            if (sc != null) sc.setBaseValue(4.0);
            ax.setCustomName(ChatColor.AQUA + "Axolotl Giant");
            ax.setCustomNameVisible(true);
            p.sendMessage(ChatColor.AQUA + "Axolotl Giant summoned. (Riding and flying are not in this version yet.)");
        }

        private void loadShrines() {
            ConfigurationSection sec = cfg.getConfigurationSection("shrines");
            if (sec == null) return;
            for (String id : sec.getKeys(false)) {
                String worldName = sec.getString(id + ".world");
                if (worldName == null || Bukkit.getWorld(worldName) == null) continue;
                Location loc = new Location(Bukkit.getWorld(worldName),
                        sec.getInt(id + ".x"), sec.getInt(id + ".y"), sec.getInt(id + ".z"));
                ShrineHolder h = new ShrineHolder(loc);
                List<?> raw = sec.getList(id + ".items");
                if (raw != null) {
                    ItemStack[] contents = new ItemStack[27];
                    for (int i = 0; i < Math.min(raw.size(), 27); i++) {
                        if (raw.get(i) instanceof ItemStack is && !is.getType().isAir()) contents[i] = is;
                    }
                    h.inv.setContents(contents);
                }
                shrines.put(h.key, h);
            }
        }

        private void saveShrines() {
            cfg.set("shrines", null);
            int idx = 0;
            for (ShrineHolder h : shrines.values()) {
                String base = "shrines.s" + (idx++);
                cfg.set(base + ".world", h.loc.getWorld().getName());
                cfg.set(base + ".x", h.loc.getBlockX());
                cfg.set(base + ".y", h.loc.getBlockY());
                cfg.set(base + ".z", h.loc.getBlockZ());
                List<ItemStack> items = new ArrayList<>();
                for (ItemStack it : h.inv.getContents()) {
                    items.add(it == null ? new ItemStack(Material.AIR) : it);
                }
                cfg.set(base + ".items", items);
            }
            host.saveModuleConfig("items", cfg);
        }

        @Override
        public void disable() {
            host.saveModuleConfig("items", cfg);
            for (BarrierState bs : barriers) restoreRing(bs);
            saveShrines();
        }

        // ----- /stickman commands -----

        private boolean handleStickman(CommandSender sender, String[] args) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "Only server operators can use this command.");
                return true;
            }
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                sendStickmanHelp(sender);
                return true;
            }
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "givegem" -> {
                    if (args.length < 2) { usage(sender, "/stickman givegem <gem> [player]"); return true; }
                    ItemDef g = gems.get(args[1].toLowerCase(Locale.ROOT));
                    if (g == null) {
                        sender.sendMessage(ChatColor.RED + "Unknown gem. Gems: " + String.join(", ", gems.keySet()));
                        return true;
                    }
                    Player target = resolveTarget(sender, args, 2);
                    if (target == null) return true;
                    if (hasGem(target)) {
                        Item extra = target.getWorld().dropItem(target.getLocation(), make(g));
                        extra.remove();
                        target.sendMessage(ChatColor.RED + "You already hold a gem. The extra gem was removed.");
                        sender.sendMessage(ChatColor.YELLOW + target.getName() + " already has a gem; the new one was removed.");
                    } else {
                        giveOrDrop(target, make(g));
                    }
                    sender.sendMessage(ChatColor.GREEN + "Gave " + g.name() + " to " + target.getName());
                }
                case "summon" -> {
                    if (args.length < 2) { usage(sender, "/stickman summon <shrine|axolotlgiant>"); return true; }
                    if (!(sender instanceof Player sp)) { sender.sendMessage(ChatColor.RED + "Only players can summon."); return true; }
                    switch (args[1].toLowerCase(Locale.ROOT)) {
                        case "shrine" -> summonShrine(sp);
                        case "axolotlgiant" -> summonAxolotlGiant(sp);
                        case "giantparrot" -> summonGiantParrot(sp);
                        case "giantmob" -> summonGiantMob(sp, args.length > 2 ? args[2] : "horse");
                        case "cutemob" -> summonGiantMob(sp, "panda");
                        default -> usage(sender, "/stickman summon <shrine|axolotlgiant>");
                    }
                }
                case "nocooldowngem" -> {
                    if (args.length < 2 || !(args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false"))) {
                        usage(sender, "/stickman nocooldowngem <true|false> [player]");
                        return true;
                    }
                    Player target = resolveTarget(sender, args, 2);
                    if (target == null) return true;
                    boolean on = args[1].equalsIgnoreCase("true");
                    if (on) gemNoCd.add(target.getUniqueId());
                    else gemNoCd.remove(target.getUniqueId());
                    sender.sendMessage(ChatColor.GREEN + "Gem cooldowns " + (on ? "OFF" : "ON") + " for " + target.getName());
                }
                case "trust" -> {
                    if (args.length < 2) { usage(sender, "/stickman trust <player>"); return true; }
                    if (!(sender instanceof Player owner)) {
                        sender.sendMessage(ChatColor.RED + "Only players can trust others.");
                        return true;
                    }
                    Player other = Bukkit.getPlayerExact(args[1]);
                    if (other == null) { sender.sendMessage(ChatColor.RED + "Player is not online."); return true; }
                    if (other.equals(owner)) { sender.sendMessage(ChatColor.RED + "You can't trust yourself."); return true; }
                    Set<UUID> set = trusts.computeIfAbsent(owner.getUniqueId(), k -> new HashSet<>());
                    if (set.remove(other.getUniqueId())) {
                        owner.sendMessage(ChatColor.YELLOW + "No longer trusting " + other.getName());
                    } else {
                        set.add(other.getUniqueId());
                        owner.sendMessage(ChatColor.GREEN + "You now trust " + other.getName() + ". Your abilities and mobs won't hurt them.");
                        other.sendMessage(ChatColor.GREEN + owner.getName() + " trusts you.");
                    }
                }
                case "cooldowns" -> {
                    if (!(sender instanceof Player cp)) { sender.sendMessage(ChatColor.RED + "Players only."); return true; }
                    for (Map.Entry<String, List<String>> en : GEM_ABILITIES.entrySet()) {
                        StringBuilder sb = new StringBuilder(hexColor(gems.get(en.getKey()).hex()) + gems.get(en.getKey()).name());
                        int slot = 0;
                        for (String ab : en.getValue()) {
                            slot++;
                            long left = cdLeft(cp, ab);
                            sb.append(ChatColor.GRAY).append(" | ").append(left > 0 ? ChatColor.RED : ChatColor.GREEN).append("A").append(slot);
                            if (left > 0) sb.append(ChatColor.RED).append(" ").append((left / 1000) + 1).append("s");
                        }
                        sender.sendMessage(sb.toString());
                    }
                }
                case "gems" -> sender.sendMessage(ChatColor.GOLD + "Gems: " + String.join(", ", gems.keySet()));
                case "menu" -> {
                    if (!(sender instanceof Player mp)) { sender.sendMessage(ChatColor.RED + "Players only."); return true; }
                    openMain(mp);
                }
                case "reload" -> {
                    cfg = host.moduleConfig("items");
                    sender.sendMessage(ChatColor.GREEN + "config.yml reloaded.");
                }
                case "clearcd" -> {
                    Player target = resolveTarget(sender, args, 1);
                    if (target == null) return true;
                    String prefix = target.getUniqueId() + ":";
                    cooldowns.keySet().removeIf(k -> k.startsWith(prefix));
                    sender.sendMessage(ChatColor.GREEN + "All cooldowns cleared for " + target.getName());
                }
                case "heal" -> {
                    Player target = resolveTarget(sender, args, 1);
                    if (target == null) return true;
                    target.setHealth(target.getMaxHealth());
                    sender.sendMessage(ChatColor.GREEN + "Healed " + target.getName());
                }
                case "feed" -> {
                    Player target = resolveTarget(sender, args, 1);
                    if (target == null) return true;
                    target.setFoodLevel(20);
                    target.setSaturation(20f);
                    sender.sendMessage(ChatColor.GREEN + "Fed " + target.getName());
                }
                case "followers" -> {
                    if (!(sender instanceof Player fp)) { sender.sendMessage(ChatColor.RED + "Players only."); return true; }
                    long count = followers.stream().filter(fid -> {
                        Entity f = Bukkit.getEntity(fid);
                        return f != null && f.isValid() && fp.getUniqueId().equals(ownerOf(f));
                    }).count();
                    sender.sendMessage(ChatColor.GREEN + "You have " + count + " follower(s).");
                }
                case "cleargems" -> {
                    Player target = resolveTarget(sender, args, 1);
                    if (target == null) return true;
                    var inv = target.getInventory();
                    int removed = 0;
                    for (int i = 0; i < inv.getSize(); i++) {
                        if (isGem(inv.getItem(i))) {
                            inv.setItem(i, null);
                            removed++;
                        }
                    }
                    sender.sendMessage(ChatColor.GREEN + "Removed " + removed + " gem(s) from " + target.getName());
                }
                case "trustlist" -> {
                    if (!(sender instanceof Player tp)) { sender.sendMessage(ChatColor.RED + "Players only."); return true; }
                    Set<UUID> set = trusts.getOrDefault(tp.getUniqueId(), Set.of());
                    if (set.isEmpty()) { sender.sendMessage(ChatColor.GRAY + "You trust nobody."); return true; }
                    StringBuilder names = new StringBuilder();
                    for (UUID u : set) names.append(Bukkit.getOfflinePlayer(u).getName()).append(' ');
                    sender.sendMessage(ChatColor.GREEN + "Trusted: " + names.toString().trim());
                }
                case "dismount" -> {
                    if (!(sender instanceof Player dp)) { sender.sendMessage(ChatColor.RED + "Players only."); return true; }
                    if (dp.getVehicle() == null) sender.sendMessage(ChatColor.GRAY + "You aren't riding anything.");
                    else {
                        dp.leaveVehicle();
                        sender.sendMessage(ChatColor.GREEN + "Dismounted.");
                    }
                }
                case "killmounts" -> {
                    if (!(sender instanceof Player kp)) { sender.sendMessage(ChatColor.RED + "Players only."); return true; }
                    int killed = 0;
                    Iterator<UUID> it = followers.iterator();
                    while (it.hasNext()) {
                        Entity f = Bukkit.getEntity(it.next());
                        if (f == null) { it.remove(); continue; }
                        if (!kp.getUniqueId().equals(ownerOf(f))) continue;
                        if (!"giantmob".equals(f.getPersistentDataContainer().get(ultKey, PersistentDataType.STRING))) continue;
                        f.remove();
                        it.remove();
                        killed++;
                    }
                    sender.sendMessage(ChatColor.GREEN + "Removed " + killed + " giant mount(s).");
                }
                case "time" -> {
                    if (args.length < 2 || !(sender instanceof Player tp)) { usage(sender, "/stickman time <day|night>"); return true; }
                    long t = args[1].equalsIgnoreCase("night") ? 13000L : 1000L;
                    tp.getWorld().setTime(t);
                    sender.sendMessage(ChatColor.GREEN + "Time set to " + args[1].toLowerCase(Locale.ROOT) + ".");
                }
                case "weather" -> {
                    if (args.length < 2 || !(sender instanceof Player wp)) { usage(sender, "/stickman weather <clear|rain|thunder>"); return true; }
                    World ww = wp.getWorld();
                    switch (args[1].toLowerCase(Locale.ROOT)) {
                        case "clear" -> { ww.setStorm(false); ww.setThundering(false); }
                        case "rain" -> { ww.setStorm(true); ww.setThundering(false); }
                        case "thunder" -> { ww.setStorm(true); ww.setThundering(true); }
                        default -> { usage(sender, "/stickman weather <clear|rain|thunder>"); return true; }
                    }
                    sender.sendMessage(ChatColor.GREEN + "Weather set to " + args[1].toLowerCase(Locale.ROOT) + ".");
                }
                case "stats" -> {
                    if (!(sender instanceof Player sp2)) { sender.sendMessage(ChatColor.RED + "Players only."); return true; }
                    long owned = followers.stream().filter(fid -> {
                        Entity f = Bukkit.getEntity(fid);
                        return f != null && sp2.getUniqueId().equals(ownerOf(f));
                    }).count();
                    sender.sendMessage(ChatColor.GOLD + "Stats for " + sp2.getName() + ":");
                    sender.sendMessage(ChatColor.GRAY + "Holding a gem: " + (hasGem(sp2) ? "yes" : "no"));
                    sender.sendMessage(ChatColor.GRAY + "Followers: " + owned);
                    sender.sendMessage(ChatColor.GRAY + "Trusted players: " + trusts.getOrDefault(sp2.getUniqueId(), Set.of()).size());
                    sender.sendMessage(ChatColor.GRAY + "Saved projectiles: " + unoStore.getOrDefault(sp2.getUniqueId(), new ArrayDeque<>()).size());
                }
                case "killfollowers" -> {
                    Player target = resolveTarget(sender, args, 1);
                    if (target == null) return true;
                    int removed = 0;
                    Iterator<UUID> it = followers.iterator();
                    while (it.hasNext()) {
                        Entity f = Bukkit.getEntity(it.next());
                        if (f == null) { it.remove(); continue; }
                        if (!target.getUniqueId().equals(ownerOf(f))) continue;
                        f.remove();
                        it.remove();
                        removed++;
                    }
                    sender.sendMessage(ChatColor.GREEN + "Removed " + removed + " follower(s) of " + target.getName());
                }
                case "lightning" -> {
                    Player target = resolveTarget(sender, args, 1);
                    if (target == null) return true;
                    target.getWorld().strikeLightningEffect(target.getLocation());
                    hit(target, 6, sender instanceof Player lp ? lp : null);
                    sender.sendMessage(ChatColor.YELLOW + "Lightning on " + target.getName());
                }
                case "bring" -> {
                    if (!(sender instanceof Player bp)) { sender.sendMessage(ChatColor.RED + "Players only."); return true; }
                    Player target = resolveTarget(sender, args, 1);
                    if (target == null) return true;
                    target.teleport(bp.getLocation());
                    sender.sendMessage(ChatColor.GREEN + "Brought " + target.getName() + " to you.");
                }
                case "tp" -> {
                    if (!(sender instanceof Player tpp)) { sender.sendMessage(ChatColor.RED + "Players only."); return true; }
                    Player target = resolveTarget(sender, args, 1);
                    if (target == null) return true;
                    tpp.teleport(target.getLocation());
                    sender.sendMessage(ChatColor.GREEN + "Teleported to " + target.getName() + ".");
                }
                case "fly" -> {
                    Player target = resolveTarget(sender, args, 1);
                    if (target == null) return true;
                    boolean on = !target.getAllowFlight();
                    target.setAllowFlight(on);
                    target.setFlying(on);
                    sender.sendMessage(ChatColor.GREEN + "Flight " + (on ? "ON" : "OFF") + " for " + target.getName());
                }
                case "invis" -> {
                    Player target = resolveTarget(sender, args, 1);
                    if (target == null) return true;
                    if (target.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        target.removePotionEffect(PotionEffectType.INVISIBILITY);
                        sender.sendMessage(ChatColor.GRAY + "Invisibility off for " + target.getName());
                    } else {
                        target.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 0, false, false));
                        sender.sendMessage(ChatColor.GRAY + "Invisibility on for " + target.getName());
                    }
                }
                case "debug" -> {
                    if (!(sender instanceof Player dp)) { sender.sendMessage(ChatColor.RED + "Players only."); return true; }
                    if (debugOn.remove(dp.getUniqueId())) sender.sendMessage(ChatColor.GRAY + "Debug off.");
                    else {
                        debugOn.add(dp.getUniqueId());
                        sender.sendMessage(ChatColor.GRAY + "Debug on. Press F or click an item and send me these lines.");
                    }
                }
                case "crowncheck" -> {
                    if (!(sender instanceof Player cp)) { sender.sendMessage(ChatColor.RED + "Players only."); return true; }
                    ItemStack helmet = cp.getInventory().getHelmet();
                    sender.sendMessage(ChatColor.GOLD + "Cursed Crown check:");
                    sender.sendMessage(ChatColor.GRAY + "Helmet slot: " + (helmet == null ? "empty" : helmet.getType().name()));
                    sender.sendMessage(ChatColor.GRAY + "Helmet id: " + idOf(helmet));
                    sender.sendMessage(ChatColor.GRAY + "Main hand id: " + idOf(cp.getInventory().getItemInMainHand()));
                    sender.sendMessage(ChatColor.GRAY + "Offhand id: " + idOf(cp.getInventory().getItemInOffHand()));
                    sender.sendMessage(ChatColor.GRAY + "holdsItem(cursedcrown): " + holdsItem(cp, "cursedcrown"));
                    sender.sendMessage(ChatColor.GRAY + "Soldiers owned: " + ownedSoldiers(cp));
                    sender.sendMessage(ChatColor.GRAY + "Soldier cooldown left: " + (cdLeft(cp, "g_soldiers") / 1000) + "s");
                }
                default -> sendStickmanHelp(sender);
            }
            return true;
        }

        private void sendStickmanHelp(CommandSender s) {
            s.sendMessage(ChatColor.GOLD + "StickmanItems gem commands (operators only):");
            s.sendMessage(ChatColor.YELLOW + "/stickman givegem <gem> [player]" + ChatColor.GRAY + " - give a gem");
            s.sendMessage(ChatColor.YELLOW + "/stickman nocooldowngem <true|false> [player]" + ChatColor.GRAY + " - gem cooldowns off/on");
            s.sendMessage(ChatColor.YELLOW + "/stickman trust <player>" + ChatColor.GRAY + " - toggle trust");
            s.sendMessage(ChatColor.YELLOW + "/stickman menu  (opens the full GUI)");
            s.sendMessage(ChatColor.YELLOW + "/stickman killfollowers|lightning|bring|tp|fly|invis [player]");
            s.sendMessage(ChatColor.YELLOW + "/stickman cooldowns | gems | reload | followers | trustlist");
            s.sendMessage(ChatColor.YELLOW + "/stickman clearcd|heal|feed|cleargems [player]");
            s.sendMessage(ChatColor.YELLOW + "/stickman summon giantmob <type> | dismount | killmounts | time day|night | weather clear|rain|thunder | stats");
            s.sendMessage(ChatColor.GRAY + "Gems: " + String.join(", ", gems.keySet()));
        }

        private List<String> stickmanTab(String[] args) {
            if (args.length == 0) return STICKMAN_SUBS;
            String last = args[args.length - 1].toLowerCase(Locale.ROOT);
            if (args.length == 1) return STICKMAN_SUBS.stream().filter(s -> s.startsWith(last)).toList();
            String sub = args[0].toLowerCase(Locale.ROOT);
            if (args.length == 2 && sub.equals("givegem")) {
                return gems.keySet().stream().filter(s -> s.startsWith(last)).toList();
            }
            if (args.length == 2 && sub.equals("nocooldowngem")) return List.of("true", "false");
            if (args.length == 2 && sub.equals("summon")) return List.of("shrine", "axolotlgiant", "giantparrot", "giantmob", "cutemob");
            boolean players = (args.length == 2 && sub.equals("trust"))
                    || (args.length == 3 && (sub.equals("givegem") || sub.equals("nocooldowngem")));
            if (players) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(n -> n.toLowerCase(Locale.ROOT).startsWith(last))
                        .toList();
            }
            return List.of();
        }

        private LivingEntity nextChain(LivingEntity from, List<LivingEntity> chain) {
            LivingEntity best = null;
            double bestDist = 8;
            for (Entity en : from.getWorld().getNearbyEntities(from.getLocation(), 8, 8, 8)) {
                if (!(en instanceof LivingEntity le) || en instanceof Player || chain.contains(le)) continue;
                double d = le.getLocation().distance(from.getLocation());
                if (d < bestDist) {
                    bestDist = d;
                    best = le;
                }
            }
            return best;
        }

        private double rand(double a, double b) {
            return a + rnd.nextDouble() * (b - a);
        }

        // ---------- Commands (operators only) ----------

        private static final List<String> SUBCOMMANDS =
                List.of("help", "list", "version", "give", "giveall", "info", "nocd", "cd", "reset", "clear");

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (cmd.getName().equalsIgnoreCase("stickman")) return handleStickman(sender, args);
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "Only server operators can use this command.");
                return true;
            }
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                sendHelp(sender);
                return true;
            }
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "list" -> sender.sendMessage(ChatColor.GREEN + "Items: " + String.join(", ", items.keySet()));
                case "version" -> sender.sendMessage(ChatColor.GREEN + "StickmanItems " + VERSION);
                case "give" -> {
                    if (args.length < 2) { usage(sender, "/stickmanitems give <item> [player]"); return true; }
                    ItemDef d = items.get(args[1].toLowerCase(Locale.ROOT));
                    if (d == null) { sender.sendMessage(ChatColor.RED + "Unknown item. Use /stickmanitems list"); return true; }
                    Player target = resolveTarget(sender, args, 2);
                    if (target == null) return true;
                    giveOrDrop(target, make(d));
                    sender.sendMessage(ChatColor.GREEN + "Gave " + d.name() + " to " + target.getName());
                }
                case "giveall" -> {
                    Player target = resolveTarget(sender, args, 1);
                    if (target == null) return true;
                    for (ItemDef d : items.values()) giveOrDrop(target, make(d));
                    sender.sendMessage(ChatColor.GREEN + "All items given to " + target.getName());
                }
                case "info" -> {
                    if (args.length < 2) { usage(sender, "/stickmanitems info <item>"); return true; }
                    ItemDef d = items.get(args[1].toLowerCase(Locale.ROOT));
                    if (d == null) { sender.sendMessage(ChatColor.RED + "Unknown item. Use /stickmanitems list"); return true; }
                    sender.sendMessage(hexColor(d.hex()) + d.name() + ChatColor.GRAY + " (" + d.id() + ", " + d.mat() + ")");
                    sender.sendMessage(ChatColor.GRAY + d.lore());
                }
                case "nocd" -> {
                    Player target = resolveTarget(sender, args, 1);
                    if (target == null) return true;
                    noCooldown.add(target.getUniqueId());
                    String prefix = target.getUniqueId() + ":";
                    cooldowns.keySet().removeIf(k -> k.startsWith(prefix));
                    sender.sendMessage(ChatColor.GREEN + "Cooldowns OFF for " + target.getName());
                    if (!sender.equals(target)) target.sendMessage(ChatColor.GREEN + "Your cooldowns are now off.");
                }
                case "cd" -> {
                    Player target = resolveTarget(sender, args, 1);
                    if (target == null) return true;
                    noCooldown.remove(target.getUniqueId());
                    sender.sendMessage(ChatColor.GREEN + "Cooldowns ON for " + target.getName());
                    if (!sender.equals(target)) target.sendMessage(ChatColor.GREEN + "Your cooldowns are back on.");
                }
                case "reset" -> {
                    Player target = resolveTarget(sender, args, 1);
                    if (target == null) return true;
                    String prefix = target.getUniqueId() + ":";
                    cooldowns.keySet().removeIf(k -> k.startsWith(prefix));
                    sender.sendMessage(ChatColor.GREEN + "Cooldowns reset for " + target.getName());
                }
                case "clear" -> {
                    Player target = resolveTarget(sender, args, 1);
                    if (target == null) return true;
                    var inv = target.getInventory();
                    int removed = 0;
                    for (int i = 0; i < inv.getSize(); i++) {
                        if (idOf(inv.getItem(i)) != null) {
                            inv.setItem(i, null);
                            removed++;
                        }
                    }
                    sender.sendMessage(ChatColor.GREEN + "Removed " + removed + " StickmanItems from " + target.getName());
                }
                default -> sender.sendMessage(ChatColor.RED + "Unknown command. /stickmanitems help");
            }
            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            if (!sender.isOp()) return List.of();
            if (cmd.getName().equalsIgnoreCase("stickman")) return stickmanTab(args);
            if (args.length == 0) return SUBCOMMANDS;
            String last = args[args.length - 1].toLowerCase(Locale.ROOT);
            if (args.length == 1) {
                return SUBCOMMANDS.stream().filter(s -> s.startsWith(last)).toList();
            }
            String sub = args[0].toLowerCase(Locale.ROOT);
            if (args.length == 2 && (sub.equals("give") || sub.equals("info"))) {
                return items.keySet().stream().filter(s -> s.startsWith(last)).toList();
            }
            boolean playerArg = (args.length == 2 && List.of("giveall", "nocd", "cd", "reset", "clear").contains(sub))
                    || (args.length == 3 && sub.equals("give"));
            if (playerArg) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(n -> n.toLowerCase(Locale.ROOT).startsWith(last))
                        .toList();
            }
            return List.of();
        }

        private void sendHelp(CommandSender s) {
            s.sendMessage(ChatColor.GOLD + "StickmanItems commands (operators only):");
            s.sendMessage(ChatColor.YELLOW + "/stickmanitems give <item> [player]" + ChatColor.GRAY + " - give one item");
            s.sendMessage(ChatColor.YELLOW + "/stickmanitems giveall [player]" + ChatColor.GRAY + " - give every item");
            s.sendMessage(ChatColor.YELLOW + "/stickmanitems info <item>" + ChatColor.GRAY + " - item details");
            s.sendMessage(ChatColor.YELLOW + "/stickmanitems nocd [player]" + ChatColor.GRAY + " - turn cooldowns off");
            s.sendMessage(ChatColor.YELLOW + "/stickmanitems cd [player]" + ChatColor.GRAY + " - turn cooldowns back on");
            s.sendMessage(ChatColor.YELLOW + "/stickmanitems reset [player]" + ChatColor.GRAY + " - clear cooldowns now");
            s.sendMessage(ChatColor.YELLOW + "/stickmanitems clear [player]" + ChatColor.GRAY + " - remove all StickmanItems");
            s.sendMessage(ChatColor.YELLOW + "/stickmanitems list | version");
        }

        private void usage(CommandSender s, String msg) {
            s.sendMessage(ChatColor.RED + "Usage: " + msg);
        }

        private Player resolveTarget(CommandSender sender, String[] args, int index) {
            if (args.length > index) {
                Player p = Bukkit.getPlayerExact(args[index]);
                if (p == null) sender.sendMessage(ChatColor.RED + "Player is not online.");
                return p;
            }
            if (sender instanceof Player self) return self;
            sender.sendMessage(ChatColor.RED + "Console must specify an online player.");
            return null;
        }
    }

    // ===== LoginModule.java =====
    public static class LoginModule implements StickModule, Listener, CommandExecutor, TabCompleter {

        private StickmanUniversal host;
        private FileConfiguration cfg;

        @Override
        public String name() {
            return "login";
        }

        @Override
        public void enable(StickmanUniversal h) {
            host = h;
            cfg = host.moduleConfig("login");

            
            accountsFile = new File(host.getDataFolder(), "accounts.yml");
            loadAccounts();
            host.getServer().getPluginManager().registerEvents(this, host);
            host.getCommand("resetpassword").setExecutor(this);
            host.getCommand("resetpassword").setTabCompleter(this);
            host.getCommand("stickauth").setExecutor(this);
            host.getCommand("stickauth").setTabCompleter(this);
            Bukkit.getScheduler().runTaskTimer(host, this::checkTimeouts, 20L, 20L);
            host.getLogger().info("StickLogin loaded " + hashes.size() + " accounts.");
        }


        /** Commands handled by this plugin. They are intercepted before the server runs them, so they are never logged. */
        static final Set<String> AUTH_ROOTS = Set.of("login", "l", "log", "register", "reg", "changepassword", "cp", "changepass", "logout");

        private final Set<UUID> loggedIn = new HashSet<>();
        private final Map<UUID, Long> pendingSince = new HashMap<>();
        private final Map<UUID, Location> savedLocation = new HashMap<>();
        private final Map<UUID, Integer> failures = new HashMap<>();
        private final Map<UUID, Long> lockedUntil = new HashMap<>();

        /** Account data, keyed by lowercase name. Passwords are only stored as PBKDF2 hashes. */
        private final Map<String, String> hashes = new HashMap<>();
        private final Map<String, Long> lastLogin = new HashMap<>();
        private final Map<String, String> ipHashes = new HashMap<>();

        private File accountsFile;
        private final SecureRandom random = new SecureRandom();

        

        @Override
        public void disable() {
            saveAccounts();
        }

        // ---------- Account storage ----------

        private void loadAccounts() {
            hashes.clear();
            lastLogin.clear();
            ipHashes.clear();
            if (!accountsFile.exists()) return;
            YamlConfiguration y = YamlConfiguration.loadConfiguration(accountsFile);
            ConfigurationSection sec = y.getConfigurationSection("accounts");
            if (sec == null) return;
            for (String name : sec.getKeys(false)) {
                ConfigurationSection a = sec.getConfigurationSection(name);
                if (a == null) continue;
                String h = a.getString("hash");
                if (h == null) continue;
                hashes.put(name, h);
                if (a.contains("last-login")) lastLogin.put(name, a.getLong("last-login"));
                if (a.contains("ip-hash")) ipHashes.put(name, a.getString("ip-hash"));
            }
        }

        private void saveAccounts() {
            YamlConfiguration y = new YamlConfiguration();
            for (Map.Entry<String, String> e : hashes.entrySet()) {
                String base = "accounts." + e.getKey() + ".";
                y.set(base + "hash", e.getValue());
                if (lastLogin.containsKey(e.getKey())) y.set(base + "last-login", lastLogin.get(e.getKey()));
                if (ipHashes.containsKey(e.getKey())) y.set(base + "ip-hash", ipHashes.get(e.getKey()));
            }
            try {
                host.getDataFolder().mkdirs();
                y.save(accountsFile);
            } catch (IOException ex) {
                host.getLogger().log(Level.SEVERE, "Could not save accounts.yml", ex);
            }
        }

        // ---------- Hashing (PBKDF2-SHA256, built into Java) ----------

        private byte[] pbkdf2(char[] password, byte[] salt, int iterations) {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, 256);
            try {
                SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                return f.generateSecret(spec).getEncoded();
            } catch (GeneralSecurityException ex) {
                throw new IllegalStateException("PBKDF2 is not available on this Java version", ex);
            } finally {
                spec.clearPassword();
            }
        }

        private String hashPassword(String password) {
            int iterations = cfg.getInt("security.hash-iterations", 120000);
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            byte[] key = pbkdf2(password.toCharArray(), salt, iterations);
            return iterations + ":" + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(key);
        }

        private boolean verifyPassword(String stored, String password) {
            String[] parts = stored.split(":");
            if (parts.length != 3) return false;
            try {
                int iterations = Integer.parseInt(parts[0]);
                byte[] salt = Base64.getDecoder().decode(parts[1]);
                byte[] expected = Base64.getDecoder().decode(parts[2]);
                byte[] actual = pbkdf2(password.toCharArray(), salt, iterations);
                return MessageDigest.isEqual(expected, actual);
            } catch (IllegalArgumentException ex) {
                return false;
            }
        }

        private String ipHash(Player p) {
            try {
                InetAddress addr = p.getAddress() == null ? null : p.getAddress().getAddress();
                String ip = addr == null ? "unknown" : addr.getHostAddress();
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                return Base64.getEncoder().encodeToString(md.digest(ip.getBytes(StandardCharsets.UTF_8)));
            } catch (GeneralSecurityException ex) {
                return "unknown";
            }
        }

        // ---------- Messages and helpers ----------

        private String msg(String key, String... pairs) {
            String text = cfg.getString("messages." + key, key);
            for (int i = 0; i + 1 < pairs.length; i += 2) text = text.replace(pairs[i], pairs[i + 1]);
            return ChatColor.translateAlternateColorCodes('&', text);
        }

        private boolean isLoggedIn(Player p) {
            return loggedIn.contains(p.getUniqueId());
        }

        private String key(String name) {
            return name.toLowerCase(Locale.ROOT);
        }

        private boolean sessionValid(Player p) {
            String k = key(p.getName());
            Long last = lastLogin.get(k);
            if (last == null) return false;
            long minutes = cfg.getLong("session.minutes", 1440);
            if (minutes <= 0) return false;
            String stored = ipHashes.get(k);
            return stored != null && stored.equals(ipHash(p))
                    && System.currentTimeMillis() - last < minutes * 60_000L;
        }

        private void sendToLimbo(Player p) {
            if (!cfg.getBoolean("limbo.enabled", true)) return;
            World w = Bukkit.getWorld(cfg.getString("limbo.world", "world"));
            if (w == null) {
                host.getLogger().warning("Limbo world '" + cfg.getString("limbo.world") + "' does not exist. Check limbo.world in config.yml.");
                return;
            }
            p.teleport(new Location(w,
                    cfg.getDouble("limbo.x", 0.5),
                    cfg.getDouble("limbo.y", 80),
                    cfg.getDouble("limbo.z", 0.5),
                    (float) cfg.getDouble("limbo.yaw", 0),
                    (float) cfg.getDouble("limbo.pitch", 0)));
        }

        private void markPending(Player p) {
            UUID id = p.getUniqueId();
            loggedIn.remove(id);
            pendingSince.put(id, System.currentTimeMillis());
            if (!savedLocation.containsKey(id)) savedLocation.put(id, p.getLocation().clone());
            sendToLimbo(p);
        }

        private void authorize(Player p, boolean announce) {
            UUID id = p.getUniqueId();
            String k = key(p.getName());
            loggedIn.add(id);
            pendingSince.remove(id);
            failures.remove(id);
            lockedUntil.remove(id);
            Location back = savedLocation.remove(id);
            if (back != null && cfg.getBoolean("login.restore-location", true)) p.teleport(back);
            lastLogin.put(k, System.currentTimeMillis());
            ipHashes.put(k, ipHash(p));
            saveAccounts();
            if (announce) p.sendMessage(msg("login-success"));
            host.getLogger().info(p.getName() + " logged in.");
        }

        // ---------- Join, quit and timeouts ----------

        @EventHandler(priority = EventPriority.LOWEST)
        public void onJoin(PlayerJoinEvent e) {
            Player p = e.getPlayer();
            boolean registered = hashes.containsKey(key(p.getName()));
            if (registered && sessionValid(p)) {
                savedLocation.put(p.getUniqueId(), p.getLocation().clone());
                authorize(p, false);
                p.sendMessage(msg("session-restored"));
                return;
            }
            markPending(p);
            Bukkit.getScheduler().runTaskLater(host, () -> {
                if (p.isOnline() && !isLoggedIn(p)) {
                    p.sendMessage(msg(registered ? "join-login" : (registrationOpen() ? "join-register" : "join-closed"),
                            "%timeout%", String.valueOf(cfg.getInt("login.timeout-seconds", 60))));
                }
            }, 40L);
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent e) {
            UUID id = e.getPlayer().getUniqueId();
            loggedIn.remove(id);
            pendingSince.remove(id);
            savedLocation.remove(id);
            failures.remove(id);
        }

        private void checkTimeouts() {
            long limit = cfg.getInt("login.timeout-seconds", 60) * 1000L;
            if (limit <= 0) return;
            long now = System.currentTimeMillis();
            for (Map.Entry<UUID, Long> e : new ArrayList<>(pendingSince.entrySet())) {
                if (now - e.getValue() < limit) continue;
                Player p = Bukkit.getPlayer(e.getKey());
                if (p != null && p.isOnline()) p.kickPlayer(msg("timeout-kick"));
                pendingSince.remove(e.getKey());
            }
        }

        // ---------- Commands typed by players (intercepted, never logged) ----------

        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
            String raw = e.getMessage().startsWith("/") ? e.getMessage().substring(1) : e.getMessage();
            String[] parts = raw.trim().split("\\s+");
            if (parts.length == 0 || parts[0].isEmpty()) return;
            String root = parts[0].toLowerCase(Locale.ROOT);
            Player p = e.getPlayer();

            if (AUTH_ROOTS.contains(root)) {
                // Cancelling the event means the server never runs or logs this command, so passwords stay out of the console.
                e.setCancelled(true);
                handleAuth(p, root, Arrays.copyOfRange(parts, 1, parts.length));
                return;
            }
            if (!isLoggedIn(p) && !cfg.getStringList("allowed-commands").contains(root)) {
                e.setCancelled(true);
                p.sendMessage(msg("must-login"));
            }
        }

        private void handleAuth(Player p, String root, String[] args) {
            switch (root) {
                case "login", "l", "log" -> login(p, args);
                case "register", "reg" -> register(p, args);
                case "changepassword", "cp", "changepass" -> changePassword(p, args);
                case "logout" -> logout(p);
                default -> { }
            }
        }

        private boolean locked(Player p) {
            Long until = lockedUntil.get(p.getUniqueId());
            return until != null && System.currentTimeMillis() < until;
        }

        private void login(Player p, String[] args) {
            if (isLoggedIn(p)) { p.sendMessage(msg("already-logged-in")); return; }
            if (locked(p)) { p.sendMessage(msg("locked")); return; }
            String k = key(p.getName());
            if (!hashes.containsKey(k)) { p.sendMessage(msg("not-registered")); return; }
            if (args.length < 1) { p.sendMessage(msg("usage-login")); return; }

            if (!verifyPassword(hashes.get(k), args[0])) {
                int count = failures.merge(p.getUniqueId(), 1, Integer::sum);
                int max = cfg.getInt("login.max-attempts", 5);
                host.getLogger().info(p.getName() + " entered a wrong password (" + count + "/" + max + ").");
                if (count >= max) {
                    failures.remove(p.getUniqueId());
                    lockedUntil.put(p.getUniqueId(), System.currentTimeMillis() + cfg.getLong("login.lockout-minutes", 5) * 60_000L);
                    p.kickPlayer(msg("too-many-attempts"));
                    return;
                }
                p.sendMessage(msg("wrong-password", "%left%", String.valueOf(max - count)));
                return;
            }
            authorize(p, true);
        }

        private boolean registrationOpen() {
            return cfg.getBoolean("registration.enabled", true);
        }

        private void setRegistration(boolean open) {
            cfg.set("registration.enabled", open);
            host.saveModuleConfig("login", cfg);
        }

        private String policyError(String name, String password) {
            int min = cfg.getInt("security.min-password-length", 8);
            int max = cfg.getInt("security.max-password-length", 64);
            if (password.length() < min) return msg("password-too-short", "%min%", String.valueOf(min));
            if (password.length() > max) return msg("password-too-long", "%max%", String.valueOf(max));
            if (cfg.getBoolean("security.forbid-username-as-password", true) && password.equalsIgnoreCase(name)) {
                return msg("password-is-username");
            }
            return null;
        }

        private void register(Player p, String[] args) {
            if (!registrationOpen()) { p.sendMessage(msg("registration-closed")); return; }
            if (isLoggedIn(p)) { p.sendMessage(msg("already-logged-in")); return; }
            String k = key(p.getName());
            if (hashes.containsKey(k)) { p.sendMessage(msg("already-registered")); return; }
            boolean confirm = cfg.getBoolean("security.require-confirm", true);
            int needed = confirm ? 2 : 1;
            if (args.length < needed) {
                p.sendMessage(msg(confirm ? "usage-register" : "usage-register-simple"));
                return;
            }
            String password = args[0];
            if (confirm && !password.equals(args[1])) { p.sendMessage(msg("password-mismatch")); return; }
            String problem = policyError(p.getName(), password);
            if (problem != null) { p.sendMessage(problem); return; }

            hashes.put(k, hashPassword(password));
            authorize(p, false);
            saveAccounts();
            p.sendMessage(msg("register-success"));
            host.getLogger().info(p.getName() + " registered an account.");
        }

        private void changePassword(Player p, String[] args) {
            if (!isLoggedIn(p)) { p.sendMessage(msg("must-login")); return; }
            if (args.length < 2) { p.sendMessage(msg("usage-changepassword")); return; }
            String k = key(p.getName());
            if (!verifyPassword(hashes.get(k), args[0])) { p.sendMessage(msg("changepassword-wrong")); return; }
            String problem = policyError(p.getName(), args[1]);
            if (problem != null) { p.sendMessage(problem); return; }
            hashes.put(k, hashPassword(args[1]));
            saveAccounts();
            p.sendMessage(msg("changepassword-success"));
            host.getLogger().info(p.getName() + " changed their password.");
        }

        private void logout(Player p) {
            if (!isLoggedIn(p)) { p.sendMessage(msg("must-login")); return; }
            loggedIn.remove(p.getUniqueId());
            lastLogin.remove(key(p.getName()));
            saveAccounts();
            markPending(p);
            p.sendMessage(msg("logout-success"));
        }

        // ---------- Admin commands ----------

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (cmd.getName().equalsIgnoreCase("stickman")) {
                if (!sender.hasPermission("stickauth.admin")) { sender.sendMessage(ChatColor.RED + "You do not have permission."); return true; }
                if (args.length >= 1 && args[0].equalsIgnoreCase("registration")) {
                    String action = args.length >= 2 ? args[1].toLowerCase(Locale.ROOT) : "status";
                    switch (action) {
                        case "enable" -> {
                            setRegistration(true);
                            sender.sendMessage(ChatColor.GREEN + "Registration is now enabled.");
                        }
                        case "disable" -> {
                            setRegistration(false);
                            sender.sendMessage(ChatColor.YELLOW + "Registration is now disabled. Existing players can still log in.");
                        }
                        case "status" -> sender.sendMessage(ChatColor.GOLD + "Registration is " + (registrationOpen() ? "enabled" : "disabled") + ".");
                        default -> sender.sendMessage(ChatColor.YELLOW + "Usage: /stickman registration enable|disable|status");
                    }
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /stickman registration enable|disable|status");
                }
                return true;
            }
            if (cmd.getName().equalsIgnoreCase("resetpassword")) {
                if (!sender.hasPermission("stickauth.admin")) { sender.sendMessage(ChatColor.RED + "You do not have permission."); return true; }
                if (args.length < 1) { sender.sendMessage(ChatColor.RED + "Usage: /resetpassword <name>"); return true; }
                resetPassword(sender, args[0]);
                return true;
            }
            if (!sender.hasPermission("stickauth.admin")) { sender.sendMessage(ChatColor.RED + "You do not have permission."); return true; }
            String sub = args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : "help";
            switch (sub) {
                case "reload" -> {
                    cfg = host.moduleConfig("login");
                    loadAccounts();
                    sender.sendMessage(ChatColor.GREEN + "StickLogin reloaded. " + hashes.size() + " accounts.");
                }
                case "info" -> {
                    if (args.length < 2) { sender.sendMessage(ChatColor.RED + "Usage: /stickauth info <name>"); return true; }
                    String k = key(args[1]);
                    if (!hashes.containsKey(k)) { sender.sendMessage(ChatColor.YELLOW + "No account for " + args[1] + "."); return true; }
                    Long last = lastLogin.get(k);
                    sender.sendMessage(ChatColor.GOLD + "Account " + args[1] + ": registered. Last login: "
                            + (last == null ? "never" : new Date(last)) + ". Password hash is hidden.");
                }
                default -> {
                    sender.sendMessage(ChatColor.GOLD + "StickLogin");
                    sender.sendMessage(ChatColor.YELLOW + "/resetpassword <name>" + ChatColor.GRAY + " - remove a password so the player registers again");
                    sender.sendMessage(ChatColor.YELLOW + "/stickauth reload" + ChatColor.GRAY + " - reload config and accounts");
                    sender.sendMessage(ChatColor.YELLOW + "/stickauth info <name>" + ChatColor.GRAY + " - account status (never shows the password)");
                }
            }
            return true;
        }

        private void resetPassword(CommandSender sender, String name) {
            String k = key(name);
            if (!hashes.containsKey(k)) {
                sender.sendMessage(ChatColor.YELLOW + "No account found for " + name + ".");
                return;
            }
            hashes.remove(k);
            lastLogin.remove(k);
            ipHashes.remove(k);
            saveAccounts();
            Player target = Bukkit.getPlayerExact(name);
            if (target != null) {
                markPending(target);
                target.sendMessage(msg("reset-player"));
            }
            // The password itself is never written anywhere, only the fact of the reset.
            host.getLogger().info(sender.getName() + " reset the password of " + name + ".");
            sender.sendMessage(ChatColor.GREEN + "Password of " + name + " reset. They must register again.");
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            if (args.length == 1) {
                if (cmd.getName().equalsIgnoreCase("resetpassword")) {
                    List<String> names = new ArrayList<>();
                    for (String n : hashes.keySet()) names.add(n);
                    return names.stream().filter(s -> s.startsWith(args[0].toLowerCase(Locale.ROOT))).toList();
                }
                return List.of("reload", "info").stream().filter(s -> s.startsWith(args[0].toLowerCase(Locale.ROOT))).toList();
            }
            return List.of();
        }

        // ---------- Blocking actions until the player logs in ----------

        @EventHandler(priority = EventPriority.LOWEST)
        public void onMove(PlayerMoveEvent e) {
            if (isLoggedIn(e.getPlayer())) return;
            if (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getY() == e.getTo().getY() && e.getFrom().getZ() == e.getTo().getZ()) return;
            org.bukkit.Location stay = e.getFrom().clone();
            stay.setYaw(e.getTo().getYaw());
            stay.setPitch(e.getTo().getPitch());
            e.setTo(stay);
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onChat(AsyncPlayerChatEvent e) {
            if (isLoggedIn(e.getPlayer())) return;
            e.setCancelled(true);
            Bukkit.getScheduler().runTask(host, () -> e.getPlayer().sendMessage(msg("must-login")));
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onBreak(BlockBreakEvent e) {
            if (!isLoggedIn(e.getPlayer())) e.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onPlace(BlockPlaceEvent e) {
            if (!isLoggedIn(e.getPlayer())) e.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onInteract(PlayerInteractEvent e) {
            if (!isLoggedIn(e.getPlayer())) e.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onDrop(PlayerDropItemEvent e) {
            if (!isLoggedIn(e.getPlayer())) e.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onPickup(PlayerPickupItemEvent e) {
            if (!isLoggedIn(e.getPlayer())) e.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onInventoryOpen(InventoryOpenEvent e) {
            if (e.getPlayer() instanceof Player p && !isLoggedIn(p)) e.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onInventoryClick(InventoryClickEvent e) {
            if (e.getWhoClicked() instanceof Player p && !isLoggedIn(p)) e.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onDamage(EntityDamageEvent e) {
            if (!cfg.getBoolean("login.freeze-damage", true)) return;
            if (e.getEntity() instanceof Player p && !isLoggedIn(p)) e.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onMobTarget(EntityTargetLivingEntityEvent e) {
            if (e.getTarget() instanceof Player p && !isLoggedIn(p)) e.setCancelled(true);
        }
    }

    // ===== TeamsModule.java =====
    public static class TeamsModule implements StickModule, Listener, CommandExecutor, TabCompleter {

        private StickmanUniversal host;
        private FileConfiguration cfg;

        static final int ECHEST_SIZE = 54;
        static final Pattern TEAM_NAME = Pattern.compile("[A-Za-z0-9_]{3,16}");

        static final class Team {
            final String name;
            UUID leader;
            final LinkedHashSet<UUID> members = new LinkedHashSet<>();
            boolean pvp = true;
            final ItemStack[] echest = new ItemStack[ECHEST_SIZE];

            Team(String name, UUID leader) {
                this.name = name;
                this.leader = leader;
                this.members.add(leader);
            }

            String key() {
                return name.toLowerCase(Locale.ROOT);
            }
        }

        /** Holder for the team ender chest GUI. Only one member can have it open at a time. */
        static final class EchestHolder implements InventoryHolder {
            final String teamKey;
            final UUID viewer;
            Inventory inv;

            EchestHolder(String teamKey, UUID viewer) {
                this.teamKey = teamKey;
                this.viewer = viewer;
            }

            @Override
            public Inventory getInventory() {
                return inv;
            }
        }

        private final Map<String, Team> teams = new LinkedHashMap<>();
        private final Map<UUID, String> teamOf = new HashMap<>();
        private final Map<UUID, String> invites = new HashMap<>();
        private final Set<UUID> teamChatMode = new HashSet<>();
        private final Map<String, EchestHolder> echestOpen = new HashMap<>();
        private File dataFile;

        @Override
        public String name() {
            return "teams";
        }

        @Override
        public void enable(StickmanUniversal h) {
            host = h;
            cfg = host.moduleConfig("teams");
            dataFile = new File(host.getDataFolder(), "teams.yml");
            loadTeams();
            host.getServer().getPluginManager().registerEvents(this, host);
            host.getCommand("stickteam").setExecutor(this);
            host.getCommand("stickteam").setTabCompleter(this);
            host.getLogger().info("Teams module loaded " + teams.size() + " teams.");
        }

        @Override
        public void disable() {
            for (EchestHolder h : new ArrayList<>(echestOpen.values())) saveEchest(h);
            echestOpen.clear();
            saveTeams();
        }

        // ---------- Storage ----------

        private void loadTeams() {
            teams.clear();
            teamOf.clear();
            if (!dataFile.exists()) return;
            YamlConfiguration y = YamlConfiguration.loadConfiguration(dataFile);
            org.bukkit.configuration.ConfigurationSection sec = y.getConfigurationSection("teams");
            if (sec == null) return;
            for (String key : sec.getKeys(false)) {
                org.bukkit.configuration.ConfigurationSection t = sec.getConfigurationSection(key);
                if (t == null) continue;
                UUID leader;
                try {
                    leader = UUID.fromString(t.getString("leader", ""));
                } catch (IllegalArgumentException ex) {
                    host.getLogger().warning("Skipping team " + key + " with a bad leader id.");
                    continue;
                }
                Team team = new Team(t.getString("name", key), leader);
                team.members.clear();
                for (String m : t.getStringList("members")) {
                    try {
                        team.members.add(UUID.fromString(m));
                    } catch (IllegalArgumentException ignored) {
                    }
                }
                team.members.add(leader);
                team.pvp = t.getBoolean("pvp", true);
                org.bukkit.configuration.ConfigurationSection ec = t.getConfigurationSection("echest");
                if (ec != null) {
                    for (int i = 0; i < ECHEST_SIZE; i++) {
                        Object o = ec.get(String.valueOf(i));
                        if (o instanceof ItemStack is) team.echest[i] = is;
                    }
                }
                teams.put(team.key(), team);
                for (UUID m : team.members) teamOf.put(m, team.key());
            }
        }

        private void saveTeams() {
            YamlConfiguration y = new YamlConfiguration();
            for (Team t : teams.values()) {
                String base = "teams." + t.key() + ".";
                y.set(base + "name", t.name);
                y.set(base + "leader", t.leader.toString());
                List<String> ms = new ArrayList<>();
                for (UUID m : t.members) ms.add(m.toString());
                y.set(base + "members", ms);
                y.set(base + "pvp", t.pvp);
                for (int i = 0; i < ECHEST_SIZE; i++) {
                    if (t.echest[i] != null) y.set(base + "echest." + i, t.echest[i]);
                }
            }
            try {
                host.getDataFolder().mkdirs();
                y.save(dataFile);
            } catch (IOException ex) {
                host.getLogger().log(Level.SEVERE, "Could not save teams.yml", ex);
            }
        }

        // ---------- Helpers ----------

        private Team teamOfPlayer(UUID id) {
            String k = teamOf.get(id);
            return k == null ? null : teams.get(k);
        }

        private void sendTeam(Team t, String msg) {
            for (UUID m : t.members) {
                Player p = Bukkit.getPlayer(m);
                if (p != null) p.sendMessage(msg);
            }
        }

        private int maxSize() {
            return cfg.getInt("max-team-size", 10);
        }

        private void removeFromTeam(UUID id, Team t) {
            t.members.remove(id);
            teamOf.remove(id);
            teamChatMode.remove(id);
        }

        private void ok(Player p, String msg) {
            p.sendMessage(ChatColor.GREEN + msg);
        }

        private void err(Player p, String msg) {
            p.sendMessage(ChatColor.RED + msg);
        }

        /** Closes a team's open ender chest and saves it before the team changes. */
        private void closeEchest(String teamKey) {
            EchestHolder h = echestOpen.remove(teamKey);
            if (h == null) return;
            Player viewer = Bukkit.getPlayer(h.viewer);
            if (viewer != null) viewer.closeInventory();
            saveEchest(h);
        }

        private void saveEchest(EchestHolder h) {
            Team t = teams.get(h.teamKey);
            if (t == null || h.inv == null) return;
            for (int i = 0; i < ECHEST_SIZE; i++) {
                ItemStack it = h.inv.getItem(i);
                t.echest[i] = it == null ? null : it.clone();
            }
            saveTeams();
        }

        /** Public lookup for other plugins (StickManDungeonsBeta calls this by reflection). Returns the team key or null. */
        public String teamKeyOf(UUID id) {
            return teamOf.get(id);
        }

        private UUID resolve(String name) {
            return Bukkit.getOfflinePlayer(name).getUniqueId();
        }

        // ---------- Commands ----------

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage(ChatColor.RED + "Players only.");
                return true;
            }
            if (args.length == 0) {
                help(p);
                return true;
            }
            String sub = args[0].toLowerCase(Locale.ROOT);
            UUID uid = p.getUniqueId();
            Team mine = teamOfPlayer(uid);

            switch (sub) {
                case "help" -> help(p);

                case "create" -> {
                    if (args.length < 2) { err(p, "Usage: /stickteam create <name>"); return true; }
                    if (mine != null) { err(p, "You are already in a team. Leave it first."); return true; }
                    if (!TEAM_NAME.matcher(args[1]).matches()) { err(p, "Team names are 3-16 letters, numbers or underscores."); return true; }
                    if (teams.containsKey(args[1].toLowerCase(Locale.ROOT))) { err(p, "That team name is taken."); return true; }
                    Team t = new Team(args[1], uid);
                    teams.put(t.key(), t);
                    teamOf.put(uid, t.key());
                    saveTeams();
                    ok(p, "Created team " + t.name + ". Invite players with /stickteam invite <player>.");
                }

                case "invite" -> {
                    if (mine == null) { err(p, "You are not in a team."); return true; }
                    if (!mine.leader.equals(uid)) { err(p, "Only the team leader can invite players."); return true; }
                    if (args.length < 2) { err(p, "Usage: /stickteam invite <player>"); return true; }
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target == null) { err(p, "That player is not online."); return true; }
                    if (teamOfPlayer(target.getUniqueId()) != null) { err(p, "That player is already in a team."); return true; }
                    if (mine.members.size() >= maxSize()) { err(p, "Your team is full (" + maxSize() + " members)."); return true; }
                    invites.put(target.getUniqueId(), mine.key());
                    ok(p, "Invited " + target.getName() + " to " + mine.name + ".");
                    target.sendMessage(ChatColor.GOLD + "You were invited to team " + mine.name
                            + ". Type /stickteam join " + mine.name + " to accept.");
                }

                case "join", "accept" -> {
                    String pending = invites.get(uid);
                    String want = args.length >= 2 ? args[1].toLowerCase(Locale.ROOT) : pending;
                    if (want == null || !want.equals(pending)) { err(p, "You have no invite to that team."); return true; }
                    if (mine != null) { err(p, "Leave your current team first."); return true; }
                    Team t = teams.get(want);
                    if (t == null) { invites.remove(uid); err(p, "That team no longer exists."); return true; }
                    if (t.members.size() >= maxSize()) { err(p, "That team is full."); return true; }
                    invites.remove(uid);
                    t.members.add(uid);
                    teamOf.put(uid, t.key());
                    saveTeams();
                    sendTeam(t, ChatColor.GREEN + p.getName() + " joined the team.");
                }

                case "leave" -> {
                    if (mine == null) { err(p, "You are not in a team."); return true; }
                    if (mine.leader.equals(uid)) {
                        if (mine.members.size() > 1) {
                            err(p, "Transfer leadership with /stickteam transfer <player> first, or use /stickteam disband.");
                            return true;
                        }
                        closeEchest(mine.key());
                        teams.remove(mine.key());
                        removeFromTeam(uid, mine);
                        saveTeams();
                        ok(p, "You left and " + mine.name + " was disbanded.");
                        return true;
                    }
                    sendTeam(mine, ChatColor.YELLOW + p.getName() + " left the team.");
                    removeFromTeam(uid, mine);
                    saveTeams();
                    ok(p, "You left " + mine.name + ".");
                }

                case "kick" -> {
                    if (mine == null || !mine.leader.equals(uid)) { err(p, "Only the team leader can kick players."); return true; }
                    if (args.length < 2) { err(p, "Usage: /stickteam kick <player>"); return true; }
                    UUID target = resolve(args[1]);
                    if (!mine.members.contains(target)) { err(p, "That player is not in your team."); return true; }
                    if (target.equals(uid)) { err(p, "You cannot kick yourself."); return true; }
                    removeFromTeam(target, mine);
                    saveTeams();
                    sendTeam(mine, ChatColor.YELLOW + args[1] + " was kicked from the team.");
                    Player tp = Bukkit.getPlayer(target);
                    if (tp != null) tp.sendMessage(ChatColor.RED + "You were kicked from " + mine.name + ".");
                }

                case "transfer" -> {
                    if (mine == null || !mine.leader.equals(uid)) { err(p, "Only the team leader can transfer leadership."); return true; }
                    if (args.length < 2) { err(p, "Usage: /stickteam transfer <player>"); return true; }
                    UUID target = resolve(args[1]);
                    if (!mine.members.contains(target)) { err(p, "That player is not in your team."); return true; }
                    mine.leader = target;
                    saveTeams();
                    sendTeam(mine, ChatColor.GOLD + args[1] + " is now the team leader.");
                }

                case "disband" -> {
                    if (mine == null || !mine.leader.equals(uid)) { err(p, "Only the team leader can disband the team."); return true; }
                    sendTeam(mine, ChatColor.RED + "" + ChatColor.BOLD + mine.name + " was disbanded by its leader.");
                    closeEchest(mine.key());
                    for (UUID m : new ArrayList<>(mine.members)) removeFromTeam(m, mine);
                    teams.remove(mine.key());
                    saveTeams();
                }

                case "info" -> {
                    Team t = args.length >= 2 ? teams.get(args[1].toLowerCase(Locale.ROOT)) : mine;
                    if (t == null) { err(p, args.length >= 2 ? "No team called " + args[1] + "." : "You are not in a team."); return true; }
                    p.sendMessage(ChatColor.GOLD + "Team " + t.name + " (" + t.members.size() + "/" + maxSize() + ")");
                    p.sendMessage(ChatColor.GRAY + "Leader: " + ChatColor.WHITE + Bukkit.getOfflinePlayer(t.leader).getName());
                    StringBuilder names = new StringBuilder();
                    for (UUID m : t.members) {
                        if (names.length() > 0) names.append(", ");
                        names.append(Bukkit.getOfflinePlayer(m).getName());
                    }
                    p.sendMessage(ChatColor.GRAY + "Members: " + ChatColor.WHITE + names);
                    p.sendMessage(ChatColor.GRAY + "Team PvP: " + (t.pvp ? ChatColor.RED + "on" : ChatColor.GREEN + "off"));
                }

                case "list" -> {
                    if (teams.isEmpty()) { p.sendMessage(ChatColor.GRAY + "There are no teams yet."); return true; }
                    p.sendMessage(ChatColor.GOLD + "Teams:");
                    for (Team t : teams.values()) {
                        p.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + t.name + ChatColor.GRAY + " (" + t.members.size() + ")");
                    }
                }

                case "pvp" -> {
                    if (mine == null || !mine.leader.equals(uid)) { err(p, "Only the team leader can change team PvP."); return true; }
                    if (args.length < 2 || !(args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("off"))) {
                        err(p, "Usage: /stickteam pvp <on|off>");
                        return true;
                    }
                    mine.pvp = args[1].equalsIgnoreCase("on");
                    saveTeams();
                    sendTeam(mine, ChatColor.GOLD + "Team PvP is now " + (mine.pvp ? "ON. Members can hurt each other." : "OFF. Members are protected from each other."));
                }

                case "chat" -> {
                    if (mine == null) { err(p, "You are not in a team."); return true; }
                    if (args.length >= 2) {
                        String msg = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                        sendTeam(mine, ChatColor.AQUA + "[Team] " + ChatColor.WHITE + p.getName() + ": " + ChatColor.RESET + msg);
                        return true;
                    }
                    if (teamChatMode.remove(uid)) {
                        ok(p, "Team chat is off. Your messages go to everyone again.");
                    } else {
                        teamChatMode.add(uid);
                        ok(p, "Team chat is on. Type normally to talk to your team. Use /stickteam chat again to turn it off.");
                    }
                }

                case "announce" -> {
                    if (mine == null || !mine.leader.equals(uid)) { err(p, "Only the team leader can make announcements."); return true; }
                    if (args.length < 2) { err(p, "Usage: /stickteam announce <message>"); return true; }
                    String msg = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                    sendTeam(mine, ChatColor.GOLD + "" + ChatColor.BOLD + "[Team Announcement] " + ChatColor.RESET + ChatColor.YELLOW + msg);
                }

                case "echest" -> {
                    if (mine == null) { err(p, "You are not in a team."); return true; }
                    if (echestOpen.containsKey(mine.key())) {
                        EchestHolder busy = echestOpen.get(mine.key());
                        err(p, "The team ender chest is in use by " + Bukkit.getOfflinePlayer(busy.viewer).getName() + ". Try again in a moment.");
                        return true;
                    }
                    EchestHolder h = new EchestHolder(mine.key(), uid);
                    h.inv = Bukkit.createInventory(h, ECHEST_SIZE, ChatColor.DARK_PURPLE + mine.name + " Team Ender Chest");
                    for (int i = 0; i < ECHEST_SIZE; i++) {
                        if (mine.echest[i] != null) h.inv.setItem(i, mine.echest[i].clone());
                    }
                    echestOpen.put(mine.key(), h);
                    p.openInventory(h.inv);
                }

                default -> {
                    err(p, "Unknown command. Try /stickteam help");
                }
            }
            return true;
        }

        private void help(Player p) {
            p.sendMessage(ChatColor.GOLD + "StickTeams");
            p.sendMessage(ChatColor.YELLOW + "/stickteam create <name> | invite <player> | join <name> | leave");
            p.sendMessage(ChatColor.YELLOW + "/stickteam kick <player> | transfer <player> | disband | info [team] | list");
            p.sendMessage(ChatColor.YELLOW + "/stickteam pvp <on|off> | chat [message] | announce <message> | echest");
        }

        // ---------- Events ----------

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onTeamPvp(EntityDamageByEntityEvent e) {
            if (!(e.getEntity() instanceof Player victim)) return;
            Player attacker = null;
            if (e.getDamager() instanceof Player dp) attacker = dp;
            else if (e.getDamager() instanceof Projectile pr && pr.getShooter() instanceof Player sp) attacker = sp;
            if (attacker == null || attacker.equals(victim)) return;
            Team t = teamOfPlayer(victim.getUniqueId());
            if (t == null || !t.members.contains(attacker.getUniqueId())) return;
            if (!t.pvp) {
                e.setCancelled(true);
                attacker.sendMessage(ChatColor.RED + "Team PvP is off. You cannot hurt your teammates.");
            }
        }

        @EventHandler
        public void onTeamChat(AsyncPlayerChatEvent e) {
            UUID id = e.getPlayer().getUniqueId();
            if (!teamChatMode.contains(id)) return;
            Team t = teamOfPlayer(id);
            if (t == null) {
                teamChatMode.remove(id);
                return;
            }
            e.setCancelled(true);
            String line = ChatColor.AQUA + "[Team] " + ChatColor.WHITE + e.getPlayer().getName() + ": " + ChatColor.RESET + e.getMessage();
            Bukkit.getScheduler().runTask(host, () -> sendTeam(t, line));
        }

        @EventHandler
        public void onEchestClose(InventoryCloseEvent e) {
            if (!(e.getInventory().getHolder() instanceof EchestHolder h)) return;
            saveEchest(h);
            echestOpen.remove(h.teamKey, h);
        }

        @EventHandler
        public void onJoin(PlayerJoinEvent e) {
            Team t = teamOfPlayer(e.getPlayer().getUniqueId());
            if (t != null) sendTeam(t, ChatColor.GRAY + e.getPlayer().getName() + " logged in.");
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent e) {
            UUID id = e.getPlayer().getUniqueId();
            teamChatMode.remove(id);
            for (EchestHolder h : new ArrayList<>(echestOpen.values())) {
                if (h.viewer.equals(id)) {
                    saveEchest(h);
                    echestOpen.remove(h.teamKey, h);
                }
            }
        }

        // ---------- Tab completion ----------

        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            if (args.length == 1) {
                List<String> subs = List.of("create", "invite", "join", "accept", "leave", "kick", "transfer", "disband",
                        "info", "list", "pvp", "chat", "announce", "echest", "help");
                return subs.stream().filter(s -> s.startsWith(args[0].toLowerCase(Locale.ROOT))).toList();
            }
            if (args.length == 2) {
                String sub = args[0].toLowerCase(Locale.ROOT);
                if (sub.equals("pvp")) return List.of("on", "off");
                if (sub.equals("join") || sub.equals("info")) {
                    return teams.keySet().stream().filter(s -> s.startsWith(args[1].toLowerCase(Locale.ROOT))).toList();
                }
                if (sub.equals("invite") || sub.equals("kick") || sub.equals("transfer")) {
                    List<String> names = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) names.add(p.getName());
                    return names.stream().filter(s -> s.toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT))).toList();
                }
            }
            return List.of();
        }
    }

    // ===== WandModule.java =====
    public static class WandModule implements StickModule, Listener, CommandExecutor, TabCompleter {

        private StickmanUniversal host;
        private FileConfiguration cfg;

        @Override
        public String name() {
            return "wand";
        }

        @Override
        public void enable(StickmanUniversal h) {
            host = h;
            cfg = host.moduleConfig("wand");

            
            zoneFile = new File(host.getDataFolder(), "zones.yml");
            wandKey = new NamespacedKey(host, "wand");
            loadZones();
            host.getServer().getPluginManager().registerEvents(this, host);
            for (String cmd : List.of("stickmanWorld", "yes", "no")) {
                host.getCommand(cmd).setExecutor(this);
            }
            host.getCommand("stickmanWorld").setTabCompleter(this);
            host.getLogger().info("StickmanWand loaded " + zones.size() + " zones.");
        }

        @Override
        public void disable() {
        }


        static final String MODE_SAFE = "safezone";
        static final String MODE_BLOCK = "blockprotect";

        /** A protected box between two corners. Mode decides what is protected. */
        record Zone(String name, String world, int x1, int y1, int z1, int x2, int y2, int z2, String mode) {
            boolean contains(Location l) {
                if (l == null || l.getWorld() == null || !l.getWorld().getName().equals(world)) return false;
                int x = l.getBlockX(), y = l.getBlockY(), z = l.getBlockZ();
                return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
            }

            boolean safe() {
                return mode.equals(MODE_SAFE);
            }
        }

        private final List<Zone> zones = new ArrayList<>();
        private final Map<UUID, String> modes = new HashMap<>();
        private final Map<UUID, Location> firstCorner = new HashMap<>();
        private final Map<UUID, Zone> pending = new HashMap<>();
        private final Map<UUID, Long> combatUntil = new HashMap<>();
        private final Map<UUID, Long> lastWarning = new HashMap<>();
        private NamespacedKey wandKey;
        private File zoneFile;

        

        // ---------- Storage ----------

        private void loadZones() {
            zones.clear();
            if (!zoneFile.exists()) return;
            YamlConfiguration y = YamlConfiguration.loadConfiguration(zoneFile);
            ConfigurationSection sec = y.getConfigurationSection("zones");
            if (sec == null) return;
            for (String name : sec.getKeys(false)) {
                ConfigurationSection z = sec.getConfigurationSection(name);
                if (z == null) continue;
                List<Integer> mn = z.getIntegerList("min");
                List<Integer> mx = z.getIntegerList("max");
                if (mn.size() < 3 || mx.size() < 3) continue;
                zones.add(new Zone(name, z.getString("world", "world"),
                        mn.get(0), mn.get(1), mn.get(2), mx.get(0), mx.get(1), mx.get(2),
                        z.getString("mode", MODE_SAFE)));
            }
        }

        private void saveZones() {
            YamlConfiguration y = new YamlConfiguration();
            for (Zone z : zones) {
                String base = "zones." + z.name() + ".";
                y.set(base + "world", z.world());
                y.set(base + "mode", z.mode());
                y.set(base + "min", List.of(z.x1(), z.y1(), z.z1()));
                y.set(base + "max", List.of(z.x2(), z.y2(), z.z2()));
            }
            try {
                host.getDataFolder().mkdirs();
                y.save(zoneFile);
            } catch (IOException ex) {
                host.getLogger().log(Level.SEVERE, "Could not save zones.yml", ex);
            }
        }

        // ---------- Helpers ----------

        private String msg(String key, String... pairs) {
            String text = cfg.getString("messages." + key, key);
            for (int i = 0; i + 1 < pairs.length; i += 2) text = text.replace(pairs[i], pairs[i + 1]);
            return ChatColor.translateAlternateColorCodes('&', text);
        }

        private Zone zoneAt(Location l) {
            for (Zone z : zones) if (z.contains(l)) return z;
            return null;
        }

        private boolean inSafezone(Location l) {
            Zone z = zoneAt(l);
            return z != null && z.safe();
        }

        private boolean inCombat(UUID id) {
            return combatUntil.getOrDefault(id, 0L) > System.currentTimeMillis();
        }

        private boolean zoneNameTaken(String name) {
            for (Zone z : zones) if (z.name().equalsIgnoreCase(name)) return true;
            return false;
        }

        private String nextName(String mode) {
            String base = mode.equals(MODE_SAFE) ? "safezone" : "blockzone";
            int i = 1;
            while (zoneNameTaken(base + i)) i++;
            return base + i;
        }

        private ItemStack wandItem() {
            ItemStack it = new ItemStack(Material.BLAZE_ROD);
            ItemMeta meta = it.getItemMeta();
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Stickman Wand");
            meta.setLore(List.of(
                    ChatColor.GRAY + "Shift + right-click a block to set corner 1.",
                    ChatColor.GRAY + "Shift + right-click another block to set corner 2.",
                    ChatColor.GRAY + "Then say " + ChatColor.GREEN + "/yes" + ChatColor.GRAY + " or " + ChatColor.RED + "/no"));
            meta.getPersistentDataContainer().set(wandKey, PersistentDataType.BYTE, (byte) 1);
            it.setItemMeta(meta);
            return it;
        }

        private boolean isWand(ItemStack it) {
            return it != null && it.hasItemMeta() && it.getItemMeta().getPersistentDataContainer().has(wandKey, PersistentDataType.BYTE);
        }

        private boolean bypass(Player p) {
            return p.hasPermission("stickman.bypass");
        }

        // ---------- Wand selection ----------

        @EventHandler(priority = EventPriority.HIGH)
        public void onWandClick(PlayerInteractEvent e) {
            if (e.getHand() != EquipmentSlot.HAND) return;
            if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getClickedBlock() == null) return;
            Player p = e.getPlayer();
            if (!p.isSneaking() || !isWand(e.getItem())) return;
            e.setCancelled(true);
            if (!p.hasPermission("stickman.admin")) return;

            UUID id = p.getUniqueId();
            Location clicked = e.getClickedBlock().getLocation();
            String mode = modes.getOrDefault(id, MODE_SAFE);

            if (pending.containsKey(id)) {
                // Starting a new selection after a confirmation is pending replaces the old one.
                pending.remove(id);
                firstCorner.remove(id);
            }
            if (!firstCorner.containsKey(id)) {
                firstCorner.put(id, clicked);
                p.sendMessage(msg("corner-1", "%x%", String.valueOf(clicked.getBlockX()),
                        "%y%", String.valueOf(clicked.getBlockY()), "%z%", String.valueOf(clicked.getBlockZ())));
                return;
            }

            Location a = firstCorner.remove(id);
            int x1 = Math.min(a.getBlockX(), clicked.getBlockX()), x2 = Math.max(a.getBlockX(), clicked.getBlockX());
            int y1 = Math.min(a.getBlockY(), clicked.getBlockY()), y2 = Math.max(a.getBlockY(), clicked.getBlockY());
            int z1 = Math.min(a.getBlockZ(), clicked.getBlockZ()), z2 = Math.max(a.getBlockZ(), clicked.getBlockZ());
            Zone candidate = new Zone(nextName(mode), clicked.getWorld().getName(), x1, y1, z1, x2, y2, z2, mode);
            pending.put(id, candidate);
            p.sendMessage(msg(mode.equals(MODE_SAFE) ? "confirm-safezone" : "confirm-blockzone"));
        }

        // ---------- Commands ----------

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            switch (cmd.getName().toLowerCase(Locale.ROOT)) {
                case "yes" -> confirm(sender, true);
                case "no" -> confirm(sender, false);
                case "stickmanworld" -> stickman(sender, args);
                default -> { }
            }
            return true;
        }

        private void confirm(CommandSender sender, boolean yes) {
            if (!(sender instanceof Player p)) { sender.sendMessage(ChatColor.RED + "Players only."); return; }
            if (!p.hasPermission("stickman.admin")) { p.sendMessage(msg("no-permission")); return; }
            Zone z = pending.remove(p.getUniqueId());
            firstCorner.remove(p.getUniqueId());
            if (z == null) { p.sendMessage(msg("nothing-pending")); return; }
            if (!yes) { p.sendMessage(msg("cancelled")); return; }
            Zone toSave = zoneNameTaken(z.name())
                    ? new Zone(nextName(z.mode()), z.world(), z.x1(), z.y1(), z.z1(), z.x2(), z.y2(), z.z2(), z.mode())
                    : z;
            zones.add(toSave);
            saveZones();
            p.sendMessage(msg(toSave.safe() ? "created-safezone" : "created-blockzone", "%name%", toSave.name()));
        }

        private void stickman(CommandSender sender, String[] args) {
            if (!sender.hasPermission("stickman.admin")) { sender.sendMessage(msg("no-permission")); return; }
            String sub = args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : "help";
            switch (sub) {
                case "wand" -> {
                    if (!(sender instanceof Player p)) { sender.sendMessage(ChatColor.RED + "Players only."); return; }
                    if (args.length >= 2 && args[1].equalsIgnoreCase("mode")) {
                        if (args.length < 3) { p.sendMessage(msg("usage-mode")); return; }
                        String mode = args[2].toLowerCase(Locale.ROOT);
                        if (!mode.equals(MODE_SAFE) && !mode.equals(MODE_BLOCK)) { p.sendMessage(msg("usage-mode")); return; }
                        modes.put(p.getUniqueId(), mode);
                        p.sendMessage(msg("mode-set", "%mode%", mode.equals(MODE_SAFE) ? "safezone" : "block protection"));
                        return;
                    }
                    if (args.length >= 2 && args[1].equalsIgnoreCase("clear")) {
                        firstCorner.remove(p.getUniqueId());
                        pending.remove(p.getUniqueId());
                        p.sendMessage(msg("selection-cleared"));
                        return;
                    }
                    p.getInventory().addItem(wandItem());
                    p.sendMessage(msg("wand-given"));
                }
                case "list" -> {
                    if (zones.isEmpty()) { sender.sendMessage(msg("no-zones")); return; }
                    sender.sendMessage(ChatColor.GOLD + "Zones (" + zones.size() + "):");
                    for (Zone z : zones) {
                        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + z.name() + ChatColor.GRAY + " ("
                                + (z.safe() ? "safezone" : "block protection") + ") in " + z.world() + " from "
                                + z.x1() + "," + z.y1() + "," + z.z1() + " to " + z.x2() + "," + z.y2() + "," + z.z2());
                    }
                }
                case "remove" -> {
                    if (args.length < 2) { sender.sendMessage(ChatColor.RED + "Usage: /stickmanWorld remove <name>"); return; }
                    boolean removed = zones.removeIf(z -> z.name().equalsIgnoreCase(args[1]));
                    if (removed) { saveZones(); sender.sendMessage(msg("zone-removed", "%name%", args[1])); }
                    else sender.sendMessage(msg("no-such-zone"));
                }
                default -> {
                    sender.sendMessage(ChatColor.GOLD + "StickmanWand");
                    sender.sendMessage(ChatColor.YELLOW + "/stickmanWorld wand" + ChatColor.GRAY + " - get the wand");
                    sender.sendMessage(ChatColor.YELLOW + "/stickmanWorld wand mode <safezone|blockprotect>" + ChatColor.GRAY + " - choose what the next zone does");
                    sender.sendMessage(ChatColor.YELLOW + "/stickmanWorld wand clear" + ChatColor.GRAY + " - clear your selection");
                    sender.sendMessage(ChatColor.YELLOW + "/stickmanWorld list | remove <name>" + ChatColor.GRAY + " - manage zones");
                    sender.sendMessage(ChatColor.YELLOW + "/yes" + ChatColor.GRAY + " or " + ChatColor.YELLOW + "/no" + ChatColor.GRAY + " - confirm a new zone");
                }
            }
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            if (!cmd.getName().equalsIgnoreCase("stickmanWorld")) return List.of();
            if (args.length == 1) return List.of("wand", "list", "remove").stream().filter(s -> s.startsWith(args[0].toLowerCase(Locale.ROOT))).toList();
            if (args.length == 2 && args[0].equalsIgnoreCase("wand")) return List.of("mode", "clear");
            if (args.length == 3 && args[1].equalsIgnoreCase("mode")) return List.of(MODE_SAFE, MODE_BLOCK);
            if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
                List<String> names = new ArrayList<>();
                for (Zone z : zones) names.add(z.name());
                return names;
            }
            return List.of();
        }

        // ---------- Protection rules ----------

        /** No breaking inside any zone. Admins with stickman.bypass are exempt. */
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onBreak(BlockBreakEvent e) {
            Zone z = zoneAt(e.getBlock().getLocation());
            if (z == null || bypass(e.getPlayer())) return;
            e.setCancelled(true);
            e.getPlayer().sendMessage(z.safe() ? msg("no-break-safezone") : msg("no-break-zone"));
        }

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onPlace(BlockPlaceEvent e) {
            Zone z = zoneAt(e.getBlock().getLocation());
            if (z == null || bypass(e.getPlayer())) return;
            e.setCancelled(true);
            e.getPlayer().sendMessage(z.safe() ? msg("no-place-safezone") : msg("no-place-zone"));
        }

        /** Safezones stop all fighting between players. Block-protection zones allow PvP. */
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onPlayerHit(EntityDamageByEntityEvent e) {
            if (!(e.getEntity() instanceof Player victim)) return;
            Player attacker = null;
            if (e.getDamager() instanceof Player dp) attacker = dp;
            else if (e.getDamager() instanceof Projectile pr && pr.getShooter() instanceof Player sp) attacker = sp;
            if (attacker == null || attacker.equals(victim)) return;

            if (inSafezone(victim.getLocation()) || inSafezone(attacker.getLocation())) {
                e.setCancelled(true);
                attacker.sendMessage(msg("no-fight-safezone"));
                return;
            }
            // A real player-vs-player hit puts both players in combat.
            long until = System.currentTimeMillis() + cfg.getLong("combat-seconds", 15) * 1000L;
            combatUntil.put(attacker.getUniqueId(), until);
            combatUntil.put(victim.getUniqueId(), until);
        }

        /** Players in combat cannot walk into a safezone. */
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onMove(PlayerMoveEvent e) {
            if (e.getFrom().getBlockX() == e.getTo().getBlockX()
                    && e.getFrom().getBlockY() == e.getTo().getBlockY()
                    && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
            Player p = e.getPlayer();
            if (!inCombat(p.getUniqueId())) return;
            if (inSafezone(e.getTo()) && !inSafezone(e.getFrom())) {
                e.setTo(e.getFrom());
                warnCombat(p);
            }
        }

        /** Teleporting into a safezone while in combat is blocked too. */
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void onTeleport(PlayerTeleportEvent e) {
            Player p = e.getPlayer();
            if (!inCombat(p.getUniqueId())) return;
            if (inSafezone(e.getTo()) && !inSafezone(e.getFrom())) {
                e.setCancelled(true);
                warnCombat(p);
            }
        }

        private void warnCombat(Player p) {
            long now = System.currentTimeMillis();
            if (now - lastWarning.getOrDefault(p.getUniqueId(), 0L) < 3000L) return;
            lastWarning.put(p.getUniqueId(), now);
            p.sendMessage(msg("combat-no-enter"));
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent e) {
            UUID id = e.getPlayer().getUniqueId();
            firstCorner.remove(id);
            pending.remove(id);
            lastWarning.remove(id);
        }
    }
}
