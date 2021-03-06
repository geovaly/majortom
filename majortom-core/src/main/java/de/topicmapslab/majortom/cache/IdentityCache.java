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
package de.topicmapslab.majortom.cache;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;
import org.tmapi.core.Construct;

import de.topicmapslab.majortom.model.core.IConstruct;
import de.topicmapslab.majortom.model.core.ILocator;
import de.topicmapslab.majortom.model.core.IName;
import de.topicmapslab.majortom.model.core.IScope;
import de.topicmapslab.majortom.model.core.ITopic;
import de.topicmapslab.majortom.model.event.ITopicMapListener;
import de.topicmapslab.majortom.model.event.TopicMapEventType;
import de.topicmapslab.majortom.util.HashUtil;

/**
 * Base implementation of a store object containing all identity informations
 * 
 * @author Sven Krosse
 * 
 */
class IdentityCache implements ITopicMapListener {

	/**
	 * enumeration used as key for internal storage
	 */
	enum Key {
		ITEM_IDENTIFIER,

		SUBJECT_IDENTIFIER,

		SUBJEC_LOCATOR
	}

	/**
	 * Map to store IDs and the corresponding constructs
	 */
	private BidiMap ids;

	/**
	 * Map to store the all identities contained by the topic map instance
	 */
	private Map<Key, Set<ILocator>> identities;

	/**
	 * storage map of the reference-locator mapping of the topic map
	 */
	private Map<String, ILocator> locators;

	/**
	 * item-identifier mapping of the topic map engine
	 */
	private Map<ILocator, IConstruct> itemIdentifiers;

	/**
	 * construct to item-identifiers mapping
	 */
	private Map<IConstruct, Set<ILocator>> constructItemIdentifiers;

	/**
	 * subject-identifier mapping of the topic map engine
	 */
	private Map<ILocator, ITopic> subjectIdentifiers;

	/**
	 * topic to subject-identifiers mapping
	 */
	private Map<ITopic, Set<ILocator>> topicSubjectIdentifiers;

	/**
	 * subject-locator mapping of the topic map engine
	 */
	private Map<ILocator, ITopic> subjectLocators;

	/**
	 * topic to subject-locators mapping
	 */
	private Map<ITopic, Set<ILocator>> topicSubjectLocators;

	/**
	 * map to store the best labels of a topic
	 */
	private Map<BestLabelKey, String> bestLabels;

	/**
	 * A Map containing all cache keys of the specified topic
	 */
	private Map<ITopic, Set<BestLabelKey>> bestLabelCacheKeys;

	/**
	 * map to store the best identifiers of a topic
	 */
	private Map<BestIdentifierKey, String> bestIdentifiers;

	class BestIdentifierKey {
		ITopic parent;
		boolean withPrefix;

		/**
		 * constructor
		 */
		public BestIdentifierKey(ITopic parent, boolean withPrefix) {
			this.parent = parent;
			this.withPrefix = withPrefix;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean equals(Object obj) {
			if (obj instanceof BestIdentifierKey) {
				boolean result = parent.equals(((BestIdentifierKey) obj).parent);
				result &= withPrefix == ((BestIdentifierKey) obj).withPrefix;
				return result;
			}
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public int hashCode() {
			int hash = parent.hashCode();
			hash |= withPrefix ? 1 : 0;
			return hash;
		}
	}

	class BestLabelKey {
		ITopic parent;
		ITopic theme;
		boolean strict;

		/**
		 * constructor
		 */
		public BestLabelKey(ITopic parent) {
			this.parent = parent;
			this.theme = null;
			this.strict = false;
		}

		/**
		 * constructor
		 */
		public BestLabelKey(ITopic parent, ITopic theme, boolean strict) {
			this.parent = parent;
			this.theme = theme;
			this.strict = strict;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean equals(Object obj) {
			if (obj instanceof BestLabelKey) {
				boolean result = parent.equals(((BestLabelKey) obj).parent);
				result &= theme == null ? ((BestLabelKey) obj).theme == null : theme.equals(((BestLabelKey) obj).theme);
				result &= strict == ((BestLabelKey) obj).strict;
				return result;
			}
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public int hashCode() {
			int hash = parent.hashCode();
			hash |= theme == null ? 0 : theme.hashCode();
			hash |= strict ? 1 : 0;
			return hash;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		if (identities != null) {
			identities.clear();
		}
		if (locators != null) {
			locators.clear();
		}
		if (itemIdentifiers != null) {
			itemIdentifiers.clear();
		}
		if (subjectIdentifiers != null) {
			subjectIdentifiers.clear();
		}
		if (subjectLocators != null) {
			subjectLocators.clear();
		}
		if (constructItemIdentifiers != null) {
			constructItemIdentifiers.clear();
		}
		if (topicSubjectIdentifiers != null) {
			topicSubjectIdentifiers.clear();
		}
		if (topicSubjectLocators != null) {
			topicSubjectLocators.clear();
		}
		if (ids != null) {
			ids.clear();
		}
		if (bestLabelCacheKeys != null) {
			bestLabelCacheKeys.clear();
		}
		if (bestLabels != null) {
			bestLabels.clear();
		}
		if (bestIdentifiers != null) {
			bestIdentifiers.clear();
		}
	}

	/**
	 * Return the construct identified by the given id.
	 * 
	 * @param id
	 *            the id
	 * @return the construct or <code>null</code>
	 */
	public IConstruct byId(final String id) {
		if (ids == null) {
			return null;
		}
		return (IConstruct) ids.get(id);
	}

	/**
	 * Secure extraction of a construct by its identity from the given map.
	 * 
	 * @param <T>
	 *            the generic type of construct to extract
	 * @param map
	 *            the map
	 * @param l
	 *            the locator
	 * @return the construct or <code>null</code> if the given map is <code>null</code> or does not contain the given
	 *         key.
	 */
	public <T extends IConstruct> T byIdentity(Map<ILocator, T> map, ILocator l) {
		if (map == null) {
			return null;
		}
		return map.get(l);
	}

	/**
	 * Return the construct identified by the given item-identifier.
	 * 
	 * @param l
	 *            the item-identifier
	 * @return the construct or <code>null</code>
	 */
	public IConstruct byItemIdentifier(final ILocator l) {
		return byIdentity(itemIdentifiers, l);
	}

	/**
	 * Return the topic identified by the given subject-identifier.
	 * 
	 * @param l
	 *            the subject-identifier
	 * @return the topic or <code>null</code>
	 */
	public ITopic bySubjectIdentifier(final ILocator l) {
		return byIdentity(subjectIdentifiers, l);
	}

	/**
	 * Return the topic identified by the given subject-locator.
	 * 
	 * @param l
	 *            the subject-locator
	 * @return the topic or <code>null</code>
	 */
	public ITopic bySubjectLocator(final ILocator l) {
		return byIdentity(subjectLocators, l);
	}

	/**
	 * Cache the mapping between the id and the construct into internal store.
	 * 
	 * @param id
	 *            the id
	 * @param c
	 *            the construct
	 */
	public void cacheId(final String id, IConstruct c) {
		if (ids == null) {
			ids = new TreeBidiMap();
		}
		ids.put(id, c);
	}

	/**
	 * Cache the mapping between the item-identifier and the construct into internal store.
	 * 
	 * @param l
	 *            the item-identifier
	 * @param c
	 *            the construct
	 */
	public void cacheItemIdentifier(final ILocator l, IConstruct c) {
		if (itemIdentifiers == null) {
			itemIdentifiers = HashUtil.getHashMap();
		}
		itemIdentifiers.put(l, c);
	}

	/**
	 * Cache the mapping between the subject-identifier and the topic into internal store.
	 * 
	 * @param l
	 *            the subject-identifier
	 * @param t
	 *            the topic
	 */
	public void cacheSubjectIdentifier(final ILocator l, ITopic t) {
		if (subjectIdentifiers == null) {
			subjectIdentifiers = HashUtil.getHashMap();
		}
		subjectIdentifiers.put(l, t);
	}

	/**
	 * Cache the mapping between the subject-locator and the topic into internal store.
	 * 
	 * @param l
	 *            the subject-locator
	 * @param t
	 *            the topic
	 */
	public void cacheSubjectLocator(final ILocator l, ITopic t) {
		if (subjectLocators == null) {
			subjectLocators = HashUtil.getHashMap();
		}
		subjectLocators.put(l, t);
	}

	/**
	 * Secure extraction of the identities of the given construct from the given map.
	 * 
	 * @param <T>
	 *            the generic type of construct
	 * @param map
	 *            the map
	 * @param construct
	 *            the construct
	 * @return the identities or <code>null</code> if the given map is <code>null</code> or does not contain the given
	 *         key.
	 */
	public <T extends IConstruct> Set<ILocator> getIdentities(Map<T, Set<ILocator>> map, T construct) {
		if (map == null || !map.containsKey(construct)) {
			return null;
		}
		return HashUtil.getHashSet(map.get(construct));
	}

	/**
	 * Return all item-identifiers of the given construct.
	 * 
	 * @param c
	 *            the construct
	 * @return the identifiers
	 */
	public Set<ILocator> getItemIdentifiers(IConstruct c) {
		return getIdentities(constructItemIdentifiers, c);
	}

	/**
	 * Return all subject-identifiers of the given topic.
	 * 
	 * @param t
	 *            the topic
	 * @return the identifiers
	 */
	public Set<ILocator> getSubjectIdentifiers(ITopic t) {
		return getIdentities(topicSubjectIdentifiers, t);
	}

	/**
	 * Return all subject-locator of the given topic.
	 * 
	 * @param t
	 *            the topic
	 * @return the locators
	 */
	public Set<ILocator> getSubjectLocators(ITopic t) {
		return getIdentities(topicSubjectLocators, t);
	}

	/**
	 * Cache the item-identifiers of the given construct to the internal store.
	 * 
	 * @param c
	 *            the construct
	 * @param identifiers
	 *            the item-identifiers
	 */
	public void cacheItemIdentifiers(IConstruct c, Set<ILocator> identifiers) {
		if (constructItemIdentifiers == null) {
			constructItemIdentifiers = HashUtil.getHashMap();
		}
		constructItemIdentifiers.put(c, HashUtil.getHashSet(identifiers));
	}

	/**
	 * Cache the subject-identifiers of the given topic to the internal store.
	 * 
	 * @param t
	 *            the topic
	 * @param identifiers
	 *            the subject-identifiers
	 */
	public void cacheSubjectIdentifiers(ITopic t, Set<ILocator> identifiers) {
		if (topicSubjectIdentifiers == null) {
			topicSubjectIdentifiers = HashUtil.getHashMap();
		}
		topicSubjectIdentifiers.put(t, HashUtil.getHashSet(identifiers));
	}

	/**
	 * Cache the subject-locators of the given topic to the internal store.
	 * 
	 * @param t
	 *            the topic
	 * @param identifiers
	 *            the subject-locators
	 */
	public void cacheSubjectLocators(ITopic t, Set<ILocator> identifiers) {
		if (topicSubjectLocators == null) {
			topicSubjectLocators = HashUtil.getHashMap();
		}
		topicSubjectLocators.put(t, HashUtil.getHashSet(identifiers));
	}

	/**
	 * Return all internal stored identifiers.
	 * 
	 * @return all identifiers
	 */
	public Set<ILocator> getIdentifiers() {
		Set<ILocator> ii = getItemIdentifiers();
		Set<ILocator> si = getSubjectIdentifiers();
		Set<ILocator> sl = getSubjectLocators();
		if (ii == null || si == null || sl == null) {
			return null;
		}
		Set<ILocator> set = HashUtil.getHashSet();
		set.addAll(ii);
		set.addAll(si);
		set.addAll(sl);
		if (set.isEmpty()) {
			return Collections.emptySet();
		}
		return set;
	}

	/**
	 * Extract the identities contained by the current topic map of the specified type.
	 * 
	 * @param key
	 *            the type of identities ( item-identifiers, subject-identifiers, subject-locators )
	 * @return the identifiers or <code>null</code>
	 */
	public Set<ILocator> getIdentities(Key key) {
		if (identities == null || !identities.containsKey(key)) {
			return null;
		}
		return identities.get(key);
	}

	/**
	 * Return all internal stored item-identifiers.
	 * 
	 * @return all item-identifiers
	 */
	public Set<ILocator> getItemIdentifiers() {
		return getIdentities(Key.ITEM_IDENTIFIER);
	}

	/**
	 * Return all internal stored subject-identifiers.
	 * 
	 * @return all subject-identifiers
	 */
	public Set<ILocator> getSubjectIdentifiers() {
		return getIdentities(Key.SUBJECT_IDENTIFIER);
	}

	/**
	 * Return all internal stored subject-locators.
	 * 
	 * @return all subject-locators
	 */
	public Set<ILocator> getSubjectLocators() {
		return getIdentities(Key.SUBJEC_LOCATOR);
	}

	/**
	 * Cache the given identities to the internal cache
	 * 
	 * @param key
	 *            the key declare the type of identities
	 * @param locators
	 *            the locators
	 */
	public void cacheIdentities(Key key, Set<ILocator> locators) {
		if (identities == null) {
			identities = HashUtil.getHashMap();
		}
		identities.put(key, locators);
	}

	/**
	 * Returns the best label for the given topic
	 * 
	 * @param t
	 *            the topic
	 * @param withPrefix
	 *            flag indicates if the identifier is prefixes
	 * @return the best label
	 */
	public String getBestIdentifier(ITopic t, boolean withPrefix) {
		if (bestIdentifiers == null) {
			return null;
		}
		return bestIdentifiers.get(new BestIdentifierKey(t, withPrefix));
	}

	/**
	 * Cache the best label of the given topic to the internal store.
	 * 
	 * @param t
	 *            the topic
	 * @param withPrefix
	 *            flag indicates if the identifier is prefixes
	 * @param bestIdentifier
	 *            the best identifier
	 */
	public void cacheBestIdentifier(ITopic t, boolean withPrefix, String bestIdentifier) {
		if (bestIdentifiers == null) {
			bestIdentifiers = HashUtil.getHashMap();
		}
		bestIdentifiers.put(new BestIdentifierKey(t, withPrefix), bestIdentifier);
	}

	/**
	 * Returns the best label for the given topic
	 * 
	 * @param t
	 *            the topic
	 * @return the best label
	 */
	public String getBestLabel(ITopic t) {
		if (bestLabels == null) {
			return null;
		}
		return bestLabels.get(new BestLabelKey(t));
	}

	/**
	 * Cache the best label of the given topic to the internal store.
	 * 
	 * @param t
	 *            the topic
	 * @param bestLabel
	 *            the best label
	 */
	public void cacheBestLabel(ITopic t, String bestLabel) {
		if (bestLabels == null) {
			bestLabels = HashUtil.getHashMap();
		}
		bestLabels.put(generateKey(t, null, false), bestLabel);
	}

	/**
	 * Returns the best label for the given topic
	 * 
	 * @param t
	 *            the topic
	 * @param theme
	 *            the theme
	 * @param strict
	 *            the strict flag
	 * @return the best label
	 */
	public String getBestLabel(ITopic t, ITopic theme, boolean strict) {
		if (bestLabels == null) {
			return null;
		}
		return bestLabels.get(new BestLabelKey(t, theme, strict));
	}

	/**
	 * Cache the best label of the given topic to the internal store.
	 * 
	 * @param t
	 *            the topic
	 * @param theme
	 *            the theme
	 * @param strict
	 *            the strict flag
	 * @param bestLabel
	 *            the best label
	 */
	public void cacheBestLabel(ITopic t, ITopic theme, boolean strict, String bestLabel) {
		if (bestLabels == null) {
			bestLabels = HashUtil.getHashMap();
		}
		bestLabels.put(generateKey(t, theme, strict), bestLabel);
	}

	/**
	 * Generates a key for the given topic and theme
	 * 
	 * @param topic
	 *            the topic
	 * @param theme
	 *            the theme
	 * @return the generated key
	 */
	private BestLabelKey generateKey(ITopic topic, ITopic theme, boolean strict) {
		if (bestLabelCacheKeys == null) {
			bestLabelCacheKeys = HashUtil.getHashMap();
		}
		Set<BestLabelKey> set = bestLabelCacheKeys.get(topic);
		if (set == null) {
			set = HashUtil.getHashSet();
			bestLabelCacheKeys.put(topic, set);
		}
		BestLabelKey key = new BestLabelKey(topic, theme, strict);
		set.add(key);
		return key;
	}

	/**
	 * Removing all best-labels for the given topic and all themes
	 * 
	 * @param topic
	 *            the topic
	 * @param themes
	 *            the themes
	 */
	private void removeBestLabels(ITopic topic, ITopic... themes) {
		if (bestLabelCacheKeys == null || !bestLabelCacheKeys.containsKey(topic) || bestLabels == null) {
			return;
		}
		Set<BestLabelKey> keys = bestLabelCacheKeys.get(topic);
		/*
		 * remove non-theme best label
		 */
		BestLabelKey key = new BestLabelKey(topic);
		keys.remove(key);
		bestLabels.remove(key);
		/*
		 * remove best-labels with theme
		 */
		for (ITopic theme : themes) {
			key = new BestLabelKey(topic, theme, false);
			keys.remove(key);
			bestLabels.remove(key);
			key = new BestLabelKey(topic, theme, true);
			keys.remove(key);
			bestLabels.remove(key);
		}
	}

	/**
	 * Removing all best-identifiers of the given topic
	 * 
	 * @param topic
	 *            the topic
	 */
	private void removeBestIdentifiers(ITopic topic) {
		if (bestIdentifiers == null) {
			return;
		}
		bestIdentifiers.remove(new BestIdentifierKey(topic, false));
		bestIdentifiers.remove(new BestIdentifierKey(topic, true));
	}

	/**
	 * {@inheritDoc}
	 */
	public void topicMapChanged(String id, TopicMapEventType event, Construct notifier, Object newValue, Object oldValue) {
		/*
		 * topic was removed
		 */
		if (event == TopicMapEventType.TOPIC_REMOVED) {
			removeTopicItemFromCache((ITopic) oldValue, (IConstruct) notifier);
		}
		/*
		 * construct was removed
		 */
		else if (event == TopicMapEventType.NAME_REMOVED || event == TopicMapEventType.ROLE_REMOVED || event == TopicMapEventType.OCCURRENCE_REMOVED || event == TopicMapEventType.VARIANT_REMOVED
				|| event == TopicMapEventType.ASSOCIATION_REMOVED) {
			removeConstructItemFromCache((IConstruct) oldValue, (IConstruct) notifier);
		}
		/*
		 * subject-identifier added
		 */
		else if (event == TopicMapEventType.SUBJECT_IDENTIFIER_ADDED) {
			ITopic topic = (ITopic) notifier;
			ILocator locator = (ILocator) newValue;
			if (topicSubjectIdentifiers != null && topicSubjectIdentifiers.containsKey(topic)) {
				topicSubjectIdentifiers.get(topic).add(locator);
			}
			cacheSubjectIdentifier(locator, topic);
			if (identities != null && identities.containsKey(Key.SUBJECT_IDENTIFIER)) {
				identities.get(Key.SUBJECT_IDENTIFIER).add(locator);
			}
			removeBestLabels(topic);
			removeBestIdentifiers(topic);
		}
		/*
		 * subject-locator added
		 */
		else if (event == TopicMapEventType.SUBJECT_LOCATOR_ADDED) {
			ITopic topic = (ITopic) notifier;
			ILocator locator = (ILocator) newValue;
			if (topicSubjectLocators != null && topicSubjectLocators.containsKey(topic)) {
				topicSubjectLocators.get(topic).add(locator);
			}
			cacheSubjectLocator(locator, topic);
			if (identities != null && identities.containsKey(Key.SUBJEC_LOCATOR)) {
				identities.get(Key.SUBJEC_LOCATOR).add(locator);
			}
			removeBestLabels(topic);
			removeBestIdentifiers(topic);
		}
		/*
		 * item-identifier added
		 */
		else if (event == TopicMapEventType.ITEM_IDENTIFIER_ADDED) {
			IConstruct construct = (IConstruct) notifier;
			ILocator locator = (ILocator) newValue;
			if (constructItemIdentifiers != null && constructItemIdentifiers.containsKey(construct)) {
				constructItemIdentifiers.get(construct).add(locator);
			}
			cacheItemIdentifier(locator, construct);
			if (identities != null && identities.containsKey(Key.ITEM_IDENTIFIER)) {
				identities.get(Key.ITEM_IDENTIFIER).add(locator);
			}
			if (construct instanceof ITopic) {
				removeBestLabels((ITopic) construct);
				removeBestIdentifiers((ITopic) construct);
			}
		}
		/*
		 * subject-identifier removed
		 */
		else if (event == TopicMapEventType.SUBJECT_IDENTIFIER_REMOVED) {
			ITopic topic = (ITopic) notifier;
			ILocator locator = (ILocator) oldValue;
			if (topicSubjectIdentifiers != null && topicSubjectIdentifiers.containsKey(topic)) {
				topicSubjectIdentifiers.get(topic).remove(locator);
			}
			if (subjectIdentifiers != null) {
				subjectIdentifiers.remove(locator);
			}
			if (identities != null && identities.containsKey(Key.SUBJECT_IDENTIFIER)) {
				identities.get(Key.SUBJECT_IDENTIFIER).remove(locator);
			}
			removeBestLabels(topic);
			removeBestIdentifiers(topic);
		}
		/*
		 * subject-locator removed
		 */
		else if (event == TopicMapEventType.SUBJECT_LOCATOR_REMOVED) {
			ITopic topic = (ITopic) notifier;
			ILocator locator = (ILocator) oldValue;
			if (topicSubjectLocators != null && topicSubjectLocators.containsKey(topic)) {
				topicSubjectLocators.get(topic).remove(locator);
			}
			if (subjectLocators != null) {
				subjectLocators.remove(locator);
			}
			if (identities != null && identities.containsKey(Key.SUBJEC_LOCATOR)) {
				identities.get(Key.SUBJEC_LOCATOR).remove(locator);
			}
			removeBestLabels(topic);
			removeBestIdentifiers(topic);
		}
		/*
		 * item-identifier removed
		 */
		else if (event == TopicMapEventType.ITEM_IDENTIFIER_REMOVED) {
			IConstruct construct = (IConstruct) notifier;
			ILocator locator = (ILocator) oldValue;
			if (constructItemIdentifiers != null && constructItemIdentifiers.containsKey(construct)) {
				constructItemIdentifiers.get(construct).remove(locator);
			}
			if (itemIdentifiers != null) {
				itemIdentifiers.remove(locator);
			}
			if (identities != null && identities.containsKey(Key.ITEM_IDENTIFIER)) {
				identities.get(Key.ITEM_IDENTIFIER).remove(locator);
			}
			if (construct instanceof ITopic) {
				removeBestLabels((ITopic) construct);
				removeBestIdentifiers((ITopic) construct);
			}
		}
		/*
		 * name was added
		 */
		else if (event == TopicMapEventType.NAME_ADDED) {
			removeBestLabels((ITopic) notifier);
		}
		/*
		 * type or scope of name was modified
		 */
		else if (event == TopicMapEventType.TYPE_SET && notifier instanceof IName) {
			removeBestLabels((ITopic) notifier.getParent());
		}
		/*
		 * scope modified
		 */
		else if (event == TopicMapEventType.SCOPE_MODIFIED && notifier instanceof IName) {
			removeBestLabels((ITopic) notifier.getParent(), ((IScope) oldValue).getThemes().toArray(new ITopic[0]));
		}
		/*
		 * topics are merging
		 */
		else if (event == TopicMapEventType.MERGE) {
			removeTopicItemFromCache((ITopic) newValue, (IConstruct) notifier);
			removeTopicItemFromCache((ITopic) oldValue, (IConstruct) notifier);
		}
		/*
		 * value changed
		 */
		else if (event == TopicMapEventType.VALUE_MODIFIED && notifier instanceof IName) {
			removeBestLabels((ITopic) notifier.getParent());
		}

	}

	/**
	 * Internal method to remove all topic dependent entries from internal caches.
	 * 
	 * @param topic
	 *            the topic reference to remove
	 * @param notifier
	 *            the parent
	 */
	private final void removeTopicItemFromCache(ITopic topic, IConstruct notifier) {
		/*
		 * clear subject-identifiers
		 */
		if (topicSubjectIdentifiers != null && topicSubjectIdentifiers.containsKey(topic)) {
			topicSubjectIdentifiers.remove(topic);
		}
		if (subjectIdentifiers != null) {
			for (Entry<ILocator, ITopic> si : HashUtil.getHashSet(subjectIdentifiers.entrySet())) {
				if (si.getValue().equals(topic)) {
					subjectIdentifiers.remove(si.getKey());
				}
			}
		}
		/*
		 * clear subject-locators
		 */
		if (topicSubjectLocators != null && topicSubjectLocators.containsKey(topic)) {
			topicSubjectLocators.remove(topic);
		}
		if (subjectLocators != null) {
			for (Entry<ILocator, ITopic> sl : HashUtil.getHashSet(subjectLocators.entrySet())) {
				if (sl.getValue().equals(topic)) {
					subjectLocators.remove(sl.getKey());
				}
			}
		}
		removeConstructItemFromCache(topic, notifier);
		/*
		 * remove best label
		 */
		removeBestLabels(topic);
		/*
		 * clear identities
		 */
		if (identities != null) {
			identities.clear();
		}
	}

	/**
	 * Remove all relevant cache entries for the given construct
	 * 
	 * @param construct
	 *            the construct
	 * @param notifier
	 *            the parent
	 */
	private final void removeConstructItemFromCache(IConstruct construct, IConstruct notifier) {
		/*
		 * clear item-identifiers
		 */
		if (constructItemIdentifiers != null && constructItemIdentifiers.containsKey(construct)) {
			constructItemIdentifiers.remove(construct);
		}
		if (itemIdentifiers != null) {
			for (Entry<ILocator, IConstruct> ii : HashUtil.getHashSet(itemIdentifiers.entrySet())) {
				if (ii.getValue().equals(construct)) {
					itemIdentifiers.remove(ii.getKey());
				}
			}
		}
		/*
		 * clear id
		 */
		if (ids != null) {
			ids.clear();
		}
		/*
		 * clear identities
		 */
		if (identities != null) {
			identities.remove(Key.ITEM_IDENTIFIER);
		}

		/*
		 * remove best label of parent topic if name was removed
		 */
		if (construct instanceof IName && bestLabels != null && bestLabels.containsKey(notifier)) {
			removeBestLabels((ITopic) notifier);
		}
	}
}
