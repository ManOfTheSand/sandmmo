package com.sandcore.mmo.manager;

import org.bukkit.entity.Player;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

public class PartyManager {

    private final Logger logger = Logger.getLogger(PartyManager.class.getName());
    // List to maintain all active parties.
    private final List<Party> parties = new ArrayList<>();

    /**
     * Creates a party with the specified leader.
     * @param leader the leader of the party.
     * @return the created Party, or null if the leader is already in a party.
     */
    public Party createParty(Player leader) {
        if (leader == null) {
            logger.warning("Cannot create party with null leader.");
            return null;
        }
        if (getParty(leader) != null) {
            logger.warning("Player " + leader.getName() + " is already in a party.");
            return null;
        }
        Party party = new Party(leader);
        parties.add(party);
        logger.info("Party created with leader: " + leader.getName());
        return party;
    }

    /**
     * Invites a player to an existing party.
     * @param party the party to which the player is invited.
     * @param invitee the player to invite.
     */
    public void invitePlayer(Party party, Player invitee) {
        if (party == null || invitee == null) {
            logger.warning("Cannot invite player: party or invitee is null.");
            return;
        }
        if (party.members.contains(invitee)) {
            logger.warning("Player " + invitee.getName() + " is already a member of the party.");
            return;
        }
        if (party.invited.contains(invitee)) {
            logger.warning("Player " + invitee.getName() + " has already been invited.");
            return;
        }
        party.invited.add(invitee);
        logger.info("Player " + invitee.getName() + " invited to party led by " + party.leader.getName());
    }

    /**
     * Removes a player from their party. If the player is the leader, the party is disbanded.
     * @param player the player leaving the party.
     */
    public void leaveParty(Player player) {
        if (player == null) {
            logger.warning("Null player cannot leave a party.");
            return;
        }
        Party party = getParty(player);
        if (party == null) {
            logger.warning("Player " + player.getName() + " is not in any party.");
            return;
        }
        if (party.leader.equals(player)) {
            parties.remove(party);
            logger.info("Party led by " + player.getName() + " has been disbanded because the leader left.");
        } else {
            party.members.remove(player);
            logger.info("Player " + player.getName() + " left the party led by " + party.leader.getName());
        }
    }

    /**
     * Helper method to find the party a player belongs to.
     * @param player the player whose party is searched.
     * @return the Party if found, or null otherwise.
     */
    public Party getParty(Player player) {
        for (Party party : parties) {
            if (party.leader.equals(player) || party.members.contains(player)) {
                return party;
            }
        }
        return null;
    }

    /**
     * Inner class representing a party.
     */
    public static class Party {
        public final Player leader;
        public final List<Player> members;
        public final List<Player> invited;

        public Party(Player leader) {
            this.leader = leader;
            this.members = new ArrayList<>();
            this.invited = new ArrayList<>();
            // Automatically add the leader to the members list.
            this.members.add(leader);
        }
    }
} 