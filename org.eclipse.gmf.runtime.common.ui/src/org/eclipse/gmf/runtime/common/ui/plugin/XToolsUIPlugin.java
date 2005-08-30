/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.plugin;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.ISaveContext;
import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIStatusCodes;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.ResourceManager;

/**
 * The abstract parent of all XTools UI plug-ins.
 * 
 * @author nbalaba
 *  
 */
public abstract class XToolsUIPlugin
	extends AbstractUIPlugin
	implements ISaveParticipant {

	private static IWorkbench alternateWorkbench = null;

	/**
	 * Attribute for a holding a list of <code>ISaveParticipant</code>
	 */
	private final List listOfSaveParticipants = new ArrayList();

	/**
	 * Error message used when startup of a plug-in fails.
	 */
	protected static final String STARTUP_ERROR_MESSAGE = ResourceManager
		.getI18NString("XToolsUIPlugin._ERROR_.startupErrorMessage"); //$NON-NLS-1$

	/**
	 * Error message used when shutdown of a plug-in fails.
	 */
	protected static final String SHUTDOWN_ERROR_MESSAGE = ResourceManager
		.getI18NString("XToolsUIPlugin._ERROR_.shutDownErrorMessage"); //$NON-NLS-1$

	/**
	 * Creates a new plug-in runtime object.
	 */
	protected XToolsUIPlugin() {
		super();
	}

	/**
	 * Creates a new plug-in runtime object for the given plug-in descriptor.
	 * 
	 * @param descriptor
	 *            The plug-in descriptor.
	 * @deprecated replace with constructor with no parameters since
	 *             IPluginDescriptor is deprecated.
	 */
	protected XToolsUIPlugin(
			org.eclipse.core.runtime.IPluginDescriptor descriptor) {
		super(descriptor);
	}

	/**
	 * Retrieves a resource manager instance for this plug-in.
	 * 
	 * @return A resource manager instance for this plug-in.
	 */
	public abstract AbstractResourceManager getResourceManager();

	/**
	 * Starts up this plug-in.
	 */
	protected void doStartup() {
		/* empty method body */
	}

	/**
	 * Shuts down this plug-in and discards all plug-in state.
	 */
	protected void doShutdown() {
		/* empty method body */
	}

	/**
	 * Sets an alternative workbench to the default PlatformUI.getWorkbench().
	 * This should only be used in <b>extreme </b> situations where UI plugins
	 * are needed in a headless eclipse application. Use with caution.
	 * 
	 * @param wb
	 *            An alternate workbench to use.
	 */
	public static void setAlternateWorkbench(IWorkbench wb) {
		alternateWorkbench = wb;
	}

	/**
	 * An overridden getWorkbench method that returns back the standard
	 * workbench if and only if an alternative workbench is not available.
	 */
	public IWorkbench getWorkbench() {
		return alternateWorkbench == null ? super.getWorkbench()
			: alternateWorkbench;
	}

	/**
	 * Retrieves an error message that indicates an error during this plug-in's
	 * startup sequence.
	 * 
	 * @return An error message string.
	 * @param pluginName
	 *            The name of this plug-in.
	 */
	protected String getStartupErrorMessage(String pluginName) {
		return MessageFormat.format(STARTUP_ERROR_MESSAGE,
			new Object[] {pluginName});
	}

	/**
	 * Retrieves an error message that indicates an error during this plug-in's
	 * shutdown sequence.
	 * 
	 * @return An error message string.
	 * @param pluginName
	 *            The name of this plug-in.
	 */
	protected String getShutdownErrorMessage(String pluginName) {
		return MessageFormat.format(SHUTDOWN_ERROR_MESSAGE,
			new Object[] {pluginName});
	}

	/**
	 * Get symbolic name for this plugin.
	 * 
	 * @return symbolic name as <code>String</code>
	 */
	public final String getSymbolicName() {
		String name = null;

		try {
			name = getBundle().getSymbolicName();
		} catch (Exception e) {
			// ignore the exception
		}

		return String.valueOf(name);
	}

	/**
	 * Get plugin name.
	 * 
	 * @return plugin name as <code>String</code>
	 */
	public final String getPluginName() {
		Object name;

		try {
			name = getBundle().getHeaders().get(Constants.BUNDLE_NAME);
		} catch (Exception e) {
			name = getSymbolicName();
		}

		return String.valueOf(name);
	}

	/**
	 * Starts up this plug-in.
	 * 
	 * @exception CoreException
	 *                If this plug-in did not start up properly.
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public final void start(BundleContext context)
		throws Exception {
		super.start(context);

		try {
			Log.info(this, CommonUIStatusCodes.OK, getPluginName()
				+ " plug-in starting up..."); //$NON-NLS-1$

			/* Register as a save participant */
			ResourcesPlugin.getWorkspace().addSaveParticipant(this, this);

			long startTime = System.currentTimeMillis();
			doStartup();
			long duration = System.currentTimeMillis() - startTime;

			Trace
				.trace(
					this,
					"Plug-in '" + getSymbolicName() + "' started up: " + duration + " msec"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} catch (Exception e) {
			Trace.throwing(CommonUIPlugin.getDefault(),
				CommonUIDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"startup", e); //$NON-NLS-1$
			Log.error(CommonUIPlugin.getDefault(),
				CommonUIStatusCodes.PLUGIN_STARTUP_FAILURE, "startup", e); //$NON-NLS-1$
			throw new CoreException(new Status(IStatus.ERROR,
				getSymbolicName(), CommonUIStatusCodes.PLUGIN_STARTUP_FAILURE,
				getStartupErrorMessage(getPluginName()), e));
		}
	}

	/**
	 * Shuts down this plug-in and discards all plug-in state.
	 * 
	 * @exception CoreException
	 *                If this method fails to shut down this plug-in.
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public final void stop(BundleContext context)
		throws Exception {
		try {
			Log.info(this, CommonUIStatusCodes.OK, getPluginName()
				+ " plug-in shutting down..."); //$NON-NLS-1$

			doShutdown();

			/* Unregister as a save participant */
			if (ResourcesPlugin.getWorkspace() != null) {
				ResourcesPlugin.getWorkspace().removeSaveParticipant(this);
			}

			Trace.trace(this, "Plug-in '" + getSymbolicName() + "' shut down."); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Exception e) {
			Trace.throwing(CommonUIPlugin.getDefault(),
				CommonUIDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"shutdown", e); //$NON-NLS-1$
			Log.error(CommonUIPlugin.getDefault(),
				CommonUIStatusCodes.PLUGIN_SHUTDOWN_FAILURE, "shutdown", e); //$NON-NLS-1$
			throw new CoreException(new Status(IStatus.ERROR,
				getSymbolicName(), CommonUIStatusCodes.PLUGIN_SHUTDOWN_FAILURE,
				getShutdownErrorMessage(getPluginName()), e));
		} finally {
			super.stop(context);
		}
	}

	/**
	 * Registers the given save participant. Once registered, the workspace save
	 * participant will actively participate in the saving of this workspace. If
	 * the participant is already registered with this plugin, this call has no
	 * effect.
	 * 
	 * @param participant
	 *            the participant
	 */
	public void addSaveParticipant(ISaveParticipant participant) {
		if (participant != null
			&& !getListOfSaveParticipants().contains(participant)) {
			getListOfSaveParticipants().add(participant);
		}
	}

	/**
	 * Removes the given save participant. If no such participant is registered,
	 * no action is taken. Once removed, the workspace save participant no
	 * longer actively participates in any future saves of this workspace.
	 * 
	 * @param participant
	 *            the participant
	 */
	public void removeSaveParticipant(ISaveParticipant participant) {
		if (participant != null) {
			getListOfSaveParticipants().remove(participant);
		}
	}

	/**
	 * @see org.eclipse.core.resources.ISaveParticipant#doneSaving(ISaveContext)
	 */
	public void doneSaving(ISaveContext context) {
		for (Iterator i = getListOfSaveParticipants().iterator(); i.hasNext();) {
			/* Get the next participant */
			ISaveParticipant participant = (ISaveParticipant) i.next();

			/* Fire the event */
			participant.doneSaving(context);
		}
	}

	/**
	 * @see org.eclipse.core.resources.ISaveParticipant#prepareToSave(ISaveContext)
	 */
	public void prepareToSave(ISaveContext context)
		throws CoreException {
		for (Iterator i = getListOfSaveParticipants().iterator(); i.hasNext();) {
			/* Get the next participant */
			ISaveParticipant participant = (ISaveParticipant) i.next();

			/* Fire the event */
			participant.prepareToSave(context);
		}
	}

	/**
	 * @see org.eclipse.core.resources.ISaveParticipant#rollback(ISaveContext)
	 */
	public void rollback(ISaveContext context) {
		for (Iterator i = getListOfSaveParticipants().iterator(); i.hasNext();) {
			/* Get the next participant */
			ISaveParticipant participant = (ISaveParticipant) i.next();

			/* Fire the event */
			participant.rollback(context);
		}
	}

	/**
	 * @see org.eclipse.core.resources.ISaveParticipant#saving(ISaveContext)
	 */
	public void saving(ISaveContext context)
		throws CoreException {
		for (Iterator i = getListOfSaveParticipants().iterator(); i.hasNext();) {
			/* Get the next participant */
			ISaveParticipant participant = (ISaveParticipant) i.next();

			/* Fire the event */
			participant.saving(context);
		}
	}

	/**
	 * Returns the listOfSaveParticipants.
	 * 
	 * @return List
	 */
	public List getListOfSaveParticipants() {
		return listOfSaveParticipants;
	}
}