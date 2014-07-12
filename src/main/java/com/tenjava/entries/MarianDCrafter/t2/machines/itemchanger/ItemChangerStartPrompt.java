package com.tenjava.entries.MarianDCrafter.t2.machines.itemchanger;

import com.tenjava.entries.MarianDCrafter.t2.TenJava;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

public class ItemChangerStartPrompt extends ValidatingPrompt {

    private ItemChangerCommandExecutor executor;
    private String message;

    public ItemChangerStartPrompt(ItemChangerCommandExecutor executor) {
        this.executor = executor;
        this.message = TenJava.PREFIX + "ItemChanger started. What to you want to do?\n" +
                ItemChanger.CONVERSATION_PROMPT_TEXT;
    }

    public ItemChangerStartPrompt(ItemChangerCommandExecutor executor, String error) {
        this.executor = executor;
        this.message = TenJava.PREFIX_FAIL + error + "\n" +
                ItemChanger.CONVERSATION_PROMPT_TEXT;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return message;
    }

    @Override
    protected boolean isInputValid(ConversationContext conversationContext, String input) {
        return true;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, String input) {
        return executor.getPrompt(input, conversationContext.getForWhom());
    }

}
