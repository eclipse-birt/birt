
package org.eclipse.birt.data.engine.api;

import java.util.List;

public interface IBaseLinkDefinition
{

    public String getLeftDataSet( );

    public List<String> getLeftColumns( );

    public String getRightDataSet( );

    public List<String> getRightColumns( );

    public String getJoinType( );

    public void setJoinType( String joinType );

    /**
     * Name of the join types.
     * They should be the same as those in DesignChoiceConstants.java
     */
    public static final String JOIN_TYPE_INNER = "inner"; //$NON-NLS-1$
    public static final String JOIN_TYPE_LEFT_OUT = "left-out"; //$NON-NLS-1$
    public static final String JOIN_TYPE_RIGHT_OUT = "right-out"; //$NON-NLS-1$
    public static final String JOIN_TYPE_FULL_OUT = "full-out"; //$NON-NLS-1$

}
