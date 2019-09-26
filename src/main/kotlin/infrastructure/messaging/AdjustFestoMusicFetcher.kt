package infrastructure.messaging

import domain.core.*
import org.jsoup.Jsoup

internal object AdjustFestoMusicFetcher : MusicFetcher {
    private const val URL = "https://adjust.miz-miz.biz/festo/user.php?rid=average&d=0100000200000"

    override fun fetchAll(): List<Music> =
        Jsoup.connect(URL).get().selectFirst(".musicdata1").select("tr")
            .filterIndexed { i, _ -> i % 4 == 0 }
            .map { tr ->
                tr.select("td").let { tds ->
                    buildMusic(
                        tds[0].attr("id").toInt(),
                        tds[1].text(),
                        null, null, null
                    )
                }
            }
}
