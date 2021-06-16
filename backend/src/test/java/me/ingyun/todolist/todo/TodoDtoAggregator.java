package me.ingyun.todolist.todo;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

import java.time.LocalDate;

public class TodoDtoAggregator implements ArgumentsAggregator {
    @Override
    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
        return TodoDto.builder()
                .owner(accessor.getString(0))
                .title(accessor.getString(1))
                .cycle(accessor.get(2, CycleType.class))
                .startDate(accessor.get(3, LocalDate.class))
                .endDate(accessor.get(4, LocalDate.class))
                .build();
    }
}
