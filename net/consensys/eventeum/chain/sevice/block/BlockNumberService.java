package net.consensys.eventeum.chain.service.block;

import java.math.BigInteger;

public interface BlockNumberService {

    BigInteger getStartBlockForNode(String nodeName);
}
