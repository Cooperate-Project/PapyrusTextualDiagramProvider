package de.cooperateproject.notation2plant.viewer;

import java.io.IOException;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.infra.gmfdiag.common.editpart.PapyrusDiagramEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import de.cooperateproject.generator.PlantGenerator;

public class Uml2PlantUmlView extends ViewPart {

	private TextViewer textviewer;
	private boolean toggle = true;
	private Action toogleAction = new Action("Live Translation", IAction.AS_CHECK_BOX) {
		public void run() {
			toggle = !this.isChecked();
		}
	};
		
	private ISelectionListener listener = new ISelectionListener() {
		@Override
		public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
			if (sourcepart != Uml2PlantUmlView.this && toggle) {
			    try {
					showSelection(sourcepart, selection);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	};



	
	/**
	 * Shows the given selection in this view.
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
		
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
        mgr.add(toogleAction);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);
	}

	
	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(listener);
		super.dispose();
	}


	@Override
	public void setFocus() {
		
	}
}
