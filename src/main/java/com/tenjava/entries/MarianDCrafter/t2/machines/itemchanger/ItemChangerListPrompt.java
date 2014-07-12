package com.tenjava.entries.MarianDCrafter.t2.machines.itemchanger;

import com.tenjava.entries.MarianDCrafter.t2.TenJava;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

public class ItemChangerListPrompt extends ValidatingPrompt {

    private ItemChangerCommandExecutor executor;

    public ItemChangerListPrompt(ItemChangerCommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        StringBuilder builder = new StringBuilder(TenJava.PREFIX + "List of all ItemChangers:");
        ChatColor color = ChatColor.GREEN;
        for (int i = 0; i < executor.getItemChangers().size(); i++) {
            builder.append("\n")
                    .append(color)
                    .append("#")
                    .append(i)
                    .append(": ")
                    .append(executor.getItemChangers().get(i).toString());

            if (color == ChatColor.GREEN)
                color = ChatColor.DARK_GREEN;
            else
                color = ChatColor.GREEN;
        }
        builder.append("\n" + ItemChanger.CONVERSATION_PROMPT_TEXT);
        return builder.toString();
    }

    @Override
    protected boolean isInputValid(ConversationContext conversationContext, String s) {
        return true;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, String input) {
        return executor.getPrompt(input, conversationContext.getForWhom());
    }
}
