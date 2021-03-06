/*
 * This file is generated by jOOQ.
 */
package com.rmulka.bs.jooq.generated.tables.pojos;


import java.io.Serializable;
import java.util.UUID;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PlayerGame implements Serializable {

    private static final long serialVersionUID = -1062425951;

    private UUID    id;
    private UUID    playerId;
    private UUID    gameId;
    private Boolean isCreator;
    private Long    recVersion;

    public PlayerGame() {}

    public PlayerGame(PlayerGame value) {
        this.id = value.id;
        this.playerId = value.playerId;
        this.gameId = value.gameId;
        this.isCreator = value.isCreator;
        this.recVersion = value.recVersion;
    }

    public PlayerGame(
        UUID    id,
        UUID    playerId,
        UUID    gameId,
        Boolean isCreator,
        Long    recVersion
    ) {
        this.id = id;
        this.playerId = playerId;
        this.gameId = gameId;
        this.isCreator = isCreator;
        this.recVersion = recVersion;
    }

    public UUID getId() {
        return this.id;
    }

    public PlayerGame setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public PlayerGame setPlayerId(UUID playerId) {
        this.playerId = playerId;
        return this;
    }

    public UUID getGameId() {
        return this.gameId;
    }

    public PlayerGame setGameId(UUID gameId) {
        this.gameId = gameId;
        return this;
    }

    public Boolean getIsCreator() {
        return this.isCreator;
    }

    public PlayerGame setIsCreator(Boolean isCreator) {
        this.isCreator = isCreator;
        return this;
    }

    public Long getRecVersion() {
        return this.recVersion;
    }

    public PlayerGame setRecVersion(Long recVersion) {
        this.recVersion = recVersion;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final PlayerGame other = (PlayerGame) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (playerId == null) {
            if (other.playerId != null)
                return false;
        }
        else if (!playerId.equals(other.playerId))
            return false;
        if (gameId == null) {
            if (other.gameId != null)
                return false;
        }
        else if (!gameId.equals(other.gameId))
            return false;
        if (isCreator == null) {
            if (other.isCreator != null)
                return false;
        }
        else if (!isCreator.equals(other.isCreator))
            return false;
        if (recVersion == null) {
            if (other.recVersion != null)
                return false;
        }
        else if (!recVersion.equals(other.recVersion))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.playerId == null) ? 0 : this.playerId.hashCode());
        result = prime * result + ((this.gameId == null) ? 0 : this.gameId.hashCode());
        result = prime * result + ((this.isCreator == null) ? 0 : this.isCreator.hashCode());
        result = prime * result + ((this.recVersion == null) ? 0 : this.recVersion.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PlayerGame (");

        sb.append(id);
        sb.append(", ").append(playerId);
        sb.append(", ").append(gameId);
        sb.append(", ").append(isCreator);
        sb.append(", ").append(recVersion);

        sb.append(")");
        return sb.toString();
    }
}
