package org.kingdoms.peacetreaties.config;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.kingdoms.config.Path;
import org.kingdoms.locale.LanguageEntry;
import org.kingdoms.locale.messenger.DefinedMessenger;

public enum PeaceTreatyLang implements DefinedMessenger {
    NOTIFICATION_SENDERS("{$s}%peacetreaty_requester_player% {$p}has sent a peace treaty proposal to {$s}%kingdoms_kingdom_name%\n" +
            "{$p}It can be viewed from {$es}hover:{/k peacetreaty review;&7Click to view;/k peacetreaty review} {$p}command.", 1),
    NOTIFICATION_RECEIVERS("{$e}The kingdom {$es}%kingdoms_kingdom_name% {$e}has sent you a peace treaty proposal.\n" +
            "It can be viewed from {$es}hover:{/k peacetreaty review;&7Click to view;/k peacetreaty review} {$e}command.", 1),
    EDITOR_UNFINISHED("{$p}You have an unfinished peace treaty contract. Edit it with " +
            "{$s}hover:{/k peacetreaty resume;&7Click to resume;/k peacetreaty resume}", 1),
    COMMAND_REVOKE_PEACETREATY_ALREADY_RECEIVED("{$e}Your kingdom has already received a peace treaty to that kingdom.", 1, 2, 3),
    COMMAND_REVOKE_PEACETREATY_ALREADY_SENT("{$e}Your kingdom has already sent a peace treaty to that kingdom.", 1, 2, 3),
    EDITOR_CANCELLED("{$e}Cancelled peace treaty.", 1),
    COMMAND_PEACETREATY_DESCRIPTION("{$s}Commands related to peace treaties."),
    COMMAND_PEACETREATY_NAME("peacetreaty"),
    COMMAND_PEACETREATY_ALREADY_ACCEPTED("{$e}Your kingdom has already accepted this peace treaty contract.", 1, 2),
    COMMAND_PEACETREATY_NO_CONTRACTS("{$e}Your kingdom didn't receive any peace treaty contracts."),
    COMMAND_PEACETREATY_NO_CONTRACT_FROM_KINGDOM("{$e}Your kingdom didn't receive any contracts from {$es}%kingdoms_name% {$e}kingdom.", 1, 2),
    COMMAND_PEACETREATY_NO_CONTRACT_TO_KINGDOM("{$e}Your kingdom doesn't have any contracts with {$es}%kingdoms_name% {$e}kingdom.", 1, 2),
    COMMAND_PEACETREATY_ALIASES("peacetreaties pt"),
    COMMAND_PEACETREATY_MISCUPGRADES_DESCRIPTION("{$s}Change misc upgrades of a kingdom under contract.", 1, 2, 3),
    COMMAND_PEACETREATY_MISCUPGRADES_USAGE("{$usage}pt miscUpgrades {$p}<kingdom>", 1, 2, 3),
    COMMAND_PEACETREATY_MISCUPGRADES_DOESNT_HAVE_TERM("{$e}The contract with the specified kingdom doesn't include misc upgrades.", 1, 2, 3),
    COMMAND_PEACETREATY_RESUME_NAME("resume", 1, 2, 3),
    COMMAND_PEACETREATY_RESUME_DESCRIPTION("{$s}Resumes a paused peace treaty contract from the editor.", 1, 2, 3),
    COMMAND_PEACETREATY_REVIEW_DESCRIPTION("{$s}Review a sent peace treaty contract in order to decide to accept it or not.", 1, 2, 3),
    COMMAND_PEACETREATY_REVIEW_NAME("review", 1, 2, 3),
    COMMAND_PEACETREATY_REVIEW_ALIASES("view", 1, 2, 3),
    COMMAND_PEACETREATY_REVIEW_HEADER("{$es}%kingdoms_other_name% {$e}peace treaty contains the following terms{$sep}:", 1, 2, 3),
    COMMAND_PEACETREATY_REVIEW_TERMS("{$sep}⚫ %term_message%", 1, 2, 3),
    COMMAND_PEACETREATY_REVIEW_FOOTER("{$e}You can accept this contract by running {$es}" +
            "hover:{/k pt accept %kingdoms_other_name%;&7Click to accept;|/k pt accept %kingdoms_other_name%}", 1, 2, 3),
    COMMAND_PEACETREATY_ACCEPT_NAME("accept", 1, 2, 3),
    COMMAND_PEACETREATY_REJECT_DESCRIPTION("{$s}Rejects a peace treaty contract.", 1, 2, 3),
    COMMAND_PEACETREATY_REJECT_USAGE("{$usage}pt reject {$p}<kingdom>", 1, 2, 3),
    COMMAND_PEACETREATY_REJECT_NAME("reject", 1, 2, 3),
    COMMAND_PEACETREATY_REJECT_NOTIFICATIONS_RECEIVER("{$e}Your kingdom has rejected {$es}%kingdoms_other_name% {$e}peace treaty.", 1, 2, 3, 4),
    COMMAND_PEACETREATY_REJECT_NOTIFICATIONS_SENDER("{$es}%kingdoms_name% {$e}has rejected your kingdom's peace treaty contract.", 1, 2, 3, 4),
    COMMAND_PEACETREATY_ACCEPT_DESCRIPTION("{$s}Accepts a peace treaty contract.", 1, 2, 3),
    COMMAND_PEACETREATY_ACCEPT_USAGE("{$usage}pt accept {$p}<kingdom>", 1, 2, 3),
    COMMAND_PEACETREATY_ACCEPT_FAILED("{$e}Your kingdom failed to meet all the contract's requirements to accept{$sep}:", 1, 2, 3),
    COMMAND_PEACETREATY_ACCEPT_ACCEPTED_RECEIVER("{$p}Your kingdom has accepted the peace treaty contract from {$s}%kingdoms_other_name% {$p}kingdom.", 1, 2, 3, 4),
    COMMAND_PEACETREATY_ACCEPT_ACCEPTED_SENDER("{$s}%kingdoms_name% {$p}has accepted your peace treaty contract.", 1, 2, 3, 4),
    COMMAND_PEACETREATY_RESUME_NONE("{$e}You don't have any active peace treaties to edit.", 1, 2, 3),
    EDITOR_PAUSED("{$p}You can resume your editing by running {$s}hover:{/k peacetreaty resume;&7Click to resume;/k peacetreaty resume}", 1),
    EDITOR_VICTIM_KINGDOM_DISBANDED("{$e}The kingdom you were proposing to {$sep}({$es}%kingdoms_kingdom_name%K{$sep}) {$e}was disbanded.", 1),
    TERM_INSUFFICIENT_WAR_POINTS("{$e}You need {$es}%total_required_war_points% {$e}total war points {$e}in order to use this term.", 1),
    TERMS_MIN("{$e}You need to specify at least {$es}%terms_min% {$e}terms.", 1),
    TERMS_TAKE_MONEY_NOT_ENOUGH("{$e}Your kingdom needs {$es}$%fancy_term_take_money_amount%", 1, 3),
    TERMS_TAKE_MONEY_ENTER("{$p}Enter the amount of money you wish to take.", 1, 3),

    WAR_POINTS_GAIN_INVADE("{$p}Your kingdom has gained {$s}%war_points% war points {$p}for the invasion.", 2, 3),
    WAR_POINTS_GAIN_KILL("{$p}Your kingdom has gained {$s}%war_points% war points {$p}for killing {$s}%other_player% {$p}by {$s}%player%", 2, 3),
    WAR_POINTS_GAIN_BREAK_TURRET("{$p}Your kingdom has gained {$s}%war_points% war points {$p}for breaking {$es}%kingdoms_kingdom_name%'s %style% {$e}turret.", 2, 3),
    WAR_POINTS_GAIN_BREAK_STRUCTURE("{$p}Your kingdom has gained {$s}%war_points% war points {$p}for breaking {$es}%kingdoms_kingdom_name%'s %style% {$e}structure.", 2, 3),
    WAR_POINTS_LOST_INVADE("{$e}Your kingdom has lost {$es}%war_points% war points {$e}for losing the invasion.", 2, 3),
    WAR_POINTS_LOST_KILL("{$e}Your kingdom has lost {$es}%war_points% war points {$p}for death of {$es}%other_player% {$e}by {$es}%player%", 2, 3),
    WAR_POINTS_LOST_BREAK_TURRET("{$e}Your kingdom has lost {$es}%war_points% war points {$e}because {$es}%kingdoms_kingdom_name% {$e}broke your {$es}%style% {$e}turret.", 2, 3),
    WAR_POINTS_LOST_BREAK_STRUCTURE("{$e}Your kingdom has lost {$es}%war_points% war points {$e}because {$es}%kingdoms_kingdom_name% {$e}broke your {$es}%style% {$e}structure.",
            2, 3),

    TERMS_TAKE_RESOURCE_POINTS_MESSAGE("{$e}Your kingdom must pay{$sep}: {$es}%fancy_term_take_resource_points_amount% resource points.", 1, 4),
    TERMS_TAKE_MONEY_MESSAGE("{$e}Your kingdom must pay{$sep}: {$es}$%fancy_term_take_money_amount%", 1, 3),
    TERMS_LEAVE_NATION_MESSAGE("{$e}Your kingdom must leave the nation and disband it if you're the capital.", 1, 3),
    TERMS_MISC_UPGRADES_MESSAGE("{$e}You will not be able to disable/enable misc upgrades and the proposer kingdom will have full control of them.", 1, 3),
    TERMS_KEEP_LANDS_MESSAGE("{$p}The following lands will be given back to your kingdom{$sep}:\n%term_keep_lands_returning_lands%", 1, 3),
    TERMS_SCUTAGE_MESSAGE("{$e}Your kingdom has to pay {$es}%term_scutage_percent%% {$e}from their bank as taxes.", 1, 2),
    TERMS_ANNUL_TREATIES_MESSAGE("{$e}All your relationships with other kingdoms will be synchronized with your overlord kingdom.", 1, 3),
    TERMS_LIMIT_STRUCTURES_MESSAGE("{$e}Your total number of placed structures will be limited to{$sep}: {$es}%term_limit_structures_amount%", 1, 3),
    TERMS_LIMIT_TURRETS_MESSAGE("{$e}Your total number of placed turrets will be limited to{$sep}: {$es}%term_limit_turrets_amount%", 1, 3),
    TERMS_LIMIT_CLAIMS_MESSAGE("{$e}Your total number of claims will be limited to{$sep}: {$es}%term_limit_claims_amount%", 1, 3),

    TERMS_TAKE_RESOURCE_POINTS_NOT_ENOUGH("{$e}Your kingdom needs {$es}%fancy_term_take_resource_points_amount% resource points.", 1, 4),
    TERMS_LEAVE_NATION_PERMANENT("{$e}The kingdom is the capital of a nation that is permanent. Can't disband a permanent nation.", 1, 3),
    TERMS_LEAVE_NATION_CANT_JOIN("{$e}Your kingdom is under a peace treaty contract that forbids you from joining nations.", 1, 3),
    TERMS_MAX_CLAIMS_LIMITED("{$e}Your kingdom is under a peace treaty contract that limits your max claims to {$es}%term_max_claims_amount%.", 1, 3),
    TERMS_MAX_TURRETS_LIMITED("{$e}Your kingdom is under a peace treaty contract that limits your max amount of turrets placed to {$es}%term_max_turrets_amount%.", 1, 3),
    TERMS_MAX_STRUCTURES_LIMITED("{$e}Your kingdom is under a peace treaty contract that limits your max amount of structures placed to {$es}%term_max_structures_amount%.", 1, 3),
    TERMS_MAX_STRUCTURES_RESTRICTED("{$e}Your kingdom is under a peace treaty contract that forbids you from changing your relationships with other kingdoms.", 1, 3),
    TERMS_MAX_STRUCTURES_SYNCHRONIZED("{$e}Your kingdom's relationship was updated due to a change in your overlord kingdom.", 1, 3),
    TERMS_MISC_UPGRADES_RESTRICTIED("{$e}Your kingdom is under a peace treaty contract that forbids you from toggling your misc upgrades.", 1, 3),

    @Path({"audit-logs", "peacetreaties", "sent"})
    LOGS_SENT("{$p}Your kingdom has sent one peace treaty to {$s}%kingdoms_name% {$p}Check your kingdom logs for more info."),
    @Path({"audit-logs", "peacetreaties", "received"})
    LOGS_RECEIVED("{$p}Your kingdom has receieved a peace treaty from {$s}%kingdoms_other_name% {$p}Check your kingdom logs for more info.");

    private final LanguageEntry languageEntry;
    private final String defaultValue;

    PeaceTreatyLang(String defaultValue, int... group) {
        this.defaultValue = defaultValue;
        this.languageEntry = DefinedMessenger.getEntry("peace-treaties", this, group);
    }

    @NonNull
    @Override
    public LanguageEntry getLanguageEntry() {
        return languageEntry;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }
}
