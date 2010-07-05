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
package de.topicmapslab.majortom.model.comparator;

import java.util.Comparator;

import org.tmapi.core.Role;

/**
 * association role comparator
 * 
 * @author Sven Krosse
 * 
 */
public class RoleComparator implements Comparator<Role> {

	private final boolean ascending;

	/**
	 * constructor
	 * 
	 * @param ascending
	 *            sorting order ascending?
	 */
	public RoleComparator(boolean ascending) {
		this.ascending = ascending;
	}

	/**
	 * Compare two association items.
	 * 
	 * <p>
	 * Return <code>0</code> if both associations roles have the same type and
	 * player (lexicographical comparison of identities).
	 * </p>
	 * <p>
	 * Return <code>-1</code> if the type or player of the first role is higher
	 * (lexicographical comparison of identities).
	 * </p>
	 * <p>
	 * Return <code>1</code> if the type or player of the second role is higher
	 * (lexicographical comparison of identities).
	 * </p>
	 */
	public int compare(Role o1, Role o2) {
		int compare = new TopicByIdentityComparator(ascending).compare(o1.getType(), o2.getType());
		if (compare == 0) {
			compare = new TopicByIdentityComparator(ascending).compare(o1.getPlayer(), o2.getPlayer());
		}
		return compare;
	}

}
