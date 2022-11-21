package Jxiang;

public class User {
    // private Long id; // 12位 id
    private String accountNumber; // 用户卡号
    private String password; // 密码
    private Spouse spouse; // 对应的用户信息

    public User() {
    }

    public User(String accountNumber, String password, Spouse spouse) {
        this.accountNumber = accountNumber;
        this.password = password;
        this.spouse = spouse;
    }

    public Spouse getSpouse() {
        return spouse;
    }

    public void setSpouse(Spouse spouse) {
        this.spouse = spouse;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
