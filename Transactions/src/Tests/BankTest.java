package Tests;

import org.junit.Assert;
import org.junit.Test;
import Main.Account;
import Main.Bank;
import org.junit.rules.Timeout;

import java.util.concurrent.atomic.AtomicInteger;

public class BankTest {

    private static volatile AtomicInteger counter = new AtomicInteger(0);

    @Test
    public void transferTest() {
        Bank bank = new Bank();

        Account viktorAccount = new Account(250000,"0123456789");
        Account vasyaAccount = new Account(250000,"1234567890");
        Account mishaAccount = new Account(250000,"2345678901");
        Account alenaAccount = new Account(250000,"3456789012");
        Account annaAccount = new Account(250000,"4567890123");
        bank.addAccount(viktorAccount);
        bank.addAccount(vasyaAccount);
        bank.addAccount(mishaAccount);
        bank.addAccount(alenaAccount);
        bank.addAccount(annaAccount);
        new Thread(()-> {
            counter.set(counter.incrementAndGet());
            for (int i = 0; i < 1000; i++) {
                try {
                    bank.transfer(viktorAccount.getAccNumber(), vasyaAccount.getAccNumber(), 10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            counter.set(counter.decrementAndGet());
        }).start();
        //viktor 240000
        //vasya 260000
        new Thread(()-> {
            counter.set(counter.incrementAndGet());
            for (int i = 0; i < 2; i++) {
                try {
                    bank.transfer(viktorAccount.getAccNumber(), alenaAccount.getAccNumber(), 60000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            counter.set(counter.decrementAndGet());
        }).start();
        //viktor 120000
        //alena 370000
        new Thread(()-> {
            counter.set(counter.incrementAndGet());
            for (int i = 0; i < 2; i++) {
                try {
                    bank.transfer(vasyaAccount.getAccNumber(), mishaAccount.getAccNumber(), 100000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            counter.set(counter.decrementAndGet());
        }).start();
        //vasya 60000
        //misha 450000
        new Thread(()-> {
            counter.set(counter.incrementAndGet());
            for (int i = 0; i < 8000; i++) {
                try {
                    bank.transfer(mishaAccount.getAccNumber(), annaAccount.getAccNumber(), 40);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            counter.set(counter.decrementAndGet());
        }).start();
        //misha 130000
        //anna 570000
        new Thread(()-> {
            counter.set(counter.incrementAndGet());
            for (int i = 0; i < 100; i++) {
                try {
                    bank.transfer(annaAccount.getAccNumber(), viktorAccount.getAccNumber(), 5500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            counter.set(counter.decrementAndGet());
        }).start();
        //viktor 670000
        //anna 20000
        new Thread(()-> {
            counter.set(counter.incrementAndGet());
            for (int i = 0; i < 1000; i++) {
                try {
                    bank.transfer(viktorAccount.getAccNumber(), vasyaAccount.getAccNumber(), 200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            counter.set(counter.decrementAndGet());
        }).start();
        //viktor 470000
        //vasya 260000
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (counter.get() != 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Assert.assertEquals(470000,viktorAccount.getMoney().longValue());
        Assert.assertEquals(20000,annaAccount.getMoney().longValue());
        Assert.assertEquals(260000,vasyaAccount.getMoney().longValue());
        Assert.assertEquals(130000,mishaAccount.getMoney().longValue());
        Assert.assertEquals(370000,alenaAccount.getMoney().longValue());
        Assert.assertEquals(1250000, viktorAccount.getMoney() + annaAccount.getMoney()
                + vasyaAccount.getMoney() + mishaAccount.getMoney() + alenaAccount.getMoney());
    }

}
