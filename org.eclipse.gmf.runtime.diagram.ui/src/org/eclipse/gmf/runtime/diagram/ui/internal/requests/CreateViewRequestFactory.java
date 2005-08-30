/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.requests;

import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectorViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectorViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectorViewRequest.ConnectorViewDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest.ViewAndElementDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.util.INotationType;
import org.eclipse.gmf.runtime.emf.core.internal.util.IHintedType;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import com.ibm.xtools.notation.Node;

/**
 * Knows how to create a view and element (if applicable) request given an
 * IElementType.
 * 
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class CreateViewRequestFactory {

	/**
	 * Creates a new <code>CreateViewRequest</code> or
	 * <code>CreateViewAndElementRequest</code> based on the
	 * <code>IElementType</code> passed in.
	 * 
	 * @param type
	 *            the <code>IElementType</code>
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return the new request
	 */
	public static CreateViewRequest getCreateShapeRequest(IElementType type, PreferencesHint preferencesHint) {
		if (type instanceof INotationType) {
			ViewDescriptor viewDescriptor = new ViewDescriptor(null,
				Node.class, ((INotationType) type).getSemanticHint(), preferencesHint);
			return new CreateViewRequest(viewDescriptor);
		} else if (type instanceof IHintedType) {
			ViewAndElementDescriptor viewDescriptor = new ViewAndElementDescriptor(
				new CreateElementRequestAdapter(new CreateElementRequest(type)),
				Node.class, ((IHintedType) type).getSemanticHint(), preferencesHint);
			return new CreateViewAndElementRequest(viewDescriptor);
		} else {
			return new CreateViewAndElementRequest(type, preferencesHint);
		}
	}

	/**
	 * Creates a new <code>CreateConnectorViewRequest</code> or
	 * <code>CreateConnectorViewAndElementRequest</code> based on the
	 * <code>IElementType</code> passed in.
	 * 
	 * @param type
	 *            the <code>IElementType</code>
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return the new request
	 */
	public static CreateConnectorViewRequest getCreateConnectorRequest(
			IElementType type, PreferencesHint preferencesHint) {
		if (type instanceof INotationType) {
			// Pass in the type as the element adapter so that it can be
			// retrieved in the cases where a popup menu is to appear with a
			// list of types.
			ConnectorViewDescriptor viewDescriptor = new ConnectorViewDescriptor(
				type, ((INotationType) type).getSemanticHint(), preferencesHint);
			return new CreateConnectorViewRequest(viewDescriptor);
		} else if (type instanceof IHintedType) {
			return new CreateConnectorViewAndElementRequest(type,
				((IHintedType) type).getSemanticHint(), preferencesHint);
		} else {
			return new CreateConnectorViewAndElementRequest(type, preferencesHint);
		}
	}
}