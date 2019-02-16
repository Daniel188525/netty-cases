package com.mwl.chapter12;

/**
 * @author mawenlong
 * @date 2019-02-15 23:01
 */
public class UserInfoV2 implements Cloneable {
    private int age;
    private Address address;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        UserInfoV2 cloneInfo = (UserInfoV2) super.clone();
        cloneInfo.setAddress((Address) address.clone());
        return cloneInfo;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
               "age=" + age +
               ", address=" + address +
               '}';
    }
}
