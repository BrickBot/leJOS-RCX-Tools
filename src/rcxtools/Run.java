package rcxtools;

/**
 * @author Tim Rinkens
 *
 * Starts the Application.
 * 
 */
public class Run {

	public static void main(java.lang.String[] args) {

		if (args.length > 0) {
			if (args[0].toUpperCase().indexOf("DOWNLOAD") > -1)
				new RCXDownload().setVisible(true); //.show();
			else if (args[0].toUpperCase().indexOf("DIRECTMODE") > -1)
				new RCXDirectMode().setVisible(true); //.show();
			else
				System.out.println(
					"Usage: java -jar rcxtools.jar [download|directmode]");
		} else {
			System.out.println(
				"Usage: java -jar rcxtools.jar [download|directmode]");
		}

	}
}
