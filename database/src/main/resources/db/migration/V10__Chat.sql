CREATE TABLE IF NOT EXISTS bs.chat(
      id              UUID PRIMARY KEY NOT NULL,
      game_id         UUID NOT NULL,
      player_id       UUID NOT NULL,
      message         TEXT NOT NULL,
      created_at      TIMESTAMP NOT NULL,
      rec_version     bigint NOT NULL DEFAULT 1
);

CREATE INDEX IF NOT EXISTS game_id_idx ON bs.chat(game_id);

