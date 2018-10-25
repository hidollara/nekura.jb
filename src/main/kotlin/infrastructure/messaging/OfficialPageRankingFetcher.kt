package infrastructure.messaging

import domain.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.jsoup.Jsoup

internal object OfficialPageRankingFetcher : RankingFetcher {
    override fun fetch(chart: Chart) =
        Jsoup.connect(chart.rankingPage).get().select(".page_navi .num").size.let { numberOfPages ->
            (1..numberOfPages)
                .map { page ->
                    Jsoup.connect(chart.rankingPageWithPage(page)).get()
                        .selectFirst("table.rank_player").select("tr").apply { removeAt(0) }
                }
                .flatten()
                .map { tr ->
                    tr.select("td").let { tds ->
                        Record(
                            chart,
                            tds[1].child(0).let { a ->
                                Player(
                                    a.attr("href").let { href ->
                                        """rival_id=(\d+)""".toRegex().find(href)!!.groupValues[1].toLong()
                                    },
                                    a.text()
                                )
                            },
                            tds[2].text().toInt(),
                            tds[3].text().let { text ->
                                DateTime.parse(text, DateTimeFormat.forPattern("yyyy/M/d HH:mm"))
                            }
                        )
                    }
                }
        }
}
