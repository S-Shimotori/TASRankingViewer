package net.terminal_end.tasrankingviewer.model.SearchQuery

import net.terminal_end.tasrankingviewer.model.SearchQuery
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * Created by S-Shimotori on 6/2/16.
 */

class SearchQueryTest {
    @Test
    fun issuerLength() {
        assertThat(SearchQuery.issuer.length, `is`(lessThanOrEqualTo(40)))
    }

    class Success {
        @Test
        fun success() {
            val jsonString = """
            """.trimMargin()
        }
    }
}