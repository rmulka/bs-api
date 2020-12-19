/*
 * This file is generated by jOOQ.
 */
package com.rmulka.bs.jooq.generated.tables;


import com.rmulka.bs.jooq.generated.Bs;
import com.rmulka.bs.jooq.generated.Keys;
import com.rmulka.bs.jooq.generated.tables.records.PlayerRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
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
public class Player extends TableImpl<PlayerRecord> {

    private static final long serialVersionUID = -2067620351;

    /**
     * The reference instance of <code>bs.player</code>
     */
    public static final Player PLAYER = new Player();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PlayerRecord> getRecordType() {
        return PlayerRecord.class;
    }

    /**
     * The column <code>bs.player.id</code>.
     */
    public final TableField<PlayerRecord, UUID> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>bs.player.player_name</code>.
     */
    public final TableField<PlayerRecord, String> PLAYER_NAME = createField(DSL.name("player_name"), org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * Create a <code>bs.player</code> table reference
     */
    public Player() {
        this(DSL.name("player"), null);
    }

    /**
     * Create an aliased <code>bs.player</code> table reference
     */
    public Player(String alias) {
        this(DSL.name(alias), PLAYER);
    }

    /**
     * Create an aliased <code>bs.player</code> table reference
     */
    public Player(Name alias) {
        this(alias, PLAYER);
    }

    private Player(Name alias, Table<PlayerRecord> aliased) {
        this(alias, aliased, null);
    }

    private Player(Name alias, Table<PlayerRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> Player(Table<O> child, ForeignKey<O, PlayerRecord> key) {
        super(child, key, PLAYER);
    }

    @Override
    public Schema getSchema() {
        return Bs.BS;
    }

    @Override
    public UniqueKey<PlayerRecord> getPrimaryKey() {
        return Keys.PLAYER_PKEY;
    }

    @Override
    public List<UniqueKey<PlayerRecord>> getKeys() {
        return Arrays.<UniqueKey<PlayerRecord>>asList(Keys.PLAYER_PKEY);
    }

    @Override
    public Player as(String alias) {
        return new Player(DSL.name(alias), this);
    }

    @Override
    public Player as(Name alias) {
        return new Player(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Player rename(String name) {
        return new Player(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Player rename(Name name) {
        return new Player(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<UUID, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}