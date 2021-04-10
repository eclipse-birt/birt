package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;

/**
 * A sub class of SubqueryDefinition. The class provides a view associated with
 * a row ID.
 *
 */
public class SubqueryLocator extends SubqueryDefinition {
	private int rowId;

	/**
	 * Constructs a SubqueryLocator. A row ID must be provided that uniquely
	 * identifies a special view.
	 * 
	 * @param rowId
	 * @param name
	 * @param parent
	 */
	public SubqueryLocator(int rowId, String name, IBaseQueryDefinition parent) {
		super(name, parent);
		this.rowId = rowId;
	}

	/**
	 * Get the row ID.
	 * 
	 * @return
	 */
	public int getRowId() {
		return rowId;
	}

}
