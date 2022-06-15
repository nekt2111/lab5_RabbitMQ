package kpi.lab.lab5;

import org.telegram.telegrambots.meta.api.objects.InputFile;

import javax.naming.OperationNotSupportedException;
import javax.sound.sampled.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public final class AudioHelper {
    private static HashMap<String, InputStream> audios;
    private static HashMap<String, Long> audioLengths;
    private static AudioFormat audioFormat;

    private static boolean isInitialized;

    public static void Initialize() throws OperationNotSupportedException, UnsupportedAudioFileException, IOException {
        LoadAudios();

        isInitialized = true;
    }

    public static InputFile GetAudio(String text) throws Exception {
        if (!isInitialized) {
            throw new Exception();
        }

        String[] letters = SplitText(text);

        InputStream[] toCombine = new InputStream[letters.length];

        Long[] toCombineLengths = new Long[letters.length];

        for (int i = 0; i < letters.length; i++) {
            toCombine[i] = audios.get(letters[i]);
            toCombineLengths[i] = audioLengths.get(letters[i]);
            System.out.println(Arrays.toString(toCombine));
            System.out.println(Arrays.toString(toCombineLengths));
        }

        AudioInputStream stream = Combine(toCombine, toCombineLengths);
        File file = new File("audio.wav");

        try {
            AudioSystem.write(stream,
                    AudioFileFormat.Type.WAVE,
                    file);
        } catch (Exception ex) {
           ex.printStackTrace();
        }

        return new InputFile(file);
    }

    private static String[] SplitText(String text) {
        return ("_" + text + "_")
                .toLowerCase()
                .replaceAll(" ", "_")
                .replaceAll("ю", "у")
                .replaceAll("я", "а")
                //.replaceAll("н", "нн")
                //.replaceAll("у", "ууу")
                //.replaceAll("т", "тт")
                //.replaceAll("л", "лл")
                //.replaceAll("е", "ее")
                .replaceAll("э", "е")
                .replaceAll("ё", "о")
                .replaceAll("ы", "и")
                .replaceAll("й", "и")
                .replaceAll("ь", "")
                .replaceAll("\\.", "")
                .replaceAll("!", "")
                .replaceAll("\\?", "")
                .replaceAll("-", "")
                .replaceAll(",", "")
                .split("");
    }

    private static void LoadAudio(String folder, String letter) throws OperationNotSupportedException, UnsupportedAudioFileException, IOException {
        if (audios == null || audioLengths == null) {
            throw new OperationNotSupportedException();
        }
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(folder + letter + ".wav"));

        audios.put(letter, new ByteArrayInputStream(audioStream.readAllBytes()));
        audioLengths.put(letter, audioStream.getFrameLength());
        if (audioFormat == null) {
            audioFormat = audioStream.getFormat();
        }
    }

    private static AudioInputStream Combine(InputStream[] streams, Long[] lengths) throws IOException {
        long length = 0;

        Vector<InputStream> streamVector = new Vector<>(streams.length);

        for (int i = 0; i < streams.length; i++) {
            System.out.println(lengths.length);
            System.out.println(lengths[i]);
            length += lengths[i];
            streamVector.add(new ByteArrayInputStream(streams[i].readAllBytes()));
            streams[i].reset();
        }

        return new AudioInputStream(
                new SequenceInputStream(streamVector.elements()),
                audioFormat,
                length);
    }

    private static void LoadAudios() throws OperationNotSupportedException, UnsupportedAudioFileException, IOException {
        if (audios != null || audioLengths != null) {
            System.out.println("we have it already");
        }
        audios = new HashMap<>();
        audioLengths = new HashMap<>();

        String folder = "/audio/";

        LoadAudio(folder, "_");
        LoadAudio(folder, "а");
        LoadAudio(folder, "б");
        LoadAudio(folder, "в");
        LoadAudio(folder, "г");
        LoadAudio(folder, "д");
        LoadAudio(folder, "е");
        LoadAudio(folder, "ж");
        LoadAudio(folder, "з");
        LoadAudio(folder, "и");
        LoadAudio(folder, "к");
        LoadAudio(folder, "л");
        LoadAudio(folder, "м");
        LoadAudio(folder, "н");
        LoadAudio(folder, "о");
        LoadAudio(folder, "п");
        LoadAudio(folder, "р");
        LoadAudio(folder, "с");
        LoadAudio(folder, "т");
        LoadAudio(folder, "у");
        LoadAudio(folder, "ф");
        LoadAudio(folder, "х");
        LoadAudio(folder, "ц");
        LoadAudio(folder, "ч");
        LoadAudio(folder, "ш");
        LoadAudio(folder, "щ");
    }
}
