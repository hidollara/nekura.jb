package config

import infrastructure.messaging.*
import infrastructure.persistence.*
import application.*
import domain.service.RankingUpdateManager
import infrastructure.persistence.mysql.*
import org.jetbrains.exposed.sql.Database

internal object Context {
    private val db = Database.connect("jdbc:mysql://localhost/nekura_jb", "com.mysql.cj.jdbc.Driver", "root")

    private val musicFetcher = OfficialPageMusicFetcher
    private val musicRepository = MySqlMusicRepository(db)
    private val musicQuerent = MySqlMusicQuerent(db)

    private val playerQuerent = MySqlPlayerQuerent(db)
    private val playerService = MySqlPlayerService(db)

    private val rankingFetcher = OfficialPageRankingFetcher
    private val rankingRepository = MySqlRankingRepository(db)
    private val rankingQuerent = MySqlRankingQuerent(db)


    private val rankingUpdateManager =
        RankingUpdateManager(rankingFetcher, rankingRepository, 30)
    val rankingAutoUpdateService =
        RankingAutoUpdateService(rankingQuerent, rankingUpdateManager, 60 * 1000)

    val musicService = MusicService(musicQuerent)
    val rankerService = RankerService(playerQuerent, playerService)

    internal fun dropDatabase() {
        Schema.drop(db)
    }

    internal fun createDatabase() {
        Schema.create(db)
        musicFetcher.fetchAll().let { musicRepository.save(it) }
    }
}
