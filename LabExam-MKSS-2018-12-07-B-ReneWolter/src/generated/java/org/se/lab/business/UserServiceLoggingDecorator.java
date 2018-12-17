// Generated Class

package org.se.lab.business;

import org.apache.log4j.Logger;
import org.apache.log4j.Logger;

public class UserServiceLoggingDecorator
	extends UserServiceDecorator
{
	/*
	 * Reference: LOG:Logger
	 */
	private final Logger LOG = Logger.getLogger(UserServiceImpl.class);

	/*
	 * Constructor: UserServiceLoggingDecorator
	 */
	public UserServiceLoggingDecorator(UserService service)
	{
		super(service);
	}

	/*
	 * Business methods
	 */

	/*
	 * Operation: addUser
	 */
	@Override
	public void addUser(final String firstName, final String lastName, final String username, final String password)
	{
		LOG.debug("addUser(" + firstName + "," + lastName + "," + username + ")");
		super.addUser(firstName, lastName, username, password);
	}

	/*
	 * Operation: removeUser
	 */
	@Override
	public void removeUser(final String idString)
	{
		LOG.debug("removeUser(" + idString + ")");
		super.removeUser(idString);
	}

	/*
	 * Operation: findAllUsers
	 */
	@Override
	public java.util.List<org.se.lab.data.User> findAllUsers()
	{
		LOG.debug("findAllUsers()");
		java.util.List<org.se.lab.data.User> obj = super.findAllUsers();
		LOG.debug("    " + obj);
		return obj;
	}
}
