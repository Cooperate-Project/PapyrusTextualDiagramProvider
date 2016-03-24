package de.cooperateproject.notation2plant.viewer;

import java.util.Collection;

import org.eclipse.ui.PartInitException;

import de.cooperateproject.notation2plant.viewer.extensions.GraphicalPlantUMLVisualizer;
import edu.kit.ipd.sdq.vitruvius.framework.util.bridges.EclipseBridge;

public class Uml2PlantUmlViewGraphicalVisualization {

    private static final String VISUALIZERS_EXTENSION_ID = "de.cooperateproject.plantumlpp.notation2plant.viewer.graphicalvisualizer";
    private static final Collection<GraphicalPlantUMLVisualizer> graphicalVisualizers = EclipseBridge
            .getRegisteredExtensions(VISUALIZERS_EXTENSION_ID, "provider", GraphicalPlantUMLVisualizer.class);;

    private boolean visualizationEnabled = false;

    public void updateVisualization(String plantUMLText) throws PartInitException {
        if (!visualizationEnabled) {
            return;
        }
        
        for (GraphicalPlantUMLVisualizer visualizer : graphicalVisualizers) {
            visualizer.init(); // the view might have been closed in the meantime
            visualizer.visualize(plantUMLText);
        }
    }

    public void changeEnabledStatus(boolean status) throws PartInitException {
        if (visualizationEnabled && !status) {
            visualizationEnabled = false;
            disable();
        } else if (!visualizationEnabled && status) {
            visualizationEnabled = true;
            enable();
        }
    }

    private void enable() throws PartInitException {
        for (GraphicalPlantUMLVisualizer visualizer : graphicalVisualizers) {
            visualizer.init();
        }
    }

    private void disable() {
        for (GraphicalPlantUMLVisualizer visualizer : graphicalVisualizers) {
            visualizer.dispose();
        }
    }
}
