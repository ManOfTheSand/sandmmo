package com.sandcore.mmo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SandCoreMainTest {

    @Test
    public void testPluginInstantiation() {
        SandCoreMain plugin = new SandCoreMain();
        assertNotNull(plugin);
    }
} 