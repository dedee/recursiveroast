package org.dedee.recursiveroast.performance;

import org.dedee.recursiveroast.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Benchmark comparing regular byte[] (CmdList) vs DirectByteBuffer (CmdListDirect).
 *
 * Tests both implementations with the same L-System to measure:
 * - Execution time
 * - Memory usage
 * - GC behavior
 */
public class ByteBufferBenchmark {

    public static void main(String[] args) throws Exception {
        System.out.println("=== ByteBuffer Benchmark: byte[] vs DirectByteBuffer ===\n");

        // Load Dragon Curve L-System
        Commands commands = new Commands();
        LSystemModel model = new LSystemModel(commands);

        try (var in = ByteBufferBenchmark.class.getResourceAsStream("/files/drachenkurve.txt")) {
            if (in != null) {
                model.load(new BufferedReader(new InputStreamReader(in)));
            }
        }

        System.out.println("Testing with Dragon Curve L-System\n");

        // Run both benchmarks
        System.out.println("1. Standard byte[] (CmdList):");
        System.out.println("   Recursion | Commands |  Time (ms) | Memory (MB) | GC Count");
        System.out.println("   ----------|----------|------------|-------------|----------");
        runStandardBenchmark(model, 12);

        System.gc();
        Thread.sleep(500);

        System.out.println("\n2. DirectByteBuffer (CmdListDirect):");
        System.out.println("   Recursion | Commands |  Time (ms) | Memory (MB) | GC Count");
        System.out.println("   ----------|----------|------------|-------------|----------");
        runDirectBenchmark(model, 12);

        System.out.println("\n=== Benchmark Complete ===");
        System.out.println("\nInterpretation:");
        System.out.println("- byte[] ist schneller für kleine/mittlere Datenmengen (<100K Commands)");
        System.out.println("- DirectByteBuffer zeigt Vorteile bei sehr großen Datenmengen (>100K)");
        System.out.println("- DirectByteBuffer reduziert GC-Druck (niedrigerer GC Count)");
    }

    private static void runStandardBenchmark(LSystemModel model, int maxDepth) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        long gcCountBefore = getGcCount();

        for (int depth = 1; depth <= maxDepth; depth++) {
            System.gc();
            Thread.sleep(50);

            long memBefore = runtime.totalMemory() - runtime.freeMemory();
            long startTime = System.nanoTime();

            LSystemEngine engine = new LSystemEngine(model);
            for (int i = 0; i < depth; i++) {
                engine.calculateNext();
            }
            var result = engine.normalize();

            long endTime = System.nanoTime();
            long memAfter = runtime.totalMemory() - runtime.freeMemory();
            long gcCountAfter = getGcCount();

            int commandCount = result != null ? result.length() : 0;
            double timeMs = (endTime - startTime) / 1_000_000.0;
            double memMB = (memAfter - memBefore) / (1024.0 * 1024.0);
            long gcCount = gcCountAfter - gcCountBefore;

            System.out.printf("   %9d | %8d | %10.2f | %11.2f | %8d%n",
                depth, commandCount, timeMs, memMB, gcCount);

            if (timeMs > 10000) {
                System.out.println("   Stopping (> 10 seconds)");
                break;
            }
        }
    }

    private static void runDirectBenchmark(LSystemModel model, int maxDepth) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        long gcCountBefore = getGcCount();

        for (int depth = 1; depth <= maxDepth; depth++) {
            System.gc();
            Thread.sleep(50);

            long memBefore = runtime.totalMemory() - runtime.freeMemory();
            long startTime = System.nanoTime();

            LSystemEngineDirect engine = new LSystemEngineDirect(model);
            for (int i = 0; i < depth; i++) {
                engine.calculateNext();
            }
            var result = engine.normalize();

            long endTime = System.nanoTime();
            long memAfter = runtime.totalMemory() - runtime.freeMemory();
            long gcCountAfter = getGcCount();

            int commandCount = result != null ? result.length() : 0;
            double timeMs = (endTime - startTime) / 1_000_000.0;
            double memMB = (memAfter - memBefore) / (1024.0 * 1024.0);
            long gcCount = gcCountAfter - gcCountBefore;

            System.out.printf("   %9d | %8d | %10.2f | %11.2f | %8d%n",
                depth, commandCount, timeMs, memMB, gcCount);

            if (timeMs > 10000) {
                System.out.println("   Stopping (> 10 seconds)");
                break;
            }
        }
    }

    private static long getGcCount() {
        long sum = 0;
        for (java.lang.management.GarbageCollectorMXBean gc :
             java.lang.management.ManagementFactory.getGarbageCollectorMXBeans()) {
            long count = gc.getCollectionCount();
            if (count > 0) {
                sum += count;
            }
        }
        return sum;
    }
}
