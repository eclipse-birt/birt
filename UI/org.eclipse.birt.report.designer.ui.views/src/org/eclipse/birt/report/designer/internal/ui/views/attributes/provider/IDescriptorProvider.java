
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.model.api.activity.SemanticException;

public interface IDescriptorProvider {

	Object load();

	void save(Object value) throws SemanticException;

	void setInput(Object input);

	String getDisplayName();

	boolean canReset();

	void reset() throws SemanticException;
}
