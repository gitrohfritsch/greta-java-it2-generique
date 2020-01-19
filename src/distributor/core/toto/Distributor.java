package distributor.core.toto;


import distributor.Money;
import distributor.core.*;
import distributor.product.Drink;
import distributor.product.DrinkType;

import java.util.List;
import java.util.Map;

public class Distributor {

    private DistributorStore store = new DistributorStore();
    private Map<DrinkType, Double> drinkTypePrice;
    private DistributorCheckout checkout = new DistributorCheckout();

    public Distributor(List<Drink> drinks, Map<DrinkType, Double> drinkTypePrice, Money money) {
        this.store.addAll(drinks);
        this.drinkTypePrice = drinkTypePrice;
        this.checkout.insert(money);
    }

    public DistributorSelectResult select(DrinkType drinkType, Money money) {
        DistributorSelectResult result = new DistributorSelectResult();
        double price = drinkTypePrice.get(drinkType);
        boolean isEnough = money.sum() >= price;

        try {
            if (/*store.findAndLoad(drinkType) &&*/ isEnough) {
                result.setDrink(store.pullLoaded());
                result.setMoneyBack(checkout.pay(money, price));
            } else {
                if (!store.isLoaded()) {
                    result.getErrors().add(DistributorSelectResultError.NO_DRINK_FOR_TYPE);
                }
                if (!isEnough) {
                    result.getErrors().add(DistributorSelectResultError.NO_ENOUGH_MONEY);
                }
            }
        } catch (DistributorStoreException e) {
            e.printStackTrace();
            result.getErrors().add(DistributorSelectResultError.ERROR_WITH_STORE);
        }


        return result;
    }

}
