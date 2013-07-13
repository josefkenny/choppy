import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.*;

class ChoppyEngine 
{

	/**
	 * @param newFileName		the name of the new file to create
	 * @param audioFormat		the audio format to use
	 */
	public static void createBlankAudioFile( String newFileName, AudioFileFormat audioFormat )
	{
		try 
		{
			byte[] bytes = {127, 127, 127};
			InputStream byteStream = new ByteArrayInputStream(bytes);
			
			DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(newFileName));
			
			outputStream.write(0);
			
			
			AudioInputStream stream = new AudioInputStream(byteStream, audioFormat.getFormat(), bytes.length);
			
			File file = new File(newFileName);
			
	
			
	        AudioSystem.write(stream, AudioFileFormat.Type.WAVE, file);

		} 
		catch (Exception e) //catch exceptions
		{
			e.printStackTrace();

		} 

	}

	/**
	 * @param sourceFileName		the file name to copy data randomly from
	 * @param destinationFileName	the file name to append data to
	 * @param millisecondsToCopy	the number of milliseconds of audio data to copy
	 */
	public static void copyAudio(String sourceFileName, String destinationFileName, String tempFileName, int millisecondsToCopy) 
	{
		AudioInputStream inputStream = null;
		AudioInputStream choppedStream = null;

		AudioInputStream outputStream = null;

		try 
		{
			File sourceFile = new File(sourceFileName);
			File destinationFile = new File(destinationFileName);
			
			File tempFile = new File(tempFileName);
			
			//Thread.sleep(millisecondsToCopy);
			
			AudioFileFormat sourceFileFormat = AudioSystem.getAudioFileFormat(sourceFile);
			AudioFormat format = sourceFileFormat.getFormat();

			// init source and destination streams
			inputStream = AudioSystem.getAudioInputStream(sourceFile);
			outputStream = AudioSystem.getAudioInputStream(destinationFile);

			int bytesPerSecond = format.getFrameSize() * (int)format.getFrameRate();	
			int bytesPerMillisecond = bytesPerSecond / 1000;

			int millisecondsInFile = (int) (sourceFile.length() / bytesPerMillisecond);

			// skip ahead a random number of bytes			
			Random r = new Random();
			int startMillisecond = millisecondsInFile+1;
			while( startMillisecond + millisecondsToCopy > millisecondsInFile)
			{
				startMillisecond = r.nextInt(millisecondsInFile);
			}

			// do the skip
			inputStream.skip(startMillisecond * bytesPerMillisecond);

			long framesOfAudioToCopy = millisecondsToCopy * (int)( format.getFrameRate() / 1000);

			choppedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
			
			//write temp file being assembled
			AudioSystem.write(outputStream, sourceFileFormat.getType(), tempFile);
			outputStream = AudioSystem.getAudioInputStream(tempFile);

			AudioInputStream streamToWrite = new AudioInputStream( new SequenceInputStream(outputStream, choppedStream), outputStream.getFormat(), (outputStream.getFrameLength() + choppedStream.getFrameLength()) );

			System.out.println("File size is now: " + outputStream.getFrameLength() );
			
			AudioSystem.write(streamToWrite, sourceFileFormat.getType(), destinationFile);


		} 
		catch (Exception e) //catch exceptions
		{
			e.printStackTrace();

		} 
		finally 
		{
			if (inputStream != null) 
				try 
			{ 
					inputStream.close(); 
			} 
			catch (Exception e) 
			{
				e.printStackTrace(); 
			}

			if (outputStream != null) 
				try 
			{ 
					outputStream.close(); 
			} 
			catch (Exception e) 
			{
				e.printStackTrace(); 
			}


			if (choppedStream != null) 
				try 
			{ 
					choppedStream.close(); 
			} 
			catch (Exception e) 
			{ 
				e.printStackTrace(); 
			}

		}
	}

	public static String getRandomFileInDirectory(String directory)
	{
		File folder = new File(directory);

		File[] filesInFolder = folder.listFiles();

		ArrayList<File> audioFiles = new ArrayList<File>();

		// sort which files are audio files
		for (int i = 0; i < filesInFolder.length; i++) 
		{
			if (filesInFolder[i].isFile() && filesInFolder[i].getName().endsWith("wav") ) 
			{
				audioFiles.add(filesInFolder[i]);
			}
		}

		Random r = new Random();

		return audioFiles.get( r.nextInt( audioFiles.size() ) ).getAbsolutePath();
	}

	public static void println(Object o) {
		System.out.println(o);
	}

	public static void print(Object o) {
		System.out.print(o);
	}

}