CREATE INDEX IF NOT EXISTS game_id_idx ON bs.player_game(game_id);
CREATE INDEX IF NOT EXISTS player_id_idx ON bs.player_game(player_id);