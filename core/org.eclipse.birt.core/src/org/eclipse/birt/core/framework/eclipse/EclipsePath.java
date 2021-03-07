/*
 * Created on 2005-3-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.core.framework.eclipse;

import org.eclipse.birt.core.framework.IPlatformPath;
import org.eclipse.core.runtime.IPath;

/**
 *
 */
public class EclipsePath implements IPlatformPath {
	protected IPath path;

	public EclipsePath(IPath path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return path.toString();
	}
}
