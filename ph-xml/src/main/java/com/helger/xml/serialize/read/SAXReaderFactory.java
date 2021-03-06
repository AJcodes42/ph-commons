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
package com.helger.xml.serialize.read;

import javax.annotation.Nonnull;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

import com.helger.commons.exception.InitializationException;
import com.helger.commons.functional.ISupplier;
import com.helger.commons.system.SystemProperties;

public final class SAXReaderFactory implements ISupplier <org.xml.sax.XMLReader>
{
  @Nonnull
  public org.xml.sax.XMLReader get ()
  {
    return createXMLReader ();
  }

  @Nonnull
  public static org.xml.sax.XMLReader createXMLReader ()
  {
    try
    {
      org.xml.sax.XMLReader ret;
      if (true)
      {
        ret = XMLReaderFactory.createXMLReader ();
        // Because of a performance flaw in this implementation, explicitly set
        // the system property to avoid scanning JAR files over and over again
        SystemProperties.setPropertyValue ("org.xml.sax.driver", ret.getClass ().getName ());
      }
      else
      {
        // This fails with Xerces on the classpath
        ret = SAXParserFactory.newInstance ().newSAXParser ().getXMLReader ();
      }
      return ret;
    }
    catch (final ParserConfigurationException ex)
    {
      throw new InitializationException ("Failed to instantiate XML reader!", ex);
    }
    catch (final SAXException ex)
    {
      throw new InitializationException ("Failed to instantiate XML reader!", ex);
    }
  }
}
