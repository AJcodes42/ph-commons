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
package com.helger.commons.text;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;

/**
 * An implementation of the {@link com.helger.commons.text.IHasText} interface
 * that always returns a constant string. Use this only for texts that never
 * need to be translated!
 *
 * @author Philip Helger
 */
@Immutable
public class ConstantHasText extends AbstractHasText
{
  private final String m_sFixedText;

  public ConstantHasText (@Nonnull final String sFixedText)
  {
    m_sFixedText = ValueEnforcer.notNull (sFixedText, "FixedText");
  }

  @Override
  protected Locale internalGetLocaleToUseWithFallback (@Nonnull final Locale aContentLocale)
  {
    // No fallback handling needed
    return aContentLocale;
  }

  @Override
  @Nullable
  protected String internalGetText (@Nonnull final Locale aContentLocale)
  {
    return m_sFixedText;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final ConstantHasText rhs = (ConstantHasText) o;
    return EqualsHelper.equals (m_sFixedText, rhs.m_sFixedText);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sFixedText).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("FixedText", m_sFixedText).toString ();
  }
}