package org.se.lab;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.json.Json;
import javax.json.JsonObject;

public class UserServiceConnectorJSON implements UserServiceConnector
{
    private final Logger LOG = Logger.getLogger(UserServiceConnectorJSON.class);
    private static final String WEB_APP_NAME = "/REST-EJB-UserService/v1";

    protected final Proxy PROXY;
    protected final String HOST;
    protected final String PORT;

    public UserServiceConnectorJSON()
    {
        LOG.info("UserServiceConnectorJSON()");

        Properties properties = new Properties();
        try
        {
            properties.load(this.getClass().getResourceAsStream("/rest.properties"));
            HOST = properties.getProperty("rest.host");
            PORT = properties.getProperty("rest.port");
            LOG.info("Connect to " + HOST + ":" + PORT);

            String proxyAddress = properties.getProperty("proxy.address");
            String proxyPort = properties.getProperty("proxy.port");
            if (proxyAddress != null && proxyPort != null)
            {
                System.out.println("Use proxy " + proxyAddress + ":" + proxyPort);
                int port = Integer.parseInt(proxyPort);
                PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, port));
            }
            else
            {
                PROXY = Proxy.NO_PROXY;
            }
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Can't setup remote connection!", e);
        }
    }


    @Override
    public void insert(User user)
    {
        LOG.info("insert(" + user + ")");

        try
        {
            URL url = new URL("http://" + HOST + ":" + PORT + WEB_APP_NAME + "/users");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection(PROXY);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            OutputStreamWriter w = new OutputStreamWriter(connection.getOutputStream());
            w.write(convertUser2Json(user));
            w.flush();
            w.close();

            // Response
            int httpResponseCode = connection.getResponseCode();
            if (httpResponseCode != 200 && httpResponseCode != 201)
                throw new ServiceException("Insert error: " + httpResponseCode);
        }
        catch (IOException e)
        {
            throw new ServiceException("Can't forward request to the service!", e);
        }
    }


    @Override
    public void update(User user)
    {
        LOG.info("update(" + user + ")");

        try
        {
            // Request
            URL url = new URL("http://" + HOST + ":" + PORT + WEB_APP_NAME + "/users/" + user.getId());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(PROXY);
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            String json = convertUser2Json(user);
            OutputStreamWriter w = new OutputStreamWriter(connection.getOutputStream());
            w.write(json);
            w.flush();
            w.close();

            // Response
            int httpResponseCode = connection.getResponseCode();
            if (httpResponseCode != 200 && httpResponseCode != 204)
                throw new ServiceException("Update error: " + httpResponseCode);
        }
        catch (IOException e)
        {
            throw new ServiceException("Can't forward request to the service!", e);
        }
    }


    @Override
    public void delete(int id)
    {
        LOG.info("delete(" + id + ")");
        try
        {
            // Request
            URL url = new URL("http://" + HOST + ":" + PORT + WEB_APP_NAME + "/users/" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(PROXY);
            connection.setRequestMethod("DELETE");

            // Response
            int httpResponseCode = connection.getResponseCode();
            if (httpResponseCode != 200 && httpResponseCode != 204)
                throw new ServiceException("Delete error: " + httpResponseCode);
        }
        catch (IOException e)
        {
            throw new ServiceException("Can't forward request to the service!", e);
        }
    }


    @Override
    public User findById(int id)
    {
        LOG.info("findById(" + id + ")");

        try
        {
            // Request
            URL url = new URL("http://" + HOST + ":" + PORT + WEB_APP_NAME + "/users/" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(PROXY);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Response
            int httpResponseCode = connection.getResponseCode();
            if (httpResponseCode != 200)
                return null;

            String json = readResponseContent(connection.getInputStream());
            User user = convertJson2User(json);
            return user;
        }
        catch (IOException e)
        {
            throw new ServiceException("Can't forward request to the service!", e);
        }
    }


    @Override
    public List<User> findAll()
    {
        LOG.info("findAll()");

        try
        {
            // Request
            URL url = new URL("http://" + HOST + ":" + PORT + WEB_APP_NAME + "/users");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(PROXY);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Response
            int httpResponseCode = connection.getResponseCode();
            if (httpResponseCode != 200)
                return null;

            String json = readResponseContent(connection.getInputStream());
            List<User> userList = convertJsonToUserList(json);
            return userList;
        }
        catch (IOException e)
        {
            throw new ServiceException("Can't forward request to the service!", e);
        }
    }


    /*
     * JSON Serialization
     */
    protected User convertJson2User(String json)
    {
        try
        {
            JSONObject obj = new JSONObject(json);
            int id = obj.getInt("id");
            String username = obj.getString("username");
            String password = obj.getString("password");

            User user = new User(id, username, password);
            return user;
        }
        catch (JSONException e)
        {
            throw new ServiceException("Can't convert JSON to User object!", e);
        }
    }

    protected String convertUser2Json(User user)
    {
        JsonObject json =
                Json.createObjectBuilder()
                        .add("id", user.getId())
                        .add("username", user.getUsername())
                        .add("password", user.getPassword())
                        .build();
        return json.toString();
    }

    protected List<User> convertJsonToUserList(String json){
        try {
            JSONArray jsonUserArray = new JSONArray(json);
            List<User> userList = new ArrayList<>();
            for (Object object : jsonUserArray) {
                JSONObject jsonObject = (JSONObject) object;
                User user = convertJson2User(jsonObject.toString());
                userList.add(user);
            }

            return userList;
        }
        catch (JSONException e)
        {
            throw new ServiceException("Can't convert JSON to List of User objects!", e);
        }
    }


    /*
     * Utility methods
     */

    protected String readResponseContent(InputStream in) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        StringBuffer content = new StringBuffer();
        while ((line = reader.readLine()) != null)
        {
            content.append(line).append("\n");
        }
        return content.toString();
    }
}
