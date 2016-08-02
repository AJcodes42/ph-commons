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
package com.helger.datetime.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Locale;

import org.junit.Test;

/**
 * Test class for class {@link EDayOfWeek}.
 *
 * @author Philip Helger
 */
public final class EDayOfWeekTest
{
  @Test
  public void testBasic ()
  {
    for (final EDayOfWeek e : EDayOfWeek.values ())
    {
      assertSame (e, EDayOfWeek.getFromIDOrNull (e.getID ()));
      assertNotNull (e.getWeekdayName (Locale.GERMANY));
      assertNotNull (e.getWeekdayShortName (Locale.GERMANY));
    }
  }
}
