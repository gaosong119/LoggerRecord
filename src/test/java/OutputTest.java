import java.io.File;

/**
 * @ClassName: OutputTest
 * @Description: TODO
 * @Author: gaosong
 * @Date 2020/10/13 9:49
 */
public class OutputTest {
    public static void main(String[] args) {
        try{
            int a=5/0;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("执行finally");
        }
    }
}
