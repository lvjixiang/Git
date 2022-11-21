package Jxiang;

/**
 * 启动器
 */
public class Main{
    public static void main(String[] args) throws Exception {
        MSG msg = new MSG();
        msg.setAllMoney(2000000); // 20万
        Menu menu = new Menu();
        menu.showMenu(msg);
    }
}