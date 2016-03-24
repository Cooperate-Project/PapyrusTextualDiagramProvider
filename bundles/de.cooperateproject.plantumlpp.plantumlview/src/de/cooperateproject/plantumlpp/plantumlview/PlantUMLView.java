package de.cooperateproject.plantumlpp.plantumlview;

import java.util.Arrays;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import net.sourceforge.plantuml.eclipse.actions.ToggleButtonAction;

public class PlantUMLView extends net.sourceforge.plantuml.eclipse.views.PlantUmlView {

    public static final String ID = "de.cooperateproject.plantumlpp.plantumlview.PlantUMLView";
    private boolean disposed = false;
    
    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
        Optional<IContributionItem> toggleButton = Iterables.tryFind(Arrays.asList(toolbarManager.getItems()),
                new Predicate<IContributionItem>() {
                    @Override
                    public boolean apply(IContributionItem input) {
                        if (input instanceof ActionContributionItem) {
                            return ((ActionContributionItem)input).getAction() instanceof ToggleButtonAction;
                        }
                        return false;
                    }
                });
        if (toggleButton.isPresent()) {
            toolbarManager.remove(toggleButton.get());            
        }
    }
    
    public void setText(String text) {
        super.updateDiagramText(text);
    }

    @Override
    protected void registerListeners() {
        /**
         * Does not call super method intentionally. We do not want any listeners to be registered.
         */
    }

    @Override
    public void dispose() {
        super.dispose();
        disposed = true;
    }

    public boolean isDisposed() {
        return disposed;
    }
    
}
