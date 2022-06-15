package kpi.lab.lab5.Bot;

import kpi.lab.lab5.AudioHelper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@Component
public class ConsumerBot extends TelegramLongPollingBot {

    private final String token = "5551635833:AAFTFXiHfIcFo_v821ztcoLOCNXIiQj03PI";
    private final String botName = "consumer_rabbit_mq_bot";

    private CountDownLatch latch = new CountDownLatch(1);

    public ConsumerBot() {
        try {
            AudioHelper.Initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void receiveMessageAndSentNewOne(Message message) throws InterruptedException {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("А он говорит:");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        makeMagicWork(message);

        getLatch().await(15000, TimeUnit.MILLISECONDS);

    }

    private void sendAudioMessage(Message messageFrom, InputFile inputFile){
        try {
            SendAudio send = new SendAudio();
            send.setChatId(messageFrom.getChatId().toString());
            send.setAudio(inputFile);
            execute(send);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeMagicWork(Message message) {
        String[] messageParts = message.getText().split(" ");

        if (messageParts.length > 1 && messageParts[0].equals("@Sv")) {
            String audioText = message.getText().substring("@Sv ".length());

            try {
                sendAudioMessage(message, AudioHelper.GetAudio(audioText));
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    @Override
    public void onUpdateReceived(Update update) {

    }
}
