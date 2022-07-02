package kim.uno.mock.extension

import org.junit.Assert
import org.junit.Test
import kotlin.math.ceil

class NumberExtTest {

    @Test
    fun `string to number`() {
        val number = " 010 3333-3333".toNumberString()
        Assert.assertEquals(number, "01033333333")
    }

    @Test
    fun `formatted number string to number`() {
        val number = "27,3030".toNumberInt()
        Assert.assertEquals(number, 273030)
    }

    @Test
    fun `minus number to number`() {
        val number = "-200".toNumberInt()
        Assert.assertEquals(number, -200)
    }

    @Test
    fun `minus number to number format`() {
        val number = "-200".toNumberFormat()
        Assert.assertEquals(number, "-200")
    }

    @Test
    fun `number to number format`() {
        val number = "-100100100".toNumberFormat()
        Assert.assertEquals(number, "-100,100,100")
    }

    @Test
    fun `store max price ceil`() {
        val price = ("39950".toNumberInt().takeIf { it > 100 }?: 0) / 100.0
        val priceCeil = (ceil(price) * 100).toInt()
        Assert.assertEquals(priceCeil.toFloat().toNumberFormat(), "40,000")
    }

    @Test
    fun `phone number verification success case 1`() {
        Assert.assertEquals("1544-4444".isTel(), true)
    }

    @Test
    fun `phone number verification success case 2`() {
        Assert.assertEquals("333-4444".isTel(), true)
    }

    @Test
    fun `phone number verification success case 3`() {
        Assert.assertEquals("02-333-4444".isTel(), true)
    }

    @Test
    fun `phone number verification success case 4`() {
        Assert.assertEquals("02-4444-4444".isTel(), true)
    }

    @Test
    fun `phone number verification success case 5`() {
        Assert.assertEquals("031-333-4444".isTel(), true)
    }

    @Test
    fun `phone number verification success case 6`() {
        Assert.assertEquals("031-4444-4444".isTel(), true)
    }

    @Test
    fun `phone number verification success case 7`() {
        Assert.assertEquals("010-333-4444".isTel(), true)
    }

    @Test
    fun `phone number verification success case 8`() {
        Assert.assertEquals("010-4444-4444".isTel(), true)
    }

    @Test
    fun `phone number verification success case 9`() {
        Assert.assertEquals("+8210-4444-4444".isTel(), true)
    }

    @Test
    fun `phone number verification fail case 1`() {
        Assert.assertEquals("1880".isTel(), false)
    }

    @Test
    fun `phone number verification fail case 2`() {
        Assert.assertEquals("22-4444".isTel(), false)
    }

    @Test
    fun `phone number verification fail case 3`() {
        Assert.assertEquals("1-333-4444".isTel(), false)
    }

    @Test
    fun `phone number verification fail case 4`() {
        Assert.assertEquals("+82-4444".isTel(), false)
    }

    @Test
    fun `phone number verification fail case 5`() {
        Assert.assertEquals("+821-4444".isTel(), false)
    }

}
