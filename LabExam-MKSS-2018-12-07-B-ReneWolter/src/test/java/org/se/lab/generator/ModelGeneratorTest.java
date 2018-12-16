package org.se.lab.generator;

import org.junit.Assert;
import org.junit.Test;
import org.se.lab.business.UserService;
import org.se.lab.data.User;
import org.se.lab.metamodel.*;

import java.util.Comparator;
import java.util.List;

public class ModelGeneratorTest {


    @Test
    public void testGenerateModel()
    {
        ModelGenerator generator = new ModelGenerator();
        MPackage mPackage = generator.createModel(UserService.class);

        // reflection can't get generic data types as simple name
        // ->   "java.util.List<org.se.lab.data.User>" instead of "List<User>"
        // reflection can't read the imports in a file

        // package check
        Assert.assertEquals("org.se.lab.business", mPackage.getName());
        Assert.assertEquals(1, mPackage.getInterfaces().size());

        // interface check
        MInterface mInterface = mPackage.getInterfaces().get(0);
        Assert.assertEquals("UserService", mInterface.getName());
        Assert.assertEquals(1, mInterface.getModifiers().size());
        Assert.assertEquals("public", mInterface.getModifiers().get(0).getName());
        Assert.assertEquals(3, mInterface.getOperations().size());

        // operations check
        MOperation mOpAdd = mInterface.getOperations().get(0);
        Assert.assertEquals("addUser", mOpAdd.getName());
        Assert.assertEquals("void", mOpAdd.getType().getName());

        MOperation mOpRemove = mInterface.getOperations().get(1);
        Assert.assertEquals("removeUser", mOpRemove.getName());
        Assert.assertEquals("void", mOpRemove.getType().getName());

        MOperation mOpFindAll = mInterface.getOperations().get(2);
        Assert.assertEquals("findAllUsers", mOpFindAll.getName());
        Assert.assertEquals("java.util.List<org.se.lab.data.User>", mOpFindAll.getType().getName());

        // parameters check
        Assert.assertEquals(4, mOpAdd.getParameters().size());
        for (MParameter parameter : mOpAdd.getParameters()){
            Assert.assertEquals("String", parameter.getType().getName());
        }
        // @note: had to recompile UserService.java with compiler-flag "-parameters" (see pom.xml, line 19)
        // for java reflection to be able to get the correct parameter names
        Assert.assertEquals("firstName", mOpAdd.getParameters().get(0).getName());
        Assert.assertEquals("lastName", mOpAdd.getParameters().get(1).getName());
        Assert.assertEquals("username", mOpAdd.getParameters().get(2).getName());
        Assert.assertEquals("password", mOpAdd.getParameters().get(3).getName());

        Assert.assertEquals(1, mOpRemove.getParameters().size());
        MParameter parameter = mOpRemove.getParameters().get(0);
        Assert.assertEquals("String", parameter.getType().getName());
        Assert.assertEquals("idString", parameter.getName());

        Assert.assertEquals(0, mOpFindAll.getParameters().size());

        System.out.println(mPackage);
    }
}