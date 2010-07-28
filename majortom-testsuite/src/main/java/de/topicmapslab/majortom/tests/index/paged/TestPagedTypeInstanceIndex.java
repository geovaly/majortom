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

import org.tmapi.core.Association;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;

import de.topicmapslab.majortom.comparator.TopicByIdentityComparator;
import de.topicmapslab.majortom.model.core.ICharacteristics;
import de.topicmapslab.majortom.model.core.IName;
import de.topicmapslab.majortom.model.core.IOccurrence;
import de.topicmapslab.majortom.model.index.paging.IPagedTypeInstanceIndex;
import de.topicmapslab.majortom.tests.MaJorToMTestCase;
import de.topicmapslab.majortom.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TestPagedTypeInstanceIndex extends MaJorToMTestCase {

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getAssociationTypes(int, int)}
	 * .
	 */
	public void testGetAssociationTypesIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		String base = "http://psi.example.org/topics/";
		Topic[] topics = new Topic[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				topics[j] = createTopicBySI(base + c + i);
				createAssociation(topics[j]);
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Topic> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getAssociationTypes(i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getAssociationTypes(100, 10);
		assertEquals(1, list.size());

		Comparator<Topic> comp = new TopicByIdentityComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getAssociationTypes(i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(topics[i * 10], list.get(0));
			assertEquals(topics[i * 10 + 1], list.get(1));
			assertEquals(topics[i * 10 + 2], list.get(2));
			assertEquals(topics[i * 10 + 3], list.get(3));
			assertEquals(topics[i * 10 + 4], list.get(4));
			assertEquals(topics[i * 10 + 5], list.get(5));
			assertEquals(topics[i * 10 + 6], list.get(6));
			assertEquals(topics[i * 10 + 7], list.get(7));
			assertEquals(topics[i * 10 + 8], list.get(8));
			assertEquals(topics[i * 10 + 9], list.get(9));
		}
		list = index.getAssociationTypes(100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(topics[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getAssociations(org.tmapi.core.Topic, int, int)}
	 * .
	 */
	public void testGetAssociationsTopicIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic type = createTopic();
		String base = "http://psi.example.org/topics/";
		Association[] associations = new Association[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				associations[j] = createAssociation(type);
				associations[j].addItemIdentifier(createLocator(base + c + i));
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Association> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getAssociations(type, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getAssociations(type, 100, 10);
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
			list = index.getAssociations(type, i * 10, 10, comp);
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
		list = index.getAssociations(type, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(associations[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getAssociations(java.util.Collection, int, int)}
	 * .
	 */
	public void testGetAssociationsCollectionOfQextendsTopicIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic type = createTopic();
		Topic otherType = createTopic();
		String base = "http://psi.example.org/topics/";
		Association[] associations = new Association[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				if (i % 2 == 0) {
					associations[j] = createAssociation(type);
				} else {
					associations[j] = createAssociation(otherType);
				}
				associations[j].addItemIdentifier(createLocator(base + c + i));
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		Collection<Topic> types = HashUtil.getHashSet();
		types.add(type);
		types.add(otherType);

		List<Association> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getAssociations(types, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getAssociations(types, 100, 10);
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
			list = index.getAssociations(types, i * 10, 10, comp);
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
		list = index.getAssociations(types, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(associations[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getCharacteristicTypes(int, int)}
	 * .
	 */
	public void testGetCharacteristicTypesIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		String base = "http://psi.example.org/topics/";
		Topic[] topics = new Topic[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				topics[j] = createTopicBySI(base + c + i);
				if (j % 2 == 0) {
					createTopic().createName(topics[j], "Name", new Topic[0]);
				} else {
					createTopic().createOccurrence(topics[j], "Occ", new Topic[0]);
				}
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Topic> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getCharacteristicTypes(i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getCharacteristicTypes(100, 10);
		assertEquals(1, list.size());

		Comparator<Topic> comp = new TopicByIdentityComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getCharacteristicTypes(i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(topics[i * 10], list.get(0));
			assertEquals(topics[i * 10 + 1], list.get(1));
			assertEquals(topics[i * 10 + 2], list.get(2));
			assertEquals(topics[i * 10 + 3], list.get(3));
			assertEquals(topics[i * 10 + 4], list.get(4));
			assertEquals(topics[i * 10 + 5], list.get(5));
			assertEquals(topics[i * 10 + 6], list.get(6));
			assertEquals(topics[i * 10 + 7], list.get(7));
			assertEquals(topics[i * 10 + 8], list.get(8));
			assertEquals(topics[i * 10 + 9], list.get(9));
		}
		list = index.getCharacteristicTypes(100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(topics[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getCharacteristics(org.tmapi.core.Topic, int, int)}
	 * .
	 */
	public void testGetCharacteristicsTopicIntInt() {

		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic type = createTopic();
		String base = "http://psi.example.org/topics/";
		ICharacteristics[] characteristics = new ICharacteristics[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				if (j % 2 == 0) {
					characteristics[j] = (IName) createTopic().createName(type, "Value", new Topic[0]);
				} else {
					characteristics[j] = (IOccurrence) createTopic().createOccurrence(type, "Value", new Topic[0]);
				}
				characteristics[j].addItemIdentifier(createLocator(base + c + i));
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<ICharacteristics> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getCharacteristics(type, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getCharacteristics(type, 100, 10);
		assertEquals(1, list.size());

		Comparator<ICharacteristics> comp = new Comparator<ICharacteristics>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(ICharacteristics o1, ICharacteristics o2) {
				return o1.getItemIdentifiers().iterator().next().getReference().compareTo(o2.getItemIdentifiers().iterator().next().getReference());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getCharacteristics(type, i * 10, 10, comp);
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
		list = index.getCharacteristics(type, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(characteristics[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getCharacteristics(java.util.Collection, int, int)}
	 * .
	 */
	public void testGetCharacteristicsCollectionOfQextendsTopicIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic type = createTopic();
		Topic otherType = createTopic();
		String base = "http://psi.example.org/topics/";
		ICharacteristics[] characteristics = new ICharacteristics[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				if (j % 2 == 0) {
					characteristics[j] = (IName) createTopic().createName(type, "Value", new Topic[0]);
				} else {
					characteristics[j] = (IOccurrence) createTopic().createOccurrence(otherType, "Value", new Topic[0]);
				}
				characteristics[j].addItemIdentifier(createLocator(base + c + i));
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		Collection<Topic> types = HashUtil.getHashSet();
		types.add(type);
		types.add(otherType);

		List<ICharacteristics> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getCharacteristics(types, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getCharacteristics(types, 100, 10);
		assertEquals(1, list.size());

		Comparator<ICharacteristics> comp = new Comparator<ICharacteristics>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(ICharacteristics o1, ICharacteristics o2) {
				return o1.getItemIdentifiers().iterator().next().getReference().compareTo(o2.getItemIdentifiers().iterator().next().getReference());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getCharacteristics(types, i * 10, 10, comp);
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
		list = index.getCharacteristics(types, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(characteristics[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getNameTypes(int, int)}
	 * .
	 */
	public void testGetNameTypesIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		String base = "http://psi.example.org/topics/";
		Topic[] topics = new Topic[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				topics[j] = createTopicBySI(base + c + i);

				createTopic().createName(topics[j], "Name", new Topic[0]);
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Topic> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getNameTypes(i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getNameTypes(100, 10);
		assertEquals(1, list.size());

		Comparator<Topic> comp = new TopicByIdentityComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getNameTypes(i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(topics[i * 10], list.get(0));
			assertEquals(topics[i * 10 + 1], list.get(1));
			assertEquals(topics[i * 10 + 2], list.get(2));
			assertEquals(topics[i * 10 + 3], list.get(3));
			assertEquals(topics[i * 10 + 4], list.get(4));
			assertEquals(topics[i * 10 + 5], list.get(5));
			assertEquals(topics[i * 10 + 6], list.get(6));
			assertEquals(topics[i * 10 + 7], list.get(7));
			assertEquals(topics[i * 10 + 8], list.get(8));
			assertEquals(topics[i * 10 + 9], list.get(9));
		}
		list = index.getNameTypes(100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(topics[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getNames(org.tmapi.core.Topic, int, int)}
	 * .
	 */
	public void testGetNamesTopicIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic type = createTopic();
		String base = "http://psi.example.org/topics/";
		Name[] names = new Name[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				names[j] = createTopic().createName(type, "Value", new Topic[0]);
				names[j].addItemIdentifier(createLocator(base + c + i));
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Name> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getNames(type, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getNames(type, 100, 10);
		assertEquals(1, list.size());

		Comparator<Name> comp = new Comparator<Name>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(Name o1, Name o2) {
				return o1.getItemIdentifiers().iterator().next().getReference().compareTo(o2.getItemIdentifiers().iterator().next().getReference());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getNames(type, i * 10, 10, comp);
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
		list = index.getNames(type, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(names[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getNames(java.util.Collection, int, int)}
	 * .
	 */
	public void testGetNamesCollectionOfQextendsTopicIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic type = createTopic();
		Topic otherType = createTopic();
		String base = "http://psi.example.org/topics/";
		Name[] names = new Name[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				if (j % 2 == 0) {
					names[j] = createTopic().createName(type, "Value", new Topic[0]);
				} else {
					names[j] = createTopic().createName(otherType, "Value", new Topic[0]);
				}
				names[j].addItemIdentifier(createLocator(base + c + i));
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		Collection<Topic> types = HashUtil.getHashSet();
		types.add(type);
		types.add(otherType);

		List<Name> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getNames(types, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getNames(types, 100, 10);
		assertEquals(1, list.size());

		Comparator<Name> comp = new Comparator<Name>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(Name o1, Name o2) {
				return o1.getItemIdentifiers().iterator().next().getReference().compareTo(o2.getItemIdentifiers().iterator().next().getReference());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getNames(types, i * 10, 10, comp);
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
		list = index.getNames(types, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(names[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getOccurrenceTypes(int, int)}
	 * .
	 */
	public void testGetOccurrenceTypesIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		String base = "http://psi.example.org/topics/";
		Topic[] topics = new Topic[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				topics[j] = createTopicBySI(base + c + i);
				createTopic().createOccurrence(topics[j], "Occ", new Topic[0]);
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Topic> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getOccurrenceTypes(i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getOccurrenceTypes(100, 10);
		assertEquals(1, list.size());

		Comparator<Topic> comp = new TopicByIdentityComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getOccurrenceTypes(i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(topics[i * 10], list.get(0));
			assertEquals(topics[i * 10 + 1], list.get(1));
			assertEquals(topics[i * 10 + 2], list.get(2));
			assertEquals(topics[i * 10 + 3], list.get(3));
			assertEquals(topics[i * 10 + 4], list.get(4));
			assertEquals(topics[i * 10 + 5], list.get(5));
			assertEquals(topics[i * 10 + 6], list.get(6));
			assertEquals(topics[i * 10 + 7], list.get(7));
			assertEquals(topics[i * 10 + 8], list.get(8));
			assertEquals(topics[i * 10 + 9], list.get(9));
		}
		list = index.getOccurrenceTypes(100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(topics[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getOccurrences(org.tmapi.core.Topic, int, int)}
	 * .
	 */
	public void testGetOccurrencesTopicIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic type = createTopic();
		String base = "http://psi.example.org/topics/";
		Occurrence[] occurrences = new Occurrence[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				occurrences[j] = createTopic().createOccurrence(type, "Value", new Topic[0]);
				occurrences[j].addItemIdentifier(createLocator(base + c + i));
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Occurrence> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getOccurrences(type, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getOccurrences(type, 100, 10);
		assertEquals(1, list.size());

		Comparator<Occurrence> comp = new Comparator<Occurrence>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(Occurrence o1, Occurrence o2) {
				return o1.getItemIdentifiers().iterator().next().getReference().compareTo(o2.getItemIdentifiers().iterator().next().getReference());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getOccurrences(type, i * 10, 10, comp);
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
		list = index.getOccurrences(type, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(occurrences[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getOccurrences(java.util.Collection, int, int)}
	 * .
	 */
	public void testGetOccurrencesCollectionOfQextendsTopicIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic type = createTopic();
		Topic otherType = createTopic();
		String base = "http://psi.example.org/topics/";
		Occurrence[] occurrences = new Occurrence[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				if (j % 2 == 0) {
					occurrences[j] = createTopic().createOccurrence(type, "Value", new Topic[0]);
				} else {
					occurrences[j] = createTopic().createOccurrence(otherType, "Value", new Topic[0]);
				}
				occurrences[j].addItemIdentifier(createLocator(base + c + i));
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		Collection<Topic> types = HashUtil.getHashSet();
		types.add(type);
		types.add(otherType);

		List<Occurrence> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getOccurrences(types, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getOccurrences(types, 100, 10);
		assertEquals(1, list.size());

		Comparator<Occurrence> comp = new Comparator<Occurrence>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(Occurrence o1, Occurrence o2) {
				return o1.getItemIdentifiers().iterator().next().getReference().compareTo(o2.getItemIdentifiers().iterator().next().getReference());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getOccurrences(types, i * 10, 10, comp);
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
		list = index.getOccurrences(types, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(occurrences[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getRoleTypes(int, int)}
	 * .
	 */
	public void testGetRoleTypesIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		String base = "http://psi.example.org/topics/";
		Topic[] topics = new Topic[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				topics[j] = createTopicBySI(base + c + i);
				createAssociation(createTopic()).createRole(topics[j], createTopic());
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Topic> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getRoleTypes(i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getRoleTypes(100, 10);
		assertEquals(1, list.size());

		Comparator<Topic> comp = new TopicByIdentityComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getRoleTypes(i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(topics[i * 10], list.get(0));
			assertEquals(topics[i * 10 + 1], list.get(1));
			assertEquals(topics[i * 10 + 2], list.get(2));
			assertEquals(topics[i * 10 + 3], list.get(3));
			assertEquals(topics[i * 10 + 4], list.get(4));
			assertEquals(topics[i * 10 + 5], list.get(5));
			assertEquals(topics[i * 10 + 6], list.get(6));
			assertEquals(topics[i * 10 + 7], list.get(7));
			assertEquals(topics[i * 10 + 8], list.get(8));
			assertEquals(topics[i * 10 + 9], list.get(9));
		}
		list = index.getRoleTypes(100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(topics[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getRoles(org.tmapi.core.Topic, int, int)}
	 * .
	 */
	public void testGetRolesTopicIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic type = createTopic();
		String base = "http://psi.example.org/topics/";
		Role[] roles = new Role[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				roles[j] = createAssociation(createTopic()).createRole(type, createTopic());
				roles[j].addItemIdentifier(createLocator(base + c + i));
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Role> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getRoles(type, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getRoles(type, 100, 10);
		assertEquals(1, list.size());

		Comparator<Role> comp = new Comparator<Role>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(Role o1, Role o2) {
				return o1.getItemIdentifiers().iterator().next().getReference().compareTo(o2.getItemIdentifiers().iterator().next().getReference());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getRoles(type, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(roles[i * 10], list.get(0));
			assertEquals(roles[i * 10 + 1], list.get(1));
			assertEquals(roles[i * 10 + 2], list.get(2));
			assertEquals(roles[i * 10 + 3], list.get(3));
			assertEquals(roles[i * 10 + 4], list.get(4));
			assertEquals(roles[i * 10 + 5], list.get(5));
			assertEquals(roles[i * 10 + 6], list.get(6));
			assertEquals(roles[i * 10 + 7], list.get(7));
			assertEquals(roles[i * 10 + 8], list.get(8));
			assertEquals(roles[i * 10 + 9], list.get(9));
		}
		list = index.getRoles(type, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(roles[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getRoles(java.util.Collection, int, int)}
	 * .
	 */
	public void testGetRolesCollectionOfQextendsTopicIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic type = createTopic();
		Topic otherType = createTopic();
		String base = "http://psi.example.org/topics/";
		Role[] roles = new Role[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				if (j % 2 == 0) {
					roles[j] = createAssociation(createTopic()).createRole(type, createTopic());
				} else {
					roles[j] = createAssociation(createTopic()).createRole(otherType, createTopic());
				}
				roles[j].addItemIdentifier(createLocator(base + c + i));
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		Collection<Topic> types = HashUtil.getHashSet();
		types.add(type);
		types.add(otherType);

		List<Role> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getRoles(types, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getRoles(types, 100, 10);
		assertEquals(1, list.size());

		Comparator<Role> comp = new Comparator<Role>() {
			/**
			 * {@inheritDoc}
			 */
			public int compare(Role o1, Role o2) {
				return o1.getItemIdentifiers().iterator().next().getReference().compareTo(o2.getItemIdentifiers().iterator().next().getReference());
			}
		};

		for (int i = 0; i < 10; i++) {
			list = index.getRoles(types, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(roles[i * 10], list.get(0));
			assertEquals(roles[i * 10 + 1], list.get(1));
			assertEquals(roles[i * 10 + 2], list.get(2));
			assertEquals(roles[i * 10 + 3], list.get(3));
			assertEquals(roles[i * 10 + 4], list.get(4));
			assertEquals(roles[i * 10 + 5], list.get(5));
			assertEquals(roles[i * 10 + 6], list.get(6));
			assertEquals(roles[i * 10 + 7], list.get(7));
			assertEquals(roles[i * 10 + 8], list.get(8));
			assertEquals(roles[i * 10 + 9], list.get(9));
		}
		list = index.getRoles(types, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(roles[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getTopicTypes(int, int)}
	 * .
	 */
	public void testGetTopicTypesIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		String base = "http://psi.example.org/topics/";
		Topic[] topics = new Topic[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				topics[j] = createTopicBySI(base + c + i);
				createTopic().addType(topics[j]);
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Topic> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getTopicTypes(i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getTopicTypes(100, 10);
		assertEquals(1, list.size());

		Comparator<Topic> comp = new TopicByIdentityComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getTopicTypes(i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(topics[i * 10], list.get(0));
			assertEquals(topics[i * 10 + 1], list.get(1));
			assertEquals(topics[i * 10 + 2], list.get(2));
			assertEquals(topics[i * 10 + 3], list.get(3));
			assertEquals(topics[i * 10 + 4], list.get(4));
			assertEquals(topics[i * 10 + 5], list.get(5));
			assertEquals(topics[i * 10 + 6], list.get(6));
			assertEquals(topics[i * 10 + 7], list.get(7));
			assertEquals(topics[i * 10 + 8], list.get(8));
			assertEquals(topics[i * 10 + 9], list.get(9));
		}
		list = index.getTopicTypes(100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(topics[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getTopics(org.tmapi.core.Topic, int, int)}
	 * .
	 */
	public void testGetTopicsTopicIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic type = createTopic();
		String base = "http://psi.example.org/topics/";
		Topic[] topics = new Topic[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				topics[j] = createTopicBySI(base + c + i);
				topics[j].addType(type);
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		List<Topic> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getTopics(type, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getTopics(type, 100, 10);
		assertEquals(1, list.size());

		Comparator<Topic> comp = new TopicByIdentityComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getTopics(type, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(topics[i * 10], list.get(0));
			assertEquals(topics[i * 10 + 1], list.get(1));
			assertEquals(topics[i * 10 + 2], list.get(2));
			assertEquals(topics[i * 10 + 3], list.get(3));
			assertEquals(topics[i * 10 + 4], list.get(4));
			assertEquals(topics[i * 10 + 5], list.get(5));
			assertEquals(topics[i * 10 + 6], list.get(6));
			assertEquals(topics[i * 10 + 7], list.get(7));
			assertEquals(topics[i * 10 + 8], list.get(8));
			assertEquals(topics[i * 10 + 9], list.get(9));
		}
		list = index.getTopics(type, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(topics[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getTopics(java.util.Collection, int, int)}
	 * .
	 */
	public void testGetTopicsCollectionOfTopicIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic type = createTopic();
		Topic otherType = createTopic();
		String base = "http://psi.example.org/topics/";
		Topic[] topics = new Topic[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				topics[j] = createTopicBySI(base + c + i);
				if (j % 2 == 0) {
					topics[j].addType(type);
				} else {
					topics[j].addType(otherType);
				}
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		Collection<Topic> types = HashUtil.getHashSet();
		types.add(type);
		types.add(otherType);

		List<Topic> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getTopics(types, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getTopics(types, 100, 10);
		assertEquals(1, list.size());

		Comparator<Topic> comp = new TopicByIdentityComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getTopics(types, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(topics[i * 10], list.get(0));
			assertEquals(topics[i * 10 + 1], list.get(1));
			assertEquals(topics[i * 10 + 2], list.get(2));
			assertEquals(topics[i * 10 + 3], list.get(3));
			assertEquals(topics[i * 10 + 4], list.get(4));
			assertEquals(topics[i * 10 + 5], list.get(5));
			assertEquals(topics[i * 10 + 6], list.get(6));
			assertEquals(topics[i * 10 + 7], list.get(7));
			assertEquals(topics[i * 10 + 8], list.get(8));
			assertEquals(topics[i * 10 + 9], list.get(9));
		}
		list = index.getTopics(types, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(topics[100], list.get(0));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.inmemory.index.paged.InMemoryPagedTypeInstanceIndex#getTopics(java.util.Collection, boolean, int, int)}
	 * .
	 */
	public void testGetTopicsCollectionOfTopicBooleanIntInt() {
		IPagedTypeInstanceIndex index = topicMap.getIndex(IPagedTypeInstanceIndex.class);
		assertNotNull(index);
		try {
			index.getTopicTypes(0, 0);
			fail("Index should be closed!");
		} catch (Exception e) {
			index.open();
		}
		Topic type = createTopic();
		Topic otherType = createTopic();
		String base = "http://psi.example.org/topics/";
		Topic[] topics = new Topic[101];
		int j = 0;
		for (String c : new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" }) {
			for (int i = 0; i < 10 && j < 101; i++) {
				topics[j] = createTopicBySI(base + c + i);
				topics[j].addType(type);
				topics[j].addType(otherType);
				j++;
			}
			if (j == 101) {
				break;
			}
		}

		Collection<Topic> types = HashUtil.getHashSet();
		types.add(type);
		types.add(otherType);

		List<Topic> list = null;
		for (int i = 0; i < 10; i++) {
			list = index.getTopics(types, true, i * 10, 10);
			assertEquals(10, list.size());
		}
		list = index.getTopics(types, true, 100, 10);
		assertEquals(1, list.size());

		Comparator<Topic> comp = new TopicByIdentityComparator(true);

		for (int i = 0; i < 10; i++) {
			list = index.getTopics(types, true, i * 10, 10, comp);
			assertEquals(10, list.size());
			assertEquals(topics[i * 10], list.get(0));
			assertEquals(topics[i * 10 + 1], list.get(1));
			assertEquals(topics[i * 10 + 2], list.get(2));
			assertEquals(topics[i * 10 + 3], list.get(3));
			assertEquals(topics[i * 10 + 4], list.get(4));
			assertEquals(topics[i * 10 + 5], list.get(5));
			assertEquals(topics[i * 10 + 6], list.get(6));
			assertEquals(topics[i * 10 + 7], list.get(7));
			assertEquals(topics[i * 10 + 8], list.get(8));
			assertEquals(topics[i * 10 + 9], list.get(9));
		}
		list = index.getTopics(types, true, 100, 10, comp);
		assertEquals(1, list.size());
		assertEquals(topics[100], list.get(0));
	}
}
