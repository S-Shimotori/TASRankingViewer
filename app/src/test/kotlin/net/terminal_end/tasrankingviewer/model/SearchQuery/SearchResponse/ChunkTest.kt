package net.terminal_end.tasrankingviewer.model.SearchQuery.SearchResponse

import com.google.gson.GsonBuilder
import net.terminal_end.tasrankingviewer.model.Chunk
import net.terminal_end.tasrankingviewer.model.SearchResponse
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.samePropertyValuesAs
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * Created by S-Shimotori on 6/3/16.
 */

class ChunkTest {
    class Failure {
        @Test
        fun failWithoutDqnId() {
            val jsonString = """
            |{
            |   "endofstream":true,
            |   "type":"hits"
            |}
            """.trimMargin()

            val stats = GsonBuilder()
                    .registerTypeAdapter(Chunk::class.java, Chunk.Deserializer())
                    .create()
                    .fromJson(jsonString, Chunk::class.java)
            assertThat(stats, nullValue())
        }

        @Test
        fun failWithoutType() {
            val jsonString = """
            |{
            |   "dqnid": "c0676eea-cc77-4317-b442-d626c5f34558",
            |   "endofstream":true
            |}
            """.trimMargin()

            val stats = GsonBuilder()
                    .registerTypeAdapter(Chunk::class.java, Chunk.Deserializer())
                    .create()
                    .fromJson(jsonString, Chunk::class.java)
            assertThat(stats, nullValue())
        }
    }

    class Stats {
        class Success {
            @Test
            fun success() {
                val jsonString = """
                |{
                |   "dqnid": "c0676eea-cc77-4317-b442-d626c5f34558",
                |   "type": "stats",
                |   "values": [
                |       {
                |           "_rowid": 0,
                |           "service": "video",
                |           "total": 213353
                |       }
                |   ],
                |   "endofstream": false
                |}
                """.trimMargin()

                val stats = GsonBuilder()
                        .registerTypeAdapter(Chunk::class.java, Chunk.Deserializer())
                        .create()
                        .fromJson(jsonString, Chunk::class.java) as Chunk.Stats
                assertThat(stats.dqnid, `is`("c0676eea-cc77-4317-b442-d626c5f34558"))
                assertThat(stats.type, `is`(Chunk.Type.stats))
                assertThat(stats.values!!.asIterable(), `is`(contains(samePropertyValuesAs(Chunk.Value.Stats(0, SearchResponse.Service.video, 213353)))))
                assertThat(stats.endofstream, `is`(false))
            }

            @Test
            fun successWithoutValues() {
                val jsonString = """
                |{
                |   "dqnid": "c0676eea-cc77-4317-b442-d626c5f34558",
                |   "type": "stats",
                |   "endofstream": true
                |}
                """.trimMargin()

                val stats = GsonBuilder()
                        .registerTypeAdapter(Chunk::class.java, Chunk.Deserializer())
                        .create()
                        .fromJson(jsonString, Chunk::class.java) as Chunk.Stats
                assertThat(stats.values, nullValue())
            }

            @Test
            fun successWithoutEndOfStream() {
                val jsonString = """
                |{
                |   "dqnid": "c0676eea-cc77-4317-b442-d626c5f34558",
                |   "type": "stats",
                |   "values": [
                |       {
                |           "_rowid": 0,
                |           "service": "video",
                |           "total": 213353
                |       }
                |   ]
                |}
                """.trimMargin()

                val stats = GsonBuilder()
                        .registerTypeAdapter(Chunk::class.java, Chunk.Deserializer())
                        .create()
                        .fromJson(jsonString, Chunk::class.java) as Chunk.Stats
                assertThat(stats.endofstream, nullValue())
            }
        }

        class Failure {
            @Test
            fun failWithInvalidValues() {
                val jsonString = """
                |{
                |   "dqnid": "c0676eea-cc77-4317-b442-d626c5f34558",
                |   "type": "stats",
                |   "values": "hoge",
                |   "endofstream": false
                |}
                """.trimMargin()

                val stats = GsonBuilder()
                        .registerTypeAdapter(Chunk::class.java, Chunk.Deserializer())
                        .create()
                        .fromJson(jsonString, Chunk::class.java) as Chunk.Stats
                assertThat(stats.values, nullValue())
            }

            @Test
            fun failWithInvalidElementInValues() {
                val jsonString = """
                |{
                |   "dqnid": "c0676eea-cc77-4317-b442-d626c5f34558",
                |   "type": "stats",
                |   "values": [
                |       "hoge"
                |   ],
                |   "endofstream": false
                |}
                """.trimMargin()

                val stats = GsonBuilder()
                        .registerTypeAdapter(Chunk::class.java, Chunk.Deserializer())
                        .create()
                        .fromJson(jsonString, Chunk::class.java) as Chunk.Stats
                assertThat(stats.values, nullValue())
            }
        }
    }

    class Hits {
        class Success {
            @Test
            fun success() {
                val jsonString = """
                |{
                |   "dqnid": "c0676eea-cc77-4317-b442-d626c5f34558",
                |   "type": "hits",
                |   "values": [
                |       {
                |           "_rowid": 0,
                |           "cmsid": "sm13208019",
                |           "title": "【DIVA 2nd】　鏡音八八花合戦　【EDIT PV】",
                |           "view_counter": 9999
                |       }
                |   ],
                |   "endofstream": false
                |}
                """.trimMargin()
                val value = Chunk.Value.Hits(
                        0, "sm13208019", "【DIVA 2nd】　鏡音八八花合戦　【EDIT PV】", null, null, null, null, 9999, null, null, null, null
                )

                val stats = GsonBuilder()
                        .registerTypeAdapter(Chunk::class.java, Chunk.Deserializer())
                        .create()
                        .fromJson(jsonString, Chunk::class.java) as Chunk.Hits
                assertThat(stats.dqnid, `is`("c0676eea-cc77-4317-b442-d626c5f34558"))
                assertThat(stats.type, `is`(Chunk.Type.hits))
                assertThat(stats.values!!.asIterable(), `is`(contains(samePropertyValuesAs(value))))
                assertThat(stats.endofstream, `is`(false))
            }

            @Test
            fun successWithoutValues() {
                val jsonString = """
                |{
                |   "dqnid": "c0676eea-cc77-4317-b442-d626c5f34558",
                |   "type": "hits",
                |   "endofstream": false
                |}
                """.trimMargin()

                val stats = GsonBuilder()
                        .registerTypeAdapter(Chunk::class.java, Chunk.Deserializer())
                        .create()
                        .fromJson(jsonString, Chunk::class.java) as Chunk.Hits
                assertThat(stats.values, nullValue())
            }

            @Test
            fun successWithoutEndOfStream() {
                val jsonString = """
                |{
                |   "dqnid": "c0676eea-cc77-4317-b442-d626c5f34558",
                |   "type": "hits",
                |   "values": [
                |       {
                |           "_rowid": 0,
                |           "cmsid": "sm13208019",
                |           "title": "【DIVA 2nd】　鏡音八八花合戦　【EDIT PV】",
                |           "view_counter": 9999
                |       }
                |   ]
                |}
                """.trimMargin()

                val stats = GsonBuilder()
                        .registerTypeAdapter(Chunk::class.java, Chunk.Deserializer())
                        .create()
                        .fromJson(jsonString, Chunk::class.java) as Chunk.Hits
                assertThat(stats.endofstream, nullValue())
            }
        }

        class Failure {
            @Test
            fun failWithInvalidValues() {
                val jsonString = """
                |{
                |   "dqnid": "c0676eea-cc77-4317-b442-d626c5f34558",
                |   "type": "hits",
                |   "values": "hoge",
                |   "endofstream": false
                |}
                """.trimMargin()

                val stats = GsonBuilder()
                        .registerTypeAdapter(Chunk::class.java, Chunk.Deserializer())
                        .create()
                        .fromJson(jsonString, Chunk::class.java) as Chunk.Hits
                assertThat(stats.values, nullValue())
            }

            @Test
            fun failWithInvalidItemInValues() {
                val jsonString = """
                |{
                |   "dqnid": "c0676eea-cc77-4317-b442-d626c5f34558",
                |   "type": "hits",
                |   "values": [
                |       "hoge"
                |   ],
                |   "endofstream": false
                |}
                """.trimMargin()

                val stats = GsonBuilder()
                        .registerTypeAdapter(Chunk::class.java, Chunk.Deserializer())
                        .create()
                        .fromJson(jsonString, Chunk::class.java) as Chunk.Hits
                assertThat(stats.values, nullValue())
            }
        }
    }
}