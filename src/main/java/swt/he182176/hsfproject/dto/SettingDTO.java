package swt.he182176.hsfproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class SettingDTO {

    private Integer id;

    @NotBlank(message = "Name is required")
    @Size(max = 20, message = "Name must be at most 20 characters")
    @Pattern(regexp = "^(?!.*\\d).+$", message = "Name must not contain digits")
    private String name;

    @NotNull(message = "Type is required")
    private Integer typeId;

    @Size(max = 100, message = "Value must be at most 100 characters")
    private String value;

    @NotNull(message = "Priority is required")
    @Positive(message = "Priority must be positive")
    private Integer priority;

    @NotNull(message = "Status is required")
    private Boolean status;

    @Size(max = 200, message = "Description must be at most 200 characters")
    private String description;

    public SettingDTO() {
        this.status = true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}