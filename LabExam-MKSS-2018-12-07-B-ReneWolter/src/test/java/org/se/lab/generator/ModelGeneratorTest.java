package org.se.lab.generator;

import org.junit.Assert;
import org.junit.Test;
import org.se.lab.business.UserService;
import org.se.lab.data.User;
import org.se.lab.metamodel.MPackage;

import java.util.List;

public class ModelGeneratorTest {


    @Test
    public void testGenerate()
    {
        ModelGenerator generator = new ModelGenerator();
        MPackage mPackage = generator.createModel(UserService.class);



        // TODO: test package

    }
}
