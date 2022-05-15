package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.interfaceee.NotificationTaskService;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.RepositoryNotificationTask;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@Service
public class NotificationTaskImpl implements NotificationTaskService {
    private final RepositoryNotificationTask repositoryNotificationTask;
    private final TelegramBot telegramBot;

    private final String str = "[0-9\\.\\:\\s]{16}";
    private final int MIN_DATA = 16;

    public NotificationTaskImpl(RepositoryNotificationTask repositoryNotificationTask, TelegramBot telegramBot) {
        this.repositoryNotificationTask = repositoryNotificationTask;
        this.telegramBot = telegramBot;
    }



    @Override
    public NotificationTask parseMessage(Long id, String message) throws ParseException {
        NotificationTask notificationTask = new NotificationTask();
        if (message.length() > 0) {
            if (message.length() > MIN_DATA) {
                String watcher = message.substring(0, 16);
                if (watcher.matches(str)) {
                    notificationTask.setLocalDateTime(format(watcher));
                }
            }
            notificationTask.setChatId(id);
            notificationTask.setMessage(message);
        } else {
            notificationTask = null;
        }
        return notificationTask;
    }

    @Override
    public SendMessage createMessage(Long id, String message) {
        SendMessage sendMessage = new SendMessage(id, message);
        return sendMessage;
    }

    @Override
    public void send(SendMessage sendMessage) {
        telegramBot.execute(sendMessage);
    }

    @Override
    public void save(NotificationTask notificationTask) {
        repositoryNotificationTask.save(notificationTask);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    private void runaway() {
        Collection<NotificationTask> notificationTaskCollection = read();
        if (notificationTaskCollection.size() > 0) {
            make(notificationTaskCollection);
        }
    }

    private Collection<NotificationTask> read() {
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        return repositoryNotificationTask.getByDateSend(localDateTime);
    }

    private void make(Collection<NotificationTask> notificationTaskCollection) {
        for (NotificationTask item : notificationTaskCollection) {
            send(createMessage(item.getChatId(), item.getMessage()));
        }
    }

    private LocalDateTime format(String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        return localDateTime;
    }
}
