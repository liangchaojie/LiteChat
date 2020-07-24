package com.rightpoint.lite_chat.IM;

import java.util.List;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/7/24 10:27 AM 
 */
public interface IResolveConversation {
    /**
     * 获取本地会话列表
     * @return 本地会话列表，每一项都是会话的最新一条消息
     */
    abstract List<Msg> getConversation();
}
