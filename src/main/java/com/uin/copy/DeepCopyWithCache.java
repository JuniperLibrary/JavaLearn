package com.uin.copy;

import cn.hutool.json.*;
import java.util.*;
import lombok.extern.slf4j.*;

class Node {

  String value;
  Node next;

  public Node(String value, Node next) {
    this.value = value;
    this.next = next;
  }

  // 深拷贝（解决循环引用）
  public Node deepCopy(Map<Node, Node> visited) {
    if (visited.containsKey(this)) {
      return visited.get(this);
    }

    Node copy = new Node(this.value, null);
    visited.put(this, copy);

    if (this.next != null) {
      copy.next = this.next.deepCopy(visited);
    }

    return copy;
  }
}

@Slf4j
public class DeepCopyWithCache {

  public static void main(String[] args) {
    Node node1 = new Node("Node1", null);
    Node node2 = new Node("Node2", node1);
    node1.next = node2; // 创建循环引用

    // 深拷贝
    Node copy = node1.deepCopy(new HashMap<>());
    log.info("copy:{}", JSONUtil.toJsonStr(copy));
    System.out.println("Deep copy succeeded!");
  }
}
