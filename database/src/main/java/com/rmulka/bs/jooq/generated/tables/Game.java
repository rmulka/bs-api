/*
 * This file is generated by jOOQ.
 */
package com.rmulka.bs.jooq.generated.tables;


import com.rmulka.bs.jooq.generated.Bs;
import com.rmulka.bs.jooq.generated.Keys;
import com.rmulka.bs.jooq.generated.tables.records.GameRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
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
public class Game extends TableImpl<GameRecord> {

    private static final long serialVersionUID = 930263063;

    /**
     * The reference instance of <code>bs.game</code>
     */
    public static final Game GAME = new Game();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<GameRecord> getRecordType() {
        return GameRecord.class;
    }

    /**
     * The column <code>bs.game.id</code>.
     */
    public final TableField<GameRecord, UUID> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>bs.game.in_progress</code>.
     */
    public final TableField<GameRecord, Boolean> IN_PROGRESS = createField(DSL.name("in_progress"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>bs.game.details</code>.
     */
    public final TableField<GameRecord, JSONB> DETAILS = createField(DSL.name("details"), org.jooq.impl.SQLDataType.JSONB, this, "");

    /**
     * The column <code>bs.game.num_players</code>.
     */
    public final TableField<GameRecord, Integer> NUM_PLAYERS = createField(DSL.name("num_players"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * Create a <code>bs.game</code> table reference
     */
    public Game() {
        this(DSL.name("game"), null);
    }

    /**
     * Create an aliased <code>bs.game</code> table reference
     */
    public Game(String alias) {
        this(DSL.name(alias), GAME);
    }

    /**
     * Create an aliased <code>bs.game</code> table reference
     */
    public Game(Name alias) {
        this(alias, GAME);
    }

    private Game(Name alias, Table<GameRecord> aliased) {
        this(alias, aliased, null);
    }

    private Game(Name alias, Table<GameRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> Game(Table<O> child, ForeignKey<O, GameRecord> key) {
        super(child, key, GAME);
    }

    @Override
    public Schema getSchema() {
        return Bs.BS;
    }

    @Override
    public UniqueKey<GameRecord> getPrimaryKey() {
        return Keys.GAME_PKEY;
    }

    @Override
    public List<UniqueKey<GameRecord>> getKeys() {
        return Arrays.<UniqueKey<GameRecord>>asList(Keys.GAME_PKEY);
    }

    @Override
    public Game as(String alias) {
        return new Game(DSL.name(alias), this);
    }

    @Override
    public Game as(Name alias) {
        return new Game(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Game rename(String name) {
        return new Game(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Game rename(Name name) {
        return new Game(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<UUID, Boolean, JSONB, Integer> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
