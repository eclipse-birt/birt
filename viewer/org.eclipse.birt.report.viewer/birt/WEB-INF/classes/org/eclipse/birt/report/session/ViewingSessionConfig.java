/*************************************************************************************
 * Copyright (c) 2008 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.session;

import java.io.Serializable;

public class ViewingSessionConfig implements Serializable {
	private static final long serialVersionUID = 6339493364214445787L;

	public enum ViewingSessionPolicy {
		SESSION_POLICY_DISCARD_OLDEST, SESSION_POLICY_DISCARD_NEW
	}

	/**
	 * Default session timeout: 10 minutes
	 */
	public static final long DEFAULT_SESSION_TIMEOUT = 600l;
	public static final int DEFAULT_MINIMUM_SESSION_THRESHOLD = 10;
	public static final int DEFAULT_MAX_SESSION_COUNT = 0;
	public static final float DEFAULT_SESSION_THRESHOLD_FACTOR = 0.75f;
	public static final ViewingSessionPolicy DEFAULT_SESSION_POLICY = ViewingSessionPolicy.SESSION_POLICY_DISCARD_OLDEST;

	/**
	 * Session timeout in seconds.
	 */
	private long sessionTimeout;
	/**
	 * Minimum value for sessionCountThreshold.
	 */
	private int minimumSessionCountThreshold;
	/**
	 * Maximum session count.
	 */
	private int maximumSessionCount;
	/**
	 * 
	 */
	private ViewingSessionPolicy maxSessionCountPolicy;

	/**
	 * Factor that defines how much the sessionCountThreshold will be increased
	 * after cleanup.
	 */
	private float sessionCountThresholdFactor;

	/**
	 * Instantiates a new viewing session configuration.
	 */
	public ViewingSessionConfig() {
		this.sessionTimeout = DEFAULT_SESSION_TIMEOUT;
		this.minimumSessionCountThreshold = DEFAULT_MINIMUM_SESSION_THRESHOLD;
		this.sessionCountThresholdFactor = DEFAULT_SESSION_THRESHOLD_FACTOR;
		this.maximumSessionCount = DEFAULT_MAX_SESSION_COUNT;
		this.maxSessionCountPolicy = DEFAULT_SESSION_POLICY;
	}

	/**
	 * @return the sessionTimeout
	 */
	public long getSessionTimeout() {
		return sessionTimeout;
	}

	/**
	 * Sets the timeout value in seconds after which a viewing session will expire.
	 * The value 0 means that a session will never expire, and the cached files will
	 * never be cleant unless the belonging HTTP session expires.
	 * 
	 * @param sessionTimeout timeout value in seconds
	 */
	public void setSessionTimeout(long sessionTimeout) {
		if (sessionTimeout > 0l) {
			this.sessionTimeout = sessionTimeout;
		} else {
			this.sessionTimeout = DEFAULT_SESSION_TIMEOUT;
		}
	}

	/**
	 * @return the minimumSessionCountThreshold.
	 */
	public int getMinimumSessionCountThreshold() {
		return minimumSessionCountThreshold;
	}

	/**
	 * Sets a session count threshold after which the cleanup process will try to
	 * clean up expired sessions.
	 * 
	 * @param minimumSessionCountThreshold the minimumSessionCountThreshold to set
	 */
	public void setMinimumSessionCountThreshold(int minimumSessionCountThreshold) {
		if (minimumSessionCountThreshold > 0) {
			this.minimumSessionCountThreshold = minimumSessionCountThreshold;
		} else {
			this.minimumSessionCountThreshold = DEFAULT_MINIMUM_SESSION_THRESHOLD;
		}
	}

	/**
	 * @return the maximumSessionCount
	 */
	public int getMaximumSessionCount() {
		return maximumSessionCount;
	}

	/**
	 * Maximum number of simultaneous viewing sessions that can be open at the same
	 * time, to prevent cache pollution through multiple requests. A value of 0
	 * means that there is no limit.
	 * 
	 * @param maximumSessionCount the maximumSessionCount to set
	 */
	public void setMaximumSessionCount(int maximumSessionCount) {
		if (maximumSessionCount >= 0) {
			this.maximumSessionCount = maximumSessionCount;
		} else {
			this.maximumSessionCount = DEFAULT_MAX_SESSION_COUNT;
		}
	}

	/**
	 * @return the maxSessionCountPolicy
	 */
	public ViewingSessionPolicy getMaxSessionCountPolicy() {
		return maxSessionCountPolicy;
	}

	/**
	 * @param maxSessionCountPolicy the maxSessionCountPolicy to set
	 */
	public void setMaxSessionCountPolicy(ViewingSessionPolicy maxSessionCountPolicy) {
		this.maxSessionCountPolicy = maxSessionCountPolicy;
	}

	/**
	 * @return the sessionCountThresholdFactor
	 */
	public float getSessionCountThresholdFactor() {
		return sessionCountThresholdFactor;
	}

	/**
	 * Load factor to recalculate the minimum threshold value based on the remaining
	 * session count after cleanup.
	 * 
	 * @param sessionCountThresholdFactor the sessionCountThresholdFactor to set
	 */
	public void setSessionCountThresholdFactor(float sessionCountThresholdFactor) {
		if (sessionCountThresholdFactor > 0.0f) {
			this.sessionCountThresholdFactor = sessionCountThresholdFactor;
		} else {
			this.sessionCountThresholdFactor = DEFAULT_SESSION_THRESHOLD_FACTOR;
		}
	}

}
