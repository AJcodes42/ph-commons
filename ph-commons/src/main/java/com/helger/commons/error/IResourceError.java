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
package com.helger.commons.error;

import javax.annotation.Nonnull;

import com.helger.commons.error.location.ErrorLocation;
import com.helger.commons.error.location.IErrorLocation;
import com.helger.commons.text.display.IHasDisplayText;

/**
 * Interface for an error that can be drilled down to a certain resource (e.g. a
 * document). The name is a bit misleading, as an "IResourceError" can also
 * contain an INFO or a WARNING message! It has an error level, a multilingual
 * error message, a location and a linked exception.
 *
 * @author Philip Helger
 */
@Deprecated
public interface IResourceError extends IHasDisplayText, IError
{
  /**
   * @return The non-<code>null</code> location of the error. Use
   *         {@link ErrorLocation#NO_LOCATION} to indicate no location is
   *         available.
   * @see #hasErrorLocation()
   */
  @Nonnull
  default IErrorLocation getLocation ()
  {
    return getErrorLocation ();
  }
}
