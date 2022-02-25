package pt.isec.metapd.communication;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record TinyGroup(int id, String name, List<TinyUser> members) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder membersString = new StringBuilder();

        for (TinyUser member : members) {
            membersString.append(member).append("\n");
        }

        return "id: " + id + "\n" +
                "name: " + name + "\n" +
                "members: " + membersString;
    }
}
