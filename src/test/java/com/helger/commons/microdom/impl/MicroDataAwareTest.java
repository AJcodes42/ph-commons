/**
 * Copyright (C) 2006-2014 phloc systems (www.phloc.com)
 * Copyright (C) 2014 Philip Helger (www.helger.com)
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
package com.helger.commons.microdom.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.helger.commons.mock.PHTestUtils;

/**
 * Test class for class {@link MicroDataAware}.
 * 
 * @author Philip Helger
 */
public final class MicroDataAwareTest
{
  @Test
  public void testCreation ()
  {
    final MicroDataAware md = new MicroDataAware (null);
    assertEquals ("", md.getData ().toString ());
    PHTestUtils.testDefaultImplementationWithEqualContentObject (md, new MicroDataAware (null));
    PHTestUtils.testDefaultImplementationWithDifferentContentObject (md, new MicroDataAware ("foo"));
    md.appendData ("fo");
    assertEquals ("fo", md.getData ().toString ());
    md.appendData ("obar");
    assertEquals ("foobar", md.getData ().toString ());
  }
}
