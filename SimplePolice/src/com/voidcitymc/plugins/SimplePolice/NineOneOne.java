package com.voidcitymc.plugins.SimplePolice;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class NineOneOne implements CommandExecutor {

// This method is called, when somebody uses our command
@Override
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
if (sender instanceof Player) {
Player player = (Player) sender;


ArrayList<Player> playerList = new ArrayList<Player>();

ArrayList<String> policeList = worker.ListPolice();
int i = 0;


//online police list
while (i < policeList.size()) {
	if (Bukkit.getPlayer(UUID.fromString(policeList.get(i))) != null) {
		playerList.add(Bukkit.getPlayer(worker.ListPolice().get(i)));
	}
	i++;
}

//send police message

i = 0;

while (i < playerList.size()) {
	playerList.get(i).sendMessage(ChatColor.DARK_AQUA + "[911] " + ChatColor.WHITE + sender.getName() + " needs help!");
	i++;
}



/////////////
/*
BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE 
BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE BUG HERE 
*/

/*
 * When there is one player online (myself) and I run /911 it says there are no players online, need to fix this!!!
*/

/////////

if (playerList.size() == 0) {
	player.sendMessage(ChatColor.DARK_AQUA + "There are no police online, you are on your own!");
} else {
	player.sendMessage(ChatColor.DARK_AQUA + "The police have gotten your message and will come asap!");
}



}
return true;
}


}
