package serverMessages;

import obj.Room;

import java.io.Serializable;

public class NewFieldSubmission implements Serializable {
    private String name;
    private String field;

    public NewFieldSubmission(String name, String field) {
        this.name = name;
        this.field = field;
    }


    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }
}
