package infrastructure.messaging

import domain.core.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.jsoup.Jsoup

internal object OfficialPageRankingFetcher : RankingFetcher {
    override fun fetch(rankingId: RankingId) =
        Jsoup.connect(rankingId.sourceUrl).get().select(".page_navi .num").size.let { numberOfPages ->
            (1..numberOfPages)
                .map { page ->
                    Jsoup.connect(rankingId.sourceUrlWith(page)).get()
                        .selectFirst("table.rank_player").select("tr").apply { removeAt(0) }
                }
                .flatten()
                .let {
                    Ranking(
                        rankingId,
                        DateTime.now(),
                        it.map {
                            it.select("td").let { tds ->
                                Record(
                                    rankingId,
                                    Player(
                                        RivalId(tds[1].child(0).attr("href").let { href ->
                                            """rival_id=(\d+)""".toRegex().find(href)!!.groupValues[1].toLong()
                                        }),
                                        tds[1].child(0).text()
                                    ),
                                    tds[2].text().toInt(),
                                    tds[3].text().let { text ->
                                        DateTime.parse(text, DateTimeFormat.forPattern("yyyy/M/d HH:mm"))
                                    }
                                )
                            }
                        }
                    )
                }
        }
}
