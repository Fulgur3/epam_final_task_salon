package com.dao;

import com.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDao extends AbstractDao<Review>{

    void addReview(int userId, int activityId, int mark, String message) throws DaoException;

    void updateReview(int reviewId, int mark, String message) throws DaoException;

    Optional<Review> findReviewById(int id) throws DaoException;

    List<Review> findReviewsByActivityId( int activityId) throws DaoException;

    void deleteReviewById(int id) throws DaoException;
}
