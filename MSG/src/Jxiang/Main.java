package Jxiang;

/**
 * 启动器
 */
public class Main{
    public static void main(String[] args) throws Exception {
        Jxiang.MSG msg = new Jxiang.MSG();
        msg.setAllMoney(2000000); // 20万
        Jxiang.Menu menu = new Jxiang.Menu();
        menu.showMenu(msg);
    }
}