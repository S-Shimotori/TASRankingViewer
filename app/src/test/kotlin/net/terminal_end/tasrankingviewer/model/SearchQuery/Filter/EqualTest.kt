package net.terminal_end.tasrankingviewer.model.SearchQuery.Filter

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.terminal_end.tasrankingviewer.model.SearchQuery
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * Created by S-Shimotori on 6/2/16.
 */

class EqualTest {
    class Failure {
        @Test
        fun failWithoutValue() {
            val jsonString = """
            |{
            |   "type": "equal",
            |   "field": "view_counter"
            |}
            """.trimMargin()

            val filter = GsonBuilder()
                    .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.FilterDeserializer())
                    .create()
                    .fromJson(jsonString, SearchQuery.Filter::class.java)
            assertThat(filter, nullValue())
        }
    }
    class IntTest {
        class Success {
            @Test
            fun success() {
                val filter0 = SearchQuery.Filter.Equal.Int(SearchQuery.Field.view_counter, 1000)
                val jsonString = Gson().toJson(filter0)

                val filter1 = GsonBuilder()
                        .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.FilterDeserializer())
                        .create()
                        .fromJson(jsonString, SearchQuery.Filter::class.java) as SearchQuery.Filter.Equal.Int
                assertThat(filter1.type, `is`(SearchQuery.Filter.Type.equal))
                assertThat(filter1.field, `is`(SearchQuery.Field.view_counter))
                assertThat(filter1.value, `is`(1000))
            }
        }
    }
    class StringTest {
        class Success {
            @Test
            fun success() {
                val filter0 = SearchQuery.Filter.Equal.String(SearchQuery.Field.start_time, "2016-06-01 00:00:00")
                val jsonString = Gson().toJson(filter0)

                val filter1 = GsonBuilder()
                        .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.FilterDeserializer())
                        .create()
                        .fromJson(jsonString, SearchQuery.Filter::class.java) as SearchQuery.Filter.Equal.String
                assertThat(filter1.type, `is`(SearchQuery.Filter.Type.equal))
                assertThat(filter1.field, `is`(SearchQuery.Field.start_time))
                assertThat(filter1.value, `is`("2016-06-01 00:00:00"))
            }
        }
    }
}