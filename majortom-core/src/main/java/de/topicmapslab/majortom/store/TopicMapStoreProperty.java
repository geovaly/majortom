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
package de.topicmapslab.majortom.store;

public interface TopicMapStoreProperty {

	public static final String PREFIX = "de.topicmapslab.majortom";

	public static final String TOPICMAPSTORE_CLASS = PREFIX + ".topicmapstore.class";	
	
	public static final String THREADPOOL_MAXIMUM = PREFIX + ".threadpool.maximum";
	
	public static final String INITIAL_COLLECTION_CAPACITY = PREFIX + ".collection.capacity";
	
	public static final String MAP_IMPLEMENTATION_CLASS = PREFIX + ".collection.map";
	
	public static final String SET_IMPLEMENTATION_CLASS = PREFIX + ".collection.set";
	
}
