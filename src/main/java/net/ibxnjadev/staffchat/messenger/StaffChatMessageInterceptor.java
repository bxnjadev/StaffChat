package net.ibxnjadev.staffchat.messenger;

import net.ibxnjadev.staffchat.StaffChatHandler;
import net.ibxnjadev.vmessenger.universal.Interceptor;

public class StaffChatMessageInterceptor implements Interceptor<StaffChatMessage> {

    private final StaffChatHandler staffChatHandler;

    public StaffChatMessageInterceptor(StaffChatHandler staffChatHandler) {
        this.staffChatHandler = staffChatHandler;
    }

    @Override
    public void subscribe(StaffChatMessage staffChatMessage) {
        staffChatHandler.executeMessage(staffChatMessage);
    }

}
