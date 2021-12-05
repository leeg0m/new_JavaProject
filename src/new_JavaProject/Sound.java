package new_JavaProject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
    List<String> soundsFilePath;//사운드파일 경로 list
    public enum soundsEnum {//사운드파일 매칭 //사운드 변경시 순서에 맞게 추가해줘야함.
        CLICK(0), FLAG(1), GAMEOVER(2), GAMEWIN(3), x(4), xx(5), UNFLAG(6), WRONG(7), BOOM(8);

        soundsEnum(int i) {
            // TODO Auto-generated constructor stub
        }
    }
    SoundPlayer sp;

    void setSounds() {//파일 경로 세팅
        String soundsPath = "./sounds/";
        File rw = new File(soundsPath);
        File[] fileList = rw.listFiles();
        for (File file : fileList) {
            if (file.isFile()) {
//	             String fileName = file.getName();
//	             System.out.println("fileName : " + fileName);
                soundsFilePath.add(soundsPath + file.getName());
            }
        }
    }
//    soundsFilePath 순서(임시)
//    fileName : sounds_click.wav
//    fileName : sounds_flag.wav
//    fileName : sounds_gameOver.wav
//    fileName : sounds_gameWin.wav
//    fileName : sounds_mainmenu.ogg
//    fileName : sounds_music.ogg
//    fileName : sounds_unflag.wav
//    fileName : sounds_wrong.wav
//	  fileName : 8_sounds_boom.mp3

    Sound(){
        soundsFilePath = new ArrayList<>();
        setSounds();
    }
    void setPath(String path) {
        sp = new SoundPlayer(path);
    }

    public class SoundPlayer implements Runnable {

        // define storage for start position
        Long nowFrame;
        Clip clip;

        // get the clip status
        String thestatus;

        AudioInputStream audioStream;
        String thePath;

        public void run() {
            try {
                clip.open(audioStream);

            } catch (LineUnavailableException | IOException e) {
                // TODO Auto-generated catch block
            }

            while(clip.isOpen()) {
                try {
//					wait(1000);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                }
            }
            clip.close();
        }

        // initialize both the clip and streams
//		public SoundPlayer(String thePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        public SoundPlayer(String thePath){
            // the input stream object
            try {
                audioStream = AudioSystem.getAudioInputStream(new File(thePath).getAbsoluteFile());
            } catch (UnsupportedAudioFileException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }

            // the reference to the clip
            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                // TODO Auto-generated catch block
            }

//			try {
//				clip.open(audioStream);
//			} catch (LineUnavailableException | IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

//			clip.loop(Clip.LOOP_CONTINUOUSLY);

            this.thePath = thePath;
        }

        // operation is now as per the user's choice

        private void gotoChoice(int a) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
            switch (a) {
                case 1:
                    pause();
                    break;
                case 2:
                    resumeAudio();
                    break;
                case 3:
                    restart();
                    break;
                case 4:
                    stop();
                    break;
                case 5:
//						System.out.println("Selected time (" + 0 +
//						", " + clip.getMicrosecondLength() + ")");
//						Scanner scan = new Scanner(System.in);
//						long cc = scan.nextLong();
                    long cc = 0;
                    jump(cc);
                    break;

            }

        }

        // play
        public void play() {
            // start the clip
            clip.start();

            thestatus = "play";
        }

        // Pause audio
        public void pause() {
            if (thestatus.equals("paused")) {
                System.out.println("audio is already paused");
                return;
            }
            this.nowFrame = this.clip.getMicrosecondPosition();
            clip.stop();
            thestatus = "paused";
        }

        // resume audio
        public void resumeAudio() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
            if (thestatus.equals("play")) {
                System.out.println("The audio is" + "being played");
                return;
            }
            clip.close();
            resetAudioStream();
            clip.setMicrosecondPosition(nowFrame);
            this.play();
        }

        // restart audio
        public void restart() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
            clip.stop();
            clip.close();
            resetAudioStream();
            nowFrame = 0L;
            clip.setMicrosecondPosition(0);
            this.play();
        }

        // stop audio
        public void stop() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
            nowFrame = 0L;
            clip.stop();
            clip.close();
        }

        // jump to a selected point
        public void jump(long a) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
            if (a > 0 && a < clip.getMicrosecondLength()) {
                clip.stop();
                clip.close();
                resetAudioStream();
                nowFrame = a;
                clip.setMicrosecondPosition(a);
                this.play();
            }
        }

        // reset the audio stream
        public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
            audioStream = AudioSystem.getAudioInputStream(new File(thePath).getAbsoluteFile());
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }

    }

    private class PlayList {
        void sound(String fileName) {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(fileName));
                Clip clip = AudioSystem.getClip();
                clip.stop();
                clip.open(ais);
                clip.start();
//				while (clip.isOpen()) {
//					try {
//						Thread.sleep(99);
//
//					} catch (Exception e) {
//
//						break;
//
//					} finally {
//
//						if (clip != null) {
//
//							try {
//								clip.close();
//							} catch (Exception e) {
//							}
//
//						}
//
//					}
//
//				}
//
//				clip.close();
            } catch (Exception ex) {
            }
        }
    }

//		private static void getDurationWithMp3Spi(File file) throws UnsupportedAudioFileException, IOException {
    //
//		    AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
//		    if (fileFormat instanceof TAudioFileFormat) {
//		        Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
//		        String key = "duration";
//		        Long microseconds = (Long) properties.get(key);
//		        int mili = (int) (microseconds / 1000);
//		        int sec = (mili / 1000) % 60;
//		    } else {
//		        throw new UnsupportedAudioFileException();
//		    }
//		}

}
