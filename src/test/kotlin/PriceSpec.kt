import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.WithDataTestName
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

@Suppress("EXPERIMENTAL_API_USAGE")
class PriceSpec : FreeSpec({
    "test totals" - {
        withData(
            Transaction("", 0),
            Transaction("A", 50),
            Transaction("AB", 80),
            Transaction("CDBA", 115),
            Transaction("AA", 100),
            Transaction("AAA", 130),
            Transaction("AAAA", 180),
            Transaction("AAAAA", 230),
            Transaction("AAAAAA", 260),
            Transaction("AAAB", 160),
            Transaction("AAABB", 175),
            Transaction("AAABBD", 190),
            Transaction("DABABA", 190),
        ) { (goods, total) ->
            price(goods) shouldBe total
        }
    }

    "test incremental" {
        val co = CheckOut(RULES)
        assertSoftly {
            0 shouldBe co.total()
            co.scan('A'); co.total() shouldBe 50
            co.scan('B'); co.total() shouldBe 80
            co.scan('A'); co.total() shouldBe 130
            co.scan('A'); co.total() shouldBe 160
            co.scan('B'); co.total() shouldBe 175
        }
    }
})

val RULES = setOf(
    SpecialPrice(FixedPrice('A', 50), units = 3, discountedPrice = 130),
    SpecialPrice(FixedPrice('B', 30), units = 2, discountedPrice = 45),
    FixedPrice('C', 20),
    FixedPrice('D', 15),
)

fun price(goods: String): Int {
    val co = CheckOut(RULES)
    goods.forEach { co.scan(it) }
    return co.total()
}

data class Transaction(val goods: String, val total: Int) : WithDataTestName {
    override fun dataTestName() = "|${goods}|"
}
