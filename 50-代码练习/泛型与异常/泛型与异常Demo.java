import java.util.*;

class BizException extends RuntimeException {
    private int code;
    public BizException(int code,String message) {
        super(message);
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}

    class Result<T>{
    private Integer code;
    private String message;
    private T data;
    public Result() {
    }
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }
    public static Result<Void> error(int code, String message) {
        return new Result<>(code, message, null);
    }
        @Override
        public String toString() {
            return "Result{code=" + code + ", message='" + message + "', data=" + data + "}";
        }

}

public class 泛型与异常Demo {
    public static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String[] args){
        Integer[] array = {1, 2, 3, 4, 5};
        swap(array, 0, 1);
        System.out.println(Arrays.toString(array));
        Result<String> r = Result.success( "课程数据");
        System.out.println(r);

        try {
            throw new BizException(4001, "课程已满");
        } catch (BizException e) {
            System.out.println("业务异常:code=" + e.getCode() + ",message=" + e.getMessage());

        }
        System.out.println("程序继续执行");

    }




}
