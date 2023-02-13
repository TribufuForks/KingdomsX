package org.kingdoms.peacetreaties.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitTask;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.namespace.Namespace;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.data.Pair;
import org.kingdoms.gui.GUIAccessor;
import org.kingdoms.gui.InteractiveGUI;
import org.kingdoms.gui.OptionHandler;
import org.kingdoms.locale.messenger.Messenger;
import org.kingdoms.main.KLogger;
import org.kingdoms.peacetreaties.PeaceTreatiesAddon;
import org.kingdoms.peacetreaties.config.PeaceTreatyConfig;
import org.kingdoms.peacetreaties.config.PeaceTreatyGUI;
import org.kingdoms.peacetreaties.config.PeaceTreatyLang;
import org.kingdoms.peacetreaties.data.PeaceTreaty;
import org.kingdoms.peacetreaties.data.WarPoint;
import org.kingdoms.peacetreaties.terms.TermGrouping;
import org.kingdoms.peacetreaties.terms.TermGroupingOptions;
import org.kingdoms.peacetreaties.terms.TermProvider;
import org.kingdoms.peacetreaties.terms.TermRegistry;
import org.kingdoms.utils.MathUtils;
import org.kingdoms.utils.time.TimeUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class StandardPeaceTreatyEditor {
    private static final Map<UUID, PendingContract> PENDING_CONTRACTS = new HashMap<>();

    private static final class PendingContract {
        private final BukkitTask reminderTask;
        private final PeaceTreaty contract;

        private void cancelReminder() {
            if (reminderTask != null) reminderTask.cancel();
        }

        private PendingContract(PeaceTreaty contract, boolean remind) {
            this.contract = contract;

            if (!remind) {
                this.reminderTask = null;
            } else {
                long every = TimeUtils.millisToTicks(PeaceTreatyConfig.UNFINISHED_CONTRACT_REMINDER.getManager().getTimeMillis());
                this.reminderTask = Bukkit.getScheduler().runTaskTimerAsynchronously(PeaceTreatiesAddon.get(), () -> {
                    Player player = contract.getPlayer().getPlayer();
                    if (player == null) return;
                    PeaceTreatyLang.EDITOR_UNFINISHED.sendMessage(player);
                }, every, every);
            }
        }
    }

    public static boolean hasPendingContract(UUID player) {
        return PENDING_CONTRACTS.containsKey(player);
    }

    private final Player player;
    private final boolean isAdmin;
    private InteractiveGUI gui;
    private final PeaceTreaty peaceTreaty;
    private final AtomicBoolean isInsideNestedGUI = new AtomicBoolean();

    public static StandardPeaceTreatyEditor fromContract(PeaceTreaty peaceTreaty) {
        Player player = peaceTreaty.getPlayer().getPlayer();
        return new StandardPeaceTreatyEditor(player, peaceTreaty);
    }

    public static StandardPeaceTreatyEditor resume(Player player) {
        PendingContract session = PENDING_CONTRACTS.get(player.getUniqueId());
        if (session == null) return null;

        session.cancelReminder();
        StandardPeaceTreatyEditor editor = new StandardPeaceTreatyEditor(player, session.contract);
        editor.open();
        return editor;
    }

    public void pause(boolean remind) {
        PENDING_CONTRACTS.put(player.getUniqueId(), new PendingContract(peaceTreaty, remind));
    }

    public StandardPeaceTreatyEditor(Player player, PeaceTreaty peaceTreaty) {
        this.player = player;
        this.peaceTreaty = peaceTreaty;
        this.isAdmin = KingdomPlayer.getKingdomPlayer(player).isAdmin();
        pause(false);
    }

    public static void kingdomNotAvailable(Kingdom kingdom) {
        List<UUID> removeList = new ArrayList<>();
        for (Map.Entry<UUID, PendingContract> entry : PENDING_CONTRACTS.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            boolean remove = false;
            if (entry.getValue().contract.getVictimKingdomId().equals(kingdom.getId())) {
                if (player != null) PeaceTreatyLang.EDITOR_VICTIM_KINGDOM_DISBANDED.sendError(player);
                remove = true;
            } else if (entry.getValue().contract.getProposerKingdomId().equals(kingdom.getId())) {
                // No need to send a message, they're already notified when their kingdom gets disbanded.
                remove = true;
            }

            if (remove) {
                if (player != null) player.closeInventory();
                removeList.add(entry.getKey());
                entry.getValue().cancelReminder();
            }
        }

        removeList.forEach(PENDING_CONTRACTS::remove);
    }

    public static void outOfKingdom(KingdomPlayer kp) {
        Player player = kp.getPlayer();
        if (player != null) player.closeInventory();

        PendingContract pendingContract = PENDING_CONTRACTS.remove(kp.getId());
        if (pendingContract == null) return;
        pendingContract.cancelReminder();
    }

    private InteractiveGUI prepareGUI(String path) {
        InteractiveGUI gui = GUIAccessor.prepare(player, path);
        peaceTreaty.getPlaceholderContextProvider(gui.getSettings());
        return gui;
    }

    public void open() {
        this.gui = prepareGUI(PeaceTreatyGUI.EDITOR.getGUIPath());

        for (Pair<String, OptionHandler> termOption : gui.getRemainingOptionsOf("term")) {
            TermGroupingOptions grouping = TermRegistry.getTermGroupings().get(termOption.getKey());
            if (grouping == null) {
                KLogger.warn("Unknown peace treaty grouping term '" + termOption.getKey() + "' in GUI.");
                continue;
            }

            termOption.getValue().getSettings().raw("war_points", grouping.getRequiredWarPoints(peaceTreaty));
            peaceTreaty.getTerms().values().forEach(group -> group.getTerms().values().forEach(term ->
                    termOption.getValue().getSettings().inheritPlaceholders(term.getEdits())));
            termOption.getValue().on(ClickType.LEFT, ctx -> {
                Optional<Messenger> cantApply = grouping.getTerms().values().stream()
                        .map(x -> x.canApply(grouping, peaceTreaty))
                        .filter(Objects::nonNull)
                        .findFirst();

                if (cantApply.isPresent()) {
                    cantApply.get().sendError(player);
                    return;
                }

                double totalRequiredWarPoints = peaceTreaty.getTotalRequiredWarPoints() + grouping.getRequiredWarPoints(peaceTreaty);
                double ownedWarPoints = WarPoint.getWarPoints(peaceTreaty.getProposerKingdom(), peaceTreaty.getVictimKingdom());
                ctx.getSettings().raw("total_required_war_points", totalRequiredWarPoints);
                if (!isAdmin && totalRequiredWarPoints > ownedWarPoints) {
                    ctx.sendError(PeaceTreatyLang.TERM_INSUFFICIENT_WAR_POINTS);
                    return;
                }

                String gui = grouping.getConfig().getString("gui");
                if (gui == null) {
                    long termsThatRequireData = grouping.getTerms().values().stream().filter(x -> x.requiresData(grouping)).limit(2).count();
                    if (termsThatRequireData > 1) {
                        gui = PeaceTreatyGUI.DEFAULT$TERM$EDITOR.getGUIPath();
                    } else if (termsThatRequireData == 0) {
                        if (peaceTreaty.getTerms().remove(grouping.getName()) == null) {
                            // Didn't have the term, add it.
                            grouping.getTerms().values().forEach(x -> peaceTreaty.addOrCreateTerm(grouping, x.construct()));
                        }
                        open();
                        return;
                    } else {
                        TermProvider term = grouping.getTerms().values().stream().filter(x -> x.requiresData(grouping)).findFirst().get();
                        term.prompt(grouping, this).thenAccept(x -> {
                            if (x != null) peaceTreaty.addOrCreateTerm(grouping, x);
                            open();
                        });
                        return;
                    }
                }

                openTermGroupingGUI(grouping, gui);
            }).on(ClickType.RIGHT, ctx -> {
                peaceTreaty.getTerms().remove(grouping.getName());
                open();
            }).done();
        }

        AtomicBoolean wasSent = new AtomicBoolean();
        gui.option("send").onNormalClicks(ctx -> {
            int minTerms = (int) MathUtils.eval(PeaceTreatyConfig.MIN_TERMS.getManager().getMathExpression(), ctx.getSettings());
            ctx.getSettings().raw("terms_min", minTerms);

            if (peaceTreaty.getTerms().size() < minTerms) {
                ctx.sendError(PeaceTreatyLang.TERMS_MIN);
                return;
            }

            WarPoint.addWarPoints(peaceTreaty.getProposerKingdom(), peaceTreaty.getVictimKingdom(), -peaceTreaty.getTotalRequiredWarPoints());

            wasSent.set(true);
            player.closeInventory();
            peaceTreaty.propose();
            removePending();
        }).done();

        gui.option("pause").onNormalClicks(ctx -> {
            removePending();
            player.closeInventory();
        }).done();

        gui.onClose(() -> {
            if (wasSent.get() || isInsideNestedGUI.get()) return;
            pause(true);
            PeaceTreatyLang.EDITOR_PAUSED.sendMessage(player);
        });

        gui.open();
        isInsideNestedGUI.set(false);
    }

    public void removePending() {
        PendingContract pending = PENDING_CONTRACTS.remove(player.getUniqueId());
        if (pending != null) pending.cancelReminder();
    }

    public InteractiveGUI openTermGroupingGUI(TermGroupingOptions grouping, String guiName) {
        isInsideNestedGUI.set(true);
        InteractiveGUI gui = prepareGUI(guiName);

        for (Pair<String, OptionHandler> termEntry : gui.getRemainingOptionsOf("term")) {
            TermProvider term = grouping.getTerms().get(Namespace.fromConfigString(termEntry.getKey()));
            if (term == null) {
                KLogger.error("Unknown term '" + termEntry.getKey() + "' for GUI: " + guiName);
                continue;
            }

            OptionHandler option = termEntry.getValue();
            option.setSettings(peaceTreaty.getOrCreateTerm(grouping, term).getEdits());

            option.on(ClickType.LEFT, ctx -> {
                term.prompt(grouping, this).thenAccept(x -> {
                    if (x != null) peaceTreaty.addOrCreateTerm(grouping, x);
                    openTermGroupingGUI(grouping, guiName);
                });
            }).on(ClickType.RIGHT, () -> {
                TermGrouping groupingTerm = peaceTreaty.getTerms().get(grouping.getName());
                if (groupingTerm != null) groupingTerm.getTerms().remove(term.getNamespace());
                openTermGroupingGUI(grouping, guiName);
            }).done();
        }

        gui.push("back", this::open);
        gui.open();

        return gui;
    }

    public Player getPlayer() {
        return player;
    }

    public InteractiveGUI getGui() {
        return gui;
    }

    public PeaceTreaty getPeaceTreaty() {
        return peaceTreaty;
    }
}
