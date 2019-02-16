package com.mwl.chapter12;

/**
 * @author mawenlong
 * @date 2019-02-15 23:07
 */
public class CloneTest {
    public static void main(String[] args) throws CloneNotSupportedException {
        testClone();
    }

    static void testClone() throws CloneNotSupportedException {
        UserInfo userInfo = new UserInfo();
        Address address = new Address();
        address.setCity("天津");
        userInfo.setAddress(address);
        userInfo.setAge(10);

        System.out.println("old value:" + userInfo);
        UserInfo userClone = (UserInfo) userInfo.clone();
        userClone.setAge(20);
        userClone.getAddress().setCity("北京");
        System.out.println("new value:" + userInfo);

    }
}
