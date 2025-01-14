package com.uin.copy;

public class DeepCopy {
  public static void main(String[] args) {
    Address1 address = new Address1("Beijing");
    Person1 p1 = new Person1("Alice", address);

    // 深拷贝
    Person1 p2 = p1.deepCopy();

    System.out.println(p1 == p2); // false，p1 和 p2 是不同对象
    System.out.println(p1.address == p2.address); // false，p1 和 p2 的 Address 实例不同
  }
}
class Address1 {
  String city;

  public Address1(String city) {
    this.city = city;
  }

  // 深拷贝方法
  public Address1 deepCopy() {
    return new Address1(this.city);
  }
}

class Person1 implements Cloneable {
  String name;
  Address1 address;

  public Person1(String name, Address1 address) {
    this.name = name;
    this.address = address;
  }

  // 深拷贝方法
  public Person1 deepCopy() {
    return new Person1(this.name, this.address.deepCopy());
  }
}