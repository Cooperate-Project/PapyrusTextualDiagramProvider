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

import com.google.common.collect.Iterables;
import edu.kit.ipd.sdq.commons.ecore2txt.handler.AbstractEcore2TxtHandler;
import java.util.Collections;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public abstract class AbstractEcoreIFile2TxtHandler extends AbstractEcore2TxtHandler<IFile> {
  public Iterable<IFile> filterSelection(final ISelection selection) {
    if ((selection instanceof IStructuredSelection)) {
      final IStructuredSelection structuredSelection = ((IStructuredSelection) selection);
      List _list = structuredSelection.toList();
      final Iterable<IFile> files = Iterables.<IFile>filter(_list, IFile.class);
      int _size = IterableExtensions.size(files);
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        return files;
      }
    }
    return Collections.<IFile>emptyList();
  }
}
