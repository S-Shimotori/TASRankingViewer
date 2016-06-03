package net.terminal_end.tasrankingviewer.model.SearchQuery.SearchResponse

import com.google.gson.Gson
import net.terminal_end.tasrankingviewer.model.Chunk
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * Created by S-Shimotori on 6/3/16.
 */

class ValueTest {
    class StatsTest {
        class Success {
            @Test
            fun success() {
                val jsonString = """
                |{
                |   "_rowid":0,
                |   "service":"video",
                |   "total":213353
                |}
                """.trimMargin()

                val stats = Gson().fromJson(jsonString, Chunk.Value.Stats::class.java)
                assertThat(stats._rowid, `is`(0))
                assertThat(stats.service, `is`("video"))
                assertThat(stats.total, `is`(213353))
            }
        }
    }

    class HitsTest {
        class Success {
            @Test
            fun success() {
                val jsonString = """
                |{
                |   "_rowid": 0,
                |   "cmsid": "sm00000000",
                |   "title": "タイトル",
                |   "description": "説明1<br />説明2",
                |   "tags": "タグ1 タグ2",
                |   "start_time": "2016-06-01 00:00:00",
                |   "thumbnail_url": "http:\/\/tn-skr2.smilevideo.jp\/smile?i=00000000",
                |   "view_counter": 10000,
                |   "comment_counter": 1000,
                |   "mylist_counter": 20,
                |   "last_res_body": "コメント1 コメント2 コメ...",
                |   "length_seconds": 100
                |}
                """.trimMargin()

                val stats = Gson().fromJson(jsonString, Chunk.Value.Hits::class.java)
                assertThat(stats._rowid, `is`(0))
                assertThat(stats.cmsid, `is`("sm00000000"))
                assertThat(stats.title, `is`("タイトル"))
                assertThat(stats.description, `is`("説明1<br />説明2"))
                assertThat(stats.tags, `is`("タグ1 タグ2"))
                assertThat(stats.start_time, `is`("2016-06-01 00:00:00"))
                assertThat(stats.thumbnail_url, `is`("http://tn-skr2.smilevideo.jp/smile?i=00000000"))
                assertThat(stats.view_counter, `is`(10000))
                assertThat(stats.comment_counter, `is`(1000))
                assertThat(stats.mylist_counter, `is`(20))
                assertThat(stats.last_res_body, `is`("コメント1 コメント2 コメ..."))
                assertThat(stats.length_seconds, `is`(100))
            }

            @Test
            fun successWithoutCmsId() {
                val jsonString = """
                |{
                |   "_rowid": 0,
                |   "title": "タイトル",
                |   "description": "説明1<br />説明2",
                |   "tags": "タグ1 タグ2",
                |   "start_time": "2016-06-01 00:00:00",
                |   "thumbnail_url": "http:\/\/tn-skr2.smilevideo.jp\/smile?i=00000000",
                |   "view_counter": 10000,
                |   "comment_counter": 1000,
                |   "mylist_counter": 20,
                |   "last_res_body": "コメント1 コメント2 コメ...",
                |   "length_seconds": 100
                |}
                """.trimMargin()

                val stats = Gson().fromJson(jsonString, Chunk.Value.Hits::class.java)
                assertThat(stats.cmsid, nullValue())
            }

            @Test
            fun successWithoutTitle() {
                val jsonString = """
                |{
                |   "_rowid": 0,
                |   "cmsid": "sm00000000",
                |   "description": "説明1<br />説明2",
                |   "tags": "タグ1 タグ2",
                |   "start_time": "2016-06-01 00:00:00",
                |   "thumbnail_url": "http:\/\/tn-skr2.smilevideo.jp\/smile?i=00000000",
                |   "view_counter": 10000,
                |   "comment_counter": 1000,
                |   "mylist_counter": 20,
                |   "last_res_body": "コメント1 コメント2 コメ...",
                |   "length_seconds": 100
                |}
                """.trimMargin()

                val stats = Gson().fromJson(jsonString, Chunk.Value.Hits::class.java)
                assertThat(stats.title, nullValue())
            }

            @Test
            fun successWithoutDescription() {
                val jsonString = """
                |{
                |   "_rowid": 0,
                |   "cmsid": "sm00000000",
                |   "title": "タイトル",
                |   "tags": "タグ1 タグ2",
                |   "start_time": "2016-06-01 00:00:00",
                |   "thumbnail_url": "http:\/\/tn-skr2.smilevideo.jp\/smile?i=00000000",
                |   "view_counter": 10000,
                |   "comment_counter": 1000,
                |   "mylist_counter": 20,
                |   "last_res_body": "コメント1 コメント2 コメ...",
                |   "length_seconds": 100
                |}
                """.trimMargin()

                val stats = Gson().fromJson(jsonString, Chunk.Value.Hits::class.java)
                assertThat(stats.description, nullValue())
            }

            @Test
            fun successWithoutTags() {
                val jsonString = """
                |{
                |   "_rowid": 0,
                |   "cmsid": "sm00000000",
                |   "title": "タイトル",
                |   "description": "説明1<br />説明2",
                |   "start_time": "2016-06-01 00:00:00",
                |   "thumbnail_url": "http:\/\/tn-skr2.smilevideo.jp\/smile?i=00000000",
                |   "view_counter": 10000,
                |   "comment_counter": 1000,
                |   "mylist_counter": 20,
                |   "last_res_body": "コメント1 コメント2 コメ...",
                |   "length_seconds": 100
                |}
                """.trimMargin()

                val stats = Gson().fromJson(jsonString, Chunk.Value.Hits::class.java)
                assertThat(stats.tags, nullValue())
            }

            @Test
            fun successWithoutStartTime() {
                val jsonString = """
                |{
                |   "_rowid": 0,
                |   "cmsid": "sm00000000",
                |   "title": "タイトル",
                |   "description": "説明1<br />説明2",
                |   "tags": "タグ1 タグ2",
                |   "thumbnail_url": "http:\/\/tn-skr2.smilevideo.jp\/smile?i=00000000",
                |   "view_counter": 10000,
                |   "comment_counter": 1000,
                |   "mylist_counter": 20,
                |   "last_res_body": "コメント1 コメント2 コメ...",
                |   "length_seconds": 100
                |}
                """.trimMargin()

                val stats = Gson().fromJson(jsonString, Chunk.Value.Hits::class.java)
                assertThat(stats.start_time, nullValue())
            }

            @Test
            fun successWithoutThumbnailUrl() {
                val jsonString = """
                |{
                |   "_rowid": 0,
                |   "cmsid": "sm00000000",
                |   "title": "タイトル",
                |   "description": "説明1<br />説明2",
                |   "tags": "タグ1 タグ2",
                |   "start_time": "2016-06-01 00:00:00",
                |   "view_counter": 10000,
                |   "comment_counter": 1000,
                |   "mylist_counter": 20,
                |   "last_res_body": "コメント1 コメント2 コメ...",
                |   "length_seconds": 100
                |}
                """.trimMargin()

                val stats = Gson().fromJson(jsonString, Chunk.Value.Hits::class.java)
                assertThat(stats.thumbnail_url, nullValue())
            }

            @Test
            fun successWithoutViewCounter() {
                val jsonString = """
                |{
                |   "_rowid": 0,
                |   "cmsid": "sm00000000",
                |   "title": "タイトル",
                |   "description": "説明1<br />説明2",
                |   "tags": "タグ1 タグ2",
                |   "start_time": "2016-06-01 00:00:00",
                |   "thumbnail_url": "http:\/\/tn-skr2.smilevideo.jp\/smile?i=00000000",
                |   "comment_counter": 1000,
                |   "mylist_counter": 20,
                |   "last_res_body": "コメント1 コメント2 コメ...",
                |   "length_seconds": 100
                |}
                """.trimMargin()

                val stats = Gson().fromJson(jsonString, Chunk.Value.Hits::class.java)
                assertThat(stats.view_counter, nullValue())
            }

            @Test
            fun successWithoutCommentCounter() {
                val jsonString = """
                |{
                |   "_rowid": 0,
                |   "cmsid": "sm00000000",
                |   "title": "タイトル",
                |   "description": "説明1<br />説明2",
                |   "tags": "タグ1 タグ2",
                |   "start_time": "2016-06-01 00:00:00",
                |   "thumbnail_url": "http:\/\/tn-skr2.smilevideo.jp\/smile?i=00000000",
                |   "view_counter": 10000,
                |   "mylist_counter": 20,
                |   "last_res_body": "コメント1 コメント2 コメ...",
                |   "length_seconds": 100
                |}
                """.trimMargin()

                val stats = Gson().fromJson(jsonString, Chunk.Value.Hits::class.java)
                assertThat(stats.comment_counter, nullValue())
            }

            @Test
            fun successWithoutMyListCounter() {
                val jsonString = """
                |{
                |   "_rowid": 0,
                |   "cmsid": "sm00000000",
                |   "title": "タイトル",
                |   "description": "説明1<br />説明2",
                |   "tags": "タグ1 タグ2",
                |   "start_time": "2016-06-01 00:00:00",
                |   "thumbnail_url": "http:\/\/tn-skr2.smilevideo.jp\/smile?i=00000000",
                |   "view_counter": 10000,
                |   "comment_counter": 1000,
                |   "last_res_body": "コメント1 コメント2 コメ...",
                |   "length_seconds": 100
                |}
                """.trimMargin()

                val stats = Gson().fromJson(jsonString, Chunk.Value.Hits::class.java)
                assertThat(stats.mylist_counter, nullValue())
            }

            @Test
            fun successWithoutLastResBody() {
                val jsonString = """
                |{
                |   "_rowid": 0,
                |   "cmsid": "sm00000000",
                |   "title": "タイトル",
                |   "description": "説明1<br />説明2",
                |   "tags": "タグ1 タグ2",
                |   "start_time": "2016-06-01 00:00:00",
                |   "thumbnail_url": "http:\/\/tn-skr2.smilevideo.jp\/smile?i=00000000",
                |   "view_counter": 10000,
                |   "comment_counter": 1000,
                |   "mylist_counter": 20,
                |   "length_seconds": 100
                |}
                """.trimMargin()

                val stats = Gson().fromJson(jsonString, Chunk.Value.Hits::class.java)
                assertThat(stats.last_res_body, nullValue())
            }

            @Test
            fun successWithoutLengthSeconds() {
                val jsonString = """
                |{
                |   "_rowid": 0,
                |   "cmsid": "sm00000000",
                |   "title": "タイトル",
                |   "description": "説明1<br />説明2",
                |   "tags": "タグ1 タグ2",
                |   "start_time": "2016-06-01 00:00:00",
                |   "thumbnail_url": "http:\/\/tn-skr2.smilevideo.jp\/smile?i=00000000",
                |   "view_counter": 10000,
                |   "comment_counter": 1000,
                |   "mylist_counter": 20,
                |   "last_res_body": "コメント1 コメント2 コメ..."
                |}
                """.trimMargin()

                val stats = Gson().fromJson(jsonString, Chunk.Value.Hits::class.java)
                assertThat(stats.length_seconds, nullValue())
            }
        }
    }
}