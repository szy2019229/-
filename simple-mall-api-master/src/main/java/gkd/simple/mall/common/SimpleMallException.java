
package gkd.simple.mall.common;

public class SimpleMallException extends RuntimeException {

    public SimpleMallException() {
    }

    public SimpleMallException(String message) {
        super(message);
    }

    /**
     * 丢出一个异常
     *
     * @param message
     */
    public static void fail(String message) {
        throw new SimpleMallException(message);
    }

}
