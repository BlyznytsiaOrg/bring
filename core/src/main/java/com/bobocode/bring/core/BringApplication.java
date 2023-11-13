package com.bobocode.bring.core;

import com.bobocode.bring.core.context.impl.BringApplicationContext;

public class BringApplication {

    private BringApplication() {
    }
    
    public static BringApplicationContext run(Class<?> clazz) {
        // Create context: register Bean definitions
        BringApplicationContext context = new BringApplicationContext(clazz);
        
        // Invoke Bean Post Processors, create Bean objects
        context.refresh();
        
        return context;
    }

    public static BringApplicationContext run(String basePackage) {
        // Create context: register Bean definitions
        BringApplicationContext context = new BringApplicationContext(basePackage);

        // Invoke Bean Post Processors, create Bean objects
        context.refresh();

        return context;
    }
    
}
