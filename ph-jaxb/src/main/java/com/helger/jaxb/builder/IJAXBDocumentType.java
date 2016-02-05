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

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.error.IResourceErrorGroup;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.commons.xml.schema.IHasSchema;
import com.helger.commons.xml.schema.XMLSchemaValidationHelper;

/**
 * Base interface describing a single JAXB based document type, independent of
 * the version and implementation.
 *
 * @author Philip Helger
 */
public interface IJAXBDocumentType extends IHasSchema, Serializable
{
  /**
   * @return The class implementing this document type. Never <code>null</code>.
   */
  @Nonnull
  Class <?> getImplementationClass ();

  /**
   * @return The package of the implementation class. Never <code>null</code>.
   */
  @Nonnull
  Package getPackage ();

  /**
   * @return The local name of the root element of an XML document of this type.
   *         Corresponds to the name of the implementation class (without a
   *         package).
   */
  @Nonnull
  String getLocalName ();

  /**
   * @return The non-<code>null</code> XML namespace of this JAXB document type.
   */
  @Nonnull
  String getNamespaceURI ();

  /**
   * @return The combination of local name and namespace URI matching this
   *         document type. Never <code>null</code>.
   */
  @Nonnull
  QName getQName ();

  /**
   * @return The list of all paths within the classpath where the main XSD file
   *         resides.
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  List <String> getAllXSDPaths ();

  /**
   * @return The resources from which the XSD can be read using the current
   *         class loader. Never <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  default List <? extends IReadableResource> getAllXSDResources ()
  {
    return CollectionHelper.newListMapped (getAllXSDPaths (), s -> new ClassPathResource (s));
  }

  /**
   * @param aClassLoader
   *        The class loader to be used. May be <code>null</code> indicating
   *        that the default class loader should be used.
   * @return The resources from which the XSD can be read. Never
   *         <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  default List <? extends IReadableResource> getAllXSDResources (@Nullable final ClassLoader aClassLoader)
  {
    return CollectionHelper.newListMapped (getAllXSDPaths (), s -> new ClassPathResource (s, aClassLoader));
  }

  /**
   * @return The non-<code>null</code> compiled {@link Schema} object retrieved
   *         by the {@link com.helger.commons.xml.schema.XMLSchemaCache}.
   */
  @Nonnull
  default Schema getSchema ()
  {
    return getSchema ((ClassLoader) null);
  }

  /**
   * @param aClassLoader
   *        The class loader to be used. May be <code>null</code> indicating
   *        that the default class loader should be used.
   * @return The non-<code>null</code> compiled {@link Schema} object retrieved
   *         by the {@link com.helger.commons.xml.schema.XMLSchemaCache}.
   */
  @Nonnull
  Schema getSchema (@Nullable ClassLoader aClassLoader);

  /**
   * @return The non-<code>null</code> compiled {@link Validator} object
   *         retrieved from the schema to be obtained from {@link #getSchema()}.
   */
  @Nonnull
  default Validator getValidator ()
  {
    return getSchema ().newValidator ();
  }

  /**
   * @param aClassLoader
   *        The class loader to be used. May be <code>null</code> indicating
   *        that the default class loader should be used.
   * @return The non-<code>null</code> compiled {@link Validator} object
   *         retrieved from the schema to be obtained from
   *         {@link #getSchema(ClassLoader)}.
   */
  @Nonnull
  default Validator getValidator (@Nullable final ClassLoader aClassLoader)
  {
    return getSchema (aClassLoader).newValidator ();
  }

  /**
   * Validate the passed XML instance against the XML Schema of this document
   * type.
   *
   * @param aXML
   *        The XML resource to be validated. May not be <code>null</code>.
   * @param aClassLoader
   *        The class loader to be used. May be <code>null</code> indicating
   *        that the default class loader should be used.
   * @return A non-<code>null</code> group of validation errors. Is empty if no
   *         error occurred.
   */
  @Nonnull
  default IResourceErrorGroup validateXML (@Nonnull final IReadableResource aXML,
                                           @Nullable final ClassLoader aClassLoader)
  {
    return XMLSchemaValidationHelper.validate (getSchema (aClassLoader), aXML);
  }
}
