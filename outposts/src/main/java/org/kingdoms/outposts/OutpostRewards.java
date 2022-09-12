package org.kingdoms.outposts;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.utils.MathUtils;
import org.kingdoms.utils.string.StringUtils;

import java.util.List;

public class OutpostRewards {
    private String resourcePoints;
    private String money;
    private List<String> commands;
    private List<ItemStack> items;

    public OutpostRewards(String resourcePoints, String money, List<String> commands, List<ItemStack> items) {
        this.resourcePoints = resourcePoints;
        this.money = money;
        this.commands = commands;
        this.items = items;
    }

    public int getResourcePoints(int lvl) {
        return (int) MathUtils.evaluateEquation(resourcePoints, "lvl", lvl);
    }

    public void performCommands(Kingdom kingdom) {
        for (OfflinePlayer member : kingdom.getPlayerMembers()) {
            StringUtils.performCommands(member, commands);
        }
    }

    public double getMoney(int lvl) {
        return MathUtils.evaluateEquation(money, "lvl", lvl);
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public String getMoney() {
        return money;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setResourcePoints(String resourcePoints) {
        this.resourcePoints = resourcePoints;
    }

    public String getResourcePoints() {
        return resourcePoints;
    }
}