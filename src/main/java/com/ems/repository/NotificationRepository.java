package com.ems.repository;

import com.ems.model.Notification;
import com.ems.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
    List<Notification> findByUserAndIsReadFalse(User user);
    List<Notification> findByIsSentFalse();
}
