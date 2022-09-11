@FunctionalInterface
public interface RequestHandler {

	Response handle(String method, String page);

}
