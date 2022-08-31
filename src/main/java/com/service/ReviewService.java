package com.service;

import com.dao.DaoException;
import com.dao.ReviewDao;
import com.dao.impl.ReviewDaoImpl;
import com.entity.Review;
import com.validation.ReviewValidator;

import java.util.List;
import java.util.Optional;

public class ReviewService {
    private static final ReviewDao reviewDao = new ReviewDaoImpl();

    ReviewService() {
    }

    public boolean addReview(int userId, int activityId, String mark, String message)
            throws ServiceException {
        try {
            if (!ReviewValidator.getInstance().validateMark(mark) ||
            !ReviewValidator.getInstance().validateMessage(message)) {
                return false;
            }
            reviewDao.addReview(userId, activityId, Integer.parseInt(mark), message);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void deleteReview(int id) throws ServiceException {
        try {
            reviewDao.deleteReviewById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<Review> findReviewById(int id) throws ServiceException {
        try {
            return reviewDao.findReviewById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Review> findReviewByActivityId(int id) throws ServiceException {
        try {
            return reviewDao.findReviewsByActivityId(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean updateReview(int id, int mark, String message) throws ServiceException {
        try {
            if (!ReviewValidator.getInstance().validateMark(String.valueOf(mark)) ||
            !ReviewValidator.getInstance().validateMessage(message)) {
                return false;
            }
            reviewDao.updateReview(id, mark, message);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}