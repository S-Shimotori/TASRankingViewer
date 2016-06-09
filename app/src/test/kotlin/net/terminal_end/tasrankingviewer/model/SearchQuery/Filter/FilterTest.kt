package net.terminal_end.tasrankingviewer.model.SearchQuery.Filter

import com.google.gson.GsonBuilder
import net.terminal_end.tasrankingviewer.model.SearchQuery
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * Created by S-Shimotori on 6/2/16.
 */

class FilterTest {
    class Failure {
        @Test
        fun failWithoutType() {
            val jsonString = """
            |{
            |   "field": "view_counter",
            |   "value": 1000
            |}
            """.trimMargin()

            val filter = GsonBuilder()
                    .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.Deserializer())
                    .create()
                    .fromJson(jsonString, SearchQuery.Filter::class.java)
            assertThat(filter, nullValue())
        }

        @Test
        fun failWithInvalidType() {
            val jsonString = """
            |{
            |   "type": "hoge",
            |   "field": "view_counter",
            |   "value": 1000
            |}
            """.trimMargin()

            val filter = GsonBuilder()
                    .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.Deserializer())
                    .create()
                    .fromJson(jsonString, SearchQuery.Filter::class.java)
            assertThat(filter, nullValue())
        }

        @Test
        fun failWithoutField() {
            val jsonString = """
            |{
            |   "type": "equal",
            |   "value": 1000
            |}
            """.trimMargin()

            val filter = GsonBuilder()
                    .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.Deserializer())
                    .create()
                    .fromJson(jsonString, SearchQuery.Filter::class.java)
            assertThat(filter, nullValue())
        }

        @Test
        fun failWithInvalidField() {
            val jsonString = """
            |{
            |   "type": "equal",
            |   "field": "hoge",
            |   "value": 1000
            |}
            """.trimMargin()

            val filter = GsonBuilder()
                    .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.Deserializer())
                    .create()
                    .fromJson(jsonString, SearchQuery.Filter::class.java)
            assertThat(filter, nullValue())
        }
    }
}