package MemoryUsage;

public class UsageMemory {

	public long getUsageMemory() {
	     return getTotalMemory() - getFreeUsageMemory();
	}
	
	public long getTotalMemory() {
		 Runtime runtime = Runtime.getRuntime();
	     return runtime.totalMemory() / 1024 / 1024;
	}
	
	public long getMaxUsageMemory() {
		Runtime runtime = Runtime.getRuntime();
		return runtime.maxMemory() / 1024 / 1024;
	}
	
	public long getFreeUsageMemory() {
		Runtime runtime = Runtime.getRuntime();
		return runtime.freeMemory() / 1024 / 1024;
	}
}
