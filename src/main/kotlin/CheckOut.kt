class CheckOut(val rules: Set<PricingRule>) {
    val scanned = mutableListOf<Char>()

    fun scan(item: Char) {
        scanned.add(item)
    }

    fun total() = rules.sumOf { it.calculate(scanned) }
}

interface PricingRule {
    fun calculate(items: List<Char>): Int
    fun appliesTo(item: Char): Boolean
}

class FixedPrice(val item: Char, val unitPrice: Int) : PricingRule {
    override fun calculate(items: List<Char>) = items.count(this::appliesTo) * unitPrice
    override fun appliesTo(item: Char) = item == this.item
}

class SpecialPrice(val base: PricingRule, val units: Int, val discountedPrice: Int) : PricingRule by base {
    override fun calculate(items: List<Char>): Int {
        val applicable = items.filter(this::appliesTo)
        return discountedPartial(applicable) + basePartial(applicable)
    }

    private fun discountedPartial(applicable: List<Char>) =
        applicable.size / units * discountedPrice

    private fun basePartial(applicable: List<Char>) =
        base.calculate(applicable.take(applicable.size % units))
}
