package de.cooperateproject.plantumlpp.notation2plant.tests;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.papyrus.infra.viewpoints.style.StylePackage;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class FileRessource {
	protected Iterable<Diagram> getDiagrams(String url) throws IOException {
		File f = new File(url);

		ResourceSet resSet = new ResourceSetImpl();
		UMLResourcesUtil.initURIConverterURIMap(resSet.getURIConverter().getURIMap());

		URI inputNotationURI = URI.createFileURI(f.getAbsolutePath());
		Resource notationResource = resSet.getResource(inputNotationURI, true);
		notationResource.load(null);
		EcoreUtil.resolveAll(notationResource);		
		TreeIterator<EObject> _allContents = notationResource.getAllContents();
		Iterable<EObject> _iterable = IteratorExtensions.<EObject> toIterable(_allContents);
		Iterable<Diagram> _filter = Iterables.<Diagram> filter(_iterable, Diagram.class);
		return _filter;
	}
	
	protected static void ressourceSetUp() {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("notation", new XMIResourceFactoryImpl());
		m.put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);

		Registry preg = EPackage.Registry.INSTANCE;
		preg.replace(NotationPackage.eNS_URI, NotationPackage.eINSTANCE);
		preg.replace(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		preg.replace(StylePackage.eNS_URI, StylePackage.eINSTANCE);
	}
	
	protected Diagram getDiagram(Iterable<Diagram> diagrams, String name) {
		return Iterables.find(diagrams, new Predicate<Diagram>() {
			@Override
			public boolean apply(Diagram input) {				
				return input.getName().equals(name);
			}
		});	
	}
}
