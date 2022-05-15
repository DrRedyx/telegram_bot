package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.Collection;

public interface RepositoryNotificationTask extends JpaRepository<NotificationTask, Long> {
    Collection<NotificationTask> findAllBy();

    Collection<NotificationTask> findByChatId(long id);

    Collection<NotificationTask> getByDateSend(LocalDateTime localDateTime);

}
