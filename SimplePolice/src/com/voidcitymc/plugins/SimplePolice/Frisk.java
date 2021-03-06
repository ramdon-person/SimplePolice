package com.voidcitymc.plugins.SimplePolice;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Frisk implements Listener {
    private final FriskCooldownManager cooldownManager = new FriskCooldownManager();


    SPPlugin plugin;

    public Frisk(SPPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFrisk(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            Worker work = new Worker(plugin);
            if (work.friskingEnabled() && work.alreadyPolice(event.getPlayer().getUniqueId().toString()) && event.getPlayer().getInventory().getItemInMainHand().getType().equals(work.getFriskStickMaterial())) {
                //begin frisk
                Player suspectedPlayer = (Player) event.getRightClicked();

                long timeLeft = System.currentTimeMillis() - cooldownManager.getCooldown(event.getPlayer().getUniqueId(), suspectedPlayer.getUniqueId());

                if (TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= FriskCooldownManager.DEFAULT_COOLDOWN) {
                    cooldownManager.setCooldown(event.getPlayer().getUniqueId(), suspectedPlayer.getUniqueId(), System.currentTimeMillis());


                    suspectedPlayer.sendMessage(Messages.getMessage("FriskMSG"));
                    event.getPlayer().sendMessage(Messages.getMessage("FriskPolice", suspectedPlayer.getName()));
                    PlayerInventory invToScan = suspectedPlayer.getInventory();

                    ItemStack[] contents = invToScan.getContents().clone();
                    int i = 0;

                    ArrayList<String> textToReturn = new ArrayList<>();
                    boolean guilty = false;
                    //Double priceToPay = 0.0;

                    while (i < contents.length) {
                        if (work.isItemContraband(contents[i])) {
                            if (plugin.getConfig().getInt("PercentOfFindingContraband") - (Math.random() * 100) >= 0) {

                                if (!Objects.requireNonNull(Objects.requireNonNull(invToScan.getItem(i)).getItemMeta()).getDisplayName().equals("")) {
                                    textToReturn.add(ChatColor.DARK_AQUA + "" + Objects.requireNonNull(invToScan.getItem(i)).getAmount() + "x " + Objects.requireNonNull(Objects.requireNonNull(invToScan.getItem(i)).getItemMeta()).getDisplayName());
                                } else {
                                    textToReturn.add(ChatColor.DARK_AQUA + "" + Objects.requireNonNull(invToScan.getItem(i)).getAmount() + "x " + work.capitalize((Objects.requireNonNull(invToScan.getItem(i)).getType().toString().replace("_", " ")).toLowerCase()));
                                }

                                guilty = true;
                                event.getPlayer().getInventory().addItem(invToScan.getContents()[i]);
                                suspectedPlayer.getInventory().setItem(i, null);


                            }

                        }
                        i++;
                    }

                    if (guilty) {
                        suspectedPlayer.sendMessage(Messages.getMessage("FriskGuilty"));
                        suspectedPlayer.sendMessage(textToReturn.toArray(new String[0]));
                    } else {
                        suspectedPlayer.sendMessage(Messages.getMessage("FriskNotGuilty"));
                    }

                    event.getPlayer().sendMessage(Messages.getMessage("FriskPoliceAfterMsg"));
                    if (textToReturn.size() < 1) {
                        textToReturn.add(Messages.getMessage("FriskNoItems"));
                    }

                    event.getPlayer().sendMessage(textToReturn.toArray(new String[0]));

                } else {
                    event.getPlayer().sendMessage(Messages.getMessage("FriskTimeLeft", String.valueOf(FriskCooldownManager.DEFAULT_COOLDOWN - TimeUnit.MILLISECONDS.toSeconds(timeLeft))));
                }

            }

        }

    }


}
