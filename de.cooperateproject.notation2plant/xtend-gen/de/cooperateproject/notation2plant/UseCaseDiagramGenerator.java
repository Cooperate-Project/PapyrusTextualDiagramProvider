package de.cooperateproject.notation2plant;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.Actor;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PackageMerge;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.UseCase;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class UseCaseDiagramGenerator {
  private final static Logger LOG = Logger.getLogger("UseCaseGenerator");
  
  public CharSequence compileUseCaseDiagram(final Diagram diagram) {
    CharSequence _xblockexpression = null;
    {
      EList _children = diagram.getChildren();
      Iterable<Shape> _filter = Iterables.<Shape>filter(_children, Shape.class);
      final Function1<Shape, Boolean> _function = (Shape x) -> {
        EObject _element = x.getElement();
        return Boolean.valueOf((!(_element instanceof Comment)));
      };
      Iterable<Shape> shapes = IterableExtensions.<Shape>filter(_filter, _function);
      EList _children_1 = diagram.getChildren();
      Iterable<Shape> _filter_1 = Iterables.<Shape>filter(_children_1, Shape.class);
      final Function1<Shape, Boolean> _function_1 = (Shape x) -> {
        EObject _element = x.getElement();
        return Boolean.valueOf((_element instanceof Comment));
      };
      Iterable<Shape> comments = IterableExtensions.<Shape>filter(_filter_1, _function_1);
      EList _edges = diagram.getEdges();
      Iterable<Connector> edges = Iterables.<Connector>filter(_edges, Connector.class);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("@startuml");
      _builder.newLine();
      _builder.append("title ");
      String _name = diagram.getName();
      _builder.append(_name, "");
      _builder.newLineIfNotEmpty();
      {
        for(final Shape shape : shapes) {
          EObject _element = shape.getElement();
          CharSequence _declaration = this.declaration(_element, shape);
          _builder.append(_declaration, "");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        for(final Connector edge : edges) {
          {
            EObject _element_1 = edge.getElement();
            boolean _notEquals = (!Objects.equal(_element_1, null));
            if (_notEquals) {
              EObject _element_2 = edge.getElement();
              CharSequence _declaration_1 = this.declaration(_element_2);
              _builder.append(_declaration_1, "");
              _builder.newLineIfNotEmpty();
            } else {
              CharSequence _declaration_2 = this.declaration(edge);
              _builder.append(_declaration_2, "");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      {
        for(final Shape comment : comments) {
          EObject _element_3 = comment.getElement();
          CharSequence _declaration_3 = this.declaration(_element_3, comment);
          _builder.append(_declaration_3, "");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("@enduml");
      _builder.newLine();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  private CharSequence _declaration(final Actor actor, final Shape s) {
    StringConcatenation _builder = new StringConcatenation();
    String _printElement = this.printElement(actor);
    _builder.append(_printElement, "");
    _builder.newLineIfNotEmpty();
    {
      EList<Generalization> _generalizations = actor.getGeneralizations();
      int _size = _generalizations.size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        EList<Generalization> _generalizations_1 = actor.getGeneralizations();
        CharSequence _printGeneralizations = this.printGeneralizations(_generalizations_1, actor);
        _builder.append(_printGeneralizations, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  private CharSequence _declaration(final org.eclipse.uml2.uml.Package pack, final Shape s) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("rectangle ");
    String _name = pack.getName();
    _builder.append(_name, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    {
      EList<PackageableElement> _packagedElements = pack.getPackagedElements();
      Iterable<UseCase> _filter = Iterables.<UseCase>filter(_packagedElements, UseCase.class);
      for(final UseCase element : _filter) {
        CharSequence _declaration = this.declaration(element);
        _builder.append(_declaration, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    CharSequence _printPackageImports = this.printPackageImports(pack);
    _builder.append(_printPackageImports, "");
    _builder.newLineIfNotEmpty();
    CharSequence _printPackageMerges = this.printPackageMerges(pack);
    _builder.append(_printPackageMerges, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  private CharSequence _declaration(final UseCase use, final Shape s) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _declaration = this.declaration(use);
    _builder.append(_declaration, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  private CharSequence _declaration(final Comment comment, final Shape s) {
    CharSequence _xblockexpression = null;
    {
      String _body = comment.getBody();
      String _substring = _body.substring(0, 3);
      final String noteName = ("N" + _substring);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("note as ");
      _builder.append(noteName, "");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      String _body_1 = comment.getBody();
      _builder.append(_body_1, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("end note");
      _builder.newLine();
      {
        EList<Element> _annotatedElements = comment.getAnnotatedElements();
        for(final Element ae : _annotatedElements) {
          _builder.append(noteName, "");
          _builder.append(" .. ");
          CharSequence _printElement = this.printElement(ae);
          _builder.append(_printElement, "");
          _builder.newLineIfNotEmpty();
        }
      }
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  private CharSequence _declaration(final Association a) {
    return this.defineAssociation(a);
  }
  
  private CharSequence _declaration(final Realization r) {
    return this.defineRealization(r);
  }
  
  private CharSequence _declaration(final Dependency d) {
    return this.defineRealization(d);
  }
  
  private CharSequence _declaration(final Usage u) {
    return this.defineRealization(u);
  }
  
  private CharSequence _declaration(final Abstraction a) {
    return this.defineRealization(a);
  }
  
  private CharSequence defineAssociation(final Association association) {
    CharSequence _xifexpression = null;
    boolean _isBinary = association.isBinary();
    if (_isBinary) {
      CharSequence _xblockexpression = null;
      {
        EList<Property> _memberEnds = association.getMemberEnds();
        final Property head = IterableExtensions.<Property>head(_memberEnds);
        EList<Property> _memberEnds_1 = association.getMemberEnds();
        final Property tail = IterableExtensions.<Property>last(_memberEnds_1);
        StringConcatenation _builder = new StringConcatenation();
        Type _type = head.getType();
        String _printElement = this.printElement(_type);
        _builder.append(_printElement, "");
        _builder.append(" -- ");
        Type _type_1 = tail.getType();
        String _printElement_1 = this.printElement(_type_1);
        _builder.append(_printElement_1, "");
        String _name = association.getName();
        CharSequence _printLabel = this.printLabel(_name);
        _builder.append(_printLabel, "");
        _builder.newLineIfNotEmpty();
        _xblockexpression = _builder;
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  private CharSequence defineRealization(final Realization realization) {
    CharSequence _xblockexpression = null;
    {
      EList<NamedElement> _clients = realization.getClients();
      final NamedElement head = IterableExtensions.<NamedElement>head(_clients);
      EList<NamedElement> _suppliers = realization.getSuppliers();
      final NamedElement tail = IterableExtensions.<NamedElement>head(_suppliers);
      CharSequence _xifexpression = null;
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(head, null));
      if (!_notEquals) {
        _and = false;
      } else {
        boolean _notEquals_1 = (!Objects.equal(tail, null));
        _and = _notEquals_1;
      }
      if (_and) {
        StringConcatenation _builder = new StringConcatenation();
        String _printElement = this.printElement(head);
        _builder.append(_printElement, "");
        _builder.append(" ..|> ");
        String _printElement_1 = this.printElement(tail);
        _builder.append(_printElement_1, "");
        String _name = realization.getName();
        CharSequence _printLabel = this.printLabel(_name);
        _builder.append(_printLabel, "");
        _builder.newLineIfNotEmpty();
        _xifexpression = _builder;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  private CharSequence defineRealization(final Dependency dependency) {
    CharSequence _xblockexpression = null;
    {
      EList<NamedElement> _clients = dependency.getClients();
      final NamedElement head = IterableExtensions.<NamedElement>head(_clients);
      EList<NamedElement> _suppliers = dependency.getSuppliers();
      final NamedElement tail = IterableExtensions.<NamedElement>head(_suppliers);
      CharSequence _xifexpression = null;
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(head, null));
      if (!_notEquals) {
        _and = false;
      } else {
        boolean _notEquals_1 = (!Objects.equal(tail, null));
        _and = _notEquals_1;
      }
      if (_and) {
        StringConcatenation _builder = new StringConcatenation();
        String _printElement = this.printElement(head);
        _builder.append(_printElement, "");
        _builder.append(" ..> ");
        String _printElement_1 = this.printElement(tail);
        _builder.append(_printElement_1, "");
        String _name = dependency.getName();
        CharSequence _printLabel = this.printLabel(_name);
        _builder.append(_printLabel, "");
        _builder.newLineIfNotEmpty();
        _xifexpression = _builder;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  private CharSequence defineRealization(final Usage usage) {
    CharSequence _xblockexpression = null;
    {
      EList<NamedElement> _clients = usage.getClients();
      final NamedElement head = IterableExtensions.<NamedElement>head(_clients);
      EList<NamedElement> _suppliers = usage.getSuppliers();
      final NamedElement tail = IterableExtensions.<NamedElement>head(_suppliers);
      CharSequence _xifexpression = null;
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(head, null));
      if (!_notEquals) {
        _and = false;
      } else {
        boolean _notEquals_1 = (!Objects.equal(tail, null));
        _and = _notEquals_1;
      }
      if (_and) {
        StringConcatenation _builder = new StringConcatenation();
        String _printElement = this.printElement(head);
        _builder.append(_printElement, "");
        _builder.append(" ..> ");
        String _printElement_1 = this.printElement(tail);
        _builder.append(_printElement_1, "");
        _builder.append(" : use");
        _builder.newLineIfNotEmpty();
        _xifexpression = _builder;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  private CharSequence defineRealization(final Abstraction abstraction) {
    CharSequence _xblockexpression = null;
    {
      EList<NamedElement> _clients = abstraction.getClients();
      final NamedElement head = IterableExtensions.<NamedElement>head(_clients);
      EList<NamedElement> _suppliers = abstraction.getSuppliers();
      final NamedElement tail = IterableExtensions.<NamedElement>head(_suppliers);
      CharSequence _xifexpression = null;
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(head, null));
      if (!_notEquals) {
        _and = false;
      } else {
        boolean _notEquals_1 = (!Objects.equal(tail, null));
        _and = _notEquals_1;
      }
      if (_and) {
        StringConcatenation _builder = new StringConcatenation();
        String _printElement = this.printElement(head);
        _builder.append(_printElement, "");
        _builder.append(" ..> ");
        String _printElement_1 = this.printElement(tail);
        _builder.append(_printElement_1, "");
        _builder.append(" : abstraction");
        _builder.newLineIfNotEmpty();
        _xifexpression = _builder;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  private CharSequence _declaration(final UseCase use) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<NamedElement> _members = use.getMembers();
      int _size = _members.size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        EList<NamedElement> _members_1 = use.getMembers();
        CharSequence _printMembers = this.printMembers(_members_1, use);
        _builder.append(_printMembers, "");
        _builder.newLineIfNotEmpty();
      } else {
        String _printElement = this.printElement(use);
        _builder.append(_printElement, "");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      EList<Generalization> _generalizations = use.getGeneralizations();
      int _size_1 = _generalizations.size();
      boolean _greaterThan_1 = (_size_1 > 0);
      if (_greaterThan_1) {
        EList<Generalization> _generalizations_1 = use.getGeneralizations();
        CharSequence _printGeneralizations = this.printGeneralizations(_generalizations_1, use);
        _builder.append(_printGeneralizations, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence printElement(final Element element) {
    CharSequence _xifexpression = null;
    if ((element instanceof NamedElement)) {
      StringConcatenation _builder = new StringConcatenation();
      String _printElement = this.printElement(((NamedElement)element));
      _builder.append(_printElement, "");
      _xifexpression = _builder;
    }
    return _xifexpression;
  }
  
  private String printElement(final NamedElement element) {
    String _xblockexpression = null;
    {
      String cs = "";
      if ((element instanceof Actor)) {
        String _cs = cs;
        String _name = ((Actor)element).getName();
        String _plus = (":" + _name);
        String _plus_1 = (_plus + ":");
        cs = (_cs + _plus_1);
      } else {
        if ((element instanceof UseCase)) {
          String _cs_1 = cs;
          String _name_1 = ((UseCase)element).getName();
          String _plus_2 = ("(" + _name_1);
          String _plus_3 = (_plus_2 + ")");
          cs = (_cs_1 + _plus_3);
        } else {
          if ((element instanceof org.eclipse.uml2.uml.Package)) {
            String _cs_2 = cs;
            String _name_2 = ((org.eclipse.uml2.uml.Package)element).getName();
            cs = (_cs_2 + _name_2);
          }
        }
      }
      EList<EObject> _stereotypeApplications = element.getStereotypeApplications();
      for (final EObject st : _stereotypeApplications) {
        String _cs_3 = cs;
        CharSequence _printStereotypeName = this.printStereotypeName(st);
        cs = (_cs_3 + _printStereotypeName);
      }
      _xblockexpression = cs;
    }
    return _xblockexpression;
  }
  
  public CharSequence printStereotypeName(final EObject object) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(" ", "");
    _builder.append("<< ");
    EClass _eClass = object.eClass();
    String _name = _eClass.getName();
    _builder.append(_name, "");
    _builder.append(" >>");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  private CharSequence printLabel(final String label) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _notEquals = (!Objects.equal(label, null));
      if (_notEquals) {
        _builder.append(" : ");
        _builder.append(label, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  private CharSequence printGeneralizations(final List<Generalization> elements, final NamedElement parent) {
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final Generalization element : elements) {
        String _printElement = this.printElement(parent);
        _builder.append(_printElement, "");
        _builder.append(" --|> ");
        Classifier _general = element.getGeneral();
        String _printElement_1 = this.printElement(_general);
        _builder.append(_printElement_1, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  private CharSequence printMembers(final List<NamedElement> elements, final NamedElement parent) {
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final NamedElement element : elements) {
        {
          if ((element instanceof Include)) {
            String _printElement = this.printElement(parent);
            _builder.append(_printElement, "");
            _builder.append(" .> (");
            UseCase _addition = ((Include)element).getAddition();
            String _name = _addition.getName();
            _builder.append(_name, "");
            _builder.append(") : include");
            _builder.newLineIfNotEmpty();
          } else {
            {
              if ((element instanceof Extend)) {
                String _printElement_1 = this.printElement(parent);
                _builder.append(_printElement_1, "");
                _builder.append(" .> (");
                UseCase _extendedCase = ((Extend)element).getExtendedCase();
                String _name_1 = _extendedCase.getName();
                _builder.append(_name_1, "");
                _builder.append(") : extend");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    return _builder;
  }
  
  private CharSequence printPackageImports(final org.eclipse.uml2.uml.Package pack) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<PackageImport> _packageImports = pack.getPackageImports();
      int _size = _packageImports.size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        {
          EList<PackageImport> _packageImports_1 = pack.getPackageImports();
          for(final PackageImport imp : _packageImports_1) {
            String _name = pack.getName();
            _builder.append(_name, "");
            _builder.append(" ..> ");
            org.eclipse.uml2.uml.Package _importedPackage = imp.getImportedPackage();
            String _name_1 = _importedPackage.getName();
            _builder.append(_name_1, "");
            _builder.append(" : import");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  private CharSequence printPackageMerges(final org.eclipse.uml2.uml.Package pack) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<PackageMerge> _packageMerges = pack.getPackageMerges();
      int _size = _packageMerges.size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        {
          EList<PackageMerge> _packageMerges_1 = pack.getPackageMerges();
          for(final PackageMerge imp : _packageMerges_1) {
            String _name = pack.getName();
            _builder.append(_name, "");
            _builder.append(" ..> ");
            org.eclipse.uml2.uml.Package _mergedPackage = imp.getMergedPackage();
            String _name_1 = _mergedPackage.getName();
            _builder.append(_name_1, "");
            _builder.append(" : merge");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  private CharSequence _declaration(final EObject o, final Shape s) {
    EClass _eClass = o.eClass();
    String _name = _eClass.getName();
    String _plus = ("Element ignored: " + _name);
    UseCaseDiagramGenerator.LOG.warn(_plus);
    return null;
  }
  
  private CharSequence _declaration(final EObject o) {
    EClass _eClass = o.eClass();
    String _name = _eClass.getName();
    String _plus = ("Element ignored: " + _name);
    UseCaseDiagramGenerator.LOG.warn(_plus);
    return null;
  }
  
  private CharSequence _declaration(final Void a) {
    UseCaseDiagramGenerator.LOG.error(("Element is " + a));
    return null;
  }
  
  private CharSequence _declaration(final Void a, final Shape s) {
    UseCaseDiagramGenerator.LOG.error(("Element is " + a));
    return null;
  }
  
  private CharSequence declaration(final EObject actor, final Shape s) {
    if (actor instanceof Actor) {
      return _declaration((Actor)actor, s);
    } else if (actor instanceof UseCase) {
      return _declaration((UseCase)actor, s);
    } else if (actor instanceof org.eclipse.uml2.uml.Package) {
      return _declaration((org.eclipse.uml2.uml.Package)actor, s);
    } else if (actor instanceof Comment) {
      return _declaration((Comment)actor, s);
    } else if (actor != null) {
      return _declaration(actor, s);
    } else if (actor == null) {
      return _declaration((Void)null, s);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(actor, s).toString());
    }
  }
  
  private CharSequence declaration(final EObject use) {
    if (use instanceof UseCase) {
      return _declaration((UseCase)use);
    } else if (use instanceof Association) {
      return _declaration((Association)use);
    } else if (use instanceof Realization) {
      return _declaration((Realization)use);
    } else if (use instanceof Abstraction) {
      return _declaration((Abstraction)use);
    } else if (use instanceof Usage) {
      return _declaration((Usage)use);
    } else if (use instanceof Dependency) {
      return _declaration((Dependency)use);
    } else if (use != null) {
      return _declaration(use);
    } else if (use == null) {
      return _declaration((Void)null);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(use).toString());
    }
  }
}
