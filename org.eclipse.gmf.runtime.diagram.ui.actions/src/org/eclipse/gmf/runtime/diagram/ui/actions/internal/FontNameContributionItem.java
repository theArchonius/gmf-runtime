/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.FontHelper;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;

/**
 * Drop down combo box conbtribution item that changes font's name
 * 
 * @author Natalia Balaba
 * @canBeSeenBy %level1
 */
public class FontNameContributionItem
	extends PropertyChangeContributionItem
	implements Listener {

	/**
	 * The current font name
	 */
	private String lastFontName;
	
	/**
	 * Constructor for FontNameContributionItem.
	 * @see org.eclipse.gmf.runtime.diagram.ui.ui.actions.ActionContribution#ActionContribution(IEditorPart, String)
	 */
	public FontNameContributionItem(IWorkbenchPage workbenchPage) {
		super(workbenchPage, ActionIds.CUSTOM_FONT_NAME, Properties.ID_FONTNAME, Messages.getString("PropertyDescriptorFactory.FontName")); //$NON-NLS-1$
		setLabel(Messages.getString("FontNameContributionItem.tooltip"));//$NON-NLS-1$
	}

	/**
	 * Create and return a drop down combo box and populate it with font names
	 * 
	 * @see org.eclipse.jface.action.ControlContribution#createControl(Composite)
	 */
	protected Control createControl(Composite parent) {
		String[] strings = FontHelper.getFontNames();
		
		Combo box = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		box.setItems(strings);
		box.addListener(SWT.Selection, this);
		box.select(0);
		return box;
	}

	/**
	 * Assert a new proprty value to be set to the selected IView object(s) [whenever there is
	 * a mutiple selection this method will be called per valid selected item]
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.internal.PropertyChangeContributionItem#getNewPropertyValue()
	 */
	protected Object getNewPropertyValue() {
		return lastFontName;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#refreshItem()
	 */
	protected void refreshItem() {
		Combo box = (Combo) getControl();
		String fontName = (String) getOperationSetPropertyValue(getPropertyId());

		if (fontName != null) {
			int index = box.indexOf(fontName);
			box.select(index);
		} else {
			box.clearSelection();
		}
		super.refreshItem();
	}

	/**
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		Combo box = (Combo) getControl();
		lastFontName = box.getItem(box.getSelectionIndex());
		runWithEvent(event);
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#isCommandStackListener()
	 */
	protected boolean isCommandStackListener() {
		return true;
	}
}
