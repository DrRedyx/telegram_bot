package pro.sky.telegrambot.interfaceee;

import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.telegrambot.model.NotificationTask;

import java.text.ParseException;

public interface NotificationTaskService {
    NotificationTask parseMessage(Long id, String message) throws ParseException;

    SendMessage createMessage(Long id, String message);

    void send(SendMessage sendMessage);

    void save(NotificationTask objectMessage);
}
