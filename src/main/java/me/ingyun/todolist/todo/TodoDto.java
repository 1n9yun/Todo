package me.ingyun.todolist.todo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor
public class TodoDto {
    private String owner;
    private boolean completed;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private CycleType cycle;
    private Integer cycleDetail;

    @NotEmpty
    private String title;
    private String description;
    private String largeCategory;
    private String mediumCategory;
}
