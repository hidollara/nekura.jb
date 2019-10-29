package infrastructure.persistence.mysql

import domain.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

internal class MySqlRecordService(private val db: Database) : RecordService {
    override fun search(
        musicTitles: List<String>,
        diffs: List<Difficulty>,
        modes: List<Mode>,
        playerNames: List<String>,
        rivalIds: List<RivalId>
    ) = transaction(db) {
        (Schema.Records innerJoin Schema.Musics innerJoin Schema.Players)
            .selectAll()
            .apply {
                if (musicTitles.isNotEmpty()) andWhere {
                    musicTitles.map { Op.build { Schema.Musics.title like "%${it}%" } }.reduce { op1, op2 -> op1 or op2 }
                }
                if (diffs.isNotEmpty()) andWhere {
                    diffs.map { Op.build { Schema.Records.diff eq it } }.reduce { op1, op2 -> op1 or op2 }
                }
                if (modes.isNotEmpty()) andWhere {
                    modes.map { Op.build { Schema.Records.mode eq it } }.reduce { op1, op2 -> op1 or op2 }
                }
                if (playerNames.isNotEmpty()) andWhere {
                    playerNames.map { Op.build { Schema.Players.name like "%${it}%" } }.reduce { op1, op2 -> op1 or op2 }
                }
                if (rivalIds.isNotEmpty()) andWhere {
                    rivalIds.map { Op.build { Schema.Players.rivalId eq it } }.reduce { op1, op2 -> op1 or op2 }
                }
            }
            .orderBy(Schema.Records.recordedAt to false)
            .limit(100)
            .map { record ->
                Record(
                    RankingId(
                        Music(
                            record[Schema.Musics.mid],
                            record[Schema.Musics.title]
                        ),
                        record[Schema.Records.diff],
                        record[Schema.Records.mode]
                    ),
                    Player(
                        record[Schema.Players.rivalId],
                        record[Schema.Players.name]
                    ),
                    record[Schema.Records.score],
                    record[Schema.Records.recordedAt]
                )
            }
    }
}
