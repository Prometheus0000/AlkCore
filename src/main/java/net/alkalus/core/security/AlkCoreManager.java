package net.alkalus.core.security;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

import net.alkalus.api.objects.misc.AcLog;
import net.alkalus.core.util.reflect.ReflectionUtils;

/**
 * A very lenient {@link SecurityManager}.
 * 
 * @author Alkalus
 *
 */
public class AlkCoreManager extends SecurityManager {

    private final String mVersion = "v0.0.1a";

    public AlkCoreManager() {

        synchronized (AlkCoreManager.class) {
            final SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                // ask the currently installed security manager if we
                // can create a new one.
                sm.checkPermission(
                        new RuntimePermission("createSecurityManager"));
                AcLog.ERROR("Security Manager already set. "
                        + sm.getClass().getCanonicalName());
            } else {
                try {
                    ReflectionUtils.setField(SecurityManager.class,
                            "initialized", true);
                    System.setSecurityManager(this);
                } catch (final Exception e) {
                    // Bad, do not set
                }
            }
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean getInCheck() {
        return super.getInCheck();
    }

    @Override
    protected Class[] getClassContext() {
        return super.getClassContext();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected ClassLoader currentClassLoader() {
        return super.currentClassLoader();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Class<?> currentLoadedClass() {
        return super.currentLoadedClass();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected int classDepth(final String name) {
        return super.classDepth(name);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected int classLoaderDepth() {
        return super.classLoaderDepth();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected boolean inClass(final String name) {
        return super.inClass(name);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected boolean inClassLoader() {
        return super.inClassLoader();
    }

    @Override
    public Object getSecurityContext() {
        return super.getSecurityContext();
    }

    @Override
    public void checkPermission(final Permission perm) {
        // super.checkPermission(perm);
    }

    @Override
    public void checkPermission(final Permission perm, final Object context) {
        // super.checkPermission(perm, context);
    }

    @Override
    public void checkCreateClassLoader() {
        // super.checkCreateClassLoader();
    }

    @Override
    public void checkAccess(final Thread t) {
        // super.checkAccess(t);
    }

    @Override
    public void checkAccess(final ThreadGroup g) {
        // super.checkAccess(g);
    }

    @Override
    public void checkExit(final int status) {
        // super.checkExit(status);
    }

    @Override
    public void checkExec(final String cmd) {
        // super.checkExec(cmd);
    }

    @Override
    public void checkLink(final String lib) {
        // super.checkLink(lib);
    }

    @Override
    public void checkRead(final FileDescriptor fd) {
        // super.checkRead(fd);
    }

    @Override
    public void checkRead(final String file) {
        // super.checkRead(file);
    }

    @Override
    public void checkRead(final String file, final Object context) {
        // super.checkRead(file, context);
    }

    @Override
    public void checkWrite(final FileDescriptor fd) {
        // super.checkWrite(fd);
    }

    @Override
    public void checkWrite(final String file) {
        // super.checkWrite(file);
    }

    @Override
    public void checkDelete(final String file) {
        // super.checkDelete(file);
    }

    @Override
    public void checkConnect(final String host, final int port) {
        // super.checkConnect(host, port);
    }

    @Override
    public void checkConnect(final String host, final int port, final Object context) {
        // super.checkConnect(host, port, context);
    }

    @Override
    public void checkListen(final int port) {
        // super.checkListen(port);
    }

    @Override
    public void checkAccept(final String host, final int port) {
        // super.checkAccept(host, port);
    }

    @Override
    public void checkMulticast(final InetAddress maddr) {
        // super.checkMulticast(maddr);
    }

    @Override
    public void checkMulticast(final InetAddress maddr, final byte ttl) {
        // super.checkMulticast(maddr, ttl);
    }

    @Override
    public void checkPropertiesAccess() {
        // super.checkPropertiesAccess();
    }

    @Override
    public void checkPropertyAccess(final String key) {
        // super.checkPropertyAccess(key);
    }

    @Override
    public boolean checkTopLevelWindow(final Object window) {
        return true;
    }

    @Override
    public void checkPrintJobAccess() {
        // super.checkPrintJobAccess();
    }

    @Override
    public void checkSystemClipboardAccess() {
        // super.checkSystemClipboardAccess();
    }

    @Override
    public void checkAwtEventQueueAccess() {
        // super.checkAwtEventQueueAccess();
    }

    @Override
    public void checkPackageAccess(final String pkg) {
        // super.checkPackageAccess(pkg);
    }

    @Override
    public void checkPackageDefinition(final String pkg) {
        // super.checkPackageDefinition(pkg);
    }

    @Override
    public void checkSetFactory() {
        // super.checkSetFactory();
    }

    @Override
    public void checkMemberAccess(final Class<?> clazz, final int which) {
        // super.checkMemberAccess(clazz, which);
    }

    @Override
    public void checkSecurityAccess(final String target) {
        // super.checkSecurityAccess(target);
    }

    @Override
    public ThreadGroup getThreadGroup() {
        return super.getThreadGroup();
    }

    /**
     * Returns the version string.
     */
    @Override
    public String toString() {
        return mVersion;
    }

}
