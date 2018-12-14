package org.se.lab.generator;

import org.se.lab.metamodel.*;
import java.lang.reflect.*;
import java.util.ArrayList;
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
        String name = aClass.getName();
        MInterface mInterface = new MInterface(name);

        List<MOperation> operations = new ArrayList<>();

        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            MOperation mOperation = createOperation(method);
            operations.add(mOperation);
        }
        mInterface.setOperations(operations);
        return mInterface;
    }

    private MOperation createOperation(Method method){
        String name = method.getName();
        MType returnType = new MType(method.getReturnType().getName());
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
        MType type = new MType(parameter.getType().getName());
        MParameter mParameter = new MParameter(name, type);
        return mParameter;
    }
}
