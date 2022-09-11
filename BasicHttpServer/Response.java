public class Response {

	int status;
	String contentType;
	String content;

	public Response(int status, String contentType, String content) {
		this.status = status;
		this.contentType = contentType;
		this.content = content;
	}

}
