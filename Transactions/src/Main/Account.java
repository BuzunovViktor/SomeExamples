package Main;

public class Account implements Comparable {

    private volatile Long money;
    private String accNumber;
    private boolean isBlocked = false;

    public Account(long money, String accNumber) {
        this.money = money;
        this.accNumber = accNumber;
    }

    public void debit(Long amount) {
        this.money += amount;
    }

    public void credit(Long amount) {
        this.money -= amount;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Account) {
            Account acc = (Account) o;
            return this.accNumber.compareTo(acc.getAccNumber());
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Account{" +
                "money=" + money +
                ", accNumber='" + accNumber + '\'' +
                ", isBlocked=" + isBlocked +
                '}';
    }
}
