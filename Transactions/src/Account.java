public class Account implements Comparable {

    private long money;
    private String accNumber;

    public Account(long money, String accNumber) {
        this.money = money;
        this.accNumber = accNumber;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Account) {
            Account acc = (Account) o;
            return this.accNumber.compareTo(acc.getAccNumber());
        }
        return 0;
    }

}
