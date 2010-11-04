/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.proxy.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collection;

import org.apache.aries.proxy.ProxyManager;
import org.osgi.framework.Bundle;

public final class JdkProxyManager extends AbstractProxyManager implements ProxyManager
{
  public Object createNewProxy(Bundle clientBundle, Collection<Class<?>> classes, InvocationHandler handler) 
  {
    return Proxy.newProxyInstance(getClassLoader(clientBundle, classes), getInterfaces(classes), handler);
  }

  private static final Class<?>[] getInterfaces(Collection<Class<?>> classes)
  {
    for (Class<?> clazz : classes) {
        if (!!!clazz.isInterface()) {
          throw new IllegalArgumentException("ARGH " + clazz + " is not an interface and I can't deal with that");
        } 
    }
    return (Class[]) classes.toArray(new Class[classes.size()]);
  }

  @Override
  protected boolean isProxyClass(Class<?> clazz) 
  {
    return Proxy.isProxyClass(clazz);
  }

  @Override
  protected InvocationHandler getInvocationHandler(Object proxy) 
  {
    Class<?> clazz = proxy.getClass();
    if (isProxyClass(clazz)) {
      return Proxy.getInvocationHandler(proxy);
    }
    return null;
  }
}