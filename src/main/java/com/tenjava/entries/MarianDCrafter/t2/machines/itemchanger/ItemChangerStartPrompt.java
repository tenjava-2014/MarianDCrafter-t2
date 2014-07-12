package com.tenjava.entries.MarianDCrafter.t2.machines.itemchanger;

import com.tenjava.entries.MarianDCrafter.t2.TenJava;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

public class ItemChangerStartPrompt implements Prompt {

    private ItemChangerCommandExecutor executor;

    public ItemChangerStartPrompt(ItemChangerCommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return TenJava.PREFIX + "ItemChanger started. What to you want to do?\n" +
                ItemChanger.CONVERSATION_PROMPT_TEXT;
    }

    @Override
    public boolean blocksForInput(ConversationContext conversationContext) {
        return false;
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String input) {
        if(input == null)
            return null;

        if(input.equals("stop"))
            return null;
        else if(input.equals("list"))
            return new ItemChangerListPrompt(executor);
        else
            return null;
    }

}
