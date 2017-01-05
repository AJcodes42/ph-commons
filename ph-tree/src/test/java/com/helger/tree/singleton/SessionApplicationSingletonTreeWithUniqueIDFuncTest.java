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
package com.helger.tree.singleton;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.helger.commons.mock.CommonsTestHelper;
import com.helger.commons.scope.mock.ScopeTestRule;
import com.helger.commons.scope.singleton.AbstractSessionApplicationSingleton;
import com.helger.tree.withid.DefaultTreeItemWithID;

/**
 * Test class for class {@link AbstractSessionApplicationSingleton}.<br>
 * Note: must reside here for Mock* stuff!
 *
 * @author Philip Helger
 */
public final class SessionApplicationSingletonTreeWithUniqueIDFuncTest
{
  @Rule
  public final TestRule m_aScopeRule = new ScopeTestRule ();

  @Test
  public void testBasic () throws Exception
  {
    assertTrue (AbstractSessionApplicationSingleton.getAllSessionApplicationSingletons ().isEmpty ());
    assertFalse (AbstractSessionApplicationSingleton.isSessionApplicationSingletonInstantiated (MockSessionApplicationSingletonTreeWithUniqueID.class));
    assertNull (AbstractSessionApplicationSingleton.getSessionApplicationSingletonIfInstantiated (MockSessionApplicationSingletonTreeWithUniqueID.class));

    final MockSessionApplicationSingletonTreeWithUniqueID a = MockSessionApplicationSingletonTreeWithUniqueID.getInstance ();
    assertNotNull (a);
    assertTrue (AbstractSessionApplicationSingleton.isSessionApplicationSingletonInstantiated (MockSessionApplicationSingletonTreeWithUniqueID.class));
    assertSame (a,
                AbstractSessionApplicationSingleton.getSessionApplicationSingletonIfInstantiated (MockSessionApplicationSingletonTreeWithUniqueID.class));

    final MockSessionApplicationSingletonTreeWithUniqueID b = MockSessionApplicationSingletonTreeWithUniqueID.getInstance ();
    assertSame (a, b);

    if (false)
      CommonsTestHelper.testDefaultSerialization (a);

    assertNotNull (a.getRootItem ());
    assertFalse (a.hasChildren (a.getRootItem ()));
    assertEquals (0, a.getChildCount (a.getRootItem ()));
    final DefaultTreeItemWithID <String, String> aItem1 = a.getRootItem ().createChildItem ("id1", "value1");
    assertSame (aItem1, a.getChildWithID (a.getRootItem (), "id1"));
    assertEquals (1, a.getChildCount (a.getRootItem ()));
    assertEquals (1, a.getAllChildren (a.getRootItem ()).size ());
    assertSame (aItem1, a.getItemWithID ("id1"));
    assertEquals (1, a.getAllItems ().size ());
    final DefaultTreeItemWithID <String, String> aItem2 = aItem1.createChildItem ("id2", "value2");
    assertEquals (2, a.getAllItems ().size ());
    assertTrue (a.isItemSameOrDescendant (aItem1.getID (), aItem2.getID ()));
    assertFalse (a.isItemSameOrDescendant (aItem2.getID (), aItem1.getID ()));
    assertTrue (a.containsItemWithID (aItem1.getID ()));
    assertEquals ("value1", a.getItemDataWithID (aItem1.getID ()));
    assertEquals (2, a.getAllItemDatas ().size ());
    assertTrue (a.removeItemWithID ("id2").isChanged ());
  }
}
