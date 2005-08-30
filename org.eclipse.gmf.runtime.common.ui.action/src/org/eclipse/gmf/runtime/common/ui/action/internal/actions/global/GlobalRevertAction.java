/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action.internal.actions.global;

import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.ResourceManager;

/**
 * Global action for the retargetable "REVERT" action.
 * 
 * @author ldamus
 */
public class GlobalRevertAction
	extends GlobalAction {

	/**
	 * Label definition of the REVERT action.
	 */
	private static final String REVERT_TEXT = ResourceManager
		.getI18NString("GlobalRevertAction.label"); //$NON-NLS-1$

	/**
	 * Action definition id of the REVERT action.
	 */
	private static final String REVERT = "org.eclipse.gmf.runtime.common.ui.actions.global.revert"; //$NON-NLS-1$

	/**
	 * Constructs a new action instance.
	 * 
	 * @param workbenchPage
	 *            the workbench page
	 */
	public GlobalRevertAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * Constructs a new action instance.
	 * 
	 * @param workbenchPart
	 *            the workbench part
	 */
	public GlobalRevertAction(IWorkbenchPart workbenchPart) {
		super(workbenchPart);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.action.global.GlobalAction#getActionId()
	 */
	public String getActionId() {
		return GlobalActionId.REVERT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.action.global.GlobalAction#init()
	 */
	public void init() {

		// Set the ID
		setId(getWorkbenchActionConstant() != null ? getWorkbenchActionConstant()
			: REVERT);

		// Set the label
		setText(REVERT_TEXT);

		super.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.action.AbstractActionHandler#isPropertyListener()
	 */
	protected boolean isPropertyListener() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPropertyListener#propertyChanged(java.lang.Object,
	 *      int)
	 */
	public void propertyChanged(Object source, int propId) {
		if (propId == ISaveablePart.PROP_DIRTY) {
			refresh();
		}
	}

}