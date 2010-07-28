/*******************************************************************************
 * Copyright 2010, Topic Map Lab ( http://www.topicmapslab.de )
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/**
 * 
 */
package de.topicmapslab.majortom.tests.index.paged;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;
import org.tmapi.core.Variant;

import de.topicmapslab.majortom.comparator.NameByValueComparator;
import de.topicmapslab.majortom.comparator.OccurrenceByValueComparator;
import de.topicmapslab.majortom.comparator.TopicByIdentityComparator;
import de.topicmapslab.majortom.comparator.VariantByValueComparator;
import de.topicmapslab.majortom.core.ScopeImpl;
import de.topicmapslab.majortom.model.core.IAssociation;
import de.topicmapslab.majortom.model.core.ICharacteristics;
import de.topicmapslab.majortom.model.core.IName;
import de.topicmapslab.majortom.model.core.IOccurrence;
import de.topicmapslab.majortom.model.core.IScopable;
import de.topicmapslab.majortom.model.core.IScope;
import de.topicmapslab.majortom.model.core.ITopic;
import de.topicmapslab.majortom.model.core.IVariant;
import de.topicmapslab.majortom.model.index.paging.IPagedScopedIndex;
import de.topicmapslab.majortom.tests.MaJorToMTestCase;
import de.topicmapslab.majortom.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TestPagedScopedIndex extends MaJorToMTestCase {

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getAssociationScopes(int, int)}
	 * .
	 */
	public void testGetAssociationScopesIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getAssociationScopes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		String base = "http://psi.example.org/topics/";
		IScope[] scopes = new IScope[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				Topic theme = createTopicBySI(base + c + i);
				IAssociation association = (IAssociation) topicMap.createAssociation(createTopic(), theme);
				scopes[j] = association.getScopeObject();
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<IScope> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getAssociationScopes(i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getAssociationScopes(100, 10);
		assertEquals(1, list.size());

		Comparator<IScope> comp = new Comparator<IScope>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(IScope arg0, IScope arg1) {
				return new TopicByIdentityComparator(true).compare(arg0.getThemes().iterator().next(), arg1.getThemes().iterator().next());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getAssociationScopes(i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(scopes[i * 10], list.get(0));
			assertEquals(scopes[i * 10 + 1], list.get(1));
			assertEquals(scopes[i * 10 + 2], list.get(2));
			assertEquals(scopes[i * 10 + 3], list.get(3));
			assertEquals(scopes[i * 10 + 4], list.get(4));
			assertEquals(scopes[i * 10 + 5], list.get(5));
			assertEquals(scopes[i * 10 + 6], list.get(6));
			assertEquals(scopes[i * 10 + 7], list.get(7));
			assertEquals(scopes[i * 10 + 8], list.get(8));
			assertEquals(scopes[i * 10 + 9], list.get(9));
		}
		list = index.getAssociationScopes(100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(scopes[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getAssociationThemes(int, int)}
	 * .
	 */
	public void testGetAssociationThemesIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getAssociationThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		String base = "http://psi.example.org/topics/";
		Topic[] themes = new Topic[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				Topic theme = createTopicBySI(base + c + i);
				topicMap.createAssociation(createTopic(), theme);
				themes[j] = theme;
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Topic> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getAssociationThemes(i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getAssociationThemes(100, 10);
		assertEquals(1, list.size());

		Comparator<Topic> comp = new TopicByIdentityComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getAssociationThemes(i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(themes[i * 10], list.get(0));
			assertEquals(themes[i * 10 + 1], list.get(1));
			assertEquals(themes[i * 10 + 2], list.get(2));
			assertEquals(themes[i * 10 + 3], list.get(3));
			assertEquals(themes[i * 10 + 4], list.get(4));
			assertEquals(themes[i * 10 + 5], list.get(5));
			assertEquals(themes[i * 10 + 6], list.get(6));
			assertEquals(themes[i * 10 + 7], list.get(7));
			assertEquals(themes[i * 10 + 8], list.get(8));
			assertEquals(themes[i * 10 + 9], list.get(9));
		}
		list = index.getAssociationThemes(100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(themes[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getAssociations(org.tmapi.core.Topic, int, int)}
	 * .
	 */
	public void testGetAssociationsTopicIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic theme = createTopic();
		Association[] associations = new Association[101];
		int j = 0;
		String base = "http://psi.example.org/topics/";
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				associations[j] = createAssociation(createTopic());
				associations[j].addTheme(theme);
				associations[j].addItemIdentifier(createLocator(base + c + i));
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Association> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getAssociations(theme, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getAssociations(theme, 100, 10);
		assertEquals(1, list.size());

		Comparator<Association> comp = new Comparator<Association>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(Association o1, Association o2) {
				return o1.getItemIdentifiers().iterator().next().getReference().compareTo(o2.getItemIdentifiers().iterator().next().getReference());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getAssociations(theme, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(associations[i * 10], list.get(0));
			assertEquals(associations[i * 10 + 1], list.get(1));
			assertEquals(associations[i * 10 + 2], list.get(2));
			assertEquals(associations[i * 10 + 3], list.get(3));
			assertEquals(associations[i * 10 + 4], list.get(4));
			assertEquals(associations[i * 10 + 5], list.get(5));
			assertEquals(associations[i * 10 + 6], list.get(6));
			assertEquals(associations[i * 10 + 7], list.get(7));
			assertEquals(associations[i * 10 + 8], list.get(8));
			assertEquals(associations[i * 10 + 9], list.get(9));
		}
		list = index.getAssociations(theme, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(associations[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getAssociations(org.tmapi.core.Topic[], boolean, int, int)}
	 * .
	 */
	public void testGetAssociationsTopicArrayBooleanIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic theme = createTopic();
		Topic other = createTopic();
		Association[] associations = new Association[101];
		int j = 0;
		String base = "http://psi.example.org/topics/";
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				associations[j] = createAssociation(createTopic());
				associations[j].addItemIdentifier(createLocator(base + c + i));

				if (j % 2 == 0) {
					associations[j].addTheme(theme);
				} else {
					associations[j].addTheme(other);
				}
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Association> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getAssociations(new Topic[] { theme, other }, false, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getAssociations(new Topic[] { theme, other }, false, 100, 10);
		assertEquals(1, list.size());

		Comparator<Association> comp = new Comparator<Association>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(Association o1, Association o2) {
				return o1.getItemIdentifiers().iterator().next().getReference().compareTo(o2.getItemIdentifiers().iterator().next().getReference());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getAssociations(new Topic[] { theme, other }, false, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(associations[i * 10], list.get(0));
			assertEquals(associations[i * 10 + 1], list.get(1));
			assertEquals(associations[i * 10 + 2], list.get(2));
			assertEquals(associations[i * 10 + 3], list.get(3));
			assertEquals(associations[i * 10 + 4], list.get(4));
			assertEquals(associations[i * 10 + 5], list.get(5));
			assertEquals(associations[i * 10 + 6], list.get(6));
			assertEquals(associations[i * 10 + 7], list.get(7));
			assertEquals(associations[i * 10 + 8], list.get(8));
			assertEquals(associations[i * 10 + 9], list.get(9));
		}
		list = index.getAssociations(new Topic[] { theme, other }, false, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(associations[100], list.get(0));

		/*
		 * matching all
		 */
		associations = new Association[101];
		j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				associations[j] = createAssociation(createTopic());
				associations[j].addItemIdentifier(createLocator(base + "all/" + c + i));
				associations[j].addTheme(theme);
				associations[j].addTheme(other);
				j++;
			}
			if (j == 101) {
				break;
			}
		}
		for (int i = 0; i < 10; i++) {
			list = index.getAssociations(new Topic[] { theme, other }, true, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getAssociations(new Topic[] { theme, other }, true, 100, 10);
		assertEquals(1, list.size());

		for (int i = 0; i < 10; i++) {
			list = index.getAssociations(new Topic[] { theme, other }, true, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(associations[i * 10], list.get(0));
			assertEquals(associations[i * 10 + 1], list.get(1));
			assertEquals(associations[i * 10 + 2], list.get(2));
			assertEquals(associations[i * 10 + 3], list.get(3));
			assertEquals(associations[i * 10 + 4], list.get(4));
			assertEquals(associations[i * 10 + 5], list.get(5));
			assertEquals(associations[i * 10 + 6], list.get(6));
			assertEquals(associations[i * 10 + 7], list.get(7));
			assertEquals(associations[i * 10 + 8], list.get(8));
			assertEquals(associations[i * 10 + 9], list.get(9));
		}
		list = index.getAssociations(new Topic[] { theme, other }, true, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(associations[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getAssociations(de.topicmapslab.majortom.model.core.IScope, int, int)}
	 * .
	 */
	public void testGetAssociationsIScopeIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		ITopic theme = createTopic();
		Set<ITopic> themes = HashUtil.getHashSet();
		themes.add(theme);
		IScope scope = new ScopeImpl(themes);
		String base = "http://psi.example.org/topics/";
		Association[] associations = new Association[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				associations[j] = createAssociation(createTopic());
				associations[j].addTheme(theme);
				associations[j].addItemIdentifier(createLocator(base + c + i));
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Association> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getAssociations(scope, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getAssociations(scope, 100, 10);
		assertEquals(1, list.size());

		Comparator<Association> comp = new Comparator<Association>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(Association o1, Association o2) {
				return o1.getItemIdentifiers().iterator().next().getReference().compareTo(o2.getItemIdentifiers().iterator().next().getReference());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getAssociations(scope, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(associations[i * 10], list.get(0));
			assertEquals(associations[i * 10 + 1], list.get(1));
			assertEquals(associations[i * 10 + 2], list.get(2));
			assertEquals(associations[i * 10 + 3], list.get(3));
			assertEquals(associations[i * 10 + 4], list.get(4));
			assertEquals(associations[i * 10 + 5], list.get(5));
			assertEquals(associations[i * 10 + 6], list.get(6));
			assertEquals(associations[i * 10 + 7], list.get(7));
			assertEquals(associations[i * 10 + 8], list.get(8));
			assertEquals(associations[i * 10 + 9], list.get(9));
		}
		list = index.getAssociations(scope, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(associations[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getAssociations(java.util.Collection, int, int)}
	 * .
	 */
	public void testGetAssociationsCollectionOfIScopeIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		ITopic theme = createTopic();
		Set<ITopic> themes = HashUtil.getHashSet();
		themes.add(theme);
		IScope scope = new ScopeImpl(themes);
		ITopic otherTheme = createTopic();
		Set<ITopic> otherThemes = HashUtil.getHashSet();
		otherThemes.add(otherTheme);
		IScope otherScope = new ScopeImpl(otherThemes);
		String base = "http://psi.example.org/topics/";
		Association[] associations = new Association[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				associations[j] = createAssociation(createTopic());
				associations[j].addItemIdentifier(createLocator(base + c + i));
				if (j % 2 == 0) {
					associations[j].addTheme(theme);
				} else {
					associations[j].addTheme(otherTheme);
				}
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		Collection<IScope> scopes = HashUtil.getHashSet();
		scopes.add(scope);
		scopes.add(otherScope);

		List<Association> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getAssociations(scopes, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getAssociations(scopes, 100, 10);
		assertEquals(1, list.size());

		Comparator<Association> comp = new Comparator<Association>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(Association o1, Association o2) {
				return o1.getItemIdentifiers().iterator().next().getReference().compareTo(o2.getItemIdentifiers().iterator().next().getReference());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getAssociations(scopes, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(associations[i * 10], list.get(0));
			assertEquals(associations[i * 10 + 1], list.get(1));
			assertEquals(associations[i * 10 + 2], list.get(2));
			assertEquals(associations[i * 10 + 3], list.get(3));
			assertEquals(associations[i * 10 + 4], list.get(4));
			assertEquals(associations[i * 10 + 5], list.get(5));
			assertEquals(associations[i * 10 + 6], list.get(6));
			assertEquals(associations[i * 10 + 7], list.get(7));
			assertEquals(associations[i * 10 + 8], list.get(8));
			assertEquals(associations[i * 10 + 9], list.get(9));
		}
		list = index.getAssociations(scopes, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(associations[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getCharacteristics(de.topicmapslab.majortom.model.core.IScope, int, int)}
	 * .
	 */
	public void testGetCharacteristicsIScopeIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		ITopic theme = createTopic();
		Set<ITopic> themes = HashUtil.getHashSet();
		themes.add(theme);
		IScope scope = new ScopeImpl(themes);

		ICharacteristics[] characteristics = new ICharacteristics[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				if (j % 2 == 0) {
					characteristics[j] = (IOccurrence) createTopic().createOccurrence(createTopic(), c + i, theme);
				} else {
					characteristics[j] = (IName) createTopic().createName(createTopic(), c + i, theme);
				}
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<ICharacteristics> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getCharacteristics(scope, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getCharacteristics(scope, 100, 10);
		assertEquals(1, list.size());

		Comparator<ICharacteristics> comp = new Comparator<ICharacteristics>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(ICharacteristics o1, ICharacteristics o2) {
				String val1 = o1 instanceof IOccurrence ? ((IOccurrence) o1).getValue() : ((IName) o1).getValue();
				String val2 = o2 instanceof IOccurrence ? ((IOccurrence) o2).getValue() : ((IName) o2).getValue();
				return val1.compareTo(val2);
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getCharacteristics(scope, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(characteristics[i * 10], list.get(0));
			assertEquals(characteristics[i * 10 + 1], list.get(1));
			assertEquals(characteristics[i * 10 + 2], list.get(2));
			assertEquals(characteristics[i * 10 + 3], list.get(3));
			assertEquals(characteristics[i * 10 + 4], list.get(4));
			assertEquals(characteristics[i * 10 + 5], list.get(5));
			assertEquals(characteristics[i * 10 + 6], list.get(6));
			assertEquals(characteristics[i * 10 + 7], list.get(7));
			assertEquals(characteristics[i * 10 + 8], list.get(8));
			assertEquals(characteristics[i * 10 + 9], list.get(9));
		}
		list = index.getCharacteristics(scope, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(characteristics[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getNameScopes(int, int)}
	 * .
	 */
	public void testGetNameScopesIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getAssociationScopes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		String base = "http://psi.example.org/topics/";
		IScope[] scopes = new IScope[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				Topic theme = createTopicBySI(base + c + i);
				IName name = (IName) createTopic().createName("Name", theme);
				scopes[j] = name.getScopeObject();
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<IScope> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getNameScopes(i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getNameScopes(100, 10);
		assertEquals(1, list.size());

		Comparator<IScope> comp = new Comparator<IScope>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(IScope arg0, IScope arg1) {
				return new TopicByIdentityComparator(true).compare(arg0.getThemes().iterator().next(), arg1.getThemes().iterator().next());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getNameScopes(i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(scopes[i * 10], list.get(0));
			assertEquals(scopes[i * 10 + 1], list.get(1));
			assertEquals(scopes[i * 10 + 2], list.get(2));
			assertEquals(scopes[i * 10 + 3], list.get(3));
			assertEquals(scopes[i * 10 + 4], list.get(4));
			assertEquals(scopes[i * 10 + 5], list.get(5));
			assertEquals(scopes[i * 10 + 6], list.get(6));
			assertEquals(scopes[i * 10 + 7], list.get(7));
			assertEquals(scopes[i * 10 + 8], list.get(8));
			assertEquals(scopes[i * 10 + 9], list.get(9));
		}
		list = index.getNameScopes(100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(scopes[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getNameThemes(int, int)}
	 * .
	 */
	public void testGetNameThemesIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getNameThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		String base = "http://psi.example.org/topics/";
		Topic[] themes = new Topic[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				Topic theme = createTopicBySI(base + c + i);
				createTopic().createName("Name", theme);
				themes[j] = theme;
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Topic> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getNameThemes(i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getNameThemes(100, 10);
		assertEquals(1, list.size());

		Comparator<Topic> comp = new TopicByIdentityComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getNameThemes(i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(themes[i * 10], list.get(0));
			assertEquals(themes[i * 10 + 1], list.get(1));
			assertEquals(themes[i * 10 + 2], list.get(2));
			assertEquals(themes[i * 10 + 3], list.get(3));
			assertEquals(themes[i * 10 + 4], list.get(4));
			assertEquals(themes[i * 10 + 5], list.get(5));
			assertEquals(themes[i * 10 + 6], list.get(6));
			assertEquals(themes[i * 10 + 7], list.get(7));
			assertEquals(themes[i * 10 + 8], list.get(8));
			assertEquals(themes[i * 10 + 9], list.get(9));
		}
		list = index.getNameThemes(100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(themes[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getNames(org.tmapi.core.Topic, int, int)}
	 * .
	 */
	public void testGetNamesTopicIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic theme = createTopic();
		Name[] names = new Name[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				names[j] = createTopic().createName(createTopic(), c + i, theme);
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Name> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getNames(theme, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getNames(theme, 100, 10);
		assertEquals(1, list.size());

		Comparator<Name> comp = new NameByValueComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getNames(theme, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(names[i * 10], list.get(0));
			assertEquals(names[i * 10 + 1], list.get(1));
			assertEquals(names[i * 10 + 2], list.get(2));
			assertEquals(names[i * 10 + 3], list.get(3));
			assertEquals(names[i * 10 + 4], list.get(4));
			assertEquals(names[i * 10 + 5], list.get(5));
			assertEquals(names[i * 10 + 6], list.get(6));
			assertEquals(names[i * 10 + 7], list.get(7));
			assertEquals(names[i * 10 + 8], list.get(8));
			assertEquals(names[i * 10 + 9], list.get(9));
		}
		list = index.getNames(theme, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(names[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getNames(org.tmapi.core.Topic[], boolean, int, int)}
	 * .
	 */
	public void testGetNamesTopicArrayBooleanIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic theme = createTopic();
		Topic other = createTopic();
		Name[] names = new Name[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				names[j] = createTopic().createName(c + i, new Topic[0]);
				if (j % 2 == 0) {
					names[j].addTheme(theme);
				} else {
					names[j].addTheme(other);
				}
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Name> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getNames(new Topic[] { theme, other }, false, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getNames(new Topic[] { theme, other }, false, 100, 10);
		assertEquals(1, list.size());

		Comparator<Name> comp = new NameByValueComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getNames(new Topic[] { theme, other }, false, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(names[i * 10], list.get(0));
			assertEquals(names[i * 10 + 1], list.get(1));
			assertEquals(names[i * 10 + 2], list.get(2));
			assertEquals(names[i * 10 + 3], list.get(3));
			assertEquals(names[i * 10 + 4], list.get(4));
			assertEquals(names[i * 10 + 5], list.get(5));
			assertEquals(names[i * 10 + 6], list.get(6));
			assertEquals(names[i * 10 + 7], list.get(7));
			assertEquals(names[i * 10 + 8], list.get(8));
			assertEquals(names[i * 10 + 9], list.get(9));
		}
		list = index.getNames(new Topic[] { theme, other }, false, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(names[100], list.get(0));

		/*
		 * matching all
		 */
		names = new Name[101];
		j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				names[j] = createTopic().createName(c + i, new Topic[0]);
				names[j].addTheme(theme);
				names[j].addTheme(other);
				j++;
			}
			if (j == 101) {
				break;
			}
		}
		for (int i = 0; i < 10; i++) {
			list = index.getNames(new Topic[] { theme, other }, true, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getNames(new Topic[] { theme, other }, true, 100, 10);
		assertEquals(1, list.size());

		for (int i = 0; i < 10; i++) {
			list = index.getNames(new Topic[] { theme, other }, true, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(names[i * 10], list.get(0));
			assertEquals(names[i * 10 + 1], list.get(1));
			assertEquals(names[i * 10 + 2], list.get(2));
			assertEquals(names[i * 10 + 3], list.get(3));
			assertEquals(names[i * 10 + 4], list.get(4));
			assertEquals(names[i * 10 + 5], list.get(5));
			assertEquals(names[i * 10 + 6], list.get(6));
			assertEquals(names[i * 10 + 7], list.get(7));
			assertEquals(names[i * 10 + 8], list.get(8));
			assertEquals(names[i * 10 + 9], list.get(9));
		}
		list = index.getNames(new Topic[] { theme, other }, true, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(names[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getNames(de.topicmapslab.majortom.model.core.IScope, int, int)}
	 * .
	 */
	public void testGetNamesIScopeIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		ITopic theme = createTopic();
		Set<ITopic> themes = HashUtil.getHashSet();
		themes.add(theme);
		IScope scope = new ScopeImpl(themes);

		Name[] names = new Name[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				names[j] = createTopic().createName(createTopic(), c + i, theme);
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Name> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getNames(scope, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getNames(scope, 100, 10);
		assertEquals(1, list.size());

		Comparator<Name> comp = new NameByValueComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getNames(scope, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(names[i * 10], list.get(0));
			assertEquals(names[i * 10 + 1], list.get(1));
			assertEquals(names[i * 10 + 2], list.get(2));
			assertEquals(names[i * 10 + 3], list.get(3));
			assertEquals(names[i * 10 + 4], list.get(4));
			assertEquals(names[i * 10 + 5], list.get(5));
			assertEquals(names[i * 10 + 6], list.get(6));
			assertEquals(names[i * 10 + 7], list.get(7));
			assertEquals(names[i * 10 + 8], list.get(8));
			assertEquals(names[i * 10 + 9], list.get(9));
		}
		list = index.getNames(scope, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(names[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getNames(java.util.Collection, int, int)}
	 * .
	 */
	public void testGetNamesCollectionOfIScopeIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		ITopic theme = createTopic();
		Set<ITopic> themes = HashUtil.getHashSet();
		themes.add(theme);
		IScope scope = new ScopeImpl(themes);
		ITopic otherTheme = createTopic();
		Set<ITopic> otherThemes = HashUtil.getHashSet();
		otherThemes.add(otherTheme);
		IScope otherScope = new ScopeImpl(otherThemes);
		Name[] names = new Name[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				if (j % 2 == 0) {
					names[j] = createTopic().createName(c + i, theme);
				} else {
					names[j] = createTopic().createName(c + i, otherTheme);
				}
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		Collection<IScope> scopes = HashUtil.getHashSet();
		scopes.add(scope);
		scopes.add(otherScope);

		List<Name> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getNames(scopes, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getNames(scopes, 100, 10);
		assertEquals(1, list.size());

		Comparator<Name> comp = new NameByValueComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getNames(scopes, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(names[i * 10], list.get(0));
			assertEquals(names[i * 10 + 1], list.get(1));
			assertEquals(names[i * 10 + 2], list.get(2));
			assertEquals(names[i * 10 + 3], list.get(3));
			assertEquals(names[i * 10 + 4], list.get(4));
			assertEquals(names[i * 10 + 5], list.get(5));
			assertEquals(names[i * 10 + 6], list.get(6));
			assertEquals(names[i * 10 + 7], list.get(7));
			assertEquals(names[i * 10 + 8], list.get(8));
			assertEquals(names[i * 10 + 9], list.get(9));
		}
		list = index.getNames(scopes, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(names[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getOccurrenceScopes(int, int)}
	 * .
	 */
	public void testGetOccurrenceScopesIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getOccurrenceScopes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		String base = "http://psi.example.org/topics/";
		IScope[] scopes = new IScope[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				Topic theme = createTopicBySI(base + c + i);
				IOccurrence occ = (IOccurrence) createTopic().createOccurrence(createTopic(), "Value", theme);
				scopes[j] = occ.getScopeObject();
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<IScope> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getOccurrenceScopes(i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getOccurrenceScopes(100, 10);
		assertEquals(1, list.size());

		Comparator<IScope> comp = new Comparator<IScope>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(IScope arg0, IScope arg1) {
				return new TopicByIdentityComparator(true).compare(arg0.getThemes().iterator().next(), arg1.getThemes().iterator().next());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getOccurrenceScopes(i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(scopes[i * 10], list.get(0));
			assertEquals(scopes[i * 10 + 1], list.get(1));
			assertEquals(scopes[i * 10 + 2], list.get(2));
			assertEquals(scopes[i * 10 + 3], list.get(3));
			assertEquals(scopes[i * 10 + 4], list.get(4));
			assertEquals(scopes[i * 10 + 5], list.get(5));
			assertEquals(scopes[i * 10 + 6], list.get(6));
			assertEquals(scopes[i * 10 + 7], list.get(7));
			assertEquals(scopes[i * 10 + 8], list.get(8));
			assertEquals(scopes[i * 10 + 9], list.get(9));
		}
		list = index.getOccurrenceScopes(100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(scopes[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getOccurrenceThemes(int, int)}
	 * .
	 */
	public void testGetOccurrenceThemesIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getOccurrenceThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		String base = "http://psi.example.org/topics/";
		Topic[] themes = new Topic[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				Topic theme = createTopicBySI(base + c + i);
				createTopic().createOccurrence(createTopic(), "Value", theme);
				themes[j] = theme;
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Topic> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getOccurrenceThemes(i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getOccurrenceThemes(100, 10);
		assertEquals(1, list.size());

		Comparator<Topic> comp = new TopicByIdentityComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getOccurrenceThemes(i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(themes[i * 10], list.get(0));
			assertEquals(themes[i * 10 + 1], list.get(1));
			assertEquals(themes[i * 10 + 2], list.get(2));
			assertEquals(themes[i * 10 + 3], list.get(3));
			assertEquals(themes[i * 10 + 4], list.get(4));
			assertEquals(themes[i * 10 + 5], list.get(5));
			assertEquals(themes[i * 10 + 6], list.get(6));
			assertEquals(themes[i * 10 + 7], list.get(7));
			assertEquals(themes[i * 10 + 8], list.get(8));
			assertEquals(themes[i * 10 + 9], list.get(9));
		}
		list = index.getOccurrenceThemes(100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(themes[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getOccurrences(org.tmapi.core.Topic, int, int)}
	 * .
	 */
	public void testGetOccurrencesTopicIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic theme = createTopic();
		Occurrence[] occurrences = new Occurrence[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				occurrences[j] = createTopic().createOccurrence(createTopic(), c + i, theme);
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Occurrence> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getOccurrences(theme, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getOccurrences(theme, 100, 10);
		assertEquals(1, list.size());

		Comparator<Occurrence> comp = new OccurrenceByValueComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getOccurrences(theme, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(occurrences[i * 10], list.get(0));
			assertEquals(occurrences[i * 10 + 1], list.get(1));
			assertEquals(occurrences[i * 10 + 2], list.get(2));
			assertEquals(occurrences[i * 10 + 3], list.get(3));
			assertEquals(occurrences[i * 10 + 4], list.get(4));
			assertEquals(occurrences[i * 10 + 5], list.get(5));
			assertEquals(occurrences[i * 10 + 6], list.get(6));
			assertEquals(occurrences[i * 10 + 7], list.get(7));
			assertEquals(occurrences[i * 10 + 8], list.get(8));
			assertEquals(occurrences[i * 10 + 9], list.get(9));
		}
		list = index.getOccurrences(theme, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(occurrences[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getOccurrences(org.tmapi.core.Topic[], boolean, int, int)}
	 * .
	 */
	public void testGetOccurrencesTopicArrayBooleanIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic theme = createTopic();
		Topic other = createTopic();
		Occurrence[] occurrences = new Occurrence[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				occurrences[j] = createTopic().createOccurrence(createTopic(), c + i, new Topic[0]);
				if (j % 2 == 0) {
					occurrences[j].addTheme(theme);
				} else {
					occurrences[j].addTheme(other);
				}
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Occurrence> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getOccurrences(new Topic[] { theme, other }, false, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getOccurrences(new Topic[] { theme, other }, false, 100, 10);
		assertEquals(1, list.size());

		Comparator<Occurrence> comp = new OccurrenceByValueComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getOccurrences(new Topic[] { theme, other }, false, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(occurrences[i * 10], list.get(0));
			assertEquals(occurrences[i * 10 + 1], list.get(1));
			assertEquals(occurrences[i * 10 + 2], list.get(2));
			assertEquals(occurrences[i * 10 + 3], list.get(3));
			assertEquals(occurrences[i * 10 + 4], list.get(4));
			assertEquals(occurrences[i * 10 + 5], list.get(5));
			assertEquals(occurrences[i * 10 + 6], list.get(6));
			assertEquals(occurrences[i * 10 + 7], list.get(7));
			assertEquals(occurrences[i * 10 + 8], list.get(8));
			assertEquals(occurrences[i * 10 + 9], list.get(9));
		}
		list = index.getOccurrences(new Topic[] { theme, other }, false, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(occurrences[100], list.get(0));

		/*
		 * matching all
		 */
		occurrences = new Occurrence[101];
		j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				occurrences[j] = createTopic().createOccurrence(createTopic(), c + i, new Topic[0]);
				occurrences[j].addTheme(theme);
				occurrences[j].addTheme(other);
				j++;
			}
			if (j == 101) {
				break;
			}
		}
		for (int i = 0; i < 10; i++) {
			list = index.getOccurrences(new Topic[] { theme, other }, true, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getOccurrences(new Topic[] { theme, other }, true, 100, 10);
		assertEquals(1, list.size());

		for (int i = 0; i < 10; i++) {
			list = index.getOccurrences(new Topic[] { theme, other }, true, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(occurrences[i * 10], list.get(0));
			assertEquals(occurrences[i * 10 + 1], list.get(1));
			assertEquals(occurrences[i * 10 + 2], list.get(2));
			assertEquals(occurrences[i * 10 + 3], list.get(3));
			assertEquals(occurrences[i * 10 + 4], list.get(4));
			assertEquals(occurrences[i * 10 + 5], list.get(5));
			assertEquals(occurrences[i * 10 + 6], list.get(6));
			assertEquals(occurrences[i * 10 + 7], list.get(7));
			assertEquals(occurrences[i * 10 + 8], list.get(8));
			assertEquals(occurrences[i * 10 + 9], list.get(9));
		}
		list = index.getOccurrences(new Topic[] { theme, other }, true, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(occurrences[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getOccurrences(de.topicmapslab.majortom.model.core.IScope, int, int)}
	 * .
	 */
	public void testGetOccurrencesIScopeIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		ITopic theme = createTopic();
		Set<ITopic> themes = HashUtil.getHashSet();
		themes.add(theme);
		IScope scope = new ScopeImpl(themes);

		Occurrence[] occurrences = new Occurrence[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				occurrences[j] = createTopic().createOccurrence(createTopic(), c + i, theme);
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Occurrence> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getOccurrences(scope, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getOccurrences(scope, 100, 10);
		assertEquals(1, list.size());

		Comparator<Occurrence> comp = new OccurrenceByValueComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getOccurrences(scope, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(occurrences[i * 10], list.get(0));
			assertEquals(occurrences[i * 10 + 1], list.get(1));
			assertEquals(occurrences[i * 10 + 2], list.get(2));
			assertEquals(occurrences[i * 10 + 3], list.get(3));
			assertEquals(occurrences[i * 10 + 4], list.get(4));
			assertEquals(occurrences[i * 10 + 5], list.get(5));
			assertEquals(occurrences[i * 10 + 6], list.get(6));
			assertEquals(occurrences[i * 10 + 7], list.get(7));
			assertEquals(occurrences[i * 10 + 8], list.get(8));
			assertEquals(occurrences[i * 10 + 9], list.get(9));
		}
		list = index.getOccurrences(scope, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(occurrences[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getOccurrences(java.util.Collection, int, int)}
	 * .
	 */
	public void testGetOccurrencesCollectionOfIScopeIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		ITopic theme = createTopic();
		Set<ITopic> themes = HashUtil.getHashSet();
		themes.add(theme);
		IScope scope = new ScopeImpl(themes);
		ITopic otherTheme = createTopic();
		Set<ITopic> otherThemes = HashUtil.getHashSet();
		otherThemes.add(otherTheme);
		IScope otherScope = new ScopeImpl(otherThemes);
		Occurrence[] occurrences = new Occurrence[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				if (j % 2 == 0) {
					occurrences[j] = createTopic().createOccurrence(createTopic(), c + i, theme);
				} else {
					occurrences[j] = createTopic().createOccurrence(createTopic(), c + i, otherTheme);
				}
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		Collection<IScope> scopes = HashUtil.getHashSet();
		scopes.add(scope);
		scopes.add(otherScope);

		List<Occurrence> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getOccurrences(scopes, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getOccurrences(scopes, 100, 10);
		assertEquals(1, list.size());

		Comparator<Occurrence> comp = new OccurrenceByValueComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getOccurrences(scopes, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(occurrences[i * 10], list.get(0));
			assertEquals(occurrences[i * 10 + 1], list.get(1));
			assertEquals(occurrences[i * 10 + 2], list.get(2));
			assertEquals(occurrences[i * 10 + 3], list.get(3));
			assertEquals(occurrences[i * 10 + 4], list.get(4));
			assertEquals(occurrences[i * 10 + 5], list.get(5));
			assertEquals(occurrences[i * 10 + 6], list.get(6));
			assertEquals(occurrences[i * 10 + 7], list.get(7));
			assertEquals(occurrences[i * 10 + 8], list.get(8));
			assertEquals(occurrences[i * 10 + 9], list.get(9));
		}
		list = index.getOccurrences(scopes, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(occurrences[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getScopables(de.topicmapslab.majortom.model.core.IScope, int, int)}
	 * .
	 */
	public void testGetScopablesIScopeIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		ITopic theme = createTopic();
		Set<ITopic> themes = HashUtil.getHashSet();
		themes.add(theme);
		IScope scope = new ScopeImpl(themes);
		String base = "http://psi.example.org/topics/";
		IScopable[] scopeables = new IScopable[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				if (j % 4 == 0) {
					scopeables[j] = (IOccurrence) createTopic().createOccurrence(createTopic(), c + i, theme);
				} else if (j % 4 == 0) {
					scopeables[j] = (IName) createTopic().createName(createTopic(), c + i, theme);
				} else if (j % 4 == 0) {
					scopeables[j] = (IVariant) createTopic().createName(createTopic(), c + i, new Topic[0]).createVariant("Value", theme);
				} else {
					scopeables[j] = createAssociation(createTopic());
					scopeables[j].addTheme(theme);
				}
				scopeables[j].addItemIdentifier(createLocator(base + c + i));
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Scoped> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getScopables(scope, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getScopables(scope, 100, 10);
		assertEquals(1, list.size());

		Comparator<Scoped> comp = new Comparator<Scoped>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(Scoped o1, Scoped o2) {
				return o1.getItemIdentifiers().iterator().next().getReference().compareTo(o2.getItemIdentifiers().iterator().next().getReference());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getScopables(scope, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(scopeables[i * 10], list.get(0));
			assertEquals(scopeables[i * 10 + 1], list.get(1));
			assertEquals(scopeables[i * 10 + 2], list.get(2));
			assertEquals(scopeables[i * 10 + 3], list.get(3));
			assertEquals(scopeables[i * 10 + 4], list.get(4));
			assertEquals(scopeables[i * 10 + 5], list.get(5));
			assertEquals(scopeables[i * 10 + 6], list.get(6));
			assertEquals(scopeables[i * 10 + 7], list.get(7));
			assertEquals(scopeables[i * 10 + 8], list.get(8));
			assertEquals(scopeables[i * 10 + 9], list.get(9));
		}
		list = index.getScopables(scope, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(scopeables[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getVariantScopes(int, int)}
	 * .
	 */
	public void testGetVariantScopesIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantScopes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		String base = "http://psi.example.org/topics/";
		IScope[] scopes = new IScope[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				Topic theme = createTopicBySI(base + c + i);
				IVariant variant = (IVariant) createTopic().createName("Value", new Topic[0]).createVariant("Value", theme);
				scopes[j] = variant.getScopeObject();
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<IScope> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getVariantScopes(i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getVariantScopes(100, 10);
		assertEquals(1, list.size());

		Comparator<IScope> comp = new Comparator<IScope>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(IScope arg0, IScope arg1) {
				return new TopicByIdentityComparator(true).compare(arg0.getThemes().iterator().next(), arg1.getThemes().iterator().next());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getVariantScopes(i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(scopes[i * 10], list.get(0));
			assertEquals(scopes[i * 10 + 1], list.get(1));
			assertEquals(scopes[i * 10 + 2], list.get(2));
			assertEquals(scopes[i * 10 + 3], list.get(3));
			assertEquals(scopes[i * 10 + 4], list.get(4));
			assertEquals(scopes[i * 10 + 5], list.get(5));
			assertEquals(scopes[i * 10 + 6], list.get(6));
			assertEquals(scopes[i * 10 + 7], list.get(7));
			assertEquals(scopes[i * 10 + 8], list.get(8));
			assertEquals(scopes[i * 10 + 9], list.get(9));
		}
		list = index.getVariantScopes(100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(scopes[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getVariantThemes(int, int)}
	 * .
	 */
	public void testGetVariantThemesIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		String base = "http://psi.example.org/topics/";
		Topic[] themes = new Topic[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				Topic theme = createTopicBySI(base + c + i);
				createTopic().createName("Value", new Topic[0]).createVariant("Value", theme);
				themes[j] = theme;
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Topic> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getVariantThemes(i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getVariantThemes(100, 10);
		assertEquals(1, list.size());

		Comparator<Topic> comp = new TopicByIdentityComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getVariantThemes(i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(themes[i * 10], list.get(0));
			assertEquals(themes[i * 10 + 1], list.get(1));
			assertEquals(themes[i * 10 + 2], list.get(2));
			assertEquals(themes[i * 10 + 3], list.get(3));
			assertEquals(themes[i * 10 + 4], list.get(4));
			assertEquals(themes[i * 10 + 5], list.get(5));
			assertEquals(themes[i * 10 + 6], list.get(6));
			assertEquals(themes[i * 10 + 7], list.get(7));
			assertEquals(themes[i * 10 + 8], list.get(8));
			assertEquals(themes[i * 10 + 9], list.get(9));
		}
		list = index.getVariantThemes(100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(themes[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getVariants(org.tmapi.core.Topic, int, int)}
	 * .
	 */
	public void testGetVariantsTopicIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic theme = createTopic();
		Variant[] variants = new Variant[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				variants[j] = createTopic().createName("name", new Topic[0]).createVariant(c + i, theme);
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Variant> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getVariants(theme, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getVariants(theme, 100, 10);
		assertEquals(1, list.size());

		Comparator<Variant> comp = new VariantByValueComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getVariants(theme, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(variants[i * 10], list.get(0));
			assertEquals(variants[i * 10 + 1], list.get(1));
			assertEquals(variants[i * 10 + 2], list.get(2));
			assertEquals(variants[i * 10 + 3], list.get(3));
			assertEquals(variants[i * 10 + 4], list.get(4));
			assertEquals(variants[i * 10 + 5], list.get(5));
			assertEquals(variants[i * 10 + 6], list.get(6));
			assertEquals(variants[i * 10 + 7], list.get(7));
			assertEquals(variants[i * 10 + 8], list.get(8));
			assertEquals(variants[i * 10 + 9], list.get(9));
		}
		list = index.getVariants(theme, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(variants[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getVariants(org.tmapi.core.Topic[], boolean, int, int)}
	 * .
	 */
	public void testGetVariantsTopicArrayBooleanIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic theme = createTopic();
		Topic other = createTopic();
		Variant[] variants = new Variant[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				if (j % 2 == 0) {
					variants[j] = createTopic().createName("Value", new Topic[0]).createVariant(c + i, theme);
				} else {
					variants[j] = createTopic().createName("Value", new Topic[0]).createVariant(c + i, other);
				}
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Variant> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getVariants(new Topic[] { theme, other }, false, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getVariants(new Topic[] { theme, other }, false, 100, 10);
		assertEquals(1, list.size());

		Comparator<Variant> comp = new VariantByValueComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getVariants(new Topic[] { theme, other }, false, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(variants[i * 10], list.get(0));
			assertEquals(variants[i * 10 + 1], list.get(1));
			assertEquals(variants[i * 10 + 2], list.get(2));
			assertEquals(variants[i * 10 + 3], list.get(3));
			assertEquals(variants[i * 10 + 4], list.get(4));
			assertEquals(variants[i * 10 + 5], list.get(5));
			assertEquals(variants[i * 10 + 6], list.get(6));
			assertEquals(variants[i * 10 + 7], list.get(7));
			assertEquals(variants[i * 10 + 8], list.get(8));
			assertEquals(variants[i * 10 + 9], list.get(9));
		}
		list = index.getVariants(new Topic[] { theme, other }, false, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(variants[100], list.get(0));

		/*
		 * matching all
		 */
		variants = new Variant[101];
		j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				variants[j] = createTopic().createName("Value", new Topic[0]).createVariant(c + i, theme, other);
				j++;
			}
			if (j == 101) {
				break;
			}
		}
		for (int i = 0; i < 10; i++) {
			list = index.getVariants(new Topic[] { theme, other }, true, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getVariants(new Topic[] { theme, other }, true, 100, 10);
		assertEquals(1, list.size());

		for (int i = 0; i < 10; i++) {
			list = index.getVariants(new Topic[] { theme, other }, true, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(variants[i * 10], list.get(0));
			assertEquals(variants[i * 10 + 1], list.get(1));
			assertEquals(variants[i * 10 + 2], list.get(2));
			assertEquals(variants[i * 10 + 3], list.get(3));
			assertEquals(variants[i * 10 + 4], list.get(4));
			assertEquals(variants[i * 10 + 5], list.get(5));
			assertEquals(variants[i * 10 + 6], list.get(6));
			assertEquals(variants[i * 10 + 7], list.get(7));
			assertEquals(variants[i * 10 + 8], list.get(8));
			assertEquals(variants[i * 10 + 9], list.get(9));
		}
		list = index.getVariants(new Topic[] { theme, other }, true, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(variants[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedScopeIndex#getVariants(de.topicmapslab.majortom.model.core.IScope, int, int)}
	 * .
	 */
	public void testGetVariantsIScopeIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		ITopic theme = createTopic();
		Set<ITopic> themes = HashUtil.getHashSet();
		themes.add(theme);
		IScope scope = new ScopeImpl(themes);

		Variant[] variants = new Variant[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				variants[j] = createTopic().createName("Value", new Topic[0]).createVariant(c + i, theme);
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Variant> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getVariants(scope, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getVariants(scope, 100, 10);
		assertEquals(1, list.size());

		Comparator<Variant> comp = new VariantByValueComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getVariants(scope, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(variants[i * 10], list.get(0));
			assertEquals(variants[i * 10 + 1], list.get(1));
			assertEquals(variants[i * 10 + 2], list.get(2));
			assertEquals(variants[i * 10 + 3], list.get(3));
			assertEquals(variants[i * 10 + 4], list.get(4));
			assertEquals(variants[i * 10 + 5], list.get(5));
			assertEquals(variants[i * 10 + 6], list.get(6));
			assertEquals(variants[i * 10 + 7], list.get(7));
			assertEquals(variants[i * 10 + 8], list.get(8));
			assertEquals(variants[i * 10 + 9], list.get(9));
		}
		list = index.getVariants(scope, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(variants[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedIndex#getVariants(java.util.Collection, int, int)}
	 * .
	 */
	public void testGetVariantsCollectionOfIScopeIntInt() {
		IPagedScopedIndex index = topicMap.getIndex(IPagedScopedIndex.class);
		assertNotNull(index);
		try {
			index.getVariantThemes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		ITopic theme = createTopic();
		Set<ITopic> themes = HashUtil.getHashSet();
		themes.add(theme);
		IScope scope = new ScopeImpl(themes);
		ITopic otherTheme = createTopic();
		Set<ITopic> otherThemes = HashUtil.getHashSet();
		otherThemes.add(otherTheme);
		IScope otherScope = new ScopeImpl(otherThemes);
		Variant[] variants = new Variant[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				if (j % 2 == 0) {
					variants[j] = createTopic().createName("value", new Topic[0]).createVariant(c + i, theme);
				} else {
					variants[j] = createTopic().createName("value", new Topic[0]).createVariant(c + i, otherTheme);
				}
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		Collection<IScope> scopes = HashUtil.getHashSet();
		scopes.add(scope);
		scopes.add(otherScope);

		List<Variant> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getVariants(scopes, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getVariants(scopes, 100, 10);
		assertEquals(1, list.size());

		Comparator<Variant> comp = new VariantByValueComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getVariants(scopes, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(variants[i * 10], list.get(0));
			assertEquals(variants[i * 10 + 1], list.get(1));
			assertEquals(variants[i * 10 + 2], list.get(2));
			assertEquals(variants[i * 10 + 3], list.get(3));
			assertEquals(variants[i * 10 + 4], list.get(4));
			assertEquals(variants[i * 10 + 5], list.get(5));
			assertEquals(variants[i * 10 + 6], list.get(6));
			assertEquals(variants[i * 10 + 7], list.get(7));
			assertEquals(variants[i * 10 + 8], list.get(8));
			assertEquals(variants[i * 10 + 9], list.get(9));
		}
		list = index.getVariants(scopes, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(variants[100], list.get(0));
	}

}
