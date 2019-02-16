package com.mwl.chapter12;

/**
 * @author mawenlong
 * @date 2019-02-15 22:32
 */
public class Address implements Cloneable {
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Address{" +
               "city='" + city + '\'' +
               '}';    }
}
