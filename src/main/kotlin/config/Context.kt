package config

import infrastructure.messaging.*
import infrastructure.persistence.*
import application.*
import application.RankingService
import domain.RankingUpdateManager
import infrastructure.persistence.mysql.*
import org.jetbrains.exposed.sql.Database

internal object Context {
    private val db = Database.connect("jdbc:mysql://localhost/nekura_jb", "com.mysql.jdbc.Driver", "root")

    private val musicFetcher = OfficialPageMusicFetcher
    private val musicCommand = MySqlMusicCommand(db)
    private val musicQuery = MySqlMusicQuery(db)
    private val rankingFetcher = OfficialPageRankingFetcher
    private val rankingCommand = MySqlRankingCommand(db)
    private val recordHeaderQuery = MySqlRecordHeaderQuery(db)
    private val recordQuery = MySqlRecordQuery(db)
    private val rankingUpdateManager = RankingUpdateManager(rankingCommand, recordHeaderQuery, rankingFetcher, 30)

    val rankingAutoUpdateService = RankingAutoUpdateService(rankingCommand, rankingFetcher, rankingUpdateManager)
    val musicService = MusicService(musicQuery)
    val rankingService = RankingService(recordHeaderQuery, recordQuery, rankingUpdateManager)
    val rankerService = RankerService(recordQuery)
    val recordService = RecordService(recordQuery)

    internal fun initialize() {
        Schema.drop(db)
        Schema.create(db)

        musicCommand.pull(musicFetcher)
    }
}