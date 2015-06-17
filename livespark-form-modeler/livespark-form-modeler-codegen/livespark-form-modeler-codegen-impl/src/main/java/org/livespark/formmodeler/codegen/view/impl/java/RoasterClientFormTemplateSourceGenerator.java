package org.livespark.formmodeler.codegen.view.impl.java;

import static org.livespark.formmodeler.codegen.util.SourceGenerationUtil.ERRAI_BOUND;
import static org.livespark.formmodeler.codegen.util.SourceGenerationUtil.ERRAI_DATAFIELD;
import static org.livespark.formmodeler.codegen.util.SourceGenerationUtil.JAVA_LANG_OVERRIDE;
import static org.livespark.formmodeler.codegen.util.SourceGenerationUtil.READONLY_PARAM;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.livespark.formmodeler.codegen.FormJavaTemplateSourceGenerator;
import org.livespark.formmodeler.codegen.SourceGenerationContext;
import org.livespark.formmodeler.codegen.util.SourceGenerationUtil;
import org.livespark.formmodeler.model.FieldDefinition;


public abstract class RoasterClientFormTemplateSourceGenerator implements FormJavaTemplateSourceGenerator {

    @Inject
    private Instance<InputCreatorHelper> creatorInstances;

    private Map<String, InputCreatorHelper> creatorHelpers = new HashMap<String, InputCreatorHelper>(  );

    @PostConstruct
    protected void init() {
        for ( InputCreatorHelper helper : creatorInstances ) {
            creatorHelpers.put( helper.getSupportedFieldType(),
                                helper );
        }
    }

    @Override
    public String generateJavaTemplateSource( SourceGenerationContext context ) {
        JavaClassSource viewClass = Roaster.create( JavaClassSource.class );
        String packageName = getPackageName( context );

        addTypeSignature( context, viewClass, packageName );
        addImports( context, viewClass );
        addAnnotations( context, viewClass );

        addBaseViewFieldsAndMethodImpls( context, viewClass );

        addAdditional( context, viewClass );

        return viewClass.toString();
    }

    protected abstract void addAdditional( SourceGenerationContext context,
                                          JavaClassSource viewClass );

    protected void addBaseViewFieldsAndMethodImpls( SourceGenerationContext context,
                            JavaClassSource viewClass ) {
        StringBuffer inputNames = new StringBuffer(  );
        StringBuffer readOnlyMethodSrc = new StringBuffer(  );

        for ( FieldDefinition fieldDefinition : context.getFormDefinition().getFields() ) {
            
            if (fieldDefinition.isAnnotatedId() && !displaysId()) 
                continue;
            
            InputCreatorHelper helper = creatorHelpers.get( fieldDefinition.getCode() );
            if (helper == null) continue;

            PropertySource<JavaClassSource> property = viewClass.addProperty( getWidgetFromHelper( helper ), fieldDefinition.getName() );

            FieldSource<JavaClassSource> field = property.getField();
            field.setPrivate();

            initializeProperty( helper, field );

            field.addAnnotation( ERRAI_BOUND ).setStringValue( "property", fieldDefinition.getBindingExpression() );
            field.addAnnotation( ERRAI_DATAFIELD );

            property.removeAccessor();
            property.removeMutator();

            inputNames.append( "inputNames.add(\"" ).append( fieldDefinition.getName() ).append( "\");" );
            readOnlyMethodSrc.append( helper.getReadonlyMethod( fieldDefinition.getName(), READONLY_PARAM ) );
        }

        viewClass.addMethod()
                .setName( "initInputNames" )
                .setBody( inputNames.toString() )
                .setPublic()
                .setReturnTypeVoid()
                .addAnnotation( JAVA_LANG_OVERRIDE );


        if ( isEditable() ) {
            MethodSource<JavaClassSource> readonlyMethod = viewClass.addMethod()
                    .setName( "setReadOnly" )
                    .setBody( readOnlyMethodSrc.toString() )
                    .setPublic()
                    .setReturnTypeVoid();
            readonlyMethod.addParameter( boolean.class, SourceGenerationUtil.READONLY_PARAM );
            readonlyMethod.addAnnotation( JAVA_LANG_OVERRIDE );
        }
    }

    protected abstract void initializeProperty( InputCreatorHelper helper, FieldSource<JavaClassSource> field );

    protected abstract boolean isEditable();

    protected abstract String getWidgetFromHelper( InputCreatorHelper helper );

    protected abstract void addAnnotations( SourceGenerationContext context,
                            JavaClassSource viewClass );

    protected abstract void addImports( SourceGenerationContext context,
                            JavaClassSource viewClass );

    protected abstract void addTypeSignature( SourceGenerationContext context,
                                              JavaClassSource viewClass,
                                              String packageName );
    
    protected abstract boolean displaysId();

    private String getPackageName( SourceGenerationContext context ) {
        return context.getLocalPackage().getPackageName();
    }
}