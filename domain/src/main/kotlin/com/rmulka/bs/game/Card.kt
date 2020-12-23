package com.rmulka.bs.game

/**
 * Ranks: 1 - Spades, 2 - Clubs, 3 - Hearts, 4 - Diamonds
 * Suits: 1 - A, 2 -> 10 (face value), 11 - J, 12 - Q, 13 - K
 */
data class Card(
        val rank: Int,
        val suit: Int
)