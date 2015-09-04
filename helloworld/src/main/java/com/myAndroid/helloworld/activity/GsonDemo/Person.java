package com.myAndroid.helloworld.activity.GsonDemo;

import java.util.List;

/**
 * Created by Norven on 2015/5/7 0007.
 */
public class Person {
    public static enum TestEnum {
        DEFAULT_ENUM(99),
        TestA(0),
        TestB(1),
        TestC(2);
        private int state;

        TestEnum(int state) {
            this.state = state;
        }

        public int getCode() {
            return state;
        }

        public static TestEnum buildEnumFromCode(int code) {
            TestEnum returnEnum = DEFAULT_ENUM;
            for (TestEnum item : TestEnum.values()) {
                if (item.state == code) {
                    returnEnum = item;

                    break;
                }
            }

            return returnEnum;
        }
    }

    public static class Address {
        private String city;
        private String street;

        @Override
        public String toString() {
            return "Address{" +
                    "city='" + city + '\'' +
                    ", street='" + street + '\'' +
                    '}';
        }
    }

    public static class Lover {
        private String name;
        private String age;

        @Override
        public String toString() {
            return "Lover{" +
                    "name='" + name + '\'' +
                    ", age='" + age + '\'' +
                    '}';
        }
    }

    private String name;
    private int age;
    private List<Lover> lovers;
    private Address address;
    private TestEnum state;
    private boolean isHero;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", lovers=" + lovers +
                ", address=" + address +
                ", state=" + state +
                ", isHero=" + isHero +
                '}';
    }
}
