package com.onzo.afk;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class AFK extends JavaPlugin implements Listener {
	private Map<Player, Long> timeSinceActive = new HashMap<Player, Long>();
	private Map<Player, Location> playerLocations = new HashMap<Player, Location>();
	private ScoreboardManager manager;
	private Scoreboard scoreboard;
	private Team team;
	private boolean skipNight;
	private String enableMessage;
	private String disableMessage;
	private boolean enableNameplate;
	private String nameplatePrefix;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		FileConfiguration config = getConfig();

		skipNight = config.getBoolean("afk-skip-night");
		enableMessage = ChatColor.translateAlternateColorCodes('&', config.getString("afk-enable-message"));
		disableMessage = ChatColor.translateAlternateColorCodes('&', config.getString("afk-disable-message"));
		enableNameplate = config.getBoolean("afk-enable-nameplate");
		nameplatePrefix = ChatColor.translateAlternateColorCodes('&', config.getString("afk-nameplate-prefix"));

		manager = Bukkit.getScoreboardManager();
		scoreboard = manager.getNewScoreboard();
		team = scoreboard.registerNewTeam("afk");

		getServer().getPluginManager().registerEvents(this, this);
		if (enableNameplate)
			team.setPrefix(nameplatePrefix);

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player != null) {
				if (player.hasPermission("afk.allow")) {
					player.setScoreboard(scoreboard);
					updateTime(player);
					updateLocation(player);
				}
			}
		}

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player != null) {
						if (!player.getLocation().equals(playerLocations.get(player))) {
							setIsAfk(player, false);
						} else if (System.currentTimeMillis() - getTimeSinceActive(player) >= 180000) {
							setIsAfk(player, true);
						}
						updateLocation(player);
					}
				}
			}
		}, 0L, 80L);
	}

	@Override
	public void onDisable() {
		team.unregister();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("afk")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cYou must be a player to use this command.");
				return true;
			}
			Player player = (Player) sender;
			if (!(player.hasPermission("afk.toggle"))) {
				//player.sendMessage("§c§oYou do not have permission to use this command.");
				return false;
			}

			setIsAfk(player, !getIsAfk(player));
			updateLocation(player);
		}

		return true;
	}

	public Long getTimeSinceActive(Player player) {
		return timeSinceActive.get(player);
	}

	@SuppressWarnings("deprecation")
	public Boolean getIsAfk(Player player) {
		return team.hasPlayer(player);
	}

	public void updateTime(Player player) {
		timeSinceActive.put(player, System.currentTimeMillis());
	}

	@SuppressWarnings("deprecation")
	public void setIsAfk(Player player, Boolean afk) {
		if (getIsAfk(player) != afk) {
			if (afk) {
				player.sendMessage(enableMessage);
				team.addPlayer(player);
				player.setSleepingIgnored(true && skipNight);
			} else {
				player.sendMessage(disableMessage);
				team.removePlayer(player);
				player.setSleepingIgnored(false);
			}
		}
	}

	public void updateLocation(Player player) {
		playerLocations.put(player, player.getLocation());
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("afk.allow")) return;
		player.setScoreboard(scoreboard);
		updateTime(player);
		updateLocation(player);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (team.hasPlayer(player)) {
			team.removePlayer(player);
		}
		if (timeSinceActive.containsKey(player)) {
			timeSinceActive.remove(player);
		}
		if (playerLocations.containsKey(player)) {
			playerLocations.remove(player);
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		setIsAfk(player, false);
		updateTime(player);
		updateLocation(player);
	}
}