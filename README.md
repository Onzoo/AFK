# AFK Plugin - v1.0 Spigot 1.8.8
Lightweight AFK plugin meant with a few bits tacked on.

## Permissions ##
<ul>
<li><strong>afk.allow</strong> - If set to false, prevents the player from being afk (whether via /afk or by standing still).</li>
<li><strong>afk.toggle</strong> - If set to false, prevents the player from using the /afk command.
</ul>

## Commands ##
<ul>
<li><strong>/afk</strong> - Automatically toggles the player's afk state.</li>
</ul>

## Technical ##
The player will enter an afk state under *any* of the following conditions:
<ul>
<li>Typing /afk whilst not afk.</li>
<li>Standing still for 3 minutes (180 seconds).</li>
</ul>

The player will exit the afk state under *any* of the following conditions:
<ul>
<li>Typing /afk whilst afk.</li>
<li>Moving.</li>
<li>Typing into the chat.</li>
</ul>

## Server Strain ##
Every 4 seconds (80 server ticks) the plugin will check if a player has been idle for 3 minutes or longer, and if so, will put them in an afk state.
