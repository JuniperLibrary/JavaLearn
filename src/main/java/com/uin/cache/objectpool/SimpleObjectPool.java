package com.uin.cache.objectpool;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简单的池化技术实践
 *
 * @author dingchuan
 */
public class SimpleObjectPool<T> {

  private List<T> pool;

  public SimpleObjectPool(int size) {
    pool = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      T obj = (T) new PowerBank();
      pool.add(obj);
    }
  }

  public synchronized T getPowerBank() {
    while (pool.isEmpty()) {
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        // 中端线程
        Thread.currentThread().interrupt();
      }
    }
    return pool.remove(0);
  }

  public synchronized void retutnPowerBank(String name) {
    PowerBank powerBank = new PowerBank();
    powerBank.setName(name);
    pool.add((T) powerBank);
  }

}

/**
 * 共享充电宝
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class PowerBank {

  private String name;
}
/**
 * 池化技术的实现要素
 * <p>
 * 1、创建一个池对象：首先，您需要创建一个池对象，用于存储要管理的对象。这个池可以是一个集合，如List或Queue，用于存储对象。
 * <p>
 * 2、初始化池：在创建池后，您需要初始化池，即在池中创建一些对象并将它们添加到池中。这些对象可以是您要池化的对象的实例。
 * <p>
 * 3、获取对象：当需要使用对象时，从池中获取对象。如果池中有可用对象，就将其中一个分配给您的代码。如果池中没有可用对象，可以考虑等待或根据需要创建新的对象。
 * <p>
 * 4、使用对象：使用从池中获取的对象进行您的操作。
 * <p>
 * 5、归还对象：完成对象的使用后，将对象返回到池中，以便其他部分的代码可以重复使用它。
 * <p>
 * 6、可选：实现对象池的一些额外功能，如超时处理、对象的清理和维护。
 */
