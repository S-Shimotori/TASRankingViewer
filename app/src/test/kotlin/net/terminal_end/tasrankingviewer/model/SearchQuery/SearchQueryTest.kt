package net.terminal_end.tasrankingviewer.model.SearchQuery

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.terminal_end.tasrankingviewer.model.SearchQuery
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * Created by S-Shimotori on 6/2/16.
 */

class SearchQueryTest {
    @Test
    fun issuerLength() {
        assertThat(SearchQuery.ISSUER_NAME.length, `is`(lessThanOrEqualTo(40)))
    }

    @Test
    fun from_isNotNegative() {
        val fromNegative = SearchQuery.getInstance("", SearchQuery.SearchField.TAG, listOf(), null, null, null, -1, null)
        val fromZero = SearchQuery.getInstance("", SearchQuery.SearchField.TAG, listOf(), null, null, null, 0, null)
        val fromPositive = SearchQuery.getInstance("", SearchQuery.SearchField.TAG, listOf(), null, null, null, 1, null)

        assertThat(fromNegative, nullValue())
        assertThat(fromZero!!.from, `is`(0))
        assertThat(fromPositive!!.from, `is`(1))
    }

    @Test
    fun size_isValid() {
        val sizeNegative = SearchQuery.getInstance("", SearchQuery.SearchField.TAG, listOf(), null, null, null, null, -1)
        val sizeZero = SearchQuery.getInstance("", SearchQuery.SearchField.TAG, listOf(), null, null, null, null, 0)
        val sizeMax = SearchQuery.getInstance("", SearchQuery.SearchField.TAG, listOf(), null, null, null, null, 100)
        val sizeTooMany = SearchQuery.getInstance("", SearchQuery.SearchField.TAG, listOf(), null, null, null, null, 101)

        assertThat(sizeNegative, nullValue())
        assertThat(sizeZero!!.size, `is`(0))
        assertThat(sizeMax!!.size, `is`(100))
        assertThat(sizeTooMany, nullValue())
    }

    class Success {
        @Test
        fun success() {
            val join = listOf(SearchQuery.Field.cmsid, SearchQuery.Field.title, SearchQuery.Field.view_counter)
            val filter: SearchQuery.Filter = SearchQuery.Filter.Range.Int(SearchQuery.Field.view_counter, null, 10000, null, null)
            val searchQuery0 = SearchQuery.getInstance(
                    "初音ミク",
                    SearchQuery.SearchField.KEYWORD,
                    join,
                    listOf(filter),
                    SearchQuery.SortBy.view_counter,
                    SearchQuery.Order.asc,
                    0, 3
            )!!
            val jsonString = Gson().toJson(searchQuery0)
            val searchQuery1 = GsonBuilder()
                    .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.Deserializer())
                    .create()
                    .fromJson(jsonString, SearchQuery::class.java)

            assertThat(searchQuery1.query, `is`("初音ミク"))
            assertThat(searchQuery1.service, `is`(contains("video")))
            assertThat(searchQuery1.search, `is`(SearchQuery.SearchField.KEYWORD.fields))
            assertThat(searchQuery1.join, `is`(join))
            assertThat(searchQuery1.filters!!.asIterable(), `is`(contains(samePropertyValuesAs(filter))))
            assertThat(searchQuery1.sort_by, `is`(SearchQuery.SortBy.view_counter))
            assertThat(searchQuery1.order, `is`(SearchQuery.Order.asc))
            assertThat(searchQuery1.from, `is`(0))
            assertThat(searchQuery1.size, `is`(3))
            assertThat(searchQuery1.issuer, `is`(SearchQuery.ISSUER_NAME))
        }

        @Test
        fun successWithoutFilters() {
            val searchQuery0 = SearchQuery.getInstance(
                    "初音ミク",
                    SearchQuery.SearchField.KEYWORD,
                    listOf(SearchQuery.Field.cmsid, SearchQuery.Field.title, SearchQuery.Field.view_counter),
                    null,
                    SearchQuery.SortBy.view_counter,
                    SearchQuery.Order.asc,
                    0, 3
            )!!
            val jsonString = Gson().toJson(searchQuery0)
            val searchQuery1 = GsonBuilder()
                    .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.Deserializer())
                    .create()
                    .fromJson(jsonString, SearchQuery::class.java)

            assertThat(searchQuery1.filters, nullValue())
        }

        @Test
        fun successWithoutSortBy() {
            val searchQuery0 = SearchQuery.getInstance(
                    "初音ミク",
                    SearchQuery.SearchField.KEYWORD,
                    listOf(SearchQuery.Field.cmsid, SearchQuery.Field.title, SearchQuery.Field.view_counter),
                    listOf(SearchQuery.Filter.Range.Int(SearchQuery.Field.view_counter, null, 10000, null, null)),
                    null,
                    SearchQuery.Order.asc,
                    0, 3
            )!!
            val jsonString = Gson().toJson(searchQuery0)
            val searchQuery1 = GsonBuilder()
                    .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.Deserializer())
                    .create()
                    .fromJson(jsonString, SearchQuery::class.java)

            assertThat(searchQuery1.sort_by, nullValue())
        }

        @Test
        fun successWithoutOrder() {
            val searchQuery0 = SearchQuery.getInstance(
                    "初音ミク",
                    SearchQuery.SearchField.KEYWORD,
                    listOf(SearchQuery.Field.cmsid, SearchQuery.Field.title, SearchQuery.Field.view_counter),
                    listOf(SearchQuery.Filter.Range.Int(SearchQuery.Field.view_counter, null, 10000, null, null)),
                    SearchQuery.SortBy.view_counter,
                    null,
                    0, 3
            )!!
            val jsonString = Gson().toJson(searchQuery0)
            val searchQuery1 = GsonBuilder()
                    .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.Deserializer())
                    .create()
                    .fromJson(jsonString, SearchQuery::class.java)

            assertThat(searchQuery1.order, nullValue())
        }

        @Test
        fun successWithoutFrom() {
            val searchQuery0 = SearchQuery.getInstance(
                    "初音ミク",
                    SearchQuery.SearchField.KEYWORD,
                    listOf(SearchQuery.Field.cmsid, SearchQuery.Field.title, SearchQuery.Field.view_counter),
                    listOf(SearchQuery.Filter.Range.Int(SearchQuery.Field.view_counter, null, 10000, null, null)),
                    SearchQuery.SortBy.view_counter,
                    SearchQuery.Order.asc,
                    null, 3
            )!!
            val jsonString = Gson().toJson(searchQuery0)
            val searchQuery1 = GsonBuilder()
                    .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.Deserializer())
                    .create()
                    .fromJson(jsonString, SearchQuery::class.java)

            assertThat(searchQuery1.from, nullValue())
        }

        @Test
        fun successWithoutSize() {
            val searchQuery0 = SearchQuery.getInstance(
                    "初音ミク",
                    SearchQuery.SearchField.KEYWORD,
                    listOf(SearchQuery.Field.cmsid, SearchQuery.Field.title, SearchQuery.Field.view_counter),
                    listOf(SearchQuery.Filter.Range.Int(SearchQuery.Field.view_counter, null, 10000, null, null)),
                    SearchQuery.SortBy.view_counter,
                    SearchQuery.Order.asc,
                    0, null
            )!!
            val jsonString = Gson().toJson(searchQuery0)
            val searchQuery1 = GsonBuilder()
                    .registerTypeAdapter(SearchQuery.Filter::class.java, SearchQuery.Deserializer())
                    .create()
                    .fromJson(jsonString, SearchQuery::class.java)

            assertThat(searchQuery1.size, nullValue())
        }
    }
}