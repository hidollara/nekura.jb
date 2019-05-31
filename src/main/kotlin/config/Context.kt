package config

import infrastructure.messaging.*
import infrastructure.persistence.*
import application.*
import application.RankingService
import domain.RankingUpdateManager
import infrastructure.persistence.mysql.*
import org.jetbrains.exposed.sql.Database

internal object Context {
    private val db = Database.connect("jdbc:mysql://localhost/nekura_jb", "com.mysql.cj.jdbc.Driver", "root")

    private val musicFetcher = OfficialPageMusicFetcher
    private val rankingFetcher = OfficialPageRankingFetcher

    private val musicCommand = MySqlMusicCommand(db)
    private val musicQuery = MySqlMusicQuery(db)
    private val rankingCommand = MySqlRankingCommand(db)
    private val rankerQuery = MySqlRankerQuery(db)
    private val recordHeaderQuery = MySqlRecordHeaderQuery(db)
    private val recordQuery = MySqlRecordQuery(db, recordHeaderQuery)

    private val rankingUpdateManager = RankingUpdateManager(rankingCommand, recordHeaderQuery, rankingFetcher, 30)

    val musicAutoUpdateService =
        MusicAutoUpdateService(musicCommand, musicFetcher, 86400000)
    val rankingAutoUpdateService =
        RankingAutoUpdateService(rankingCommand, rankingFetcher, rankingUpdateManager, 60000)

    val musicService = MusicService(musicQuery)
    val rankingService = RankingService(recordHeaderQuery, recordQuery, rankingUpdateManager)
    val rankerService = RankerService(rankerQuery, recordQuery)
    val recordService = RecordService(recordQuery)

    internal fun dropDatabase() {
        Schema.drop(db)
    }

    internal fun createDatabase() {
        Schema.create(db)
        musicCommand.pull(musicFetcher)
    }
}
