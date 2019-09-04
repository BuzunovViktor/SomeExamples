package Main;

import Main.Account;

import java.util.HashMap;
import java.util.Random;

public class Bank {
    private HashMap<String, Account> accounts;
    private final Random random = new Random();

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        //Thread.sleep(1000);
//        return random.nextBoolean();
        return false;
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws Exception {
        Account accountFrom = accounts.get(fromAccountNum);
        Account accountTo = accounts.get(toAccountNum);
        if (accountFrom == null || accountTo == null) {
            throw new Exception("Invalid account\n" + accountFrom + "\n" + accountTo);
        }
        if (amount <= 0) {
            throw new Exception("Invalid amount " + amount);
        }
        if (accountFrom.isBlocked()) {
            throw new Exception("Main.Account is blocked " + accountFrom);
        }
        if (accountTo.isBlocked()) {
            throw new Exception("Main.Account is blocked " + accountTo);
        }
        boolean isWait = false;

        int compareresult = accountFrom.compareTo(accountTo);
        if (compareresult > 0) {
            synchronized (accountTo) {
                synchronized (accountFrom) {
                    while ((getBalance(fromAccountNum) - amount) < 0) {
                        System.out.println("wait");
                        try {
                            wait();
                            isWait = true;
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        //throw new Exception("Isn't money enought. Main.Account " + fromAccountNum);
                    }
                    accountTo.debit(amount);
                    accountFrom.credit(amount);
                    if (isWait) {
                        notify();
                        isWait = false;
                    }
                    if (amount > 50000) {
                        try {
                            boolean result = isFraud(fromAccountNum, toAccountNum, amount);
                            accountFrom.setBlocked(result);
                            accountTo.setBlocked(result);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (compareresult < 0) {
            synchronized (accountFrom) {
                synchronized (accountTo) {
                    while ((getBalance(fromAccountNum) - amount) < 0) {
                        System.out.println("wait");
                        try {
                            wait();
                            isWait = true;
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        //throw new Exception("Isn't money enought. Main.Account " + fromAccountNum);
                    }
                    accountTo.debit(amount);
                    accountFrom.credit(amount);
                    if (isWait) {
                        notify();
                        isWait = false;
                    }
                    if (amount > 50000) {
                        try {
                            boolean result = isFraud(fromAccountNum, toAccountNum, amount);
                            accountFrom.setBlocked(result);
                            accountTo.setBlocked(result);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (compareresult == 0) {
            final Object o = new Object();
            synchronized (o) {
                synchronized (accountFrom) {
                    synchronized (accountTo) {
                        while ((getBalance(fromAccountNum) - amount) < 0) {
                            System.out.println("wait");
                            try {
                                wait();
                                isWait = true;
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            //throw new Exception("Isn't money enought. Main.Account " + fromAccountNum);
                        }
                        accountTo.debit(amount);
                        accountFrom.credit(amount);
                        if (isWait) {
                            notify();
                            isWait = false;
                        }
                        if (amount > 50000) {
                            try {
                                boolean result = isFraud(fromAccountNum, toAccountNum, amount);
                                accountFrom.setBlocked(result);
                                accountTo.setBlocked(result);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) throws Exception {
            Account account = accounts.get(accountNum);
            if (account == null) {
                throw new Exception("Invalid account\n" + account);
            }
            return account.getMoney();
    }

    public void addAccount(Account account) {
        if (accounts == null) {
            accounts = new HashMap<>();
        }
        if (account == null) return;
        accounts.put(account.getAccNumber(),account);
    }

}
