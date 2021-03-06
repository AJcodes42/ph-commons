/**
 * Copyright (C) 2014-2017 Philip Helger (www.helger.com)
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
package com.helger.commons.collection.attr;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.state.EChange;

/**
 * Base class for all kind of string-string mapping container. This
 * implementation is not thread-safe!
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class StringMap extends AttributeContainer <String, String> implements IStringMap
{
  public StringMap ()
  {
    super ();
  }

  public StringMap (@Nonnull final Map <String, String> aMap)
  {
    super (aMap);
  }

  public StringMap (@Nonnull final IStringMap aCont)
  {
    super (aCont);
  }

  @Nonnull
  public EChange putIn (@Nonnull final String sName, final boolean bValue)
  {
    return putIn (sName, Boolean.toString (bValue));
  }

  @Nonnull
  public EChange putIn (@Nonnull final String sName, final int nValue)
  {
    return putIn (sName, Integer.toString (nValue));
  }

  @Nonnull
  public EChange putIn (@Nonnull final String sName, final long nValue)
  {
    return putIn (sName, Long.toString (nValue));
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public StringMap getClone ()
  {
    return new StringMap (this);
  }
}
