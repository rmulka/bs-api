CREATE SCHEMA IF NOT EXISTS bs;
CREATE TABLE IF NOT EXISTS bs.players
(
id              UUID PRIMARY KEY,
player_name     TEXT NOT NULL
);
