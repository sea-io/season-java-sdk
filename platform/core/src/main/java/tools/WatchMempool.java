package org.platform.core.src.main.java..tools;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

public class WatchMempool {
   
    public static void main(String[] args) throws InterruptedException {
        BriefLogFormatter.init();
        PeerGroup peerGroup = new PeerGroup(PARAMS);
        peerGroup.setMaxConnections(32);
        peerGroup.addPeerDiscovery(new DnsDiscovery(PARAMS));
        peerGroup.addOnTransactionBroadcastListener((peer, tx) -> {
            Result result = DefaultRiskAnalysis.FACTORY.create(null, tx, NO_DEPS).analyze();
            incrementCounter(TOTAL_KEY);
            log.info("tx {} result {}", tx.getTxId(), result);
            incrementCounter(result.name());
            if (result == Result.NON_STANDARD)
                incrementCounter(Result.NON_STANDARD + "-" + DefaultRiskAnalysis.isStandard(tx));
        });
        peerGroup.start();

        while (true) {
            Thread.sleep(STATISTICS_FREQUENCY_MS);
            printCounters();
        }
    }

}
