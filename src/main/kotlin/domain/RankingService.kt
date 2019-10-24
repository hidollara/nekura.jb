package domain

internal interface RankingService {
    fun findEarliestUpdated(): Ranking
}
