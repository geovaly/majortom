package de.topicmapslab.majortom.model.store;

import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.index.Index;

import de.topicmapslab.majortom.model.core.IConstruct;
import de.topicmapslab.majortom.model.core.IConstructFactory;
import de.topicmapslab.majortom.model.core.ILocator;
import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.majortom.model.core.ITopicMapSystem;
import de.topicmapslab.majortom.model.event.ITopicMapListener;
import de.topicmapslab.majortom.model.exception.TopicMapStoreException;
import de.topicmapslab.majortom.model.transaction.ITransaction;

/**
 * Interface definition of a topic map data store
 * 
 * @author Sven Krosse
 * 
 */
public interface ITopicMapStore {

	/**
	 * Initializing method of the topic map store.
	 * 
	 * @param topicMapBaseLocator
	 *            the base locator
	 * 
	 * @throws TopicMapStoreException
	 *             thrown if initialization failed
	 */
	public void initialize(Locator topicMapBaseLocator) throws TopicMapStoreException;

	/**
	 * Setting the topic map system to topic map store
	 * 
	 * @param topicMapSystem
	 *            the topic map system
	 */
	public void setTopicMapSystem(ITopicMapSystem topicMapSystem);

	/**
	 * Open the connection to the topic map store.
	 * 
	 * @throws TopicMapStoreException
	 *             thrown if the connection cannot be established
	 */
	public void connect() throws TopicMapStoreException;

	/**
	 * Close the connection to the topic map data store.
	 * 
	 * @throws TopicMapStoreException
	 *             thrown if the connection is currently used.
	 */
	public void close() throws TopicMapStoreException;

	/**
	 * Checks if the connection to the data store is already established.
	 * 
	 * @return <code>true</code> if the connection is established, <code>false</code> otherwise.
	 */
	public boolean isConnected();

	/**
	 * Indicates if the current topic map store instance only supports read-only access.
	 * 
	 * @return <code>true</code> if only read-operations are supported, <code>false</code> if there is a
	 *         read-write-access.
	 */
	public boolean isReadOnly();

	/**
	 * Indicates if the current topic map store instance supports the history functionality.
	 * 
	 * @return <code>true</code> if the store save all changes of the topic map, <code>false</code> otherwise.
	 */
	public boolean isRevisionManagementSupported();

	/**
	 * Operation method to merge a set of constructs to one new construct
	 * 
	 * @param context
	 *            the construct to merge
	 * @param others
	 *            the other constructs to merge in
	 * @throws TopicMapStoreException
	 *             thrown if the operation fails or is not supported
	 */
	<T extends Construct> void doMerge(final T context, final T... others) throws TopicMapStoreException;

	/**
	 * Operation method to delete a construct from the store.
	 * 
	 * @param context
	 *            the construct to remove
	 * @param paramType
	 *            the parameter specify the content to remove
	 * @param params
	 *            an array of arguments
	 * @throws TopicMapStoreException
	 *             thrown if the operation fails or is not supported
	 */
	void doRemove(final IConstruct context, TopicMapStoreParameterType paramType, Object... params) throws TopicMapStoreException;

	/**
	 * Operation method to delete a construct from the store.
	 * 
	 * @param context
	 *            the construct to remove
	 * @param cascade
	 *            flag indicates if all dependent constructs should be removed too
	 * @throws TopicMapStoreException
	 *             thrown if the operation fails or is not supported
	 */
	void doRemove(final IConstruct context, boolean cascade) throws TopicMapStoreException;

	/**
	 * Operation method to read some informations form the store
	 * 
	 * @param context
	 *            the context
	 * @param paramType
	 *            the parameter specify the content to read
	 * @param params
	 *            an array of arguments
	 * @return the result of this operation
	 * @throws TopicMapStoreException
	 *             thrown if the operation fails or is not supported
	 */
	Object doRead(final IConstruct context, TopicMapStoreParameterType paramType, Object... params) throws TopicMapStoreException;

	/**
	 * Operation method to create a new information item
	 * 
	 * @param context
	 *            the context
	 * @param paramType
	 *            the parameter specify the content to read
	 * @param params
	 *            an array of arguments
	 * @return the create construct
	 * @throws TopicMapStoreException
	 *             thrown if the operation fails or is not supported
	 */
	Object doCreate(final IConstruct context, TopicMapStoreParameterType paramType, Object... params) throws TopicMapStoreException;

	/**
	 * Operation method to add or set some information items to a construct of the store
	 * 
	 * @param context
	 *            the context
	 * @param paramType
	 *            the parameter specify the content to modify
	 * @param params
	 *            an array of arguments
	 * @throws TopicMapStoreException
	 *             thrown if the operation fails or is not supported
	 */
	void doModify(final IConstruct context, TopicMapStoreParameterType paramType, Object... params) throws TopicMapStoreException;

	/**
	 * Registers the listener to the topic map.
	 * 
	 * @param listener
	 *            the listener to register
	 */
	public void addTopicMapListener(ITopicMapListener listener);

	/**
	 * Removes the listener to the topic map.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeTopicMapListener(ITopicMapListener listener);

	/**
	 * Returns the internal index instance for the given class attribute.
	 * 
	 * @param <I>
	 *            the index type
	 * @param clazz
	 *            the class of the index
	 * @return the index instance and never null
	 */
	public <I extends Index> I getIndex(Class<I> clazz);

	/**
	 * Creating a new transaction.
	 * 
	 * @return the created transaction
	 */
	public ITransaction createTransaction();

	/**
	 * Checks if the underlying store support transactions
	 * 
	 * @return transaction support
	 */
	public boolean isTransactable();

	/**
	 * Return the internal topic map instance of this store.
	 * 
	 * @return the topic map
	 */
	public ITopicMap getTopicMap();

	/**
	 * Method commit all changes of every queue of the topic map store. The calling thread will be blocked until the
	 * changes are committed.
	 */
	public void commit();

	/**
	 * Method removes all duplicated from the topic map
	 */
	public void removeDuplicates();

	/**
	 * Removes everything from the topic map store
	 */
	public void clear();

	/**
	 * Method to enable or disable the internal revision management mechanism.
	 * 
	 * @param enabled
	 *            <code>true</code> if the revision management should be enabled, <code>false</code> otherwise.
	 * @throws TopicMapStoreException
	 *             thrown if the topic map store does not support history management
	 */
	public void enableRevisionManagement(boolean enabled) throws TopicMapStoreException;

	/**
	 * Returns the current state of revision management.
	 * 
	 * @return <code>true</code> if the revision management feature will be supported by the current topic map store and
	 *         the management is enabled, <code>false</code> otherwise.
	 */
	public boolean isRevisionManagementEnabled();

	/**
	 * Return the construct factory to create constructs for the current topic map store instance.
	 * 
	 * @return the internal construct factory
	 */
	public IConstructFactory getConstructFactory();

	/**
	 * Method returns the internal state of caching.
	 * 
	 * @return <code>true</code> if caching is enabled, <code>false</code> otherwise.
	 */
	public boolean isCachingEnabled();

	/**
	 * Enable the caching mechanism of the database topic map store. If the caching is enabled, the cache stores any
	 * read access and deliver the values from cache instead calling the database. The cache will be updated
	 * automatically. If the cache is disabled, it will be destroyed. Any cached values are lost.
	 * 
	 * <p>
	 * <b>Hint:</b> If the topic map store does not support caching, the method has no effect.
	 * </p>
	 * 
	 * @param enable
	 *            <code>true</code> to enable the cache, <code>false</code> to disable it
	 */
	public void enableCaching(boolean enable);

	/**
	 * Returns the meta data instance of this topic map store.
	 * 
	 * @return the meta data
	 * @since 1.1.2
	 */
	public ITopicMapStoreMetaData getMetaData();

	/**
	 * Returns the internal identity of the topic map
	 * 
	 * @return the identity
	 */
	public ITopicMapStoreIdentity getTopicMapIdentity();

	/**
	 * Returns the base locator of the topic map
	 * 
	 * @return the base locator
	 * @since 1.1.4
	 */
	public ILocator getTopicMapBaseLocator();

	/**
	 * The topic map base locator reference
	 * 
	 * @return the topicMapBaseLocator
	 * @since 1.1.4
	 */
	public String getTopicMapBaseLocatorReference();

	/**
	 * Generates a new Id
	 * 
	 * @since 1.2.0
	 * @return the new id
	 */
	public long generateId();
}
