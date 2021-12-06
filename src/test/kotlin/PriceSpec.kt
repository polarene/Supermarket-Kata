import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PriceSpec : StringSpec({
    "test totals" {
        assertSoftly {
            price("") shouldBe 0
            price("A") shouldBe 50
            price("AB") shouldBe 80
            price("CDBA") shouldBe 115
            price("AA") shouldBe 100
            price("AAA") shouldBe 130
            price("AAAA") shouldBe 180
            price("AAAAA") shouldBe 230
            price("AAAAAA") shouldBe 260
            price("AAAB") shouldBe 160
            price("AAABB") shouldBe 175
            price("AAABBD") shouldBe 190
            price("DABABA") shouldBe 190
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
