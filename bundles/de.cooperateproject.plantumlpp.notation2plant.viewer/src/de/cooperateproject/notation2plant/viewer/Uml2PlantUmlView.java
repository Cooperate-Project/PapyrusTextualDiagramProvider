 package de.cooperateproject.notation2plant.viewer;

import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.infra.gmfdiag.common.editpart.PapyrusDiagramEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;

import de.cooperateproject.generator.PlantGenerator; 

public class Uml2PlantUmlView extends ViewPart {

	private TextViewer textviewer;
	private static final String PLUGIN_ID = "de.cooperateproject.notation2plant.viewer";
	private static final Logger LOG =  Logger.getLogger("Uml2PlantView");
	private boolean toggle = true;
	
	private Action toggleAction = new Action("", IAction.AS_CHECK_BOX) {
		public void run() {
			if (this.isChecked()) {
				removeSelectionListener();
			} else {
				addSelectionListener();
				try {
					showActualSelection();
				} catch (IOException e) {
					LOG.error("couldn't get actual Selection", e);
				}
			}
		}
	};

	private ISelectionListener listener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
			if (sourcepart != Uml2PlantUmlView.this && toggle) {
				try {
					showSelection(sourcepart, selection);
				} catch (IOException e) {
					LOG.error("couldn't show Selection", e);
				}
			}
		}
	};
	
	private void showActualSelection() throws IOException {
		IEditorPart actualEditor = getSite().getWorkbenchWindow().getActivePage().getActiveEditor();
		if (actualEditor != null) {
			ISelection actualSelection = actualEditor.getEditorSite().getSelectionProvider().getSelection();
			showSelection(actualEditor.getSite().getPart(), actualSelection);
		} else {
			showText("No Model Available");
		}
	}

	private ImageDescriptor getImageDescriptor(String relativePath) {
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		URL url = FileLocator.find(bundle, new Path("icons/" + relativePath), null);
		return ImageDescriptor.createFromURL(url);
	}

	/**
	 * Shows the given selection in this view.
	 * 
	 * @throws IOException
	 */
	public void showSelection(IWorkbenchPart sourcepart, ISelection selection) throws IOException {
		setContentDescription(sourcepart.getTitle() + " (" + selection.getClass().getName() + ")");

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			showItems(ss.getFirstElement());
		}
	}

	private void showItems(Object item) {
		if (item instanceof PapyrusDiagramEditPart) {
			Object model = ((PapyrusDiagramEditPart) item).getModel();
			if (model instanceof Diagram) {
				showPlantUml((Diagram) model);
			}
		} else if (item instanceof EditPart) {
			Object obj = ((EditPart) item).getModel();
			if (obj instanceof Shape) {
				showPlantUml(((Shape) obj).getDiagram());
			} else if (obj instanceof Connector) {
				showPlantUml(((Connector) obj).getDiagram());
			}
		}
	}

	private void showPlantUml(Diagram diagram) {
		PlantGenerator p = new PlantGenerator();
		showText(p.compile(diagram).toString());
	}

	private void showText(String text) {
		textviewer.setDocument(new Document(text));
	}

	@Override
	public void createPartControl(Composite parent) {
		textviewer = new TextViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		textviewer.setEditable(false);

		//toggleAction.setImageDescriptor(getImageDescriptor("cross.png"));
		toggleAction.setToolTipText("Disable live translation");

		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(toggleAction);
		addSelectionListener();
		try {
			showActualSelection();
		} catch (IOException e) {
			LOG.error("couldn't get actual Selection", e);
		}
				
	}

	@Override
	public void dispose() {
		removeSelectionListener();
		super.dispose();
	}
	
	private void addSelectionListener() {
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);
	}
	
	private void removeSelectionListener() {
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(listener);
	}

	@Override
	public void setFocus() {

	}
}
