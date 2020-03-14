package rcxdirect;

/**
 * RCX sound routines.
 */
public class Sound extends DirectSend {

	private Sound() {
	}

	/**
	 * Play a system sound.
	 * <TABLE BORDER=1>
	 * <TR><TH>aCode</TH><TH>Resulting Sound</TH></TR>
	 * <TR><TD>0</TD><TD>short beep</TD></TR>
	 * <TR><TD>1</TD><TD>double beep</TD></TR>
	 * <TR><TD>2</TD><TD>descending arpeggio</TD></TR>
	 * <TR><TD>3</TD><TD>ascending  arpeggio</TD></TR>
	 * <TR><TD>4</TD><TD>long, low beep</TD></TR>
	 * <TR><TD>5</TD><TD>quick ascending arpeggio</TD></TR>
	 * </TABLE>
	 */
	public static void systemSound(int aCode) {
		String sound_cmd = "51 0" + aCode;
		int result = sendToRCX(sound_cmd, "sound");
	}
	/**
	 * Beeps once.
	 */
	public static void beep() {
		systemSound(0);
	}

	/**
	 * Beeps twice.
	 */
	public static void twoBeeps() {
		systemSound(1);
	}

	/**
	 * Downward tones.
	 */
	public static void beepSequence() {
		systemSound(2);
	}

	/**
	 * Low buzz.
	 */
	public static void buzz() {
		systemSound(4);
	}

}
