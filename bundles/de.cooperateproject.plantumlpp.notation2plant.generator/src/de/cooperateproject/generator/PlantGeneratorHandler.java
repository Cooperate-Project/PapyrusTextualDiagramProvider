package de.cooperateproject.generator;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;


import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.infra.onefile.model.IPapyrusFile;
import org.eclipse.papyrus.infra.viewpoints.style.StylePackage;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.parser.IEncodingProvider;
import org.eclipse.xtext.service.AbstractGenericModule;

import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.inject.Guice;

import edu.kit.ipd.sdq.commons.ecore2txt.handler.AbstractEcore2TxtHandler;

public class PlantGeneratorHandler extends AbstractEcore2TxtHandler<IFile> {

    private final JavaIoFileSystemAccess fsa = new JavaIoFileSystemAccess();
    private final PlantGenerator generator = new PlantGenerator();

    public PlantGeneratorHandler() {
        Guice.createInjector(new AbstractGenericModule() {

            @SuppressWarnings("unused")
            public Class<? extends IEncodingProvider> bindIEncodingProvider() {
                return IEncodingProvider.Runtime.class;
            }

        }).injectMembers(fsa);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<IFile> filterSelection(ISelection selection) {
        if (!(selection instanceof IStructuredSelection)) {
            return (Iterable<IFile>) Collections.<IFile> emptyList();
        }

        IStructuredSelection structuredSelection = (IStructuredSelection) selection;
        FluentIterable<IPapyrusFile> files = FluentIterable
        		.from(structuredSelection.toList())
        		.filter(IPapyrusFile.class);
        //can not be combined in eclipse due to dubios reasons
        return files
        		.transform(IPapyrusFile::getMainFile)
        		.filter(Predicates.notNull());
    }

    @Override
    public String getPlugInID() {
        return "de.cooperateproject.plantumlpp.notation2plant.generator";
    }

    @Override
    public void executeEcore2TxtGenerator(Iterable<IFile> filteredSelection, ExecutionEvent event, String plugInID)
            throws ExecutionException {
        if (filteredSelection == null) {
            return;
        }

        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("notation", new XMIResourceFactoryImpl());
        m.put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);

        Registry preg = EPackage.Registry.INSTANCE;
        preg.replace(NotationPackage.eNS_URI, NotationPackage.eINSTANCE);
        preg.replace(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
        preg.replace(StylePackage.eNS_URI, StylePackage.eINSTANCE);

        ResourceSet resSet = new ResourceSetImpl();
        UMLResourcesUtil.initURIConverterURIMap(resSet.getURIConverter().getURIMap());

        for (IFile file : filteredSelection) {
            try {
                executeEcore2TxtGenerator(resSet, file);
            } catch (IOException e) {
                throw new ExecutionException("Error while processing file " + file.getName() + ".", e);
            }
        }
    }

    private void executeEcore2TxtGenerator(ResourceSet resSet, IFile file) throws IOException {
        IPath modelPath = file.getLocation().removeFileExtension();
        File umlFile = new File(modelPath.addFileExtension("uml").toOSString());
        URI inputModelURI = URI.createFileURI(umlFile.getAbsolutePath());
        Resource modelResource = resSet.getResource(inputModelURI, true);
        modelResource.load(null);

        File notationFile = new File(modelPath.addFileExtension("notation").toOSString());
        URI inputNotationURI = URI.createFileURI(notationFile.getAbsolutePath());
        Resource notationResource = resSet.getResource(inputNotationURI, true);
        notationResource.load(null);
        EcoreUtil.resolveAll(notationResource);

        String outBase = URI.createFileURI(modelPath + "/out/").toFileString();

        fsa.setOutputPath(outBase);

        generator.doGenerate(notationResource, fsa);
    }

}
