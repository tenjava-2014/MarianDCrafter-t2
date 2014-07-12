package com.tenjava.entries.MarianDCrafter.t2.machines.itemchanger;

import com.tenjava.entries.MarianDCrafter.t2.TenJava;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

public class ItemChangerListPrompt implements Prompt {

    private ItemChangerCommandExecutor executor;

    public ItemChangerListPrompt(ItemChangerCommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        StringBuilder builder = new StringBuilder(TenJava.PREFIX + "List of all ItemChangers:");
        for (int i = 0; i < executor.getItemChangers().size(); i++)
            builder.append("#")
                    .append(i)
                    .append(": ")
                    .append(executor.getItemChangers().get(i).toString());
        builder.append(ItemChanger.CONVERSATION_PROMPT_TEXT);
        return builder.toString();
    }

    @Override
    public boolean blocksForInput(ConversationContext conversationContext) {
        return false;
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        return null;
    }
}
