/*
 * This file is generated by jOOQ.
 */
package com.rmulka.bs.jooq.generated;


import com.rmulka.bs.jooq.generated.tables.Players;
import com.rmulka.bs.jooq.generated.tables.records.PlayersRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>bs</code> schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<PlayersRecord> PLAYERS_PKEY = UniqueKeys0.PLAYERS_PKEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class UniqueKeys0 {
        public static final UniqueKey<PlayersRecord> PLAYERS_PKEY = Internal.createUniqueKey(Players.PLAYERS, "players_pkey", new TableField[] { Players.PLAYERS.ID }, true);
    }
}
