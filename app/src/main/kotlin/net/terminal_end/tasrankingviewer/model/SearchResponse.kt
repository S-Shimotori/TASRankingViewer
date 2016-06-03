package net.terminal_end.tasrankingviewer.model

import com.google.gson.GsonBuilder

/**
 * Created by S-Shimotori on 6/3/16.
 */

class SearchResponse(hits: List<Chunk.Hits>, stats: List<Chunk.Stats>) {
    val hits = hits
    val stats = stats

    companion object {
        fun getInstance(responseString: String): SearchResponse {
            val strings = responseString.split("\n")
            val gson = GsonBuilder()
                    .registerTypeAdapter(Chunk::class.java, Chunk.ChunkDeserializer())
                    .create()
            val chunks = strings.map { gson.fromJson(it, Chunk::class.java) }
            val hits = mutableListOf<Chunk.Hits>()
            val stats = mutableListOf<Chunk.Stats>()
            for (chunk in chunks) {
                when (chunk) {
                    is Chunk.Hits -> {
                        hits.add(chunk)
                    }
                    is Chunk.Stats -> {
                        stats.add(chunk)
                    }
                }
            }
            return SearchResponse(hits, stats)
        }
    }

    enum class Service {
        video
    }
}