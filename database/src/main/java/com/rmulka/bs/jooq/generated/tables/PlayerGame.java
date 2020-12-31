/*
 * This file is generated by jOOQ.
 */
package com.rmulka.bs.jooq.generated.tables;


import com.rmulka.bs.jooq.generated.Bs;
import com.rmulka.bs.jooq.generated.Indexes;
import com.rmulka.bs.jooq.generated.Keys;
import com.rmulka.bs.jooq.generated.tables.records.PlayerGameRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PlayerGame extends TableImpl<PlayerGameRecord> {

    private static final long serialVersionUID = 272132931;

    /**
     * The reference instance of <code>bs.player_game</code>
     */
    public static final PlayerGame PLAYER_GAME = new PlayerGame();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PlayerGameRecord> getRecordType() {
        return PlayerGameRecord.class;
    }

    /**
     * The column <code>bs.player_game.id</code>.
     */
    public final TableField<PlayerGameRecord, UUID> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.UUID.nullable(false).defaultValue(org.jooq.impl.DSL.field("uuid_generate_v4()", org.jooq.impl.SQLDataType.UUID)), this, "");

    /**
     * The column <code>bs.player_game.player_id</code>.
     */
    public final TableField<PlayerGameRecord, UUID> PLAYER_ID = createField(DSL.name("player_id"), org.jooq.impl.SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>bs.player_game.game_id</code>.
     */
    public final TableField<PlayerGameRecord, UUID> GAME_ID = createField(DSL.name("game_id"), org.jooq.impl.SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>bs.player_game.is_creator</code>.
     */
    public final TableField<PlayerGameRecord, Boolean> IS_CREATOR = createField(DSL.name("is_creator"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>bs.player_game.rec_version</code>.
     */
    public final TableField<PlayerGameRecord, Long> REC_VERSION = createField(DSL.name("rec_version"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("1", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * Create a <code>bs.player_game</code> table reference
     */
    public PlayerGame() {
        this(DSL.name("player_game"), null);
    }

    /**
     * Create an aliased <code>bs.player_game</code> table reference
     */
    public PlayerGame(String alias) {
        this(DSL.name(alias), PLAYER_GAME);
    }

    /**
     * Create an aliased <code>bs.player_game</code> table reference
     */
    public PlayerGame(Name alias) {
        this(alias, PLAYER_GAME);
    }

    private PlayerGame(Name alias, Table<PlayerGameRecord> aliased) {
        this(alias, aliased, null);
    }

    private PlayerGame(Name alias, Table<PlayerGameRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> PlayerGame(Table<O> child, ForeignKey<O, PlayerGameRecord> key) {
        super(child, key, PLAYER_GAME);
    }

    @Override
    public Schema getSchema() {
        return Bs.BS;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.GAME_ID_IDX, Indexes.PLAYER_ID_IDX);
    }

    @Override
    public UniqueKey<PlayerGameRecord> getPrimaryKey() {
        return Keys.PLAYER_GAME_PKEY;
    }

    @Override
    public List<UniqueKey<PlayerGameRecord>> getKeys() {
        return Arrays.<UniqueKey<PlayerGameRecord>>asList(Keys.PLAYER_GAME_PKEY);
    }

    @Override
    public List<ForeignKey<PlayerGameRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<PlayerGameRecord, ?>>asList(Keys.PLAYER_GAME__PLAYER_GAME_PLAYER_ID_FKEY, Keys.PLAYER_GAME__PLAYER_GAME_GAME_ID_FKEY);
    }

    public Player player() {
        return new Player(this, Keys.PLAYER_GAME__PLAYER_GAME_PLAYER_ID_FKEY);
    }

    public Game game() {
        return new Game(this, Keys.PLAYER_GAME__PLAYER_GAME_GAME_ID_FKEY);
    }

    @Override
    public TableField<PlayerGameRecord, Long> getRecordVersion() {
        return REC_VERSION;
    }

    @Override
    public PlayerGame as(String alias) {
        return new PlayerGame(DSL.name(alias), this);
    }

    @Override
    public PlayerGame as(Name alias) {
        return new PlayerGame(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PlayerGame rename(String name) {
        return new PlayerGame(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PlayerGame rename(Name name) {
        return new PlayerGame(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<UUID, UUID, UUID, Boolean, Long> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
