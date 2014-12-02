import edu.wildlifesecurity.framework.ILogger;


public class SystemOutLogger implements ILogger {

	@Override
	public void info(String message) {
		System.out.println("INFO\t" + message);
	}

	@Override
	public void warn(String message) {
		System.out.println("WARN\t" + message);

	}

	@Override
	public void error(String message) {
		System.out.println("ERROR\t" + message);
	}

}
