/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.cache;

import java.util.concurrent.atomic.AtomicInteger;

public class Cacheable {

	protected FileCacheManager manager;
	protected Comparable cacheKey;
	protected AtomicInteger referenceCount;
	protected Cacheable prev;
	protected Cacheable next;

	public Cacheable(FileCacheManager manager, Comparable cacheKey) {
		this.manager = manager;
		this.cacheKey = cacheKey;
		this.referenceCount = new AtomicInteger(0);
	}

	Cacheable() {
	}

	public Comparable getCacheKey() {
		return cacheKey;
	}

	public AtomicInteger getReferenceCount() {
		return referenceCount;
	}

	public Cacheable getPrev() {
		return prev;
	}

	public Cacheable getNext() {
		return next;
	}

	public void setPrev(Cacheable prev) {
		this.prev = prev;
	}

	public void setNext(Cacheable next) {
		this.next = next;
	}
}
