package infrastructure.messaging

import domain.core.*
import org.jsoup.Jsoup

internal object OfficialPageMusicFetcher : MusicFetcher {
    private const val URL = "https://p.eagate.573.jp/game/jubeat/festo/ranking/ranking3.html"

    override fun fetchAll(): List<Music> =
        Jsoup.connect(URL).get().select(".page_navi .num").size.let { numberOfPages ->
            (1..numberOfPages)
                .map { page ->
                    Jsoup.connect("$URL?page=$page").get().selectFirst("table.music_data").select("tr")
                }
                .flatten()
                .map { tr ->
                    tr.select("td").let { tds ->
                        buildMusic(
                            tds[0].selectFirst("img").attr("src").let { src ->
                                """id(\d+).gif""".toRegex().find(src)!!.groupValues[1].toInt()
                            },
                            tds[1].text(),
                            null, null, null
                        )
                    }
                }
        }
}
