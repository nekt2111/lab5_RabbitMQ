package kpi.lab.lab5.Bot;

import kpi.lab.lab5.AudioHelper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.naming.OperationNotSupportedException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;


@Component
public class SenderBot extends TelegramLongPollingBot {
    private final String token = "5436751651:AAEjVvmWUOi8nhj4XdnWojYSxZY4ZVClzjo";
    private final String botName = "sender_rabbit_mq_bot";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public SenderBot() {
        try {
            AudioHelper.Initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText("ALLO!");
        try {
            rabbitTemplate.convertAndSend("queue1",update.getMessage());
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        try {
            AudioHelper.Initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Eto ya!");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
