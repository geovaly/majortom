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
package de.topicmapslab.majortom.tests.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.tmapi.core.Locator;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.Topic;

import de.topicmapslab.geotype.wgs84.Wgs84Circuit;
import de.topicmapslab.geotype.wgs84.Wgs84Coordinate;
import de.topicmapslab.geotype.wgs84.Wgs84Degree;
import de.topicmapslab.majortom.model.core.IOccurrence;
import de.topicmapslab.majortom.model.core.IVariant;
import de.topicmapslab.majortom.model.namespace.Namespaces;
import de.topicmapslab.majortom.tests.MaJorToMTestCase;
import de.topicmapslab.majortom.util.DatatypeAwareUtils;

/**
 * Testcase
 * 
 * @author Sven Krosse
 * 
 */
public class TestDatattypeAwareImpl extends MaJorToMTestCase {

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.core.DataTypeAwareImpl#booleanValue()}.
	 */
	public void testBooleanValue() throws Exception {
		Topic topic = createTopic();
		IOccurrence occurrence = (IOccurrence) topic.createOccurrence(createTopic(), "true", new Topic[0]);
		assertTrue(occurrence.getValue().equalsIgnoreCase("true"));
		assertFalse(occurrence.getValue().equals(true));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.BOOLEAN)));
		assertTrue(occurrence.booleanValue());

		try {
			occurrence.setValue((Boolean) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		occurrence.setValue(false);
		assertTrue(occurrence.getValue().equalsIgnoreCase("false"));
		assertFalse(occurrence.getValue().equals(true));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.BOOLEAN)));
		assertFalse(occurrence.booleanValue());

		IVariant variant = (IVariant) topic.createName("name", new Topic[0]).createVariant("true", createTopic());
		assertTrue(variant.getValue().equalsIgnoreCase("true"));
		assertFalse(variant.getValue().equals(true));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.BOOLEAN)));
		assertTrue(variant.booleanValue());

		try {
			variant.setValue((Boolean) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		variant.setValue(false);
		assertTrue(variant.getValue().equalsIgnoreCase("false"));
		assertFalse(variant.getValue().equals(true));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.BOOLEAN)));
		assertFalse(variant.booleanValue());

	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.core.DataTypeAwareImpl#coordinateValue()}
	 * .
	 */
	public void testCoordinateValue() throws Exception {
		Topic topic = createTopic();
		Wgs84Degree lng = new Wgs84Degree(12.263102);
		Wgs84Degree lat = new Wgs84Degree(50.430539);
		Wgs84Coordinate coord = new Wgs84Coordinate(lng, lat);
		final String ref = coord.toString();
		IOccurrence occurrence = (IOccurrence) topic.createOccurrence(createTopic(), ref, new Topic[0]);
		assertTrue(occurrence.getValue().equalsIgnoreCase(ref));
		assertFalse(occurrence.getValue().equals(coord));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.WGS84_COORDINATE)));
		assertTrue(occurrence.coordinateValue().equals(coord));

		try {
			occurrence.setValue((Wgs84Coordinate) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		occurrence.setValue(coord);
		assertTrue(occurrence.getValue().equalsIgnoreCase(ref));
		assertFalse(occurrence.getValue().equals(coord));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.WGS84_COORDINATE)));
		assertTrue(occurrence.coordinateValue().equals(coord));

		IVariant variant = (IVariant) topic.createName("name", new Topic[0]).createVariant(ref, createTopic());
		assertTrue(variant.getValue().equalsIgnoreCase(ref));
		assertFalse(variant.getValue().equals(coord));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.WGS84_COORDINATE)));
		assertTrue(variant.coordinateValue().equals(coord));

		try {
			variant.setValue((Wgs84Coordinate) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		variant.setValue(coord);
		assertTrue(variant.getValue().equalsIgnoreCase(ref));
		assertFalse(variant.getValue().equals(coord));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.WGS84_COORDINATE)));
		assertTrue(variant.coordinateValue().equals(coord));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.core.DataTypeAwareImpl#dateTimeValue()}.
	 */
	public void testDateTimeValue() throws Exception {
		Topic topic = createTopic();
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat dateTimeFormat = DatatypeAwareUtils.getDateTimeFormat();
		final String ref = dateTimeFormat.format(calendar.getTime());
		IOccurrence occurrence = (IOccurrence) topic.createOccurrence(createTopic(), ref, new Topic[0]);
		assertTrue(occurrence.getValue().equalsIgnoreCase(ref));
		assertFalse(occurrence.getValue().equals(calendar));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.DATETIME)));
		assertTrue(occurrence.dateTimeValue().equals(calendar));

		try {
			occurrence.setValue((Calendar) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		occurrence.setValue(calendar);
		assertEquals(ref, occurrence.getValue());
		assertFalse(occurrence.getValue().equals(calendar));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.DATETIME)));
		assertTrue(occurrence.dateTimeValue().equals(calendar));

		IVariant variant = (IVariant) topic.createName("name", new Topic[0]).createVariant(ref, createTopic());
		assertTrue(variant.getValue().equalsIgnoreCase(ref));
		assertFalse(variant.getValue().equals(calendar));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.DATETIME)));
		assertTrue(variant.dateTimeValue().equals(calendar));

		try {
			variant.setValue((Calendar) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		variant.setValue(calendar);
		assertTrue(variant.getValue().equalsIgnoreCase(ref));
		assertFalse(variant.getValue().equals(calendar));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.DATETIME)));
		assertTrue(variant.dateTimeValue().equals(calendar));

		calendar = new GregorianCalendar(2000, 1, 1, 1, 1, 1);
		occurrence.setValue(calendar);
		for (int field : new int[] { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND,
				Calendar.MILLISECOND, Calendar.ZONE_OFFSET }) {
			int oldValue = calendar.get(field);
			calendar.set(field, oldValue + 1);
			assertEquals(oldValue + 1, calendar.get(field));
			assertEquals(oldValue, occurrence.dateTimeValue().get(field));
		}
	}

	/**
	 * Test immutable calendar instance
	 * 
	 * @throws Exception
	 */
	public void testImmutableCalendar() throws Exception {
		Topic topic = createTopic();
		/*
		 * check for occurrence
		 */
		IOccurrence occurrence = (IOccurrence) topic.createOccurrence(createTopic(), "value", new Topic[0]);
		Calendar calendar = new GregorianCalendar(2000, 1, 1, 1, 1, 1);
		occurrence.setValue(calendar);
		/*
		 * modify field values of initial calendar instance
		 */
		for (int field : new int[] { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND,
				Calendar.MILLISECOND, Calendar.ZONE_OFFSET }) {
			int oldValue = calendar.get(field);
			calendar.set(field, oldValue + 1);
			assertEquals(oldValue + 1, calendar.get(field));
			assertEquals(oldValue, occurrence.dateTimeValue().get(field));
		}

		/*
		 * modify field values of returned calendar instance
		 */
		for (int field : new int[] { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND,
				Calendar.MILLISECOND, Calendar.ZONE_OFFSET }) {
			calendar = occurrence.dateTimeValue();
			int oldValue = calendar.get(field);
			calendar.set(field, oldValue + 1);
			assertEquals(oldValue + 1, calendar.get(field));
			assertEquals(oldValue, occurrence.dateTimeValue().get(field));
		}
		/*
		 * check for variants
		 */
		IVariant variant = (IVariant) topic.createName("name", new Topic[0]).createVariant("Value", createTopic());
		calendar = new GregorianCalendar(2000, 1, 1, 1, 1, 1);
		variant.setValue(calendar);
		/*
		 * modify field values of initial calendar instance
		 */
		for (int field : new int[] { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND,
				Calendar.MILLISECOND, Calendar.ZONE_OFFSET }) {
			int oldValue = calendar.get(field);
			calendar.set(field, oldValue + 1);
			assertEquals(oldValue + 1, calendar.get(field));
			assertEquals(oldValue, variant.dateTimeValue().get(field));
		}

		/*
		 * modify field values of returned calendar instance
		 */
		for (int field : new int[] { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND,
				Calendar.MILLISECOND, Calendar.ZONE_OFFSET }) {
			calendar = variant.dateTimeValue();
			int oldValue = calendar.get(field);
			calendar.set(field, oldValue + 1);
			assertEquals(oldValue + 1, calendar.get(field));
			assertEquals(oldValue, variant.dateTimeValue().get(field));
		}
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.core.DataTypeAwareImpl#doubleValue()}.
	 */
	public void testDoubleValue() throws Exception {
		Topic topic = createTopic();
		IOccurrence occurrence = (IOccurrence) topic.createOccurrence(createTopic(), "12.5", new Topic[0]);
		assertTrue(occurrence.getValue().equalsIgnoreCase("12.5"));
		assertFalse(occurrence.getValue().equals(12.5));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.DOUBLE)));
		assertTrue(occurrence.doubleValue().equals(12.5D));

		try {
			occurrence.setValue((Double) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		occurrence.setValue(12.5D);
		assertTrue(occurrence.getValue().equalsIgnoreCase("12.5"));
		assertFalse(occurrence.getValue().equals(12.5));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.DOUBLE)));
		assertTrue(occurrence.doubleValue().equals(12.5D));

		IVariant variant = (IVariant) topic.createName("name", new Topic[0]).createVariant("12.5", createTopic());
		assertTrue(variant.getValue().equalsIgnoreCase("12.5"));
		assertFalse(variant.getValue().equals(12.5));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.DOUBLE)));
		assertTrue(variant.doubleValue().equals(12.5D));

		try {
			variant.setValue((Double) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		variant.setValue(12.5);
		assertTrue(variant.getValue().equalsIgnoreCase("12.5"));
		assertFalse(variant.getValue().equals(12.5));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.DOUBLE)));
		assertTrue(variant.doubleValue().equals(12.5D));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.core.DataTypeAwareImpl#uriValue()}.
	 */
	public void testUriValue() throws Exception {
		Topic topic = createTopic();
		String ref = "http://www.example.org";
		URI uri = new URI(ref);
		IOccurrence occurrence = (IOccurrence) topic.createOccurrence(createTopic(), ref, new Topic[0]);
		assertTrue(occurrence.getValue().equalsIgnoreCase(ref));
		assertFalse(occurrence.getValue().equals(uri));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.ANYURI)));
		assertTrue(occurrence.uriValue().equals(uri));

		try {
			occurrence.setValue((URI) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		occurrence.setValue(uri);
		assertTrue(occurrence.getValue().equalsIgnoreCase(ref));
		assertFalse(occurrence.getValue().equals(uri));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.ANYURI)));
		assertTrue(occurrence.uriValue().equals(uri));

		IVariant variant = (IVariant) topic.createName("name", new Topic[0]).createVariant(ref, createTopic());
		assertTrue(variant.getValue().equalsIgnoreCase(ref));
		assertFalse(variant.getValue().equals(uri));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.ANYURI)));
		assertTrue(variant.uriValue().equals(uri));

		try {
			variant.setValue((URI) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		variant.setValue(uri);
		assertTrue(variant.getValue().equalsIgnoreCase(ref));
		assertFalse(variant.getValue().equals(uri));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.ANYURI)));
		assertTrue(variant.uriValue().equals(uri));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.core.DataTypeAwareImpl#decimalValue()}.
	 */
	public void testDecimalValue() {
		Topic topic = createTopic();
		BigDecimal bigDec = BigDecimal.valueOf(12.5D);
		IOccurrence occurrence = (IOccurrence) topic.createOccurrence(createTopic(), "12.5", new Topic[0]);
		assertTrue(occurrence.getValue().equalsIgnoreCase("12.5"));
		assertFalse(occurrence.getValue().equals(bigDec));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.DECIMAL)));
		assertTrue(occurrence.decimalValue().equals(bigDec));

		try {
			occurrence.setValue((BigDecimal) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		occurrence.setValue(bigDec);
		assertTrue(occurrence.getValue().equalsIgnoreCase("12.5"));
		assertFalse(occurrence.getValue().equals(bigDec));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.DECIMAL)));
		assertTrue(occurrence.decimalValue().equals(bigDec));

		IVariant variant = (IVariant) topic.createName("name", new Topic[0]).createVariant("12.5", createTopic());
		assertTrue(variant.getValue().equalsIgnoreCase("12.5"));
		assertFalse(variant.getValue().equals(bigDec));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.DECIMAL)));
		assertTrue(variant.decimalValue().equals(bigDec));

		try {
			variant.setValue((BigDecimal) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		variant.setValue(bigDec);
		assertTrue(variant.getValue().equalsIgnoreCase("12.5"));
		assertFalse(variant.getValue().equals(bigDec));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.DECIMAL)));
		assertTrue(variant.decimalValue().equals(bigDec));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.core.DataTypeAwareImpl#floatValue()}.
	 */
	public void testFloatValue() {
		Topic topic = createTopic();
		IOccurrence occurrence = (IOccurrence) topic.createOccurrence(createTopic(), "12.5", new Topic[0]);
		assertTrue(occurrence.getValue().equalsIgnoreCase("12.5"));
		assertFalse(occurrence.getValue().equals(12.5));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.FLOAT)));
		assertTrue(occurrence.floatValue() == 12.5);

		occurrence.setValue(12.5F);
		assertTrue(occurrence.getValue().equalsIgnoreCase("12.5"));
		assertFalse(occurrence.getValue().equals(12.5));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.FLOAT)));
		assertTrue(occurrence.floatValue() == 12.5);

		IVariant variant = (IVariant) topic.createName("name", new Topic[0]).createVariant("12.5", createTopic());
		assertTrue(variant.getValue().equalsIgnoreCase("12.5"));
		assertFalse(variant.getValue().equals(12.5));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.FLOAT)));
		assertTrue(variant.floatValue() == 12.5);

		variant.setValue(12.5F);
		assertTrue(variant.getValue().equalsIgnoreCase("12.5"));
		assertFalse(variant.getValue().equals(12.5));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.FLOAT)));
		assertTrue(variant.floatValue() == 12.5);
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.core.DataTypeAwareImpl#intValue()}.
	 */
	public void testIntValue() {
		Topic topic = createTopic();
		IOccurrence occurrence = (IOccurrence) topic.createOccurrence(createTopic(), "12", new Topic[0]);
		assertTrue(occurrence.getValue().equalsIgnoreCase("12"));
		assertFalse(occurrence.getValue().equals(12));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.INT)));
		assertTrue(occurrence.intValue() == 12);

		occurrence.setValue(12);
		assertTrue(occurrence.getValue().equalsIgnoreCase("12"));
		assertFalse(occurrence.getValue().equals(12));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.INT)));
		assertTrue(occurrence.intValue() == 12);

		IVariant variant = (IVariant) topic.createName("name", new Topic[0]).createVariant("12", createTopic());
		assertTrue(variant.getValue().equalsIgnoreCase("12"));
		assertFalse(variant.getValue().equals(12));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.INT)));
		assertTrue(variant.intValue() == 12);

		variant.setValue(12);
		assertTrue(variant.getValue().equalsIgnoreCase("12"));
		assertFalse(variant.getValue().equals(12));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.INT)));
		assertTrue(variant.intValue() == 12);
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.core.DataTypeAwareImpl#integerValue()}.
	 */
	public void testIntegerValue() {
		Topic topic = createTopic();
		BigInteger bigInt = BigInteger.valueOf(12);
		IOccurrence occurrence = (IOccurrence) topic.createOccurrence(createTopic(), "12", new Topic[0]);
		assertTrue(occurrence.getValue().equalsIgnoreCase("12"));
		assertFalse(occurrence.getValue().equals(bigInt));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.INTEGER)));
		assertTrue(occurrence.integerValue().equals(bigInt));

		try {
			occurrence.setValue((BigInteger) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		occurrence.setValue(bigInt);
		assertTrue(occurrence.getValue().equalsIgnoreCase("12"));
		assertFalse(occurrence.getValue().equals(bigInt));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.INTEGER)));
		assertTrue(occurrence.integerValue().equals(bigInt));

		IVariant variant = (IVariant) topic.createName("name", new Topic[0]).createVariant("12", createTopic());
		assertTrue(variant.getValue().equalsIgnoreCase("12"));
		assertFalse(variant.getValue().equals(bigInt));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.INTEGER)));
		assertTrue(variant.integerValue().equals(bigInt));

		try {
			variant.setValue((BigInteger) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		variant.setValue(bigInt);
		assertTrue(variant.getValue().equalsIgnoreCase("12"));
		assertFalse(variant.getValue().equals(bigInt));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.INTEGER)));
		assertTrue(variant.integerValue().equals(bigInt));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.core.DataTypeAwareImpl#locatorValue()}.
	 */
	public void testLocatorValue() {
		Topic topic = createTopic();
		Locator locator = topicMap.createLocator("http://www.example.org");
		IOccurrence occurrence = (IOccurrence) topic.createOccurrence(createTopic(), locator.getReference(), new Topic[0]);
		assertTrue(occurrence.getValue().equalsIgnoreCase(locator.getReference()));
		assertFalse(occurrence.getValue().equals(locator));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.ANYURI)));
		assertTrue(occurrence.locatorValue().equals(locator));

		try {
			occurrence.setValue((Locator) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		occurrence.setValue(locator);
		assertTrue(occurrence.getValue().equalsIgnoreCase(locator.getReference()));
		assertFalse(occurrence.getValue().equals(locator));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.ANYURI)));
		assertTrue(occurrence.locatorValue().equals(locator));

		IVariant variant = (IVariant) topic.createName("name", new Topic[0]).createVariant(locator.getReference(), createTopic());
		assertTrue(variant.getValue().equalsIgnoreCase(locator.getReference()));
		assertFalse(variant.getValue().equals(locator));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.ANYURI)));
		assertTrue(variant.locatorValue().equals(locator));

		try {
			variant.setValue((Locator) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		variant.setValue(locator);
		assertTrue(variant.getValue().equalsIgnoreCase(locator.getReference()));
		assertFalse(variant.getValue().equals(locator));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.ANYURI)));
		assertTrue(variant.locatorValue().equals(locator));
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.core.DataTypeAwareImpl#longValue()}.
	 */
	public void testLongValue() {
		Topic topic = createTopic();
		IOccurrence occurrence = (IOccurrence) topic.createOccurrence(createTopic(), "12", new Topic[0]);
		assertTrue(occurrence.getValue().equalsIgnoreCase("12"));
		assertFalse(occurrence.getValue().equals(12L));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.LONG)));
		assertTrue(occurrence.longValue() == 12);

		occurrence.setValue(12L);
		assertTrue(occurrence.getValue().equalsIgnoreCase("12"));
		assertFalse(occurrence.getValue().equals(12L));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.LONG)));
		assertTrue(occurrence.longValue() == 12);

		IVariant variant = (IVariant) topic.createName("name", new Topic[0]).createVariant("12", createTopic());
		assertTrue(variant.getValue().equalsIgnoreCase("12"));
		assertFalse(variant.getValue().equals(12L));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.LONG)));
		assertTrue(variant.longValue() == 12);

		variant.setValue(12L);
		assertTrue(variant.getValue().equalsIgnoreCase("12"));
		assertFalse(variant.getValue().equals(12L));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.LONG)));
		assertTrue(variant.longValue() == 12);
	}

	/**
	 * Test method for
	 * {@link de.topicmapslab.majortom.core.DataTypeAwareImpl#surfaceValue()}.
	 */
	public void testSurfaceValue() throws Exception {
		Topic topic = createTopic();
		Wgs84Degree lng = new Wgs84Degree(12.263102);
		Wgs84Degree lat = new Wgs84Degree(50.430539);
		Wgs84Coordinate coord = new Wgs84Coordinate(lng, lat);
		Wgs84Circuit circuit = new Wgs84Circuit(coord, 1000D);
		final String ref = circuit.toString();
		IOccurrence occurrence = (IOccurrence) topic.createOccurrence(createTopic(), ref, new Topic[0]);
		assertTrue(occurrence.getValue().equalsIgnoreCase(ref));
		assertFalse(occurrence.getValue().equals(circuit));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.GEOSURFACE)));
		assertTrue(occurrence.surfaceValue().equals(circuit));

		try {
			occurrence.setValue((Wgs84Circuit) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		occurrence.setValue(circuit);
		assertTrue(occurrence.getValue().equalsIgnoreCase(ref));
		assertFalse(occurrence.getValue().equals(circuit));
		assertFalse(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(occurrence.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.GEOSURFACE)));
		assertTrue(occurrence.surfaceValue().equals(circuit));

		IVariant variant = (IVariant) topic.createName("name", new Topic[0]).createVariant(ref, createTopic());
		assertTrue(variant.getValue().equalsIgnoreCase(ref));
		assertFalse(variant.getValue().equals(circuit));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.GEOSURFACE)));
		assertTrue(variant.surfaceValue().equals(circuit));

		try {
			variant.setValue((Wgs84Circuit) null);
			fail("Null is not allowed!");
		} catch (ModelConstraintException e) {
			// NOTHING TO DO
		}

		variant.setValue(circuit);
		assertTrue(variant.getValue().equalsIgnoreCase(ref));
		assertFalse(variant.getValue().equals(circuit));
		assertFalse(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.STRING)));
		assertTrue(variant.getDatatype().equals(topicMap.createLocator(Namespaces.XSD.GEOSURFACE)));
		assertTrue(variant.surfaceValue().equals(circuit));
	}

}
