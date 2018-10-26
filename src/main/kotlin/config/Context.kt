package config

import infrastructure.messaging.*
import infrastructure.persistence.*
import application.*
import infrastructure.persistence.mysql.*
import org.jetbrains.exposed.sql.Database

internal object Context {
    private val db = Database.connect("jdbc:mysql://localhost/nekura_jb", "com.mysql.jdbc.Driver", "root")

    private val musicCommand = MySqlMusicCommand(db)
    private val musicQuery = MySqlMusicQuery(db)
    private val rankingCommand = MySqlRankingCommand(db)
    private val rankingQuery = MySqlRankingQuery(db)
    private val recordQuery = MySqlRecordQuery(db)
    private val musicFetcher = OfficialPageMusicFetcher
    private val recordFetcher = OfficialPageRankingFetcher
    private val rankingUpdateManager = RankingUpdateManager(rankingCommand, rankingQuery, recordFetcher, 30)

    val rankingAutoUpdater = RankingAutoUpdater(rankingCommand, recordFetcher, rankingUpdateManager)
    val rankerService = RankerService(recordQuery)
    val latestUpdateService = LatestUpdateService(recordQuery)
    val musicService = MusicService(musicQuery, rankingQuery, rankingUpdateManager)

    internal fun initialize() {
        Schema.drop(db)
        Schema.create(db)

        musicCommand.pull(musicFetcher)
    }
}