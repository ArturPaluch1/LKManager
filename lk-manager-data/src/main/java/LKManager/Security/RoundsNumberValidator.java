package LKManager.Security;

import LKManager.model.RecordsAndDTO.AddScheduleDTO;
import LKManager.model.RecordsAndDTO.ScheduleType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoundsNumberValidator implements ConstraintValidator<RoundsNumberRequired, AddScheduleDTO> {
    @Override
    public boolean isValid(AddScheduleDTO addScheduleDTO, ConstraintValidatorContext constraintValidatorContext) {
        if(addScheduleDTO.getScheduleType()==null)
          return false;
        else
        {
            if(addScheduleDTO.getScheduleType().equals(ScheduleType.swissSchedule)&&addScheduleDTO.getRoundsNumber()==null)
                return false;
            else return true;
        }

    }
}
