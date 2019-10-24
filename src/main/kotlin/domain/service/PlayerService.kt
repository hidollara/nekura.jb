package domain.service

import domain.core.*

internal interface PlayerService {
    fun getRecords(player: Player): List<Record>
}
