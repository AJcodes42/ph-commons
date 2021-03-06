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
package com.helger.xml.microdom;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.collection.impl.ICommonsOrderedMap;
import com.helger.commons.collection.impl.ICommonsOrderedSet;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.functional.IPredicate;
import com.helger.commons.functional.ITriConsumer;
import com.helger.commons.mutable.MutableInt;
import com.helger.commons.state.EChange;
import com.helger.commons.state.EContinue;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.StringParser;
import com.helger.commons.typeconvert.TypeConverter;

/**
 * Represents a single element (=tag) of a document.
 *
 * @author Philip Helger
 */
public interface IMicroElement extends IMicroNodeWithChildren
{
  /**
   * @return <code>true</code> if this element has at least one attribute,
   *         <code>false</code> otherwise
   */
  boolean hasAttributes ();

  /**
   * @return <code>true</code> if this element has no attribute,
   *         <code>false</code> if at least one attribute is present.
   */
  boolean hasNoAttributes ();

  /**
   * @return The number of assigned attributes. Always &ge; 0.
   */
  @Nonnegative
  int getAttributeCount ();

  /**
   * Check if this element has an attribute with the specified name.
   *
   * @param sAttrName
   *        The attribute name to check.
   * @return <code>true</code> if such an attribute is present,
   *         <code>false</code> otherwise
   */
  default boolean hasAttribute (@Nullable final String sAttrName)
  {
    return hasAttribute (null, sAttrName);
  }

  /**
   * Check if this element has an attribute with the specified name.
   *
   * @param sNamespaceURI
   *        Namespace URI to use. May be <code>null</code>.
   * @param sAttrName
   *        The attribute name to check.
   * @return <code>true</code> if such an attribute is present,
   *         <code>false</code> otherwise
   */
  default boolean hasAttribute (@Nullable final String sNamespaceURI, @Nullable final String sAttrName)
  {
    if (StringHelper.hasNoText (sAttrName))
      return false;
    return hasAttribute (new MicroQName (sNamespaceURI, sAttrName));
  }

  /**
   * Check if this element has an attribute with the specified name.
   *
   * @param aAttrName
   *        The qualified attribute name to check. May be <code>null</code>.
   * @return <code>true</code> if such an attribute is present,
   *         <code>false</code> otherwise
   */
  boolean hasAttribute (@Nullable IMicroQName aAttrName);

  /**
   * Get the attribute object with the specified name.
   *
   * @param sAttrName
   *        The attribute locale name to query. May be <code>null</code>.
   * @return <code>null</code> of no such attribute object exists.
   */
  @Nullable
  default IMicroAttribute getAttributeObj (@Nullable final String sAttrName)
  {
    return getAttributeObj (null, sAttrName);
  }

  /**
   * Get the attribute object with the specified namespace URI and local name.
   *
   * @param sNamespaceURI
   *        Namespace URI to use. May be <code>null</code>.
   * @param sAttrName
   *        The attribute locale name to query. May be <code>null</code>.
   * @return <code>null</code> of no such attribute object exists.
   */
  @Nullable
  default IMicroAttribute getAttributeObj (@Nullable final String sNamespaceURI, @Nullable final String sAttrName)
  {
    if (StringHelper.hasNoText (sAttrName))
      return null;
    return getAttributeObj (new MicroQName (sNamespaceURI, sAttrName));
  }

  /**
   * Get the attribute object with the specified qualified name.
   *
   * @param aAttrName
   *        The qualified attribute name to check. May be <code>null</code>.
   * @return <code>null</code> of no such attribute object exists.
   */
  @Nullable
  IMicroAttribute getAttributeObj (@Nullable IMicroQName aAttrName);

  /**
   * Get the attribute value of the given attribute name. If this element has no
   * such attribute, <code>null</code> is returned.
   *
   * @param sAttrName
   *        The attribute name to retrieve the value of.
   * @return The assigned attribute value or <code>null</code>.
   */
  @Nullable
  default String getAttributeValue (@Nullable final String sAttrName)
  {
    return getAttributeValue ((String) null, sAttrName);
  }

  /**
   * Get the attribute value of the given attribute name. If this element has no
   * such attribute, <code>null</code> is returned.
   *
   * @param sNamespaceURI
   *        Namespace URI to use. May be <code>null</code>.
   * @param sAttrName
   *        The attribute name to retrieve the value of.
   * @return The assigned attribute value or <code>null</code>.
   */
  @Nullable
  default String getAttributeValue (@Nullable final String sNamespaceURI, @Nullable final String sAttrName)
  {
    return getAttributeValue (new MicroQName (sNamespaceURI, sAttrName));
  }

  /**
   * Get the attribute value of the given attribute name. If this element has no
   * such attribute, <code>null</code> is returned.
   *
   * @param aAttrName
   *        The qualified attribute name to retrieve the value of. May be
   *        <code>null</code>.
   * @return The assigned attribute value or <code>null</code>.
   */
  @Nullable
  default String getAttributeValue (@Nullable final IMicroQName aAttrName)
  {
    final IMicroAttribute aAttr = getAttributeObj (aAttrName);
    return aAttr == null ? null : aAttr.getAttributeValue ();
  }

  /**
   * Get the attribute value of the given attribute name. If this element has no
   * such attribute, <code>null</code> is returned. The attribute value is
   * converted via the {@link com.helger.commons.typeconvert.TypeConverter} to
   * the desired destination class. If no such attribute is present,
   * <code>null</code> is returned.
   *
   * @param <DSTTYPE>
   *        Destination type
   * @param sAttrName
   *        The attribute name to retrieve the value of.
   * @param aDstClass
   *        The destination class.
   * @return The assigned attribute value or <code>null</code>.
   * @throws ClassCastException
   *         if the value cannot be converted
   */
  @Nullable
  default <DSTTYPE> DSTTYPE getAttributeValueWithConversion (@Nullable final String sAttrName,
                                                             @Nonnull final Class <DSTTYPE> aDstClass)
  {
    return getAttributeValueWithConversion ((String) null, sAttrName, aDstClass);
  }

  /**
   * Get the attribute value of the given attribute name. If this element has no
   * such attribute, <code>null</code> is returned. The attribute value is
   * converted via the {@link com.helger.commons.typeconvert.TypeConverter} to
   * the desired destination class. If no such attribute is present,
   * <code>null</code> is returned.
   *
   * @param <DSTTYPE>
   *        Destination type
   * @param sNamespaceURI
   *        Namespace URI to use. May be <code>null</code>.
   * @param sAttrName
   *        The attribute name to retrieve the value of.
   * @param aDstClass
   *        The destination class.
   * @return The assigned attribute value or <code>null</code>.
   * @throws ClassCastException
   *         if the value cannot be converted
   */
  @Nullable
  default <DSTTYPE> DSTTYPE getAttributeValueWithConversion (@Nullable final String sNamespaceURI,
                                                             @Nullable final String sAttrName,
                                                             @Nonnull final Class <DSTTYPE> aDstClass)
  {
    return getAttributeValueWithConversion (new MicroQName (sNamespaceURI, sAttrName), aDstClass);
  }

  /**
   * Get the attribute value of the given attribute name. If this element has no
   * such attribute, <code>null</code> is returned. The attribute value is
   * converted via the {@link com.helger.commons.typeconvert.TypeConverter} to
   * the desired destination class. If no such attribute is present,
   * <code>null</code> is returned.
   *
   * @param <DSTTYPE>
   *        Destination type
   * @param aAttrName
   *        The attribute qualified name to retrieve the value of.
   * @param aDstClass
   *        The destination class.
   * @return The assigned attribute value or <code>null</code>.
   * @throws ClassCastException
   *         if the value cannot be converted
   */
  @Nullable
  <DSTTYPE> DSTTYPE getAttributeValueWithConversion (@Nullable IMicroQName aAttrName,
                                                     @Nonnull Class <DSTTYPE> aDstClass);

  default boolean getAttributeValueAsBool (@Nullable final String sAttrName, final boolean bDefault)
  {
    return StringParser.parseBool (getAttributeValue (sAttrName), bDefault);
  }

  default boolean getAttributeValueAsBool (@Nullable final String sNamespaceURI,
                                           @Nullable final String sAttrName,
                                           final boolean bDefault)
  {
    return StringParser.parseBool (getAttributeValue (sNamespaceURI, sAttrName), bDefault);
  }

  default boolean getAttributeValueAsBool (@Nullable final IMicroQName aAttrName, final boolean bDefault)
  {
    return StringParser.parseBool (getAttributeValue (aAttrName), bDefault);
  }

  default double getAttributeValueAsDouble (@Nullable final String sAttrName, final double dDefault)
  {
    return StringParser.parseDouble (getAttributeValue (sAttrName), dDefault);
  }

  default double getAttributeValueAsDouble (@Nullable final String sNamespaceURI,
                                            @Nullable final String sAttrName,
                                            final double dDefault)
  {
    return StringParser.parseDouble (getAttributeValue (sNamespaceURI, sAttrName), dDefault);
  }

  default double getAttributeValueAsDouble (@Nullable final IMicroQName aAttrName, final double dDefault)
  {
    return StringParser.parseDouble (getAttributeValue (aAttrName), dDefault);
  }

  default float getAttributeValueAsFloat (@Nullable final String sAttrName, final float fDefault)
  {
    return StringParser.parseFloat (getAttributeValue (sAttrName), fDefault);
  }

  default float getAttributeValueAsFloat (@Nullable final String sNamespaceURI,
                                          @Nullable final String sAttrName,
                                          final float fDefault)
  {
    return StringParser.parseFloat (getAttributeValue (sNamespaceURI, sAttrName), fDefault);
  }

  default float getAttributeValueAsFloat (@Nullable final IMicroQName aAttrName, final float fDefault)
  {
    return StringParser.parseFloat (getAttributeValue (aAttrName), fDefault);
  }

  default int getAttributeValueAsInt (@Nullable final String sAttrName, final int nDefault)
  {
    return StringParser.parseInt (getAttributeValue (sAttrName), nDefault);
  }

  default int getAttributeValueAsInt (@Nullable final String sNamespaceURI,
                                      @Nullable final String sAttrName,
                                      final int nDefault)
  {
    return StringParser.parseInt (getAttributeValue (sNamespaceURI, sAttrName), nDefault);
  }

  default int getAttributeValueAsInt (@Nullable final IMicroQName aAttrName, final int nDefault)
  {
    return StringParser.parseInt (getAttributeValue (aAttrName), nDefault);
  }

  default long getAttributeValueAsLong (@Nullable final String sAttrName, final long nDefault)
  {
    return StringParser.parseLong (getAttributeValue (sAttrName), nDefault);
  }

  default long getAttributeValueAsLong (@Nullable final String sNamespaceURI,
                                        @Nullable final String sAttrName,
                                        final long nDefault)
  {
    return StringParser.parseLong (getAttributeValue (sNamespaceURI, sAttrName), nDefault);
  }

  default long getAttributeValueAsLong (@Nullable final IMicroQName aAttrName, final long nDefault)
  {
    return StringParser.parseLong (getAttributeValue (aAttrName), nDefault);
  }

  /**
   * Get a list of all attributes. Is ensured to be not <code>null</code> if
   * {@link #hasAttributes()} returns <code>true</code>.
   *
   * @return May be <code>null</code>.
   */
  @Nullable
  @ReturnsMutableCopy
  ICommonsList <? extends IMicroAttribute> getAllAttributeObjs ();

  /**
   * Get a map of all fully qualified attribute names and values. Is ensured to
   * be not <code>null</code> if {@link #hasAttributes()} returns
   * <code>true</code>.
   *
   * @return May be <code>null</code>.
   */
  @Nullable
  @ReturnsMutableCopy
  ICommonsOrderedMap <IMicroQName, String> getAllQAttributes ();

  /**
   * Get a set of all attribute names. Is ensured to be not <code>null</code> if
   * {@link #hasAttributes()} returns <code>true</code>.
   *
   * @return May be <code>null</code>.
   */
  @Nullable
  @ReturnsMutableCopy
  ICommonsOrderedSet <IMicroQName> getAllAttributeQNames ();

  /**
   * Iterate all attribute objects.
   *
   * @param aConsumer
   *        The consumer. May not be <code>null</code>. May only perform reading
   *        operations!
   */
  void forAllAttributes (@Nonnull Consumer <? super IMicroAttribute> aConsumer);

  /**
   * Iterate all attribute objects.
   *
   * @param aConsumer
   *        The consumer that takes the QName and the value. May not be
   *        <code>null</code>. May only perform reading operations!
   */
  void forAllAttributes (@Nonnull BiConsumer <? super IMicroQName, ? super String> aConsumer);

  /**
   * Iterate all attribute objects.
   *
   * @param aConsumer
   *        The consumer that takes the namespace URI, the attribute local name
   *        and the attribute value. May not be <code>null</code>. May only
   *        perform reading operations!
   */
  void forAllAttributes (@Nonnull ITriConsumer <? super String, ? super String, ? super String> aConsumer);

  /**
   * Set an attribute value of this element.
   *
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param sAttrValue
   *        If the value is <code>null</code> the attribute is removed (if
   *        present)
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nonnull final String sAttrName, @Nullable final String sAttrValue)
  {
    return setAttribute (new MicroQName (sAttrName), sAttrValue);
  }

  /**
   * Set an attribute value of this element.
   *
   * @param sNamespaceURI
   *        Namespace URI to use. May be <code>null</code>.
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param sAttrValue
   *        If the value is <code>null</code> the attribute is removed (if
   *        present)
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nullable final String sNamespaceURI,
                                      @Nonnull final String sAttrName,
                                      @Nullable final String sAttrValue)
  {
    return setAttribute (new MicroQName (sNamespaceURI, sAttrName), sAttrValue);
  }

  /**
   * Set an attribute value of this element.
   *
   * @param aAttrName
   *        Qualified name of the attribute. May neither be <code>null</code>
   *        nor empty.
   * @param sAttrValue
   *        If the value is <code>null</code> the attribute is removed (if
   *        present)
   * @return this
   */
  @Nonnull
  IMicroElement setAttribute (@Nonnull IMicroQName aAttrName, @Nullable String sAttrValue);

  /**
   * Set an attribute value of this element.
   *
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param aAttrValueProvider
   *        The attribute value provider. May not be <code>null</code>. If the
   *        contained attribute value is <code>null</code> the attribute is
   *        removed (if present)
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nonnull final String sAttrName,
                                      @Nonnull final IHasAttributeValue aAttrValueProvider)
  {
    return setAttribute (new MicroQName (sAttrName), aAttrValueProvider);
  }

  /**
   * Set an attribute value of this element.
   *
   * @param sNamespaceURI
   *        Namespace URI to use. May be <code>null</code>.
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param aAttrValueProvider
   *        The attribute value provider. May not be <code>null</code>. If the
   *        contained attribute value is <code>null</code> the attribute is
   *        removed (if present)
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nullable final String sNamespaceURI,
                                      @Nonnull final String sAttrName,
                                      @Nonnull final IHasAttributeValue aAttrValueProvider)
  {
    return setAttribute (new MicroQName (sNamespaceURI, sAttrName), aAttrValueProvider);
  }

  /**
   * Set an attribute value of this element.
   *
   * @param aAttrName
   *        Qualified name of the attribute. May neither be <code>null</code>
   *        nor empty.
   * @param aAttrValueProvider
   *        The attribute value provider. May not be <code>null</code>. If the
   *        contained attribute value is <code>null</code> the attribute is
   *        removed (if present)
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nonnull final IMicroQName aAttrName,
                                      @Nonnull final IHasAttributeValue aAttrValueProvider)
  {
    ValueEnforcer.notNull (aAttrValueProvider, "AttrValueProvider");
    return setAttribute (aAttrName, aAttrValueProvider.getAttrValue ());
  }

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(sAttrName, Boolean.toString (nValue))</code>. That
   * means, that the serialized value of the attribute is either
   * <code>true</code> or <code>false</code>. If you need something else (like
   * "yes" or "no") don't use this method.
   *
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param bAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nonnull final String sAttrName, final boolean bAttrValue)
  {
    return setAttribute (sAttrName, Boolean.toString (bAttrValue));
  }

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(sNamespaceURI, sAttrName, Boolean.toString (nValue))</code>
   * . That means, that the serialized value of the attribute is either
   * <code>true</code> or <code>false</code>. If you need something else (like
   * "yes" or "no") don't use this method.
   *
   * @param sNamespaceURI
   *        Namespace URI to use. May be <code>null</code>.
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param bAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nullable final String sNamespaceURI,
                                      @Nonnull final String sAttrName,
                                      final boolean bAttrValue)
  {
    return setAttribute (sNamespaceURI, sAttrName, Boolean.toString (bAttrValue));
  }

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(aAttrName, Boolean.toString (nValue))</code>. That
   * means, that the serialized value of the attribute is either
   * <code>true</code> or <code>false</code>. If you need something else (like
   * "yes" or "no") don't use this method.
   *
   * @param aAttrName
   *        Qualified name of the attribute. May neither be <code>null</code>
   *        nor empty.
   * @param bAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nonnull final IMicroQName aAttrName, final boolean bAttrValue)
  {
    return setAttribute (aAttrName, Boolean.toString (bAttrValue));
  }

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(sAttrName, Double.toString (nValue))</code>.
   *
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param dAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nonnull final String sAttrName, final double dAttrValue)
  {
    return setAttribute (sAttrName, Double.toString (dAttrValue));
  }

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(sNamespaceURI, sAttrName, Double.toString (nValue))</code>
   * .
   *
   * @param sNamespaceURI
   *        Namespace URI to use. May be <code>null</code>.
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param dAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nullable final String sNamespaceURI,
                                      @Nonnull final String sAttrName,
                                      final double dAttrValue)
  {
    return setAttribute (sNamespaceURI, sAttrName, Double.toString (dAttrValue));
  }

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(aAttrName, Double.toString (nValue))</code>.
   *
   * @param aAttrName
   *        Qualified name of the attribute. May neither be <code>null</code>
   *        nor empty.
   * @param dAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nonnull final IMicroQName aAttrName, final double dAttrValue)
  {
    return setAttribute (aAttrName, Double.toString (dAttrValue));
  }

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(sAttrName, Float.toString (nValue))</code>.
   *
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param fAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nonnull final String sAttrName, final float fAttrValue)
  {
    return setAttribute (sAttrName, Float.toString (fAttrValue));
  }

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(sNamespaceURI, sAttrName, Float.toString (nValue))</code>
   * .
   *
   * @param sNamespaceURI
   *        Namespace URI to use. May be <code>null</code>.
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param fAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nullable final String sNamespaceURI,
                                      @Nonnull final String sAttrName,
                                      final float fAttrValue)
  {
    return setAttribute (sNamespaceURI, sAttrName, Float.toString (fAttrValue));
  }

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(aAttrName, Float.toString (nValue))</code>.
   *
   * @param aAttrName
   *        Qualified name of the attribute. May neither be <code>null</code>
   *        nor empty.
   * @param fAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nonnull final IMicroQName aAttrName, final float fAttrValue)
  {
    return setAttribute (aAttrName, Float.toString (fAttrValue));
  }

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(sAttrName, Integer.toString (nValue))</code>.
   *
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param nAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nonnull final String sAttrName, final int nAttrValue)
  {
    return setAttribute (sAttrName, Integer.toString (nAttrValue));
  }

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(sNamespaceURI, sAttrName, Integer.toString (nValue))</code>
   * .
   *
   * @param sNamespaceURI
   *        Namespace URI to use. May be <code>null</code>.
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param nAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nullable final String sNamespaceURI,
                                      @Nonnull final String sAttrName,
                                      final int nAttrValue)
  {
    return setAttribute (sNamespaceURI, sAttrName, Integer.toString (nAttrValue));
  }

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(aAttrName, Integer.toString (nValue))</code>.
   *
   * @param aAttrName
   *        Qualified name of the attribute. May neither be <code>null</code>
   *        nor empty.
   * @param nAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nonnull final IMicroQName aAttrName, final int nAttrValue)
  {
    return setAttribute (aAttrName, Integer.toString (nAttrValue));
  }

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(name, Long.toString (nValue))</code>.
   *
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param nAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nonnull final String sAttrName, final long nAttrValue)
  {
    return setAttribute (sAttrName, Long.toString (nAttrValue));
  }

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(name, Long.toString (nValue))</code>.
   *
   * @param sNamespaceURI
   *        Namespace URI to use. May be <code>null</code>.
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param nAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nullable final String sNamespaceURI,
                                      @Nonnull final String sAttrName,
                                      final long nAttrValue)
  {
    return setAttribute (sNamespaceURI, sAttrName, Long.toString (nAttrValue));
  }

  /**
   * Set an attribute value of this element. This is a shortcut for
   * <code>setAttribute(name, Long.toString (nValue))</code>.
   *
   * @param aAttrName
   *        Qualified name of the attribute. May neither be <code>null</code>
   *        nor empty.
   * @param nAttrValue
   *        The new value to be set.
   * @return this
   */
  @Nonnull
  default IMicroElement setAttribute (@Nonnull final IMicroQName aAttrName, final long nAttrValue)
  {
    return setAttribute (aAttrName, Long.toString (nAttrValue));
  }

  /**
   * Set an attribute value of this element. If the type of the value is not
   * {@link String}, the {@link com.helger.commons.typeconvert.TypeConverter} is
   * invoked to convert it to a {@link String} object.
   *
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param aAttrValue
   *        If the value is <code>null</code> the attribute is removed (if
   *        present)
   * @return this
   */
  @Nonnull
  default IMicroElement setAttributeWithConversion (@Nonnull final String sAttrName, @Nullable final Object aAttrValue)
  {
    return setAttributeWithConversion (new MicroQName (sAttrName), aAttrValue);
  }

  /**
   * Set an attribute value of this element. If the type of the value is not
   * {@link String}, the {@link com.helger.commons.typeconvert.TypeConverter} is
   * invoked to convert it to a {@link String} object.
   *
   * @param sNamespaceURI
   *        Namespace URI to use. May be <code>null</code>.
   * @param sAttrName
   *        Name of the attribute. May neither be <code>null</code> nor empty.
   * @param aAttrValue
   *        If the value is <code>null</code> the attribute is removed (if
   *        present)
   * @return this
   */
  @Nonnull
  default IMicroElement setAttributeWithConversion (@Nullable final String sNamespaceURI,
                                                    @Nonnull final String sAttrName,
                                                    @Nullable final Object aAttrValue)
  {
    return setAttributeWithConversion (new MicroQName (sNamespaceURI, sAttrName), aAttrValue);
  }

  /**
   * Set an attribute value of this element. If the type of the value is not
   * {@link String}, the {@link com.helger.commons.typeconvert.TypeConverter} is
   * invoked to convert it to a {@link String} object.
   *
   * @param aAttrName
   *        Qualified name of the attribute. May neither be <code>null</code>
   *        nor empty.
   * @param aAttrValue
   *        If the value is <code>null</code> the attribute is removed (if
   *        present)
   * @return this
   */
  @Nonnull
  default IMicroElement setAttributeWithConversion (@Nonnull final IMicroQName aAttrName,
                                                    @Nullable final Object aAttrValue)
  {
    final String sAttrValue = TypeConverter.convert (aAttrValue, String.class);
    return setAttribute (aAttrName, sAttrValue);
  }

  /**
   * Remove the attribute with the given name.
   *
   * @param sAttrName
   *        The name of the attribute to be removed. May be <code>null</code>.
   * @return {@link EChange#CHANGED} if the attribute was removed,
   *         {@link EChange#UNCHANGED} if no such attribute exists at this
   *         element.
   */
  @Nonnull
  default EChange removeAttribute (@Nullable final String sAttrName)
  {
    return removeAttribute (null, sAttrName);
  }

  /**
   * Remove the attribute with the given name.
   *
   * @param sNamespaceURI
   *        Namespace URI to use. May be <code>null</code>.
   * @param sAttrName
   *        The name of the attribute to be removed. May be <code>null</code>.
   * @return {@link EChange#CHANGED} if the attribute was removed,
   *         {@link EChange#UNCHANGED} if no such attribute exists at this
   *         element.
   */
  @Nonnull
  default EChange removeAttribute (@Nullable final String sNamespaceURI, @Nullable final String sAttrName)
  {
    if (StringHelper.hasNoText (sAttrName))
      return EChange.UNCHANGED;
    return removeAttribute (new MicroQName (sNamespaceURI, sAttrName));
  }

  /**
   * Remove the attribute with the given name.
   *
   * @param aAttrName
   *        The qualified name of the attribute to be removed. May be
   *        <code>null</code>.
   * @return {@link EChange#CHANGED} if the attribute was removed,
   *         {@link EChange#UNCHANGED} if no such attribute exists at this
   *         element.
   */
  @Nonnull
  EChange removeAttribute (@Nullable IMicroQName aAttrName);

  /**
   * Remove all attributes from this element
   *
   * @return {@link EChange}.
   */
  @Nonnull
  EChange removeAllAttributes ();

  // Other API

  /**
   * Get the namespace URI of this element
   *
   * @return May be <code>null</code> if this element has no namespace URI.
   */
  @Nullable
  String getNamespaceURI ();

  /**
   * Set a new namespace URI for this element.
   *
   * @param sNamespaceURI
   *        The namespace URI to be set. May be <code>null</code> or empty to
   *        indicate that the namespace should be removed.
   * @return {@link EChange}
   */
  @Nonnull
  EChange setNamespaceURI (@Nullable String sNamespaceURI);

  /**
   * Check if this element has a specified namespace URI.
   *
   * @return <code>true</code> if this element has a specified namespace URI,
   *         <code>false</code> otherwise
   */
  default boolean hasNamespaceURI ()
  {
    return getNamespaceURI () != null;
  }

  /**
   * Check if this element has no namespace URI.
   *
   * @return <code>true</code> if this element has no namespace URI,
   *         <code>false</code> otherwise
   */
  default boolean hasNoNamespaceURI ()
  {
    return getNamespaceURI () == null;
  }

  /**
   * Check if this element has the specified namespace URI.
   *
   * @param sNamespaceURI
   *        The namespace URI to check. May not be <code>null</code>.
   * @return <code>true</code> if this element has the specified namespace URI,
   *         <code>false</code> otherwise
   */
  default boolean hasNamespaceURI (@Nullable final String sNamespaceURI)
  {
    return EqualsHelper.equals (getNamespaceURI (), sNamespaceURI);
  }

  /**
   * Get the local name of the element. Is the same name as returned by
   * {@link #getTagName()} but it is only present, if a namespace URI is
   * present.
   *
   * @return May be <code>null</code> if no namespace is present.
   */
  @Nullable
  String getLocalName ();

  /**
   * Check if this element has the provided local name. The local name is the
   * same name as returned by {@link #getTagName()} but is only present if a
   * namespace URI is present.
   *
   * @param sLocalName
   *        The local name to compare against. May be <code>null</code>.
   * @return <code>true</code> if local name and the passed name match.
   */
  default boolean hasLocalName (@Nullable final String sLocalName)
  {
    return EqualsHelper.equals (getLocalName (), sLocalName);
  }

  /**
   * Get the name of the tag. It never contains XML schema prefixes or the like.
   * Is the same as {@link #getLocalName()} if a namespace URI is present.
   *
   * @return May not be <code>null</code>.
   */
  @Nonnull
  String getTagName ();

  /**
   * Check if this element has the provided tag name.
   *
   * @param sTagName
   *        The tag name to compare against. May be <code>null</code>.
   * @return <code>true</code> if tag name and the passed name match.
   */
  default boolean hasTagName (@Nullable final String sTagName)
  {
    return getTagName ().equals (sTagName);
  }

  /**
   * Check if this element has the provided tag name but ignoring case.
   *
   * @param sTagName
   *        The tag name to compare against. May be <code>null</code>.
   * @return <code>true</code> if tag name and the passed name match case
   *         insensitive.
   */
  default boolean hasTagNameIgnoreCase (@Nullable final String sTagName)
  {
    return getTagName ().equalsIgnoreCase (sTagName);
  }

  /**
   * Get the number of all direct child elements.
   *
   * @return The number of all direct child elements. Always &ge; 0.
   */
  @Nonnegative
  default int getChildElementCount ()
  {
    return getChildElementCount ((Predicate <? super IMicroElement>) null);
  }

  /**
   * Get the number of direct child elements that match the provided filter.
   *
   * @param aFilter
   *        The filter to be applied. May be <code>null</code>.
   * @return The number of all direct child elements that match the provided
   *         filter. Always &ge; 0.
   */
  @Nonnegative
  default int getChildElementCount (@Nullable final Predicate <? super IMicroElement> aFilter)
  {
    final MutableInt ret = new MutableInt (0);
    forAllChildElements (aFilter, aChildElement -> ret.inc ());
    return ret.intValue ();
  }

  @Nonnull
  static IPredicate <? super IMicroElement> filterNamespaceURI (@Nullable final String sNamespaceURI)
  {
    return aChildElement -> aChildElement.hasNamespaceURI (sNamespaceURI);
  }

  @Nonnull
  static IPredicate <? super IMicroElement> filterName (@Nullable final String sTagOrLocalName)
  {
    return aChildElement -> aChildElement.hasTagName (sTagOrLocalName);
  }

  @Nonnull
  static IPredicate <? super IMicroElement> filterNamespaceURIAndName (@Nullable final String sNamespaceURI,
                                                                       @Nullable final String sTagOrLocalName)
  {
    if (StringHelper.hasNoText (sNamespaceURI))
      return filterName (sTagOrLocalName);

    return aChildElement -> aChildElement.hasNamespaceURI (sNamespaceURI) &&
                            aChildElement.hasLocalName (sTagOrLocalName);
  }

  /**
   * Get a list of all direct child elements. Text nodes and other other child
   * nodes are not returned with this call. Micro container children are
   * inlined.
   *
   * @return Never be <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  default ICommonsList <IMicroElement> getAllChildElements ()
  {
    return getAllChildElements ((Predicate <? super IMicroElement>) null);
  }

  /**
   * Get a list of all direct child elements having the specified tag name.
   * Micro container children are inlined.
   *
   * @param sTagName
   *        The tag name to check. May be <code>null</code>.
   * @return Never be <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  default ICommonsList <IMicroElement> getAllChildElements (@Nullable final String sTagName)
  {
    return getAllChildElements (filterName (sTagName));
  }

  /**
   * Get a list of all direct child elements having the specified namespace and
   * the specified tag name. Micro container children are inlined.
   *
   * @param sNamespaceURI
   *        The namespace URI to check. May be <code>null</code>.
   * @param sLocalName
   *        The tag name to check. May be <code>null</code>.
   * @return Never be <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  default ICommonsList <IMicroElement> getAllChildElements (@Nullable final String sNamespaceURI,
                                                            @Nullable final String sLocalName)
  {
    return getAllChildElements (filterNamespaceURIAndName (sNamespaceURI, sLocalName));
  }

  /**
   * Get a list of all direct child elements matching the provided filter. Micro
   * container children are inlined.
   *
   * @param aFilter
   *        The filter to be applied. May be <code>null</code> in which all
   *        direct child elements are returned.
   * @return A new list and never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  default ICommonsList <IMicroElement> getAllChildElements (@Nullable final Predicate <? super IMicroElement> aFilter)
  {
    final ICommonsList <IMicroElement> ret = new CommonsArrayList <> ();
    forAllChildElements (aFilter, ret::add);
    return ret;
  }

  /**
   * Recursively get all child elements. Micro container children are inlined.
   *
   * @return A list containing all recursively contained elements. May not be
   *         <code>null</code> but empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  ICommonsList <IMicroElement> getAllChildElementsRecursive ();

  /**
   * Check if this element has at least one direct child element. Micro
   * container children are also checked.
   *
   * @return <code>true</code> if this element has at least one child element
   */
  default boolean hasChildElements ()
  {
    return containsAnyChildElement (null);
  }

  /**
   * Check if this element has at least one direct child element with the
   * specified tag name. Micro container children are also checked.
   *
   * @param sTagName
   *        The tag name to check. May be <code>null</code>.
   * @return <code>true</code> if this element has at least one child element
   *         with the specified tag name
   */
  default boolean hasChildElements (@Nullable final String sTagName)
  {
    return containsAnyChildElement (filterName (sTagName));
  }

  /**
   * Check if this element has at least one direct child element with the
   * specified namespace URI and tag name. Micro container children are also
   * checked.
   *
   * @param sNamespaceURI
   *        The namespace URI to check. May be <code>null</code>.
   * @param sLocalName
   *        The tag name to check. May be <code>null</code>.
   * @return <code>true</code> if this element has at least one child element
   *         with the specified namespace URI and tag name
   */
  default boolean hasChildElements (@Nullable final String sNamespaceURI, @Nullable final String sLocalName)
  {
    return containsAnyChildElement (filterNamespaceURIAndName (sNamespaceURI, sLocalName));
  }

  /**
   * Check if this element has at least one direct child element that matches
   * the provided filter. Micro container children are also checked.
   *
   * @param aFilter
   *        The filter to be applied. May be <code>null</code>.
   * @return <code>true</code> if this element has at least one direct child
   *         element that matches the passed filter, <code>false</code>
   *         otherwise.
   */
  boolean containsAnyChildElement (@Nullable Predicate <? super IMicroElement> aFilter);

  /**
   * Get the first child element of this element. Micro container children are
   * also checked.
   *
   * @return The first child element or <code>null</code> if this element has no
   *         child element.
   */
  @Nullable
  default IMicroElement getFirstChildElement ()
  {
    return findFirstChildElement (null);
  }

  /**
   * Get the first child element with the given tag name. Micro container
   * children are also checked.
   *
   * @param sTagName
   *        The tag name of the element to search. May be <code>null</code>.
   * @return <code>null</code> if no such child element is present
   */
  @Nullable
  default IMicroElement getFirstChildElement (@Nullable final String sTagName)
  {
    return findFirstChildElement (filterName (sTagName));
  }

  /**
   * Get the first child element with the given tag name and the given
   * namespace. Micro container children are also checked.
   *
   * @param sNamespaceURI
   *        The namespace URL to search.
   * @param sLocalName
   *        The tag name of the element to search.
   * @return <code>null</code> if no such child element is present
   */
  @Nullable
  default IMicroElement getFirstChildElement (@Nullable final String sNamespaceURI, @Nullable final String sLocalName)
  {
    return findFirstChildElement (filterNamespaceURIAndName (sNamespaceURI, sLocalName));
  }

  @Nullable
  IMicroElement findFirstChildElement (@Nullable Predicate <? super IMicroElement> aFilter);

  default void forAllChildElements (@Nonnull final Consumer <? super IMicroElement> aConsumer)
  {
    forAllChildElements (null, aConsumer);
  }

  void forAllChildElements (@Nullable Predicate <? super IMicroElement> aFilter,
                            @Nonnull Consumer <? super IMicroElement> aConsumer);

  @Nonnull
  default EContinue forAllChildElementsBreakable (@Nonnull final Function <? super IMicroElement, EContinue> aConsumer)
  {
    return forAllChildElementsBreakable (null, aConsumer);
  }

  @Nonnull
  EContinue forAllChildElementsBreakable (@Nullable Predicate <? super IMicroElement> aFilter,
                                          @Nonnull Function <? super IMicroElement, EContinue> aConsumer);

  /**
   * {@inheritDoc}
   */
  @Nonnull
  IMicroElement getClone ();
}
