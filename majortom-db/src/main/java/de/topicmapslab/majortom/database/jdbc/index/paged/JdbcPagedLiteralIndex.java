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
package de.topicmapslab.majortom.database.jdbc.index.paged;

import java.net.URI;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Variant;

import de.topicmapslab.geotype.wgs84.Wgs84Coordinate;
import de.topicmapslab.majortom.database.jdbc.model.ISession;
import de.topicmapslab.majortom.database.store.JdbcTopicMapStore;
import de.topicmapslab.majortom.index.paged.PagedLiteralIndexImpl;
import de.topicmapslab.majortom.model.core.ICharacteristics;
import de.topicmapslab.majortom.model.core.IDatatypeAware;
import de.topicmapslab.majortom.model.exception.TopicMapStoreException;
import de.topicmapslab.majortom.model.index.ILiteralIndex;
import de.topicmapslab.majortom.model.index.paging.IPagedLiteralIndex;
import de.topicmapslab.majortom.model.namespace.Namespaces;
import de.topicmapslab.majortom.util.DatatypeAwareUtils;
import de.topicmapslab.majortom.util.HashUtil;

/**
 * Implementation class of {@link IPagedLiteralIndex} of the Jdbc Topic Map Store.
 * 
 * @author Sven Krosse
 * 
 */
public class JdbcPagedLiteralIndex extends PagedLiteralIndexImpl<JdbcTopicMapStore> {

	/**
	 * constructor
	 * 
	 * @param store
	 *            the store
	 * @param parentIndex
	 *            the parent index ( non-paged index)
	 */
	public JdbcPagedLiteralIndex(JdbcTopicMapStore store, ILiteralIndex parentIndex) {
		super(store, parentIndex);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetBooleans(boolean value, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetBooleans(value, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetBooleans(boolean value, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getOccurrences(getTopicMapStore().getTopicMap(), Boolean.toString(value), Namespaces.XSD.BOOLEAN, offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetCharacteristics(Locator datatype, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetCharacteristics(datatype, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetCharacteristics(Locator datatype, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getCharacteristicsByDatatype(getTopicMapStore().getTopicMap(), datatype.getReference(), offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetCharacteristics(String value, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetCharacteristics(value, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetCharacteristics(String value, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getCharacteristics(getTopicMapStore().getTopicMap(), value, offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetCharacteristics(String value, Locator datatype, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetCharacteristics(value, datatype, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetCharacteristics(String value, Locator datatype, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getCharacteristics(getTopicMapStore().getTopicMap(), value, offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetCharacteristicsMatches(Pattern regExp, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetCharacteristicsMatches(regExp, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetCharacteristicsMatches(Pattern regExp, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getCharacteristicsByPattern(getTopicMapStore().getTopicMap(), regExp.pattern(), offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetCharacteristicsMatches(Pattern regExp, Locator datatype, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetCharacteristicsMatches(regExp, datatype, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetCharacteristicsMatches(Pattern regExp, Locator datatype, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getCharacteristicsByPattern(getTopicMapStore().getTopicMap(), regExp.pattern(), datatype.getReference(), offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Currently not supported.
	 * </p>
	 */
	protected List<ICharacteristics> doGetCoordinates(Wgs84Coordinate value, double deviance, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetCoordinates(value, deviance, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Currently not supported.
	 * </p>
	 */
	protected List<ICharacteristics> doGetCoordinates(Wgs84Coordinate value, double deviance, int offset, int limit) {
		return super.doGetCoordinates(value, deviance, offset, limit);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetCoordinates(Wgs84Coordinate value, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetCoordinates(value, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetCoordinates(Wgs84Coordinate value, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getOccurrences(getTopicMapStore().getTopicMap(), value.toString(), Namespaces.XSD.WGS84_COORDINATE, offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<IDatatypeAware> doGetDatatypeAwares(Locator dataType, int offset, int limit, Comparator<IDatatypeAware> comparator) {
		return super.doGetDatatypeAwares(dataType, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<IDatatypeAware> doGetDatatypeAwares(Locator dataType, int offset, int limit) {
		try {
			List<IDatatypeAware> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getDatatypeAwaresByDatatype(getTopicMapStore().getTopicMap(), dataType.getReference(), offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetDateTime(Calendar value, Calendar deviance, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetDateTime(value, deviance, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetDateTime(Calendar value, Calendar deviance, int offset, int limit) {
		try {
			Calendar lower = (Calendar) value.clone();
			Calendar upper = (Calendar) value.clone();
			for (int field : new int[] { Calendar.SECOND, Calendar.MINUTE, Calendar.HOUR, Calendar.DAY_OF_MONTH, Calendar.MONTH, Calendar.YEAR }) {
				lower.add(field, -1 * deviance.get(field));
				upper.add(field, deviance.get(field));
			}
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getOccurrences(getTopicMapStore().getTopicMap(), lower, upper, offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetDateTime(Calendar value, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetDateTime(value, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetDateTime(Calendar value, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getOccurrences(getTopicMapStore().getTopicMap(), DatatypeAwareUtils.toString(value, Namespaces.XSD.DATETIME), Namespaces.XSD.DATETIME, offset, limit));
			list.addAll(session.getProcessor().getOccurrences(getTopicMapStore().getTopicMap(), DatatypeAwareUtils.toString(value, Namespaces.XSD.DATE), Namespaces.XSD.DATE, offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetDoubles(double value, double deviance, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetDoubles(value, deviance, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetDoubles(double value, double deviance, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getOccurrences(getTopicMapStore().getTopicMap(), value, deviance, Namespaces.XSD.DOUBLE, offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetDoubles(double value, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetDoubles(value, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetDoubles(double value, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getOccurrences(getTopicMapStore().getTopicMap(), Double.toString(value), Namespaces.XSD.DOUBLE, offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetFloats(float value, double deviance, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetFloats(value, deviance, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetFloats(float value, double deviance, int offset, int limit) {

		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getOccurrences(getTopicMapStore().getTopicMap(), value, deviance, Namespaces.XSD.FLOAT, offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetFloats(float value, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetFloats(value, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetFloats(float value, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getOccurrences(getTopicMapStore().getTopicMap(), Float.toString(value), Namespaces.XSD.FLOAT, offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetIntegers(int value, double deviance, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetIntegers(value, deviance, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetIntegers(int value, double deviance, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getOccurrences(getTopicMapStore().getTopicMap(), value, deviance, Namespaces.XSD.INT, offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetIntegers(int value, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetIntegers(value, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetIntegers(int value, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getOccurrences(getTopicMapStore().getTopicMap(), Integer.toString(value), Namespaces.XSD.INT, offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetLongs(long value, double deviance, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetLongs(value, deviance, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetLongs(long value, double deviance, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getOccurrences(getTopicMapStore().getTopicMap(), value, deviance, Namespaces.XSD.LONG, offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetLongs(long value, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetLongs(value, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetLongs(long value, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getOccurrences(getTopicMapStore().getTopicMap(), Long.toString(value), Namespaces.XSD.LONG, offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<Name> doGetNames(int offset, int limit, Comparator<Name> comparator) {
		return super.doGetNames(offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<Name> doGetNames(int offset, int limit) {
		try {
			List<Name> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getNames(getTopicMapStore().getTopicMap(), offset, limit));
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<Occurrence> doGetOccurrences(int offset, int limit, Comparator<Occurrence> comparator) {
		return super.doGetOccurrences(offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<Occurrence> doGetOccurrences(int offset, int limit) {
		try {
			List<Occurrence> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getOccurrences(getTopicMapStore().getTopicMap(), offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<ICharacteristics> doGetUris(URI value, int offset, int limit, Comparator<ICharacteristics> comparator) {
		return super.doGetUris(value, offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<ICharacteristics> doGetUris(URI value, int offset, int limit) {
		try {
			List<ICharacteristics> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getOccurrences(getTopicMapStore().getTopicMap(), value.toString(), Namespaces.XSD.ANYURI, offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * <b>Hint:</b> Method extracts all items from database to enable the usage of comparators. The operation can be
	 * very slowly.
	 * </p>
	 */
	protected List<Variant> doGetVariants(int offset, int limit, Comparator<Variant> comparator) {
		return super.doGetVariants(offset, limit, comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	protected List<Variant> doGetVariants(int offset, int limit) {
		try {
			List<Variant> list = HashUtil.getList();
			ISession session = getTopicMapStore().openSession();
			list.addAll(session.getProcessor().getVariants(getTopicMapStore().getTopicMap(), offset, limit));
			session.commit();
			session.close();
			return list;
		} catch (SQLException e) {
			throw new TopicMapStoreException("Internal database error!", e);
		}
	}

}
