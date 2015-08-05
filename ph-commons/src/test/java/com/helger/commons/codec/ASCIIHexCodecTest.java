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
package com.helger.commons.codec;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.helger.commons.charset.CCharset;
import com.helger.commons.charset.CharsetManager;
import com.helger.commons.codec.ASCIIHexCodec;

/**
 * Test class for class {@link ASCIIHexCodec}
 * 
 * @author Philip Helger
 */
public final class ASCIIHexCodecTest
{
  @Test
  public void testDecode ()
  {
    final String sEncoded = "616263\n" + "414243>";
    final byte [] aDecoded = new ASCIIHexCodec ().getDecoded (CharsetManager.getAsBytes (sEncoded,
                                                                                         CCharset.CHARSET_US_ASCII_OBJ));
    final String sDecoded = CharsetManager.getAsString (aDecoded, CCharset.CHARSET_US_ASCII_OBJ);
    assertEquals ("abcABC", sDecoded);
  }
}