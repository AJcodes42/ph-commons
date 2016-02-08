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
package com.helger.jaxb.builder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.OverrideOnDemand;
import com.helger.commons.string.ToStringGenerator;
import com.helger.commons.xml.EXMLParserFeature;
import com.helger.commons.xml.XMLHelper;
import com.helger.commons.xml.serialize.read.SAXReaderFactory;
import com.helger.commons.xml.serialize.read.SAXReaderSettings;
import com.helger.jaxb.IJAXBReader;
import com.helger.jaxb.JAXBContextCache;
import com.helger.jaxb.validation.LoggingValidationEventHandler;

/**
 * Abstract builder class for reading JAXB documents.
 *
 * @author Philip Helger
 * @param <JAXBTYPE>
 *        The JAXB implementation class to be read
 * @param <IMPLTYPE>
 *        The implementation class implementing this abstract class.
 */
@NotThreadSafe
public abstract class AbstractJAXBReaderBuilder <JAXBTYPE, IMPLTYPE extends AbstractJAXBReaderBuilder <JAXBTYPE, IMPLTYPE>>
                                                extends AbstractJAXBBuilder <IMPLTYPE> implements IJAXBReader <JAXBTYPE>
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractJAXBReaderBuilder.class);

  protected Class <JAXBTYPE> m_aImplClass;
  protected ValidationEventHandler m_aEventHandler = JAXBBuilderDefaultSettings.getDefaultValidationEventHandler ();

  public AbstractJAXBReaderBuilder (@Nonnull final IJAXBDocumentType aDocType,
                                    @Nonnull final Class <JAXBTYPE> aImplClass)
  {
    super (aDocType);
    m_aImplClass = ValueEnforcer.notNull (aImplClass, "ImplClass");
  }

  /**
   * @return The special JAXB validation event handler to be used. By default
   *         {@link JAXBBuilderDefaultSettings#getDefaultValidationEventHandler()}
   *         is used.
   */
  @Nullable
  public ValidationEventHandler getValidationEventHandler ()
  {
    return m_aEventHandler;
  }

  /**
   * Set the JAXB validation event handler to be used. May be <code>null</code>.
   *
   * @param aEventHandler
   *        The event handler to be used. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public IMPLTYPE setValidationEventHandler (@Nullable final ValidationEventHandler aEventHandler)
  {
    m_aEventHandler = aEventHandler;
    return thisAsT ();
  }

  public final boolean isReadSecure ()
  {
    return true;
  }

  @Nullable
  public JAXBTYPE read (@Nonnull final SAXReaderSettings aSettings, @Nonnull final InputSource aInputSource)
  {
    ValueEnforcer.notNull (aSettings, "Settings");
    ValueEnforcer.notNull (aInputSource, "InputSource");

    // Create new XML reader
    final XMLReader aParser = SAXReaderFactory.createXMLReader ();

    // Apply settings
    aSettings.applyToSAXReader (aParser);

    return read (new SAXSource (aParser, aInputSource));
  }

  @Nonnull
  @OverrideOnDemand
  protected SAXReaderSettings createDefaultSAXReaderSettings ()
  {
    // Initialize settings with defaults
    final SAXReaderSettings aSettings = new SAXReaderSettings ();
    // Apply settings that make reading more secure
    aSettings.setFeatureValues (EXMLParserFeature.AVOID_XML_ATTACKS);
    return aSettings;
  }

  @Nullable
  public final JAXBTYPE read (@Nonnull final InputSource aInputSource)
  {
    // Initialize settings with defaults
    final SAXReaderSettings aSettings = createDefaultSAXReaderSettings ();

    return read (aSettings, aInputSource);
  }

  @Nonnull
  protected Unmarshaller createUnmarshaller () throws JAXBException
  {
    // Since creating the JAXB context is quite cost intensive this is done
    // only once!
    final JAXBContext aJAXBContext = JAXBContextCache.getInstance ().getFromCache (m_aDocType.getImplementationClass (),
                                                                                   m_aClassLoader);

    // create an Unmarshaller
    final Unmarshaller aUnmarshaller = aJAXBContext.createUnmarshaller ();
    if (m_aEventHandler != null)
      aUnmarshaller.setEventHandler (m_aEventHandler);
    else
      aUnmarshaller.setEventHandler (new LoggingValidationEventHandler (aUnmarshaller.getEventHandler ()));

    // Validating!
    aUnmarshaller.setSchema (m_aDocType.getSchema (m_aClassLoader));

    return aUnmarshaller;
  }

  /**
   * Customize the unmarshaller
   *
   * @param aUnmarshaller
   *        The unmarshaller to customize. Never <code>null</code>.
   */
  @OverrideOnDemand
  protected void customizeUnmarshaller (@Nonnull final Unmarshaller aUnmarshaller)
  {}

  @OverrideOnDemand
  protected void handleReadException (@Nonnull final JAXBException ex)
  {
    if (ex instanceof UnmarshalException)
    {
      // The JAXB specification does not mandate how the JAXB provider
      // must behave when attempting to unmarshal invalid XML data. In
      // those cases, the JAXB provider is allowed to terminate the
      // call to unmarshal with an UnmarshalException.
      final Throwable aLinked = ((UnmarshalException) ex).getLinkedException ();
      if (aLinked instanceof SAXParseException)
        s_aLogger.error ("Failed to parse XML document: " + aLinked.getMessage ());
      else
        s_aLogger.error ("Unmarshal exception reading document", ex);
    }
    else
      s_aLogger.warn ("JAXB Exception reading document", ex);
  }

  /**
   * Convert the passed XML node into a domain object.<br>
   * Note: this is the generic API for reading all types of JAXB documents.
   *
   * @param aNode
   *        The XML node to be converted. May not be <code>null</code>.
   * @return <code>null</code> in case conversion to the specified class failed.
   *         See the log output for details.
   */
  @Nullable
  public JAXBTYPE read (@Nonnull final Node aNode)
  {
    ValueEnforcer.notNull (aNode, "Node");

    final String sNodeNamespaceURI = XMLHelper.getNamespaceURI (aNode);

    // Avoid class cast exception later on
    if (!m_aDocType.getNamespaceURI ().equals (sNodeNamespaceURI))
    {
      s_aLogger.error ("You cannot read an '" + sNodeNamespaceURI + "' as a " + m_aImplClass.getName ());
      return null;
    }

    JAXBTYPE ret = null;
    try
    {
      final Unmarshaller aUnmarshaller = createUnmarshaller ();

      // Customize on demand
      customizeUnmarshaller (aUnmarshaller);

      // start unmarshalling
      ret = aUnmarshaller.unmarshal (aNode, m_aImplClass).getValue ();
      if (ret == null)
        throw new IllegalStateException ("Failed to read JAXB document of class " +
                                         m_aImplClass.getName () +
                                         " - without exception!");
    }
    catch (final JAXBException ex)
    {
      handleReadException (ex);
    }

    return ret;
  }

  /**
   * Interpret the passed {@link Source} as a JAXB document.<br>
   * Note: this is the generic API for reading all types of JAXB documents.
   *
   * @param aSource
   *        The source to read from. May not be <code>null</code>.
   * @return The evaluated JAXB document or <code>null</code> in case of a
   *         parsing error
   */
  @Nullable
  public JAXBTYPE read (@Nonnull final Source aSource)
  {
    ValueEnforcer.notNull (aSource, "Source");

    // as we don't have a node, we need to trust the implementation class
    final Schema aSchema = m_aDocType.getSchema (m_aClassLoader);
    if (aSchema == null)
    {
      s_aLogger.error ("Don't know how to read JAXB document of type " + m_aImplClass.getName ());
      return null;
    }

    JAXBTYPE ret = null;
    try
    {
      final Unmarshaller aUnmarshaller = createUnmarshaller ();

      // Customize on demand
      customizeUnmarshaller (aUnmarshaller);

      // start unmarshalling
      ret = aUnmarshaller.unmarshal (aSource, m_aImplClass).getValue ();
      if (ret == null)
        throw new IllegalStateException ("Failed to read JAXB document of class " +
                                         m_aImplClass.getName () +
                                         " - without exception!");
    }
    catch (final JAXBException ex)
    {
      handleReadException (ex);
    }

    return ret;
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("ImplClass", m_aImplClass)
                            .append ("EventHandler", m_aEventHandler)
                            .toString ();
  }
}
