package org.se.lab;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserServiceConnectorTest 
{
	private static final JdbcTestHelper JDBC_HELPER = new JdbcTestHelper();
	
	private UserServiceConnector service;
	
	@Before
	public void init()
	{
		JDBC_HELPER.executeSqlScript("src/test/resources/sql/createUserTable.sql");
		JDBC_HELPER.executeSqlScript("src/test/resources/sql/insertUserTable.sql");
	
		service = new UserServiceConnectorJSON();
	}
	
	@After
	public void destroy()
	{
		JDBC_HELPER.executeSqlScript("src/test/resources/sql/dropUserTable.sql");		
	}

	@Test
	public void testInsert()
	{
		// test case updated!!!
		System.out.println("starting testInsert()");
		int countBeforeInsert = service.findAll().size();

	    User user = new User(0, "maggie", "AZ2wv9X4WVHLRuRFLpZChYwAQVU=");
	    service.insert(user);

	    int countAfterInsert = service.findAll().size();
	    // assert exactly one more entry in database
		Assert.assertEquals(countBeforeInsert + 1, countAfterInsert);

		// assert last element has the correct values
		User foundUser = service.findById(countAfterInsert);
		Assert.assertNotNull(foundUser);
		Assert.assertEquals("maggie", foundUser.getUsername());
		Assert.assertEquals("AZ2wv9X4WVHLRuRFLpZChYwAQVU=", foundUser.getPassword());
		System.out.println("testInsert() successful");
	}

	@Test
	public void testUpdate() throws IOException, JAXBException
	{
		// test case updated!!!
		System.out.println("starting testUpdate()");

		User foundUser = service.findById(1);
		Assert.assertNotNull(foundUser);
		Assert.assertNotEquals("newHomer", foundUser.getUsername());
		Assert.assertNotEquals("newPassword", foundUser.getPassword());

		// homer gets a new name and a new password
	    User user = new User(1, "newHomer", "newPassword");
	    service.update(user);

		foundUser = service.findById(1);
		Assert.assertNotNull(foundUser);
		Assert.assertEquals("newHomer", foundUser.getUsername());
		Assert.assertEquals("newPassword", foundUser.getPassword());
		System.out.println("testUpdate() successful");
	}

	@Test
	public void testDelete()
	{
		// test case updated!!!
		System.out.println("starting testDelete()");
		Assert.assertNotNull(service.findById(2));
		service.delete(2);
		Assert.assertNull(service.findById(2));
		System.out.println("testDelete() successful");
	}
	
	
	@Test
	public void testFindById() 
	{
	    User user = service.findById(3);
	    
	    Assert.assertNotNull(user);
	    Assert.assertEquals("bart", user.getUsername());
	    Assert.assertEquals("Ls4jKY8G2ftFdy/bHTgIaRjID0Q=", user.getPassword());
	}
	
	
	@Test
	public void testFindAll()
	{
	    List<User> users = service.findAll();
        
	    Assert.assertNotNull(users);
        Assert.assertEquals(4, users.size());
        System.out.println(users);
        Assert.assertEquals("homer", users.get(0).getUsername());
        Assert.assertEquals("marge", users.get(1).getUsername());
        Assert.assertEquals("bart", users.get(2).getUsername());
        Assert.assertEquals("lisa", users.get(3).getUsername());

	}
}
