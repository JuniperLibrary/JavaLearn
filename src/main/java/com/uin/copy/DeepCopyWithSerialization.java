package com.uin.copy;

import java.io.*;
import org.apache.commons.lang3.*;

public class DeepCopyWithSerialization {
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    Address2 address = new Address2("Shanghai");
    Person2 p1 = new Person2("Bob", address);

    // 深拷贝
    Person2 p2 = p1.deepCopy();

    System.out.println(p1 == p2); // false
    System.out.println(p1.address == p2.address); // false
  }
}

class Address2 implements Serializable {
  String city;

  public Address2(String city) {
    this.city = city;
  }
}

class Person2 implements Serializable {
  String name;
  Address2 address;

  public Person2(String name, Address2 address) {
    this.name = name;
    this.address = address;
  }

  // 深拷贝方法
  public Person2 deepCopy() throws IOException, ClassNotFoundException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(this);

    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bis);
    return (Person2) ois.readObject();
//    return  SerializationUtils.clone(new Person2("12",new Address2("254")));
  }
}



