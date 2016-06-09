package net.terminal_end.tasrankingviewer.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject

/**
 * Created by S-Shimotori on 6/3/16.
 */

interface Chunk {
    val dqnid: String
    val type: Type
    val values: List<Value>?
    val endofstream: Boolean?

    class Stats(dqnid: String, values: List<Value.Stats>?, endofstream: Boolean?): Chunk {
        override val dqnid = dqnid
        override val type = Type.stats
        override val values = values
        override val endofstream = endofstream
    }

    class Hits(dqnid: String, values: List<Value.Hits>?, endofstream: Boolean?): Chunk {
        override val dqnid = dqnid
        override val type = Type.hits
        override val values = values
        override val endofstream = endofstream
    }

    class Deserializer : JsonDeserializer<Chunk> {
        override fun deserialize(json: JsonElement?, typeOfT: java.lang.reflect.Type?, context: JsonDeserializationContext?): Chunk? {
            if (json == null || context == null) {
                return null
            }

            val jsonObject = json as JsonObject
            if (jsonObject["dqnid"] == null || jsonObject["type"] == null) {
                return null
            }
            val dqnId = try {
                jsonObject["dqnid"].asString
            } catch (e: Exception) {
                return null
            }
            val type = try {
                Type.valueOf(jsonObject["type"].asString)
            } catch (e: Exception) {
                return null
            }
            val endOfStream = if (jsonObject["endofstream"] != null) {
                try {
                    jsonObject["endofstream"].asBoolean
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }

            return when (type) {
                Type.stats -> {
                    val values = if (jsonObject["values"] != null) {
                        try {
                            val jsonArray = jsonObject["values"].asJsonArray
                            jsonArray.map {
                                context.deserialize<Value.Stats>(it, Value.Stats::class.java)
                            }
                        } catch (e: Exception) {
                            null
                        }
                    } else {
                        null
                    }
                    Chunk.Stats(dqnId, values, endOfStream)
                }
                Type.hits -> {
                    val values = if (jsonObject["values"] != null) {
                        try {
                            val jsonArray = jsonObject["values"].asJsonArray
                            jsonArray.map {
                                context.deserialize<Value.Hits>(it, Value.Hits::class.java)
                            }
                        } catch (e: Exception) {
                            null
                        }
                    } else {
                        null
                    }
                    Chunk.Hits(dqnId, values, endOfStream)
                }
            }
        }

    }

    enum class Type {
        stats,
        hits
    }

    interface Value {
        val _rowid: Int

        class Stats(rowId: Int, val service: SearchResponse.Service, val total: Int): Value {
            override val _rowid = rowId
        }

        class Hits(rowId: Int,
                   cmsId: String?, title: String?, description: String?, tags: String?, startTime: String?, thumbnailUrl: String?,
                   viewCounter: Int?, commentCounter: Int?, myListCounter: Int?,
                   lastResBody: String?, lengthSeconds: Int?): Value {
            override val _rowid = rowId
            val cmsid = cmsId
            val title = title
            val description = description
            val tags = tags
            val start_time = startTime
            val thumbnail_url = thumbnailUrl
            val view_counter = viewCounter
            val comment_counter = commentCounter
            val mylist_counter = myListCounter
            val last_res_body = lastResBody
            val length_seconds = lengthSeconds
        }
    }
}
