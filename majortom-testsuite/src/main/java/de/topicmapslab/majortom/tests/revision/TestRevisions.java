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
package de.topicmapslab.majortom.tests.revision;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Variant;

import de.topicmapslab.majortom.model.core.IAssociation;
import de.topicmapslab.majortom.model.core.ITopic;
import de.topicmapslab.majortom.model.event.TopicMapEventType;
import de.topicmapslab.majortom.model.index.IRevisionIndex;
import de.topicmapslab.majortom.model.revision.Changeset;
import de.topicmapslab.majortom.model.revision.IRevision;
import de.topicmapslab.majortom.model.revision.IRevisionChange;
import de.topicmapslab.majortom.revision.core.ReadOnlyAssociation;
import de.topicmapslab.majortom.revision.core.ReadOnlyAssociationRole;
import de.topicmapslab.majortom.tests.MaJorToMTestCase;
import de.topicmapslab.majortom.util.FeatureStrings;

/**
 * @author Sven Krosse
 * 
 */
public class TestRevisions extends MaJorToMTestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.topicmapslab.engine.tests.MaJorToMTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factory.setFeature(FeatureStrings.SUPPORT_HISTORY, true);
	}

	public void testTopicRevisions() throws Exception {
		boolean hasFeature = factory.getFeature(FeatureStrings.TOPIC_MAPS_TYPE_INSTANCE_ASSOCIATION);
		IRevisionIndex index = topicMap.getIndex(IRevisionIndex.class);
		index.open();

		if (topicMap.getTopicMapSystem().getFeature(FeatureStrings.SUPPORT_HISTORY)) {

			assertNotNull(index.getLastModification());

			ITopic topic = createTopic();
			topicMap.getStore().commit();
			assertEquals(1, index.getRevisions(topic).size());
			assertEquals(2, index.getRevisions(topic).get(0).getChangeset().size());
			checkChange(index.getRevisions(topic).get(0).getChangeset().get(0), TopicMapEventType.TOPIC_ADDED,
					topicMap, topic, null);
			assertEquals(2, index.getChangeset(topic).size());

			topic.addSubjectIdentifier(topicMap.createLocator("http://psi.exampple.org/topicWithoutII"));
			topicMap.getStore().commit();
			assertEquals(2, index.getRevisions(topic).size());
			assertEquals(1, index.getRevisions(topic).get(1).getChangeset().size());
			assertEquals(3, index.getChangeset(topic).size());

			ITopic type = createTopic();
			topicMap.getStore().commit();
			assertEquals(1, index.getRevisions(type).size());
			assertEquals(2, index.getRevisions(type).get(0).getChangeset().size());

			topic.addType(type);
			topicMap.getStore().commit();
			assertEquals(3, index.getRevisions(topic).size());

			int cntRev = 1, cntCS = 4;
			if (hasFeature) {
				cntRev += 6;
				cntCS++;
			}
			assertEquals(cntRev, index.getRevisions(topic).get(2).getChangeset().size());
			assertEquals(cntCS, index.getChangeset(topic).size());
			assertEquals(2, index.getRevisions(type).size());
			assertEquals(cntRev, index.getRevisions(type).get(1).getChangeset().size());
			assertEquals(hasFeature ? 4 : 3, index.getChangeset(type).size());
			topic.createName(type, "Name", new Topic[0]);
			topicMap.getStore().commit();
			assertEquals(hasFeature ? 5 : 4, index.getChangeset(type).size());
			cntCS += 4;
			assertEquals(cntCS, index.getChangeset(topic).size());
		}
	}

	public void testLastModification() throws Exception {
		IRevisionIndex index = topicMap.getIndex(IRevisionIndex.class);
		index.open();

		assertNotNull(index.getLastModification());

		ITopic topicWithoutIdentifier = createTopic();
		topicMap.getStore().commit();
		Calendar calendar = new GregorianCalendar();
		Calendar lastModification = index.getLastModification();
		assertNotNull(lastModification);
		assertEquals(calendar.getTimeInMillis(), lastModification.getTimeInMillis(), 20);

		Calendar lastModificationOrTopic = index.getLastModification(topicWithoutIdentifier);
		assertNotNull(lastModificationOrTopic);
		assertEquals(calendar.getTimeInMillis(), lastModificationOrTopic.getTimeInMillis(), 20);

		topicWithoutIdentifier.addSubjectIdentifier(topicMap.createLocator("http://psi.exampple.org/topicWithoutII"));
		topicMap.getStore().commit();

		assertNotSame(lastModificationOrTopic, index.getLastModification(topicWithoutIdentifier));
		assertNotSame(lastModification, index.getLastModification());
	}

	public void checkChange(IRevisionChange change, TopicMapEventType type, Construct context, Object newValue,
			Object oldValue) {
		assertEquals(type, change.getType());
		assertEquals(context, change.getContext());
		assertEquals(newValue, change.getNewValue());
		assertEquals(oldValue, change.getOldValue());
	}

	public void testTopicRemovedRevision() throws Exception {
		ITopic topic = createTopic();
		ITopic type = createTopic();
		IAssociation association = createAssociation(createTopic());
		Role role = association.createRole(type, topic);

		topic.remove(true);
		topicMap.getStore().commit();
		
		IRevisionIndex index = topicMap.getIndex(IRevisionIndex.class);
		index.open();
		IRevision revision = index.getLastRevision();
		assertEquals(3, revision.getChangeset().size());
		Changeset set = revision.getChangeset();
		assertEquals(topic, set.get(2).getOldValue());
		ITopic clone = (ITopic) set.get(2).getOldValue();
		assertEquals(1, clone.getRolesPlayed().size());
		assertTrue(clone.getRolesPlayed().contains(role));
		assertEquals(1, clone.getRolesPlayed(type).size());
		assertTrue(clone.getRolesPlayed(type).contains(role));
		assertEquals(1, clone.getAssociationsPlayed().size());
		assertTrue(clone.getAssociationsPlayed().contains(association));
	}

	public void testRoleRevisions() throws Exception {
		IRevisionIndex index = topicMap.getIndex(IRevisionIndex.class);
		index.open();

		IAssociation association = createAssociation(createTopic());
		ITopic player = createTopic();
		ITopic type = createTopic();
		Role role = association.createRole(type, player);
		role.remove();
		topicMap.getStore().commit();
		
		IRevision revision = index.getLastRevision();
		assertNotNull(revision);
		assertFalse(revision.getChangeset().isEmpty());
		IRevisionChange change = revision.getChangeset().get(0);
		checkChange(change, TopicMapEventType.ROLE_REMOVED, association, null, role);
		role = (Role) change.getOldValue();
		assertTrue(role instanceof ReadOnlyAssociationRole);
		assertEquals(type, role.getType());
		assertEquals(player, role.getPlayer());
	}

	public void testAssociationRevisions() throws Exception {
		IRevisionIndex index = topicMap.getIndex(IRevisionIndex.class);
		index.open();

		ITopic assoicationType = createTopic();
		IAssociation association = createAssociation(assoicationType);
		ITopic player = createTopic();
		ITopic type = createTopic();
		Role role = association.createRole(type, player);
		ITopic otherPlayer = createTopic();
		ITopic otherType = createTopic();
		Role otherRole = association.createRole(otherType, otherPlayer);
		topicMap.getStore().commit();

		Changeset set = index.getAssociationChangeset(assoicationType);
		assertEquals(5, set.size());

		IAssociation other = createAssociation(assoicationType);
		topicMap.getStore().commit();
		set = index.getAssociationChangeset(assoicationType);
		assertEquals(8, set.size());

		other.createRole(createTopic(), createTopic());
		topicMap.getStore().commit();
		set = index.getAssociationChangeset(assoicationType);
		assertEquals(9, set.size());

		association.remove();
		topicMap.getStore().commit();

		IRevision revision = index.getLastRevision();
		assertNotNull(revision);
		assertEquals(3, revision.getChangeset().size());
		IRevisionChange change = revision.getChangeset().get(0);
		assertEquals(TopicMapEventType.ROLE_REMOVED, change.getType());
		assertEquals(association, change.getContext());
		assertNull(change.getNewValue());
		assertTrue(otherRole.equals(change.getOldValue()) || role.equals(change.getOldValue()));

		change = revision.getChangeset().get(1);
		assertEquals(TopicMapEventType.ROLE_REMOVED, change.getType());
		assertEquals(association, change.getContext());
		assertNull(change.getNewValue());
		assertTrue(otherRole.equals(change.getOldValue()) || role.equals(change.getOldValue()));

		change = revision.getChangeset().get(2);
		checkChange(change, TopicMapEventType.ASSOCIATION_REMOVED, topicMap, null, association);
		association = (IAssociation) change.getOldValue();
		assertTrue(association instanceof ReadOnlyAssociation);
		assertEquals(2, association.getRoles().size());
		assertTrue(association.getRoles().contains(otherRole));
		assertTrue(association.getRoles().contains(role));

		set = index.getAssociationChangeset(assoicationType);

	}

	public void testRemovingTypeInstanceRelation() throws Exception {
		boolean hasFeature = factory.getFeature(FeatureStrings.TOPIC_MAPS_TYPE_INSTANCE_ASSOCIATION);
		ITopic topic = createTopic();
		ITopic other = createTopic();

		topic.addType(other);
		topicMap.getStore().commit();

		assertEquals(1, topic.getTypes().size());
		assertTrue(topic.getTypes().contains(other));
		int cnt = hasFeature ? 1 : 0;

		assertEquals(cnt, topic.getAssociationsPlayed().size());
		if (hasFeature) {
			Association a = topic.getAssociationsPlayed().iterator().next();
			a.remove();
			topicMap.getStore().commit();
			cnt--;
		}

		assertEquals(cnt, topic.getAssociationsPlayed().size());
		assertEquals(1, topic.getTypes().size());
		assertTrue(topic.getTypes().contains(other));
		topic.remove();
		topicMap.getStore().commit();

		IRevisionIndex index = topicMap.getIndex(IRevisionIndex.class);
		index.open();

		topic = createTopic();
		topic.addType(other);
		topicMap.getStore().commit();

		assertEquals(1, topic.getTypes().size());
		assertTrue(topic.getTypes().contains(other));
		cnt = hasFeature ? cnt + 1 : cnt;
		assertEquals(cnt, topic.getAssociationsPlayed().size());
		assertEquals(1, topic.getTypes().size());
		assertTrue(topic.getTypes().contains(other));
		topic.remove();
	}

	public void testMetaData() {
		createTopic();
		topicMap.getStore().commit();
		IRevisionIndex index = topicMap.getIndex(IRevisionIndex.class);
		index.open();
		IRevision revision = index.getLastRevision();
		assertNotNull(revision);

		for (long l = 1; l < 100; l++) {
			revision.addMetaData("key#" + Long.toString(l), "value#" + Long.toString(l));
			assertEquals("value#" + Long.toString(l), revision.getMetaData("key#" + Long.toString(l)));
			assertEquals(l, revision.getMetadata().size());
		}

		for (long l = 1; l < 100; l++) {
			revision.addMetaData("key#" + Long.toString(l), "new#" + Long.toString(l));
			assertEquals("new#" + Long.toString(l), revision.getMetaData("key#" + Long.toString(l)));
			assertEquals("Number of meta-data should be keep constants because of overwrite key", 99, revision
					.getMetadata().size());
		}
	}

	public void testChangesetType() throws Exception {
		if (factory.getFeature(FeatureStrings.SUPPORT_HISTORY)) {
			IRevisionIndex index = topicMap.getIndex(IRevisionIndex.class);
			index.open();

			Topic t = createTopic();
			topicMap.getStore().commit();

			IRevision r = index.getLastRevision();
			assertNotNull(r);
			assertEquals(TopicMapEventType.TOPIC_ADDED, r.getChangesetType());

			Name n = t.createName("Name");
			topicMap.getStore().commit();
			r = index.getLastRevision();
			assertNotNull(r);
			assertEquals(TopicMapEventType.NAME_ADDED, r.getChangesetType());

			Variant v = n.createVariant("Name", createTopic());
			topicMap.getStore().commit();
			r = index.getLastRevision();
			assertNotNull(r);
			assertEquals(TopicMapEventType.VARIANT_ADDED, r.getChangesetType());

			v.remove();
			topicMap.getStore().commit();
			r = index.getLastRevision();
			assertNotNull(r);
			assertEquals(TopicMapEventType.VARIANT_REMOVED, r.getChangesetType());

			n.remove();
			topicMap.getStore().commit();
			r = index.getLastRevision();
			assertNotNull(r);
			assertEquals(TopicMapEventType.NAME_REMOVED, r.getChangesetType());

			Occurrence o = t.createOccurrence(createTopic(), "Value");
			topicMap.getStore().commit();
			r = index.getLastRevision();
			assertNotNull(r);
			assertEquals(TopicMapEventType.OCCURRENCE_ADDED, r.getChangesetType());

			o.remove();
			topicMap.getStore().commit();
			r = index.getLastRevision();
			assertNotNull(r);
			assertEquals(TopicMapEventType.OCCURRENCE_REMOVED, r.getChangesetType());

			Association a = createAssociation(createTopic());
			topicMap.getStore().commit();
			r = index.getLastRevision();
			assertNotNull(r);
			assertEquals(TopicMapEventType.ASSOCIATION_ADDED, r.getChangesetType());

			Role role = a.createRole(createTopic(), createTopic());
			System.out.println(role.getId());
			topicMap.getStore().commit();
			System.out.println(role.getId());
			r = index.getLastRevision();
			assertNotNull(r);
			assertEquals(TopicMapEventType.ROLE_ADDED, r.getChangesetType());

			role.remove();
			topicMap.getStore().commit();
			r = index.getLastRevision();
			assertNotNull(r);
			assertEquals(TopicMapEventType.ROLE_REMOVED, r.getChangesetType());

			a.remove();
			topicMap.getStore().commit();
			r = index.getLastRevision();
			assertNotNull(r);
			assertEquals(TopicMapEventType.ASSOCIATION_REMOVED, r.getChangesetType());
		}
	}
	
	public void testTopicMapRevision() throws Exception{
		if (factory.getFeature(FeatureStrings.SUPPORT_HISTORY)) {
			IRevisionIndex index = topicMap.getIndex(IRevisionIndex.class);
			index.open();
			
			/*
			 * first revision may not be null 
			 */
			if ( topicMap.getStore().isRevisionManagementEnabled()){
				assertNotNull(index.getFirstRevision());
				IRevision r = index.getFirstRevision();
				assertEquals(TopicMapEventType.TOPIC_MAP_CREATED,r.getChangesetType());
				assertEquals(1, r.getChangeset().size());
				IRevisionChange diff = r.getChangeset().getFirst();
				assertEquals(topicMap, diff.getContext());
				assertEquals(TopicMapEventType.TOPIC_MAP_CREATED, diff.getType());
				assertEquals(topicMap, diff.getNewValue());
				assertNull(diff.getOldValue());
			}
		}
	}
	
	public void testMergeInRevision() throws Exception{
		createTopicBySI("http://psi.example.org/mySi1").createName("Name");
		createTopicBySI("http://psi.example.org/mySi2").createName("Name");
		createTopic().createName("Name");
		createTopic().createName("Name");
		
		TopicMap other = topicMapSystem.createTopicMap("http://psi.example.org/newTopicMap");
		other.createTopicBySubjectIdentifier(other.createLocator("http://psi.example.org/mySi3")).createName("Name");
		other.createTopic().createName("Name");
		other.createTopic().createName("Name");
		other.createTopic().createName("Name");
		
		IRevisionIndex index = topicMap.getIndex(IRevisionIndex.class);
		index.open();
		
		IRevision r = index.getLastRevision();
		assertNotNull(r);
		assertNull(r.getFuture());
		topicMap.mergeIn(other);
		
		IRevision oIRevision =r.getFuture();
		assertNotNull(oIRevision);
		assertNull(oIRevision.getFuture());		
	}
	
	public void testRemoveDuplicatesRevision() throws Exception{
		Topic t = createTopic();
		for ( int i = 0 ; i < 10 ; i++ ){
			t.createName("Name").createVariant("Variant", createTopic());
		}
		assertEquals(10, t.getNames().size());
		for ( Name n : t.getNames()){
			assertEquals(1, n.getVariants().size());
		}
		
		IRevisionIndex index = topicMap.getIndex(IRevisionIndex.class);
		index.open();
		
		IRevision r = index.getLastRevision();
		assertNotNull(r);
		assertNull(r.getFuture());
		topicMap.removeDuplicates();		
		r = r.getFuture();
		assertNotNull(r);
		assertNull(r.getFuture());
		assertEquals(TopicMapEventType.REMOVE_DUPLICATES, r.getChangesetType());
		assertEquals(1, t.getNames().size());
		assertEquals(10, t.getNames().iterator().next().getVariants().size());
	}
}
