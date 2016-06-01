package net.terminal_end.tasrankingviewer.util

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import java.util.*

/**
 * Created by S-Shimotori on 6/1/16.
 */

class CalendarTest {
    class ToCalendar {
        class Success {
            @Test
            fun success() {
                val string = "2016-06-01 07:05:28"
                val calendar = string.toCalendar().get()

                assertThat(calendar.get(Calendar.YEAR), `is`(2016))
                assertThat(calendar.get(Calendar.MONTH), `is`(Calendar.JUNE))
                assertThat(calendar.get(Calendar.DATE), `is`(1))
                assertThat(calendar.get(Calendar.HOUR_OF_DAY), `is`(7))
                assertThat(calendar.get(Calendar.MINUTE), `is`(5))
                assertThat(calendar.get(Calendar.SECOND), `is`(28))
            }
        }
    }
}