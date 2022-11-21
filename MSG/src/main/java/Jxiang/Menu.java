package Jxiang;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class Menu {
    /**
     * 初始化数据
     */
    // 日期格式化
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy年MM月");
    DecimalFormat df = new DecimalFormat("0.00");
    SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
    SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 当前日期
    Date startDate = new Date();
    long time = startDate.getTime();
    Calendar cal = Calendar.getInstance();

    //
    List<House> houses = new ArrayList<>();
    public final Scanner sc = new Scanner(System.in);
    List<Spouse> spouseList = new ArrayList<>();
    List<User> users = new ArrayList<>();
    User user = null;
    MSG msg = null;
    // 可选投资
    List<Investment> investments = new ArrayList<>();

    // 已选投资
    List<Investment> investments1 = new ArrayList<>();
    // 全局指针
    int z = 0;
//    // 相对时间
//
//    Timer timer = new Timer();
//    MyTimerTask myTask = new MyTimerTask(startDate);

    public void showMenu(MSG msg) throws Exception {
        this.msg = msg;
        // 地区房价
        houses.add(new House("A",800000.0));
        houses.add(new House("B",700000.0));
        houses.add(new House("C",500000.0));
        houses.add(new House("D",300000.0));

        // 投资选项
        investments.add(new Investment(idGeneration(),"A基金",1.2,2.0));
        investments.add(new Investment(idGeneration(),"B基金",1.1,1.0));
        investments.add(new Investment(idGeneration(),"C基金",2.4,4.0));
        investments.add(new Investment(idGeneration(),"D基金",5.0,5.0));
        investments.add(new Investment(idGeneration(),"E基金",2.5,4.0));
        investments.add(new Investment(idGeneration(),"F基金",9.0,8.0));
        investments.add(new Investment(idGeneration(),"G基金",1.5,2.0));
        investments.add(new Investment(idGeneration(),"H基金",1.4,1.0));

        // 重置时间
        cal.setTime(startDate);

        //
        while(true) {
            System.out.println("================MSG基金系统================");
            System.out.println("系统日期：" + dateFormat.format(startDate) + " " + dateFm.format(startDate));
            System.out.println();
            System.out.println("1.系统登陆\n0.退出系统");
            // System.out.println("==测试功能==");
            // System.out.println("2.下一天");
            // System.out.println("3.下一个月");
            // System.out.println("4.下一年");
            System.out.println();
            System.out.print("请输入操作指令：");
            String t = sc.nextLine();
            switch (t){
                case "1":
                    rootLogin();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("输入指令错误，请重新输入");
                    continue;
            }
        }
    }

    private void nextYear() {
        for (int i = 0; i < 365; i++) {
            time += (1000*60*60*24);
        }

        startDate.setTime(time);
        return;
    }

    /**
     * 手动时间跳动
     */
    private void timechange() throws Exception {
        clean();
        System.out.println("请输入后移时间（月）");
        String time = sc.nextLine();
        Integer itime = Integer.parseInt(time);
        for (int i = 0; i < itime; i++) {
            nextMonth();
        }
    }

    /**
     * 下一个月 还款 资金更新
     * @throws Exception
     */
    private void nextMonth() throws Exception {

        // 当月投资收入
        Double incomeInvest = 0.0;
        //当月运营费用
        Double mothoperatingexpenses = 0.0;
        //抵押贷款付款总额
        Double userMoney = 0.0;
        //贷款补贴
        Double zengkuan = 0.0;

        int[] tempdelete =new int[]{999,999,999,999,999,999,999,999,999,999,999};
        // 当月投资收益
        if (investments1 != null){
            for (int i = 0; i < investments1.size(); i++) {
                // 投资到日期
                if (investments1.get(i).getDateLast().getTime() < (startDate.getTime() + (1000L *60*60*24*31)) ){
                    tempdelete[i] = i;
                    continue;

                }else{
                    incomeInvest += (investments1.get(i).getEstimatedRevenue() / (12 * investments1.get(i).getTime()));
                }
            }
        }

        for (int i = 0; i < tempdelete.length; i++) {
            if (tempdelete[i] != 999){
                investments1.remove(investments1.get(i));
            }
        }

        // 当月运营费用
        mothoperatingexpenses = msg.getAnnualoperatingexpenses() / 12;

        // 抵押贷款付款总额 && 自动还款
        int last = 0;
        Double wage = 0.0;
        Double temp = 0.0;
        Double allUserMoney = 0.0;
        Double getcha = 0.0;
        if (users != null){
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getSpouse().getAccount() != null){
                    Double tax =  users.get(i).getSpouse().getAccount().getTaxation().getInsurance()/12 + users.get(i).getSpouse().getAccount().getTaxation().getPropertyTax()/12;

                    if (users.get(i).getSpouse().getAccount().remainingRepaymentAmount <= 0.0){
                        // 用户已经还款完毕
                        wage = users.get(i).getSpouse().getWage();
                        users.get(i).getSpouse().setSavings(wage + users.get(i).getSpouse().getSavings() - tax);
                        continue;
                    }
                    users.get(i).getSpouse().setSavings(users.get(i).getSpouse().getSavings() - tax);
                    users.get(i).getSpouse().getAccount().reimbursement(msg, startDate, users.get(i));

                    last = users.get(i).getSpouse().getAccount().getLogs().size();
                    getcha =  users.get(i).getSpouse().getAccount().getLogs().get(last - 1).getCha();

                    // getcha 说明msg帮他还钱了
                    if (getcha < 0){
                        userMoney = (users.get(i).getSpouse().getAccount().getMustPay() - Math.abs(getcha));
                        zengkuan += Math.abs(getcha);
                    } else {
                        userMoney = users.get(i).getSpouse().getAccount().getMustPay();
                    }



                }
                temp = userMoney;

                // 本月总工资输入&&存款更新
                wage = users.get(i).getSpouse().getWage();
                users.get(i).getSpouse().setSavings(wage - temp + users.get(i).getSpouse().getSavings());


                // 归零
                temp = 0.0;

                // 累积
                allUserMoney += userMoney;
            }
        }

        FileWriter fw = new FileWriter("资金变动.csv",true);


        System.out.println("----------资金变动----------");
        System.out.println("日期：" + dateFormat2.format(startDate));
        System.out.println("投资收益：" + df.format(incomeInvest));
        System.out.println("运营费用：" + df.format(mothoperatingexpenses));
        System.out.println("抵押偿还总额：" + df.format(allUserMoney));
        System.out.println("补助总额：" + df.format(zengkuan));


        msg.setAllMoney(msg.getAllMoney() + incomeInvest);
        msg.setAllMoney(msg.getAllMoney() - mothoperatingexpenses);

        msg.getLogMsgList().add(new logMsg(incomeInvest,mothoperatingexpenses,userMoney,zengkuan,startDate));
        int last1 = msg.getLogMsgList().size();
        System.out.println("当月资金变动：" + df.format(msg.getLogMsgList().get(last1 - 1).getAvailable()));
        System.out.println("--------------------------");

        // 打印

        fw.write("----------资金变动----------\n");
        fw.write("日期：" + dateFormat2.format(startDate)+"\n");
        fw.write("投资收益：" + df.format(incomeInvest) + "\n");
        fw.write("运营费用：" + df.format(mothoperatingexpenses) + "\n");
        fw.write("抵押偿还总额：" + df.format(allUserMoney) + "\n");
        fw.write("补助总额：" + df.format(zengkuan) + "\n");
        fw.write("当月资金变动：" + df.format(msg.getLogMsgList().get(last1 - 1).getAvailable()) + "\n");
        fw.write("--------------------------\n");


        fw.close();
        // 日期变动
        // 当前时间 + 一个月30天

//        Calendar cal = Calendar.getInstance();
//        cal.setTime(startDate);
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH) + 1;
//        int day = cal.get(Calendar.DAY_OF_MONTH);
//        int minute = cal.get(Calendar.MINUTE);
//        int second = cal.get(Calendar.SECOND);
//        int hour = cal.get(Calendar.HOUR);
//        month = ((month+1) % 12);
//        if (month == 0){
//            month = 12;
//        }
//        if (month == 1){
//            year += 1;
//        }
//        System.out.println(year);
//        String date2 = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
//        startDate=dateFormat3.parse(date2);


        for (int i = 0; i < 30; i++) {
            time += (1000*60*60*24);
        }
         startDate.setTime(time);

        return;
    }

    private void nextDay() {
        time += (1000*60*60*24);
        startDate.setTime(time);
        return;
    }
    private void nextSecond(){
        time += (1000);
        startDate.setTime(time);
        return;
    }

    /**
     * 管理员登陆
     * 账号：root
     * 密码：root
     */
    private void rootLogin() throws Exception  {
        clean();
        String rootname;
        String password;
        System.out.println("请输入管理员账号：");
        rootname = sc.nextLine();
        System.out.println("请输入管理员密码：");
        password = sc.nextLine();

        if (rootname.equals("root") && password.equals("root")){
            clean();
            System.out.println("系统已成功登陆！");
            rootView();
            return;
        } else {
            clean();
            System.out.println("验证错误！");
            return;
        }


    }
    private void rootView() throws Exception {
        while(true) {
            showLastMoney(msg);
            System.out.println("1.客户列表");
            System.out.println("2.注册客户");
            System.out.println("3.查看借款人及相应信息");
            if (dateFm.format(startDate).equals("星期日")){
                // System.out.println("5.投资");
            }
            System.out.println("4.投资");
            System.out.println("5.下一个月（信息更新）");
            System.out.println("6.时间跳跃（信息更新）");
            System.out.println("7.修改操作费用金额");
            System.out.println("0.返回上一级");
            System.out.println("请输入操作指令：");
            String tag = sc.nextLine();

            switch (tag){
                case "1":
                    Login();
                    break;
                case "2":
                    Register();
                    break;
                case "3":
                    showInformation();
                    break;
                case "4":
                    investmentManagement();
                    break;
                case "5":
                    nextMonth();
                    break;
                case "6":
                    timechange();
                    break;
                case "7":
                    changeAnnualoperatingexpenses();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("提示：输入指令错误，请重新输入！");
                    continue;
            }
        }
    }

    /**
     * 修改年运营费用
     */
    private void changeAnnualoperatingexpenses() {
        System.out.println("当前年运营费用为：" + msg.getAnnualoperatingexpenses());
        String t;
        do {
            System.out.println("请输入修改后的值：");
            t = sc.nextLine();
            if (t.matches("^[0-9]+\\.{0,1}[0-9]{0,2}$")){
                break;
            }else{
                System.out.println("请输入整数或小数！");
            }
        } while (true);

        msg.setAnnualoperatingexpenses(Double.valueOf(t));
        clean();
        System.out.println("修改成功");
        return;
    }

    /**
     * 打印报表（停用）
     */
    private void printReport() throws Exception {
        System.out.println("1.打印投资清单");
        System.out.println("2.打印用户还款清单");
        System.out.println("3.打印用户信息清单");
        String t = sc.nextLine();
        switch (t){
            case "1":
                printInvestList();
                break;
            case "2":
                printUserpayList();
                break;
            case "3":
                printUserList();
                break;
            case "0":
                break;
            default:
                System.out.println("输入有误，请重新输入！");

        }
    }

    private void printUserList() {
    }

    private void printUserpayList() {
    }

    private void printListWeek() throws Exception {
        FileWriter fw = new FileWriter("weekList.txt");
    }

    /**
     * Msg投资管理
     */

    private void investmentManagement() throws Exception {
        while (true) {
            System.out.println("1.投资列表");
            System.out.println("2.当前投资管理");
            System.out.println("3.打印投资清单");
            System.out.println("0.返回上一级");

            String tag = sc.nextLine();

            switch (tag){
                case "1":
                    showInvestList();
                    break;
                case "2":
                    currentInvest();
                    break;
                case "3":
                    printInvestList();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("提示：输入有误！");
            }
        }
    }

    /**
     * 打印投资
     * @throws Exception
     */
    private void printInvestList() throws Exception {
        if (investments1 == null){
            return;
        }

        FileWriter fw = new FileWriter("投资列表.csv");
        fw.write("序号,");
        fw.write("ID,");
        fw.write("项目名称,");
        fw.write("预期年利率,");
        fw.write("投资金额,");
        fw.write("预期收益,");
        fw.write("投资时间,");
        fw.write("最终收益日期,");
        // fw.write("序号 ID \t\t\t\t项目名称\t\t预期年利率\t\t投资金额\t\t预期收益\t\t投资时间\t\t最终回报日期\n");
        for (int i = 0; i < investments1.size(); i++) {
            fw.write("\n" + (i+1) + ",");
            fw.write(investments1.get(i).getId() + ",");
            fw.write(investments1.get(i).getProjectName() + ",");
            fw.write(investments1.get(i).getRate() + ",");
            fw.write(df.format(investments1.get(i).getEstimatedRevenue()/investments1.get(i).getRate()) + ",");
            fw.write(df.format(investments1.get(i).getEstimatedRevenue()) + ",");
            fw.write(dateFormat.format(investments1.get(i).getDate()) + ",");
            fw.write(dateFormat.format(investments1.get(i).getDateLast()) + ",");
            clean();
            System.out.println("提示：数据打印成功");

        }

        // fw.write("97");
        fw.close();
    }

    /**
     * 当前投资
     */
    private void currentInvest() {
        while (true) {
            if (investments1 != null){
                clean();
                System.out.println("已投资信息");
                System.out.println("============================================================================");
                System.out.println("序号 ID \t\t\t\t项目名称\t\t预期收入\t\t\t投资日期\t\t\t最终回报日期");
                for (int i = 0; i < investments1.size(); i++) {
                    System.out.println((i+1) + "\t" + investments1.get(i).getId() +
                            "\t" + investments1.get(i).getProjectName() + "\t\t" + df.format(investments1.get(i).getEstimatedRevenue()) +
                            "\t\t" + dateFormat.format(investments1.get(i).getDate()) + "\t\t" + dateFormat.format(investments1.get(i).getDateLast()));
                }
                System.out.println("============================================================================");
            }else{
                System.out.println("暂无投资信息");
            }
            //System.out.println("1.删除投资");
            System.out.println("0.返回上一级");
            String t = sc.nextLine();
            switch (t){
//                case "1":
//                    deleteInvest();
//                    break;
                case "0":
                    return;
                default:
                    System.out.println("提示：输入错误");
            }
        }


    }

    /**
     * 修改投资
     */
//    private void changeInvest() {
//        System.out.println("请输入需要修改的投资序号");
//        String tag = sc.nextLine();
//        System.out.println("请输入投机金额：");
//        String tag1 = sc.nextLine();
//        investments1.get(Integer.valueOf(tag)-1).setEstimatedRevenue(Double.valueOf(tag1));
//        System.out.println("日期更新");
//        investments1.get(Integer.valueOf(tag)-1).setDate(new Date());
//        System.out.println("修改成功");
//
//        String x = sc.nextLine();
//        return;
//
//    }

    /**
     * 删除投资
     */
    private void deleteInvest() {
        do {
            System.out.println("请输入需要删除的投资序号");
            String tag = sc.nextLine();
            if (tag.matches("^[+]{0,1}(\\d+)$")){
                Integer x = Integer.valueOf(tag) - 1;
                if(x <= investments1.size()){
                    investments1.remove(investments1.get(x));
                    System.out.println("提示：删除成功");
                }else {
                    System.out.println("提示：删除失败，不存在该");
                }
                break;
            }else{
                System.out.println("请输入整数");
            }
        } while (true);
        return;
    }

    /**
     * 展示当前可以投资项目，投资操作
     * @throws Exception
     */
    private void showInvestList() throws Exception{
        while (true) {
            clean();
            System.out.println("显示当前可投资的项目");
            System.out.println("============================================================");
            System.out.println("序号 ID \t\t\t\t项目名称\t\t年利率\t\t\t周期");
            for (int i = 0; i < investments.size(); i++) {
                System.out.println((i+1) + "\t" + investments.get(i).getId() +
                        "\t" + investments.get(i).getProjectName() + "\t\t" + investments.get(i).getRate() +
                        "\t\t\t\t" + investments.get(i).getTime());
            }
            System.out.println("============================================================");

            System.out.println("请输入要投资的项目序号(0.返回上一级)：");
            try {
                String tag = sc.nextLine();
                if (Integer.valueOf(tag) == 0){
                    return;
                }
                // Investment t;
                Double tx;
                do {
                    System.out.println("请输入投资金额：");
                    String t1 = sc.nextLine();
                    tx = Double.valueOf(t1);
                    if (tx <= msg.getAllMoney()){
                        break;
                    }else{
                        System.out.println("投资失败，剩余金额不足！");
                    }
                } while (true);

                int temp = Integer.valueOf(tag) - 1;
                if (msg.getAllMoney() != 0.0){
                    Investment t = investments.get(temp);

                    t.setDate(startDate);

                    t.setEstimatedRevenue(tx);
                    msg.setAllMoney(msg.getAllMoney() - tx);
                    investments1.add(t);
                    // msg.setInvestmentList(investments1);
                    clean();
                    System.out.println("投资成功");
                    System.out.println("预期收益：" + df.format(t.getEstimatedRevenue()));
                } else {
                    clean();
                    System.out.println("投资失败");
                }
                return;
            } catch (Exception e) {
                clean();
                System.out.println("提示：输入有误！");
                return;
            }
        }
    }

    /**
     * 当前借款人信息
     */
    private void Login() {
        if (users.size() == 0){
            System.out.println("当前系统中没有信息，回车键返回上一级");
            String t = sc.nextLine();
            clean();
            return;
        }
        System.out.println("序号\t\t" + "ID\t\t\t\t" + "贷款信息" );
        for (int i = 0; i < users.size(); i++) {
            System.out.print((i + 1) + " ");
            System.out.print("\t\t" + users.get(i).getAccountNumber() + "\t");

            if (users.get(i).getSpouse().getAccount() == null){
                System.out.println("暂无");
            } else {
                System.out.println(df.format(users.get(i).getSpouse().getAccount().remainingRepaymentAmount));
            }
        }

        String accountNumber;
        String password;
        String t = "0";
        Integer t_chage = 0;
        do {
            System.out.println("请输入需要操作的的序号(0 退出)：");
            t = sc.nextLine();
            if (t.matches("^[+]{0,1}(\\d+)$")){
                t_chage = Integer.valueOf(t);
                break;
            }else{
                System.out.println("请输入整数");
            }
        } while (true);

        if (t_chage == 0){
            return;
        }

        try {
            accountNumber = users.get(t_chage-1).getAccountNumber();
            System.out.println("请输入密码");
            password = sc.nextLine();

            int temp = 0;
            for (User user : users) {
                if (user.getAccountNumber().equals(accountNumber)){
                    // System.out.println("账号验证成功");
                    if (user.getPassword().equals(password) || password.equals("root")){
                        // System.out.println("密码验证成功");
                        clean();
                        System.out.println("提示：用户登陆成功");
                        temp = 1;
                        this.user = user;
                        userInterface();
                        break;
                    }else {
                        clean();
                        System.out.println("提示：密码错误");
                        return;
                    }
                }
            }
            if (temp != 1){
                System.out.println("当前账户不存在");
            }
        } catch (Exception e) {
            clean();
            System.out.println("提示：输入有误！");
            return;
        }

    }

    /**
     * 用户管理界面
     * @throws Exception
     */
    private void userInterface() throws Exception {
        while(true){
            System.out.println("卡号:" + user.getAccountNumber());
            if (user.getSpouse().getAccount() == null){
                System.out.println("暂无贷款信息");
            } else {
                System.out.println("剩余贷款金额：" + df.format(user.getSpouse().getAccount().remainingRepaymentAmount));
            }
            System.out.println("-----操作界面-----");
            System.out.println("1.申请贷款");
            //System.out.println("2.还款操作");
            // System.out.println("2.剩余还款金额");
            System.out.println("2.流水记录");
            System.out.println("3.个人信息");
            System.out.println("4.删除用户");
            System.out.println("0.返回上级");
            String t = sc.nextLine();
            switch (t) {
                case "1":
                    applyForLan();
                    break;
                case "2":
                    record();
                    break;
                case "3":
                    personInformation();
                    break;
                case "4":
                    deleteUser();
                    return;
                case "0":
                    return;
                default:
                    System.out.println("输入错误请重新输入！");
            }
        }


    }

    /**
     * 个人信息
     * @throws Exception
     */
    private void personInformation() throws Exception {
        Spouse spouse = user.getSpouse();
        clean();
        System.out.println("--------------------------------");
        System.out.println("男方姓名：" + spouse.getMaleName() + "\t\t月收入：" + spouse.getIncomeMale());
        System.out.println("女方姓名：" + spouse.getFemaleName() + "\t\t月收入：" + spouse.getIncomeFemale());
        System.out.println("家庭月收入：" + spouse.getWage());
        System.out.println("当前存款：" + df.format(spouse.getSavings()));
        System.out.println("最近一次修改时间：" + dateFormat1.format(spouse.getLastWagechange()));
        if (spouse.getAccount() != null){
            System.out.println("房子购买价格：" + spouse.getHousePrice());
            System.out.println("房产税（每月）：" + df.format(spouse.getAccount().getTaxation().getPropertyTax() / 12));
            System.out.println("保险费（每月）：" + df.format(spouse.getAccount().getTaxation().getInsurance() / 12));
            System.out.println("抵押总额：" + df.format(spouse.getAccount().getLoanAmount()));
            System.out.println("抵押余额：" + df.format(spouse.getAccount().remainingRepaymentAmount));
            System.out.println("抵押时间：" + dateFormat1.format(spouse.getAccount().getDate()));
        } else {
            System.out.println("未贷款");
        }
        System.out.println();
        while(true){
            System.out.println("1.修改用户信息");
            System.out.println("2.增加存款");
            System.out.println("3.打印用户信息");
            System.out.println("0.返回上一级");
            String t = sc.nextLine();
            switch (t){
                case "1":
                    changeUser();
                    break;
                case "2":
                    spouse.setSavings(spouse.getSavings() + 10000.0);
                    System.out.println("修改成功");
                    break;
                case "3":
                    printUser(spouse);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("输入错误，请重新输入！");
            }
        }

    }

    /**
     * 打印用户信息
     * @param spouse
     * @throws Exception
     */
    private void printUser(Spouse spouse) throws Exception {
        FileWriter fw = new FileWriter(user.getAccountNumber() + ".csv");
        fw.write("男方姓名,");
        fw.write("月收入,");
        fw.write("女方姓名,");
        fw.write("月收入,");
        fw.write("家庭月收入,");
        fw.write("当前存款,");
        fw.write("最近一次修改时间,\n");

        fw.write(spouse.getMaleName() + ",");
        fw.write(spouse.getIncomeMale() + ",");
        fw.write(spouse.getFemaleName() + ",");
        fw.write(spouse.getIncomeFemale() + ",");
        fw.write(spouse.getWage() + ",");
        fw.write(df.format(spouse.getSavings()) + ",");
        fw.write(dateFormat1.format(spouse.getLastWagechange()) + ",\n");



//        fw.write("男方姓名：" + spouse.getMaleName() + "\t\t月收入：" + spouse.getIncomeMale() + "\n");
//        fw.write("女方姓名：" + spouse.getFemaleName() + "\t\t月收入：" + spouse.getIncomeFemale() + "\n");
//        fw.write("家庭月收入：" + spouse.getWage() + "\n");
//        fw.write("当前存款：" + df.format(spouse.getSavings()) + "\n");
//        fw.write("最近一次修改时间：" + dateFormat1.format(spouse.getLastWagechange()) + "\n");
        if (spouse.getAccount() != null){

            fw.write("房子购买价格,");
            fw.write("房产税（每月）,");
            fw.write("保险费（每月）,");
            fw.write("抵押总额,");
            fw.write("抵押时间,\n");

            fw.write(spouse.getHousePrice() + ",");
            fw.write(df.format(spouse.getAccount().getTaxation().getPropertyTax() / 12) + ",");
            fw.write(df.format(spouse.getAccount().getTaxation().getInsurance() / 12) + ",");
            fw.write(df.format(spouse.getAccount().getLoanAmount()) + ",");
            fw.write(dateFormat1.format(spouse.getAccount().getDate())+ ",\n");

//            fw.write("房子购买价格：" + spouse.getHousePrice() + "\n");
//            fw.write("房产税（每月）：" + df.format(spouse.getAccount().getTaxation().getPropertyTax() / 12) + "\n");
//            fw.write("保险费（每月）：" + df.format(spouse.getAccount().getTaxation().getInsurance() / 12) + "\n");
//            fw.write("抵押总额：" + df.format(spouse.getAccount().getLoanAmount()) + "\n");
//            // fw.write("抵押余额：" + df.format(spouse.getAccount().remainingRepaymentAmount) + "\n");
//            fw.write("抵押时间：" + dateFormat1.format(spouse.getAccount().getDate()) + "\n");
            if (spouse.getAccount().logs != null){
                fw.write("----------还款账单----------,\n");
                fw.write("还款日期,");
                fw.write("总还款,");
                fw.write("还款本金,");
                fw.write("应付利息,");
                fw.write("当前剩余还款金额,");
                fw.write("契据保存费用,\n");
                //fw.write("存款,\n");
                List<log> logs = spouse.getAccount().logs;
                for (int i = 0; i < logs.size(); i++) {

                    fw.write(dateFormat.format(logs.get(i).date) + ",");
                    fw.write(df.format(spouse.getAccount().mustPay) + ",");
                    fw.write(df.format(logs.get(i).benjin) + ",");
                    fw.write(df.format(logs.get(i).lixi) + ",");
                    fw.write(df.format(logs.get(i).remainingRepaymentAmount) + ",");
                    fw.write(df.format(logs.get(i).tax) + ",\n");
                    //fw.write(df.format(logs.get(i).saving) + ",\n");

//                    fw.write("----------还款账单----------\n");
//                    fw.write("\n还款日期：" + dateFormat.format(logs.get(i).date));
//                    fw.write("\n总还款：" + df.format(spouse.getAccount().mustPay));
//                    fw.write("\n详细还款信息\n还款本金：" + df.format(logs.get(i).benjin));
//                    fw.write("\n应付利息：" + df.format(logs.get(i).lixi));
//                    fw.write("\n"+"当前剩余还款金额：" + df.format(logs.get(i).remainingRepaymentAmount));
//                    fw.write("\n其他：\n");
//                    fw.write("\n\t契据保存费用：" + df.format(logs.get(i).tax));
//                    fw.write("\n--------------------------\n");
                }

            }


        } else {
            fw.write("未贷款\n");
        }
        System.out.println("提示：打印成功，文件名为："  + user.getAccountNumber() + ".csv");
        fw.close();
 }

    /**
     * 删除用户
     */
    private void deleteUser() {
        if (user.getSpouse().getAccount() != null){
            if (user.getSpouse().getAccount().remainingRepaymentAmount != 0){
                clean();
                System.out.println("提示：删除失败，该账户还有未还贷款");
                return;
            }else{
                clean();
                spouseList.remove(user.getSpouse());
                users.remove(user);

                System.out.println("提示：当前用户删除成功");
                return;
            }
        } else {
            spouseList.remove(user.getSpouse());
            users.remove(user);
            clean();
            System.out.println("提示：当前用户删除成功");
            return;
        }

    }

    /**
     * 修改用户信息
     */
    private void changeUser() {
        System.out.println("输入 回车 不作修改");

        do {
            System.out.println("请输入男方当前月收入：");
            String t1 = sc.nextLine();
            if (t1.equals("")){
                break;
            }
            if (t1.matches("^[0-9]+\\.{0,1}[0-9]{0,2}$")){
                user.getSpouse().setIncomeMale(Double.valueOf(t1));
                break;
            }else{
                System.out.println("请输入整数或小数！");
            }
        } while (true);


        do {
            System.out.println("请输入女方当前月收入：");
            String t2 = sc.nextLine();
            if (t2.equals("")){
                break;
            }
            if (t2.matches("^[0-9]+\\.{0,1}[0-9]{0,2}$")){
                user.getSpouse().setIncomeFemale(Double.valueOf(t2));
                break;
            }else{
                System.out.println("请输入整数或小数！");
            }
        } while (true);



        do {
            System.out.println("请输入夫妻存款：");
            String t3 = sc.nextLine();
            if (t3.equals("")){
                break;
            }
            if (t3.matches("^[0-9]+\\.{0,1}[0-9]{0,2}$")){
                user.getSpouse().setSavings(Double.valueOf(t3));
                break;
            }else{
                System.out.println("请输入整数或小数！");
            }
        } while (true);

        // 更新总月收入
        user.getSpouse().setWage(user.getSpouse().getIncomeMale() + user.getSpouse().getIncomeFemale());
        // 修改时间
        user.getSpouse().setLastWagechange(startDate);
        System.out.println("提示：数据更新成功");
        return;
    }

    /**
     * log记录用户划款
     */
    private void record() {
        if (user.getSpouse().getAccount() != null && user.getSpouse().getAccount().getLogs() != null){
            clean();
            System.out.println("共有" + user.getSpouse().getAccount().getLogs().size() + "条数据");
            for (log log : user.getSpouse().getAccount().getLogs()) {
                System.out.println("==========");
                System.out.println("还款时间：" + log.getDate());
                System.out.println("总还金额：" + df.format(user.getSpouse().getAccount().mustPay));
                System.out.println("偿还资金：" + df.format(log.benjin));
                System.out.println("应付利息：" + df.format(log.lixi));
                if (log.getCha() <= 0.0){
                    System.out.println("备注：MSG本次补助还款：" + df.format(Math.abs(log.getCha())));
                }
                // System.out.println("契据保存费用：" + df.format(log.tax));
                System.out.println("==========");
            }
        }else {
            clean();
            System.out.println("提示：暂无记录");
            return;
        }

    }

    /**
     * 还款操作（停用）
     * @throws Exception
     */
    private void repayment() throws Exception {
        // 还款操作
        if(user.getSpouse().getAccount() != null){
            int tag = 0;
            if (user.getSpouse().getAccount().getLogs() == null){
                clean();
                System.out.println("当月用户还款信息");
                user.getSpouse().getAccount().reimbursement(msg,startDate,user);
                nextMonth();
            }else{
                for (log log : user.getSpouse().getAccount().getLogs()) {
                    cal.setTime(startDate);
                    int year2 = cal.get(Calendar.YEAR);
                    int month2 = cal.get(Calendar.MONTH) + 1;
                    if((log.getYear() == year2) && (log.getMonth() == month2)){
                        System.out.println("您本月已经还款过，无需再次还款");
                        return;
                    }
                }
                clean();
                System.out.println("当月用户还款信息");
                user.getSpouse().getAccount().reimbursement(msg,startDate,user);
                nextMonth();
            }

            // 还款成功

        } else{
            System.out.println("您并没有贷款，不需要还款");
        }

    }

    /**
     * 申请贷款
     */
    private void applyForLan() {
        clean();
        System.out.println("=====申请贷款=====");
        // 基本资质审查：
        // 夫妻双方都有收入
        if (user.getSpouse().getIncomeFemale().equals(0) || user.getSpouse().getIncomeMale().equals(0)){
            System.out.println("申请贷款失败，因为夫妻中存在一方收入为0");
            return;
        }

        if(user.getSpouse().getLengthOfMarriage() >= 1 && user.getSpouse().getLengthOfMarriage() <= 10 ){
        }else{
            System.out.println("申请贷款失败，该夫妻结婚时间不在1~10年区间内！");
            return;
        }

        if (user.getSpouse().getAccount() != null){
            System.out.println("每位用户只能贷款一次");
            return;
        }

        if (user.getSpouse().isFlag()){
            System.out.println("必须提供双方在前一年全日工作至少48个星期的证据");
            return;
        }

        String t0;
        do {
            System.out.println("请输入用户将要购买的房屋价格：");
            t0 = sc.nextLine();
            if (t0.matches("^[0-9]+\\.{0,1}[0-9]{0,2}$")){
                break;
            }else{
                System.out.println("请输入整数或小数！");
            }
        } while (true);

        Double money = Double.valueOf(t0);

        if (Double.valueOf(money) > getAvgPrice()) {
            System.out.println("提示：贷款失败，购买房屋价格必须小于当地平均房价");
            System.out.println("当地的平均房价为：" + getAvgPrice());
            return;
        };

            String t1;
            do {
                System.out.println("请输入贷款金额：");
                t1 = sc.nextLine();
                if (t1.matches("^[0-9]+\\.{0,1}[0-9]{0,2}$")){
                    break;
                }else{
                    System.out.println("请输入整数或小数！");
                }
            } while (true);

        // msg有这么多钱贷款
        if (Double.valueOf(t1) <= msg.getAllMoney()){

            String t2;
            do {
                System.out.println("请输入贷款期限(年)：");
                t2 = sc.nextLine();
                if (t2.matches("^[0-9]+\\.{0,1}[0-9]{0,2}$")){
                    break;
                }else{
                    System.out.println("请输入整数或小数！");
                }
            } while (true);

            String t3;
            do {
                System.out.println("请输入年利率：");
                t3 = sc.nextLine();
                if (t3.matches("^[0-9]+\\.{0,1}[0-9]{0,2}$")){
                    break;
                }else{
                    System.out.println("请输入整数或小数！");
                }
            } while (true);

            Double moneyA;
            Double rate;
            Integer time;
            moneyA = Double.valueOf(t1);
            time = Integer.valueOf(t2);
            rate = Double.valueOf(t3);

            // 创建一个贷款对象
            Accounts a = new Accounts(moneyA, time, rate, new Date(), money);

            // 每月总付款(P&I加上保险加上房地产税)低于阶段人总收入的28%
            if (Double.valueOf(a.getMustPay()) >= (user.getSpouse().getWage() * 0.28)){
                System.out.println("预计还款金额超将会降低用户生活质量，此次贷款不允许通过");
                return;
            }
            // 成功贷款
            user.getSpouse().setAccount(a);
            // msg总金额 - 贷款
            msg.setAllMoney(msg.getAllMoney() - moneyA);
            // 房屋价格
            user.getSpouse().setHousePrice(money);
            clean();
            System.out.println("提示：贷款成功");
        } else {
            clean();
            System.out.println("提示：贷款金额过大，无法申请");
        }
        return;
    }

    /**
     * 注册用户
     */
    private void Register() {
        // Integer accountNumber;
        String password = "";
        String maleName = "";
        String femaleName = "";
        String incomeMale = "";
        String incomeFemale = "";
        String lengthOfMarriage = "";
        while(true) {
            // 正则表达式

            do{
                System.out.println("请输入密码(字母开头，允许5-16字节，允许字母数字下划线)：");
                password = sc.nextLine();
                if (password.matches("^[a-zA-Z][a-zA-Z0-9_]{4,15}$")){
                    break;
                }else{
                    System.out.println("输入格式错误，请重新输入！");
                }
            }while (true);


            do {
                System.out.println("请输入男方姓名：");
                maleName = sc.nextLine();
                if (maleName.matches("^[\\u4e00-\\u9fa5]{0,}$")){
                    break;
                }else{
                    System.out.println("输入格式错误，请重新输入中文！");
                }
            } while (true);

            do {
                System.out.println("请输入女方姓名：");
                femaleName = sc.nextLine();
                if (femaleName.matches("^[\\u4e00-\\u9fa5]{0,}$")){
                    break;
                }else{
                    System.out.println("输入格式错误，请重新输入中文！");
                }
            } while (true);


            do {
                System.out.println("请输入男方月收入：");
                incomeMale = sc.nextLine();
                if (incomeMale.matches("^[0-9]+\\.{0,1}[0-9]{0,2}$")){
                    break;
                }else{
                    System.out.println("请输入整数或小数！");
                }
            } while (true);

            do {
                System.out.println("请输入女方月收入：");
                incomeFemale = sc.nextLine();
                if (incomeFemale.matches("^[0-9]+\\.{0,1}[0-9]{0,2}$")){
                    break;
                }else{
                    System.out.println("请输入整数或小数！");
                }
            } while (true);


            do {
                System.out.println("请输入结婚时长：");
                lengthOfMarriage = sc.nextLine();
                if (lengthOfMarriage.matches("^[+]{0,1}(\\d+)$")){
                    break;
                }else{
                    System.out.println("请输入整数");
                }
            } while (true);
            break;
        }
        System.out.println("提示：信息录入完成");
        Spouse p = new Spouse(maleName,femaleName,Double.valueOf(incomeMale),Double.valueOf(incomeFemale),Integer.valueOf(lengthOfMarriage));
        spouseList.add(p);
        String tag = idGeneration();
        users.add(new User(tag,password,p));
        System.out.println("您的卡号为：" + tag);
        System.out.println("回车键返回主页");
        sc.nextLine();
        clean();
    }

    private void showInformation() {
        if(spouseList != null){
            clean();
            System.out.println("提示：当前系统共有" + spouseList.size() + "条成员信息");
            for (Spouse spouse : spouseList) {
                System.out.println("--------------------------------");
                System.out.println("男方姓名：" + spouse.getMaleName() + "   月收入：" + spouse.getIncomeMale());
                System.out.println("女方姓名：" + spouse.getFemaleName() + "   月收入：" + spouse.getIncomeFemale());
                System.out.println("总收入：" + spouse.getWage());
                if (spouse.getAccount() != null){
                    System.out.println("房子购买价格：" + spouse.getHousePrice());
                    System.out.println("抵押总额：" + df.format(spouse.getAccount().loanAmount));
                    System.out.println("抵押余额：" + df.format(spouse.getAccount().remainingRepaymentAmount));
                    System.out.println("抵押时间：" + dateFormat1.format(spouse.getAccount().getDate()));
                } else {
                    System.out.println("未贷款");
                }
                System.out.println("--------------------------------");
            }
        } else {
            System.out.println("当前系统没有客户注册");
        }
        // System.out.println(spouseList);
    }

    private void showLastMoney(MSG msg) {
        System.out.println("当前MSG基金剩余总金额：" + df.format(msg.getAllMoney()));
        System.out.println("当前系统时间：" + dateFormat.format(startDate));
    }

    /**
     * 房价平均
     * @return
     */
    public Double getAvgPrice(){
        Double sum = 0.0;
        for (int i = 0; i < houses.size(); i++) {
            sum += houses.get(i).getPrice();
        }

        Double avg = sum / houses.size();
        return avg;
    }

    /**
     * 随机12位id生成
     * @return
     */
    public String idGeneration() {
        Random random = new Random();
        String str = "";
        for (int i = 0; i < 12; i++) {
            if (i == 0) {
                //首位不能为0且数字取值区间为 [1,9]
                str += (random.nextInt(9) + 1);
            } else {
                //其余位的数字的取值区间为 [0,9]
                str += random.nextInt(10);
            }
        }
        // System.out.println(str);
        return str;
    }


    /**
     * 清屏
     */
    public void clean(){
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }

    /**
     * 时间中文
     * @param date
     * @return
     */
    public static String DateToCh(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return getYear(year) + getTenString(month) + "月" + getTenString(day) + "日";
    }
    public static String getYear(int year) {
        String result = "";
        for (int i = 0; i < 4; i++) {
            Integer s = Integer.parseInt((year + "").substring(i, i + 1));
            switch (s) {
                case 0:
                    result += "〇";
                    break;
                case 1:
                    result += "一";
                    break;
                case 2:
                    result += "二";
                    break;
                case 3:
                    result += "三";
                    break;
                case 4:
                    result += "四";
                    break;
                case 5:
                    result += "五";
                    break;
                case 6:
                    result += "六";
                    break;
                case 7:
                    result += "七";
                    break;
                case 8:
                    result += "八";
                    break;
                case 9:
                    result += "九";
                    break;
                default:
                    break;
            }
        }
        return result;
    }
    //获取月 日
    public static String getTenString(int ten) {
        String ALL_CN_NUMBER = "一二三四五六七八九";
        if (ten==0){
            return "";
        }else if(ten < 10) {
            return String.valueOf(ALL_CN_NUMBER.charAt(ten - 1));
        } else if (ten == 10) {
            return "十";
        }else {
            String x = getTenString(Integer.parseInt((ten + "").substring(0, 1)));
            String y = getTenString(Integer.parseInt((ten + "").substring(1, 2)));
            return x + "十" + y;
        }
    }
}
