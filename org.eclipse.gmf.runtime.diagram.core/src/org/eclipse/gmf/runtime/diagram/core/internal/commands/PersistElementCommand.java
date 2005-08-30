/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.core.internal.commands;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramPlugin;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.Messages;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import com.ibm.xtools.notation.Diagram;
import com.ibm.xtools.notation.Edge;
import com.ibm.xtools.notation.View;


/**
 * @author mmostafa
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.core.*
 * Command that will persist transient views.
 */
public class PersistElementCommand extends AbstractModelCommand { 
	private View _view;

	public PersistElementCommand(View view) {
		super(Messages.getString("AddCommand.Label"), null);//$NON-NLS-1$
		_view = view;
	}
	
	/**
	 * Walks up the supplied element's container tree until a container is
	 * found in the detached element map and then moves all of the detached
	 * element's children over to the attached elements.
	 *
	 * @return the detached root element.
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		try {
			assert null != _view: "Null view in PersistElementCommand::doExecute";//$NON-NLS-1$
			EObject container = _view.eContainer();
			if (_view instanceof Edge){
				Diagram diagram = (Diagram)container;
				diagram.persistEdges();
			}
			else if (container instanceof View)
				((View)container).persistChildren();
			
			return newOKCommandResult(_view);
		}
		catch (Exception e) {
			Log.error(DiagramPlugin.getInstance(), IStatus.ERROR,
				e.getMessage(), e);
			return newErrorCommandResult(e.getMessage());
		}
	}
}
