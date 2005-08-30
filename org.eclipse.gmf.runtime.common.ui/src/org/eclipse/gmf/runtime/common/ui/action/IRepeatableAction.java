/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;

/**
 * The interface for all actions that could potentially be repeated.
 * 
 * @author khussey
 */
public interface IRepeatableAction {
    /**
     * Enumerated type for work indicator type
     */
    public class WorkIndicatorType extends EnumeratedType {
        private static int nextOrdinal = 0;
    
        /** No work indicator. */
        public static final WorkIndicatorType NONE = new WorkIndicatorType("None"); //$NON-NLS-1$
    
        /** Busy work indicator. */
        public static final WorkIndicatorType BUSY = new WorkIndicatorType("Busy"); //$NON-NLS-1$
    
        /** Progress monitor work indicator. */
        public static final WorkIndicatorType PROGRESS_MONITOR = new WorkIndicatorType("Progress Monitor"); //$NON-NLS-1$
    
        /** Cancelable progress monitor work indicator. */
    	public static final WorkIndicatorType CANCELABLE_PROGRESS_MONITOR = new WorkIndicatorType("Cancelable Progress Monitor"); //$NON-NLS-1$
    	
        /**
         * The list of values for this enumerated type.
         */
        private static final WorkIndicatorType[] VALUES =
            { NONE, BUSY, PROGRESS_MONITOR, CANCELABLE_PROGRESS_MONITOR };
    
        /**
         * Constructor for WorkIndicatorType.
         * @param name The name for the WorkIndicatorType
         * @param ordinal The ordinal for theWorkIndicatorType
         */
        protected WorkIndicatorType(String name, int ordinal) {
            super(name, ordinal);
        }
    
        /**
         * Constructor for WorkIndicatorType.
         * @param name The name for the WorkIndicatorType
         */
        private WorkIndicatorType(String name) {
            this(name, nextOrdinal++);
        }
    
        /**
         * Retrieves the list of constants for this enumerated type.
         * @return The list of constants for this enumerated type.
         */
        protected List getValues() {
            return Collections.unmodifiableList(Arrays.asList(VALUES));
        }
    }

    /**
     * Retrieves the label for this repeatable action.
     * 
     * @return The label for this repeatable action.
     */
    public String getLabel();

    /**
     * Retrieves a Boolean indicating whether this repeatable action can be
     * repeated.
     * 
     * @return <code>true</code> if this repeatable action can be repeated;
     *          <code>false</code> otherwise.
     */
    public boolean isRepeatable();

    /**
     * Retrieves a Boolean indicating whether this repeatable action can be
     * run.
     * 
     * @return <code>true</code> if this repeatable action can be run;
     *          <code>false</code> otherwise.
     */
    public boolean isRunnable();

    /**
     * Refreshes various aspects of this repeatable action, such as its label
     * and whether or not it is enabled.
     */
    public void refresh();
    
    /**
     * Sets up the action. Should always be called before
     * {@link #run(IProgressMonitor)} is called.
     * @return <code>true</code> if the setup completed successfully,
     * 		   <code>false</code> otherwise.
     */
    public boolean setup();

    /**
     * Re-runs this repeatable action.
     * 
     * @param progressMonitor <code>IProgressMonitor</code> monitoring the execution of this action
     */
    public void repeat(IProgressMonitor progressMonitor);

    /**
     * Runs this repeatable action.
     * 
     * @param progressMonitor <code>IProgressMonitor</code> monitoring the execution of this action
     */
    public void run(IProgressMonitor progressMonitor);

    /**
     * Gets type of work indicator (progress monitor, hourglass, or none).
     * 
     * @return type of work indicator
     */
    public WorkIndicatorType getWorkIndicatorType();
}
