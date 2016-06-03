package net.terminal_end.tasrankingviewer.model.SearchQuery.SearchResponse

import net.terminal_end.tasrankingviewer.model.Chunk
import net.terminal_end.tasrankingviewer.model.SearchResponse
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.samePropertyValuesAs
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * Created by S-Shimotori on 6/3/16.
 */

class SearchResponseTest {
    class Success {
        @Test
        fun success() {
            val responseString = """
            |{"dqnid":"c0676eea-cc77-4317-b442-d626c5f34558","type":"hits","values":[{"_rowid":0,"cmsid":"sm13208019","title":"【DIVA 2nd】　鏡音八八花合戦　【EDIT PV】","view_counter":9999}]}
            |{"dqnid":"c0676eea-cc77-4317-b442-d626c5f34558","type":"stats","values":[{"_rowid":0,"service":"video","total":213353}]}
            |{"dqnid":"c0676eea-cc77-4317-b442-d626c5f34558","endofstream":true,"type":"hits"}
            |{"dqnid":"c0676eea-cc77-4317-b442-d626c5f34558","endofstream":true,"type":"stats"}
            """.trimMargin()

            val searchResponse = SearchResponse.getInstance(responseString)
            assertThat(searchResponse.stats[0].values!![0].service, `is`(SearchResponse.Service.video))
            assertThat(searchResponse.stats[0].values!![0].total, `is`(213353))
            assertThat(searchResponse.hits[0].values!!.asIterable(), `is`(contains(samePropertyValuesAs(Chunk.Value.Hits(0, "sm13208019", "【DIVA 2nd】　鏡音八八花合戦　【EDIT PV】", null, null, null, null, 9999, null, null, null, null)))))
        }
    }
}