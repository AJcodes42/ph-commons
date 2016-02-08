/**
 * Copyright (C) 2014-2016 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.jaxb.builder;

import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.namespace.NamespaceContext;

import com.helger.commons.concurrent.SimpleReadWriteLock;

/**
 * A class containing some default settings for the various JAXB builders.
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class JAXBBuilderDefaultSettings
{
  private static final SimpleReadWriteLock s_aRWLock = new SimpleReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static boolean s_bUseContextCache = true;
  @GuardedBy ("s_aRWLock")
  private static ValidationEventHandler s_aEventHandler;
  @GuardedBy ("s_aRWLock")
  private static NamespaceContext s_aNamespaceContext;
  @GuardedBy ("s_aRWLock")
  private static boolean s_bFormattedOutput;

  private JAXBBuilderDefaultSettings ()
  {}

  /**
   * Enable or disable the usage of the JAXBContext cache. For performance
   * reasons it is recommended to enable it. By default it is enabled.
   *
   * @param bUseContextCache
   *        <code>true</code> to enable it, <code>false</code> to disable it.
   */
  public static void setDefaultUseContextCache (final boolean bUseContextCache)
  {
    s_aRWLock.writeLocked ( () -> s_bUseContextCache = bUseContextCache);
  }

  /**
   * @return <code>true</code> if the JAXBContext cache should be used. Default
   *         is <code>true</code>.
   */
  public static boolean isDefaultUseContextCache ()
  {
    return s_aRWLock.readLocked ( () -> s_bUseContextCache);
  }

  /**
   * Set a global event handler that should be passed to all read/write actions.
   * If no global validation handler is defined, a default logging event handler
   * is used.
   *
   * @param aEventHandler
   *        The new default event handler. May be <code>null</code> to indicate,
   *        that the default handler should be used.
   */
  public static void setDefaultValidationEventHandler (@Nullable final ValidationEventHandler aEventHandler)
  {
    s_aRWLock.writeLocked ( () -> s_aEventHandler = aEventHandler);
  }

  /**
   * @return The current default validation event handler. May be
   *         <code>null</code> to indicate that no global validation event
   *         handler is defined, and the default validation handler is used.
   */
  @Nullable
  public static ValidationEventHandler getDefaultValidationEventHandler ()
  {
    return s_aRWLock.readLocked ( () -> s_aEventHandler);
  }

  /**
   * Set the default namespace context (prefix to namespace URL mapping) to be
   * used.
   *
   * @param aNamespaceContext
   *        The namespace context to be used by default. May be
   *        <code>null</code>.
   */
  public static void setDefaultNamespaceContext (@Nullable final NamespaceContext aNamespaceContext)
  {
    s_aRWLock.writeLocked ( () -> s_aNamespaceContext = aNamespaceContext);
  }

  /**
   * @return The special JAXB namespace context to be used. <code>null</code> by
   *         default.
   */
  @Nullable
  public static NamespaceContext getDefaultNamespaceContext ()
  {
    return s_aRWLock.readLocked ( () -> s_aNamespaceContext);
  }

  /**
   * Enable or disable the formatting of the output.
   *
   * @param bFormattedOutput
   *        <code>true</code> to enable it, <code>false</code> to disable it.
   */
  public static void setDefaultFormattedOutput (final boolean bFormattedOutput)
  {
    s_aRWLock.writeLocked ( () -> s_bFormattedOutput = bFormattedOutput);
  }

  /**
   * @return <code>true</code> if the JAXB output should be formatted. Only for
   *         writers. Default is <code>false</code>.
   */
  public static boolean isDefaultFormattedOutput ()
  {
    return s_aRWLock.readLocked ( () -> s_bFormattedOutput);
  }
}
