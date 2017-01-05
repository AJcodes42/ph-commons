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
package com.helger.commons.io.provider;

import java.io.Writer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.io.EAppend;

/**
 * A callback interface to retrieve {@link Writer} objects from a given name.
 *
 * @author Philip Helger
 */
@FunctionalInterface
public interface IWriterProvider
{
  /**
   * Get the writer from the given name.
   *
   * @param sName
   *        The name to be resolved. May not be <code>null</code>.
   * @param eAppend
   *        Appending mode. May not be <code>null</code>.
   * @return <code>null</code> if resolving failed.
   */
  @Nullable
  Writer getWriter (@Nonnull String sName, @Nonnull EAppend eAppend);
}
