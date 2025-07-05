import java.util.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

// BACKEND PART 
// this class handle the logic for our GUI
public class MorseCodeController {
    // We'll use a hashmap to translate user input morse code
    // a hashmap is a data structure that stores key/ value pairs
    // in this case, we'll use the letter as the key and the corresponding morse
    // code as value
    // this way, we can easily look up the morse code for any given letter by using
    // the letter as the key

    // Here I am declaring a hashmap to have a key of type "character" with a value
    // of type "string".
    private HashMap<Character, String> morseCodeMap;

    public MorseCodeController() {
        morseCodeMap = new HashMap<>();

        // uppercase
        morseCodeMap.put('A', ".-");
        morseCodeMap.put('B', "-...");
        morseCodeMap.put('C', "-.-.");
        morseCodeMap.put('D', "-..");
        morseCodeMap.put('E', ".");
        morseCodeMap.put('F', "..-.");
        morseCodeMap.put('G', "--.");
        morseCodeMap.put('H', "....");
        morseCodeMap.put('I', "..");
        morseCodeMap.put('J', ".---");
        morseCodeMap.put('K', "-.-");
        morseCodeMap.put('L', ".-..");
        morseCodeMap.put('M', "--");
        morseCodeMap.put('N', "-.");
        morseCodeMap.put('O', "---");
        morseCodeMap.put('P', ".--.");
        morseCodeMap.put('Q', "--.-");
        morseCodeMap.put('R', ".-.");
        morseCodeMap.put('S', "...");
        morseCodeMap.put('T', "-");
        morseCodeMap.put('U', "..-");
        morseCodeMap.put('V', "...-");
        morseCodeMap.put('W', ".--");
        morseCodeMap.put('X', "-..-");
        morseCodeMap.put('Y', "-.--");
        morseCodeMap.put('Z', "--..");

        // lowercase
        morseCodeMap.put('a', ".-");
        morseCodeMap.put('b', "-...");
        morseCodeMap.put('c', "-.-.");
        morseCodeMap.put('d', "-..");
        morseCodeMap.put('e', ".");
        morseCodeMap.put('f', "..-.");
        morseCodeMap.put('g', "--.");
        morseCodeMap.put('h', "....");
        morseCodeMap.put('i', "..");
        morseCodeMap.put('j', ".---");
        morseCodeMap.put('k', "-.-");
        morseCodeMap.put('l', ".-..");
        morseCodeMap.put('m', "--");
        morseCodeMap.put('n', "-.");
        morseCodeMap.put('o', "---");
        morseCodeMap.put('p', ".--.");
        morseCodeMap.put('q', "--.-");
        morseCodeMap.put('r', ".-.");
        morseCodeMap.put('s', "...");
        morseCodeMap.put('t', "-");
        morseCodeMap.put('u', "..-");
        morseCodeMap.put('v', "...-");
        morseCodeMap.put('w', ".--");
        morseCodeMap.put('x', "-..-");
        morseCodeMap.put('y', "-.--");
        morseCodeMap.put('z', "--..");

        // numbers
        morseCodeMap.put('0', "-----");
        morseCodeMap.put('1', ".----");
        morseCodeMap.put('2', "..---");
        morseCodeMap.put('3', "...--");
        morseCodeMap.put('4', "....-");
        morseCodeMap.put('5', ".....");
        morseCodeMap.put('6', "-....");
        morseCodeMap.put('7', "--...");
        morseCodeMap.put('8', "---..");
        morseCodeMap.put('9', "----.");

        // special characters
        morseCodeMap.put(' ', "/");
        morseCodeMap.put(',', "--..--");
        morseCodeMap.put('.', ".-.-.-");
        morseCodeMap.put('?', "..--..");
        morseCodeMap.put(';', "-.-.-.");
        morseCodeMap.put(':', "---...");
        morseCodeMap.put('(', "-.--.");
        morseCodeMap.put(')', "-.--.-");
        morseCodeMap.put('[', "-.--.");
        morseCodeMap.put(']', "-.--.-");
        morseCodeMap.put('{', "-.--.");
        morseCodeMap.put('}', "-.--.-");
        morseCodeMap.put('+', ".-.-.");
        morseCodeMap.put('-', "-....-");
        morseCodeMap.put('_', "..--.-");
        morseCodeMap.put('"', ".-..-.");
        morseCodeMap.put('\'', ".----.");
        morseCodeMap.put('/', "-..-.");
        morseCodeMap.put('\\', "-..-.");
        morseCodeMap.put('@', ".--.-.");
        morseCodeMap.put('=', "-...-");
        morseCodeMap.put('!', "-.-.--");
    }

    public String translateToMorse(String textToTranslate) {
        StringBuilder translatedText = new StringBuilder();
        for (Character letter : textToTranslate.toCharArray()) {
            translatedText.append(morseCodeMap.get(letter) + " ");
        }

        return translatedText.toString();
    }

    // morseMessage - contains the morse message after being translated
    public void playSound(String[] morseMessage) {

        // audio format specifies the audio properties(i.e. the type of sound we want)
        AudioFormat audioFormat = new AudioFormat(44100, 16, 1, true, false);

        // create the data line (to play incoming audio data)
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine sourceDataLine = null;
        try {
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
        } catch (Exception e) {
            e.printStackTrace();
            return; // Exit if audio setup fails
        }

        // duration of the sound to be played (I just messed around with the values to
        // get it close enough)
        int dotDuration = 200;
        int dashDuration = (int) (1.5 * dotDuration);
        int slashDuration = 2 * dashDuration;

        for (String pattern : morseMessage) {
            // play the letter sound
            for (char c : pattern.toCharArray()) {
                if (c == '.') {
                    playBeep(sourceDataLine, dotDuration);
                    try {
                        Thread.sleep(dotDuration);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (c == '-') {
                    playBeep(sourceDataLine, dashDuration);
                    try {
                        Thread.sleep(dotDuration);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (c == '/') {
                    playBeep(sourceDataLine, slashDuration);
                }
            }

            // waits a bit before playing the next sequence
            try {
                Thread.sleep(dotDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // close audio output line(cleans up resources)
        sourceDataLine.drain();
        sourceDataLine.stop();
        sourceDataLine.close();
    }

    // sends audio data to be played to the data line
    private void playBeep(SourceDataLine line, int duration) {
        // create audio data
        byte[] data = new byte[duration * 44100 / 1000];

        for (int i = 0; i < data.length; i++) {
            // calculates the angle of the sine wave for the current sample based on the
            // sample rate and frequency
            double angle = i / (44100.0 / 440) * 2 * Math.PI;

            // calculates the amplitude of the sine wave at the current angle and scale it
            // to fit within the range of
            // a signed byte (-128 to 127)
            // also in the context of audio processing, a signed bytes is often used to
            // represent audio data because it
            // can represent both positive and negative amplitudes of sound waves
            data[i] = (byte) (Math.sin(angle) * 127);
        }

        // write the audio dat in the data line to be played
        line.write(data, 0, data.length);
    }
}
