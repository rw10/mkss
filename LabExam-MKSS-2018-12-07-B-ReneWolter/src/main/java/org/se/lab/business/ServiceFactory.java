package org.se.lab.business;

import java.sql.Connection;

public interface ServiceFactory
{
	UserService createUserService(Connection connection);
	UserService createUserService();
}
