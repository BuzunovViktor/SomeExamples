package Tests;

import org.junit.Assert;
import org.junit.Test;
import Main.Account;
import Main.Bank;
import org.junit.rules.Timeout;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BankTest {

    private static volatile AtomicInteger counter = new AtomicInteger(0);

    @Test
    public void transferTest() {
        Bank bank = new Bank();

        AtomicLong viktorCorrection = new AtomicLong(0);
        AtomicLong vasyaCorrection = new AtomicLong(0);
        AtomicLong mishaCorrection = new AtomicLong(0);
        AtomicLong alenaCorrection = new AtomicLong(0);
        AtomicLong annaCorrection = new AtomicLong(0);

        Long money = (long)250000;
        Account viktorAccount = new Account(money,"0123456789");
        Account vasyaAccount = new Account(money,"1234567890");
        Account mishaAccount = new Account(money,"2345678901");
        Account alenaAccount = new Account(money,"3456789012");
        Account annaAccount = new Account(money,"4567890123");

        bank.addAccount(viktorAccount);
        bank.addAccount(vasyaAccount);
        bank.addAccount(mishaAccount);
        bank.addAccount(alenaAccount);
        bank.addAccount(annaAccount);

        new Thread(()-> {
            counter.set(counter.incrementAndGet());
            for (int i = 0; i < 1000; i++) {
                Long amount = (long) 10;
                try {
                    bank.transfer(viktorAccount.getAccNumber(), vasyaAccount.getAccNumber(), amount);
                } catch (Exception e) {
                    viktorCorrection.getAndAdd(-amount);
                    vasyaCorrection.getAndAdd(amount);
                    e.printStackTrace();
                }
            }
            counter.set(counter.decrementAndGet());
        }).start();

        new Thread(()-> {
            counter.set(counter.incrementAndGet());
            Long amount = (long) 60000;
            for (int i = 0; i < 2; i++) {
                try {
                    bank.transfer(viktorAccount.getAccNumber(), alenaAccount.getAccNumber(), amount);
                } catch (Exception e) {
                    viktorCorrection.getAndAdd(-amount);
                    alenaCorrection.getAndAdd(amount);
                    e.printStackTrace();
                }
            }
            counter.set(counter.decrementAndGet());
        }).start();

        new Thread(()-> {
            counter.set(counter.incrementAndGet());
            Long amount = (long) 100000;
            for (int i = 0; i < 2; i++) {
                try {
                    bank.transfer(vasyaAccount.getAccNumber(), mishaAccount.getAccNumber(), amount);
                } catch (Exception e) {
                    vasyaCorrection.getAndAdd(-amount);
                    mishaCorrection.getAndAdd(amount);
                    e.printStackTrace();
                }
            }
            counter.set(counter.decrementAndGet());
        }).start();

        new Thread(()-> {
            counter.set(counter.incrementAndGet());
            Long amount = (long) 40;
            for (int i = 0; i < 8000; i++) {
                try {
                    bank.transfer(mishaAccount.getAccNumber(), annaAccount.getAccNumber(), amount);
                } catch (Exception e) {
                    mishaCorrection.getAndAdd(-amount);
                    annaCorrection.getAndAdd(amount);
                    e.printStackTrace();
                }
            }
            counter.set(counter.decrementAndGet());
        }).start();

        new Thread(()-> {
            counter.set(counter.incrementAndGet());
            Long amount = (long) 5500;
            for (int i = 0; i < 100; i++) {
                try {
                    bank.transfer(annaAccount.getAccNumber(), viktorAccount.getAccNumber(), 5500);
                } catch (Exception e) {
                    annaCorrection.getAndAdd(-amount);
                    viktorCorrection.getAndAdd(amount);
                    e.printStackTrace();
                }
            }
            counter.set(counter.decrementAndGet());
        }).start();

        new Thread(()-> {
            counter.set(counter.incrementAndGet());
            Long amount = (long) 200;
            for (int i = 0; i < 1000; i++) {
                try {
                    bank.transfer(viktorAccount.getAccNumber(), vasyaAccount.getAccNumber(), 200);
                } catch (Exception e) {
                    viktorCorrection.getAndAdd(-amount);
                    vasyaCorrection.getAndAdd(amount);
                    e.printStackTrace();
                }
            }
            counter.set(counter.decrementAndGet());
        }).start();

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

        Assert.assertEquals(470000 - viktorCorrection.get(),viktorAccount.getMoney().longValue());
        Assert.assertEquals(20000 - annaCorrection.get(),annaAccount.getMoney().longValue());
        Assert.assertEquals(260000 - vasyaCorrection.get(),vasyaAccount.getMoney().longValue());
        Assert.assertEquals(130000 - mishaCorrection.get(),mishaAccount.getMoney().longValue());
        Assert.assertEquals(370000 - alenaCorrection.get(),alenaAccount.getMoney().longValue());
        Assert.assertEquals(1250000, viktorAccount.getMoney() + annaAccount.getMoney()
                + vasyaAccount.getMoney() + mishaAccount.getMoney() + alenaAccount.getMoney());
    }

}
