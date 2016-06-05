package net.terminal_end.tasrankingviewer.model

/**
 * Created by S-Shimotori on 6/3/16.
 */

class SearchError(dqnId: String, errorId: ErrorId) {
    val dqnid = dqnId
    val dqsid: String? = null
    val errid = errorId.id
    val desc = "Caught exception while processing request."
    val level: String? = null
    val opid: String? = null

    enum class ErrorId(val id: String) {
        MAINTENANCE("101"),
        INVALID_QUERY("300"),
        INVALID_FIELD("701"),
        TIMEOUT("1001")
    }

    class Exception(val errid: String, detailMessage: String): java.lang.Exception(detailMessage) {
    }
}