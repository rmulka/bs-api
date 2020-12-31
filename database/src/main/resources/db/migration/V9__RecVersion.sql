ALTER TABLE bs.game ADD COLUMN rec_version bigint NOT NULL DEFAULT 1;
ALTER TABLE bs.player ADD COLUMN rec_version bigint NOT NULL DEFAULT 1;
ALTER TABLE bs.player_game ADD COLUMN rec_version bigint NOT NULL DEFAULT 1;