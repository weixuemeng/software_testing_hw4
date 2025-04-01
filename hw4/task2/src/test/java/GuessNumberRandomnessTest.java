import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GuessNumberRandomnessTest {
    private Map<Integer, Integer> freq = new HashMap<>();
    GuessNumber guessNumber ;
    @BeforeEach
    public void setUp(){
        guessNumber = new GuessNumber();


    }

    @AfterEach
    void restoreSystemIO() {
        System.setOut(System.out); // Restore original System.out
        System.setIn(System.in);
    }

    @Test
    public void testProbeDistribution() throws InterruptedException {
        int minCount = Integer.MAX_VALUE;
        int maxCount = Integer.MIN_VALUE;
        int threadCount = 4;
        Random random = new Random(0);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final int totalRuns = 30000;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        int runsPerThread = totalRuns / threadCount;
        Object randomLock = new Object();

        for(int t = 0; t< threadCount; t++){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    for(int i =0; i< runsPerThread; i++){
                        byte[] data = "10\n23\n30\n22\n87\n".getBytes(StandardCharsets.UTF_8);
                        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(data));


                        int generatedInt;
                        synchronized (randomLock){
                            System.setIn(in);
                            generatedInt=guessNumber.guessingNumberGame(random);

                        }

                        if(generatedInt>=1 && generatedInt<=100){
                            freq.put(generatedInt, freq.getOrDefault(generatedInt,0)+1);
                        }
                    }
                    countDownLatch.countDown();


                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();

        minCount = Collections.min(freq.values());
        maxCount = Collections.max(freq.values());
        System.out.println(minCount);
        System.out.println(maxCount);
        assertTrue (minCount> 0.5*maxCount && maxCount <= 1.5*minCount);


    }
}
