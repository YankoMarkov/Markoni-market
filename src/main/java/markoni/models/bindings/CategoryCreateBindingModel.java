package markoni.models.bindings;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CategoryCreateBindingModel {

    private String name;

    @NotNull
    @NotEmpty(message = "missing name")
    @Length(min = 3, message = "Name must by minimum 3 symbols")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
