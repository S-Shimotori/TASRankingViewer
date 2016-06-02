package net.terminal_end.tasrankingviewer.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

/**
 * Created by S-Shimotori on 6/2/16.
 */

class SearchQuery private constructor(query: String, search: SearchField, join: List<Field>, filters: List<Filter>?, sortBy: SortBy?, order: Order?, from: Int?, size: Int?, issuer: String) {
    val query = query
    val search = search.fields
    val join = join
    val filters = filters
    val sort_by = sortBy
    val order = order
    val from = from
    val size = size

    companion object {
        val service = listOf("video")
        val issuer = "net.terminal_end.tasrankingviewer"

        fun getInstance(query: String, search: SearchField, join: List<Field>, filters: List<Filter>?, sortBy: SortBy?, order: Order?, from: Int?, size: Int?, issuer: String): SearchQuery? {
            if ((from == null || from >= 0) && (size == null || 0 <= size && size <= 100)) {
                return SearchQuery(query, search, join, filters, sortBy, order, from, size, issuer)
            } else {
                return null
            }
        }
    }

    enum class Field {
        cmsid,
        title,
        description,
        tags,
        start_time,
        thumbnail_url,
        view_counter,
        comment_counter,
        mylist_counter,
        last_res_body,
        length_seconds
    }

    interface Filter {
        val type: Type
        val field: Field

        interface Equal: Filter {
            class Int(field: Field, value: kotlin.Int): Equal {
                override val type = Type.equal
                override val field = field
                val value = value
            }
            class String(field: Field, value: kotlin.String): Equal {
                override val type = Type.equal
                override val field = field
                val value = value
            }
        }

        interface Range: Filter {
            val include_lower: Boolean?
            val include_upper: Boolean?

            class Int(field: Field, from: kotlin.Int?, to: kotlin.Int?, include_lower: Boolean?, include_upper: Boolean?): Range {
                override val type = Type.range
                override val field = field
                val from = from
                val to = to
                override val include_lower = include_lower
                override val include_upper = include_upper
            }

            class String(field: Field, from: kotlin.String?, to: kotlin.String?, include_lower: Boolean?, include_upper: Boolean?): Range {
                override val type = Type.range
                override val field = field
                val from = from
                val to = to
                override val include_lower = include_lower
                override val include_upper = include_upper
            }
        }

        enum class Type() {
            equal,
            range
        }
    }

    class FilterDeserializer: JsonDeserializer<Filter> {
        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Filter? {
            if (json == null) {
                return null
            }

            val jsonObject = json as JsonObject
            if (jsonObject["type"] == null || jsonObject["field"] == null) {
                return null
            }

            val type = try {
                Filter.Type.valueOf(jsonObject["type"].asString)
            } catch (e: Exception) {
                return null
            }
            val field = try {
                Field.valueOf(jsonObject["field"].asString)
            } catch (e: Exception) {
                return null
            }

            return when (type) {
                Filter.Type.equal -> {
                    if (jsonObject["value"] != null) {
                        try {
                            Filter.Equal.Int(field, jsonObject["value"].asInt)
                        } catch (e: Exception) {
                            Filter.Equal.String(field, jsonObject["value"].asString)
                        } catch (e: Exception) {
                            null
                        }
                    } else {
                        null
                    }
                }
                Filter.Type.range -> {
                    val includeLowerJsonElement = jsonObject["include_lower"]
                    val includeLower = if (includeLowerJsonElement != null) {
                        try {
                            includeLowerJsonElement.asBoolean
                        } catch (e: Exception) {
                            return null
                        }
                    } else {
                        null
                    }
                    val includeUpperJsonElement = jsonObject["include_upper"]
                    val includeUpper = if (includeUpperJsonElement != null) {
                        try {
                            includeUpperJsonElement.asBoolean
                        } catch (e: Exception) {
                            return null
                        }
                    } else {
                        null
                    }

                    val fromJsonElement = jsonObject["from"]
                    val toJsonElement = jsonObject["to"]

                    if (fromJsonElement != null && toJsonElement != null) {
                        return try {
                            Filter.Range.Int(field, fromJsonElement.asInt, toJsonElement.asInt, includeLower, includeUpper)
                        } catch (e: Exception) {
                            Filter.Range.String(field, fromJsonElement.asString, toJsonElement.asString, includeLower, includeUpper)
                        } catch (e: Exception) {
                            null
                        }
                    } else if (fromJsonElement != null) {
                        return try {
                            Filter.Range.Int(field, fromJsonElement.asInt, null, includeLower, includeUpper)
                        } catch (e: Exception) {
                            Filter.Range.String(field, fromJsonElement.asString, null, includeLower, includeUpper)
                        }
                    } else if (toJsonElement != null) {
                        return try {
                            Filter.Range.Int(field, null, toJsonElement.asInt, includeLower, includeUpper)
                        } catch (e: Exception) {
                            Filter.Range.String(field, null, toJsonElement.asString, includeLower, includeUpper)
                        } catch (e: Exception) {
                            null
                        }
                    } else {
                        return Filter.Range.Int(field, null, null, includeLower, includeUpper)
                    }
                }
            }
        }
    }

    enum class SearchField(val fields: List<String>) {
        KEYWORD(listOf("title", "description", "tags")),
        TAG(listOf("tags_exact"))
    }

    enum class SortBy() {
        last_comment_time,
        view_counter,
        start_time,
        mylist_counter,
        comment_counter,
        length_seconds
    }

    enum class Order() {
        desc,
        asc
    }
}