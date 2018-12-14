package org.se.lab.business;

import java.sql.Connection;
import java.util.List;

import org.se.lab.data.DAOException;
import org.se.lab.data.User;
import org.se.lab.data.UserDAO;


class UserServiceImpl // package private
	extends AbstractService 
	implements UserService
{
//	private final Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	/*
	 * Constructor injection
	 */
	
	public UserServiceImpl(Connection connection)
	{
		setConnection(connection);
	}
	
	
	/*
	 * Dependency: ---[1]-> UserDAO 
	 */
	private UserDAO userDAO;
	protected final UserDAO getUserDAO()
	{
		return userDAO;
	}
	public final void setUserDAO(final UserDAO userDAO)
	{
		if(userDAO == null)
			throw new IllegalArgumentException();
		
		this.userDAO = userDAO;
	}

	
	/*
	 * Business methods
	 */
	
    public void addUser(final String firstName, final String lastName, 
			final String username, final String password) 
	{
//		logger.debug("addUser(" + firstName + "," + lastName + "," + username + ")");
		
		String md5Password;
		try
		{
			md5Password = MD5Encoder.convertToMD5String(password);

			txBegin();
			getUserDAO().createUser(firstName,lastName, username, md5Password);
			txCommit();			
		}
		catch(DAOException e)
		{
			txRollback();
			throw new ServiceException("Can't add user " + username, e);
		}
		catch(Throwable e)
		{
			txRollback();
			throw new ServiceException("Unknown error during adding: " + username, e);			
		}
	}
	

    public void removeUser(final String idString) 
	{
//		logger.debug("removeUser(" + idString + ")");
		
		if(idString == null)
			throw new IllegalArgumentException();

		try
		{
			int id = Integer.valueOf(idString);
			
			txBegin();
			User u = getUserDAO().findById(id);
			getUserDAO().delete(u);
			txCommit();
		}
        catch(DAOException e)
        {
            txRollback();
            throw new ServiceException("Can't remove user with id = " + idString);
        }
		catch(Throwable e)
		{
			txRollback();
			throw new ServiceException("Unknown error during removing: " + idString);			
		}
	}


    public List<User> findAllUsers()
	{
//		logger.debug("findAllUsers()");
		
		List<User> users = null;
		try
		{
			txBegin();
			users = getUserDAO().findAll();
			txCommit();
//			logger.debug("    " + users);
		}
        catch(DAOException e)
        {
            txRollback();
            throw new ServiceException("Can't find all user ", e);
        }
		catch(Throwable e)
		{
			txRollback();
			throw new ServiceException("Unknown error during finding all users: ", e);			
		}
		return users;
	}
}
