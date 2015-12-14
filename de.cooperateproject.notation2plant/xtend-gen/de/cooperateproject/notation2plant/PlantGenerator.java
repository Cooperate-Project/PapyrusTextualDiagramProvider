package de.cooperateproject.notation2plant;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.cooperateproject.notation2plant.ActivityDiagramGenerator;
import de.cooperateproject.notation2plant.ClassDiagramGenerator;
import de.cooperateproject.notation2plant.UseCaseDiagramGenerator;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class PlantGenerator implements IGenerator {
  private final static Logger LOG = Logger.getLogger("PlantGenerator");
  
  @Override
  public void doGenerate(final Resource resource, final IFileSystemAccess fsa) {
    TreeIterator<EObject> _allContents = resource.getAllContents();
    Iterable<EObject> _iterable = IteratorExtensions.<EObject>toIterable(_allContents);
    Iterable<Diagram> _filter = Iterables.<Diagram>filter(_iterable, Diagram.class);
    for (final Diagram d : _filter) {
      String _name = d.getName();
      String _replace = _name.replace(" ", "_");
      String _plus = (_replace + ".pu");
      CharSequence _compile = this.compile(d);
      fsa.generateFile(_plus, _compile);
    }
  }
  
  public CharSequence compile(final Diagram diagram) {
    CharSequence _xifexpression = null;
    String _type = diagram.getType();
    boolean _equals = Objects.equal(_type, "PapyrusUMLClassDiagram");
    if (_equals) {
      CharSequence _xblockexpression = null;
      {
        ClassDiagramGenerator generator = new ClassDiagramGenerator();
        _xblockexpression = generator.compileClassDiagram(diagram);
      }
      _xifexpression = _xblockexpression;
    } else {
      CharSequence _xifexpression_1 = null;
      String _type_1 = diagram.getType();
      boolean _equals_1 = Objects.equal(_type_1, "PapyrusUMLActivityDiagram");
      if (_equals_1) {
        CharSequence _xblockexpression_1 = null;
        {
          ActivityDiagramGenerator generator = new ActivityDiagramGenerator();
          _xblockexpression_1 = generator.compileActivityDiagram(diagram);
        }
        _xifexpression_1 = _xblockexpression_1;
      } else {
        CharSequence _xifexpression_2 = null;
        String _type_2 = diagram.getType();
        boolean _equals_2 = Objects.equal(_type_2, "UseCase");
        if (_equals_2) {
          CharSequence _xblockexpression_2 = null;
          {
            UseCaseDiagramGenerator generator = new UseCaseDiagramGenerator();
            _xblockexpression_2 = generator.compileUseCaseDiagram(diagram);
          }
          _xifexpression_2 = _xblockexpression_2;
        } else {
          String _xblockexpression_3 = null;
          {
            String _name = diagram.getName();
            String _plus = ("no matching diagram type found for: " + _name);
            PlantGenerator.LOG.error(_plus);
            _xblockexpression_3 = "(empty)";
          }
          _xifexpression_2 = _xblockexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
}
