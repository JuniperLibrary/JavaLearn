package com.uin.spi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AdaptiveInvocationHandler<T> implements InvocationHandler {

  private final Class<T> clazz;

  public AdaptiveInvocationHandler(Class<T> tClass) {
    clazz = tClass;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (args.length == 0) {
      return method.invoke(proxy, args);
    }
    // 找 URL 参数
    URL url = null;
    for (Object arg : args) {
      if (arg instanceof URL) {
        url = (URL) arg;
        break;
      }
    }
    if (url == null) {
      return method.invoke(proxy, args);
    }

    Adaptive adaptive = method.getAnnotation(Adaptive.class);
    if (adaptive == null) {
      return method.invoke(proxy, args);
    }

    String extendNameKey = adaptive.value();
    String extendName;
    if (URLKeyConst.PROTOCOL.equals(extendNameKey)) {
      extendName = url.getProtocol();
    } else {
      extendName = url.getParam(extendNameKey, method.getDeclaringClass() + "." + method.getName());
    }

    ExtensionLoader<T> extensionLoader = ExtensionLoader.getLoader(clazz);
    T extension = extensionLoader.getExtension(extendName);
    return method.invoke(extension, args);
  }
}