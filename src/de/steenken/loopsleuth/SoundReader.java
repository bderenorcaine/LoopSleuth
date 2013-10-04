package de.steenken.loopsleuth;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class SoundReader {

	private static void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException,                                                                                                LineUnavailableException
	{
	  byte[] data = new byte[4096];
	  SourceDataLine line = getLine(targetFormat); 
	  if (line != null)
	  {
	    // Start
	    line.start();
	    int nBytesRead = 0, nBytesWritten = 0;
	    while (nBytesRead != -1)
	    {
	        nBytesRead = din.read(data, 0, data.length);
	        if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
	    }
	    // Stop
	    line.drain();
	    line.stop();
	    line.close();
	    din.close();
	  } 
	}

	private static SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
	{
	  SourceDataLine res = null;
	  DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
	  res = (SourceDataLine) AudioSystem.getLine(info);
	  res.open(audioFormat);
	  return res;
	} 

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {

			File inputFile = new File("res/fightofthecentury.mp3");
			AudioInputStream inStream = AudioSystem.getAudioInputStream(inputFile);
		    AudioInputStream decodedInStream = null;
		    AudioFormat audioFormat = inStream.getFormat();
		    AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 
		                                                                                  audioFormat.getSampleRate(),
		                                                                                  16,
		                                                                                  audioFormat.getChannels(),
		                                                                                  audioFormat.getChannels() * 2,
		                                                                                  audioFormat.getSampleRate(),
		                                                                                  false);
		    decodedInStream = AudioSystem.getAudioInputStream(decodedFormat, inStream);
			
			System.out.println("Base File Format: " + audioFormat);

			System.out.println("Channels: "+audioFormat.getChannels());
            System.out.println("Encoding: "+audioFormat.getEncoding());
            System.out.println("Frame Rate: "+audioFormat.getFrameRate());
            System.out.println("Frame Size: "+audioFormat.getFrameSize());
            System.out.println("Sample Rate: "+audioFormat.getSampleRate());
            System.out.println("Sample size (bits): "+audioFormat.getSampleSizeInBits());
            System.out.println("Big endian: "+audioFormat.isBigEndian());
            System.out.println("Audio Format String: "+audioFormat.toString());
            
            // Play now. 
		    rawplay(decodedFormat, decodedInStream);
		    inStream.close();
			
		} catch (Exception e) {
			System.err.println("Exception \"" + e.toString() + "\" occurred.");
			e.printStackTrace(System.err);
		}
	}
}
