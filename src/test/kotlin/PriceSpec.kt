import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PriceSpec : StringSpec({
    "test totals" {
        0 shouldBe price("")
        50 shouldBe price("A")
        80 shouldBe price("AB")
        115 shouldBe price("CDBA")
        100 shouldBe price("AA")
        130 shouldBe price("AAA")
        180 shouldBe price("AAAA")
        230 shouldBe price("AAAAA")
        260 shouldBe price("AAAAAA")
        160 shouldBe price("AAAB")
        175 shouldBe price("AAABB")
        190 shouldBe price("AAABBD")
        190 shouldBe price("DABABA")
    }

    "test incremental" {
        val co = CheckOut(RULES)
        0 shouldBe co.total()
        co.scan('A'); 50 shouldBe co.total()
        co.scan('B'); 80 shouldBe co.total()
        co.scan('A'); 130 shouldBe co.total()
        co.scan('A'); 160 shouldBe co.total()
        co.scan('B'); 175 shouldBe co.total()
    }
})

val RULES: Any = TODO()

fun price(goods: String): Int {
    val co = CheckOut(RULES)
    goods.forEach { co.scan(it) }
    return co.total()
}
