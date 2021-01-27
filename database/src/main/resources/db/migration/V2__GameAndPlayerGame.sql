CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS bs.game
(
    id UUID PRIMARY KEY,
    in_progress BOOLEAN NOT NULL,
    details jsonb
);

CREATE TABLE IF NOT EXISTS bs.player_game
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    player_id UUID  NOT NULL REFERENCES bs.player,
    game_id UUID NOT NULL REFERENCES bs.game,
    is_creator BOOLEAN NOT NULL
);