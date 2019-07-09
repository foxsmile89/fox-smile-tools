package com.smile.tools.keyword;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FinalClass {

    private static final Logger logger = LoggerFactory.getLogger(FinalClass.class);

    final int AGE = 10;

    public void sayAge(){
        logger.info("AGE:{}", AGE);
    }

}
