# AFK Plugin - v1.0 Spigot 1.8.8
A basic, easy-to-use AFK plugin.

## Permissions ##
<ul>
<li><strong>afk.allow</strong> (Default: ON) - If on, allows the player to enter an afk state through idling.</li>
<li><strong>afk.toggle</strong> (Default: ON) - If on, allows the player to use the /afk command. <strong>afk.allow</strong> MUST be active alongside this.</li>
<li><strong>afk.*</strong> (Default: ON) - If on, enables both <strong>afk.allow</strong> and <strong>afk.toggle</strong>.</li>
</ul>

## Commands ##
<ul>
<li><strong>/afk</strong> - Automatically toggles the player's afk state.</li>
</ul>

## Effects of being AFK ##
By default, an afk person will:
<ul>
<li>Be exempt from needing to be in a bed to sleep through the night (configurable).</li>
<li>Have a prefix on their nameplate and tab menu (configurable).</li>
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
