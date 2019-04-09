package me.codalot.core.commands;

import lombok.Getter;
import me.codalot.core.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class CmdNode {

    @Getter protected Map<String, CmdNode> subNodes;

    public CmdNode() {
        subNodes = new HashMap<>();
    }

    public List<String> getCompletionOptions(String[] args) {
        List<String> options = new ArrayList<>();

        if (args.length == 1)
            options.addAll(subNodes.keySet());

        return options;
    }

    public boolean canExecute(Executor executor) {
        return true;
    }

    public void run(Executor executor, String[] args) {
        if (!canExecute(executor)) {
            failure(executor);
            return;
        }

        if (args.length != 0) {
            CmdNode subNode = subNodes.get(args[0].toLowerCase());

            if (subNode != null) {
                subNode.run(executor, CollectionUtils.removeFirst(args));
                return;
            }
        }

        execute(executor, args);
    }

    public abstract void execute(Executor executor, String[] args);

    public abstract void failure(Executor executor);



}
