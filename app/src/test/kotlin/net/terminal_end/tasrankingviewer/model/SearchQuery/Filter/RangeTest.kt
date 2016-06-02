package net.terminal_end.tasrankingviewer.model.SearchQuery.Filter

import com.google.gson.GsonBuilder
import net.terminal_end.tasrankingviewer.model.SearchQuery
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * Created by S-Shimotori on 6/2/16.
 */

class RangeTest {
    class Success {
        @Test
        fun successWithoutIncludeLower() {
            val jsonString = """
            |{
            |   "type": "range",
            |   "field": "length_seconds",
            |   "from": 0,
            |   "to": 300,
            |   "include_upper": true
            |}
            """.trimMargin()

            val filter = GsonBuilder()
                    .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.FilterDeserializer())
                    .create()
                    .fromJson(jsonString, SearchQuery.Filter::class.java) as SearchQuery.Filter.Range.Int
            assertThat(filter.type, `is`(SearchQuery.Filter.Type.range))
            assertThat(filter.field, `is`(SearchQuery.Field.length_seconds))
            assertThat(filter.include_lower, nullValue())
            assertThat(filter.include_upper, `is`(true))
        }

        @Test
        fun successWithoutIncludeUpper() {
            val jsonString = """
            |{
            |   "type": "range",
            |   "field": "length_seconds",
            |   "from": 0,
            |   "to": 300,
            |   "include_lower": true
            |}
            """.trimMargin()

            val filter = GsonBuilder()
                    .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.FilterDeserializer())
                    .create()
                    .fromJson(jsonString, SearchQuery.Filter::class.java) as SearchQuery.Filter.Range.Int
            assertThat(filter.type, `is`(SearchQuery.Filter.Type.range))
            assertThat(filter.field, `is`(SearchQuery.Field.length_seconds))
            assertThat(filter.include_lower, `is`(true))
            assertThat(filter.include_upper, nullValue())
        }
    }

    class IntTest {
        class Success {
            @Test
            fun success() {
                val jsonString = """
                |{
                |   "type": "range",
                |   "field": "length_seconds",
                |   "from": 0,
                |   "to": 300,
                |   "include_lower": true,
                |   "include_upper": true
                |}
                """.trimMargin()

                val filter = GsonBuilder()
                        .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.FilterDeserializer())
                        .create()
                        .fromJson(jsonString, SearchQuery.Filter::class.java) as SearchQuery.Filter.Range.Int
                assertThat(filter.type, `is`(SearchQuery.Filter.Type.range))
                assertThat(filter.field, `is`(SearchQuery.Field.length_seconds))
                assertThat(filter.from, `is`(0))
                assertThat(filter.to, `is`(300))
                assertThat(filter.include_lower, `is`(true))
                assertThat(filter.include_upper, `is`(true))
            }

            @Test
            fun successWithoutFrom() {
                val jsonString = """
                |{
                |   "type": "range",
                |   "field": "length_seconds",
                |   "to": 300,
                |   "include_lower": true,
                |   "include_upper": true
                |}
                """.trimMargin()

                val filter = GsonBuilder()
                        .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.FilterDeserializer())
                        .create()
                        .fromJson(jsonString, SearchQuery.Filter::class.java) as SearchQuery.Filter.Range.Int
                assertThat(filter.from, nullValue())
            }

            @Test
            fun successWithoutTo() {
                val jsonString = """
                |{
                |   "type": "range",
                |   "field": "length_seconds",
                |   "from": 0,
                |   "include_lower": true,
                |   "include_upper": true
                |}
                """.trimMargin()

                val filter = GsonBuilder()
                        .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.FilterDeserializer())
                        .create()
                        .fromJson(jsonString, SearchQuery.Filter::class.java) as SearchQuery.Filter.Range.Int
                assertThat(filter.to, nullValue())
            }
        }
    }

    class StringTest {
        class Success {
            @Test
            fun success() {
                val jsonString = """
                |{
                |   "type": "range",
                |   "field": "start_time",
                |   "from": "2016-05-01 00:00:00",
                |   "to": "2016-05-31 23:59:59",
                |   "include_lower": true,
                |   "include_upper": true
                |}
                """.trimMargin()

                val filter = GsonBuilder()
                        .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.FilterDeserializer())
                        .create()
                        .fromJson(jsonString, SearchQuery.Filter::class.java) as SearchQuery.Filter.Range.String
                assertThat(filter.type, `is`(SearchQuery.Filter.Type.range))
                assertThat(filter.field, `is`(SearchQuery.Field.start_time))
                assertThat(filter.from, `is`("2016-05-01 00:00:00"))
                assertThat(filter.to, `is`("2016-05-31 23:59:59"))
                assertThat(filter.include_lower, `is`(true))
                assertThat(filter.include_upper, `is`(true))
            }

            @Test
            fun successWithoutFrom() {
                val jsonString = """
                |{
                |   "type": "range",
                |   "field": "start_time",
                |   "to": "2016-05-31 23:59:59",
                |   "include_lower": true,
                |   "include_upper": true
                |}
                """.trimMargin()

                val filter = GsonBuilder()
                        .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.FilterDeserializer())
                        .create()
                        .fromJson(jsonString, SearchQuery.Filter::class.java) as SearchQuery.Filter.Range.String
                assertThat(filter.from, nullValue())
            }

            @Test
            fun successWithoutTo() {
                val jsonString = """
                |{
                |   "type": "range",
                |   "field": "start_time",
                |   "from": "2016-05-01 00:00:00",
                |   "include_lower": true,
                |   "include_upper": true
                |}
                """.trimMargin()

                val filter = GsonBuilder()
                        .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.FilterDeserializer())
                        .create()
                        .fromJson(jsonString, SearchQuery.Filter::class.java) as SearchQuery.Filter.Range.String
                assertThat(filter.to, nullValue())
            }
        }
    }
}