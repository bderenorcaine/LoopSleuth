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

	private static void rawPlay(AudioFormat targetFormat, AudioInputStream din)
			throws IOException, LineUnavailableException {
		byte[] data = new byte[4096];
		SourceDataLine line = getLine(targetFormat);
		if (line != null) {
			// Start
			line.start();
			int nBytesRead = 0, nBytesWritten = 0;
			while (nBytesRead != -1) {
				nBytesRead = din.read(data, 0, data.length);
				if (nBytesRead != -1)
					nBytesWritten = line.write(data, 0, nBytesRead);
			}
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
	}

	private static void modifiedPlay(AudioFormat targetFormat,
			AudioInputStream din) throws IOException, LineUnavailableException {
		byte[] data = new byte[4096];
		SourceDataLine line = getLine(targetFormat);
		if (line != null) {
			// Start
			line.start();
			int nBytesRead = 0, nBytesWritten = 0;
			while (nBytesRead != -1) {
				nBytesRead = din.read(data, 0, data.length);
				if (nBytesRead != -1) {
					for (int i = 0; i < nBytesRead; i += 4)
					{
						data[i] *= 32;
						data[i + 2] *= 32;
					}
					nBytesWritten = line.write(data, 0, nBytesRead);
					System.out.println("Read and modified " + nBytesRead
							+ ", written " + nBytesWritten);
				}
			}
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
	}

	private static SourceDataLine getLine(AudioFormat audioFormat)
			throws LineUnavailableException {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

	private static String frameToString(byte[] frame) {
		String result = "[ ";
		for (int i = 0; i < frame.length; ++i)
			result += frame[i] + " ";
		result += "]";
		return result;
	}

	private static void analyzeWaveform(AudioInputStream stream) {
		AudioFormat format = stream.getFormat();
		System.out.println("This stream contains " + stream.getFrameLength()
				+ " frames, each " + format.getFrameSize()
				+ " bytes long. Per second, " + format.getFrameRate()
				+ " frames are used.");
//		byte[] frame = new byte[4];
//		int index = 0;
//		try {
//			while (stream.read(frame) > 0) {
//
//				System.out.println("Frame " + index++ + ": "
//						+ frameToString(frame));
//			}
//		} catch (IOException e) {
//			System.err.println("IO Exception caught: " + e.toString());
//		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			File inputFile = new File(
					"/home/dominik/GitWorkspace/LoopSleuth/res/fightofthecentury.mp3");
			AudioInputStream inStream = AudioSystem
					.getAudioInputStream(inputFile);
			AudioInputStream decodedInStream = null;
			AudioFormat audioFormat = inStream.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					audioFormat.getSampleRate(), 16, audioFormat.getChannels(),
					audioFormat.getChannels() * 2, audioFormat.getSampleRate(),
					false);
			decodedInStream = AudioSystem.getAudioInputStream(decodedFormat,
					inStream);
			analyzeWaveform(decodedInStream);

			System.out.println("Base File Format: " + audioFormat);

			System.out.println("Channels: " + audioFormat.getChannels());
			System.out.println("Encoding: " + audioFormat.getEncoding());
			System.out.println("Frame Rate: " + audioFormat.getFrameRate());
			System.out.println("Frame Size: " + audioFormat.getFrameSize());
			System.out.println("Sample Rate: " + audioFormat.getSampleRate());
			System.out.println("Sample size (bits): "
					+ audioFormat.getSampleSizeInBits());
			System.out.println("Big endian: " + audioFormat.isBigEndian());
			System.out
					.println("Audio Format String: " + audioFormat.toString());

			// Play now.
			modifiedPlay(decodedFormat, decodedInStream);
			inStream.close();

		} catch (Exception e) {
			System.err.println("Exception \"" + e.toString() + "\" occurred.");
			e.printStackTrace(System.err);
		}
	}
}
