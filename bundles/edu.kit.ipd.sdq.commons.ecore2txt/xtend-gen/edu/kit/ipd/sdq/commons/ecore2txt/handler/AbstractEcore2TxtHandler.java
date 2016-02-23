/**
 * Copyright (c) 2015 Software Design and Quality (SDQ) research group
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    M. Kramer - initial API and implementation
 */
package edu.kit.ipd.sdq.commons.ecore2txt.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

@SuppressWarnings("all")
public abstract class AbstractEcore2TxtHandler<T extends IResource> extends AbstractHandler implements IHandler {
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    final ISelection selection = HandlerUtil.getCurrentSelection(event);
    final Iterable<T> filteredSelection = this.filterSelection(selection);
    String _plugInID = this.getPlugInID();
    this.executeEcore2TxtGenerator(filteredSelection, event, _plugInID);
    return null;
  }
  
  public boolean isEnabled() {
    return true;
  }
  
  public abstract Iterable<T> filterSelection(final ISelection selection);
  
  public abstract String getPlugInID();
  
  public abstract void executeEcore2TxtGenerator(final Iterable<T> filteredSelection, final ExecutionEvent event, final String plugInID) throws ExecutionException;
}
