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
package com.helger.commons.callback.exception;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.string.ToStringGenerator;

/**
 * A specific implementation of the {@link IExceptionCallback} interface, that
 * stores all exceptions.
 *
 * @author Philip Helger
 * @param <EXTYPE>
 *        The exception type to be handled
 */
public class CollectingExceptionListCallback <EXTYPE extends Throwable> implements IExceptionCallback <EXTYPE>
{
  private final ICommonsList <EXTYPE> m_aExceptions = new CommonsArrayList <> ();

  public void onException (@Nullable final EXTYPE aEx)
  {
    m_aExceptions.addIfNotNull (aEx);
  }

  public boolean hasException ()
  {
    return m_aExceptions.isNotEmpty ();
  }

  @Nonnegative
  public int getExceptionCount ()
  {
    return m_aExceptions.size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <EXTYPE> getAllExceptions ()
  {
    return m_aExceptions.getClone ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("exceptions", m_aExceptions).toString ();
  }
}
