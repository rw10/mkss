package org.se.lab.generator;

import org.se.lab.metamodel.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ModelGenerator {

    public MPackage createModel(Class<?> clazz){
        MPackage mPackage = createPackage(clazz);
        return mPackage;
    }

    private MPackage createPackage(Class<?> aClass){
        Package pkg = aClass.getPackage();
        String name = pkg.getName();
        MPackage mPackage = new MPackage(name);

        MInterface mInterface = createInterface(aClass);
        List<MInterface> interfaces = new ArrayList<>();
        interfaces.add(mInterface);
        mPackage.setInterfaces(interfaces);
        return mPackage;
    }

    private MInterface createInterface(Class<?> aClass){
        String name = aClass.getSimpleName();
        MInterface mInterface = new MInterface(name);

        // public modifier check
        int modifierMask = aClass.getModifiers();
        List<MTypeModifier> modifiers = new ArrayList<>();
        if (Modifier.isPublic(modifierMask))
            modifiers.add(new MTypeModifier("public"));
        mInterface.setModifiers(modifiers);

        List<MOperation> operations = new ArrayList<>();

        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            MOperation mOperation = createOperation(method);
            operations.add(mOperation);
        }
        // sort operations, so they can be tested properly
        // order may vary, if methods are detected with reflection
        Collections.sort(operations, this::compare);
        mInterface.setOperations(operations);
        return mInterface;
    }

    private MOperation createOperation(Method method){
        String name = method.getName();
        MType returnType = createType(method.getReturnType(), method.getGenericReturnType(), false);
        MOperation mOperation = new MOperation(name, returnType);

        List<MParameter> parameters = new ArrayList<>();

        Parameter[] params = method.getParameters();
        for (Parameter param : params) {
            MParameter mParameter = createParameter(param);
            parameters.add(mParameter);
        }
        mOperation.setParameters(parameters);
        return mOperation;
    }

    private MParameter createParameter(Parameter parameter){
        String name = parameter.getName();
        MType type = createType(parameter.getType(), parameter.getParameterizedType(), true);
        MParameter mParameter = new MParameter(name, type);
        return mParameter;
    }

    private MType createType(Class<?> clazz, Type type, boolean useModifiers) {
        String typeName = clazz.getName();
        String genericName = type.getTypeName();
        if (typeName.equals(genericName)) {
            // if no generic type, we can shorten the type
            typeName = clazz.getSimpleName();
        } else {
            typeName = genericName;
        }

        MType mType = new MType(typeName);
        if (useModifiers){
            int modifierMask = type.getClass().getModifiers();
            List<MTypeModifier> modifiers = createModifiers(modifierMask);
            mType.setModifiers(modifiers);
        }
        return mType;
    }


    private List<MTypeModifier> createModifiers(int modifierMask){
        List<MTypeModifier> modifiers = new ArrayList<>();
        if (Modifier.isFinal(modifierMask))
            modifiers.add(new MTypeModifier("final"));

        // java reflection also recognizes implicit modifiers which is not desired
        // therefore the modifier check was limited to final and parameter types only (-> no modifiers for return types)
        /*
        if (Modifier.isPublic(modifierMask))
            modifiers.add(new MTypeModifier("public"));
        if (Modifier.isProtected(modifierMask))
            modifiers.add(new MTypeModifier("protected"));
        if (Modifier.isPrivate(modifierMask))
            modifiers.add(new MTypeModifier("private"));
        if (Modifier.isStatic(modifierMask))
            modifiers.add(new MTypeModifier("static"));
        if (Modifier.isTransient(modifierMask))
            modifiers.add(new MTypeModifier("transient"));
        if (Modifier.isVolatile(modifierMask))
            modifiers.add(new MTypeModifier("volatile"));
         */

        return modifiers;
    }

    private int compare(MOperation o1, MOperation o2) {
        // sort by count of parameters
        int paramCount = o2.getParameters().size() - o1.getParameters().size();

        // sort alphabetically, if identical count
        return paramCount!=0? paramCount :
            o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
    }
}
