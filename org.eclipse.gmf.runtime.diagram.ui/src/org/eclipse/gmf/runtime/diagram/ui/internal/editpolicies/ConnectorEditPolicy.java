/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.diagram.ui.requests.GroupRequestViaKeyboard;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import com.ibm.xtools.notation.View;

/**
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 * 
 * An editpolicy that is responsible for Connection related model events. It
 * currently handles only the DELETE request
 */
public class ConnectorEditPolicy
	extends org.eclipse.gef.editpolicies.ConnectionEditPolicy {
	
	private static final String DELETE_FROM_DIAGRAM_DLG_TITLE = PresentationResourceManager
	.getI18NString("PromptingDeleteAction.DeleteFromDiagramDialog.Title"); //$NON-NLS-1$ 
	private static final String DELETE_FROM_DIAGRAM_DLG_MESSAGE = PresentationResourceManager
	.getI18NString("PromptingDeleteAction.DeleteFromDiagramDialog.Message"); //$NON-NLS-1$ 	
	private static final String DELETE_FROM_MODEL_DLG_TOGGLE_LABEL =PresentationResourceManager
	.getI18NString("MessageDialogWithToggle.DoNotPromptAgainToggle.label"); //$NON-NLS-1$

	/**
	 * Returns a delete command to honour the supplied request. Calls
	 * {@link #createDeleteSemanticCommand(GroupRequest)}if
	 * {@link #shouldDeleteSemantic()}returns <tt>true</tt>; othwerise
	 * {@link #createDeleteViewCommand(GroupRequest)}is called.
	 */
	protected final Command getDeleteCommand(GroupRequest deleteRequest) {
		boolean isDeleteFromKeyBoard = deleteRequest instanceof GroupRequestViaKeyboard;
		
			if (shouldDeleteSemantic()){
				return createDeleteSemanticCommand(deleteRequest);
			}else{
					
				boolean proceedToDeleteView = true;
				if (isDeleteFromKeyBoard){
					GroupRequestViaKeyboard groupRequestViaKeyboard = (GroupRequestViaKeyboard)deleteRequest;					
					if (groupRequestViaKeyboard.isShowInformationDialog()){
						proceedToDeleteView = showPrompt();
						groupRequestViaKeyboard.setShowInformationDialog(false);
						if (!(proceedToDeleteView))
							return UnexecutableCommand.INSTANCE;
					}					
				}
				
				return createDeleteViewCommand(deleteRequest);
			}
					
	}

	
	/**
	 * Return <tt>true</tt> if either the connectors source or target
	 * editparts are canonical; otherwise <tt>false</tt>.
	 */
	protected boolean shouldDeleteSemantic() {

		Assert.isTrue(getHost() instanceof AbstractConnectionEditPart);
		
		
		AbstractConnectionEditPart cep = (AbstractConnectionEditPart) getHost();

		if (cep instanceof IGraphicalEditPart) {
			if (ViewUtil
				.resolveSemanticElement((View) ((IGraphicalEditPart) cep)
					.getModel()) == null)
				return false;
		}
		boolean isCanonical = false;
		if (cep.getSource() != null)
			isCanonical = IsCanonical(cep.getSource());
		if (cep.getTarget() != null)
			return isCanonical ? isCanonical : IsCanonical(cep.getTarget());
		return isCanonical;
		
	}

	/**
	 * Return <tt>true</tt> if the supplied editpart is canonical; otherwise,
	 * <tt>false</tt>.
	 */
	private boolean IsCanonical(EditPart ep) {
		EditPart parent = ep.getParent();
		return parent instanceof GraphicalEditPart
			? ((GraphicalEditPart) parent).isCanonical()
			: false;
	}	

	/**
	 * Return a command to delete the host's view.
	 * 
	 * @param deleteRequest
	 *            the original delete request.
	 */
	protected Command createDeleteViewCommand(GroupRequest deleteRequest) {
		return new EtoolsProxyCommand(
			new DeleteCommand((View) getHost().getModel()));
	}
	/**
	 * Return a command to delete the host's semantic element. This method is
	 * called if the host is canonical.
	 * 
	 * @see #shouldDeleteSemantic()
	 * @param deleteRequest
	 *            the original delete request.
	 */
	protected Command createDeleteSemanticCommand(GroupRequest deleteRequest) {
		EditCommandRequestWrapper semReq =
			new EditCommandRequestWrapper(new DestroyElementRequest(false));
		Command semanticCmd = getHost().getCommand(semReq);
		if (semanticCmd != null && semanticCmd.canExecute()) {
			CompoundCommand cc = new CompoundCommand();
			cc.add(semanticCmd);
			return cc;
		}
		return null;
	}
	/**
	 * Performs the delete action on the selected objects.
	 */
	private boolean showPrompt() {
		boolean prompt = ((IPreferenceStore)
			((IGraphicalEditPart) getHost()).getDiagramPreferencesHint().getPreferenceStore())
			.getBoolean(IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_DIAGRAM);
		
		if(prompt) 
			if(showMessageDialog()) 
				return true; 
			else
				return false;
											
		return true;		
		
	}	
	
	
	
	/**
	 * launches the prompting dialogBox on deletion of elements from the diagram for the end user.
	 * 
	 * @return boolean  true if user pressed YES; false otherwise
	 */
	private boolean showMessageDialog() {	
		MessageDialogWithToggle dialog = MessageDialogWithToggle
			.openYesNoQuestion(Display.getCurrent().getActiveShell(),
				DELETE_FROM_DIAGRAM_DLG_TITLE, DELETE_FROM_DIAGRAM_DLG_MESSAGE,
				DELETE_FROM_MODEL_DLG_TOGGLE_LABEL, false,
				(IPreferenceStore) ((IGraphicalEditPart) getHost())
					.getDiagramPreferencesHint().getPreferenceStore(),
				IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_DIAGRAM);
		
		if (dialog.getReturnCode() == IDialogConstants.YES_ID) 
			return true;
		else 
			return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#getCommand(org.eclipse.gef.Request)
	 */
	public Command getCommand(Request request) {
		if (request instanceof GroupRequestViaKeyboard){
			return getDeleteCommand((GroupRequest)request);
		}
		return super.getCommand(request);
	}

}
