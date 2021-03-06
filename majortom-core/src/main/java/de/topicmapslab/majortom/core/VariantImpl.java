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
package de.topicmapslab.majortom.core;

import de.topicmapslab.majortom.model.core.IName;
import de.topicmapslab.majortom.model.core.IVariant;
import de.topicmapslab.majortom.model.store.ITopicMapStoreIdentity;

/**
 * Base Implementation of {@link IVariant}.
 * 
 * @author Sven Krosse
 * 
 */
public class VariantImpl extends DataTypeAwareImpl implements IVariant {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5589243111079475788L;

	/**
	 * constructor
	 * 
	 * @param identity
	 *            the identity of the topic map store
	 * @param parent
	 */
	protected VariantImpl(ITopicMapStoreIdentity identity, IName parent) {
		super(identity, parent.getTopicMap(), parent);
	}

	/**
	 * {@inheritDoc}
	 */
	public IName getParent() {
		return (IName) super.getParent();
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "Topic-Name-Variant{Parent:" + (getParent() == null ? "null" : getParent().toString()) + ";Value:" + getValue() + ";Datatype:"
				+ getDatatype().toExternalForm() + "}";
	}
}
