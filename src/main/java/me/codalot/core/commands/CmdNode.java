package me.codalot.core.commands;

import me.codalot.core.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class CmdNode {

    protected Map<String, CmdNode> subNodes;

    public List<String> getCompletionOptions(String current, int index) {
        List<String> options = new ArrayList<>();

        if (index == 0)
            options.addAll(subNodes.keySet());

        return CollectionUtils.getMatches(current, options);
    }

    public boolean canExecute(Executor executor) {
        return true;
    }

    public abstract void execute(Executor executor, String[] args);

}
