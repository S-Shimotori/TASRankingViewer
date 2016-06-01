package net.terminal_end.tasrankingviewer

import com.google.gson.Gson
import net.terminal_end.tasrankingviewer.model.LastModified
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * Created by S-Shimotori on 6/1/16.
 */

class LastModifiedTest {
    @Test
    fun fromJson_isCorrect() {
        val jsonString = """
        |{
        |   "last_modified" : "2016-06-01 07:05:28"
        |}
        """.trimMargin()

        val lastModified = Gson().fromJson(jsonString, LastModified::class.java)
        assertThat(lastModified.last_modified, `is`("2016-06-01 07:05:28"))
    }
}