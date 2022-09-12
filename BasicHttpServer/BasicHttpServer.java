import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Lightweight single-threaded server mainly for handling logins or other browser communication.
 * Does not support encryption, so should be used with 0.0.0.0 as the host.
 */
public class BasicHttpServer implements Closeable {

	private final ServerSocket serverSocket;
	private final RequestHandler handler;

	public BasicHttpServer(String host, int port, RequestHandler handler) throws IOException {
		serverSocket = new ServerSocket(port, 50, InetAddress.getByName(host));
		this.handler = handler;
	}

	public void run() throws IOException {
		while(!serverSocket.isClosed()) {
			Socket socket;

			try {
				socket = serverSocket.accept();
			}
			catch(SocketException error) {
				if(!serverSocket.isClosed()) {
					throw error;
				}
				break;
			}

			handle: {
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String first = reader.readLine();
				String[] firsts = first.split(" ");

				Response response;

				if(firsts.length < 2) break handle;

				response = handler.handle(firsts[0], URLDecoder.decode(firsts[1], StandardCharsets.UTF_8));

				PrintStream stream = new PrintStream(socket.getOutputStream(), false, StandardCharsets.UTF_8);
				stream.println("HTTP/1.1 " + response.status);
				stream.println("Content-Type: " + response.contentType + "; charset=UTF-8");
				stream.println("Content-Length: " + response.content.length());
				stream.println();
				stream.println(response.content);
				stream.flush();
			}

			socket.close();
		}
	}

	@Override
	public void close() throws IOException {
		serverSocket.close();
	}

}
