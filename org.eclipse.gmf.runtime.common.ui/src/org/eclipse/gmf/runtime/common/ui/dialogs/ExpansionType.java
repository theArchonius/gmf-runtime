/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.dialogs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;

/**
 * Expansion type enumeration for SRE.
 * 
 * Incoming, outoging, both incoming and ouotgoing, and all connected.
 * 
 * @author wdiu, Wayne Diu
 */

public class ExpansionType
	extends EnumeratedType {

	/**
	 * An internal unique identifier for selection of elements.
	 */
	private static int nextOrdinal = 0;

	/**
	 * No expansion type. Do not expand.
	 */
	public static final ExpansionType NONE = new ExpansionType("None"); //$NON-NLS-1$

	/**
	 * Incoming relationships
	 */
	public static final ExpansionType INCOMING = new ExpansionType("Incoming"); //$NON-NLS-1$

	/**
	 * Outgoing relationships
	 */
	public static final ExpansionType OUTGOING = new ExpansionType("Outgoing"); //$NON-NLS-1$

	/**
	 * Incoming and outgoing relationships
	 */
	public static final ExpansionType BOTH = new ExpansionType("Both"); //$NON-NLS-1$

	/**
	 * All connected relatinoships
	 */
	public static final ExpansionType ALL = new ExpansionType("All"); //$NON-NLS-1$

	/**
	 * The list of values for this enumerated type.
	 */
	public static final ExpansionType[] VALUES = {NONE, INCOMING, OUTGOING,
		BOTH, ALL};

	/**
	 * Constructs a new model type with the specified name and ordinal.
	 * 
	 * @param name
	 *            The name of the new model type.
	 * @param ordinal
	 *            The ordinal for the new model type.
	 */
	protected ExpansionType(String name, int ordinal) {
		super(name, ordinal);
	}

	/**
	 * Constructs a new model type with the specified name.
	 * 
	 * @param name
	 *            The name of the new model type.
	 */
	private ExpansionType(String name) {
		this(name, nextOrdinal++);
	}

	/**
	 * Retrieves the list of constants for this enumerated type.
	 * 
	 * @return List of constants for this enumerated type.
	 * 
	 * @see EnumeratedType#getValues()
	 */
	protected List getValues() {
		return Collections.unmodifiableList(Arrays.asList(VALUES));
	}

}