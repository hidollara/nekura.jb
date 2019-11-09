package config

import domain.*
import application.*
import infrastructure.messaging.*
import infrastructure.persistence.*
import infrastructure.persistence.mysql.*
import org.jetbrains.exposed.sql.Database

internal object Context {
    private val db = Database.connect("jdbc:mysql://localhost/nekura_jb", "com.mysql.cj.jdbc.Driver", "root")

    private val musicFetcher = OfficialPageMusicFetcher
    private val musicRepository = MySqlMusicRepository(db)

    private val rankingFetcher = OfficialPageRankingFetcher
    private val rankingRepository = MySqlRankingRepository(db)
    private val rankingService = MySqlRankingService(db)

    private val recordService = MySqlRecordService(db)

    val rankingAutoUpdateService =
        RankingAutoUpdateService(rankingFetcher, rankingRepository, rankingService, 30, 10 * 1000)

    val recordApplicationService = RecordApplicationService(recordService)

    internal fun updateMusics() {
        musicFetcher.fetchAll().let { musicRepository.save(it) }
    }

    internal fun dropDatabase() {
        Schema.drop(db)
    }

    internal fun createDatabase() {
        Schema.create(db)
        updateMusics()
    }

}
