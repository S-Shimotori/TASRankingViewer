package net.terminal_end.tasrankingviewer

import com.google.gson.Gson
import net.terminal_end.tasrankingviewer.model.LastModified
import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.*

/**
 * Created by S-Shimotori on 6/1/16.
 */

class LastModifiedTest {
    @Test
    fun fromJson_isCorrect() {
        val jsonString = """
        |{
        |   "last_modified" : "yyyy-MM-dd HH:mm:ss"
        |}
        """.trimMargin()

        val lastModified = Gson().fromJson(jsonString, LastModified::class.java)
        assertThat(lastModified.last_modified, `is`("yyyy-MM-dd HH:mm:ss"))
    }
}