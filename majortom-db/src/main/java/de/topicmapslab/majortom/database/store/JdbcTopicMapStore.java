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
package de.topicmapslab.majortom.database.store;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.tmapi.core.Locator;
import org.tmapi.core.TopicMap;
import org.tmapi.index.Index;

import de.topicmapslab.majortom.core.ConstructImpl;
import de.topicmapslab.majortom.database.jdbc.core.ConnectionProviderFactory;
import de.topicmapslab.majortom.database.jdbc.model.IConnectionProvider;
import de.topicmapslab.majortom.model.core.IAssociation;
import de.topicmapslab.majortom.model.core.IAssociationRole;
import de.topicmapslab.majortom.model.core.ICharacteristics;
import de.topicmapslab.majortom.model.core.IConstruct;
import de.topicmapslab.majortom.model.core.IDatatypeAware;
import de.topicmapslab.majortom.model.core.ILocator;
import de.topicmapslab.majortom.model.core.IName;
import de.topicmapslab.majortom.model.core.IOccurrence;
import de.topicmapslab.majortom.model.core.IReifiable;
import de.topicmapslab.majortom.model.core.IScopable;
import de.topicmapslab.majortom.model.core.IScope;
import de.topicmapslab.majortom.model.core.ITopic;
import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.majortom.model.core.ITopicMapSystem;
import de.topicmapslab.majortom.model.core.ITypeable;
import de.topicmapslab.majortom.model.core.IVariant;
import de.topicmapslab.majortom.model.exception.ConcurrentThreadsException;
import de.topicmapslab.majortom.model.exception.TopicMapStoreException;
import de.topicmapslab.majortom.model.revision.Changeset;
import de.topicmapslab.majortom.model.revision.IRevision;
import de.topicmapslab.majortom.model.transaction.ITransaction;
import de.topicmapslab.majortom.store.TopicMapStoreImpl;
import de.topicmapslab.majortom.util.DatatypeAwareUtils;
import de.topicmapslab.majortom.util.XmlSchemeDatatypes;

/**
 * MaJorToM database topic map store
 * 
 * @author Sven Krosse
 * 
 */
public class JdbcTopicMapStore extends TopicMapStoreImpl {

	/**
	 * the connection provider
	 */
	private IConnectionProvider provider;
	/**
	 * the topic map identity
	 */
	private JdbcIdentity identity;
	/**
	 * the base locator of the topic map
	 */
	private ILocator baseLocator;

	/**
	 * constructor
	 */
	public JdbcTopicMapStore() {
		// VOID
	}

	/**
	 * @param topicMapSystem
	 */
	public JdbcTopicMapStore(ITopicMapSystem topicMapSystem) {
		super(topicMapSystem);
	}

	/**
	 * {@inheritDoc}
	 */
	protected IAssociation doCreateAssociation(ITopicMap topicMap, ITopic type) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateAssociation(topicMap, type);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IAssociation doCreateAssociation(ITopicMap topicMap, ITopic type, Collection<ITopic> themes) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateAssociation(topicMap, type, themes);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected ILocator doCreateItemIdentifier(ITopicMap topicMap) {
		return doCreateLocator(topicMap, doReadLocator(topicMap).getReference() + "/" + UUID.randomUUID());
	}

	/**
	 * {@inheritDoc}
	 */
	protected ILocator doCreateLocator(ITopicMap topicMap, String reference) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateLocator(topicMap, reference);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IName doCreateName(ITopic topic, String value) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateName(topic, value);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IName doCreateName(ITopic topic, String value, Collection<ITopic> themes) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateName(topic, value, themes);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IName doCreateName(ITopic topic, ITopic type, String value) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateName(topic, type, value);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IName doCreateName(ITopic topic, ITopic type, String value, Collection<ITopic> themes) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateName(topic, type, value, themes);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IOccurrence doCreateOccurrence(ITopic topic, ITopic type, String value) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateOccurrence(topic, type, value);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IOccurrence doCreateOccurrence(ITopic topic, ITopic type, String value, Collection<ITopic> themes) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateOccurrence(topic, type, value, themes);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IOccurrence doCreateOccurrence(ITopic topic, ITopic type, ILocator value) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateOccurrence(topic, type, value);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IOccurrence doCreateOccurrence(ITopic topic, ITopic type, ILocator value, Collection<ITopic> themes) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateOccurrence(topic, type, value, themes);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IOccurrence doCreateOccurrence(ITopic topic, ITopic type, String value, ILocator datatype) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateOccurrence(topic, type, value, datatype);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IOccurrence doCreateOccurrence(ITopic topic, ITopic type, String value, ILocator datatype, Collection<ITopic> themes)
			throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateOccurrence(topic, type, value, datatype, themes);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IAssociationRole doCreateRole(IAssociation association, ITopic type, ITopic player) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateRole(association, type, player);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IScope doCreateScope(ITopicMap topicMap, Collection<ITopic> themes) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateScope(topicMap, themes);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected ITopic doCreateTopicByItemIdentifier(ITopicMap topicMap, ILocator itemIdentifier) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateTopicByItemIdentifier(topicMap, itemIdentifier);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected ITopic doCreateTopicBySubjectIdentifier(ITopicMap topicMap, ILocator subjectIdentifier) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateTopicBySubjectIdentifier(topicMap, subjectIdentifier);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected ITopic doCreateTopicBySubjectLocator(ITopicMap topicMap, ILocator subjectLocator) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateTopicBySubjectLocator(topicMap, subjectLocator);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IVariant doCreateVariant(IName name, String value, Collection<ITopic> themes) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateVariant(name, value, themes);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IVariant doCreateVariant(IName name, ILocator value, Collection<ITopic> themes) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateVariant(name, value, themes);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IVariant doCreateVariant(IName name, String value, ILocator datatype, Collection<ITopic> themes) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doCreateVariant(name, value, datatype, themes);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doMergeTopicMaps(TopicMap context, TopicMap other) throws TopicMapStoreException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doMergeTopics(ITopic context, ITopic other) throws TopicMapStoreException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doModifyItemIdentifier(IConstruct c, ILocator itemIdentifier) throws TopicMapStoreException {
		try {
			provider.getProcessor().doModifyItemIdentifier(c, itemIdentifier);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doModifyPlayer(IAssociationRole role, ITopic player) throws TopicMapStoreException {
		try {
			provider.getProcessor().doModifyPlayer(role, player);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doModifyReifier(IReifiable r, ITopic reifier) throws TopicMapStoreException {
		try {
			provider.getProcessor().doModifyReifier(r, reifier);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doModifyScope(IScopable s, ITopic theme) throws TopicMapStoreException {
		try {
			provider.getProcessor().doModifyScope(s, theme);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doModifySubjectIdentifier(ITopic t, ILocator subjectIdentifier) throws TopicMapStoreException {
		try {
			provider.getProcessor().doModifySubjectIdentifier(t, subjectIdentifier);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doModifySubjectLocator(ITopic t, ILocator subjectLocator) throws TopicMapStoreException {
		try {
			provider.getProcessor().doModifySubjectLocator(t, subjectLocator);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doModifySupertype(ITopic t, ITopic type) throws TopicMapStoreException {
		try {
			provider.getProcessor().doModifySupertype(t, type);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doModifyTag(ITopicMap tm, String tag) throws TopicMapStoreException {
		throw new UnsupportedOperationException("not implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doModifyTag(ITopicMap tm, String tag, Calendar timestamp) throws TopicMapStoreException {
		throw new UnsupportedOperationException("Not implemented");

	}

	/**
	 * {@inheritDoc}
	 */
	protected void doModifyType(ITypeable t, ITopic type) throws TopicMapStoreException {
		try {
			provider.getProcessor().doModifyType(t, type);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doModifyType(ITopic t, ITopic type) throws TopicMapStoreException {
		try {
			provider.getProcessor().doModifyType(t, type);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doModifyValue(IName n, String value) throws TopicMapStoreException {
		try {
			provider.getProcessor().doModifyValue(n, value);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doModifyValue(IDatatypeAware t, String value) throws TopicMapStoreException {
		try {
			provider.getProcessor().doModifyValue(t, value);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doModifyValue(IDatatypeAware t, String value, ILocator datatype) throws TopicMapStoreException {
		try {
			provider.getProcessor().doModifyValue(t, value, datatype);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doModifyValue(IDatatypeAware t, Object value) throws TopicMapStoreException {
		try {
			final ILocator loc = doCreateLocator(t.getTopicMap(), XmlSchemeDatatypes.javaToXsd(value.getClass()));
			provider.getProcessor().doModifyValue(t, DatatypeAwareUtils.toString(value, loc), loc);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IAssociation> doReadAssociation(ITopic t) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadAssociation(t);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IAssociation> doReadAssociation(ITopic t, ITopic type) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadAssociation(t, type);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IAssociation> doReadAssociation(ITopic t, ITopic type, IScope scope) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadAssociation(t, type, scope);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IAssociation> doReadAssociation(ITopic t, IScope scope) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadAssociation(t, scope);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IAssociation> doReadAssociation(ITopicMap tm) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadAssociation(tm);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IAssociation> doReadAssociation(ITopicMap tm, ITopic type) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadAssociation(tm, type);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IAssociation> doReadAssociation(ITopicMap tm, ITopic type, IScope scope) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadAssociation(tm, type, scope);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IAssociation> doReadAssociation(ITopicMap tm, IScope scope) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadAssociation(tm, scope);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Changeset doReadChangeSet(IRevision r) throws TopicMapStoreException {
		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<ICharacteristics> doReadCharacteristics(ITopic t) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadCharacteristics(t);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<ICharacteristics> doReadCharacteristics(ITopic t, ITopic type) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadCharacteristics(t, type);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<ICharacteristics> doReadCharacteristics(ITopic t, ITopic type, IScope scope) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadCharacteristics(t, type, scope);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<ICharacteristics> doReadCharacteristics(ITopic t, IScope scope) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadCharacteristics(t, scope);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IConstruct doReadConstruct(ITopicMap t, String id) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadConstruct(t, id);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IConstruct doReadConstruct(ITopicMap t, ILocator itemIdentifier) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadConstruct(t, itemIdentifier);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected ILocator doReadDataType(IDatatypeAware d) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadDataType(d);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IRevision doReadFutureRevision(IRevision r) throws TopicMapStoreException {
		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected String doReadId(IConstruct c) throws TopicMapStoreException {
		if (c instanceof ITopicMap) {
			return this.identity.getId();
		}
		return ((ConstructImpl) c).getIdentity().getId();
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<ILocator> doReadItemIdentifiers(IConstruct c) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadItemIdentifiers(c);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected ILocator doReadLocator(ITopicMap t) throws TopicMapStoreException {
		return baseLocator;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IName> doReadNames(ITopic t) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadNames(t);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IName> doReadNames(ITopic t, ITopic type) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadNames(t, type);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IName> doReadNames(ITopic t, ITopic type, IScope scope) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadNames(t, type, scope);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IName> doReadNames(ITopic t, IScope scope) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadNames(t, scope);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IOccurrence> doReadOccurrences(ITopic t) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadOccurrences(t);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IOccurrence> doReadOccurrences(ITopic t, ITopic type) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadOccurrences(t, type);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IOccurrence> doReadOccurrences(ITopic t, ITopic type, IScope scope) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadOccurrences(t, type, scope);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IOccurrence> doReadOccurrences(ITopic t, IScope scope) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadOccurrences(t, scope);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected ITopic doReadPlayer(IAssociationRole role) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadPlayer(role);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IRevision doReadPreviousRevision(IRevision r) throws TopicMapStoreException {
		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected IReifiable doReadReification(ITopic t) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadReification(t);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected ITopic doReadReification(IReifiable r) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadReification(r);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Calendar doReadRevisionBegin(IRevision r) throws TopicMapStoreException {
		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected Calendar doReadRevisionEnd(IRevision r) throws TopicMapStoreException {
		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<ITopic> doReadRoleTypes(IAssociation association) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadRoleTypes(association);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IAssociationRole> doReadRoles(IAssociation association) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadRoles(association);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IAssociationRole> doReadRoles(IAssociation association, ITopic type) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadRoles(association, type);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IAssociationRole> doReadRoles(ITopic player) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadRoles(player);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IAssociationRole> doReadRoles(ITopic player, ITopic type) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadRoles(player, type);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IAssociationRole> doReadRoles(ITopic player, ITopic type, ITopic assocType) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadRoles(player, type, assocType);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected IScope doReadScope(IScopable s) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadScope(s);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<ILocator> doReadSubjectIdentifiers(ITopic t) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadSubjectIdentifiers(t);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<ILocator> doReadSubjectLocators(ITopic t) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadSubjectLocators(t);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<ITopic> doReadSuptertypes(ITopic t) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadSuptertypes(t);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected ITopic doReadTopicBySubjectIdentifier(ITopicMap t, ILocator subjectIdentifier) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadTopicBySubjectIdentifier(t, subjectIdentifier);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected ITopic doReadTopicBySubjectLocator(ITopicMap t, ILocator subjectLocator) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadTopicBySubjectLocator(t, subjectLocator);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<ITopic> doReadTopics(ITopicMap t) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadTopics(t);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<ITopic> doReadTopics(ITopicMap t, ITopic type) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadTopics(t, type);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected ITopic doReadType(ITypeable typed) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadType(typed);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<ITopic> doReadTypes(ITopic t) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadTypes(t);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object doReadValue(IName n) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadValue(n);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object doReadValue(IDatatypeAware t) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadValue(t);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	protected <T> T doReadValue(IDatatypeAware t, Class<T> type) throws TopicMapStoreException {
		try {
			return (T) DatatypeAwareUtils.toValue(provider.getProcessor().doReadValue(t), type);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		} catch (Exception e) {
			throw new TopicMapStoreException("Cannot convert to given type", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IVariant> doReadVariants(IName n) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadVariants(n);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Set<IVariant> doReadVariants(IName n, IScope scope) throws TopicMapStoreException {
		try {
			return provider.getProcessor().doReadVariants(n, scope);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doRemoveAssociation(IAssociation association, boolean cascade) throws TopicMapStoreException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doRemoveItemIdentifier(IConstruct c, ILocator itemIdentifier) throws TopicMapStoreException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doRemoveName(IName name, boolean cascade) throws TopicMapStoreException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doRemoveOccurrence(IOccurrence occurrence, boolean cascade) throws TopicMapStoreException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doRemoveRole(IAssociationRole role, boolean cascade) throws TopicMapStoreException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doRemoveScope(IScopable s, ITopic theme) throws TopicMapStoreException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doRemoveSubjectIdentifier(ITopic t, ILocator subjectIdentifier) throws TopicMapStoreException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doRemoveSubjectLocator(ITopic t, ILocator subjectLocator) throws TopicMapStoreException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doRemoveSupertype(ITopic t, ITopic type) throws TopicMapStoreException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doRemoveTopic(ITopic topic, boolean cascade) throws TopicMapStoreException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doRemoveTopicMap(ITopicMap topicMap, boolean cascade) throws TopicMapStoreException {
		try {
			provider.getProcessor().doRemoveTopicMap(topicMap, cascade);
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doRemoveType(ITopic t, ITopic type) throws TopicMapStoreException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doRemoveVariant(IVariant variant, boolean cascade) throws TopicMapStoreException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	public void commit() throws ConcurrentThreadsException {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException("Not implemented!");
	}

	/**
	 * {@inheritDoc}
	 */
	public ITransaction createTransaction() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public <I extends Index> I getIndex(Class<I> clazz) {
		throw new UnsupportedOperationException("The index class '" + (clazz == null ? "null" : clazz.getCanonicalName())
				+ "' is not supported by the current engine.");
	}

	/**
	 * {@inheritDoc}
	 */
	public void initialize(Locator topicMapBaseLocator) throws TopicMapStoreException {
		Object host = getTopicMapSystem().getProperty(JdbcTopicMapStoreProperty.DATABASE_HOST);
		Object database = getTopicMapSystem().getProperty(JdbcTopicMapStoreProperty.DATABASE_NAME);
		Object user = getTopicMapSystem().getProperty(JdbcTopicMapStoreProperty.DATABASE_PASSWORD);
		Object password = getTopicMapSystem().getProperty(JdbcTopicMapStoreProperty.DATABASE_USER);
		Object dialect = getTopicMapSystem().getProperty(JdbcTopicMapStoreProperty.SQL_DIALECT);

		if (database == null || host == null || user == null || dialect == null) {
			throw new TopicMapStoreException("Missing connection properties!");
		}
		provider = ConnectionProviderFactory.getFactory().newConnectionProvider(dialect.toString());
		try {
			provider.openConnection(host.toString(), database.toString(), user.toString(), password == null ? "" : password.toString());
			this.identity = new JdbcIdentity(provider.getProcessor().doCreateTopicMap((ILocator) topicMapBaseLocator));
		} catch (SQLException e) {
			throw new TopicMapStoreException("Cannot open connection to database!", e);
		}
		this.baseLocator = (ILocator) topicMapBaseLocator;

	}

	/**
	 * {@inheritDoc}
	 */
	public void close() throws TopicMapStoreException {
		try {
			provider.closeConnection();
		} catch (SQLException e) {
			throw new TopicMapStoreException("Cannot close connection to database!", e);
		}
		super.close();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isTransactable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportRevisions() {
		return false;
	}

}
