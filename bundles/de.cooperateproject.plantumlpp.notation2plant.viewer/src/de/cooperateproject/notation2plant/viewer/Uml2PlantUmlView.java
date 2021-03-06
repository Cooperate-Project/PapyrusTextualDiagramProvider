package de.cooperateproject.notation2plant.viewer;

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
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;

import de.cooperateproject.generator.PlantGenerator;

public class Uml2PlantUmlView extends ViewPart {

    private static final String PLUGIN_ID = "de.cooperateproject.plantumlpp.notation2plant.viewer";
    private static final String TERMINATE = "terminate.png";
    private static final String START = "start.png";
    private static final String PLANT = "plant.png";
    private static final String ENABLE_TOOLTIP = "Enable Live Transformation";
    private static final String DISABLE_TOOLTIP = "Disable Live Transformation";
    private static final String PLANT_TOOLTIP = "Show UML Diagram";
    private static final String VISUALIZATION_ENABLE_TOOLTIP = "Enable Graphical Visualizations";
    private static final String VISUALIZATION_DISABLE_TOOLTIP = "Disable Graphical Visualizations";
    private static final Logger LOGGER = Logger.getLogger(Uml2PlantUmlView.class);

    private final Uml2PlantUmlViewGraphicalVisualization visualization = new Uml2PlantUmlViewGraphicalVisualization();
    private boolean toggleLive = true;
    private TextViewer textviewer;

    private class ShowVisualizationAction extends Action {
        public ShowVisualizationAction() {
            super("", IAction.AS_CHECK_BOX);
            setChecked(false);
            PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ETOOL_DEF_PERSPECTIVE);
        }
        
        @Override
        public void run() {
            if (this.isChecked()) {
                setToolTipText(VISUALIZATION_DISABLE_TOOLTIP);
            } else {
                setToolTipText(VISUALIZATION_ENABLE_TOOLTIP);
            }
            try {
                visualization.changeEnabledStatus(this.isChecked());
                showActualSelection();
            } catch (PartInitException e) {
                LOGGER.warn("The status of the graphical visualization could not be set.", e);
            }
        }
    }
    
    /**
     * Action to toggle if the diagram should be always translated when an change happen. 
     */
    private Action toggleLiveAction = new Action("", IAction.AS_PUSH_BUTTON) {
        @Override
        public void run() {
            toggleLive = !toggleLive;
            if (toggleLive) {
                toggleLiveAction.setImageDescriptor(getImage(TERMINATE));
                toggleLiveAction.setToolTipText(DISABLE_TOOLTIP);
                showActualSelection();                    
            } else {
                toggleLiveAction.setImageDescriptor(getImage(START));
                toggleLiveAction.setToolTipText(ENABLE_TOOLTIP);
            }    
        }
    };


    private final Action showDiagramAction = new ShowVisualizationAction();


    /**
     * Listener fires if there was a change in the papyrus diagram editor.
     */
    private IPropertyListener propertyListener = new IPropertyListener() {
        @Override
        public void propertyChanged(Object source, int propId) {
            showActualSelection();
        }
    }; 

    /**
     * Listener for the part life cycle that adds and removes property listeners,
     * if a editor has been selected or deselected.
     */
    private IPartListener2 partListener = new IPartListener2() {
        @Override
        public void partActivated(IWorkbenchPartReference partRef) {
            partRef.addPropertyListener(propertyListener);
        }

        @Override
        public void partBroughtToTop(IWorkbenchPartReference partRef) {
            showActualSelection();
        }
        
        @Override
        public void partDeactivated(IWorkbenchPartReference partRef) {
            partRef.removePropertyListener(propertyListener);
        }

        @Override
        public void partClosed(IWorkbenchPartReference partRef) { }

        @Override
        public void partOpened(IWorkbenchPartReference partRef) { }

        @Override
        public void partHidden(IWorkbenchPartReference partRef) { }
        
        @Override
        public void partVisible(IWorkbenchPartReference partRef) { }
        
        @Override
        public void partInputChanged(IWorkbenchPartReference partRef) { }
    };

    /**
     * Gets the selection from the actual editor and shows it.
     */
    private void showActualSelection() {
        IEditorPart actualEditor = getSite().getWorkbenchWindow().getActivePage().getActiveEditor();
        if (actualEditor != null) {
            ISelection actualSelection = actualEditor.getEditorSite().getSelectionProvider().getSelection();
            showSelection(actualEditor.getSite().getPart(), actualSelection);
        } else {
            showText("No Model Available");
        }
    }

    /**
     * Returns an ImageDescriptor for the given image.
     * @param relativePath the path to the image
     * @return an ImageDescriptor for the given image
     */
    private static ImageDescriptor getImage(String relativePath) {
        Bundle bundle = Platform.getBundle(PLUGIN_ID);
        URL url = FileLocator.find(bundle, new Path("/icons/" + relativePath), null);
        return ImageDescriptor.createFromURL(url);
    }

    /**
     * Shows the given selection in this view.
     */
    public void showSelection(IWorkbenchPart sourcepart, ISelection selection) {
        setContentDescription(sourcepart.getTitle());

        if (selection instanceof IStructuredSelection && toggleLive) {
            IStructuredSelection ss = (IStructuredSelection) selection;
            showItems(ss.getFirstElement());
        }
    }

    /**
     * Gets the diagram of the given item and compiles the diagram.
     * @param item the item to get the diagram for
     */
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

    /**
     * Compiles the given diagram and shows the result.
     * @param diagram the diagram to translate
     */
    private void showPlantUml(Diagram diagram) {
        PlantGenerator p = new PlantGenerator();
        showText(p.compile(diagram).toString());
    }

    /**
     * Shows the given text in the textviewer.
     * @param text the text to show in the textviewer
     */
    private void showText(String text) {
        textviewer.setDocument(new Document(text));
        try {
            visualization.updateVisualization(text);
        } catch (PartInitException e) {
            LOGGER.warn("The graphical visualization could not be updated.", e);
        }
    }

    @Override
    public void createPartControl(Composite parent) {
        textviewer = new TextViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        textviewer.setEditable(false);

        toggleLiveAction.setImageDescriptor(getImage(TERMINATE));        
        toggleLiveAction.setToolTipText(DISABLE_TOOLTIP);

        showDiagramAction.setImageDescriptor(getImage(PLANT));
        showDiagramAction.setToolTipText(PLANT_TOOLTIP);
        
        IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();

        mgr.add(toggleLiveAction);
        mgr.add(showDiagramAction);

        getSite().getPage().addPartListener(partListener);
        showActualSelection();
    }

    @Override
    public void dispose() {
        removeAllPropertyListeners();
        super.dispose();
    }

    /**
     * Removes all PropertyListeners.
     */
    private void removeAllPropertyListeners() {
        IEditorReference[] ref = getSite().getPage().getEditorReferences();
        for (IEditorReference editor : ref) {
            editor.removePropertyListener(propertyListener);
        }
    }

    @Override
    public void setFocus() {

    }
}
