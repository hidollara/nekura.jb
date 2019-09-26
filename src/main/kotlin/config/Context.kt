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
    private val musicCommander = MySqlMusicCommander(db)
    private val musicQuerent = MySqlMusicQuerent(db)

    private val rankingFetcher = OfficialPageRankingFetcher
    private val rankingCommander = MySqlRankingCommander(db)
    private val rankingQuerent = MySqlRankingQuerent(db)

    private val rankerQuerent = MySqlRankerQuerent(db)

    private val rankingUpdateManager =
        RankingUpdateManager(rankingFetcher, rankingCommander, 30)

    val musicAutoUpdateService =
        MusicAutoUpdateService(musicFetcher, musicCommander, 24 * 60 * 60 * 1000)
    val rankingAutoUpdateService =
        RankingAutoUpdateService(rankingQuerent, rankingUpdateManager, 60 * 1000)

    val musicService = MusicService(musicQuerent)
    val rankerService = RankerService(rankerQuerent)

    internal fun dropDatabase() {
        Schema.drop(db)
    }

    internal fun createDatabase() {
        Schema.create(db)
        musicFetcher.fetchAll().let { musicCommander.save(it) }
    }
}
