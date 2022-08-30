public class CollisionException extends Exception {
    private String _code;

    public CollisionException(String code, String message) {
        super(message);
        this.setCode(code);
    }

    public CollisionException(String code, String message, Throwable cause) {
        super(message, cause);
        this.setCode(code);
    }

    public String getCode() {
        return this._code;
    }
    public void setCode(String code) {
        this._code = code;
    }
}
