package com.controller;

import com.constant.PageAddress;
import com.constant.RequestParameter;
import com.pool.ConnectionPool;
import com.pool.PoolException;
import com.pool.ProxyConnection;
import com.validation.NumberValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "ImageServlet", urlPatterns = {"/image"})
@MultipartConfig(maxFileSize = 16177215) //16MB
public class ImageServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger();
    private static ConnectionPool pool = ConnectionPool.getInstance();
    private static final int IMAGE_NAME_MAX_LENTH = 160;
    private static final int IMAGE_MAX_SIZE = 16177215;
    private static final String FIND_IMAGE_BY_USER_ID = "SELECT image, image_name " +
            "FROM user WHERE user_id = ?";
    private static final String UPDATE_USER_IMAGE = "UPDATE user " +
            "SET image = ?, image_name = ? WHERE user_id = ?";
    private static final String DB_USER_IMAGE_FIELD = "image";
    private static final String DB_USER_IMAGE_NAME_FIELD = "image_name";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_LENGTH_HEADER = "Content-Length";
    private static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";
    private static final String CONTENT_DISPOSITION_HEADER_VALUE = "inline; filename=\"";
    private static final String BACK_SLASH = "\"";
    private static final String HEADER_SPLITTER = ";";
    private static final String EMPTY = "";
    private static final String FILENAME_PARAMETER = "filename";

    /**
     * Retrieves user's ID from request and returns an image of that user from the database
     *
     * @param request
     * @param response
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter(RequestParameter.USER_ID);
        if (NumberValidator.getInstance().validateNumber(userId)){
            ProxyConnection connection = null;
            PreparedStatement preparedStatement = null;
            try {
                connection = pool.takeConnection();
                byte[] imageData = {};
                String imageFileName = EMPTY;
                preparedStatement = connection.prepareStatement(FIND_IMAGE_BY_USER_ID);
                preparedStatement.setInt(1, Integer.parseInt(userId));
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    imageData = resultSet.getBytes(DB_USER_IMAGE_FIELD);
                    imageFileName = resultSet.getString(DB_USER_IMAGE_NAME_FIELD);
                }
                String contentType = this.getServletContext().getMimeType(imageFileName);
                response.setHeader(CONTENT_TYPE_HEADER, contentType);
                response.setHeader(CONTENT_LENGTH_HEADER, String.valueOf(imageData.length));
                response.setHeader(CONTENT_DISPOSITION_HEADER,
                        CONTENT_DISPOSITION_HEADER_VALUE + imageFileName + BACK_SLASH);
                response.getOutputStream().write(imageData);
            } catch (PoolException e) {
                logger.fatal(e.getMessage(), e);
                throw new RuntimeException(e);
            } catch (SQLException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            } finally {
                closeStatement(preparedStatement);
                try {
                    pool.releaseConnection(connection);
                } catch (PoolException e) {
                    logger.fatal("Can't release connection: " + e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter(RequestParameter.USER_ID));
        InputStream inputStream = null;
        Part filePart = request.getPart(RequestParameter.PHOTO);
        String fileName = getFileName(filePart);
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        if (filePart.getSize() == 0 || filePart.getSize() > IMAGE_MAX_SIZE) {
            logger.log(Level.ERROR, "File size is too big or empty");
            return;
        }
        logger.log(Level.INFO, filePart.getName());
        logger.log(Level.INFO, filePart.getSize());
        logger.log(Level.INFO, filePart.getContentType());
        inputStream = filePart.getInputStream();
        try {
            if (inputStream == null) {
                return;
            }
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_USER_IMAGE);
            preparedStatement.setBlob(1, inputStream);
            preparedStatement.setString(2, fileName);
            preparedStatement.setInt(3, userId);
            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                logger.log(Level.INFO, "File uploaded and saved into database");
            }
            response.sendRedirect(PageAddress.VIEW_USER_INFO);
        } catch (PoolException e) {
            logger.fatal(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
        } finally {
            closeStatement(preparedStatement);
            try {
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.fatal("Can't release connection: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }


    private String getFileName(Part part) {
        for (String content : part.getHeader(CONTENT_DISPOSITION_HEADER).split(HEADER_SPLITTER)) {
            if (content.trim().startsWith(FILENAME_PARAMETER)) {
                String result = content.substring(
                        content.indexOf('=') + 1).trim().replace(BACK_SLASH, EMPTY);
                if (result.length() > IMAGE_NAME_MAX_LENTH) {
                    result = result.substring(result.length() - IMAGE_NAME_MAX_LENTH);
                }
                return result;
            }
        }
        return EMPTY;
    }

    private void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Can't close statement: " + e.getMessage(), e);
            }
        }
    }
}