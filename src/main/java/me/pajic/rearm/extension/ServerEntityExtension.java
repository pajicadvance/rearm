package me.pajic.rearm.extension;

public interface ServerEntityExtension {
	boolean rearm$hasPendingChanges();
	void rearm$sendDeferredChanges();
}
