package gadget

import gadget.basic.tool.DateTime
import org.junit.Test
import java.text.SimpleDateFormat

class GadgetUnitTest {

    @Test
    fun hi() {
        println("Hi!")
    }

    @Test
    fun testDateTime() {
        val formater = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        assert(DateTime.getSpecificTimestampOneDay(formater.parse("2026-01-01 08:30:00")!!.time, 10 * 60 * 60 * 1000) == 1767232800000L/*2026-01-01 10:00:00*/)
        assert(DateTime.getDayDifference(formater.parse("2026-01-01 08:30:00")!!.time, formater.parse("2026-01-01 18:30:00")!!.time) == 0)
        assert(DateTime.getDayDifference(formater.parse("2026-01-01 08:30:00")!!.time, formater.parse("2026-01-03 18:30:00")!!.time) == 2)
        assert(DateTime.getDayDifference(formater.parse("2026-01-01 08:30:00")!!.time, formater.parse("2025-01-01 18:30:00")!!.time) == -365)
        println("[==== testDateTime OK! ====]")
    }
}