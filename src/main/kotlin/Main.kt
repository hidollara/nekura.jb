import config.Context
import interfaces.api.ApiServer

fun main(args: Array<String>) {
    Context.rankingAutoUpdater.start()
    ApiServer.start()
}
