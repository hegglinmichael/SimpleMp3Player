import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundExecutor {
	private static ExecutorService backgroundEx = Executors.newCachedThreadPool();

	public BackgroundExecutor(){}

	public static ExecutorService get(){
		return backgroundEx;		
	}
}