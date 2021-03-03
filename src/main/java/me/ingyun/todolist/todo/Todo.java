package me.ingyun.todolist.todo;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
public class Todo {
    @Id @GeneratedValue
    private Integer id;
    private String owner;
    @Builder.Default
    private LocalDate created = LocalDate.now();

    private boolean completed;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private CycleType cycle;
    private Integer cycleDetail;

    private String title;
    private String description;
    private String largeCategory;
    private String mediumCategory;

}
