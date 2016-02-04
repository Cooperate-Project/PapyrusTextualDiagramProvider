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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Iterables;

import de.cooperateproject.notation2plant.UseCaseDiagramGenerator;

public class UseCaseDiagramGeneratorTest {

	private static UseCaseDiagramGenerator generator;

	@BeforeClass
	public static void setUp() {
		generator = new UseCaseDiagramGenerator();

		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("notation", new XMIResourceFactoryImpl());
		m.put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);

		Registry preg = EPackage.Registry.INSTANCE;
		preg.replace(NotationPackage.eNS_URI, NotationPackage.eINSTANCE);
		preg.replace(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		preg.replace(StylePackage.eNS_URI, StylePackage.eINSTANCE);
	}

	@Test
	public void nullDiagramTest() {
		generator.compileUseCaseDiagram(null);
	}

	@Test
	public void test1() throws IOException {
		String s = "";
		for (final Diagram d : getDiagrams("testdiagrams/usecase.notation")) {
			s = generator.compileUseCaseDiagram(d).toString();
			s = s;
		}
		
	}

	private Iterable<Diagram> getDiagrams(String url) throws IOException {
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

	/*
	 * @Test public void test() { Diagram d = mock(Diagram.class);
	 * 
	 * EList<EObject> l= new BasicEList<EObject>(); EList<EObject> edges= new
	 * BasicEList<EObject>(); l.add(UMLFactory.eINSTANCE.createPackage());
	 * when(d.getChildren()).thenReturn(l);
	 * when(d.getEdges()).thenReturn(edges);
	 * generator.compileActivityDiagram(d);
	 * 
	 * }
	 */
	@AfterClass
	public static void cleanUp() {
		generator = null;
	}
}
