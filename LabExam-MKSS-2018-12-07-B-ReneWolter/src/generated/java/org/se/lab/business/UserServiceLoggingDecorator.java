// Generated Class

package org.se.lab.business;

import java.util.List;

import org.apache.log4j.Logger;
import org.se.lab.data.User;

public class UserServiceLoggingDecorator
	extends UserServiceDecorator
{
	private final Logger LOG = Logger.getLogger(UserServiceImpl.class);
	
	public UserServiceLoggingDecorator(UserService service)
	{
		super(service);
	}
	
	/*
	 * Business methods
	 */
	
	@Override
    public void addUser(final String firstName, final String lastName, 
			final String username, final String password) 
	{
		LOG.debug("addUser(" + firstName + "," + lastName + "," + username + ")");
		super.addUser(firstName, lastName, username, password);
	}
    
    @Override
    public void removeUser(final String idString) 
	{
		LOG.debug("removeUser(" + idString + ")");
		super.removeUser(idString);
	}
    
    @Override
    public List<User> findAllUsers()
	{
		LOG.debug("findAllUsers()");
		List<User> list = super.findAllUsers();
		LOG.debug("    " + list);
		return list;
	}
}
