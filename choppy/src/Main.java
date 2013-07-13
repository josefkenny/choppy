import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;


public class Main {

	/**
	 * @param args	specify directory
	 */
	public static void main(String[] args) 
	{
		// for deployment
		//String directoryPath = args[0];
		//int millis = Integer.parseInt(args[1]);
		//int repeats = Integer.parseInt(args[2]);

		// for testing
		String directoryPath = "/Users/josefkenny/vaporgen/";
		int millis = 1;
		int repeats = 600; //2.5 minutes
		
		int playEach = 16;

		// make output directory
		File outputDir = new File(directoryPath + "output");

		// if the directory does not exist, create it
		if (!outputDir.exists())
		{
			boolean result = outputDir.mkdir();  
		}

		
		
		// make output file
		Random r = new Random();
		int newFileNumber = r.nextInt(99999);
		String newFileName = directoryPath + "/output/"+ Integer.toString( newFileNumber ) + ".wav";

		//44.1KHz PCM, 2 channels, 16 bit, signed, little endian
		AudioFormat theNewAudioFormat = new AudioFormat(44100, 16, 2, true, false);
		AudioFileFormat theNewAudioFileFormat = new AudioFileFormat(AudioFileFormat.Type.WAVE, theNewAudioFormat, AudioSystem.NOT_SPECIFIED);
		ChoppyEngine.createBlankAudioFile(newFileName, theNewAudioFileFormat);
		
		// make temp file
		String tempFileName = directoryPath + "/output/temp.wav";
		ChoppyEngine.createBlankAudioFile(tempFileName, theNewAudioFileFormat);

		for(int i = 0; i < repeats; i++)
		{
			String sourceFileName = ChoppyEngine.getRandomFileInDirectory(directoryPath);
			for(int j = 0; j < playEach; j++)
			{
				ChoppyEngine.copyAudio(sourceFileName, newFileName, tempFileName, millis);
			}
			millis = millis+(i);
			System.out.print(millis);
		}
	}


}
