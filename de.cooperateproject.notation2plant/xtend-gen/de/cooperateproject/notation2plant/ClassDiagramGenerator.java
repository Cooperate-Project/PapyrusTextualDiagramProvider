package de.cooperateproject.notation2plant;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import java.util.Arrays;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.BasicCompartment;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.ClassifierTemplateParameter;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.ConnectableElement;
import org.eclipse.uml2.uml.ConnectableElementTemplateParameter;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.DurationObservation;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InformationItem;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.ParameterableElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.TemplateSignature;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ClassDiagramGenerator {
  private final static Logger LOG = Logger.getLogger("PlantGenerator");
  
  public CharSequence compileClassDiagram(final Diagram diagram) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@startuml");
    _builder.newLine();
    _builder.append("title ");
    String _name = diagram.getName();
    _builder.append(_name, "");
    _builder.newLineIfNotEmpty();
    {
      EList _children = diagram.getChildren();
      Iterable<Shape> _filter = Iterables.<Shape>filter(_children, Shape.class);
      for(final Shape s : _filter) {
        EObject _element = s.getElement();
        CharSequence _declaration = this.declaration(_element, s);
        _builder.append(_declaration, "");
        _builder.append(" ");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      EList _edges = diagram.getEdges();
      Iterable<Connector> _filter_1 = Iterables.<Connector>filter(_edges, Connector.class);
      for(final Connector edge : _filter_1) {
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
    _builder.append("@enduml");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _declaration(final Connector g) {
    EClass _eClass = g.eClass();
    String _name = _eClass.getName();
    String _plus = ("encountered unsupported connector " + _name);
    ClassDiagramGenerator.LOG.error(_plus);
    return null;
  }
  
  protected CharSequence _declaration(final Generalization g) {
    return this.defineGeneralization(g);
  }
  
  protected CharSequence _declaration(final Association a) {
    return this.defineAssociation(a);
  }
  
  protected CharSequence _declaration(final InterfaceRealization a) {
    return this.defineRealization(a);
  }
  
  protected CharSequence _declaration(final org.eclipse.uml2.uml.Class c, final Shape s) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _defineClass = this.defineClass(c);
    _builder.append(_defineClass, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    {
      EList _children = s.getChildren();
      Iterable<BasicCompartment> _filter = Iterables.<BasicCompartment>filter(_children, BasicCompartment.class);
      for(final BasicCompartment bc : _filter) {
        _builder.append("\t", "");
        _builder.newLineIfNotEmpty();
        {
          EList _children_1 = bc.getChildren();
          Iterable<Shape> _filter_1 = Iterables.<Shape>filter(_children_1, Shape.class);
          for(final Shape innerShape : _filter_1) {
            _builder.append("\t");
            EObject _element = innerShape.getElement();
            CharSequence _declaration = this.declaration(_element);
            _builder.append(_declaration, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _declaration(final Interface i, final Shape s) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _defineInterface = this.defineInterface(i);
    _builder.append(_defineInterface, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    {
      EList _children = s.getChildren();
      Iterable<BasicCompartment> _filter = Iterables.<BasicCompartment>filter(_children, BasicCompartment.class);
      for(final BasicCompartment bc : _filter) {
        _builder.append("\t", "");
        _builder.newLineIfNotEmpty();
        {
          EList _children_1 = bc.getChildren();
          Iterable<Shape> _filter_1 = Iterables.<Shape>filter(_children_1, Shape.class);
          for(final Shape innerShape : _filter_1) {
            _builder.append("\t");
            EObject _element = innerShape.getElement();
            CharSequence _declaration = this.declaration(_element);
            _builder.append(_declaration, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _declaration(final org.eclipse.uml2.uml.Package p, final Shape s) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _definePackage = this.definePackage(p);
    _builder.append(_definePackage, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    {
      EList _children = s.getChildren();
      Iterable<BasicCompartment> _filter = Iterables.<BasicCompartment>filter(_children, BasicCompartment.class);
      for(final BasicCompartment bc : _filter) {
        _builder.append("\t", "");
        _builder.newLineIfNotEmpty();
        {
          EList _children_1 = bc.getChildren();
          Iterable<Shape> _filter_1 = Iterables.<Shape>filter(_children_1, Shape.class);
          for(final Shape innerShape : _filter_1) {
            _builder.append("\t");
            EObject _element = innerShape.getElement();
            CharSequence _declaration = this.declaration(_element, innerShape);
            _builder.append(_declaration, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _declaration(final Enumeration e, final Shape s) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _defineEnum = this.defineEnum(e);
    _builder.append(_defineEnum, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    {
      EList _children = s.getChildren();
      Iterable<BasicCompartment> _filter = Iterables.<BasicCompartment>filter(_children, BasicCompartment.class);
      for(final BasicCompartment bc : _filter) {
        _builder.append("\t", "");
        _builder.newLineIfNotEmpty();
        {
          EList _children_1 = bc.getChildren();
          Iterable<Shape> _filter_1 = Iterables.<Shape>filter(_children_1, Shape.class);
          for(final Shape innerShape : _filter_1) {
            _builder.append("\t");
            EObject _element = innerShape.getElement();
            CharSequence _declaration = this.declaration(_element);
            _builder.append(_declaration, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _declaration(final DataType d, final Shape s) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _defineDataType = this.defineDataType(d);
    _builder.append(_defineDataType, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    {
      EList _children = s.getChildren();
      Iterable<BasicCompartment> _filter = Iterables.<BasicCompartment>filter(_children, BasicCompartment.class);
      for(final BasicCompartment bc : _filter) {
        _builder.append("\t", "");
        _builder.newLineIfNotEmpty();
        {
          EList _children_1 = bc.getChildren();
          Iterable<Shape> _filter_1 = Iterables.<Shape>filter(_children_1, Shape.class);
          for(final Shape innerShape : _filter_1) {
            _builder.append("\t");
            EObject _element = innerShape.getElement();
            CharSequence _declaration = this.declaration(_element);
            _builder.append(_declaration, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _declaration(final Signal sig, final Shape s) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _defineSignal = this.defineSignal(sig);
    _builder.append(_defineSignal, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    {
      EList _children = s.getChildren();
      Iterable<BasicCompartment> _filter = Iterables.<BasicCompartment>filter(_children, BasicCompartment.class);
      for(final BasicCompartment bc : _filter) {
        _builder.append("\t", "");
        _builder.newLineIfNotEmpty();
        {
          EList _children_1 = bc.getChildren();
          Iterable<Shape> _filter_1 = Iterables.<Shape>filter(_children_1, Shape.class);
          for(final Shape innerShape : _filter_1) {
            _builder.append("\t");
            EObject _element = innerShape.getElement();
            CharSequence _declaration = this.declaration(_element);
            _builder.append(_declaration, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _declaration(final InformationItem i, final Shape s) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("class ");
    String _name = i.getName();
    _builder.append(_name, "");
    _builder.append(" << (>,orchid) >> {");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _declaration(final InstanceSpecification i, final Shape s) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("class \"");
    String _name = i.getName();
    _builder.append(_name, "");
    _builder.append(":");
    EList<Classifier> _classifiers = i.getClassifiers();
    CharSequence _names = this.getNames(_classifiers);
    _builder.append(_names, "");
    _builder.append("\" << (i,pink) >> {");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence getNames(final EList<Classifier> list) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _hasElements = false;
      for(final Classifier c : list) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _name = c.getName();
        _builder.append(_name, "");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _declaration(final Comment c, final Shape s) {
    CharSequence _xblockexpression = null;
    {
      String _body = c.getBody();
      String _substring = _body.substring(0, 3);
      final String noteName = ("N" + _substring);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("note as ");
      _builder.append(noteName, "");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      String _body_1 = c.getBody();
      _builder.append(_body_1, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("end note");
      _builder.newLine();
      {
        EList<Element> _annotatedElements = c.getAnnotatedElements();
        for(final Element ae : _annotatedElements) {
          _builder.append("\t", "");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append(noteName, "\t");
          _builder.append("..");
          String _name = ((org.eclipse.uml2.uml.Class) ae).getName();
          _builder.append(_name, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  protected CharSequence _declaration(final DurationObservation c, final Shape s) {
    CharSequence _xblockexpression = null;
    {
      String _name = c.getName();
      final String noteName = ("NDuration" + _name);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("note as ");
      _builder.append(noteName, "");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("end note");
      _builder.newLine();
      {
        EList<NamedElement> _events = c.getEvents();
        for(final NamedElement ae : _events) {
          _builder.append("\t", "");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append(noteName, "\t");
          _builder.append("..");
          String _name_1 = ae.getName();
          _builder.append(_name_1, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  protected CharSequence _declaration(final EObject e, final Shape s) {
    EClass _eClass = e.eClass();
    String _name = _eClass.getName();
    String _plus = ("encountered a " + _name);
    ClassDiagramGenerator.LOG.error(_plus);
    throw new UnsupportedOperationException();
  }
  
  protected CharSequence _declaration(final Property element) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isStatic = element.isStatic();
      if (_isStatic) {
        _builder.append("{static}");
      }
    }
    _builder.newLineIfNotEmpty();
    VisibilityKind _visibility = element.getVisibility();
    String _declareVisibility = this.declareVisibility(_visibility);
    _builder.append(_declareVisibility, "");
    String _name = element.getName();
    _builder.append(_name, "");
    CharSequence _declareType = this.declareType(element);
    _builder.append(_declareType, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _declaration(final Operation element) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isAbstract = element.isAbstract();
      if (_isAbstract) {
        _builder.append("{abstract}");
      }
    }
    _builder.newLineIfNotEmpty();
    {
      boolean _isStatic = element.isStatic();
      if (_isStatic) {
        _builder.append("{static}");
      }
    }
    _builder.newLineIfNotEmpty();
    VisibilityKind _visibility = element.getVisibility();
    String _declareVisibility = this.declareVisibility(_visibility);
    _builder.append(_declareVisibility, "");
    String _name = element.getName();
    _builder.append(_name, "");
    _builder.append("(");
    EList<Parameter> _inputParameters = element.inputParameters();
    CharSequence _declareParameters = this.declareParameters(_inputParameters);
    _builder.append(_declareParameters, "");
    _builder.append(")");
    CharSequence _declareReturnType = this.declareReturnType(element);
    _builder.append(_declareReturnType, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _declaration(final EnumerationLiteral element) {
    StringConcatenation _builder = new StringConcatenation();
    String _name = element.getName();
    _builder.append(_name, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence declareReturnType(final Operation o) {
    CharSequence _xblockexpression = null;
    {
      EList<Parameter> _outputParameters = o.outputParameters();
      final Function1<Parameter, Boolean> _function = (Parameter p) -> {
        ParameterDirectionKind _direction = p.getDirection();
        return Boolean.valueOf(Objects.equal(_direction, ParameterDirectionKind.RETURN_LITERAL));
      };
      final Iterable<Parameter> retTypes = IterableExtensions.<Parameter>filter(_outputParameters, _function);
      final Parameter retType = IterableExtensions.<Parameter>head(retTypes);
      CharSequence _xifexpression = null;
      boolean _notEquals = (!Objects.equal(retType, null));
      if (_notEquals) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(": ");
        Type _type = retType.getType();
        String _name = _type.getName();
        _builder.append(_name, "");
        {
          boolean _isMultivalued = retType.isMultivalued();
          if (_isMultivalued) {
            _builder.append("[]");
          }
        }
        _xifexpression = _builder;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public CharSequence declareType(final Property p) {
    CharSequence _xblockexpression = null;
    {
      final Type dataType = p.getType();
      CharSequence _xifexpression = null;
      boolean _notEquals = (!Objects.equal(dataType, null));
      if (_notEquals) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(": ");
        String _name = dataType.getName();
        _builder.append(_name, "");
        {
          boolean _isMultivalued = p.isMultivalued();
          if (_isMultivalued) {
            _builder.append("[]");
          }
        }
        _xifexpression = _builder;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected CharSequence _declaration(final EObject element) {
    return element.toString();
  }
  
  public CharSequence declareParameters(final EList<Parameter> parameters) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _hasElements = false;
      for(final Parameter p : parameters) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _name = p.getName();
        _builder.append(_name, "");
        _builder.append(": ");
        Type _type = p.getType();
        String _name_1 = _type.getName();
        _builder.append(_name_1, "");
        {
          boolean _isMultivalued = p.isMultivalued();
          if (_isMultivalued) {
            _builder.append("[]");
          }
        }
      }
    }
    return _builder;
  }
  
  public String declareVisibility(final VisibilityKind vis) {
    String _switchResult = null;
    if (vis != null) {
      switch (vis) {
        case PRIVATE_LITERAL:
          _switchResult = "-";
          break;
        case PROTECTED_LITERAL:
          _switchResult = "#";
          break;
        case PUBLIC_LITERAL:
          _switchResult = "+";
          break;
        case PACKAGE_LITERAL:
          _switchResult = "~";
          break;
        default:
          _switchResult = "";
          break;
      }
    } else {
      _switchResult = "";
    }
    return _switchResult;
  }
  
  public CharSequence defineAssociation(final Association association) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isBinary = association.isBinary();
      if (_isBinary) {
        EList<Property> _memberEnds = association.getMemberEnds();
        final Property head = IterableExtensions.<Property>head(_memberEnds);
        _builder.newLineIfNotEmpty();
        EList<Property> _memberEnds_1 = association.getMemberEnds();
        final Property tail = IterableExtensions.<Property>last(_memberEnds_1);
        _builder.newLineIfNotEmpty();
        Type _type = head.getType();
        String _name = _type.getName();
        _builder.append(_name, "");
        _builder.append(" \"");
        String _name_1 = head.getName();
        _builder.append(_name_1, "");
        _builder.append(" ");
        CharSequence _cardinality = this.cardinality(head);
        _builder.append(_cardinality, "");
        _builder.append("\" ");
        String _leftArrow = this.leftArrow(head);
        _builder.append(_leftArrow, "");
        _builder.append("--");
        String _rightArrow = this.rightArrow(tail);
        _builder.append(_rightArrow, "");
        _builder.append("  \"");
        String _name_2 = tail.getName();
        _builder.append(_name_2, "");
        _builder.append(" ");
        CharSequence _cardinality_1 = this.cardinality(tail);
        _builder.append(_cardinality_1, "");
        _builder.append("\" ");
        Type _type_1 = tail.getType();
        String _name_3 = _type_1.getName();
        _builder.append(_name_3, "");
        _builder.append(" ");
        {
          String _name_4 = association.getName();
          boolean _notEquals = (!Objects.equal(_name_4, null));
          if (_notEquals) {
            _builder.append(": ");
            String _name_5 = association.getName();
            _builder.append(_name_5, "");
          }
        }
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public String leftArrow(final Property property) {
    String _xblockexpression = null;
    {
      final Property opp = property.getOtherEnd();
      String _switchResult = null;
      AggregationKind _aggregation = opp.getAggregation();
      int _value = _aggregation.getValue();
      switch (_value) {
        case AggregationKind.COMPOSITE:
          _switchResult = "*";
          break;
        case AggregationKind.SHARED:
          _switchResult = "o";
          break;
        default:
          String _xifexpression = null;
          boolean _isNavigable = opp.isNavigable();
          boolean _not = (!_isNavigable);
          if (_not) {
            _xifexpression = "<";
          } else {
            _xifexpression = "";
          }
          _switchResult = _xifexpression;
          break;
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
  
  public String rightArrow(final Property property) {
    String _xblockexpression = null;
    {
      final Property opp = property.getOtherEnd();
      String _switchResult = null;
      AggregationKind _aggregation = opp.getAggregation();
      int _value = _aggregation.getValue();
      switch (_value) {
        case AggregationKind.COMPOSITE:
          _switchResult = "*";
          break;
        case AggregationKind.SHARED:
          _switchResult = "o";
          break;
        default:
          String _xifexpression = null;
          boolean _isNavigable = opp.isNavigable();
          boolean _not = (!_isNavigable);
          if (_not) {
            _xifexpression = ">";
          } else {
            _xifexpression = "";
          }
          _switchResult = _xifexpression;
          break;
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
  
  public CharSequence cardinality(final Property p) {
    CharSequence _xifexpression = null;
    boolean _and = false;
    int _lowerBound = p.lowerBound();
    boolean _equals = (_lowerBound == 0);
    if (!_equals) {
      _and = false;
    } else {
      int _upperBound = p.upperBound();
      boolean _equals_1 = (_upperBound == (-1));
      _and = _equals_1;
    }
    if (_and) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(" ");
      _builder.append("*");
      _xifexpression = _builder;
    } else {
      CharSequence _xifexpression_1 = null;
      boolean _and_1 = false;
      int _lowerBound_1 = p.lowerBound();
      boolean _equals_2 = (_lowerBound_1 == 1);
      if (!_equals_2) {
        _and_1 = false;
      } else {
        int _upperBound_1 = p.upperBound();
        boolean _equals_3 = (_upperBound_1 == (-1));
        _and_1 = _equals_3;
      }
      if (_and_1) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append(" ");
        _builder_1.append("1..*");
        _xifexpression_1 = _builder_1;
      } else {
        CharSequence _xifexpression_2 = null;
        int _lowerBound_2 = p.lowerBound();
        int _upperBound_2 = p.upperBound();
        boolean _equals_4 = (_lowerBound_2 == _upperBound_2);
        if (_equals_4) {
          StringConcatenation _builder_2 = new StringConcatenation();
          int _lowerBound_3 = p.lowerBound();
          _builder_2.append(_lowerBound_3, "");
          _xifexpression_2 = _builder_2;
        } else {
          StringConcatenation _builder_3 = new StringConcatenation();
          _builder_3.append(" ");
          int _lowerBound_4 = p.lowerBound();
          _builder_3.append(_lowerBound_4, " ");
          _builder_3.append("..");
          int _upperBound_3 = p.upperBound();
          _builder_3.append(_upperBound_3, " ");
          _xifexpression_2 = _builder_3;
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public CharSequence defineGeneralization(final Generalization generalization) {
    StringConcatenation _builder = new StringConcatenation();
    Classifier _general = generalization.getGeneral();
    String _name = _general.getName();
    _builder.append(_name, "");
    _builder.append(" <|-- ");
    Classifier _specific = generalization.getSpecific();
    String _name_1 = _specific.getName();
    _builder.append(_name_1, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence defineRealization(final InterfaceRealization i) {
    StringConcatenation _builder = new StringConcatenation();
    Interface _contract = i.getContract();
    String _name = _contract.getName();
    _builder.append(_name, "");
    _builder.append(" <|-- ");
    BehavioredClassifier _implementingClassifier = i.getImplementingClassifier();
    String _name_1 = _implementingClassifier.getName();
    _builder.append(_name_1, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence definePackage(final org.eclipse.uml2.uml.Package p) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    String _name = p.getName();
    _builder.append(_name, "");
    return _builder;
  }
  
  public CharSequence defineEnum(final Enumeration e) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("enum ");
    String _name = e.getName();
    _builder.append(_name, "");
    return _builder;
  }
  
  public CharSequence defineDataType(final DataType d) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("class ");
    String _name = d.getName();
    _builder.append(_name, "");
    _builder.append(" << (D,blue) >>");
    return _builder;
  }
  
  public CharSequence defineSignal(final Signal sig) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("class ");
    String _name = sig.getName();
    _builder.append(_name, "");
    _builder.append(" << (S,red) >>");
    return _builder;
  }
  
  public CharSequence defineInterface(final Interface i) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("interface ");
    String _name = i.getName();
    _builder.append(_name, "");
    EList<EObject> _stereotypeApplications = i.getStereotypeApplications();
    for (final EObject st : _stereotypeApplications) {
      this.getStereotypeName(st);
    }
    return _builder;
  }
  
  public CharSequence defineClass(final org.eclipse.uml2.uml.Class clazz) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isAbstract = clazz.isAbstract();
      if (_isAbstract) {
        _builder.append("abstract ");
      }
    }
    _builder.append("class ");
    String _name = clazz.getName();
    _builder.append(_name, "");
    CharSequence _templateName = this.getTemplateName(clazz);
    _builder.append(_templateName, "");
    EList<EObject> _stereotypeApplications = clazz.getStereotypeApplications();
    for (final EObject st : _stereotypeApplications) {
      this.getStereotypeName(st);
    }
    return _builder;
  }
  
  public CharSequence getTemplateName(final org.eclipse.uml2.uml.Class c) {
    CharSequence _xifexpression = null;
    boolean _isTemplate = c.isTemplate();
    if (_isTemplate) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<");
      {
        TemplateSignature _ownedTemplateSignature = c.getOwnedTemplateSignature();
        EList<TemplateParameter> _parameters = _ownedTemplateSignature.getParameters();
        boolean _hasElements = false;
        for(final TemplateParameter p : _parameters) {
          if (!_hasElements) {
            _hasElements = true;
          } else {
            _builder.appendImmediate(", ", "");
          }
          CharSequence _templateParDef = this.templateParDef(p);
          _builder.append(_templateParDef, "");
        }
      }
      _builder.append(">");
      _xifexpression = _builder;
    }
    return _xifexpression;
  }
  
  protected CharSequence _templateParDef(final TemplateParameter p) {
    ParameterableElement _parameteredElement = p.getParameteredElement();
    return ((NamedElement) _parameteredElement).getName();
  }
  
  protected CharSequence _templateParDef(final ConnectableElementTemplateParameter p) {
    CharSequence _xblockexpression = null;
    {
      ParameterableElement _parameteredElement = p.getParameteredElement();
      final ConnectableElement conEl = ((ConnectableElement) _parameteredElement);
      StringConcatenation _builder = new StringConcatenation();
      String _name = conEl.getName();
      _builder.append(_name, "");
      _builder.append(": ");
      Type _type = conEl.getType();
      String _name_1 = _type.getName();
      _builder.append(_name_1, "");
      {
        ParameterableElement _default = p.getDefault();
        boolean _notEquals = (!Objects.equal(_default, null));
        if (_notEquals) {
          _builder.append(" = ");
          ParameterableElement _default_1 = p.getDefault();
          String _stringValue = ((ValueSpecification) _default_1).stringValue();
          _builder.append(_stringValue, "");
        }
      }
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  protected CharSequence _templateParDef(final ClassifierTemplateParameter p) {
    StringConcatenation _builder = new StringConcatenation();
    ParameterableElement _parameteredElement = p.getParameteredElement();
    String _name = ((NamedElement) _parameteredElement).getName();
    _builder.append(_name, "");
    {
      EList<Classifier> _constrainingClassifiers = p.getConstrainingClassifiers();
      boolean _isEmpty = _constrainingClassifiers.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder.append(" extends ");
        EList<Classifier> _constrainingClassifiers_1 = p.getConstrainingClassifiers();
        Classifier _head = IterableExtensions.<Classifier>head(_constrainingClassifiers_1);
        String _name_1 = _head.getName();
        _builder.append(_name_1, "");
      }
    }
    return _builder;
  }
  
  public CharSequence getStereotypeName(final EObject object) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<<");
    String _name = ((NamedElement) object).getName();
    _builder.append(_name, "");
    _builder.append(">>");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence declaration(final EObject a) {
    if (a instanceof InterfaceRealization) {
      return _declaration((InterfaceRealization)a);
    } else if (a instanceof Association) {
      return _declaration((Association)a);
    } else if (a instanceof Operation) {
      return _declaration((Operation)a);
    } else if (a instanceof Property) {
      return _declaration((Property)a);
    } else if (a instanceof EnumerationLiteral) {
      return _declaration((EnumerationLiteral)a);
    } else if (a instanceof Connector) {
      return _declaration((Connector)a);
    } else if (a instanceof Generalization) {
      return _declaration((Generalization)a);
    } else if (a != null) {
      return _declaration(a);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(a).toString());
    }
  }
  
  public CharSequence declaration(final EObject c, final Shape s) {
    if (c instanceof org.eclipse.uml2.uml.Class) {
      return _declaration((org.eclipse.uml2.uml.Class)c, s);
    } else if (c instanceof Enumeration) {
      return _declaration((Enumeration)c, s);
    } else if (c instanceof DataType) {
      return _declaration((DataType)c, s);
    } else if (c instanceof InformationItem) {
      return _declaration((InformationItem)c, s);
    } else if (c instanceof Interface) {
      return _declaration((Interface)c, s);
    } else if (c instanceof Signal) {
      return _declaration((Signal)c, s);
    } else if (c instanceof DurationObservation) {
      return _declaration((DurationObservation)c, s);
    } else if (c instanceof InstanceSpecification) {
      return _declaration((InstanceSpecification)c, s);
    } else if (c instanceof org.eclipse.uml2.uml.Package) {
      return _declaration((org.eclipse.uml2.uml.Package)c, s);
    } else if (c instanceof Comment) {
      return _declaration((Comment)c, s);
    } else if (c != null) {
      return _declaration(c, s);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(c, s).toString());
    }
  }
  
  public CharSequence templateParDef(final TemplateParameter p) {
    if (p instanceof ClassifierTemplateParameter) {
      return _templateParDef((ClassifierTemplateParameter)p);
    } else if (p instanceof ConnectableElementTemplateParameter) {
      return _templateParDef((ConnectableElementTemplateParameter)p);
    } else if (p != null) {
      return _templateParDef(p);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(p).toString());
    }
  }
}
