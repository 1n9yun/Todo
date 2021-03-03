package me.ingyun.todolist.todo;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDate;

@Component
public class TodoValidator {
    public void validate(TodoDto todoDto, Errors errors){
        if(todoDto.getCycle() != null && todoDto.getCycleDetail() == null){
            errors.rejectValue("cycleDetail", "NotEmpty", "Cycle is specified but CycleDetail is Empty");
            errors.reject("wrongCycle", "Specification of Cycle is wrong");
        }

        if(todoDto.getCycle() == null){
            LocalDate startDate = todoDto.getStartDate();
            LocalDate endDate = todoDto.getEndDate();

            if(startDate == null) errors.rejectValue("startDate", "StartDate is null");
            else if(endDate == null) errors.rejectValue("endDate", "EndDate is null");
            else if(!(startDate.isEqual(endDate) || startDate.isBefore(endDate))){
                errors.rejectValue("startDate", "InvalidDateRange", "endDate is before startDate");
                errors.rejectValue("endDate", "InvalidDateRange", "endDate is before startDate");
            }
        }

        if(todoDto.getLargeCategory() == null && todoDto.getMediumCategory() != null){
            errors.rejectValue("largeCategory", "LargeCategory is not Specified");
        }
    }
}
