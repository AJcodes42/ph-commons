/**
 * Copyright (C) 2014-2015 Philip Helger (www.helger.com)
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
package com.helger.commons.format;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;

/**
 * This class represents a single object with an additional formatter.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class FormatableObject implements IFormatableObject
{
  /** The current value. Maybe <code>null</code>. */
  private final Object m_aValue;

  /** The optional formatter to use. */
  private final IFormatter m_aFormatter;

  /**
   * Init the field with a value.
   *
   * @param aValue
   *        The value to be used. May be <code>null</code>.
   * @param aFormatter
   *        The optional formatter to use. May be <code>null</code>.
   */
  public FormatableObject (@Nullable final Object aValue, @Nullable final IFormatter aFormatter)
  {
    m_aValue = aValue;
    m_aFormatter = aFormatter;
  }

  /**
   * Init the field with a value.
   *
   * @param aValue
   *        The value to be used. May be <code>null</code>.
   * @param aFormatterProvider
   *        The formatter provider to use. May not be <code>null</code>.
   */
  public FormatableObject (@Nullable final Object aValue, @Nonnull final IFormatterProvider aFormatterProvider)
  {
    ValueEnforcer.notNull (aFormatterProvider, "FormatterProvider");
    m_aValue = aValue;
    m_aFormatter = aFormatterProvider.getFormatter ();
  }

  @Nullable
  public Object getValue ()
  {
    return m_aValue;
  }

  @Nullable
  public IFormatter getFormatter ()
  {
    return m_aFormatter;
  }

  @Nullable
  public String getAsString ()
  {
    return m_aFormatter != null ? m_aFormatter.getFormattedValue (m_aValue) : String.valueOf (m_aValue);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final FormatableObject rhs = (FormatableObject) o;
    return EqualsHelper.equals (m_aValue, rhs.m_aValue) && EqualsHelper.equals (m_aFormatter, rhs.m_aFormatter);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aValue).append (m_aFormatter).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", m_aValue)
                                       .appendIfNotNull ("formatter", m_aFormatter)
                                       .toString ();
  }
}