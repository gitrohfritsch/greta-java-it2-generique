package distributor.core;

import distributor.Coin;
import distributor.Money;

import java.math.BigDecimal;

public class DistributorCheckout {

    private Money total = new Money();

    public void insert(Money insert) {
        total.add(insert);
    }

    public Money pay(Money money, double price) {
        insert(money);
        double moneyBack = BigDecimal.valueOf(money.sum())
                .subtract(BigDecimal.valueOf(price))
                .doubleValue();
        return pull(moneyBack);
    }

    private Money pull(double value) {
        Money moneyBack = new Money();
        if(value > 0) {
            for(Coin coin : Coin.orderByValueDesc()) {
                while(total.contains(coin) && value >= coin.getValue()) {
                    moneyBack.add(coin);
                    total.remove(coin);
                    BigDecimal newValue =
                            BigDecimal.valueOf(value)
                            .subtract(BigDecimal.valueOf(coin.getValue()));
                    value = newValue.doubleValue();
                }
            }
        }
        return moneyBack;
    }
}
