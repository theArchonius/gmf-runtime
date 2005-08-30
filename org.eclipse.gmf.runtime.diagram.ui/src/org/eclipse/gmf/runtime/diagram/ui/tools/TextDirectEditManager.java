/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.tools;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import org.eclipse.gmf.runtime.common.ui.contentassist.ContentAssistantHelper;
import org.eclipse.gmf.runtime.diagram.ui.editparts.TextCompartmentEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.draw2d.ui.internal.l10n.Draw2dResourceManager;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.gef.ui.parts.TextCellEditorEx;

/**
 * @author melaasar
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public class TextDirectEditManager
	extends DirectEditManager {
	
	/**
	 * content assist background color
	 */
	private Color proposalPopupBackgroundColor = null;

	/**
	 * content assist foreground color
	 */
	private Color proposalPopupForegroundColor = null;
	
	private boolean committed = false;
	
	/**
	 * flag used to avoid unhooking listeners twice if the UI thread is blocked
	 */
	private boolean listenersAttached = true;
	/**
	 * constructor
	 * @param source 
	 * @param editorType 
	 * @param locator
	 */
	public TextDirectEditManager(GraphicalEditPart source, Class editorType,
			CellEditorLocator locator) {

		super(source, editorType, locator);
	}

	/**
	 * Given a <code>WrapLabel</code> object, this will calculate the 
	 * correct Font needed to display into screen coordinates, taking into 
	 * account the current mapmode.  This will typically be used by direct
	 * edit cell editors that need to display independent of the zoom or any
	 * coordinate mapping that is taking place on the drawing surface.
	 * 
	 * @param label the <code>WrapLabel</code> to use for the font calculation
	 * @return the <code>Font</code> that is scaled to the screen coordinates.  
	 * Note: the returned <code>Font</code> should not be disposed since it is
	 * cached by a common resource manager.
	 */
	protected Font getScaledFont(WrapLabel label) {
		Font scaledFont = label.getFont();
		FontData data = scaledFont.getFontData()[0];
		Dimension fontSize = new Dimension(0, MapMode.DPtoLP(data.getHeight()));
		label.translateToAbsolute(fontSize);
		
		if( Math.abs( data.getHeight() - fontSize.height ) < 2 )
			fontSize.height = data.getHeight();

		data.setHeight(fontSize.height);
		Font newFont = Draw2dResourceManager.getInstance().getFont(null, data);
		return newFont;
	}
	
	protected void initCellEditor() {
		committed = false;

		// Get the Text Compartments Edit Part
		TextCompartmentEditPart textEP = (TextCompartmentEditPart) getEditPart();

		setEditText(textEP.getEditText());

		WrapLabel label = textEP.getLabel();
		Assert.isNotNull(label);
		Text text = (Text) getCellEditor().getControl();
		// scale the font accordingly to the zoom level
		text.setFont(getScaledFont(label));
		
		
		// register a validator on the cell editor
		getCellEditor().setValidator(textEP.getEditTextValidator());

		if (textEP.getParser() != null) {
			IContentAssistProcessor processor = textEP.getCompletionProcessor();
			if (processor != null) {
				// register content assist
				proposalPopupBackgroundColor = new Color(getCellEditor()
					.getControl().getShell().getDisplay(), new RGB(254, 241,
					233));
				proposalPopupForegroundColor = new Color(getCellEditor()
					.getControl().getShell().getDisplay(), new RGB(0, 0, 0));

				ContentAssistantHelper.createTextContentAssistant(text,
					proposalPopupForegroundColor, proposalPopupBackgroundColor,
					processor);
			}
		}
	}

	/**
	 * @see org.eclipse.gef.tools.DirectEditManager#commit()
	 */
	protected void commit() {
		Shell activeShell = Display.getCurrent().getActiveShell();
		if (activeShell != null
			&& getCellEditor().getControl().getShell().equals(
				activeShell.getParent())) {
			Control[] children = activeShell.getChildren();
			if (children.length == 1 && children[0] instanceof Table) {
				/*
				 * CONTENT ASSIST: focus is lost to the content assist pop up -
				 * stay in focus
				 */
				getCellEditor().getControl().setVisible(true);
				return;
			}
		}

		// content assist hacks
		if (committed) {
			bringDown();
			return;
		}
		committed = true;
		super.commit();
	}

	/**
	 * @see org.eclipse.gef.tools.DirectEditManager#bringDown()
	 */
	protected void bringDown() {
		if (proposalPopupForegroundColor != null) {
			proposalPopupForegroundColor.dispose();
			proposalPopupForegroundColor = null;
		}
		if (proposalPopupBackgroundColor != null) {
			proposalPopupBackgroundColor.dispose();
			proposalPopupBackgroundColor = null;
		}

		// myee - RATLC00523014: crashes when queued in asyncExec()
		eraseFeedback();
		
		Display.getCurrent().asyncExec(new Runnable() {

			public void run() {
				// Content Assist hack - allow proper cleanup on childen
				// controls
				TextDirectEditManager.super.bringDown();
			}
		});
	}

	/**
	 * This method is used to set the cell editors text
	 * 
	 * @param toEdit
	 *            String to be set in the cell editor
	 */
	public void setEditText(String toEdit) {

		// Get the cell editor
		CellEditor cellEditor = getCellEditor();

		// IF the cell editor doesn't exist yet...
		if (cellEditor == null) {
			// Do nothing
			return;
		}

		// Get the Text Compartment Edit Part
		TextCompartmentEditPart textEP = (TextCompartmentEditPart) getEditPart();

		// Get the Text control
		Text textControl = (Text) cellEditor.getControl();

		// Get the Text Edit Part's Figure (WrapLabel)
		WrapLabel label = textEP.getLabel();
		Assert.isNotNull(label);
		// Set the Figures text
		label.setText(toEdit);
		
		
		// See RATLC00522324
		if (cellEditor instanceof TextCellEditorEx){
			((TextCellEditorEx)cellEditor).setValueAndProcessEditOccured(toEdit);
		} else {
			cellEditor.setValue(toEdit);
		}
		
		// Set the controls text and position the caret at the end of the text
		textControl.selectAll();
	}

	/**
	 * Performs show and sets the edit string to be the initial character
	 * @param initialChar
	 */
	public void show(char initialChar) {
		
		show();
		// Set the cell editor text to the initial character
		String initialString = String.valueOf(initialChar);
		setEditText(initialString);

	}

	/**
	 * 
	 * Performs show and sends an extra mouse click to the point location so 
	 * that cursor appears at the mouse click point
	 * 
	 * The Text control does not allow for the cursor to appear at point location but
	 * at a character location
	 * 
	 * @param location
	 */
	public void show(Point location) {		
		show();
		sendMouseClick(location);	
	}

	
	/**
	 * 
	 * Sends a SWT MouseUp and MouseDown event to the point location 
	 * to the current Display
	 * 
	 * @param location
	 */
	private void sendMouseClick(final Point location) {		
		
		final Display currDisplay = Display.getCurrent();
		
		new Thread() {
			Event event;
			public void run() {
					event = new Event();
					event.type = SWT.MouseDown;
					event.button = 1;
					event.x = location.x;
					event.y = location.y;
					currDisplay.post(event);
					event.type = SWT.MouseUp;
					currDisplay.post(event);
			}
		}.start();
	}

	/* 
	 * Overrides super unhookListeners to set listeners attached flag
	 * This method prevents unhooking listeners twice if the UI thread is blocked.
	 * For example, a validation dialog may block the thread
	 */
	protected void unhookListeners() {
		if (listenersAttached) {
			listenersAttached = false;
			super.unhookListeners();
		}
	}

	/* 
	 * Sets the listeners attached flag if the cell editor exists
	 */
	protected void setCellEditor(CellEditor editor) {
		super.setCellEditor(editor);
		if (editor != null) {
			listenersAttached = true;
		}
	}

}