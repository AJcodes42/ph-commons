package com.helger.commons.lang.priviledged;

import java.lang.reflect.AccessibleObject;
import java.security.PrivilegedAction;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;

@FunctionalInterface
public interface IPrivilegedAction <T> extends PrivilegedAction <T>
{
  @Nullable
  default T invokeSafe ()
  {
    if (System.getSecurityManager () == null)
    {
      // No need to use AccessController
      return run ();
    }
    return AccessControllerHelper.call (this);
  }

  @Nonnull
  static IPrivilegedAction <Object> setAccessible (@Nonnull final AccessibleObject aObject)
  {
    return setAccessible (aObject, true);
  }

  @Nonnull
  static IPrivilegedAction <Object> setAccessible (@Nonnull final AccessibleObject aObject, final boolean bAccessible)
  {
    // A value of true indicates that the reflected object should suppress
    // Java language access checking when it is used.
    return () -> {
      aObject.setAccessible (bAccessible);
      return null;
    };
  }

  @Nonnull
  static IPrivilegedAction <ClassLoader> classLoaderGetParent (@Nonnull final ClassLoader aBaseClassLoader)
  {
    return () -> aBaseClassLoader.getParent ();
  }

  @Nonnull
  static IPrivilegedAction <ClassLoader> getClassLoader (@Nonnull final Class <?> aClass)
  {
    return () -> aClass.getClassLoader ();
  }

  @Nonnull
  static IPrivilegedAction <ClassLoader> getContextClassLoader ()
  {
    return () -> Thread.currentThread ().getContextClassLoader ();
  }

  @Nonnull
  static IPrivilegedAction <Object> setContextClassLoader (@Nonnull final ClassLoader aClassLoader)
  {
    ValueEnforcer.notNull (aClassLoader, "ClassLoader");
    return () -> {
      Thread.currentThread ().setContextClassLoader (aClassLoader);
      return null;
    };
  }

  @Nonnull
  static IPrivilegedAction <ClassLoader> getSystemClassLoader ()
  {
    return () -> ClassLoader.getSystemClassLoader ();
  }

  @Nonnull
  static IPrivilegedAction <String> systemClearProperty (@Nonnull final String sKey)
  {
    ValueEnforcer.notNull (sKey, "Key");
    return () -> System.clearProperty (sKey);
  }

  @Nonnull
  static IPrivilegedAction <String> systemGetProperty (@Nonnull final String sKey)
  {
    ValueEnforcer.notNull (sKey, "Key");
    return () -> System.getProperty (sKey);
  }

  @Nonnull
  static IPrivilegedAction <Properties> systemGetProperties ()
  {
    return () -> System.getProperties ();
  }

  @Nonnull
  static IPrivilegedAction <String> systemSetProperty (@Nonnull final String sKey, @Nonnull final String sValue)
  {
    ValueEnforcer.notNull (sKey, "Key");
    ValueEnforcer.notNull (sValue, "Value");
    return () -> System.setProperty (sKey, sValue);
  }
}
