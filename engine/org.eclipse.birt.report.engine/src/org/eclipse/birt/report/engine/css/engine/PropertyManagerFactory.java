package org.eclipse.birt.report.engine.css.engine;


public interface PropertyManagerFactory {
	
    /**
     * Returns the number of properties.
     */
    abstract public int getNumberOfProperties();

    /**
     * Returns the property index, or -1.
     */
    abstract public int getPropertyIndex(String name);
    /**
     * Returns the ValueManagers.
     */
    abstract public ValueManager getValueManager(int idx);

    /**
     * Returns the name of the property at the given index.
     */
    abstract public String getPropertyName(int idx);
}
