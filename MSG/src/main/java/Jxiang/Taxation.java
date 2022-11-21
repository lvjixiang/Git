package Jxiang;

/**
 * 各种税费
 */
public class Taxation {
    Double propertyTax; // 财产税每年
    Double rate = 0.003; // 税率
    Double insurance = 2000.0; // 房产保险费每年

    public Taxation(Double housePrice) {
        this.propertyTax = housePrice * this.rate;
    }

    public Double getPropertyTax() {
        return propertyTax;
    }

    public void setPropertyTax(Double propertyTax) {
        this.propertyTax = propertyTax;
    }

    public Double getInsurance() {
        return insurance;
    }

    public void setInsurance(Double insurance) {
        this.insurance = insurance;
    }
}
