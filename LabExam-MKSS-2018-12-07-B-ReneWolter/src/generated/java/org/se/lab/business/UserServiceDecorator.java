// Generated Class

package org.se.lab.business;

public abstract class UserServiceDecorator
	implements UserService
{
	/*
	 * Reference: service:UserService
	 */
	private UserService service;

	/*
	 * Constructor: UserServiceDecorator
	 */
	public UserServiceDecorator(UserService service)
	{
		if(service == null)
			throw new IllegalArgumentException("UserService is null!");
		this.service = service;
	}

	/*
	 * Operation: addUser
	 */
	@Override
	public void addUser(final String firstName, final String lastName, final String username, final String password)
	{
		service.addUser(firstName, lastName, username, password);
	}

	/*
	 * Operation: removeUser
	 */
	@Override
	public void removeUser(final String idString)
	{
		service.removeUser(idString);
	}

	/*
	 * Operation: findAllUsers
	 */
	@Override
	public java.util.List<org.se.lab.data.User> findAllUsers()
	{
		return service.findAllUsers();
	}
}
