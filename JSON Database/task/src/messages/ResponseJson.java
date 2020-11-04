package messages;

public class ResponseJson {

    private String response;
    private String value;
    private String reason;

    public ResponseJson(String response, String value, String reason) {
        this.response = response;
        this.value = value;
        this.reason = reason;
    }

}
