// Generated class

package org.se.lab.business;

import java.util.List;

import org.se.lab.data.User;

public abstract class UserServiceDecorator
	implements UserService
{	
	/*
	 * Reference: service:UserService
	 */
	private UserService service;
	
	public UserServiceDecorator(UserService service)
	{
		if(service == null)
			throw new IllegalArgumentException("UserService is null!");
		this.service = service;
	}
	
	@Override
	public void addUser(String firstName, String lastName, String username,
			String password)
	{
		service.addUser(firstName, lastName, username, password);
	}

	@Override
	public void removeUser(String idString)
	{
		service.removeUser(idString);
	}

	@Override
	public List<User> findAllUsers()
	{
		return service.findAllUsers();
	}
}
