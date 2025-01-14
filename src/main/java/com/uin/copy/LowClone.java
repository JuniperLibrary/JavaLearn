package com.uin.copy;

import lombok.extern.slf4j.*;

@Slf4j
public class LowClone {

  public static void main(String[] args) throws CloneNotSupportedException {
    Address address = new Address("Beijing");
    Person p1 = new Person("Alice", address);

    // 浅拷贝
    Person p2 = (Person) p1.clone();

    log.info("p1 == p2 ： {}", p1 == p2); // false，p1 和 p2 是不同对象
    log.info("p1.address == p2.address : {}", p1.address == p2.address); // true，共享同一个 Address 实例
  }
}


class Address {

  String city;

  public Address(String city) {
    this.city = city;
  }
}

class Person implements Cloneable {

  String name;
  Address address;

  public Person(String name, Address address) {
    this.name = name;
    this.address = address;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone(); // 默认实现浅拷贝
  }
}

