package net.terminal_end.tasrankingviewer.model.SearchQuery

import com.google.gson.Gson
import net.terminal_end.tasrankingviewer.model.SearchError
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * Created by S-Shimotori on 6/3/16.
 */

class SearchErrorTest {
    class Success {
        @Test
        fun success() {
            val jsonString = """
            |{
            |   "dqnid": "963e9bdb-4759-4e43-97fc-315b09a980f9",
            |   "dqsid": "",
            |   "errid": "101",
            |   "desc": "Caught exception while processing request.",
            |   "level": "DQNERR_FATAL_STOP_STREAM",
            |   "opid": "noopid"
            |}
            """.trimMargin()

            val searchError = Gson().fromJson(jsonString, SearchError::class.java)
            assertThat(searchError.dqnid, `is`("963e9bdb-4759-4e43-97fc-315b09a980f9"))
            assertThat(searchError.dqsid, `is`(""))
            assertThat(searchError.errid, `is`("101"))
            assertThat(searchError.desc, `is`("Caught exception while processing request."))
            assertThat(searchError.level, `is`("DQNERR_FATAL_STOP_STREAM"))
            assertThat(searchError.opid, `is`("noopid"))
        }
    }
}