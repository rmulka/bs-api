package com.rmulka.bs.game

class Deck {
    private val deck: Array<Card>
    private var currentCardIdx: Int

    init {
        deck = (1..4).flatMap { suit -> (1..13).map { rank -> Card(rank=rank, suit=suit) } }.toTypedArray()
        currentCardIdx = 0
    }

    fun shuffle() {
        deck.shuffle()
    }

    fun dealCard(): Card = deck[currentCardIdx++]

    fun isEmpty(): Boolean = currentCardIdx >= 52

    fun reset() {
        currentCardIdx = 0
        this.shuffle()
    }
}