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
package de.topicmapslab.majortom.comparator;

import java.util.Comparator;

import org.tmapi.core.Locator;

/**
 * Locator comparator
 * 
 * @author Sven Krosse
 * 
 */
public class LocatorByReferenceComparator implements Comparator<Locator> {

	private static LocatorByReferenceComparator instanceAsc = null;
	private static LocatorByReferenceComparator instanceDesc = null;
	
	/**
	 * Returns the singleton instance of the comparator
	 * 
	 * @param ascending
	 *            sorting order ascending?
	 * @return the instance the comparator instance
	 */
	public static LocatorByReferenceComparator getInstance(boolean ascending) {
		if (ascending) {
			if (instanceAsc == null) {
				instanceAsc = new LocatorByReferenceComparator(true);
			}
			return instanceAsc;
		}
		if (instanceDesc == null) {
			instanceDesc = new LocatorByReferenceComparator(false);
		}
		return instanceDesc;
	}
	
	private final boolean ascending;

	/**
	 * constructor
	 * 
	 * @param ascending
	 *            sorting order ascending?
	 */
	private LocatorByReferenceComparator(boolean ascending) {
		this.ascending = ascending;
	}

	/**
	 * {@inheritDoc}
	 */
	public int compare(Locator o1, Locator o2) {
		int compare = o1.getReference().compareTo(o2.getReference());
		return ascending ? compare : compare * -1;
	}
}
